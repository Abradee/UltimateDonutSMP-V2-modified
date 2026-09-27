package com.bx.ultimateDonutSmp2.amethyst;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmethystDurationTest {

    @Test
    void parsesHumanUnitsAndBareSeconds() {
        assertEquals(3600L, AmethystDuration.parseSeconds("1h"));
        assertEquals(18_000L, AmethystDuration.parseSeconds("5h"));
        assertEquals(36_000L, AmethystDuration.parseSeconds("10h"));
        assertEquals(86_400L, AmethystDuration.parseSeconds("1d"));
        assertEquals(432_000L, AmethystDuration.parseSeconds("5d"));
        assertEquals(104_400L, AmethystDuration.parseSeconds("1d 5h"));
        assertEquals(104_400L, AmethystDuration.parseSeconds("1d5h"));
        assertEquals(3600L, AmethystDuration.parseSeconds("3600"));
        assertEquals(90L, AmethystDuration.parseSeconds("90s"));
        assertEquals(0L, AmethystDuration.parseSeconds("soon"));
        assertEquals(0L, AmethystDuration.parseSeconds(""));
        assertEquals(0L, AmethystDuration.parseSeconds(null));
        assertEquals(0L, AmethystDuration.parseSeconds("99999999999999999999h"));
    }

    @Test
    void formatDhMNeverPrintsRawSecondsForHourScaleValues() {
        assertEquals("1h", AmethystDuration.formatDhM(3600L));
        assertEquals("5h", AmethystDuration.formatDhM(18_000L));
        assertEquals("10h", AmethystDuration.formatDhM(36_000L));
        assertEquals("1d", AmethystDuration.formatDhM(86_400L));
        assertEquals("5d", AmethystDuration.formatDhM(432_000L));
        assertEquals("1d 5h", AmethystDuration.formatDhM(104_400L));
        assertEquals("1h 5m", AmethystDuration.formatDhM(3900L));
        assertEquals("45s", AmethystDuration.formatDhM(45L));
        assertEquals("0m", AmethystDuration.formatDhM(0L));
    }

    @Test
    void lastOnlineUsesDaysHoursMinutesOnly() {
        assertEquals("<1m", AmethystDuration.formatLastOnline(12_000L));
        assertEquals("5m", AmethystDuration.formatLastOnline(5 * 60_000L));
        assertEquals("2h", AmethystDuration.formatLastOnline(2 * 3_600_000L));
        assertEquals("1d 2h", AmethystDuration.formatLastOnline((86_400L + 7_200L) * 1000L));
    }
}
