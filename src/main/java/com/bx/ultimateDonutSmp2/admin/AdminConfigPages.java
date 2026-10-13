package com.bx.ultimateDonutSmp2.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Splits one config section into dialog pages. Inputs and buttons are paged separately so a
 * file like shop.yml can show every key without putting hundreds of controls on one screen.
 */
public final class AdminConfigPages {

    public static final int INPUTS_PER_PAGE = 4;
    public static final int BUTTONS_PER_PAGE = 6;

    public record Entry(String key, List<String> path, String label, String tooltip, String searchText) {
    }

    public record Page(List<Entry> inputs, List<Entry> buttons, int page, int pageCount) {
    }

    private AdminConfigPages() {
    }

    public static boolean matches(String query, String searchText) {
        if (query == null || query.isBlank()) {
            return true;
        }
        if (searchText == null) {
            return false;
        }
        return searchText.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT));
    }

    public static Page slice(List<Entry> inputs, List<Entry> buttons, int page) {
        List<Entry> safeInputs = inputs == null ? List.of() : inputs;
        List<Entry> safeButtons = buttons == null ? List.of() : buttons;
        int inputPages = pages(safeInputs.size(), INPUTS_PER_PAGE);
        int buttonPages = pages(safeButtons.size(), BUTTONS_PER_PAGE);
        int pageCount = Math.max(inputPages, buttonPages);
        int current = pageCount == 0 ? 0 : Math.max(0, Math.min(page, pageCount - 1));
        return new Page(
                window(safeInputs, current, INPUTS_PER_PAGE),
                window(safeButtons, current, BUTTONS_PER_PAGE),
                current,
                pageCount
        );
    }

    public static String searchText(String key, List<String> comments, String value) {
        StringBuilder builder = new StringBuilder();
        if (key != null) {
            builder.append(key).append(' ').append(AdminConfigGuide.humanize(key));
        }
        if (comments != null) {
            for (String comment : comments) {
                builder.append(' ').append(comment);
            }
        }
        if (value != null) {
            builder.append(' ').append(value);
        }
        return builder.toString();
    }

    private static int pages(int count, int size) {
        if (count <= 0) {
            return 0;
        }
        return (count + size - 1) / size;
    }

    private static List<Entry> window(List<Entry> entries, int page, int size) {
        int start = page * size;
        if (start >= entries.size()) {
            return List.of();
        }
        return new ArrayList<>(entries.subList(start, Math.min(entries.size(), start + size)));
    }
}
