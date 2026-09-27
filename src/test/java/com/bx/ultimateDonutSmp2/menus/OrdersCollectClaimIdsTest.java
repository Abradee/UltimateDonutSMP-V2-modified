package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.models.OrderCollectionClaim;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersCollectClaimIdsTest {

    @Test
    void dropAllUsesEveryClaimWhileDropPageKeepsTheCurrentPage() {
        UUID owner = UUID.randomUUID();
        List<OrderCollectionClaim> claims = List.of(
                claim(11L, owner),
                claim(12L, owner),
                claim(13L, owner)
        );

        assertEquals(List.of(11L, 12L), OrdersCollectMenu.claimIds(claims, 0, 2));
        assertEquals(List.of(13L), OrdersCollectMenu.claimIds(claims, 2, 4));
        assertEquals(List.of(11L, 12L, 13L), OrdersCollectMenu.claimIds(claims, 0, claims.size()));
    }

    @Test
    void emptyQueueHasNoClaimIds() {
        assertTrue(OrdersCollectMenu.claimIds(List.of(), 0, 45).isEmpty());
        assertTrue(OrdersCollectMenu.claimIds(null, 0, 45).isEmpty());
    }

    private static OrderCollectionClaim claim(long id, UUID owner) {
        return new OrderCollectionClaim(
                id,
                owner,
                1L,
                OrderCollectionClaim.ClaimType.ITEM,
                null,
                0D,
                0L,
                0L
        );
    }
}
