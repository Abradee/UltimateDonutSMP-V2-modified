package com.bx.ultimateDonutSmp2.dialogs.screens;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.admin.AdminConfigStore;
import com.bx.ultimateDonutSmp2.admin.DonutRankInstaller;
import com.bx.ultimateDonutSmp2.admin.DonutRankSetup;
import com.bx.ultimateDonutSmp2.dialogs.DialogFactory;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import io.papermc.paper.dialog.DialogResponseView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Sets up the built-in Donut ranks in LuckPerms, including the prefix the tab and scoreboard show.
 * PlaceholderAPI and LuckPerms are required; a missing one opens an error popup instead.
 */
public final class DonutRankDialog {

    private static final String OPEN = "uds2a_ranks";
    private static final String INSTALL = "uds2a_rank_install";
    private static final String REMOVE_GROUPS = "uds2a_rank_remove_groups";
    private static final String SCOREBOARD = "uds2a_rank_board";
    private static final String REMOVE_BOARD = "uds2a_rank_board_remove";
    private static final String GIVE = "uds2a_rank_give";
    private static final String APPLY = "uds2a_rank_apply_";
    private static final String CLEAR_PLAYER = "uds2a_rank_clear";
    private static final String CUSTOM = "uds2a_rank_custom";
    private static final String CUSTOM_SET = "uds2a_rank_custom_set";
    private static final String CUSTOM_CLEAR = "uds2a_rank_custom_clear";
    private static final String CLOSE = "uds2a_rank_close";
    private static final String PLAYER = "player";
    private static final String GROUP = "group";
    private static final String PREFIX = "prefix";

    private final UltimateDonutSmp2 plugin;
    private final Map<UUID, String> notice = new ConcurrentHashMap<>();
    private final Map<UUID, String> error = new ConcurrentHashMap<>();

    public DonutRankDialog(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player) {
        List<String> missing = DonutRankSetup.missingPlugins(placeholderApi(), luckPerms());
        if (!missing.isEmpty()) {
            return showMissing(player, missing);
        }
        plugin.getSpigotScheduler().runAsync(() -> {
            int cleared = 0;
            try {
                cleared = new DonutRankInstaller().clearBuiltInBadgePrefixes();
            } catch (RuntimeException | LinkageError exception) {
                plugin.getLogger().log(Level.WARNING, "Could not remove duplicate Donut rank prefixes", exception);
            }
            int removed = cleared;
            plugin.getSpigotScheduler().runEntity(player, () -> {
                if (!player.isOnline()) {
                    return;
                }
                if (removed > 0) {
                    refreshOnlineRanks();
                }
                showMenu(player);
            });
        });
        return true;
    }

    public boolean handle(Player player, String action, DialogResponseView response) {
        if (action == null || !action.startsWith("uds2a_rank")) {
            return false;
        }
        if (CLOSE.equals(action)) {
            DialogSupport.close(player);
            return true;
        }
        List<String> missing = DonutRankSetup.missingPlugins(placeholderApi(), luckPerms());
        if (!missing.isEmpty()) {
            showMissing(player, missing);
            return true;
        }
        if (OPEN.equals(action)) {
            showMenu(player);
            return true;
        }
        if (INSTALL.equals(action)) {
            runInstall(player);
            return true;
        }
        if (REMOVE_GROUPS.equals(action)) {
            runDeleteGroups(player);
            return true;
        }
        if (SCOREBOARD.equals(action)) {
            addScoreboardLine(player);
            return true;
        }
        if (REMOVE_BOARD.equals(action)) {
            removeScoreboardLine(player);
            return true;
        }
        if (GIVE.equals(action)) {
            showGive(player);
            return true;
        }
        if (CLEAR_PLAYER.equals(action)) {
            String name = response == null ? "" : response.getText(PLAYER);
            runRemove(player, name);
            return true;
        }
        if (CUSTOM.equals(action)) {
            showCustom(player);
            return true;
        }
        if (CUSTOM_SET.equals(action)) {
            runCustomPrefix(player, text(response, GROUP), text(response, PREFIX), true);
            return true;
        }
        if (CUSTOM_CLEAR.equals(action)) {
            runCustomPrefix(player, text(response, GROUP), null, false);
            return true;
        }
        if (action.startsWith(APPLY)) {
            DonutRankSetup.Rank rank = DonutRankSetup.rank(action.substring(APPLY.length()));
            String name = response == null ? "" : response.getText(PLAYER);
            runGive(player, name, rank);
            return true;
        }
        return false;
    }

    private boolean showMissing(Player player, List<String> missing) {
        DialogFactory.Screen screen = DialogFactory.screen(player)
                .title("&cMissing plugins")
                .externalTitle("Missing plugins")
                .columns(1)
                .canCloseWithEscape(true)
                .text(DonutRankSetup.missingMessage(missing))
                .button("&fClose", "&7Close this popup.", 200, CLOSE);
        return DialogSupport.show(player, screen.build());
    }

    private boolean showMenu(Player player) {
        DialogFactory.Screen screen = DialogFactory.screen(player)
                .title("&fDonut ranks")
                .externalTitle("Donut ranks")
                .columns(1)
                .text("&7A custom LuckPerms rank stays hidden in the tab until that group has a &fprefix&7.")
                .text("&7Own group, such as admin or mvp, uses Custom group. The name alone is not enough.")
                .text("&7The tab reads &f%prefix%&7. These groups use the same badge as the plugin: &f+&7, &f++&7, and &f+++&7.")
                .text("&7Groups: &fdonutplus&7, &fdonutplusplus&7, and &fdonutplusplusplus&7. A higher one already includes the lower permissions.")
                .text(statusLine(player));
        for (DonutRankSetup.Rank rank : DonutRankSetup.ranks()) {
            screen.text("&f" + rank.prefix().trim() + " &8— &7" + rank.perks());
        }
        if (!luckPermsExpansion()) {
            screen.text("&eThe LuckPerms PlaceholderAPI expansion is not installed. The tab still uses the prefix. The scoreboard line needs &f/papi ecloud download LuckPerms&e then &f/papi reload&e.");
        }
        if (groupsInstalled()) {
            screen.button("&cRemove groups",
                    "&7Deletes donutplus, donutplusplus, and donutplusplusplus from LuckPerms. Their + badges stay built into the tab.",
                    280, REMOVE_GROUPS);
        } else {
            screen.button("&aInstall groups",
                    "&7Creates the three groups and their permissions only. The tab already draws +, ++, and +++.",
                    280, INSTALL);
        }
        if (scoreboardLineInstalled()) {
            screen.button("&cRemove scoreboard line",
                    "&7Removes the rank line this setup added.",
                    280, REMOVE_BOARD);
        } else {
            screen.button("&bAdd scoreboard line",
                    "&7Adds a line that shows %luckperms_prefix%.",
                    280, SCOREBOARD);
        }
        screen.button("&fCustom group",
                        "&7Set or remove the prefix of your own LuckPerms group. Example: admin, &cAdmin. Not for donutplus.",
                        280, CUSTOM)
                .button("&fGive or remove a rank",
                        "&7Puts a player in + / ++ / +++, or takes those ranks away. Example: Steve",
                        280, GIVE)
                .button("&fBack", "&7Return to the config list.", 200, "uds2a");
        return DialogSupport.show(player, screen.build());
    }

    private boolean groupsInstalled() {
        try {
            return new DonutRankInstaller().groupsInstalled();
        } catch (RuntimeException | LinkageError ignored) {
            return false;
        }
    }

    private boolean scoreboardLineInstalled() {
        try {
            return DonutRankSetup.hasScoreboardRankLine(serialize(AdminConfigStore.load(plugin, "scoreboard.yml")));
        } catch (RuntimeException | java.io.IOException | com.bx.ultimateDonutSmp2.admin.AdminConfigEditException ignored) {
            return false;
        }
    }

    private boolean showGive(Player player) {
        DialogFactory.Screen screen = DialogFactory.screen(player)
                .title("&fGive or remove a rank")
                .externalTitle("Give or remove a rank")
                .columns(1)
                .text("&7Type the player, then pick +, ++, or +++. Remove clears every Donut rank from that player.")
                .text("&8Example: &fSteve")
                .text(takeError(player))
                .textInput(PLAYER, "&fPlayer &8— &7Example: &fSteve", "", 16, false);
        for (DonutRankSetup.Rank rank : DonutRankSetup.ranks()) {
            screen.button("&f" + rank.prefix().trim(),
                    "&7Gives &f" + rank.label() + "&7. " + rank.perks(),
                    280,
                    APPLY + rank.group());
        }
        screen.button("&cRemove rank", "&7Takes +, ++, and +++ off the player you typed.", 280, CLEAR_PLAYER);
        screen.button("&fBack", "&7Return to rank setup.", 200, OPEN);
        return DialogSupport.show(player, screen.build());
    }

    private boolean showCustom(Player player) {
        DialogFactory.Screen screen = DialogFactory.screen(player)
                .title("&fCustom group")
                .externalTitle("Custom group")
                .columns(1)
                .text("&7Use a LuckPerms group you made yourself, such as admin or mvp. Prefixes are not added to donutplus, donutplusplus, or donutplusplusplus.")
                .text("&7Those three already show &f+&7, &f++&7, and &f+++&7 in the tab.")
                .text("&8Group example: &fadmin")
                .text("&8Prefix example: &cAdmin &7or <#00A4FC>+")
                .text(takeError(player))
                .textInput(GROUP, "&fGroup &8— &7Example: &fadmin", "", 36, false)
                .textInput(PREFIX, "&fPrefix &8— &7Example: &cAdmin", "", 64, false)
                .button("&aSet prefix", "&7Writes the prefix onto that group so the tab and scoreboard can show it.", 280, CUSTOM_SET)
                .button("&cRemove prefix", "&7Clears the prefix from that group. The group itself stays.", 280, CUSTOM_CLEAR)
                .button("&fBack", "&7Return to rank setup.", 200, OPEN);
        return DialogSupport.show(player, screen.build());
    }

    private void runCustomPrefix(Player player, String groupName, String prefix, boolean set) {
        plugin.getSpigotScheduler().runAsync(() -> {
            try {
                DonutRankInstaller installer = new DonutRankInstaller();
                if (set) {
                    boolean created = installer.setCustomPrefix(groupName, prefix);
                    String group = DonutRankSetup.normalizeGroup(groupName);
                    plugin.getSpigotScheduler().runEntity(player, () -> {
                        if (!player.isOnline()) {
                            return;
                        }
                        refreshOnlineRanks();
                        showSuccess(player, created
                                ? "&aCreated group &f" + group + " &aand set its prefix."
                                : "&aSet the prefix on group &f" + group + "&a. Players in that group now show it in the tab.");
                    });
                } else {
                    installer.clearCustomPrefix(groupName);
                    String group = DonutRankSetup.normalizeGroup(groupName);
                    plugin.getSpigotScheduler().runEntity(player, () -> {
                        if (!player.isOnline()) {
                            return;
                        }
                        refreshOnlineRanks();
                        showSuccess(player, "&aRemoved the prefix from group &f" + group + "&a. The group is still there.");
                    });
                }
            } catch (RuntimeException | LinkageError exception) {
                String message = exception instanceof IllegalArgumentException && exception.getMessage() != null
                        ? exception.getMessage()
                        : "&cCould not update that group. Check the console.";
                if (!(exception instanceof IllegalArgumentException)) {
                    plugin.getLogger().log(Level.SEVERE, "Could not update a custom LuckPerms group prefix", exception);
                }
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (player.isOnline()) {
                        error.put(player.getUniqueId(), message);
                        showCustom(player);
                    }
                });
            }
        });
    }

    private static String text(DialogResponseView response, String key) {
        if (response == null) {
            return "";
        }
        String value = response.getText(key);
        return value == null ? "" : value;
    }

    private void runInstall(Player player) {
        plugin.getSpigotScheduler().runAsync(() -> {
            try {
                new DonutRankInstaller().installGroups();
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    notice.remove(player.getUniqueId());
                    error.remove(player.getUniqueId());
                    refreshOnlineRanks();
                    showSuccess(player, "&aInstalled the &f+&a, &f++&a, and &f+++&a groups with their permissions. No extra prefix was added.");
                });
            } catch (RuntimeException | LinkageError exception) {
                plugin.getLogger().log(Level.SEVERE, "Could not install Donut ranks in LuckPerms", exception);
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (player.isOnline()) {
                        error.put(player.getUniqueId(), "&cCould not update LuckPerms. Check the console.");
                        showMenu(player);
                    }
                });
            }
        });
    }

    private void runGive(Player player, String name, DonutRankSetup.Rank rank) {
        plugin.getSpigotScheduler().runAsync(() -> {
            try {
                String saved = new DonutRankInstaller().give(name, rank);
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    notice.remove(player.getUniqueId());
                    error.remove(player.getUniqueId());
                    Player target = Bukkit.getPlayerExact(saved);
                    if (target != null) {
                        refresh(target);
                    }
                    showSuccess(player, "&aGave &f" + rank.prefix().trim() + " &ato &f" + saved + "&a.");
                });
            } catch (RuntimeException | LinkageError exception) {
                String message = exception instanceof IllegalArgumentException && exception.getMessage() != null
                        ? exception.getMessage()
                        : "&cCould not give that rank. Check the console.";
                if (!(exception instanceof IllegalArgumentException)) {
                    plugin.getLogger().log(Level.SEVERE, "Could not give a Donut rank", exception);
                }
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (player.isOnline()) {
                        error.put(player.getUniqueId(), message);
                        showGive(player);
                    }
                });
            }
        });
    }

    private void addScoreboardLine(Player player) {
        try {
            var document = AdminConfigStore.load(plugin, "scoreboard.yml");
            String updated = DonutRankSetup.addScoreboardRankLine(serialize(document));
            if (updated == null) {
                showResult(player, "&eDone", "&eThe scoreboard already shows a rank prefix.");
                return;
            }
            AdminConfigStore.save(plugin, "scoreboard.yml", updated);
            plugin.reloadAllPluginConfigurations();
            showSuccess(player, "&aAdded the rank line. It shows &f%luckperms_prefix%&a, which is &f+&a, &f++&a, or &f+++&a.");
            return;
        } catch (RuntimeException | java.io.IOException | com.bx.ultimateDonutSmp2.admin.AdminConfigEditException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not add the rank line to scoreboard.yml", exception);
            error.put(player.getUniqueId(), "&cCould not edit the scoreboard. Check the console.");
        }
        showMenu(player);
    }

    private void removeScoreboardLine(Player player) {
        try {
            var document = AdminConfigStore.load(plugin, "scoreboard.yml");
            String updated = DonutRankSetup.removeScoreboardRankLine(serialize(document));
            if (updated == null) {
                showResult(player, "&eDone", "&eThe rank line is not on the scoreboard.");
                return;
            }
            AdminConfigStore.save(plugin, "scoreboard.yml", updated);
            plugin.reloadAllPluginConfigurations();
            showSuccess(player, "&aRemoved the rank line from the scoreboard.");
        } catch (RuntimeException | java.io.IOException | com.bx.ultimateDonutSmp2.admin.AdminConfigEditException exception) {
            plugin.getLogger().log(Level.SEVERE, "Could not remove the rank line from scoreboard.yml", exception);
            error.put(player.getUniqueId(), "&cCould not edit the scoreboard. Check the console.");
            showMenu(player);
        }
    }

    private void runDeleteGroups(Player player) {
        plugin.getSpigotScheduler().runAsync(() -> {
            try {
                int removed = new DonutRankInstaller().deleteGroups();
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    refreshOnlineRanks();
                    showResult(player, removed == 0 ? "&eDone" : "&aSuccess", removed == 0
                            ? "&eThose rank groups were not in LuckPerms."
                            : "&aRemoved the &f+&a, &f++&a, and &f+++&a groups from LuckPerms.");
                });
            } catch (RuntimeException | LinkageError exception) {
                plugin.getLogger().log(Level.SEVERE, "Could not delete Donut ranks from LuckPerms", exception);
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (player.isOnline()) {
                        error.put(player.getUniqueId(), "&cCould not delete the LuckPerms groups. Check the console.");
                        showMenu(player);
                    }
                });
            }
        });
    }

    private void runRemove(Player player, String name) {
        plugin.getSpigotScheduler().runAsync(() -> {
            try {
                String saved = new DonutRankInstaller().remove(name);
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (!player.isOnline()) {
                        return;
                    }
                    Player target = Bukkit.getPlayerExact(saved);
                    if (target != null) {
                        refresh(target);
                    }
                    showSuccess(player, "&aRemoved &f+&a, &f++&a, and &f+++&a from &f" + saved + "&a.");
                });
            } catch (RuntimeException | LinkageError exception) {
                String message = exception instanceof IllegalArgumentException && exception.getMessage() != null
                        ? exception.getMessage()
                        : "&cCould not remove that rank. Check the console.";
                if (!(exception instanceof IllegalArgumentException)) {
                    plugin.getLogger().log(Level.SEVERE, "Could not remove a Donut rank", exception);
                }
                plugin.getSpigotScheduler().runEntity(player, () -> {
                    if (player.isOnline()) {
                        error.put(player.getUniqueId(), message);
                        showGive(player);
                    }
                });
            }
        });
    }

    private boolean showSuccess(Player player, String message) {
        return showResult(player, "&aSuccess", message);
    }

    private boolean showResult(Player player, String title, String message) {
        DialogFactory.Screen screen = DialogFactory.screen(player)
                .title(title)
                .externalTitle("Rank setup")
                .columns(1)
                .canCloseWithEscape(true)
                .text(message)
                .button("&fContinue", "&7Return to rank setup.", 200, OPEN);
        return DialogSupport.show(player, screen.build());
    }

    private void refresh(Player target) {
        if (target == null) {
            return;
        }
        if (plugin.getTablistManager() != null) {
            plugin.getTablistManager().update(target);
            plugin.getTablistManager().updateTablistName(target);
        }
        if (plugin.getScoreboardManager() != null) {
            plugin.getScoreboardManager().update(target);
        }
    }

    private void refreshOnlineRanks() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            refresh(online);
        }
    }

    private String statusLine(Player player) {
        StringBuilder builder = new StringBuilder();
        String pendingError = error.remove(player.getUniqueId());
        String pendingNotice = notice.remove(player.getUniqueId());
        if (pendingError != null) {
            builder.append(pendingError);
        }
        if (pendingNotice != null) {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append(pendingNotice);
        }
        return builder.isEmpty() ? "&7Nothing installed yet from this screen." : builder.toString();
    }

    private String takeError(Player player) {
        String pending = error.remove(player.getUniqueId());
        return pending == null ? "" : pending;
    }

    private static String serialize(com.bx.ultimateDonutSmp2.admin.AdminConfigDocument document) throws com.bx.ultimateDonutSmp2.admin.AdminConfigEditException {
        return document.apply(List.of());
    }

    private static boolean placeholderApi() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private static boolean luckPerms() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    private static boolean luckPermsExpansion() {
        if (!placeholderApi()) {
            return false;
        }
        try {
            return me.clip.placeholderapi.PlaceholderAPI.isRegistered("luckperms");
        } catch (RuntimeException | LinkageError ignored) {
            return false;
        }
    }
}
