package com.bx.ultimateDonutSmp2.dialogs.screens;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.amethyst.AmethystDuration;
import com.bx.ultimateDonutSmp2.amethyst.AmethystToolAppearance;
import com.bx.ultimateDonutSmp2.amethyst.AmethystToolsManager;
import com.bx.ultimateDonutSmp2.amethyst.AmethystToolsManager.ShardToolVariant;
import com.bx.ultimateDonutSmp2.dialogs.DialogActions;
import com.bx.ultimateDonutSmp2.dialogs.DialogConfig;
import com.bx.ultimateDonutSmp2.dialogs.DialogFactory;
import com.bx.ultimateDonutSmp2.dialogs.DialogScreen;
import com.bx.ultimateDonutSmp2.dialogs.DialogSession;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.dialogs.DialogText;
import io.papermc.paper.dialog.DialogResponseView;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Admin give flow for {@code /amethysttool} and {@code /shardtool}: pick a tool, pick any
 * known player (online or last-online), pick a duration, then grant the item.
 */
public final class AmethystToolDialog extends DialogScreen {

    private static final String TYPES = "AMETHYST_TYPE_DIALOG";
    private static final String PLAYERS = "AMETHYST_PLAYER_DIALOG";
    private static final String SEARCH = "AMETHYST_PLAYER_SEARCH_DIALOG";
    private static final String TIMES = "AMETHYST_DURATION_DIALOG";
    private static final String CUSTOM = "AMETHYST_DURATION_CUSTOM_DIALOG";

    private static final int DEFAULT_PAGE_SIZE = 21;

    public AmethystToolDialog(UltimateDonutSmp2 plugin, DialogConfig config, DialogSession.Store sessions) {
        super(plugin, config, sessions);
    }

    public boolean open(Player player) {
        session(player).clearAmethyst();
        return openTypes(player);
    }

    private boolean openTypes(Player player) {
        Map<String, String> tokens = DialogConfig.tokens("player", player.getName());
        int width = config.buttonWidth(TYPES, 200);
        DialogFactory.Screen screen = screen(player)
                .title(config.string(TYPES + ".TITLE", "Amethyst Tools", tokens))
                .externalTitle(config.string(TYPES + ".EXTERNAL-TITLE", null, tokens))
                .columns(config.columns(TYPES, 2))
                .text(config.string(TYPES + ".DESCRIPTION", "&7Choose an amethyst tool to give", tokens));

        String labelFormat = config.string(TYPES + ".ENTRY-FORMAT", "%icon% %name%");
        AmethystToolsManager manager = plugin.getAmethystToolsManager();
        for (ShardToolVariant variant : ShardToolVariant.values()) {
            Material material = manager.getDialogMaterial(variant);
            String icon = QuickBuyItemDialog.getSpriteTag(material);
            String name = manager.getDialogName(variant);
            Map<String, String> entry = DialogConfig.tokens(
                    "icon", icon == null ? "" : icon,
                    "name", name,
                    "type", variant.getId()
            );
            String label = DialogConfig.apply(labelFormat, entry);
            List<String> lore = manager.getDialogLore(variant);
            long preview = variant.getDefaultDuration();
            List<String> resolved = AmethystToolAppearance.resolveLore(lore, preview);
            String tooltip = resolved.isEmpty() ? null : String.join("\n", resolved);
            screen.button(
                    DialogText.of(label, player),
                    tooltip == null ? null : DialogText.tooltip(tooltip, player),
                    width,
                    DialogActions.AMETHYST_TYPE + variant.getId()
            );
        }
        return show(player, screen.build());
    }

    private boolean openPlayers(Player player, int page) {
        DialogSession session = session(player);
        ShardToolVariant variant = session.getAmethystVariant();
        if (variant == null) {
            return openTypes(player);
        }
        session.setAmethystPlayerPage(page);

        List<PlayerRow> rows = filterPlayers(collectPlayers(), session.getAmethystSearch());
        int pageSize = Math.max(1, config.integer(PLAYERS + ".PAGE-SIZE", DEFAULT_PAGE_SIZE));
        int pageCount = Math.max(1, (rows.size() + pageSize - 1) / pageSize);
        int safePage = Math.max(0, Math.min(page, pageCount - 1));
        session.setAmethystPlayerPage(safePage);
        int from = safePage * pageSize;
        int to = Math.min(rows.size(), from + pageSize);

        Map<String, String> tokens = DialogConfig.tokens(
                "player", player.getName(),
                "type", plugin.getAmethystToolsManager().getDialogName(variant),
                "page", String.valueOf(safePage + 1),
                "pages", String.valueOf(pageCount)
        );
        int width = config.buttonWidth(PLAYERS, 150);
        DialogFactory.Screen screen = screen(player)
                .title(config.string(PLAYERS + ".TITLE", "Choose a player", tokens))
                .externalTitle(config.string(PLAYERS + ".EXTERNAL-TITLE", null, tokens))
                .columns(config.columns(PLAYERS, 3))
                .text(config.string(PLAYERS + ".DESCRIPTION", "&7Online and offline players", tokens));

        if (rows.isEmpty()) {
            screen.text(config.string(PLAYERS + ".EMPTY-TEXT", "&7No players matched."));
        }
        for (PlayerRow row : rows.subList(from, to)) {
            Component head = DialogPlayerHeads.component(row.uuid(), row.name());
            Component label = head == null || head.equals(Component.empty())
                    ? DialogText.of(row.name(), player)
                    : head.append(Component.space()).append(DialogText.of(row.name(), player));
            screen.button(
                    label,
                    DialogText.tooltip(statusLore(row), player),
                    width,
                    DialogActions.AMETHYST_PLAYER + row.uuid()
            );
        }

        screen.button(
                config.string(PLAYERS + ".SEARCH-LABEL", "&7Search"),
                config.string(PLAYERS + ".SEARCH-TOOLTIP", "&7Filter by name"),
                width,
                DialogActions.AMETHYST_SEARCH
        );
        if (safePage > 0) {
            screen.button(
                    config.string(PLAYERS + ".PREV-LABEL", "&7Previous"),
                    null,
                    width,
                    DialogActions.AMETHYST_PAGE + (safePage - 1)
            );
        }
        if (safePage + 1 < pageCount) {
            screen.button(
                    config.string(PLAYERS + ".NEXT-LABEL", "&7Next"),
                    null,
                    width,
                    DialogActions.AMETHYST_PAGE + (safePage + 1)
            );
        }
        screen.button(
                config.string(PLAYERS + ".BACK-LABEL", "&7Back"),
                null,
                width,
                DialogActions.AMETHYST_TYPES
        );
        return show(player, screen.build());
    }

    private boolean openSearch(Player player) {
        Map<String, String> tokens = DialogConfig.tokens("player", player.getName());
        String current = session(player).getAmethystSearch();
        DialogFactory.Screen screen = screen(player)
                .title(config.string(SEARCH + ".TITLE", "Search players", tokens))
                .externalTitle(config.string(SEARCH + ".EXTERNAL-TITLE", null, tokens))
                .columns(config.columns(SEARCH, 2))
                .text(config.string(SEARCH + ".DESCRIPTION", "&7Type a name", tokens))
                .inputs(config.inputs(SEARCH, tokens));
        if (current != null && !current.isBlank() && config.inputs(SEARCH, tokens).isEmpty()) {
            screen.input(new DialogConfig.InputSpec("query", "Player", 300, 16, current, List.of()));
        }
        screen.buttons(config.buttonsOf(SEARCH, tokens));
        return show(player, screen.build());
    }

    private boolean openTimes(Player player) {
        DialogSession session = session(player);
        ShardToolVariant variant = session.getAmethystVariant();
        UUID target = session.getAmethystTarget();
        if (variant == null) {
            return openTypes(player);
        }
        if (target == null) {
            return openPlayers(player, session.getAmethystPlayerPage());
        }
        String name = session.getAmethystTargetName() == null
                ? displayName(target, target.toString())
                : session.getAmethystTargetName();
        Map<String, String> tokens = DialogConfig.tokens(
                "player", player.getName(),
                "type", plugin.getAmethystToolsManager().getDialogName(variant),
                "target", name
        );
        int width = config.buttonWidth(TIMES, 150);
        DialogFactory.Screen screen = screen(player)
                .title(config.string(TIMES + ".TITLE", "Choose duration", tokens))
                .externalTitle(config.string(TIMES + ".EXTERNAL-TITLE", null, tokens))
                .columns(config.columns(TIMES, 2));

        Component head = DialogPlayerHeads.component(target, name);
        Component header = head == null || head.equals(Component.empty())
                ? DialogText.of(name, player)
                : DialogPlayerHeads.insertBetween(null, head, Component.text(" " + name));
        screen.text(header);
        screen.text(config.string(TIMES + ".DESCRIPTION", "&7How long should this tool last?", tokens));

        boolean hasBack = false;
        boolean hasCustom = false;
        List<Map<?, ?>> durations = plugin.getConfigManager().getDialog().getMapList(TIMES + ".DURATIONS");
        if (durations.isEmpty()) {
            durations = defaultDurations();
        }
        for (Map<?, ?> entry : durations) {
            Object label = entry.get("LABEL");
            if (label == null) {
                continue;
            }
            Object action = entry.get("ACTION");
            if (action != null && "back".equalsIgnoreCase(String.valueOf(action))) {
                hasBack = true;
                screen.button(String.valueOf(label), null, width, DialogActions.AMETHYST_PLAYERS);
                continue;
            }
            if (action != null && "custom".equalsIgnoreCase(String.valueOf(action))) {
                hasCustom = true;
                screen.button(String.valueOf(label), null, width, DialogActions.AMETHYST_TIME_CUSTOM);
                continue;
            }
            long seconds = number(entry.get("SECONDS"));
            if (seconds <= 0L) {
                seconds = AmethystDuration.parseSeconds(String.valueOf(label));
            }
            if (seconds <= 0L) {
                continue;
            }
            String shown = String.valueOf(label);
            if (BARE_SECONDS.matcher(shown.trim()).matches()) {
                shown = AmethystDuration.formatDhM(seconds);
            }
            screen.button(shown, AmethystDuration.formatDhM(seconds), width,
                    DialogActions.AMETHYST_TIME + seconds);
        }
        if (!hasCustom) {
            screen.button(
                    config.string(TIMES + ".CUSTOM-LABEL", "Custom"),
                    null,
                    width,
                    DialogActions.AMETHYST_TIME_CUSTOM
            );
        }
        if (!hasBack) {
            screen.button(
                    config.string(TIMES + ".BACK-LABEL", "&7Back"),
                    null,
                    width,
                    DialogActions.AMETHYST_PLAYERS
            );
        }
        return show(player, screen.build());
    }

    private static final java.util.regex.Pattern BARE_SECONDS = java.util.regex.Pattern.compile("\\d+");

    private boolean openCustom(Player player) {
        DialogSession session = session(player);
        ShardToolVariant variant = session.getAmethystVariant();
        UUID target = session.getAmethystTarget();
        if (variant == null || target == null) {
            return openTypes(player);
        }
        Map<String, String> tokens = DialogConfig.tokens(
                "player", player.getName(),
                "type", plugin.getAmethystToolsManager().getDialogName(variant),
                "target", session.getAmethystTargetName()
        );
        return show(player, screen(player)
                .title(config.string(CUSTOM + ".TITLE", "Custom duration", tokens))
                .externalTitle(config.string(CUSTOM + ".EXTERNAL-TITLE", null, tokens))
                .columns(config.columns(CUSTOM, 1))
                .text(config.string(CUSTOM + ".DESCRIPTION",
                        "&7Type a duration such as &e1h&7, &e5h&7, &e1d&7, or &e1d 5h", tokens))
                .inputs(config.inputs(CUSTOM, tokens))
                .buttons(config.buttonsOf(CUSTOM, tokens))
                .build());
    }

    @Override
    public boolean handle(Player player, String action, DialogResponseView response) {
        if (DialogActions.AMETHYST_TYPES.equals(action) || DialogActions.AMETHYST_MENU.equals(action)) {
            return openTypes(player);
        }
        if (DialogActions.AMETHYST_PLAYERS.equals(action)) {
            return openPlayers(player, session(player).getAmethystPlayerPage());
        }
        if (DialogActions.AMETHYST_SEARCH.equals(action)) {
            return openSearch(player);
        }
        if (DialogActions.AMETHYST_SEARCH_CAN.equals(action)) {
            return openPlayers(player, session(player).getAmethystPlayerPage());
        }
        if (DialogActions.AMETHYST_SEARCH_GO.equals(action)) {
            String query = input(response, "query");
            if (query == null) {
                query = input(response, "player_name");
            }
            session(player).setAmethystSearch(query);
            return openPlayers(player, 0);
        }
        if (DialogActions.AMETHYST_TIMES.equals(action)) {
            return openTimes(player);
        }
        if (DialogActions.AMETHYST_TIME_CUSTOM.equals(action)) {
            return openCustom(player);
        }
        if (DialogActions.AMETHYST_TIME_CUSTOM_GO.equals(action)) {
            String raw = input(response, "duration");
            if (raw == null) {
                raw = input(response, "time");
            }
            long seconds = AmethystDuration.parseSeconds(raw);
            if (seconds <= 0L) {
                message(player, plugin.getAmethystToolsManager().getMessage("DURATION-INVALID"));
                return openCustom(player);
            }
            return grant(player, seconds);
        }

        String typeArg = DialogActions.argument(action, DialogActions.AMETHYST_TYPE);
        if (typeArg != null) {
            ShardToolVariant variant = AmethystToolsManager.resolveVariant(typeArg);
            if (variant == null) {
                return openTypes(player);
            }
            session(player).setAmethystVariant(variant);
            return openPlayers(player, 0);
        }

        String pageArg = DialogActions.argument(action, DialogActions.AMETHYST_PAGE);
        if (pageArg != null) {
            try {
                return openPlayers(player, Integer.parseInt(pageArg));
            } catch (NumberFormatException ignored) {
                return openPlayers(player, 0);
            }
        }

        String playerArg = DialogActions.argument(action, DialogActions.AMETHYST_PLAYER);
        if (playerArg != null) {
            UUID target = parseUuid(playerArg);
            if (target == null) {
                return openPlayers(player, session(player).getAmethystPlayerPage());
            }
            session(player).setAmethystTarget(target);
            session(player).setAmethystTargetName(displayName(target, target.toString()));
            return openTimes(player);
        }

        String timeArg = DialogActions.argument(action, DialogActions.AMETHYST_TIME);
        if (timeArg != null) {
            try {
                long seconds = Long.parseLong(timeArg);
                if (seconds <= 0L) {
                    return openTimes(player);
                }
                return grant(player, seconds);
            } catch (NumberFormatException ignored) {
                return openTimes(player);
            }
        }
        return false;
    }

    private boolean grant(Player admin, long durationSeconds) {
        DialogSession session = session(admin);
        ShardToolVariant variant = session.getAmethystVariant();
        UUID target = session.getAmethystTarget();
        if (variant == null || target == null) {
            return openTypes(admin);
        }
        AmethystToolsManager.GrantOutcome outcome = plugin.getAmethystToolsManager()
                .grant(target, variant, durationSeconds);
        String typeName = plugin.getAmethystToolsManager().getDialogName(variant);
        String time = AmethystDuration.formatDhM(durationSeconds);
        String targetName = session.getAmethystTargetName() == null
                ? displayName(target, target.toString())
                : session.getAmethystTargetName();
        String key = switch (outcome) {
            case ONLINE -> "GIVE-SUCCESS";
            case QUEUED -> "GIVE-QUEUED";
            case FAILED -> "GIVE-FAILED";
        };
        message(admin, plugin.getAmethystToolsManager().getMessage(
                key,
                "{type}", typeName,
                "{player}", targetName,
                "{time}", time
        ));
        DialogSupport.close(admin);
        session.clearAmethyst();
        return true;
    }

    private List<PlayerRow> collectPlayers() {
        Map<UUID, PlayerRow> rows = new LinkedHashMap<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            rows.put(online.getUniqueId(), new PlayerRow(
                    online.getUniqueId(),
                    online.getName(),
                    true,
                    System.currentTimeMillis()
            ));
        }
        for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
            addOfflineRow(rows, offline == null ? null : offline.getUniqueId(),
                    offline == null ? null : offline.getName(),
                    lastSeenMillis(offline));
        }
        if (plugin.getDatabaseManager() != null) {
            for (var identity : plugin.getDatabaseManager().loadKnownPlayerIdentities()) {
                if (identity == null) {
                    continue;
                }
                addOfflineRow(rows, identity.uuid(), identity.username(), lastSeenMillis(identity.uuid()));
            }
        }
        List<PlayerRow> list = new ArrayList<>(rows.values());
        list.sort(Comparator
                .comparing(PlayerRow::online).reversed()
                .thenComparing(PlayerRow::name, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    private void addOfflineRow(Map<UUID, PlayerRow> rows, UUID uuid, String name, long lastSeen) {
        if (uuid == null || rows.containsKey(uuid)) {
            return;
        }
        String resolved = name == null || name.isBlank() ? uuid.toString() : name;
        rows.put(uuid, new PlayerRow(uuid, resolved, false, lastSeen));
    }

    private List<PlayerRow> filterPlayers(List<PlayerRow> rows, String search) {
        if (search == null || search.isBlank()) {
            return rows;
        }
        String needle = search.toLowerCase(Locale.ROOT);
        List<PlayerRow> filtered = new ArrayList<>();
        for (PlayerRow row : rows) {
            if (row.name().toLowerCase(Locale.ROOT).contains(needle)) {
                filtered.add(row);
            }
        }
        return filtered;
    }

    private long lastSeenMillis(UUID uuid) {
        return lastSeenMillis(Bukkit.getOfflinePlayer(uuid));
    }

    private long lastSeenMillis(OfflinePlayer offline) {
        return offline == null ? 0L : offline.getLastPlayed();
    }

    private String statusLore(PlayerRow row) {
        if (row.online()) {
            return plugin.getAmethystToolsManager().getMessage("PLAYER-ONLINE",
                    "{status}", "online",
                    "{player}", row.name());
        }
        long elapsed = row.lastSeenMillis() <= 0L
                ? 0L
                : Math.max(0L, System.currentTimeMillis() - row.lastSeenMillis());
        String time = AmethystDuration.formatLastOnline(elapsed);
        return plugin.getAmethystToolsManager().getMessage("PLAYER-OFFLINE",
                "{status}", "offline",
                "{time}", time,
                "{last_online}", time,
                "{player}", row.name());
    }

    private static List<Map<?, ?>> defaultDurations() {
        List<Map<?, ?>> list = new ArrayList<>();
        list.add(Map.of("LABEL", "1h", "SECONDS", 3600));
        list.add(Map.of("LABEL", "5h", "SECONDS", 18_000));
        list.add(Map.of("LABEL", "10h", "SECONDS", 36_000));
        list.add(Map.of("LABEL", "1d", "SECONDS", 86_400));
        list.add(Map.of("LABEL", "5d", "SECONDS", 432_000));
        list.add(Map.of("LABEL", "Custom", "ACTION", "custom"));
        return list;
    }

    private static long number(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value != null) {
            try {
                return Long.parseLong(String.valueOf(value).trim());
            } catch (NumberFormatException ignored) {
                return 0L;
            }
        }
        return 0L;
    }

    private record PlayerRow(UUID uuid, String name, boolean online, long lastSeenMillis) {
    }
}
