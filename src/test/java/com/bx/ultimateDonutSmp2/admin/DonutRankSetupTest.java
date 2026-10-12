package com.bx.ultimateDonutSmp2.admin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DonutRankSetupTest {

    @Test
    void ranksCarryThePermissionsAndAVisiblePrefix() {
        assertEquals(3, DonutRankSetup.ranks().size());
        DonutRankSetup.Rank plus = DonutRankSetup.rank("donutplus");
        DonutRankSetup.Rank plusPlus = DonutRankSetup.rank("donutplusplus");
        DonutRankSetup.Rank top = DonutRankSetup.rank("donutplusplusplus");
        assertTrue(plus.permissions().contains("ultimatedonutsmp2.donutplus"));
        assertTrue(plus.prefix().startsWith("<#00A4FC>+"));
        assertFalse(plus.prefix().toLowerCase().contains("donut"));
        assertEquals("<#00A4FC>++ ", plusPlus.prefix());
        assertEquals("<#00A4FC>+++ ", top.prefix());
        assertTrue(DonutRankSetup.isBuiltInBadgePrefix("<#00A4FC>+++ "));
        assertTrue(DonutRankSetup.isBuiltInBadgePrefix("&fDonut&#00A4FC+"));
        assertFalse(DonutRankSetup.isBuiltInBadgePrefix("&cAdmin"));
        assertEquals("donutplus", plusPlus.parent());
        assertEquals("donutplusplus", top.parent());
        assertTrue(top.weight() > plusPlus.weight());
        assertTrue(DonutRankSetup.isManagedPermission("donutplusplusplus"));
        assertFalse(DonutRankSetup.isManagedPermission("ultimatedonutsmp2.command.shop"));
        assertEquals("admin", DonutRankSetup.normalizeGroup(" Admin "));
        assertNull(DonutRankSetup.normalizeGroup("default"));
        assertNull(DonutRankSetup.normalizeGroup("bad name"));
        assertEquals("&cAdmin ", DonutRankSetup.prefixWithSpace(" &cAdmin"));
        assertEquals("<#00A4FC>+ ", DonutRankSetup.prefixWithSpace("<#00A4FC>+ "));
    }

    @Test
    void missingEitherPluginIsNamedInTheError() {
        assertEquals(List.of("PlaceholderAPI", "LuckPerms"), DonutRankSetup.missingPlugins(false, false));
        assertEquals(List.of("LuckPerms"), DonutRankSetup.missingPlugins(true, false));
        assertTrue(DonutRankSetup.missingMessage(List.of("LuckPerms")).contains("LuckPerms"));
        assertTrue(DonutRankSetup.missingPlugins(true, true).isEmpty());
    }

    @Test
    void scoreboardGainsAPrefixLineOnce() throws Exception {
        String yaml = """
                SCOREBOARD:
                  MODERN:
                    LINES:
                      - '&fMoney'
                  LEGACY:
                    LINES:
                      - '&fKills'
                  LINES:
                    - '%prefix% already here'
                """;
        String updated = DonutRankSetup.addScoreboardRankLine(yaml);
        assertTrue(updated.contains("%luckperms_prefix%"));
        assertTrue(updated.contains("%prefix% already here"));
        assertTrue(DonutRankSetup.hasScoreboardRankLine(updated));
        assertNull(DonutRankSetup.addScoreboardRankLine(updated));
        String removed = DonutRankSetup.removeScoreboardRankLine(updated);
        assertFalse(removed.contains(DonutRankSetup.SCOREBOARD_LINE));
        assertTrue(removed.contains("%prefix% already here"));
        assertNull(DonutRankSetup.removeScoreboardRankLine(removed));
    }
}
