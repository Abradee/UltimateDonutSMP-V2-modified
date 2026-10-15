package com.bx.ultimateDonutSmp2.managers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TpaPendingSelectionTest {

    @Test
    void aSecondRequestStaysBesideTheFirst() {
        UUID target = UUID.randomUUID();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        List<TPAManager.TpaRequest> pending = new ArrayList<>();

        assertTrue(TPAManager.addPending(pending, request(first, target, false)));
        assertTrue(TPAManager.addPending(pending, request(second, target, false)));
        assertFalse(TPAManager.addPending(pending, request(first, target, false)));

        assertEquals(first, TPAManager.selectPending(pending, first, false).requester());
        assertEquals(second, TPAManager.selectPending(pending, second, false).requester());
        assertEquals(second, TPAManager.selectPending(pending, null, null).requester());
    }

    @Test
    void namingTheFirstPlayerDoesNotTakeTheNewerRequest() {
        UUID target = UUID.randomUUID();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        List<TPAManager.TpaRequest> pending = new ArrayList<>();
        TPAManager.addPending(pending, request(first, target, false));
        TPAManager.addPending(pending, request(second, target, false));

        TPAManager.TpaRequest taken = TPAManager.takePending(pending, first, null);
        assertEquals(first, taken.requester());
        assertEquals(1, pending.size());
        assertEquals(second, pending.get(0).requester());
        assertNull(TPAManager.takePending(pending, UUID.randomUUID(), null));
        assertEquals(1, pending.size());
    }

    @Test
    void theSamePlayerCanKeepATpaAndATpahere() {
        UUID target = UUID.randomUUID();
        UUID requester = UUID.randomUUID();
        List<TPAManager.TpaRequest> pending = new ArrayList<>();
        TPAManager.addPending(pending, request(requester, target, false));
        TPAManager.addPending(pending, request(requester, target, true));

        assertFalse(TPAManager.selectPending(pending, requester, false).tpaHere());
        assertTrue(TPAManager.selectPending(pending, requester, true).tpaHere());
        assertEquals(requester, TPAManager.takePending(pending, requester, false).requester());
        assertEquals(1, pending.size());
        assertTrue(pending.get(0).tpaHere());
    }

    private static TPAManager.TpaRequest request(UUID requester, UUID target, boolean tpaHere) {
        return new TPAManager.TpaRequest(requester, target, tpaHere, false);
    }
}
