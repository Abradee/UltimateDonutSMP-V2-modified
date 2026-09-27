package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.models.PunishmentType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The wipe flag in offenses.yml is only meant to fire on a ban, so a preset that mutes or warns
 * keeps the account even with the flag turned on.
 */
class OffendWipeGateTest {

    @Test
    void bansAndBlacklistsWipe() throws Exception {
        assertTrue(isBan(PunishmentType.BAN));
        assertTrue(isBan(PunishmentType.BLACKLIST));
    }

    @Test
    void lesserPunishmentsDoNotWipe() throws Exception {
        assertFalse(isBan(PunishmentType.MUTE));
        assertFalse(isBan(PunishmentType.KICK));
        assertFalse(isBan(PunishmentType.WARN), "a 0s tier is issued as a warning and must keep the account");
    }

    @Test
    void parsesWipeOverrideOptionsCaseInsensitively() {
        assertTrue(OffendCommand.parseWipeOverride("wipe"));
        assertTrue(OffendCommand.parseWipeOverride("WIPE"));
        assertTrue(OffendCommand.parseWipeOverride("true"));
        assertTrue(OffendCommand.parseWipeOverride("True"));
        assertFalse(OffendCommand.parseWipeOverride("nowipe"));
        assertFalse(OffendCommand.parseWipeOverride("NOWIPE"));
        assertFalse(OffendCommand.parseWipeOverride("false"));
        assertFalse(OffendCommand.parseWipeOverride("False"));
        org.junit.jupiter.api.Assertions.assertNull(OffendCommand.parseWipeOverride(null));
        org.junit.jupiter.api.Assertions.assertNull(OffendCommand.parseWipeOverride(""));
        org.junit.jupiter.api.Assertions.assertNull(OffendCommand.parseWipeOverride("unknown"));
    }

    @Test
    void wipeOverrideRespectsBansAndLesserPunishments() {
        // Explicit wipe overrides preset false on bans
        assertTrue(OffendCommand.shouldWipe(true, false, PunishmentType.BAN));
        assertTrue(OffendCommand.shouldWipe(true, false, PunishmentType.BLACKLIST));

        // Explicit nowipe overrides preset true on bans
        assertFalse(OffendCommand.shouldWipe(false, true, PunishmentType.BAN));
        assertFalse(OffendCommand.shouldWipe(false, true, PunishmentType.BLACKLIST));

        // Omitted argument falls back to preset
        assertTrue(OffendCommand.shouldWipe(null, true, PunishmentType.BAN));
        assertFalse(OffendCommand.shouldWipe(null, false, PunishmentType.BAN));

        // Non-bans never wipe even if explicit wipe was passed
        assertFalse(OffendCommand.shouldWipe(true, true, PunishmentType.MUTE));
        assertFalse(OffendCommand.shouldWipe(true, true, PunishmentType.WARN));
        assertFalse(OffendCommand.shouldWipe(true, true, PunishmentType.KICK));
    }

    private boolean isBan(PunishmentType type) throws Exception {
        Method method = OffendCommand.class.getDeclaredMethod("isBan", PunishmentType.class);
        method.setAccessible(true);
        return (boolean) method.invoke(null, type);
    }
}
