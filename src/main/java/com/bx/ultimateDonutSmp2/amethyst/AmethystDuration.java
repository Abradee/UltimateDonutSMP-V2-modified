package com.bx.ultimateDonutSmp2.amethyst;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and prints amethyst grant durations as {@code 1h}, {@code 5d}, {@code 1d 5h}
 * rather than raw seconds.
 *
 * <p>Supported input: {@code 1d}, {@code 5h}, {@code 10m}, {@code 1d5h}, {@code 1d 5h},
 * or a bare number of seconds for the chat command.
 */
public final class AmethystDuration {

    private static final Pattern TOKEN = Pattern.compile("(\\d+)\\s*([dhms])", Pattern.CASE_INSENSITIVE);
    private static final Pattern BARE_SECONDS = Pattern.compile("\\d+");

    private AmethystDuration() {
    }

    /**
     * @return duration in seconds, or {@code 0} when the input cannot be read
     */
    public static long parseSeconds(String input) {
        if (input == null || input.isBlank()) {
            return 0L;
        }
        String trimmed = input.trim();
        Matcher matcher = TOKEN.matcher(trimmed);
        long total = 0L;
        boolean matched = false;
        while (matcher.find()) {
            matched = true;
            final long amount;
            try {
                amount = Long.parseLong(matcher.group(1));
            } catch (NumberFormatException ignored) {
                return 0L;
            }
            total += switch (matcher.group(2).toLowerCase(Locale.ROOT)) {
                case "d" -> amount * 86_400L;
                case "h" -> amount * 3_600L;
                case "m" -> amount * 60L;
                default -> amount;
            };
        }
        if (matched) {
            return total;
        }
        if (BARE_SECONDS.matcher(trimmed).matches()) {
            try {
                return Long.parseLong(trimmed);
            } catch (NumberFormatException ignored) {
                return 0L;
            }
        }
        return 0L;
    }

    /**
     * Compact {@code d}/{@code h}/{@code m} label. Seconds are omitted unless the duration is
     * under a minute, so a button never has to say {@code 3600}.
     */
    public static String formatDhM(long totalSeconds) {
        long seconds = Math.max(0L, totalSeconds);
        if (seconds == 0L) {
            return "0m";
        }
        long days = seconds / 86_400L;
        long hours = (seconds % 86_400L) / 3_600L;
        long minutes = (seconds % 3_600L) / 60L;
        long remaining = seconds % 60L;
        List<String> parts = new ArrayList<>(3);
        if (days > 0L) {
            parts.add(days + "d");
        }
        if (hours > 0L) {
            parts.add(hours + "h");
        }
        if (minutes > 0L) {
            parts.add(minutes + "m");
        }
        if (parts.isEmpty()) {
            return remaining > 0L ? remaining + "s" : "<1m";
        }
        return String.join(" ", parts);
    }

    /** Elapsed last-online text using {@code d}, {@code h}, {@code m} only. */
    public static String formatLastOnline(long elapsedMillis) {
        long seconds = Math.max(0L, elapsedMillis) / 1_000L;
        if (seconds < 60L) {
            return "<1m";
        }
        return formatDhM(seconds);
    }
}
