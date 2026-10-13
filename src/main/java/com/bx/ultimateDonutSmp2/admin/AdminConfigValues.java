package com.bx.ultimateDonutSmp2.admin;

import java.util.Locale;

/**
 * Reads and writes a single yaml scalar the way the bundled configs already spell them.
 */
public final class AdminConfigValues {

    public enum Kind {
        STRING,
        BOOLEAN,
        INTEGER,
        DECIMAL,
        NULL,
        BLOCK
    }

    public record Decoded(Kind kind, String raw, boolean quoted) {
    }

    private AdminConfigValues() {
    }

    public static Decoded decode(String yamlValue) {
        if (yamlValue == null) {
            return new Decoded(Kind.NULL, "", false);
        }
        String trimmed = yamlValue.trim();
        if (trimmed.isEmpty() || trimmed.equals("~") || trimmed.equalsIgnoreCase("null")) {
            return new Decoded(Kind.NULL, "", false);
        }
        if (trimmed.startsWith("|") || trimmed.startsWith(">")) {
            return new Decoded(Kind.BLOCK, "", false);
        }
        if ((trimmed.startsWith("'") && trimmed.endsWith("'") && trimmed.length() >= 2)
                || (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2)) {
            return new Decoded(Kind.STRING, unquote(trimmed), true);
        }
        if (trimmed.equals("true") || trimmed.equals("false")) {
            return new Decoded(Kind.BOOLEAN, trimmed, false);
        }
        if (trimmed.matches("-?\\d+")) {
            return new Decoded(Kind.INTEGER, trimmed, false);
        }
        if (trimmed.matches("-?\\d+\\.\\d+")) {
            return new Decoded(Kind.DECIMAL, trimmed, false);
        }
        return new Decoded(Kind.STRING, trimmed, false);
    }

    /**
     * Formats a value an operator typed. The kind is the value already in the file, so a title
     * stays text even when it looks like a number.
     */
    public static String format(Kind kind, String typed, boolean originallyQuoted) throws AdminConfigEditException {
        String text = typed == null ? "" : typed;
        return switch (kind) {
            case BOOLEAN -> formatBoolean(text);
            case INTEGER -> formatInteger(text);
            case DECIMAL -> formatDecimal(text);
            case NULL -> formatNull(text);
            case BLOCK -> text;
            case STRING -> formatString(text, originallyQuoted);
        };
    }

    public static String formatListItem(Kind kind, String typed, boolean originallyQuoted) throws AdminConfigEditException {
        return format(kind == Kind.NULL ? Kind.STRING : kind, typed, originallyQuoted || kind == Kind.STRING);
    }

    private static String formatBoolean(String text) throws AdminConfigEditException {
        String trimmed = text.trim().toLowerCase(Locale.ROOT);
        if (trimmed.equals("true") || trimmed.equals("false")) {
            return trimmed;
        }
        throw new AdminConfigEditException("Enter true or false.");
    }

    private static String formatInteger(String text) throws AdminConfigEditException {
        String trimmed = text.trim();
        try {
            Long.parseLong(trimmed);
        } catch (NumberFormatException exception) {
            throw new AdminConfigEditException("Enter a whole number.");
        }
        return trimmed;
    }

    private static String formatDecimal(String text) throws AdminConfigEditException {
        String trimmed = text.trim();
        try {
            double value = Double.parseDouble(trimmed);
            if (!Double.isFinite(value)) {
                throw new NumberFormatException(trimmed);
            }
        } catch (NumberFormatException exception) {
            throw new AdminConfigEditException("Enter a decimal number.");
        }
        if (!trimmed.contains(".") && !trimmed.toLowerCase(Locale.ROOT).contains("e")) {
            return trimmed + ".0";
        }
        return trimmed;
    }

    private static String formatNull(String text) throws AdminConfigEditException {
        String trimmed = text.trim();
        if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("null")) {
            return "null";
        }
        return formatString(trimmed, true);
    }

    private static String formatString(String text, boolean originallyQuoted) {
        if (!originallyQuoted && isPlain(text)) {
            return text;
        }
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static boolean isPlain(String text) {
        if (text.isEmpty()) {
            return false;
        }
        if (!text.matches("[A-Za-z0-9_./+;*-]+")) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        if (lower.equals("true") || lower.equals("false") || lower.equals("null")
                || lower.equals("yes") || lower.equals("no")) {
            return false;
        }
        return !text.matches("-?\\d+(\\.\\d+)?");
    }

    static String unquote(String quoted) {
        char quote = quoted.charAt(0);
        String interior = quoted.substring(1, quoted.length() - 1);
        if (quote == '\'') {
            return interior.replace("''", "'");
        }
        StringBuilder builder = new StringBuilder(interior.length());
        for (int index = 0; index < interior.length(); index++) {
            char current = interior.charAt(index);
            if (current == '\\' && index + 1 < interior.length()) {
                char next = interior.charAt(++index);
                builder.append(switch (next) {
                    case 'n' -> '\n';
                    case 't' -> '\t';
                    case 'r' -> '\r';
                    default -> next;
                });
            } else {
                builder.append(current);
            }
        }
        return builder.toString();
    }

    /** Folds a quoted scalar that was wrapped across lines back into the value players see. */
    public static String foldQuoted(String quotedBlock) {
        String trimmed = quotedBlock.trim();
        if (trimmed.length() < 2) {
            return trimmed;
        }
        char quote = trimmed.charAt(0);
        int close = lastClosingQuote(trimmed, quote);
        if (close <= 0) {
            return decode(trimmed).raw();
        }
        String interior = trimmed.substring(1, close);
        String[] parts = interior.split("\n", -1);
        StringBuilder builder = new StringBuilder(parts[0]);
        for (int index = 1; index < parts.length; index++) {
            if (parts[index].isBlank()) {
                builder.append('\n');
            } else if (!builder.isEmpty() && builder.charAt(builder.length() - 1) != '\n') {
                builder.append(' ');
                builder.append(parts[index].stripLeading());
            } else {
                builder.append(parts[index].stripLeading());
            }
        }
        String folded = builder.toString();
        if (quote == '\'') {
            return folded.replace("''", "'");
        }
        return unquote("\"" + folded + "\"");
    }

    private static int lastClosingQuote(String text, char quote) {
        for (int index = text.length() - 1; index > 0; index--) {
            if (text.charAt(index) != quote) {
                continue;
            }
            if (quote == '\'' && index + 1 < text.length() && text.charAt(index + 1) == '\'') {
                continue;
            }
            if (quote == '"' && text.charAt(index - 1) == '\\') {
                continue;
            }
            return index;
        }
        return -1;
    }
}
