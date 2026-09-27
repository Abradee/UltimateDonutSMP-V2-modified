package com.bx.ultimateDonutSmp2.amethyst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmethystPendingGrantsTest {

    @TempDir
    File tempDir;

    @Test
    void queuedGrantsSurviveReloadAndAreTakenOnce() {
        File file = new File(tempDir, "amethyst-pending.yml");
        AmethystPendingGrants first = new AmethystPendingGrants(file, null);
        UUID player = UUID.fromString("11111111-1111-1111-1111-111111111111");
        first.queue(player, "pickaxe-silk", 3600L);
        first.queue(player, "axe", 86_400L);
        assertEquals(2, first.count(player));

        AmethystPendingGrants reloaded = new AmethystPendingGrants(file, null);
        reloaded.load();
        assertEquals(2, reloaded.count(player));

        List<AmethystPendingGrants.Grant> taken = reloaded.take(player);
        assertEquals(2, taken.size());
        assertEquals("pickaxe-silk", taken.get(0).typeId());
        assertEquals(3600L, taken.get(0).durationSeconds());
        assertEquals("axe", taken.get(1).typeId());
        assertTrue(reloaded.take(player).isEmpty());
        assertEquals(0, reloaded.count(player));
    }
}
