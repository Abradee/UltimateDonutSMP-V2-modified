package com.bx.ultimateDonutSmp2.admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A yaml config kept as lines so an in-game edit can change one value and leave comments,
 * key order, and the rest of the file where they were.
 *
 * <p>Language files are never opened here. The dialog walks {@link MapNode} and {@link ListNode}
 * children as buttons and {@link ScalarNode} leaves as inputs.
 */
public final class AdminConfigDocument {

    private final String fileName;
    private final List<String> lines;
    private final String lineSeparator;
    private final boolean trailingLineSeparator;
    private final MapNode root;

    private AdminConfigDocument(
            String fileName,
            List<String> lines,
            String lineSeparator,
            boolean trailingLineSeparator,
            MapNode root
    ) {
        this.fileName = fileName;
        this.lines = lines;
        this.lineSeparator = lineSeparator;
        this.trailingLineSeparator = trailingLineSeparator;
        this.root = root;
    }

    public static AdminConfigDocument parse(String fileName, String text) {
        String source = text == null ? "" : text;
        if (source.startsWith("\uFEFF")) {
            source = source.substring(1);
        }
        String separator = detectSeparator(source);
        boolean trailing = source.endsWith("\n") || source.endsWith("\r");
        List<String> lines = new ArrayList<>(List.of(source.split("\\r\\n|\\n|\\r", -1)));
        if (trailing && !lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        Parser parser = new Parser(fileName, lines);
        MapNode root = parser.parseRoot();
        return new AdminConfigDocument(fileName, lines, separator, trailing, root);
    }

    public String fileName() {
        return fileName;
    }

    public MapNode root() {
        return root;
    }

    public Node resolve(List<String> path) {
        Node current = root;
        if (path == null) {
            return current;
        }
        for (String segment : path) {
            if (current instanceof MapNode map) {
                current = map.child(segment);
            } else if (current instanceof ListNode list) {
                Integer index = listIndex(segment);
                current = index == null || index < 0 || index >= list.elements().size()
                        ? null
                        : list.elements().get(index);
            } else {
                return null;
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    /** Applies every edit or none of them. Line numbers come from this parse, so call it once. */
    public String apply(List<Edit> edits) throws AdminConfigEditException {
        if (edits == null || edits.isEmpty()) {
            return serialize(lines);
        }
        List<Span> spans = new ArrayList<>();
        for (Edit edit : edits) {
            spans.add(spanFor(edit));
        }
        spans.sort(Comparator.comparingInt(Span::start).reversed());
        List<String> updated = new ArrayList<>(lines);
        for (Span span : spans) {
            if (span.start < 0 || span.end > updated.size() || span.start > span.end) {
                throw new AdminConfigEditException("Could not find that option in " + fileName + ".");
            }
            updated.subList(span.start, span.end).clear();
            updated.addAll(span.start, span.replacement);
        }
        return serialize(updated);
    }

    private Span spanFor(Edit edit) throws AdminConfigEditException {
        Node node = resolve(edit.path());
        if (node == null) {
            throw new AdminConfigEditException("That option is no longer in " + fileName + ".");
        }
        if (edit instanceof ScalarEdit scalar) {
            if (!(node instanceof ScalarNode value)) {
                throw new AdminConfigEditException(label(node) + " is a section. Open it instead of typing a value.");
            }
            if (value.block()) {
                return rewriteBlock(value, scalar.text());
            }
            String formatted = formatField(value.key(), value.comments(), value.kind(), scalar.text(), value.quoted());
            return new Span(value.startLine(), value.endLine(), List.of(rewriteScalarLine(value, formatted)));
        }
        if (edit instanceof ListEdit listEdit) {
            if (!(node instanceof ListNode list) || !list.scalarElements()) {
                throw new AdminConfigEditException(label(node) + " is not a list of values.");
            }
            return rewriteScalarList(list, listEdit.items());
        }
        throw new AdminConfigEditException("Unsupported edit.");
    }

    private Span rewriteScalarList(ListNode list, List<String> items) throws AdminConfigEditException {
        List<String> cleaned = new ArrayList<>();
        AdminConfigValues.Kind kind = list.elementKind();
        boolean quoted = list.elementQuoted();
        int lineNumber = 1;
        for (String item : items) {
            if (item == null) {
                continue;
            }
            String text = item.trim();
            if (text.isEmpty()) {
                continue;
            }
            try {
                cleaned.add(AdminConfigValues.formatListItem(kind, text, quoted));
            } catch (AdminConfigEditException exception) {
                String example = AdminConfigGuide.example("line", List.of(), kind, null);
                throw new AdminConfigEditException("Line " + lineNumber + ": " + exception.getMessage()
                        + " Example: " + example);
            }
            lineNumber++;
        }
        String itemIndent = list.itemIndent().isEmpty() ? indent(list.indent() + 2) : list.itemIndent();
        List<String> replacement = new ArrayList<>();
        for (String formatted : cleaned) {
            replacement.add(itemIndent + "- " + formatted);
        }
        if (list.inlineEmpty()) {
            if (cleaned.isEmpty()) {
                return new Span(list.startLine(), list.endLine(), List.of(keyHeader(list) + " []"));
            }
            replacement.add(0, keyHeader(list));
            return new Span(list.startLine(), list.endLine(), replacement);
        }
        if (cleaned.isEmpty()) {
            replacement.add(keyHeader(list) + " []");
            return new Span(list.startLine(), list.endLine(), replacement);
        }
        return new Span(list.itemsStart(), list.endLine(), replacement);
    }

    private Span rewriteBlock(ScalarNode node, String text) {
        String body = text == null ? "" : text.replace("\r\n", "\n").replace('\r', '\n');
        if (body.endsWith("\n")) {
            body = body.substring(0, body.length() - 1);
        }
        String original = lines.get(node.startLine());
        int colon = keyColon(original, node.indent());
        String header = colon < 0
                ? indent(node.indent()) + node.keyToken() + ":"
                : original.substring(0, colon + 1);
        if (body.isEmpty()) {
            return new Span(node.startLine(), node.endLine(), List.of(header + " ''"));
        }
        String indicator = colon < 0 ? "|-" : original.substring(colon + 1).trim();
        String token = indicator.startsWith("|") || indicator.startsWith(">")
                ? indicator.split("\\s+", 2)[0]
                : "|-";
        List<String> replacement = new ArrayList<>();
        replacement.add(header + " " + token);
        String childIndent = indent(node.indent() + 2);
        for (String line : body.split("\n", -1)) {
            replacement.add(childIndent + line);
        }
        return new Span(node.startLine(), node.endLine(), replacement);
    }

    private String rewriteScalarLine(ScalarNode node, String formatted) {
        if (node.block()) {
            return formatted;
        }
        String original = lines.get(node.startLine());
        String inline = node.inlineComment() == null ? "" : " " + node.inlineComment();
        if (node.listElement()) {
            int dash = original.indexOf('-');
            String prefix = dash < 0 ? indent(node.indent()) + "-" : original.substring(0, dash + 1);
            return prefix + " " + formatted + inline;
        }
        int colon = keyColon(original, node.indent());
        String prefix = colon < 0 ? indent(node.indent()) + node.keyToken() : original.substring(0, colon + 1);
        return prefix + " " + formatted + inline;
    }

    private String keyHeader(ListNode list) {
        String original = lines.get(list.startLine());
        int colon = keyColon(original, list.indent());
        if (colon < 0) {
            return indent(list.indent()) + list.keyToken() + ":";
        }
        return original.substring(0, colon + 1);
    }

    private String formatField(
            String key,
            List<String> comments,
            AdminConfigValues.Kind kind,
            String typed,
            boolean quoted
    ) throws AdminConfigEditException {
        try {
            if (kind == AdminConfigValues.Kind.BLOCK) {
                return typed == null ? "" : typed;
            }
            return AdminConfigValues.format(kind, typed, quoted);
        } catch (AdminConfigEditException exception) {
            String example = AdminConfigGuide.example(key, comments, kind, null);
            throw new AdminConfigEditException(AdminConfigGuide.humanize(key) + ": " + exception.getMessage()
                    + " Example: " + example);
        }
    }

    private String label(Node node) {
        return AdminConfigGuide.humanize(node.key() == null ? "Value" : node.key());
    }

    private String serialize(List<String> content) {
        String joined = String.join(lineSeparator, content);
        return trailingLineSeparator ? joined + lineSeparator : joined;
    }

    private static Integer listIndex(String segment) {
        if (segment == null || !segment.matches("\\d+")) {
            return null;
        }
        try {
            return Integer.parseInt(segment);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static String detectSeparator(String content) {
        int crlf = content.indexOf("\r\n");
        int lf = content.indexOf('\n');
        if (crlf >= 0 && (lf < 0 || crlf <= lf)) {
            return "\r\n";
        }
        if (content.indexOf('\r') >= 0 && lf < 0) {
            return "\r";
        }
        return "\n";
    }

    private static String indent(int spaces) {
        return " ".repeat(Math.max(0, spaces));
    }

    public sealed interface Node permits MapNode, ListNode, ScalarNode {
        String key();

        List<String> comments();

        int startLine();

        int endLine();

        int indent();
    }

    public static final class MapNode implements Node {
        private final String key;
        private final String keyToken;
        private final List<String> comments;
        private final int startLine;
        private final int endLine;
        private final int indent;
        private final List<Node> children;

        private MapNode(
                String key,
                String keyToken,
                List<String> comments,
                int startLine,
                int endLine,
                int indent,
                List<Node> children
        ) {
            this.key = key;
            this.keyToken = keyToken;
            this.comments = comments;
            this.startLine = startLine;
            this.endLine = endLine;
            this.indent = indent;
            this.children = children;
        }

        @Override
        public String key() {
            return key;
        }

        public String keyToken() {
            return keyToken;
        }

        @Override
        public List<String> comments() {
            return comments;
        }

        @Override
        public int startLine() {
            return startLine;
        }

        @Override
        public int endLine() {
            return endLine;
        }

        @Override
        public int indent() {
            return indent;
        }

        public List<Node> children() {
            return children;
        }

        public Node child(String key) {
            Node found = null;
            for (Node child : children) {
                if (child.key() != null && child.key().equals(key)) {
                    found = child;
                }
            }
            return found;
        }
    }

    public static final class ListNode implements Node {
        private final String key;
        private final String keyToken;
        private final List<String> comments;
        private final int startLine;
        private final int endLine;
        private final int indent;
        private final int itemsStart;
        private final String itemIndent;
        private final boolean inlineEmpty;
        private final List<Node> elements;

        private ListNode(
                String key,
                String keyToken,
                List<String> comments,
                int startLine,
                int endLine,
                int indent,
                int itemsStart,
                String itemIndent,
                boolean inlineEmpty,
                List<Node> elements
        ) {
            this.key = key;
            this.keyToken = keyToken;
            this.comments = comments;
            this.startLine = startLine;
            this.endLine = endLine;
            this.indent = indent;
            this.itemsStart = itemsStart;
            this.itemIndent = itemIndent;
            this.inlineEmpty = inlineEmpty;
            this.elements = elements;
        }

        @Override
        public String key() {
            return key;
        }

        public String keyToken() {
            return keyToken;
        }

        @Override
        public List<String> comments() {
            return comments;
        }

        @Override
        public int startLine() {
            return startLine;
        }

        @Override
        public int endLine() {
            return endLine;
        }

        @Override
        public int indent() {
            return indent;
        }

        public int itemsStart() {
            return itemsStart;
        }

        public String itemIndent() {
            return itemIndent;
        }

        public boolean inlineEmpty() {
            return inlineEmpty;
        }

        public List<Node> elements() {
            return elements;
        }

        public boolean scalarElements() {
            for (Node element : elements) {
                if (!(element instanceof ScalarNode)) {
                    return false;
                }
            }
            return true;
        }

        public AdminConfigValues.Kind elementKind() {
            for (Node element : elements) {
                if (element instanceof ScalarNode scalar) {
                    return scalar.kind();
                }
            }
            return AdminConfigValues.Kind.STRING;
        }

        public boolean elementQuoted() {
            for (Node element : elements) {
                if (element instanceof ScalarNode scalar) {
                    return scalar.quoted();
                }
            }
            return true;
        }
    }

    public static final class ScalarNode implements Node {
        private final String key;
        private final String keyToken;
        private final List<String> comments;
        private final int startLine;
        private final int endLine;
        private final int indent;
        private final String raw;
        private final AdminConfigValues.Kind kind;
        private final boolean quoted;
        private final boolean listElement;
        private final boolean block;
        private final String inlineComment;

        private ScalarNode(
                String key,
                String keyToken,
                List<String> comments,
                int startLine,
                int endLine,
                int indent,
                String raw,
                AdminConfigValues.Kind kind,
                boolean quoted,
                boolean listElement,
                boolean block,
                String inlineComment
        ) {
            this.key = key;
            this.keyToken = keyToken;
            this.comments = comments;
            this.startLine = startLine;
            this.endLine = endLine;
            this.indent = indent;
            this.raw = raw;
            this.kind = kind;
            this.quoted = quoted;
            this.listElement = listElement;
            this.block = block;
            this.inlineComment = inlineComment;
        }

        @Override
        public String key() {
            return key;
        }

        public String keyToken() {
            return keyToken;
        }

        @Override
        public List<String> comments() {
            return comments;
        }

        @Override
        public int startLine() {
            return startLine;
        }

        @Override
        public int endLine() {
            return endLine;
        }

        @Override
        public int indent() {
            return indent;
        }

        public String raw() {
            return raw;
        }

        public AdminConfigValues.Kind kind() {
            return kind;
        }

        public boolean quoted() {
            return quoted;
        }

        public boolean listElement() {
            return listElement;
        }

        public boolean block() {
            return block;
        }

        public String inlineComment() {
            return inlineComment;
        }
    }

    public sealed interface Edit permits ScalarEdit, ListEdit {
        List<String> path();

        static Edit scalar(List<String> path, String text) {
            return new ScalarEdit(List.copyOf(path), text);
        }

        static Edit list(List<String> path, List<String> items) {
            return new ListEdit(List.copyOf(path), List.copyOf(items));
        }
    }

    public record ScalarEdit(List<String> path, String text) implements Edit {
    }

    public record ListEdit(List<String> path, List<String> items) implements Edit {
    }

    private record Span(int start, int end, List<String> replacement) {
    }

    private static final class Parser {
        private final String fileName;
        private final List<String> lines;

        private Parser(String fileName, List<String> lines) {
            this.fileName = fileName;
            this.lines = lines;
        }

        private MapNode parseRoot() {
            List<Node> children = new ArrayList<>();
            int cursor = 0;
            while (cursor < lines.size()) {
                int content = nextContent(cursor);
                if (content < 0) {
                    break;
                }
                Node child = parseKeyAt(content);
                if (child.endLine() <= content) {
                    throw error(content, "parser did not advance");
                }
                children.add(child);
                cursor = child.endLine();
            }
            return new MapNode(null, null, List.of(), 0, lines.size(), -1, children);
        }

        private Node parseKeyAt(int lineIndex) {
            String line = lines.get(lineIndex);
            int indent = indentOf(line);
            int colon = keyColon(line, indent);
            if (colon < 0) {
                throw error(lineIndex, "expected a key");
            }
            String keyToken = line.substring(indent, colon).trim();
            String key = decodeKey(keyToken);
            String rawValue = line.substring(colon + 1);
            return finishValue(key, keyToken, commentsAbove(lineIndex), lineIndex, indent, rawValue, false);
        }

        private Node finishValue(
                String key,
                String keyToken,
                List<String> comments,
                int lineIndex,
                int indent,
                String rawValue,
                boolean keyIsListItem
        ) {
            String inline = inlineComment(rawValue);
            String value = withoutInlineComment(rawValue).trim();
            if (isBlockIndicator(value)) {
                return parseBlock(key, keyToken, comments, lineIndex, indent, value, inline);
            }
            if (startsQuoted(value) && unclosedQuote(rawValue)) {
                return parseWrappedQuote(key, keyToken, comments, lineIndex, indent, keyIsListItem);
            }
            if (value.equals("{}")) {
                return new MapNode(key, keyToken, comments, lineIndex, lineIndex + 1, indent, List.of());
            }
            if (value.equals("[]")) {
                return new ListNode(key, keyToken, comments, lineIndex, lineIndex + 1, indent,
                        lineIndex, "", true, List.of());
            }
            if (value.isEmpty() && inline == null) {
                int next = nextContent(lineIndex + 1);
                if (next >= 0) {
                    String nextTrim = lines.get(next).trim();
                    int nextIndent = indentOf(lines.get(next));
                    if (nextTrim.startsWith("-") && isListUnderKey(keyIsListItem, indent, nextIndent)) {
                        return parseList(key, keyToken, comments, lineIndex, indent, false);
                    }
                    if (!nextTrim.startsWith("-") && nextIndent > indent) {
                        return parseMap(key, keyToken, comments, lineIndex, indent);
                    }
                }
                return scalar(key, keyToken, comments, lineIndex, lineIndex + 1, indent,
                        "", AdminConfigValues.Kind.NULL, false, keyIsListItem, false, null);
            }
            AdminConfigValues.Decoded decoded = AdminConfigValues.decode(value);
            return scalar(key, keyToken, comments, lineIndex, lineIndex + 1, indent,
                    decoded.raw(), decoded.kind(), decoded.quoted(), keyIsListItem, false, inline);
        }

        private MapNode parseMap(String key, String keyToken, List<String> comments, int keyLine, int keyIndent) {
            List<Node> children = new ArrayList<>();
            int cursor = keyLine + 1;
            while (cursor < lines.size()) {
                int content = nextContent(cursor);
                if (content < 0) {
                    cursor = lines.size();
                    break;
                }
                if (indentOf(lines.get(content)) <= keyIndent || lines.get(content).trim().startsWith("-")) {
                    break;
                }
                Node child = parseKeyAt(content);
                if (child.endLine() <= content) {
                    throw error(content, "parser did not advance");
                }
                children.add(child);
                cursor = child.endLine();
            }
            return new MapNode(key, keyToken, comments, keyLine, cursor, keyIndent, children);
        }

        private ListNode parseList(
                String key,
                String keyToken,
                List<String> comments,
                int keyLine,
                int keyIndent,
                boolean inlineEmpty
        ) {
            List<Node> elements = new ArrayList<>();
            int cursor = keyLine + 1;
            int itemsStart = -1;
            String itemIndent = "";
            Integer itemColumn = null;
            while (cursor < lines.size()) {
                if (cursor < lines.size() && lines.get(cursor).trim().isEmpty()) {
                    cursor++;
                    continue;
                }
                if (cursor < lines.size() && lines.get(cursor).trim().startsWith("#")) {
                    int ahead = nextContent(cursor);
                    if (ahead >= 0 && isItemAt(ahead, itemColumn, keyIndent)) {
                        cursor = ahead;
                    } else {
                        break;
                    }
                }
                if (cursor >= lines.size() || !isItemAt(cursor, itemColumn, keyIndent)) {
                    break;
                }
                if (itemsStart < 0) {
                    itemsStart = cursor;
                    itemIndent = whitespace(lines.get(cursor));
                    itemColumn = indentOf(lines.get(cursor));
                }
                Node element = parseListItem(cursor);
                if (element.endLine() <= cursor) {
                    throw error(cursor, "parser did not advance");
                }
                elements.add(element);
                cursor = element.endLine();
            }
            if (itemsStart < 0) {
                itemsStart = keyLine + 1;
            }
            return new ListNode(key, keyToken, comments, keyLine, Math.max(cursor, keyLine + 1), keyIndent,
                    itemsStart, itemIndent, inlineEmpty, elements);
        }

        private boolean isItemAt(int lineIndex, Integer itemColumn, int keyIndent) {
            if (lineIndex < 0 || lineIndex >= lines.size()) {
                return false;
            }
            String trimmed = lines.get(lineIndex).trim();
            if (!trimmed.startsWith("-")) {
                return false;
            }
            int indent = indentOf(lines.get(lineIndex));
            if (itemColumn != null) {
                return indent == itemColumn;
            }
            return indent >= keyIndent;
        }

        private Node parseListItem(int dashLine) {
            String rest = afterDash(lines.get(dashLine));
            int dashIndent = indentOf(lines.get(dashLine));
            List<String> comments = commentsAbove(dashLine);
            if (rest.isEmpty()) {
                MapNode nested = parseMap(null, null, comments, dashLine, dashIndent);
                return nested;
            }
            if (isMapEntry(rest)) {
                int colon = keyColon(rest, 0);
                String keyToken = rest.substring(0, colon).trim();
                String key = decodeKey(keyToken);
                String rawValue = rest.substring(colon + 1);
                Node first = finishValue(key, keyToken, List.of(), dashLine, dashIndent, rawValue, true);
                List<Node> children = new ArrayList<>();
                children.add(first);
                int cursor = first.endLine();
                while (cursor < lines.size()) {
                    int content = nextContent(cursor);
                    if (content < 0) {
                        cursor = lines.size();
                        break;
                    }
                    int indent = indentOf(lines.get(content));
                    String trimmed = lines.get(content).trim();
                    if (indent <= dashIndent || trimmed.startsWith("-")) {
                        break;
                    }
                    Node more = parseKeyAt(content);
                    if (more.endLine() <= content) {
                        throw error(content, "parser did not advance");
                    }
                    children.add(more);
                    cursor = more.endLine();
                }
                return new MapNode(null, null, comments, dashLine, cursor, dashIndent, children);
            }
            return parseScalarItem(dashLine, rest, comments, dashIndent);
        }

        private ScalarNode parseScalarItem(int dashLine, String rest, List<String> comments, int dashIndent) {
            if (startsQuoted(rest) && unclosedQuote(rest)) {
                int end = dashLine;
                StringBuilder block = new StringBuilder(rest);
                while (unclosedQuote(block.toString()) && end + 1 < lines.size()) {
                    end++;
                    block.append('\n').append(lines.get(end).trim());
                }
                if (unclosedQuote(block.toString())) {
                    throw error(dashLine, "unclosed quote");
                }
                String raw = AdminConfigValues.foldQuoted(block.toString());
                return scalar(null, null, comments, dashLine, end + 1, dashIndent,
                        raw, AdminConfigValues.Kind.STRING, true, true, false, null);
            }
            String inline = inlineComment(rest);
            String value = withoutInlineComment(rest).trim();
            AdminConfigValues.Decoded decoded = AdminConfigValues.decode(value);
            return scalar(null, null, comments, dashLine, dashLine + 1, dashIndent,
                    decoded.raw(), decoded.kind(), decoded.quoted(), true, false, inline);
        }

        private ScalarNode parseWrappedQuote(
                String key,
                String keyToken,
                List<String> comments,
                int lineIndex,
                int indent,
                boolean listElement
        ) {
            int end = lineIndex;
            StringBuilder block = new StringBuilder(lines.get(lineIndex).substring(keyColon(lines.get(lineIndex), indent) + 1));
            while (unclosedQuote(block.toString()) && end + 1 < lines.size()) {
                end++;
                block.append('\n').append(lines.get(end));
            }
            if (unclosedQuote(block.toString())) {
                throw error(lineIndex, "unclosed quote");
            }
            String raw = AdminConfigValues.foldQuoted(block.toString());
            return scalar(key, keyToken, comments, lineIndex, end + 1, indent,
                    raw, AdminConfigValues.Kind.STRING, true, listElement, false, null);
        }

        private ScalarNode parseBlock(
                String key,
                String keyToken,
                List<String> comments,
                int lineIndex,
                int indent,
                String indicator,
                String inline
        ) {
            boolean strip = indicator.contains("-");
            int cursor = lineIndex + 1;
            int contentIndent = -1;
            List<String> content = new ArrayList<>();
            while (cursor < lines.size()) {
                String line = lines.get(cursor);
                if (line.trim().isEmpty()) {
                    content.add("");
                    cursor++;
                    continue;
                }
                int lineIndent = indentOf(line);
                if (lineIndent <= indent) {
                    break;
                }
                if (contentIndent < 0) {
                    contentIndent = lineIndent;
                }
                if (lineIndent < contentIndent) {
                    break;
                }
                content.add(line.substring(contentIndent));
                cursor++;
            }
            while (!content.isEmpty() && content.get(content.size() - 1).isEmpty() && strip) {
                content.remove(content.size() - 1);
            }
            String raw = String.join("\n", content);
            return scalar(key, keyToken, comments, lineIndex, cursor, indent,
                    raw, AdminConfigValues.Kind.BLOCK, false, false, true, inline);
        }

        private ScalarNode scalar(
                String key,
                String keyToken,
                List<String> comments,
                int start,
                int end,
                int indent,
                String raw,
                AdminConfigValues.Kind kind,
                boolean quoted,
                boolean listElement,
                boolean block,
                String inline
        ) {
            return new ScalarNode(key, keyToken, comments, start, end, indent, raw, kind, quoted,
                    listElement, block, inline);
        }

        private int nextContent(int from) {
            for (int index = from; index < lines.size(); index++) {
                String trimmed = lines.get(index).trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                return index;
            }
            return -1;
        }

        private List<String> commentsAbove(int lineIndex) {
            int start = lineIndex;
            while (start > 0 && lines.get(start - 1).trim().startsWith("#")) {
                start--;
            }
            List<String> comments = new ArrayList<>();
            for (int index = start; index < lineIndex; index++) {
                String trimmed = lines.get(index).trim();
                if (trimmed.startsWith("#")) {
                    comments.add(trimmed.substring(1).trim());
                }
            }
            return List.copyOf(comments);
        }

        private IllegalArgumentException error(int lineIndex, String message) {
            String preview = lineIndex >= 0 && lineIndex < lines.size() ? lines.get(lineIndex).trim() : "";
            return new IllegalArgumentException(fileName + " line " + (lineIndex + 1) + ": " + message
                    + (preview.isEmpty() ? "" : " (" + preview + ")"));
        }
    }

    private static boolean isListUnderKey(boolean keyIsListItem, int keyIndent, int nextIndent) {
        return keyIsListItem ? nextIndent > keyIndent : nextIndent >= keyIndent;
    }

    private static boolean isBlockIndicator(String value) {
        return value.startsWith("|") || value.startsWith(">");
    }

    private static boolean startsQuoted(String value) {
        return value.startsWith("'") || value.startsWith("\"");
    }

    private static boolean isMapEntry(String rest) {
        if (rest.startsWith("'") || rest.startsWith("\"") || rest.startsWith("[") || rest.startsWith("{")) {
            return false;
        }
        int colon = keyColon(rest, 0);
        if (colon <= 0) {
            return false;
        }
        String key = rest.substring(0, colon).trim();
        return !key.isEmpty() && !key.contains(" ");
    }

    private static String afterDash(String line) {
        String trimmed = line.trim();
        if (!trimmed.startsWith("-")) {
            return trimmed;
        }
        return trimmed.substring(1).trim();
    }

    private static int indentOf(String line) {
        int index = 0;
        while (index < line.length() && line.charAt(index) == ' ') {
            index++;
        }
        return index;
    }

    private static String whitespace(String line) {
        return line.substring(0, indentOf(line));
    }

    static int keyColon(String line, int indent) {
        if (line == null || indent > line.length()) {
            return -1;
        }
        int index = Math.max(0, indent);
        if (index < line.length() && (line.charAt(index) == '"' || line.charAt(index) == '\'')) {
            char quote = line.charAt(index);
            index++;
            while (index < line.length()) {
                if (line.charAt(index) == quote) {
                    if (quote == '\'' && index + 1 < line.length() && line.charAt(index + 1) == '\'') {
                        index += 2;
                        continue;
                    }
                    index++;
                    break;
                }
                if (quote == '"' && line.charAt(index) == '\\' && index + 1 < line.length()) {
                    index += 2;
                    continue;
                }
                index++;
            }
            while (index < line.length() && line.charAt(index) == ' ') {
                index++;
            }
            return index < line.length() && line.charAt(index) == ':' ? index : -1;
        }
        int colon = line.indexOf(':', index);
        return colon <= index ? -1 : colon;
    }

    static String decodeKey(String token) {
        if (token.length() >= 2 && ((token.startsWith("\"") && token.endsWith("\""))
                || (token.startsWith("'") && token.endsWith("'")))) {
            return AdminConfigValues.unquote(token);
        }
        return token;
    }

    private static String inlineComment(String rawValue) {
        String stripped = withoutInlineComment(rawValue);
        if (stripped.length() == rawValue.length()) {
            return null;
        }
        String comment = rawValue.substring(stripped.length()).trim();
        return comment.isEmpty() ? null : comment;
    }

    private static String withoutInlineComment(String rawValue) {
        boolean single = false;
        boolean doubled = false;
        boolean escaped = false;
        for (int index = 0; index < rawValue.length(); index++) {
            char current = rawValue.charAt(index);
            if (current == '"' && !single && !escaped) {
                doubled = !doubled;
            } else if (current == '\'' && !doubled) {
                single = !single;
            } else if (current == '#' && !single && !doubled
                    && (index == 0 || Character.isWhitespace(rawValue.charAt(index - 1)))) {
                return rawValue.substring(0, index);
            }
            escaped = current == '\\' && doubled && !escaped;
            if (current != '\\') {
                escaped = false;
            }
        }
        return rawValue;
    }

    private static boolean unclosedQuote(String rawValue) {
        String trimmed = rawValue.stripLeading();
        if (!trimmed.startsWith("\"") && !trimmed.startsWith("'")) {
            return false;
        }
        boolean single = false;
        boolean doubled = false;
        boolean escaped = false;
        for (int index = 0; index < rawValue.length(); index++) {
            char current = rawValue.charAt(index);
            if (current == '"' && !single && !escaped) {
                doubled = !doubled;
            } else if (current == '\'' && !doubled) {
                if (single && index + 1 < rawValue.length() && rawValue.charAt(index + 1) == '\'') {
                    index++;
                } else {
                    single = !single;
                }
            }
            escaped = current == '\\' && doubled && !escaped;
            if (current != '\\') {
                escaped = false;
            }
        }
        return single || doubled;
    }
}
