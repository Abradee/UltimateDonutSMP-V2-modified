package com.bx.ultimateDonutSmp2.dialogs.screens;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.admin.AdminConfigCatalog;
import com.bx.ultimateDonutSmp2.admin.AdminConfigDocument;
import com.bx.ultimateDonutSmp2.admin.AdminConfigEditException;
import com.bx.ultimateDonutSmp2.admin.AdminConfigGuide;
import com.bx.ultimateDonutSmp2.admin.AdminConfigPages;
import com.bx.ultimateDonutSmp2.admin.AdminConfigStore;
import com.bx.ultimateDonutSmp2.admin.AdminConfigValues;
import com.bx.ultimateDonutSmp2.dialogs.DialogConfig;
import com.bx.ultimateDonutSmp2.dialogs.DialogFactory;
import com.bx.ultimateDonutSmp2.dialogs.DialogScreen;
import com.bx.ultimateDonutSmp2.dialogs.DialogSession;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import io.papermc.paper.dialog.DialogResponseView;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * In-game editor for the plugin yaml files. Language files are not offered: they hold thousands
 * of keys and stay on disk. Saving writes the file and reloads the plugin, so {@code /uds reload}
 * is not required after a change made here.
 */
public final class AdminConfigDialog extends DialogScreen {

    static final String OPEN = "uds2a";
    private static final String HOME = "uds2a_home";
    private static final String BACK = "uds2a_back";
    private static final String SAVE = "uds2a_save";
    private static final String SEARCH = "uds2a_search";
    private static final String CLEAR = "uds2a_clear";
    private static final String ADD = "uds2a_add";
    private static final String CLOSE = "uds2a_close";
    private static final String PREVIEW = "uds2a_preview";
    private static final String LEAVE_SAVE = "uds2a_leave_save";
    private static final String LEAVE_DISCARD = "uds2a_leave_discard";
    private static final String LEAVE_STAY = "uds2a_leave_stay";
    private static final String PAGE = "uds2a_page_";
    private static final String OPEN_CHILD = "uds2a_open_";
    private static final String SEARCH_INPUT = "q";
    private static final int TEXT_LIMIT = 1024;

    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    public AdminConfigDialog(UltimateDonutSmp2 plugin, DialogConfig config, DialogSession.Store sessions) {
        super(plugin, config, sessions);
    }

    public boolean open(Player player) {
        Session session = state(player);
        session.file = null;
        session.path = List.of();
        session.page = 0;
        session.query = "";
        session.notice = null;
        session.error = null;
        return render(player);
    }

    public void forget(UUID playerId) {
        sessions.remove(playerId);
    }

    public void clear() {
        sessions.clear();
    }

    @Override
    public boolean handle(Player player, String action, DialogResponseView response) {
        if (action == null || (!action.equals(OPEN) && !action.startsWith("uds2a_"))) {
            return false;
        }
        Session session = state(player);
        if (!session.confirming) {
            capture(player, response);
        }
        if (!SAVE.equals(action) && !LEAVE_SAVE.equals(action)) {
            session.notice = null;
            session.error = null;
        }
        if (LEAVE_STAY.equals(action)) {
            session.confirming = false;
            session.pendingLeave = null;
        } else if (LEAVE_DISCARD.equals(action)) {
            discardEdits(session);
            session.confirming = false;
            if (finishLeave(player, session)) {
                return true;
            }
        } else if (LEAVE_SAVE.equals(action)) {
            session.confirming = false;
            save(player, session);
            if (session.error != null) {
                session.pendingLeave = null;
            } else if (finishLeave(player, session)) {
                return true;
            }
        } else if (OPEN.equals(action) || HOME.equals(action)) {
            session.file = null;
            session.path = List.of();
            session.page = 0;
            session.query = "";
        } else if (BACK.equals(action)) {
            if (askBeforeLeaving(player, session, BACK)) {
                return true;
            }
            goBack(session);
        } else if (SAVE.equals(action)) {
            save(player, session);
        } else if (PREVIEW.equals(action)) {
            session.previewRequested = true;
        } else if (SEARCH.equals(action)) {
            session.query = text(response, SEARCH_INPUT);
            session.page = 0;
        } else if (CLEAR.equals(action)) {
            session.query = "";
            session.page = 0;
        } else if (ADD.equals(action)) {
            addLine(session);
        } else if (CLOSE.equals(action)) {
            if (askBeforeLeaving(player, session, CLOSE)) {
                return true;
            }
            DialogSupport.close(player);
            return true;
        } else if (action.startsWith(PAGE)) {
            session.page = parseIndex(action.substring(PAGE.length()));
        } else if (action.startsWith(OPEN_CHILD)) {
            openTarget(session, parseIndex(action.substring(OPEN_CHILD.length())));
        }
        render(player);
        return true;
    }

    /** True when a confirm screen was opened and the caller should not keep navigating. */
    private boolean askBeforeLeaving(Player player, Session session, String action) {
        if (session.file == null || !dirty(session)) {
            return false;
        }
        session.pendingLeave = action;
        session.confirming = true;
        String name = AdminConfigCatalog.displayName(session.file);
        DialogFactory.Screen screen = screen(player)
                .title("&eUnsaved changes")
                .externalTitle("Unsaved changes")
                .columns(1)
                .canCloseWithEscape(false)
                .text("&7You edited &f" + name + "&7 but have not pressed Save changes.")
                .text("&7Save the file, discard the edits, or stay on this page.")
                .button("&aSave changes", "&7Write the file and reload, then leave this page.", 280, LEAVE_SAVE)
                .button("&cDiscard", "&7Throw away the edits that were not saved.", 280, LEAVE_DISCARD)
                .button("&fStay", "&7Go back to the options you were editing.", 280, LEAVE_STAY);
        if (!show(player, screen.build())) {
            session.confirming = false;
            session.pendingLeave = null;
            return false;
        }
        return true;
    }

    /** @return true when the dialog was closed and should not be redrawn */
    private boolean finishLeave(Player player, Session session) {
        String action = session.pendingLeave;
        session.pendingLeave = null;
        if (CLOSE.equals(action)) {
            DialogSupport.close(player);
            return true;
        }
        if (BACK.equals(action)) {
            goBack(session);
        }
        return false;
    }

    private void discardEdits(Session session) {
        if (session.file != null) {
            session.drafts.remove(session.file);
        }
        session.notice = "Changes discarded.";
        session.error = null;
    }

    private void goBack(Session session) {
        session.page = 0;
        session.query = "";
        if (session.path != null && !session.path.isEmpty()) {
            session.path = session.path.subList(0, session.path.size() - 1);
            return;
        }
        session.file = null;
        session.path = List.of();
    }

    private void openTarget(Session session, int index) {
        View view = session.view;
        if (view == null || index < 0 || index >= view.targets.size()) {
            return;
        }
        Target target = view.targets.get(index);
        session.file = target.file;
        session.path = target.path;
        session.page = 0;
        session.query = "";
    }

    private boolean render(Player player) {
        Session session = state(player);
        if (session.file == null) {
            return renderHome(player, session);
        }
        try {
            AdminConfigDocument document = AdminConfigStore.load(plugin, session.file);
            AdminConfigDocument.Node node = document.resolve(session.path);
            if (!(node instanceof AdminConfigDocument.MapNode) && !(node instanceof AdminConfigDocument.ListNode)
                    && !(node instanceof AdminConfigDocument.ScalarNode)) {
                session.error = "That section is not in " + AdminConfigCatalog.displayName(session.file) + " anymore.";
                session.file = null;
                session.path = List.of();
                return renderHome(player, session);
            }
            if (node instanceof AdminConfigDocument.ListNode list && list.scalarElements()) {
                return renderScalarList(player, session, document, list);
            }
            if (node instanceof AdminConfigDocument.ScalarNode scalar) {
                return renderScalar(player, session, scalar);
            }
            return renderChildren(player, session, document, node);
        } catch (RuntimeException | IOException exception) {
            plugin.getLogger().log(Level.WARNING, "Could not open /uds2a for " + session.file, exception);
            session.error = "Could not open that file. Check the console.";
            session.file = null;
            session.path = List.of();
            return renderHome(player, session);
        }
    }

    private boolean renderHome(Player player, Session session) {
        List<AdminConfigCatalog.FileInfo> matched = new ArrayList<>();
        for (AdminConfigCatalog.FileInfo file : AdminConfigCatalog.files()) {
            String haystack = file.name() + " " + AdminConfigCatalog.displayName(file.name())
                    + " " + file.summary() + " " + file.example();
            if (AdminConfigPages.matches(session.query, haystack)) {
                matched.add(file);
            }
        }
        int pageCount = pages(matched.size(), AdminConfigPages.BUTTONS_PER_PAGE);
        session.page = clamp(session.page, pageCount);
        int start = session.page * AdminConfigPages.BUTTONS_PER_PAGE;
        List<AdminConfigCatalog.FileInfo> window = matched.subList(
                Math.min(start, matched.size()),
                Math.min(matched.size(), start + AdminConfigPages.BUTTONS_PER_PAGE)
        );

        DialogFactory.Screen screen = base(player, "Config", session)
                .columns(2)
                .text("&7Edit a config file here. Buttons open a section. Inputs edit a value.")
                .text("&7Each one shows what it does and an example. Save changes writes the file and reloads it.")
                .text("&7Editing the yaml file by hand still works. Language files are not listed — edit languages/ in the folder, those files have thousands of keys.")
                .text("&8Search example: &fshop")
                .textInput(SEARCH_INPUT, "&fSearch", session.query, 48, false);

        screen.button("&bRank setup",
                "&7Creates +, ++, and +++ in LuckPerms so the rank shows in the tab and scoreboard.",
                150,
                "uds2a_ranks");

        View view = new View(null, List.of(), false, null, List.of());
        int index = 0;
        for (AdminConfigCatalog.FileInfo file : window) {
            screen.button("&f" + AdminConfigCatalog.displayName(file.name()), AdminConfigCatalog.tooltip(file), 150, OPEN_CHILD + index);
            view.targets.add(new Target(file.name(), List.of()));
            index++;
        }
        if (window.isEmpty()) {
            screen.text("&7No files match. Example: shop");
        }
        navigation(screen, session, pageCount, false, false);
        session.view = view;
        return show(player, screen.build());
    }

    private boolean renderChildren(Player player, Session session, AdminConfigDocument document, AdminConfigDocument.Node node) {
        List<Slot> inputs = new ArrayList<>();
        List<Target> sections = new ArrayList<>();
        List<String> sectionLabels = new ArrayList<>();
        List<String> sectionTips = new ArrayList<>();
        if (node instanceof AdminConfigDocument.MapNode map) {
            for (AdminConfigDocument.Node child : map.children()) {
                if (child.key() == null) {
                    continue;
                }
                consider(session, inputs, sections, sectionLabels, sectionTips, child, append(session.path, child.key()));
            }
        } else if (node instanceof AdminConfigDocument.ListNode list) {
            for (int index = 0; index < list.elements().size(); index++) {
                AdminConfigDocument.Node element = list.elements().get(index);
                String label = entryLabel(element, index);
                String tip = entryTip(element, index);
                if (!AdminConfigPages.matches(session.query, label + " " + tip + " " + preview(element))) {
                    continue;
                }
                sections.add(new Target(session.file, append(session.path, Integer.toString(index))));
                sectionLabels.add(label);
                sectionTips.add(tip);
            }
        }

        List<Integer> shownSections = new ArrayList<>();
        for (int index = 0; index < sections.size(); index++) {
            shownSections.add(index);
        }
        int pageCount = Math.max(
                pages(inputs.size(), AdminConfigPages.INPUTS_PER_PAGE),
                pages(shownSections.size(), AdminConfigPages.BUTTONS_PER_PAGE)
        );
        session.page = clamp(session.page, pageCount);
        List<Slot> inputWindow = window(inputs, session.page, AdminConfigPages.INPUTS_PER_PAGE);
        List<Integer> buttonWindow = window(shownSections, session.page, AdminConfigPages.BUTTONS_PER_PAGE);

        DialogFactory.Screen screen = base(player, titleFor(session), session)
                .columns(1)
                .text("&7" + breadcrumb(session))
                .text("&7" + sectionIntro(session, node))
                .text(dirty(session) ? "&eUnsaved edits. Press Save changes." : "&7Press Save changes when you are done.")
                .text("&8Search example: &fprice")
                .textInput(SEARCH_INPUT, "&fSearch", session.query, 48, false);

        boolean anyColor = false;
        for (Slot slot : inputWindow) {
            if (!slot.editable() || slot.bool()) {
                continue;
            }
            String name = slot.path().isEmpty()
                    ? "Value"
                    : AdminConfigGuide.humanize(slot.path().get(slot.path().size() - 1));
            String preview = AdminConfigGuide.colorPreview(name, slot.initial());
            if (preview != null) {
                if (!anyColor) {
                    screen.text("&7Color preview");
                }
                anyColor = true;
                screen.text(preview);
            }
        }
        if (session.previewRequested && !anyColor) {
            screen.text("&7No color codes on this page. Example: &aHello");
        }
        session.previewRequested = false;

        List<Field> fields = new ArrayList<>();
        int inputIndex = 0;
        for (Slot slot : inputWindow) {
            if (!slot.editable) {
                screen.text(slot.note);
                continue;
            }
            String inputId = "i" + inputIndex++;
            fields.add(new Field(inputId, slot.path, pathKey(slot.path), slot.bool, -1));
            if (slot.bool) {
                screen.boolInput(inputId, slot.label, slot.checked);
            } else {
                screen.textInput(inputId, slot.label, slot.initial, TEXT_LIMIT, slot.multiline);
            }
        }
        View view = new View(session.file, session.path, false, null, fields);
        int buttonIndex = 0;
        for (Integer sectionIndex : buttonWindow) {
            screen.button("&f" + clip(sectionLabels.get(sectionIndex), 40), sectionTips.get(sectionIndex), 280,
                    OPEN_CHILD + buttonIndex);
            view.targets.add(sections.get(sectionIndex));
            buttonIndex++;
        }
        if (inputWindow.isEmpty() && buttonWindow.isEmpty()) {
            screen.text(session.query == null || session.query.isBlank()
                    ? "&7This section has no options yet."
                    : "&7Nothing in this section matches. Example: title");
        }
        navigation(screen, session, pageCount, true, false);
        session.view = view;
        return show(player, screen.build());
    }

    private boolean renderScalarList(
            Player player,
            Session session,
            AdminConfigDocument document,
            AdminConfigDocument.ListNode list
    ) {
        String listKey = pathKey(session.path);
            List<String> values = currentList(document, session.drafts.get(session.file), listKey, list);
        List<Integer> matched = new ArrayList<>();
        for (int index = 0; index < values.size(); index++) {
            if (AdminConfigPages.matches(session.query, values.get(index))) {
                matched.add(index);
            }
        }
        int pageCount = pages(matched.size(), AdminConfigPages.INPUTS_PER_PAGE);
        session.page = clamp(session.page, pageCount);
        List<Integer> window = window(matched, session.page, AdminConfigPages.INPUTS_PER_PAGE);
        AdminConfigValues.Kind kind = list.elementKind();

        DialogFactory.Screen screen = base(player, titleFor(session), session)
                .columns(1)
                .text("&7" + breadcrumb(session))
                .text("&7One value per line. Leave a box empty to remove it, then press Save changes.")
                .text("&8Example: &f" + AdminConfigGuide.example(list.key() == null ? "line" : list.key(), list.comments(), kind,
                        values.isEmpty() ? null : values.get(0)))
                .textInput(SEARCH_INPUT, "&fSearch", session.query, 48, false);

        boolean anyColor = false;
        for (Integer lineIndex : window) {
            String preview = AdminConfigGuide.colorPreview("Line " + (lineIndex + 1), values.get(lineIndex));
            if (preview != null) {
                if (!anyColor) {
                    screen.text("&7Color preview");
                }
                anyColor = true;
                screen.text(preview);
            }
        }
        if (session.previewRequested && !anyColor) {
            screen.text("&7No color codes on this page. Example: &aHello");
        }
        session.previewRequested = false;

        List<Field> fields = new ArrayList<>();
        int inputIndex = 0;
        for (Integer lineIndex : window) {
            String inputId = "i" + inputIndex++;
            String value = values.get(lineIndex);
            fields.add(new Field(inputId, session.path, listKey, false, lineIndex));
            if (value.length() > TEXT_LIMIT) {
                screen.text("&7Line " + (lineIndex + 1) + " is too long for the dialog. Edit it in the file.");
                continue;
            }
            screen.textInput(inputId, AdminConfigGuide.listLineLabel(lineIndex + 1, kind, value), value, TEXT_LIMIT, false);
        }
        if (window.isEmpty()) {
            screen.text(values.isEmpty()
                    ? "&7This list is empty. Add a line, then press Save changes."
                    : "&7No lines match. Example: diamond");
        }
        View view = new View(session.file, session.path, true, listKey, fields);
        session.view = view;
        navigation(screen, session, pageCount, true, true);
        return show(player, screen.build());
    }

    private boolean renderScalar(Player player, Session session, AdminConfigDocument.ScalarNode scalar) {
        Slot slot = slotFor(session, scalar, session.path);
        DialogFactory.Screen screen = base(player, titleFor(session), session)
                .columns(1)
                .text("&7" + breadcrumb(session))
                .text("&8Example: &f" + AdminConfigGuide.example(scalar.key(), scalar.comments(), scalar.kind(), scalar.raw()));
        String preview = AdminConfigGuide.colorPreview(
                scalar.key() == null ? "Value" : AdminConfigGuide.humanize(scalar.key()),
                slot.initial());
        if (preview != null) {
            screen.text("&7Color preview");
            screen.text(preview);
        } else if (session.previewRequested) {
            screen.text("&7No color codes on this page. Example: &aHello");
        }
        session.previewRequested = false;
        List<Field> fields = new ArrayList<>();
        if (slot.editable) {
            fields.add(new Field("i0", slot.path, pathKey(slot.path), slot.bool, -1));
            if (slot.bool) {
                screen.boolInput("i0", slot.label, slot.checked);
            } else {
                screen.textInput("i0", slot.label, slot.initial, TEXT_LIMIT, slot.multiline);
            }
        } else {
            screen.text(slot.note);
        }
        session.view = new View(session.file, session.path, false, null, fields);
        navigation(screen, session, 1, true, false);
        return show(player, screen.build());
    }

    private void consider(
            Session session,
            List<Slot> inputs,
            List<Target> sections,
            List<String> labels,
            List<String> tips,
            AdminConfigDocument.Node child,
            List<String> path
    ) {
        if (child instanceof AdminConfigDocument.ScalarNode scalar) {
            if (!AdminConfigPages.matches(session.query, AdminConfigPages.searchText(scalar.key(), scalar.comments(), scalar.raw()))) {
                return;
            }
            inputs.add(slotFor(session, scalar, path));
            return;
        }
        String key = child.key() == null ? "entry" : child.key();
        StringBuilder haystack = new StringBuilder(AdminConfigPages.searchText(key, child.comments(), null));
        if (child instanceof AdminConfigDocument.MapNode map) {
            for (AdminConfigDocument.Node nested : map.children()) {
                if (nested.key() != null) {
                    haystack.append(' ').append(nested.key());
                }
            }
        } else if (child instanceof AdminConfigDocument.ListNode list) {
            haystack.append(' ').append(list.elements().size()).append(" entries");
        }
        if (!AdminConfigPages.matches(session.query, haystack.toString())) {
            return;
        }
        sections.add(new Target(session.file, path));
        labels.add(AdminConfigGuide.humanize(key));
        List<String> childKeys = new ArrayList<>();
        if (child instanceof AdminConfigDocument.MapNode map) {
            for (AdminConfigDocument.Node nested : map.children()) {
                if (nested.key() != null) {
                    childKeys.add(nested.key());
                }
            }
        } else if (child instanceof AdminConfigDocument.ListNode list) {
            childKeys.add(list.elements().size() + " entries");
        }
        tips.add(AdminConfigGuide.sectionTooltip(key, child.comments(), childKeys));
    }

    private Slot slotFor(Session session, AdminConfigDocument.ScalarNode scalar, List<String> path) {
        String current = drafted(session, path, scalar);
        String label = AdminConfigGuide.inputLabel(scalar.key() == null ? "Value" : scalar.key(), scalar.comments(),
                scalar.kind(), current);
        if (scalar.kind() != AdminConfigValues.Kind.BOOLEAN && current.length() > TEXT_LIMIT) {
            return Slot.note("&7" + AdminConfigGuide.humanize(scalar.key())
                    + " is too long for the dialog (" + current.length()
                    + " characters). Edit it in the file.\n&8Example: &f"
                    + AdminConfigGuide.example(scalar.key(), scalar.comments(), scalar.kind(), null));
        }
        boolean multiline = scalar.block() || current.indexOf('\n') >= 0;
        boolean checked = "true".equalsIgnoreCase(current);
        return Slot.editable(path, label, scalar.kind() == AdminConfigValues.Kind.BOOLEAN, checked, current, multiline);
    }

    private void navigation(DialogFactory.Screen screen, Session session, int pageCount, boolean fileScreen, boolean list) {
        if (fileScreen) {
            screen.button("&aSave changes", "&7Writes this file and reloads the plugin.\n&8Example: &fchange a price, then click here.",
                    280, SAVE);
            screen.button("&bPreview", "&7Shows color codes on the label without saving.\n&8Example: &aHello &for &#A303F9Shard",
                    280, PREVIEW);
        }
        if (list) {
            screen.button("&fAdd line", "&7Adds one entry to this list.\n&8Example: &fDIAMOND", 140, ADD);
        }
        if (pageCount > 1) {
            screen.text("&7Page " + (session.page + 1) + " of " + pageCount);
            if (session.page > 0) {
                screen.button("&fPrevious", "&7Previous page of options.", 140, PAGE + (session.page - 1));
            }
            if (session.page + 1 < pageCount) {
                screen.button("&fNext", "&7Next page of options.", 140, PAGE + (session.page + 1));
            }
        }
        screen.button("&fSearch", "&7Apply the search box.\n&8Example: &fprice", 140, SEARCH);
        if (session.query != null && !session.query.isBlank()) {
            screen.button("&fShow all", "&7Clear the search and show every option.", 140, CLEAR);
        }
        if (fileScreen) {
            screen.button("&fBack", "&7Go back one section.", 140, BACK);
        }
        screen.button("&fClose", "&7Close this screen. Unsaved edits stay until you leave.", 140, CLOSE);
    }

    private DialogFactory.Screen base(Player player, String title, Session session) {
        DialogFactory.Screen screen = screen(player)
                .title("&f" + title)
                .externalTitle(title)
                .columns(1)
                .canCloseWithEscape(session.file == null || !dirty(session));
        if (session.error != null && !session.error.isBlank()) {
            screen.text("&c" + session.error);
        }
        if (session.notice != null && !session.notice.isBlank()) {
            screen.text("&a" + session.notice);
        }
        return screen;
    }

    private void save(Player player, Session session) {
        if (session.file == null) {
            session.error = "Open a file before saving.";
            return;
        }
        if (session.saving) {
            return;
        }
        Draft draft = session.drafts.get(session.file);
        if (draft == null || (draft.scalars.isEmpty() && draft.lists.isEmpty())) {
            session.notice = "No changes to save.";
            session.error = null;
            return;
        }
        session.saving = true;
        try {
            AdminConfigDocument document = AdminConfigStore.load(plugin, session.file);
            List<AdminConfigDocument.Edit> edits = new ArrayList<>();
            for (Map.Entry<String, String> entry : draft.scalars.entrySet()) {
                edits.add(AdminConfigDocument.Edit.scalar(pathOf(entry.getKey()), entry.getValue()));
            }
            for (Map.Entry<String, List<String>> entry : draft.lists.entrySet()) {
                edits.add(AdminConfigDocument.Edit.list(pathOf(entry.getKey()), entry.getValue()));
            }
            String updated = document.apply(edits);
            AdminConfigStore.save(plugin, session.file, updated);
            try {
                plugin.reloadAllPluginConfigurations();
                String shown = AdminConfigCatalog.displayName(session.file);
                session.notice = "Saved " + shown + " and reloaded.";
                session.error = null;
                player.sendMessage(ColorUtils.toComponent("&aSaved &f" + shown + "&a and reloaded."));
            } catch (RuntimeException exception) {
                plugin.getLogger().log(Level.SEVERE, "Saved " + session.file + " from /uds2a but reload failed", exception);
                session.notice = null;
                session.error = "Saved " + AdminConfigCatalog.displayName(session.file) + ", but reload failed. Run /uds reload.";
            }
            session.drafts.remove(session.file);
        } catch (AdminConfigEditException exception) {
            session.error = exception.getMessage();
            session.notice = null;
        } catch (RuntimeException | IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + session.file + " from /uds2a", exception);
            session.error = "Could not save " + AdminConfigCatalog.displayName(session.file) + ". Check the console.";
            session.notice = null;
        } finally {
            session.saving = false;
        }
    }

    private void capture(Player player, DialogResponseView response) {
        Session session = state(player);
        View view = session.view;
        if (view == null || view.file == null || response == null || view.fields.isEmpty()) {
            return;
        }
        AdminConfigDocument document;
        try {
            document = AdminConfigStore.load(plugin, view.file);
        } catch (IOException exception) {
            return;
        }
        Draft draft = session.drafts.computeIfAbsent(view.file, ignored -> new Draft());
        if (view.scalarList) {
            captureList(document, draft, view, response);
            return;
        }
        for (Field field : view.fields) {
            String typed = field.bool ? boolText(response, field.inputId) : response.getText(field.inputId);
            if (typed == null) {
                continue;
            }
            if (!(document.resolve(field.path) instanceof AdminConfigDocument.ScalarNode scalar)) {
                continue;
            }
            if (same(scalar, typed)) {
                draft.scalars.remove(field.key);
            } else {
                draft.scalars.put(field.key, typed);
            }
        }
    }

    private void captureList(AdminConfigDocument document, Draft draft, View view, DialogResponseView response) {
        if (!(document.resolve(view.path) instanceof AdminConfigDocument.ListNode list) || !list.scalarElements()) {
            return;
        }
        List<String> working = currentList(document, draft, view.listPathKey, list);
        for (Field field : view.fields) {
            if (field.listIndex < 0) {
                continue;
            }
            String typed = response.getText(field.inputId);
            if (typed == null) {
                continue;
            }
            while (working.size() <= field.listIndex) {
                working.add("");
            }
            working.set(field.listIndex, typed);
        }
        if (sameList(fileValues(list), working)) {
            draft.lists.remove(view.listPathKey);
        } else {
            draft.lists.put(view.listPathKey, working);
        }
    }

    private void addLine(Session session) {
        View view = session.view;
        if (view == null || !view.scalarList || view.file == null) {
            return;
        }
        try {
            AdminConfigDocument document = AdminConfigStore.load(plugin, view.file);
            if (!(document.resolve(view.path) instanceof AdminConfigDocument.ListNode list)) {
                return;
            }
            Draft draft = session.drafts.computeIfAbsent(view.file, ignored -> new Draft());
            List<String> working = currentList(document, draft, view.listPathKey, list);
            String sample = AdminConfigGuide.example(
                    list.key() == null ? "line" : list.key(),
                    list.comments(),
                    list.elementKind(),
                    working.isEmpty() ? null : working.get(0)
            );
            if (sample.startsWith("leave empty") || sample.startsWith("Any ")) {
                sample = "";
            }
            working.add(sample);
            draft.lists.put(view.listPathKey, working);
            session.page = Math.max(0, pages(working.size(), AdminConfigPages.INPUTS_PER_PAGE) - 1);
            session.query = "";
        } catch (IOException exception) {
            session.error = "Could not add a line. Check the console.";
        }
    }

    private List<String> currentList(AdminConfigDocument document, Draft draft, String listKey, AdminConfigDocument.ListNode list) {
        if (draft != null) {
            List<String> edited = draft.lists.get(listKey);
            if (edited != null) {
                return new ArrayList<>(edited);
            }
        }
        if (document != null && document.resolve(pathOf(listKey)) instanceof AdminConfigDocument.ListNode resolved
                && resolved.scalarElements()) {
            return fileValues(resolved);
        }
        return fileValues(list);
    }

    private static List<String> fileValues(AdminConfigDocument.ListNode list) {
        List<String> values = new ArrayList<>();
        for (AdminConfigDocument.Node element : list.elements()) {
            if (element instanceof AdminConfigDocument.ScalarNode scalar) {
                values.add(scalar.raw());
            }
        }
        return values;
    }

    private String drafted(Session session, List<String> path, AdminConfigDocument.ScalarNode scalar) {
        Draft draft = session.drafts.get(session.file);
        if (draft != null) {
            String edited = draft.scalars.get(pathKey(path));
            if (edited != null) {
                return edited;
            }
        }
        return scalar.raw() == null ? "" : scalar.raw();
    }

    private boolean dirty(Session session) {
        Draft draft = session.drafts.get(session.file);
        return draft != null && (!draft.scalars.isEmpty() || !draft.lists.isEmpty());
    }

    private static boolean same(AdminConfigDocument.ScalarNode scalar, String typed) {
        String current = scalar.raw() == null ? "" : scalar.raw();
        String text = typed == null ? "" : typed.replace("\r\n", "\n").replace('\r', '\n');
        if (text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        if (scalar.kind() == AdminConfigValues.Kind.NULL) {
            return text.trim().isEmpty() || text.trim().equalsIgnoreCase("null");
        }
        if (scalar.kind() == AdminConfigValues.Kind.BOOLEAN
                || scalar.kind() == AdminConfigValues.Kind.INTEGER
                || scalar.kind() == AdminConfigValues.Kind.DECIMAL) {
            return current.equals(text.trim());
        }
        return current.equals(text);
    }

    private static boolean sameList(List<String> file, List<String> edited) {
        List<String> cleaned = new ArrayList<>();
        for (String value : edited) {
            if (value != null && !value.trim().isEmpty()) {
                cleaned.add(value.trim());
            }
        }
        List<String> current = new ArrayList<>();
        for (String value : file) {
            current.add(value == null ? "" : value.trim());
        }
        return cleaned.equals(current);
    }

    private static String boolText(DialogResponseView response, String key) {
        String chosen = response.getText(key);
        if (chosen != null && !chosen.isBlank()) {
            return chosen.trim();
        }
        Boolean value = response.getBoolean(key);
        if (value == null) {
            return null;
        }
        return value ? "true" : "false";
    }

    private static String text(DialogResponseView response, String key) {
        if (response == null) {
            return "";
        }
        String value = response.getText(key);
        return value == null ? "" : value.trim();
    }

    private static String titleFor(Session session) {
        if (session.path == null || session.path.isEmpty()) {
            return AdminConfigCatalog.displayName(session.file);
        }
        return clip(AdminConfigGuide.humanize(session.path.get(session.path.size() - 1)), 32);
    }

    private static String sectionIntro(Session session, AdminConfigDocument.Node node) {
        if (session.path == null || session.path.isEmpty()) {
            return fileSummary(session.file);
        }
        if (node != null && node.key() != null) {
            String function = AdminConfigGuide.function(node.key(), node.comments(), AdminConfigValues.Kind.STRING);
            if (!function.startsWith("Setting for")) {
                return function;
            }
        }
        return "Options in this section.";
    }

    private static String breadcrumb(Session session) {
        StringBuilder builder = new StringBuilder(AdminConfigCatalog.displayName(session.file));
        for (String segment : session.path) {
            builder.append(" > ");
            if (segment.matches("\\d+")) {
                builder.append("Entry ").append(Integer.parseInt(segment) + 1);
            } else {
                builder.append(AdminConfigGuide.humanize(segment));
            }
        }
        return builder.toString();
    }

    private static String fileSummary(String name) {
        return AdminConfigCatalog.describe(name).summary();
    }

    private static String entryLabel(AdminConfigDocument.Node element, int index) {
        String preview = preview(element);
        if (preview.isEmpty()) {
            return "Entry " + (index + 1);
        }
        return clip("Entry " + (index + 1) + " " + preview, 40);
    }

    private static String entryTip(AdminConfigDocument.Node element, int index) {
        List<String> keys = new ArrayList<>();
        if (element instanceof AdminConfigDocument.MapNode map) {
            for (AdminConfigDocument.Node child : map.children()) {
                if (child.key() != null) {
                    keys.add(child.key());
                }
            }
        }
        return "&fEntry " + (index + 1)
                + "\n&7" + (preview(element).isEmpty() ? "One item in this list." : preview(element))
                + "\n&8Example keys: &f" + (keys.isEmpty() ? "open it to see the options" : String.join(", ",
                keys.subList(0, Math.min(5, keys.size()))))
                + "\n&7Opens this entry.";
    }

    private static String preview(AdminConfigDocument.Node element) {
        if (element instanceof AdminConfigDocument.ScalarNode scalar) {
            return clip(scalar.raw(), 24);
        }
        if (element instanceof AdminConfigDocument.MapNode map) {
            for (String key : List.of("DISPLAY_NAME", "NAME", "TITLE", "MATERIAL", "enchantment")) {
                if (map.child(key) instanceof AdminConfigDocument.ScalarNode scalar && scalar.raw() != null) {
                    return clip(scalar.raw(), 24);
                }
            }
        }
        return "";
    }

    private static List<String> append(List<String> path, String segment) {
        List<String> copy = new ArrayList<>(path);
        copy.add(segment);
        return List.copyOf(copy);
    }

    private static String pathKey(List<String> path) {
        return String.join("\u001f", path);
    }

    private static List<String> pathOf(String key) {
        if (key == null || key.isEmpty()) {
            return List.of();
        }
        return List.of(key.split("\u001f", -1));
    }

    private static int pages(int count, int size) {
        if (count <= 0) {
            return 1;
        }
        return (count + size - 1) / size;
    }

    private static int clamp(int page, int pageCount) {
        if (pageCount <= 1) {
            return 0;
        }
        return Math.max(0, Math.min(page, pageCount - 1));
    }

    private static <T> List<T> window(List<T> items, int page, int size) {
        int start = page * size;
        if (start >= items.size()) {
            return List.of();
        }
        return items.subList(start, Math.min(items.size(), start + size));
    }

    private static int parseIndex(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static String clip(String text, int limit) {
        if (text == null) {
            return "";
        }
        String plain = text.replace('\n', ' ').trim();
        if (plain.length() <= limit) {
            return plain;
        }
        return plain.substring(0, Math.max(0, limit - 3)).trim() + "...";
    }

    private Session state(Player player) {
        return sessions.computeIfAbsent(player.getUniqueId(), ignored -> new Session());
    }

    private static final class Session {
        private String file;
        private List<String> path = List.of();
        private int page;
        private String query = "";
        private String notice;
        private String error;
        private boolean saving;
        private boolean confirming;
        private boolean previewRequested;
        private String pendingLeave;
        private View view;
        private final Map<String, Draft> drafts = new LinkedHashMap<>();
    }

    private static final class Draft {
        private final Map<String, String> scalars = new LinkedHashMap<>();
        private final Map<String, List<String>> lists = new LinkedHashMap<>();
    }

    private static final class View {
        private final String file;
        private final List<String> path;
        private final boolean scalarList;
        private final String listPathKey;
        private final List<Field> fields;
        private final List<Target> targets = new ArrayList<>();

        private View(String file, List<String> path, boolean scalarList, String listPathKey, List<Field> fields) {
            this.file = file;
            this.path = path;
            this.scalarList = scalarList;
            this.listPathKey = listPathKey;
            this.fields = fields;
        }
    }

    private record Target(String file, List<String> path) {
    }

    private record Field(String inputId, List<String> path, String key, boolean bool, int listIndex) {
    }

    private record Slot(
            List<String> path,
            String label,
            boolean bool,
            boolean checked,
            String initial,
            boolean multiline,
            boolean editable,
            String note
    ) {
        private static Slot editable(List<String> path, String label, boolean bool, boolean checked, String initial, boolean multiline) {
            return new Slot(path, label, bool, checked, initial, multiline, true, null);
        }

        private static Slot note(String note) {
            return new Slot(List.of(), null, false, false, null, false, false, note);
        }
    }
}
