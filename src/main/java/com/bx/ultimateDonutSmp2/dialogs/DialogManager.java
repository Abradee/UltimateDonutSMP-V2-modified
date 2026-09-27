package com.bx.ultimateDonutSmp2.dialogs;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.screens.AmethystToolDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.FriendsDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.HomesDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.LeaderboardDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.MainDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.PayDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.OrdersDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.RtpQueueDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.SettingsDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.StatsDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.TeleportDialog;
import io.papermc.paper.dialog.DialogResponseView;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Entry point for the dialog interface.
 *
 * <p>Commands ask this class to open a screen and fall back to their chest menu when it answers
 * false, which happens on Spigot, on a pre-1.21.6 Paper, or when the operator turned the screen
 * off in {@code dialog.yml}. Button presses arrive from {@link DialogListener} and are offered to
 * each screen in turn until one claims the action.
 *
 * <p>Nothing here is constructed unless {@link DialogSupport#isAvailable()} already said yes, so
 * a Spigot server never loads a Paper class.
 */
public final class DialogManager {

    private final UltimateDonutSmp2 plugin;
    private final DialogConfig config;
    private final DialogSession.Store sessions = new DialogSession.Store();

    private final MainDialog main;
    private final SettingsDialog settings;
    private final PayDialog pay;
    private final StatsDialog stats;
    private final LeaderboardDialog leaderboards;
    private final HomesDialog homes;
    private final FriendsDialog friends;
    private final TeleportDialog teleport;
    private final RtpQueueDialog rtpQueue;
    private final QuickBuyItemDialog quickBuyItem;
    private final OrdersDialog orders;
    private final com.bx.ultimateDonutSmp2.dialogs.screens.BountyDialog bounty;
    private final AmethystToolDialog amethyst;
    private final List<DialogScreen> screens;
    private final PauseScreenDatapack pauseScreen;

    public DialogManager(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
        this.config = new DialogConfig(plugin);
        this.main = new MainDialog(plugin, config, sessions);
        this.settings = new SettingsDialog(plugin, config, sessions);
        this.pay = new PayDialog(plugin, config, sessions);
        this.stats = new StatsDialog(plugin, config, sessions);
        this.leaderboards = new LeaderboardDialog(plugin, config, sessions);
        this.homes = new HomesDialog(plugin, config, sessions);
        this.friends = new FriendsDialog(plugin, config, sessions);
        this.teleport = new TeleportDialog(plugin, config, sessions);
        this.rtpQueue = new RtpQueueDialog(plugin, config, sessions);
        this.quickBuyItem = new QuickBuyItemDialog(plugin, config, sessions);
        this.orders = new OrdersDialog(plugin, config, sessions);
        this.bounty = new com.bx.ultimateDonutSmp2.dialogs.screens.BountyDialog(plugin, config, sessions);
        this.amethyst = new AmethystToolDialog(plugin, config, sessions);
        // Ordered so the cheap exact-match screens answer before the prefix-matching ones.
        this.screens = List.of(main, settings, pay, stats, leaderboards, homes, friends, teleport, rtpQueue, bounty, amethyst, orders, quickBuyItem);
        this.pauseScreen = new PauseScreenDatapack(plugin);
    }

    @SuppressWarnings("unchecked")
    public <T extends DialogScreen> T getScreen(Class<T> screenClass) {
        for (DialogScreen screen : screens) {
            if (screenClass.isInstance(screen)) {
                return (T) screen;
            }
        }
        return null;
    }


    /**
     * Refreshes the generated pause-screen datapack. Called once the worlds exist, and again on
     * reload so an edit to dialog.yml is picked up.
     */
    public void refreshPauseScreen() {
        pauseScreen.refresh(plugin.getConfigManager().getDialog());
    }

    public DialogConfig getConfig() {
        return config;
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public boolean openMain(Player player) {
        return config.isScreenEnabled("MAIN") && main.open(player);
    }

    public boolean openSettings(Player player) {
        return config.isScreenEnabled("SETTINGS") && settings.open(player);
    }

    public boolean openPay(Player player) {
        return config.isScreenEnabled("PAY") && pay.open(player);
    }

    /** Opens the pay flow already pointed at a player, used by {@code /pay <name>}. */
    public boolean openPay(Player player, UUID target, String targetName, Double amount) {
        return config.isScreenEnabled("PAY") && pay.openFor(player, target, targetName, amount);
    }

    public boolean openStats(Player player) {
        return config.isScreenEnabled("STATS") && stats.open(player);
    }

    public boolean openStats(Player player, String targetName) {
        return config.isScreenEnabled("STATS") && stats.openFor(player, targetName);
    }

    public boolean openLeaderboards(Player player) {
        return config.isScreenEnabled("LEADERBOARDS") && leaderboards.open(player);
    }

    public boolean openHomes(Player player) {
        return config.isScreenEnabled("HOMES") && homes.open(player);
    }

    public boolean openFriends(Player player) {
        return openFriends(player, DialogSession.FriendFilter.ALL, null);
    }

    public boolean openFriends(Player player, DialogSession.FriendFilter filter, String search) {
        return config.isScreenEnabled("FRIENDS") && friends.open(player, filter, search);
    }

    public boolean openTeleport(Player player) {
        return config.isScreenEnabled("TELEPORT") && teleport.open(player);
    }

    public boolean openRtpQueue(Player player) {
        return config.isScreenEnabled("RTP-QUEUE") && rtpQueue.open(player);
    }

    public boolean openOrders(Player player) {
        main.openOrders(player);
        return true;
    }

    public boolean openBountySearch(Player player, String query, boolean sortByRecent, int page) {
        return bounty.openSearch(player, query, sortByRecent, page);
    }

    public boolean openAmethyst(Player player) {
        return config.isScreenEnabled("AMETHYST") && amethyst.open(player);
    }

    /** Routes one button press. Unknown ids are ignored rather than logged, to stay quiet. */
    public void handle(Player player, Key key, DialogResponseView response) {
        if (player == null || !DialogActions.isOurs(key) || !config.isEnabled()) {
            return;
        }
        String action = key.value();
        if (DialogActions.ORDERS.equals(action) || DialogActions.ORDERS_MENU.equals(action)) {
            main.openOrders(player);
            return;
        }
        String commandId = DialogActions.argument(action, DialogActions.RUN_COMMAND);
        if (commandId != null) {
            runCommandButton(player, commandId);
            return;
        }
        for (DialogScreen screen : screens) {
            if (screen.handle(player, action, response)) {
                return;
            }
        }
    }

    /**
     * Runs a COMMAND button. The dialog is closed first so a command that opens nothing — a
     * teleport, say — does not leave the menu sitting over the world.
     */
    private void runCommandButton(Player player, String commandId) {
        String command = sessions.get(player.getUniqueId()).commandFor(commandId);
        if (command == null || command.isBlank()) {
            return;
        }
        String cmdStr = command.trim();
        String trimmedCmd = cmdStr.startsWith("/") ? cmdStr.substring(1) : cmdStr;
        if (trimmedCmd.equalsIgnoreCase("orders")) {
            main.openOrders(player);
            return;
        }
        DialogSupport.close(player);
        plugin.getSpigotScheduler().runEntityLater(player, () -> {
            if (player.isOnline()) {
                plugin.getSpigotScheduler().dispatchPlayerCommand(player, trimmedCmd);
            }
        }, 1L);
    }

    public void forget(UUID playerId) {
        sessions.remove(playerId);
        DialogSupport.forget(playerId);
        if (plugin.getPlayerSettingToggles() != null) {
            plugin.getPlayerSettingToggles().dropAuctionCache(playerId);
        }
    }

    public void clear() {
        sessions.clear();
    }
}
