package com.bx.ultimateDonutSmp2.admin;

import java.util.List;
import java.util.Locale;

/**
 * Turns a yaml key into the sentence and example shown on its dialog control.
 *
 * <p>Comments already written above the key win. A key with no comment still gets a plain
 * description and a concrete sample, so an operator can tell what to type without opening the file.
 */
public final class AdminConfigGuide {

    private static final int FUNCTION_LIMIT = 48;
    private static final int EXAMPLE_LIMIT = 32;

    private AdminConfigGuide() {
    }

    /** {@code PRICE-PER-UNIT} becomes {@code Price Per Unit}. Keys that are already a phrase stay. */
    public static String humanize(String key) {
        if (key == null || key.isBlank()) {
            return "Value";
        }
        if (key.indexOf('.') >= 0 || key.indexOf(' ') >= 0) {
            return key;
        }
        String[] parts = key.split("[-_]+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return builder.isEmpty() ? key : builder.toString();
    }

    public static String function(String key, List<String> comments, AdminConfigValues.Kind kind) {
        String fromComment = commentBody(comments);
        if (!fromComment.isEmpty()) {
            return clip(fromComment, FUNCTION_LIMIT);
        }
        return generatedFunction(key, kind);
    }

    /**
     * A value the operator can copy. Secrets never echo the live password or webhook; the text
     * box still holds the real value so it can be replaced.
     */
    public static String example(String key, List<String> comments, AdminConfigValues.Kind kind, String current) {
        if (sensitive(key)) {
            return sensitiveExample(key);
        }
        if (current != null && !current.isBlank() && current.length() <= EXAMPLE_LIMIT && current.indexOf('\n') < 0) {
            return current;
        }
        String options = availableOptions(comments);
        if (options != null && !options.toLowerCase(Locale.ROOT).startsWith("any valid") && options.length() <= EXAMPLE_LIMIT) {
            return options;
        }
        String sample = sampleFor(key, kind);
        if (sample != null) {
            return sample;
        }
        return fallback(kind);
    }

    /**
     * One short line. A label that wraps inside a dialog input is drawn italic by the client,
     * so the long explanation stays out of the label and only the name plus a short example remain.
     */
    public static String inputLabel(String key, List<String> comments, AdminConfigValues.Kind kind, String current) {
        String name = humanize(key);
        if (kind == AdminConfigValues.Kind.BOOLEAN || hasColorCodes(current)) {
            return "&f" + name;
        }
        String sample = example(key, comments, kind, current);
        String plain = name + " — " + sample;
        if (plain.length() > 36) {
            return "&f" + name + " &8— &f" + clip(sample, 12);
        }
        return "&f" + name + " &8— &f" + sample;
    }

    /** Tooltip for a button that opens a section or a list. */
    public static String sectionTooltip(String key, List<String> comments, List<String> childKeys) {
        String shown = childKeys == null || childKeys.isEmpty()
                ? "(open it to see the options)"
                : String.join(", ", childKeys.subList(0, Math.min(5, childKeys.size())));
        return "&f" + humanize(key)
                + "\n&7" + function(key, comments, AdminConfigValues.Kind.STRING)
                + "\n&8Example keys: &f" + shown
                + "\n&7Opens every option in this section.";
    }

    public static String listLineLabel(int lineNumber, AdminConfigValues.Kind kind, String current) {
        if (hasColorCodes(current)) {
            return "&fLine " + lineNumber;
        }
        String sample = example("line", List.of(), kind, current);
        return "&fLine " + lineNumber + " &8— &f" + clip(sample, 16);
    }

    /** Full colored line shown above the input. Null when the value has no color codes. */
    public static String colorPreview(String name, String value) {
        if (!hasColorCodes(value)) {
            return null;
        }
        String title = name == null || name.isBlank() ? "Value" : name;
        return "&7" + title + "&8: &r" + value;
    }

    /** True when the text contains a color or format code the dialog can draw. */
    public static boolean hasColorCodes(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (int index = 0; index < text.length(); index++) {
            char current = text.charAt(index);
            if (current != '&' && current != '§') {
                continue;
            }
            if (index > 0 && isCodeTokenChar(text.charAt(index - 1))) {
                continue;
            }
            if (index + 1 >= text.length()) {
                continue;
            }
            char next = text.charAt(index + 1);
            if (next == '#' && index + 7 < text.length() && isHex(text, index + 2, 6)) {
                return true;
            }
            if (isFormatCode(next)) {
                return true;
            }
        }
        return text.indexOf("<#") >= 0;
    }

    private static String oneLine(String text) {
        String flat = text.replace('\r', ' ').replace('\n', ' ').trim();
        return flat.length() <= 64 ? flat : flat.substring(0, 61).trim() + "...";
    }

    private static boolean isCodeTokenChar(char character) {
        return (character >= 'A' && character <= 'Z')
                || (character >= 'a' && character <= 'z')
                || (character >= '0' && character <= '9');
    }

    private static boolean isFormatCode(char character) {
        char lower = Character.toLowerCase(character);
        return (lower >= '0' && lower <= '9') || lower == 'a' || lower == 'b' || lower == 'c'
                || lower == 'd' || lower == 'e' || lower == 'f' || lower == 'k' || lower == 'l'
                || lower == 'm' || lower == 'n' || lower == 'o' || lower == 'r';
    }

    private static boolean isHex(String text, int start, int length) {
        for (int index = start; index < start + length; index++) {
            char character = text.charAt(index);
            boolean digit = character >= '0' && character <= '9';
            char lower = Character.toLowerCase(character);
            boolean hex = lower >= 'a' && lower <= 'f';
            if (!digit && !hex) {
                return false;
            }
        }
        return true;
    }

    public static boolean sensitive(String key) {
        if (key == null) {
            return false;
        }
        String lower = key.toLowerCase(Locale.ROOT);
        return lower.contains("password")
                || lower.contains("secret")
                || lower.contains("token")
                || lower.contains("webhook")
                || lower.equals("url")
                || lower.equals("uri");
    }

    private static String sensitiveExample(String key) {
        String lower = key.toLowerCase(Locale.ROOT);
        if (lower.contains("webhook") || lower.equals("url")) {
            return "https://discord.com/api/webhooks/your_webhook_here";
        }
        if (lower.equals("uri")) {
            return "mongodb://localhost:27017";
        }
        return "change-me";
    }

    private static String generatedFunction(String key, AdminConfigValues.Kind kind) {
        String lower = key == null ? "" : key.toLowerCase(Locale.ROOT);
        if (kind == AdminConfigValues.Kind.BOOLEAN || lower.contains("enabled")) {
            return "Turns this on or off.";
        }
        if (lower.contains("material") || lower.equals("type") || lower.contains("icon")) {
            return "Which item, block, or type this uses.";
        }
        if (lower.contains("slot")) {
            return "Inventory slot, counting from 0 at the top left.";
        }
        if (lower.contains("title") || lower.contains("name")) {
            return "Text shown to players. Color codes such as &f and &#RRGGBB work.";
        }
        if (lower.contains("lore") || lower.contains("line") || lower.contains("message")) {
            return "Text shown to players. One color code can start the line.";
        }
        if (lower.contains("command")) {
            return "Command run for this action, without the leading slash. Leave it empty for none.";
        }
        if (lower.contains("permission")) {
            return "Permission node a player needs for this.";
        }
        if (lower.contains("sound")) {
            return "Sound name played for this action.";
        }
        if (lower.contains("world")) {
            return "World name this applies to.";
        }
        if (lower.contains("price") || lower.contains("cost") || lower.contains("amount")) {
            return "How much this costs or how many are given.";
        }
        if (hasToken(lower, "host")) {
            return "Hostname or IP address to connect to.";
        }
        if (hasToken(lower, "port")) {
            return "Network port number.";
        }
        if (hasToken(lower, "location")) {
            return "World and coordinates, or leave empty.";
        }
        if (kind == AdminConfigValues.Kind.INTEGER) {
            return "A whole number.";
        }
        if (kind == AdminConfigValues.Kind.DECIMAL) {
            return "A decimal number.";
        }
        return "Setting for " + humanize(key) + ".";
    }

    private static String sampleFor(String key, AdminConfigValues.Kind kind) {
        String lower = key == null ? "" : key.toLowerCase(Locale.ROOT);
        if (lower.contains("material")) {
            return "DIAMOND";
        }
        if (lower.contains("slot")) {
            return "13";
        }
        if (lower.contains("title")) {
            return "&8Shard Shop";
        }
        if (lower.contains("name") || lower.contains("display")) {
            return "&fExample name";
        }
        if (lower.contains("lore") || lower.equals("line")) {
            return "&7Example line";
        }
        if (lower.contains("command")) {
            return "say Hello";
        }
        if (lower.contains("permission")) {
            return "ultimatedonutsmp2.vip";
        }
        if (lower.contains("sound")) {
            return "ENTITY_PLAYER_LEVELUP";
        }
        if (lower.contains("world")) {
            return "world";
        }
        if (lower.contains("price") || lower.contains("cost")) {
            return "1500";
        }
        if (lower.contains("amount") || lower.contains("quantity")) {
            return "1";
        }
        if (lower.contains("color") || lower.contains("colour")) {
            return "&#A303F9";
        }
        if (hasToken(lower, "location")) {
            return "world 0 64 0";
        }
        if (hasToken(lower, "host")) {
            return "localhost";
        }
        if (hasToken(lower, "port")) {
            return "3306";
        }
        if (lower.contains("cooldown") || lower.contains("delay")) {
            return "30";
        }
        if (lower.contains("radius")) {
            return "1000";
        }
        if (lower.contains("file") || lower.contains("path")) {
            return "data/data.db";
        }
        if (kind == AdminConfigValues.Kind.BOOLEAN) {
            return "true";
        }
        if (kind == AdminConfigValues.Kind.INTEGER) {
            return "10";
        }
        if (kind == AdminConfigValues.Kind.DECIMAL) {
            return "1.5";
        }
        if (kind == AdminConfigValues.Kind.NULL) {
            return "leave empty for null";
        }
        return null;
    }

    private static String fallback(AdminConfigValues.Kind kind) {
        return switch (kind) {
            case BOOLEAN -> "true";
            case INTEGER -> "10";
            case DECIMAL -> "1.5";
            case NULL -> "leave empty for null";
            case BLOCK -> "first line of the text";
            case STRING -> "example text";
        };
    }

    private static String commentBody(List<String> comments) {
        if (comments == null || comments.isEmpty()) {
            return "";
        }
        for (String comment : comments) {
            if (comment == null || comment.isBlank() || isBanner(comment) || boilerplate(comment)) {
                continue;
            }
            String line = comment.trim();
            int options = line.toLowerCase(Locale.ROOT).indexOf("available options:");
            if (options >= 0) {
                line = line.substring(0, options).trim();
            }
            int comma = line.indexOf(',');
            if (comma > 12 && comma < 56) {
                line = line.substring(0, comma).trim();
            }
            return line;
        }
        return "";
    }

    /** Generated comments such as "The text or value for Spawn Location" repeat the key name. */
    private static boolean boilerplate(String comment) {
        String lower = comment.trim().toLowerCase(Locale.ROOT);
        return lower.startsWith("the text or value for")
                || lower.startsWith("the numerical value for")
                || lower.startsWith("the decimal value for")
                || lower.startsWith("determines whether")
                || lower.startsWith("configuration section for")
                || lower.startsWith("available options");
    }

    private static boolean hasToken(String lowerKey, String token) {
        if (lowerKey == null || lowerKey.isEmpty()) {
            return false;
        }
        for (String part : lowerKey.split("[-_\\s.]+")) {
            if (part.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private static String availableOptions(List<String> comments) {
        if (comments == null) {
            return null;
        }
        String joined = String.join(" ", comments);
        int options = joined.toLowerCase(Locale.ROOT).indexOf("available options:");
        if (options < 0) {
            return null;
        }
        return joined.substring(options + "available options:".length()).trim();
    }

    private static boolean isBanner(String comment) {
        String stripped = comment.replace("=", "").replace("-", "").trim();
        return stripped.isEmpty();
    }

    private static String clip(String text, int limit) {
        String trimmed = text.trim();
        if (trimmed.length() <= limit) {
            return trimmed;
        }
        return trimmed.substring(0, Math.max(0, limit - 3)).trim() + "...";
    }
}
