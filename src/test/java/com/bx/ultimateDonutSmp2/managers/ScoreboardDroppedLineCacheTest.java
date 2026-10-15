package com.bx.ultimateDonutSmp2.managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScoreboardDroppedLineCacheTest {

    private static final String PLAYTIME_RAW = "&#FCE300⌚ &f12h 4m";
    private static final String PLAYTIME_TEXT = "§612h 4m";

    @Test
    void turningPlaytimeOffAndOnSendsTheLineAgain() {
        String[] rawCache = new String[15];
        String[] textCache = new String[15];

        assertTrue(ScoreboardManager.rememberFormattedLine(textCache, rawCache, 4, PLAYTIME_RAW, PLAYTIME_TEXT));
        assertTrue(ScoreboardManager.sidebarRawUnchanged(rawCache[4], PLAYTIME_RAW));

        // The legacy layout's last row is playtime when the player has no team and no booster.
        // Turning it off pulls that slot off the sidebar and blanks the team prefix.
        ScoreboardManager.dropTrailingLineCache(textCache, rawCache, 4);

        assertEquals("", textCache[4]);
        assertNull(rawCache[4]);
        assertFalse(ScoreboardManager.sidebarRawUnchanged(rawCache[4], PLAYTIME_RAW));
        assertTrue(ScoreboardManager.rememberFormattedLine(textCache, rawCache, 4, PLAYTIME_RAW, PLAYTIME_TEXT));
        assertEquals(PLAYTIME_TEXT, textCache[4]);
    }

    @Test
    void hidingAMiddleLineStillRedrawsTheRowThatSlidOffTheEnd() {
        String[] rawCache = new String[15];
        String[] textCache = new String[15];
        ScoreboardManager.rememberFormattedLine(textCache, rawCache, 2, "&#FC0000🗡 &f3", "§c3");
        ScoreboardManager.rememberFormattedLine(textCache, rawCache, 4, PLAYTIME_RAW, PLAYTIME_TEXT);

        ScoreboardManager.dropTrailingLineCache(textCache, rawCache, 4);

        assertTrue(ScoreboardManager.sidebarRawUnchanged(rawCache[2], "&#FC0000🗡 &f3"));
        assertTrue(ScoreboardManager.rememberFormattedLine(textCache, rawCache, 4, PLAYTIME_RAW, PLAYTIME_TEXT));
    }

    @Test
    void anUnchangedVisibleLineIsLeftAlone() {
        String[] rawCache = new String[15];
        String[] textCache = new String[15];
        String raw = "&#00FC00$ &f1.2K";
        String text = "§a1.2K";

        assertTrue(ScoreboardManager.rememberFormattedLine(textCache, rawCache, 0, raw, text));
        assertTrue(ScoreboardManager.sidebarRawUnchanged(rawCache[0], raw));
        assertFalse(ScoreboardManager.rememberFormattedLine(textCache, rawCache, 0, raw, text));
    }
}
