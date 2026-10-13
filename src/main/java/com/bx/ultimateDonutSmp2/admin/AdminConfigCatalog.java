package com.bx.ultimateDonutSmp2.admin;

import com.bx.ultimateDonutSmp2.managers.ConfigManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The yaml files an operator can edit from {@code /uds2a}.
 *
 * <p>Language files are left out on purpose. Each one has thousands of keys, and a dialog cannot
 * present them in a way that is safer than editing the file.
 */
public final class AdminConfigCatalog {

    public record FileInfo(String name, String summary, String example) {
    }

    private static final Map<String, FileInfo> DESCRIBED = described();

    private AdminConfigCatalog() {
    }

    public static List<FileInfo> files() {
        List<FileInfo> files = new ArrayList<>();
        for (String name : ConfigManager.bundledConfigurationNames()) {
            files.add(describe(name));
        }
        if (files.stream().noneMatch(file -> file.name().equals("offenses.yml"))) {
            files.add(describe("offenses.yml"));
        }
        return List.copyOf(files);
    }

    public static boolean editable(String name) {
        if (name == null || name.isBlank() || name.contains("/") || name.contains("\\") || name.contains("..")) {
            return false;
        }
        String normalized = name.toLowerCase(Locale.ROOT);
        if (normalized.startsWith("languages")) {
            return false;
        }
        return files().stream().anyMatch(file -> file.name().equalsIgnoreCase(name));
    }

    public static FileInfo describe(String name) {
        FileInfo known = DESCRIBED.get(name);
        if (known != null) {
            return known;
        }
        String base = name.endsWith(".yml") ? name.substring(0, name.length() - 4) : name;
        return new FileInfo(
                name,
                "Settings stored in " + name + ".",
                "Open " + base + " and edit a key, then press Save & reload."
        );
    }

    /** {@code shop.yml} is shown as {@code Shop}. The real file name stays on disk. */
    public static String displayName(String name) {
        if (name == null || name.isBlank()) {
            return "Config";
        }
        String base = name;
        if (base.endsWith(".yml")) {
            base = base.substring(0, base.length() - 4);
        } else if (base.endsWith(".yaml")) {
            base = base.substring(0, base.length() - 5);
        }
        return AdminConfigGuide.humanize(base);
    }

    public static String tooltip(FileInfo file) {
        return "&f" + displayName(file.name())
                + "\n&7" + file.summary()
                + "\n&8Example: &f" + file.example()
                + "\n&7Click to edit this file.";
    }

    private static Map<String, FileInfo> described() {
        Map<String, FileInfo> files = new LinkedHashMap<>();
        put(files, "config.yml",
                "Language, homes, combat, tablist, optimization, and the rest of the main settings.",
                "SETTINGS.HOME-DEFAULT is 3");
        put(files, "death-messages.yml",
                "Chat lines shown when a player dies.",
                "a death message such as &7{player} fell");
        put(files, "menus.yml",
                "Chest menu titles, button names, and lore.",
                "GLOBAL or a menu TITLE such as &8Settings");
        put(files, "dialog.yml",
                "Dialog screens, the pause menu, and button labels.",
                "ENABLED is true");
        put(files, "scoreboard.yml",
                "Sidebar scoreboard title and lines.",
                "SCOREBOARD title text");
        put(files, "shop.yml",
                "Shard shop and quick buy prices, icons, and layout.",
                "SHARD-MENU.TITLE is &8Shard Shop");
        put(files, "sounds.yml",
                "Sounds played for menus, shop, teleport, and crates.",
                "ENTITY_EXPERIENCE_ORB_PICKUP");
        put(files, "rtp.yml",
                "Random teleport radius, queue, and worlds.",
                "ENABLED is true");
        put(files, "worth.yml",
                "Item sell prices and the worth tooltip.",
                "DISPLAY.FORMAT is &#00FC00$ &f{price}");
        put(files, "amethyst-tools.yml",
                "Amethyst tool types, durations, and messages.",
                "AMETHYST-TOOLS");
        put(files, "ender-chest.yml",
                "Ender chest rows and messages.",
                "ENDER-CHEST");
        put(files, "invsee.yml",
                "Inventory inspector layout.",
                "INVSEE");
        put(files, "freeze.yml",
                "Freeze command settings and messages.",
                "FREEZE");
        put(files, "auction-house.yml",
                "Auction listings, tax, and menus.",
                "SETTINGS");
        put(files, "orders.yml",
                "Order pricing, delivery, and menus.",
                "SETTINGS");
        put(files, "enchantments.yml",
                "Enchantment picker slots and item pages.",
                "gui.title is &#444444pick enchantments");
        put(files, "filter.yml",
                "Item categories used by orders and quick buy search.",
                "Blocks includes STONE");
        put(files, "duels.yml",
                "Duel countdown, kits, and arena settings.",
                "SETTINGS");
        put(files, "ffa.yml",
                "Free-for-all arena rules.",
                "SETTINGS");
        put(files, "pvp.yml",
                "PvP ranks, elo, and match settings.",
                "SETTINGS");
        put(files, "crates.yml",
                "Crate rewards and key settings.",
                "SETTINGS");
        put(files, "spawners.yml",
                "Spawner types and the spawner menu.",
                "SETTINGS");
        put(files, "spawn-stash.yml",
                "Amethyst stash layouts, timers, and blocks.",
                "TYPES.1.DISPLAY_NAME");
        put(files, "network.yml",
                "Cross-server network and maintenance.",
                "NETWORK");
        put(files, "staff-mode.yml",
                "Staff mode items, menus, and messages.",
                "STAFF-MODE");
        put(files, "hide.yml",
                "Hide and disguise names, skins, and messages.",
                "COOLDOWN-SECONDS is 30");
        put(files, "database.yml",
                "Database and Redis connection settings.",
                "DATABASE.TYPE is SQLITE");
        put(files, "server-wipe.yml",
                "Server wipe worlds and the backup folder.",
                "ENABLED is true");
        put(files, "discord.yml",
                "Discord webhook messages for punishments.",
                "WEBHOOKS.ENABLED is true");
        put(files, "anvil-moderation.yml",
                "Words blocked in anvils, and the punishments for using them.",
                "banned-words includes example");
        put(files, "offenses.yml",
                "Staff punishment offense presets.",
                "offenses");
        return files;
    }

    private static void put(Map<String, FileInfo> files, String name, String summary, String example) {
        files.put(name, new FileInfo(name, summary, example));
    }
}
