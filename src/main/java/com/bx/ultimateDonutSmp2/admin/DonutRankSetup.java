package com.bx.ultimateDonutSmp2.admin;

import java.util.ArrayList;
import java.util.List;

/**
 * The three built-in Donut ranks and the LuckPerms prefix that makes them show in the tab
 * and on the scoreboard. The tab reads {@code %prefix%}; the scoreboard line reads
 * {@code %luckperms_prefix%}, which only resolves when PlaceholderAPI and LuckPerms are installed.
 */
public final class DonutRankSetup {

    public static final String SCOREBOARD_LINE = "&#00A4FC★ &fRank %luckperms_prefix%";

    public static final List<String> SCOREBOARD_LINE_PATHS = List.of(
            "SCOREBOARD.MODERN.LINES",
            "SCOREBOARD.LEGACY.LINES",
            "SCOREBOARD.LINES"
    );

    public record Rank(
            String group,
            String label,
            int weight,
            String prefix,
            String parent,
            List<String> permissions,
            String perks
    ) {
    }

    private static final List<Rank> RANKS = List.of(
            new Rank(
                    "donutplus",
                    "+",
                    10,
                    "<#00A4FC>+ ",
                    null,
                    List.of("ultimatedonutsmp2.donutplus", "donutplus"),
                    "9 homes, 45 auction slots, 45 order slots"
            ),
            new Rank(
                    "donutplusplus",
                    "++",
                    20,
                    "<#00A4FC>++ ",
                    "donutplus",
                    List.of("ultimatedonutsmp2.donutplusplus", "donutplusplus"),
                    "27 homes, 90 auction slots, 90 order slots"
            ),
            new Rank(
                    "donutplusplusplus",
                    "+++",
                    30,
                    "<#00A4FC>+++ ",
                    "donutplusplus",
                    List.of("ultimatedonutsmp2.donutplusplusplus", "donutplusplusplus"),
                    "90 homes, 90 auction slots, 90 order slots"
            )
    );

    private DonutRankSetup() {
    }

    public static List<Rank> ranks() {
        return RANKS;
    }

    public static Rank rank(String group) {
        if (group == null) {
            return null;
        }
        for (Rank rank : RANKS) {
            if (rank.group().equalsIgnoreCase(group)) {
                return rank;
            }
        }
        return null;
    }

    public static boolean isRankGroup(String group) {
        return rank(group) != null;
    }

    /** LuckPerms group names are lowercase. Blank, default, and illegal characters are rejected. */
    public static String normalizeGroup(String raw) {
        if (raw == null) {
            return null;
        }
        String name = raw.trim().toLowerCase(java.util.Locale.ROOT);
        if (name.isEmpty() || name.equals("default") || !name.matches("[a-z0-9._-]{1,36}")) {
            return null;
        }
        return name;
    }

    /** True when a LuckPerms prefix is only the plugin's own +, ++, or +++ badge. */
    public static boolean isBuiltInBadgePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return false;
        }
        String plain = prefix
                .replaceAll("(?i)<#[0-9a-f]{6}>", "")
                .replaceAll("(?i)&#[0-9a-f]{6}", "")
                .replaceAll("(?i)[&§][0-9a-fk-or]", "")
                .replaceAll("(?i)donut", "")
                .replaceAll("\\s+", "");
        return plain.equals("+") || plain.equals("++") || plain.equals("+++");
    }

    public static String prefixWithSpace(String raw) {
        if (raw == null) {
            return null;
        }
        String prefix = raw.trim();
        if (prefix.isEmpty()) {
            return null;
        }
        return prefix.endsWith(" ") ? prefix : prefix + " ";
    }

    public static boolean isManagedPermission(String permission) {
        if (permission == null) {
            return false;
        }
        for (Rank rank : RANKS) {
            for (String node : rank.permissions()) {
                if (node.equalsIgnoreCase(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> missingPlugins(boolean placeholderApi, boolean luckPerms) {
        List<String> missing = new ArrayList<>();
        if (!placeholderApi) {
            missing.add("PlaceholderAPI");
        }
        if (!luckPerms) {
            missing.add("LuckPerms");
        }
        return List.copyOf(missing);
    }

    public static String missingMessage(List<String> missing) {
        String names = missing == null || missing.isEmpty() ? "PlaceholderAPI and LuckPerms" : String.join(" and ", missing);
        return "&cRank setup needs &f" + names + "&c.\n"
                + "&7Install the missing plugin, restart the server, then open Rank setup again.\n"
                + "&7PlaceholderAPI lets the scoreboard read &f%luckperms_prefix%&7.\n"
                + "&7LuckPerms stores the rank permission and the prefix the tab displays.";
    }

    public static boolean showsRank(List<String> lines) {
        if (lines == null) {
            return false;
        }
        for (String line : lines) {
            if (line != null && (line.contains("%luckperms_prefix%") || line.contains("%prefix%"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasScoreboardRankLine(String yaml) {
        if (yaml == null || yaml.isBlank()) {
            return false;
        }
        AdminConfigDocument document = AdminConfigDocument.parse("scoreboard.yml", yaml);
        for (String path : SCOREBOARD_LINE_PATHS) {
            List<String> segments = List.of(path.split("\\."));
            if (!(document.resolve(segments) instanceof AdminConfigDocument.ListNode list)) {
                continue;
            }
            for (AdminConfigDocument.Node element : list.elements()) {
                if (element instanceof AdminConfigDocument.ScalarNode scalar
                        && SCOREBOARD_LINE.equals(scalar.raw())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the rank line to every scoreboard list that does not already show a prefix.
     * Returns null when nothing changed.
     */
    public static String addScoreboardRankLine(String yaml) throws AdminConfigEditException {
        AdminConfigDocument document = AdminConfigDocument.parse("scoreboard.yml", yaml);
        List<AdminConfigDocument.Edit> edits = new ArrayList<>();
        for (String path : SCOREBOARD_LINE_PATHS) {
            List<String> segments = List.of(path.split("\\."));
            if (!(document.resolve(segments) instanceof AdminConfigDocument.ListNode list) || !list.scalarElements()) {
                continue;
            }
            List<String> values = new ArrayList<>();
            for (AdminConfigDocument.Node element : list.elements()) {
                if (element instanceof AdminConfigDocument.ScalarNode scalar) {
                    values.add(scalar.raw());
                }
            }
            if (showsRank(values)) {
                continue;
            }
            values.add(0, SCOREBOARD_LINE);
            edits.add(AdminConfigDocument.Edit.list(segments, values));
        }
        if (edits.isEmpty()) {
            return null;
        }
        return document.apply(edits);
    }

    /** Removes only the rank line this setup added. Returns null when that line is not present. */
    public static String removeScoreboardRankLine(String yaml) throws AdminConfigEditException {
        AdminConfigDocument document = AdminConfigDocument.parse("scoreboard.yml", yaml);
        List<AdminConfigDocument.Edit> edits = new ArrayList<>();
        for (String path : SCOREBOARD_LINE_PATHS) {
            List<String> segments = List.of(path.split("\\."));
            if (!(document.resolve(segments) instanceof AdminConfigDocument.ListNode list) || !list.scalarElements()) {
                continue;
            }
            List<String> values = new ArrayList<>();
            boolean removed = false;
            for (AdminConfigDocument.Node element : list.elements()) {
                if (element instanceof AdminConfigDocument.ScalarNode scalar) {
                    if (SCOREBOARD_LINE.equals(scalar.raw())) {
                        removed = true;
                        continue;
                    }
                    values.add(scalar.raw());
                }
            }
            if (removed) {
                edits.add(AdminConfigDocument.Edit.list(segments, values));
            }
        }
        if (edits.isEmpty()) {
            return null;
        }
        return document.apply(edits);
    }
}
