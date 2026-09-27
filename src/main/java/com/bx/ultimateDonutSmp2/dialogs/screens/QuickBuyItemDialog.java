package com.bx.ultimateDonutSmp2.dialogs.screens;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.DialogActions;
import com.bx.ultimateDonutSmp2.dialogs.DialogConfig;
import com.bx.ultimateDonutSmp2.dialogs.DialogFactory;
import com.bx.ultimateDonutSmp2.dialogs.DialogScreen;
import com.bx.ultimateDonutSmp2.dialogs.DialogSession;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.managers.WorthManager;
import com.bx.ultimateDonutSmp2.menus.AuctionHouseBrowseMenu;
import com.bx.ultimateDonutSmp2.menus.AuctionHouseSounds;
import com.bx.ultimateDonutSmp2.menus.AuctionYourItemsMenu;
import com.bx.ultimateDonutSmp2.menus.QuickBuyItemSelectMenu;
import com.bx.ultimateDonutSmp2.menus.QuickBuyMenu;
import com.bx.ultimateDonutSmp2.menus.QuickBuySounds;
import com.bx.ultimateDonutSmp2.menus.QuickBuyTransactionsMenu;
import com.bx.ultimateDonutSmp2.managers.EnchantmentsManager;
import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import io.papermc.paper.dialog.DialogResponseView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class QuickBuyItemDialog extends DialogScreen {

    /** Side-by-side Cancel / Add to Quick Buy, matching Design/Quick Buy/Buy Item/3.png. */
    static final int AMOUNT_COLUMNS = 2;
    static final int AMOUNT_BUTTON_WIDTH = 150;
    static final int AMOUNT_INPUT_WIDTH = 200;

    /** Choose Enchantments matching Design/Quick Buy/Buy Item/Choose Enchantments/main.png. */
    public static final int ENCHANT_COLUMNS = 6;
    public static final int ENCHANT_NAME_WIDTH = 145;
    public static final int ENCHANT_LEVEL_WIDTH = 42;
    public static final int ENCHANT_INERT_WIDTH = 1;
    public static final int ENCHANT_BOTTOM_WIDTH = 160;

    public record EnchantmentSpec(
            String key,
            Enchantment enchantment,
            String displayName,
            boolean isCurse,
            int maxLevel
    ) {}

    public QuickBuyItemDialog(UltimateDonutSmp2 plugin, DialogConfig config, DialogSession.Store sessions) {
        super(plugin, config, sessions);
    }

    public boolean openSelection(Player player, int targetSlot, String query) {
        List<Material> items = getSelectableMaterials(plugin, query);
        DialogFactory.Screen screen = screen(player)
                .title("Choose Item")
                .columns(4)
                .input(new DialogConfig.InputSpec("search", "Search", 200, 32, query == null ? "" : query, List.of()))
                .button("&fSearch", null, 120, DialogActions.QUICK_BUY_SEARCH + targetSlot);

        for (Material mat : items) {
            String name = plugin.getWorthManager().prettifyMaterial(mat);
            String spriteTag = getSpriteTag(mat);
            String label = spriteTag.isEmpty() ? ("&f" + name) : (spriteTag + " &f" + name);
            screen.button(
                    label,
                    chooseItemWorthTooltip(plugin, player, mat, name),
                    120,
                    DialogActions.QUICK_BUY_ITEM_SELECT + targetSlot + "_" + mat.name()
            );
        }

        screen.exit("&cCancel", DialogActions.QUICK_BUY_CANCEL, 120);
        return show(player, screen.build());
    }

    public boolean openEnchantments(Player player, int targetSlot, Material material) {
        if (material == null || !isEnchantable(material)) {
            return openAmount(player, targetSlot, material);
        }
        DialogSession session = session(player);
        if (session.getQuickBuySlot() != targetSlot || session.getQuickBuyMaterial() != material) {
            session.startQuickBuy(targetSlot, material);
        }
        Map<Enchantment, Integer> selected = session.getQuickBuyEnchantments();

        ItemStack preview = new ItemStack(material);
        if (!selected.isEmpty()) {
            ItemMeta meta = preview.getItemMeta();
            if (meta != null) {
                if (material == Material.ENCHANTED_BOOK && meta instanceof EnchantmentStorageMeta esm) {
                    for (Map.Entry<Enchantment, Integer> entry : selected.entrySet()) {
                        esm.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                    }
                } else {
                    for (Map.Entry<Enchantment, Integer> entry : selected.entrySet()) {
                        meta.addEnchant(entry.getKey(), entry.getValue(), true);
                    }
                }
                preview.setItemMeta(meta);
            }
        }

        DialogFactory.Screen screen = screen(player)
                .title("Choose Enchantments")
                .item(preview)
                .columns(ENCHANT_COLUMNS);

        List<EnchantmentSpec> specs = getEnchantmentSpecs(plugin, material);
        for (EnchantmentSpec spec : specs) {
            int currentLevel = 0;
            if (spec.enchantment() != null) {
                currentLevel = selected.getOrDefault(spec.enchantment(), 0);
            } else {
                for (Map.Entry<Enchantment, Integer> e : selected.entrySet()) {
                    if (e.getKey() != null && e.getKey().getKey().getKey().equalsIgnoreCase(spec.key())) {
                        currentLevel = e.getValue();
                        break;
                    }
                }
            }
            String enchKey = spec.key().toLowerCase(Locale.ROOT);

            // Column 0: Enchantment Name Button
            Component nameComp;
            if (spec.isCurse()) {
                NamedTextColor color = (currentLevel > 0) ? NamedTextColor.GREEN : NamedTextColor.RED;
                nameComp = Component.text(spec.displayName(), color).decoration(TextDecoration.ITALIC, false);
            } else {
                NamedTextColor color = (currentLevel > 0) ? NamedTextColor.GREEN : NamedTextColor.WHITE;
                String text = spec.displayName() + (spec.maxLevel() > 1 ? (" " + (currentLevel > 0 ? toRoman(currentLevel) : "I")) : "");
                nameComp = Component.text(text, color).decoration(TextDecoration.ITALIC, false);
            }

            int nameTargetLevel = currentLevel > 0 ? 0 : 1;
            screen.button(
                    nameComp,
                    null,
                    ENCHANT_NAME_WIDTH,
                    DialogActions.QUICK_BUY_ENCHANT_LVL + targetSlot + "/" + material.name() + "/" + enchKey + "/" + nameTargetLevel
            );

            // Columns 1 to 5: Roman numeral level buttons or inert placeholders
            for (int lvl = 1; lvl <= 5; lvl++) {
                if (lvl <= spec.maxLevel()) {
                    String roman = toRoman(lvl);
                    NamedTextColor color = (currentLevel == lvl) ? NamedTextColor.GREEN : NamedTextColor.WHITE;
                    Component lvlComp = Component.text(roman, color).decoration(TextDecoration.ITALIC, false);
                    screen.button(
                            lvlComp,
                            null,
                            ENCHANT_LEVEL_WIDTH,
                            DialogActions.QUICK_BUY_ENCHANT_LVL + targetSlot + "/" + material.name() + "/" + enchKey + "/" + lvl
                    );
                } else {
                    screen.inertButton("", null, ENCHANT_INERT_WIDTH);
                }
            }
        }

        // Bottom row:
        // Centered side-by-side buttons: Back to Items & Skip/Confirm Enchantments
        screen.button(
                Component.text("Back to Items", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                null,
                ENCHANT_BOTTOM_WIDTH,
                DialogActions.QUICK_BUY_ENCHANT_BACK + targetSlot
        );
        if (selected.isEmpty()) {
            screen.button(
                    Component.text("Skip Enchantments", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                    null,
                    ENCHANT_BOTTOM_WIDTH,
                    DialogActions.QUICK_BUY_ENCHANT_SKIP + targetSlot + "/" + material.name()
            );
        } else {
            screen.button(
                    Component.text("Confirm Enchantments", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                    null,
                    ENCHANT_BOTTOM_WIDTH,
                    DialogActions.QUICK_BUY_ENCHANT_CONFIRM + targetSlot + "/" + material.name()
            );
        }

        return show(player, screen.build());
    }

    public boolean openAmount(Player player, int targetSlot, Material material) {
        if (material == null || isBlacklisted(plugin, material)) {
            return false;
        }
        int max = material.getMaxStackSize();
        ItemStack icon = new ItemStack(material);
        Map<Enchantment, Integer> enchants = session(player).getQuickBuyEnchantments();
        if (!enchants.isEmpty()) {
            ItemMeta meta = icon.getItemMeta();
            if (meta != null) {
                if (material == Material.ENCHANTED_BOOK && meta instanceof EnchantmentStorageMeta esm) {
                    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                        esm.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                    }
                } else {
                    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                        meta.addEnchant(entry.getKey(), entry.getValue(), true);
                    }
                }
                icon.setItemMeta(meta);
            }
        }
        DialogFactory.Screen screen = screen(player)
                .title("How many to buy?")
                .item(icon)
                .text("&fMax per purchase: " + max)
                .columns(AMOUNT_COLUMNS)
                .input(new DialogConfig.InputSpec(
                        "amount",
                        "Amount",
                        AMOUNT_INPUT_WIDTH,
                        10,
                        "1",
                        List.of()
                ))
                .button("&cCancel", null, AMOUNT_BUTTON_WIDTH, DialogActions.QUICK_BUY_CANCEL)
                .button(
                        "&fAdd to Quick Buy",
                        null,
                        AMOUNT_BUTTON_WIDTH,
                        DialogActions.QUICK_BUY_CONFIRM + targetSlot + "_" + material.name()
                );

        return show(player, screen.build());
    }

    public boolean openAuctionSearch(Player player, String currentQuery, String source) {
        String query = currentQuery == null ? "" : currentQuery;
        DialogFactory.Screen screen = screen(player)
                .title("Search Auction")
                .item(new ItemStack(Material.OAK_SIGN))
                .columns(2)
                .input(new DialogConfig.InputSpec("query", "Search", 300, 32, query, List.of()))
                .button("&cCancel", null, 120, DialogActions.QUICK_BUY_SEARCH_CANCEL + source)
                .button("&aSearch", null, 120, DialogActions.QUICK_BUY_SEARCH_SUBMIT + source);

        return show(player, screen.build());
    }

    @Override
    public boolean handle(Player player, String action, DialogResponseView response) {
        if (action.equals(DialogActions.QUICK_BUY_CANCEL)) {
            session(player).clearQuickBuy();
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            DialogSupport.close(player);
            plugin.getSpigotScheduler().runEntity(player, () -> new QuickBuyMenu(plugin).open(player));
            return true;
        }

        if (action.startsWith(DialogActions.QUICK_BUY_SEARCH_CANCEL)) {
            String source = action.substring(DialogActions.QUICK_BUY_SEARCH_CANCEL.length());
            playSearchDialogSound(player, source, false);
            DialogSupport.close(player);
            plugin.getSpigotScheduler().runEntity(player, () -> {
                if (isYourItemsSearch(source)) {
                    new AuctionYourItemsMenu(plugin, yourItemsOrigin(source)).open(player);
                } else if ("transactions".equalsIgnoreCase(source)) {
                    new QuickBuyTransactionsMenu(plugin, 0).open(player);
                } else if ("ah_browse".equalsIgnoreCase(source)) {
                    new AuctionHouseBrowseMenu(plugin, 1, com.bx.ultimateDonutSmp2.managers.AuctionHouseManager.AuctionSort.NEWEST).open(player);
                } else if (source.startsWith("select_")) {
                    try {
                        int targetSlot = Integer.parseInt(source.substring(7));
                        new QuickBuyItemSelectMenu(plugin, targetSlot, 0, "").open(player);
                    } catch (NumberFormatException e) {
                        new QuickBuyMenu(plugin).open(player);
                    }
                } else {
                    new QuickBuyMenu(plugin).open(player);
                }
            });
            return true;
        }

        if (action.startsWith(DialogActions.QUICK_BUY_SEARCH_SUBMIT)) {
            String source = action.substring(DialogActions.QUICK_BUY_SEARCH_SUBMIT.length());
            playSearchDialogSound(player, source, true);
            String query = input(response, "query");
            String trimmedQuery = query == null || query.isBlank() ? "" : query.trim();
            DialogSupport.close(player);
            plugin.getSpigotScheduler().runEntity(player, () -> {
                if (isYourItemsSearch(source)) {
                    new AuctionYourItemsMenu(
                            plugin,
                            yourItemsOrigin(source),
                            AuctionYourItemsMenu.YourItemsFilter.DEFAULT,
                            trimmedQuery
                    ).open(player);
                } else if ("transactions".equalsIgnoreCase(source)) {
                    new QuickBuyTransactionsMenu(plugin, 0, trimmedQuery).open(player);
                } else if ("ah_browse".equalsIgnoreCase(source)) {
                    plugin.getAuctionHouseManager().setSearchQuery(player.getUniqueId(), trimmedQuery);
                    new AuctionHouseBrowseMenu(plugin, 1, com.bx.ultimateDonutSmp2.managers.AuctionHouseManager.AuctionSort.NEWEST, "ALL", trimmedQuery).open(player);
                } else if (source.startsWith("select_")) {
                    try {
                        int targetSlot = Integer.parseInt(source.substring(7));
                        new QuickBuyItemSelectMenu(plugin, targetSlot, 0, trimmedQuery).open(player);
                    } catch (NumberFormatException e) {
                        new QuickBuyMenu(plugin).open(player);
                    }
                } else {
                    new QuickBuyMenu(plugin, QuickBuyMenu.Filter.DEFAULT, false, trimmedQuery).open(player);
                }
            });
            return true;
        }

        if (action.startsWith(DialogActions.QUICK_BUY_SEARCH)) {
            QuickBuySounds.play(player, plugin, QuickBuySounds.SEARCH);
            String slotStr = action.substring(DialogActions.QUICK_BUY_SEARCH.length());
            int slot = parseSlot(slotStr);
            String search = input(response, "search");
            openSelection(player, slot, search == null ? "" : search);
            return true;
        }

        if (action.startsWith(DialogActions.QUICK_BUY_ITEM_SELECT)) {
            click(player);
            String data = action.substring(DialogActions.QUICK_BUY_ITEM_SELECT.length());
            String[] parts = data.split("_", 2);
            if (parts.length == 2) {
                int slot = parseSlot(parts[0]);
                Material mat = Material.matchMaterial(parts[1]);
                if (mat != null) {
                    if (!isBlacklisted(plugin, mat)) {
                        session(player).startQuickBuy(slot, mat);
                        if (isEnchantable(mat)) {
                            openEnchantments(player, slot, mat);
                        } else {
                            openAmount(player, slot, mat);
                        }
                    } else {
                        QuickBuySounds.fail(player, plugin);
                    }
                    return true;
                }
            }
        }

        if (action.startsWith(DialogActions.QUICK_BUY_ENCHANT_LVL)) {
            String rest = action.substring(DialogActions.QUICK_BUY_ENCHANT_LVL.length());
            String[] parts = rest.split("/");
            if (parts.length == 4) {
                int slot = parseSlot(parts[0]);
                Material mat = Material.matchMaterial(parts[1]);
                String enchKey = parts[2];
                int lvl = 1;
                try {
                    lvl = Integer.parseInt(parts[3]);
                } catch (NumberFormatException ignored) {}

                if (mat != null) {
                    Enchantment ench = QuickBuyEntry.findEnchantment(enchKey);
                    if (ench != null) {
                        if (lvl <= 0) {
                            session(player).setQuickBuyEnchantment(ench, 0);
                        } else {
                            session(player).toggleQuickBuyEnchantment(ench, lvl);
                        }
                    }
                    click(player);
                    openEnchantments(player, slot, mat);
                    return true;
                }
            }
        }

        if (action.startsWith(DialogActions.QUICK_BUY_ENCHANT_BACK)) {
            String slotStr = action.substring(DialogActions.QUICK_BUY_ENCHANT_BACK.length());
            int slot = parseSlot(slotStr);
            session(player).clearQuickBuy();
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            openSelection(player, slot, "");
            return true;
        }

        if (action.startsWith(DialogActions.QUICK_BUY_ENCHANT_SKIP)) {
            String rest = action.substring(DialogActions.QUICK_BUY_ENCHANT_SKIP.length());
            String[] parts = rest.split("/");
            if (parts.length == 2) {
                int slot = parseSlot(parts[0]);
                Material mat = Material.matchMaterial(parts[1]);
                if (mat != null) {
                    session(player).clearQuickBuyEnchantments();
                    click(player);
                    openAmount(player, slot, mat);
                    return true;
                }
            }
        }

        if (action.startsWith(DialogActions.QUICK_BUY_ENCHANT_CONFIRM)) {
            String rest = action.substring(DialogActions.QUICK_BUY_ENCHANT_CONFIRM.length());
            String[] parts = rest.split("/");
            if (parts.length == 2) {
                int slot = parseSlot(parts[0]);
                Material mat = Material.matchMaterial(parts[1]);
                if (mat != null) {
                    click(player);
                    openAmount(player, slot, mat);
                    return true;
                }
            }
        }

        if (action.startsWith(DialogActions.QUICK_BUY_CONFIRM)) {
            String data = action.substring(DialogActions.QUICK_BUY_CONFIRM.length());
            String[] parts = data.split("_", 2);
            if (parts.length == 2) {
                int slot = parseSlot(parts[0]);
                Material mat = Material.matchMaterial(parts[1]);
                if (mat != null) {
                    if (!isBlacklisted(plugin, mat)) {
                        String amtStr = input(response, "amount");
                        int amount = 1;
                        if (amtStr != null) {
                            try {
                                amount = Integer.parseInt(amtStr.trim());
                            } catch (NumberFormatException ignored) {}
                        }
                        amount = Math.max(1, Math.min(mat.getMaxStackSize(), amount));
                        Map<Enchantment, Integer> enchants = session(player).getQuickBuyEnchantments();
                        String serializedEnchants = QuickBuyEntry.serializeEnchantments(enchants);
                        QuickBuyEntry entry = new QuickBuyEntry(slot, mat, amount, serializedEnchants, true);
                        plugin.getShopManager().setQuickBuyEntry(player.getUniqueId(), entry);
                        session(player).clearQuickBuy();
                        QuickBuySounds.play(player, plugin, QuickBuySounds.PIN);
                        DialogSupport.close(player);
                        plugin.getSpigotScheduler().runEntity(player, () -> new QuickBuyMenu(plugin).open(player));
                    } else {
                        QuickBuySounds.fail(player, plugin);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    private void playSearchDialogSound(Player player, String source, boolean submit) {
        boolean auction = isYourItemsSearch(source) || "ah_browse".equalsIgnoreCase(source);
        if (auction) {
            AuctionHouseSounds.play(player, plugin, submit ? AuctionHouseSounds.SEARCH : AuctionHouseSounds.CANCEL);
            return;
        }
        QuickBuySounds.play(player, plugin, submit ? QuickBuySounds.SEARCH : QuickBuySounds.CANCEL);
    }

    private static boolean isYourItemsSearch(String source) {
        return source != null && source.toLowerCase(Locale.ROOT).startsWith("your_items");
    }

    private static AuctionYourItemsMenu.Origin yourItemsOrigin(String source) {
        return "your_items_shop".equalsIgnoreCase(source)
                ? AuctionYourItemsMenu.Origin.SHOP
                : AuctionYourItemsMenu.Origin.AUCTION;
    }

    private int parseSlot(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Hover text for a Choose Item button: the item name, then {@code Worth:} plus the inventory
     * worth lore line (green {@code $} and compact amount). Unsellable items, a globally disabled
     * worth-lore switch, and a player who turned Worth Display off leave the tooltip off.
     */
    public static String chooseItemWorthTooltip(
            UltimateDonutSmp2 plugin,
            Player player,
            Material material,
            String itemName
    ) {
        if (plugin == null || plugin.getWorthManager() == null || material == null) {
            return null;
        }
        WorthManager worth = plugin.getWorthManager();
        if (!worth.isWorthLoreEnabled()) {
            return null;
        }
        if (player != null
                && plugin.getPlayerDataManager() != null
                && !worth.isWorthDisplayEnabledFor(player)) {
            return null;
        }
        return formatChooseItemWorthTooltip(itemName, worth.getWorthLoreLine(new ItemStack(material)));
    }

    public static String formatChooseItemWorthTooltip(String itemName, String worthLoreLine) {
        if (worthLoreLine == null || worthLoreLine.isBlank()) {
            return null;
        }
        String lore = "&7Worth: " + worthLoreLine;
        if (itemName == null || itemName.isBlank()) {
            return lore;
        }
        return "&f" + itemName + "\n" + lore;
    }

    public List<Material> getSelectableMaterials(String query) {
        return getSelectableMaterials(plugin, query);
    }

    public static List<Material> getSelectableMaterials(UltimateDonutSmp2 plugin, String query) {
        return getSelectableMaterials(plugin, query, ChooseItemBlacklist.configuredEntries(plugin));
    }

    /**
     * @param shopBlacklist names merged onto the built-in unsuitable set. Quick Buy
     *        passes {@code QUICK-BUY.CHOOSE-ITEM.BLACKLIST}. The orders picker passes
     *        an empty list so a shop.yml entry does not hide the same item there.
     */
    public static List<Material> getSelectableMaterials(
            UltimateDonutSmp2 plugin,
            String query,
            Collection<String> shopBlacklist
    ) {
        Collection<String> extras = shopBlacklist == null ? List.of() : shopBlacklist;
        String mode = "VANILLA";
        int maxItems = 2000;
        if (plugin != null && plugin.getConfigManager() != null && plugin.getConfigManager().getShop() != null) {
            mode = plugin.getConfigManager().getShop()
                    .getString("QUICK-BUY.CHOOSE-ITEM.MODE", "VANILLA")
                    .trim().toUpperCase(Locale.ROOT);
            maxItems = plugin.getConfigManager().getShop()
                    .getInt("QUICK-BUY.CHOOSE-ITEM.MAX-ITEMS", 2000);
        }
        if (maxItems <= 0) {
            maxItems = Integer.MAX_VALUE;
        }

        List<Material> list = new ArrayList<>();
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

        if ("CUSTOM".equals(mode) && plugin != null && plugin.getWorthManager() != null) {
            Set<Material> seen = new HashSet<>();
            for (WorthManager.WorthBrowserEntry entry : plugin.getWorthManager().getBrowserEntries()) {
                Material mat = entry.material();
                if (mat == null || mat.isAir() || mat.isLegacy() || mat.name().startsWith("LEGACY_")) {
                    continue;
                }
                if (ChooseItemBlacklist.isBlacklisted(mat, extras)) {
                    continue;
                }
                if (!seen.add(mat)) {
                    continue;
                }
                String name = plugin.getWorthManager().prettifyMaterial(mat).toLowerCase(Locale.ROOT);
                if (q.isBlank() || name.contains(q) || mat.name().toLowerCase(Locale.ROOT).contains(q)) {
                    list.add(mat);
                }
            }
        } else {
            // Default: VANILLA directly from vanilla Minecraft matching server version
            for (Material mat : Material.values()) {
                if (mat.isLegacy() || mat.name().startsWith("LEGACY_") || isAir(mat)) {
                    continue;
                }
                if (!isItem(mat) || ChooseItemBlacklist.isBlacklisted(mat, extras)) {
                    continue;
                }
                String name = plugin != null && plugin.getWorthManager() != null
                        ? plugin.getWorthManager().prettifyMaterial(mat).toLowerCase(Locale.ROOT)
                        : mat.name().toLowerCase(Locale.ROOT);
                if (q.isBlank() || name.contains(q) || mat.name().toLowerCase(Locale.ROOT).contains(q)) {
                    list.add(mat);
                }
            }
        }

        if (plugin != null && plugin.getWorthManager() != null) {
            list.sort(Comparator.comparing(m -> plugin.getWorthManager().prettifyMaterial(m)));
        } else {
            list.sort(Comparator.comparing(Enum::name));
        }

        if (list.size() > maxItems) {
            return new ArrayList<>(list.subList(0, maxItems));
        }
        return list;
    }

    /**
     * True when {@code material} is hidden by the Choose Item blacklist (built-in
     * survival-unsuitable items plus {@code QUICK-BUY.CHOOSE-ITEM.BLACKLIST}).
     */
    public static boolean isBlacklisted(UltimateDonutSmp2 plugin, Material material) {
        return ChooseItemBlacklist.isBlacklisted(plugin, material);
    }

    private static boolean hasRegistry() {
        try {
            return org.bukkit.Bukkit.getServer() != null && Material.STONE.isBlock();
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isAir(Material mat) {
        if (mat == null || mat == Material.AIR) {
            return true;
        }
        return mat.name().endsWith("_AIR");
    }

    private static final Set<String> KNOWN_TEXTURES = new HashSet<>();

    static {
        try (var in = QuickBuyItemDialog.class.getResourceAsStream("/minecraft_textures.txt")) {
            if (in != null) {
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty()) {
                            KNOWN_TEXTURES.add(line);
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static boolean isBlock(Material mat) {
        if (mat == null) {
            return false;
        }
        if (hasRegistry()) {
            try {
                return mat.isBlock();
            } catch (Throwable ignored) {
            }
        }
        String name = mat.name().toLowerCase(Locale.ROOT);
        if (KNOWN_TEXTURES.contains("item/" + name)) {
            return false;
        }
        return true;
    }

    public static boolean isItem(Material mat) {
        if (mat == null || mat.isLegacy() || mat.name().startsWith("LEGACY_")) {
            return false;
        }
        if (isAir(mat)) {
            return false;
        }
        if (hasRegistry()) {
            try {
                return mat.isItem();
            } catch (Throwable ignored) {
            }
        }
        String name = mat.name().toLowerCase(Locale.ROOT);
        return !name.startsWith("wall_")
                && !name.startsWith("potted_")
                && !name.startsWith("attached_")
                && !name.endsWith("_wall_sign")
                && !name.endsWith("_wall_hanging_sign")
                && !name.endsWith("_wall_fan")
                && !name.endsWith("_wall_banner")
                && !name.endsWith("_wall_skull")
                && !name.endsWith("_wall_head")
                && !name.endsWith("_wall_torch")
                && !name.endsWith("_candle_cake")
                && !name.equals("candle_cake")
                && !name.endsWith("_crop")
                && !name.endsWith("_stem")
                && !name.equals("water")
                && !name.equals("lava")
                && !name.equals("water_cauldron")
                && !name.equals("lava_cauldron")
                && !name.equals("powder_snow_cauldron")
                && !name.equals("beetroots")
                && !name.equals("carrots")
                && !name.equals("potatoes")
                && !name.equals("cocoa")
                && !name.equals("bamboo_sapling")
                && !name.equals("sweet_berry_bush")
                && !name.equals("tall_seagrass")
                && !name.equals("fire")
                && !name.equals("soul_fire")
                && !name.equals("nether_portal")
                && !name.equals("end_portal")
                && !name.equals("end_gateway")
                && !name.equals("redstone_wire")
                && !name.equals("tripwire")
                && !name.equals("moving_piston")
                && !name.equals("piston_head")
                && !name.equals("powder_snow")
                && !name.equals("frosted_ice")
                && !name.equals("bubble_column")
                && !name.equals("test_block");
    }

    public static String getSpriteTag(Material mat) {
        if (mat == null) {
            return "";
        }
        String head = getHeadTexture(mat);
        if (!head.isEmpty()) {
            return "<head:" + head + ">";
        }
        String path = getSpritePath(mat);
        if (path == null || path.isBlank()) {
            return "";
        }
        return "<item:" + path + ">";
    }

    /**
     * The skin texture that draws a head or a skull, or blank for everything else.
     *
     * <p>No atlas holds a head, but a head can be drawn from any skin texture, and a mob texture
     * lays its face out where a player skin keeps one. So a creeper head is a head wearing
     * creeper.png rather than a stand-in item that merely hints at the mob.
     */
    public static String getHeadTexture(Material mat) {
        if (mat == null) {
            return "";
        }
        String name = mat.name().toLowerCase(Locale.ROOT);
        if (mat.isLegacy() || name.startsWith("legacy_")) {
            return "";
        }
        return switch (name) {
            case "player_head" -> "entity/player/wide/steve";
            case "creeper_head" -> "entity/creeper/creeper";
            case "zombie_head" -> "entity/zombie/zombie";
            case "piglin_head" -> "entity/piglin/piglin";
            case "skeleton_skull" -> "entity/skeleton/skeleton";
            case "wither_skeleton_skull" -> "entity/skeleton/wither_skeleton";
            default -> "";
        };
    }

    public static String getSpritePath(Material mat) {
        if (mat == null) {
            return "";
        }
        String name = mat.name().toLowerCase(Locale.ROOT);
        if (mat.isLegacy() || name.startsWith("legacy_") || isAir(mat)) {
            return "";
        }

        // Heads, skulls and the shield are entity models, so nothing in the item or block atlas
        // draws them. Heads are handled by getHeadTexture instead and have no atlas path at all.
        if (name.equals("shield")) {
            return "item/iron_ingot";
        }
        if (name.endsWith("_skull") || name.endsWith("_head")) {
            // The dragon is the one head whose model does not lay its face out where a player skin
            // does, so a texture override would crop the wrong corner of dragon.png. Its own block
            // stands in for it.
            return name.equals("dragon_head") ? "block/dragon_egg" : "";
        }

        // Beds & Banners -> colored wool
        if (name.endsWith("_bed")) {
            String color = name.substring(0, name.length() - 4);
            return "block/" + color + "_wool";
        }
        if (name.endsWith("_banner")) {
            String color = name.substring(0, name.length() - 7);
            return "block/" + color + "_wool";
        }

        // Carpets
        if (name.endsWith("_carpet")) {
            if (name.equals("moss_carpet")) {
                return "block/moss_block";
            }
            if (name.equals("pale_moss_carpet")) {
                return "block/pale_moss_carpet";
            }
            String color = name.substring(0, name.length() - 7);
            return "block/" + color + "_wool";
        }

        // Harnesses (wolf armor) -> colored wool or lead
        if (name.endsWith("_harness")) {
            String color = name.substring(0, name.length() - 8);
            if (hasTexture("block/" + color + "_wool")) {
                return "block/" + color + "_wool";
            }
            return "item/lead";
        }

        // Chests
        if (name.equals("chest") || name.equals("trapped_chest")) {
            return "block/oak_planks";
        }
        if (name.equals("ender_chest")) {
            return "block/obsidian";
        }
        if (name.contains("copper_chest") || name.contains("copper_golem_statue")) {
            return "block/copper_block";
        }

        // Animated / state items
        if (name.equals("clock")) {
            return "item/clock_00";
        }
        if (name.equals("compass")) {
            return "item/compass_00";
        }
        if (name.equals("recovery_compass")) {
            return "item/recovery_compass_00";
        }
        if (name.equals("crossbow")) {
            return "item/crossbow_standby";
        }
        if (name.equals("enchanted_golden_apple")) {
            return "item/golden_apple";
        }
        if (name.equals("debug_stick")) {
            return "item/stick";
        }
        if (name.equals("bundle")) {
            return "item/bundle";
        }
        if (name.equals("tipped_arrow")) {
            return hasTexture("item/tipped_arrow_base") ? "item/tipped_arrow_base" : "item/arrow";
        }

        // Eggs
        if (name.equals("blue_egg") || name.equals("brown_egg")) {
            return "item/egg";
        }

        // Spears
        if (name.endsWith("_spear")) {
            String base = name.substring(0, name.length() - 6);
            if (hasTexture("item/" + base + "_sword")) {
                return "item/" + base + "_sword";
            }
            return "item/trident";
        }

        // Copper nugget
        if (name.equals("copper_nugget")) {
            return "item/iron_nugget";
        }

        // Goat horn
        if (name.equals("goat_horn")) {
            return "item/goat_horn";
        }

        // Check item/ first if it exists in textures
        if (hasTexture("item/" + name)) {
            return "item/" + name;
        }

        // Clean prefixes (waxed_, infested_)
        String clean = name;
        if (clean.startsWith("waxed_")) {
            clean = clean.substring(6);
        }
        if (clean.startsWith("infested_")) {
            clean = clean.substring(9);
            if (clean.endsWith("_brick")) {
                clean += "s";
            }
        }

        // Check if clean item sprite exists (e.g. waxed_copper_door -> item/copper_door)
        if (hasTexture("item/" + clean)) {
            return "item/" + clean;
        }

        // Direct block check
        if (hasTexture("block/" + clean)) {
            return "block/" + clean;
        }

        // Block overrides
        String override = getBlockSpriteOverride(clean);
        if (override != null) {
            return "block/" + override;
        }

        // Glass panes
        if (clean.equals("glass_pane")) {
            return "block/glass";
        }
        if (clean.endsWith("_stained_glass_pane")) {
            String color = clean.substring(0, clean.length() - 19);
            return "block/" + color + "_stained_glass";
        }

        // Wood logs
        if (clean.endsWith("_wood")) {
            return "block/" + clean.substring(0, clean.length() - 5) + "_log";
        }
        if (clean.endsWith("_hyphae")) {
            return "block/" + clean.substring(0, clean.length() - 7) + "_stem";
        }

        // Slabs, stairs, walls, fences, buttons, pressure plates
        String base = extractBaseBlock(clean);
        if (base != null) {
            return "block/" + resolveBasePlankOrStone(base);
        }

        // Fallback: if isBlock or registry says block, try block/
        if (isBlock(mat)) {
            if (hasTexture("block/" + clean)) {
                return "block/" + clean;
            }
        } else {
            if (hasTexture("item/" + clean)) {
                return "item/" + clean;
            }
        }

        // If known textures is populated and not found, return empty string so no magenta error appears
        if (!KNOWN_TEXTURES.isEmpty()) {
            if (KNOWN_TEXTURES.contains("item/" + name)) {
                return "item/" + name;
            }
            if (KNOWN_TEXTURES.contains("block/" + clean)) {
                return "block/" + clean;
            }
            return "";
        }

        return "block/" + clean;
    }

    private static boolean hasTexture(String path) {
        return KNOWN_TEXTURES.contains(path);
    }

    private static String getBlockSpriteOverride(String name) {
        return switch (name) {
            case "ancient_debris" -> "ancient_debris_side";
            case "barrel" -> "barrel_side";
            case "basalt" -> "basalt_side";
            case "polished_basalt" -> "polished_basalt_side";
            case "beehive" -> "beehive_front";
            case "bee_nest" -> "bee_nest_front";
            case "big_dripleaf" -> "big_dripleaf_top";
            case "small_dripleaf" -> "small_dripleaf_top";
            case "tnt" -> "tnt_side";
            case "crafting_table" -> "crafting_table_front";
            case "furnace" -> "furnace_front";
            case "blast_furnace" -> "blast_furnace_front";
            case "smoker" -> "smoker_front";
            case "fletching_table" -> "fletching_table_front";
            case "smithing_table" -> "smithing_table_front";
            case "cartography_table" -> "cartography_table_side1";
            case "loom" -> "loom_front";
            case "hay_block" -> "hay_block_side";
            case "piston" -> "piston_side";
            case "sticky_piston" -> "piston_top_sticky";
            case "dispenser" -> "dispenser_front";
            case "dropper" -> "dropper_front";
            case "observer" -> "observer_front";
            case "daylight_detector" -> "daylight_detector_top";
            case "chiseled_bookshelf" -> "chiseled_bookshelf_empty";
            case "crafter" -> "crafter_top";
            case "lectern" -> "lectern_top";
            case "bone_block" -> "bone_block_side";
            case "azalea" -> "azalea_side";
            case "flowering_azalea" -> "flowering_azalea_side";
            case "grass_block" -> "grass_block_side";
            case "dirt_path" -> "dirt_path_side";
            case "podzol" -> "podzol_side";
            case "mycelium" -> "mycelium_side";
            case "cactus" -> "cactus_side";
            case "melon" -> "melon_side";
            case "pumpkin" -> "pumpkin_side";
            case "jukebox" -> "jukebox_top";
            case "enchanting_table" -> "enchanting_table_top";
            case "end_portal_frame" -> "end_portal_frame_top";
            case "composter" -> "composter_side";
            case "grindstone" -> "grindstone_side";
            case "stonecutter" -> "stonecutter_top";
            case "lodestone" -> "lodestone_top";
            case "respawn_anchor" -> "respawn_anchor_top";
            case "sculk_sensor" -> "sculk_sensor_top";
            case "calibrated_sculk_sensor" -> "calibrated_sculk_sensor_top";
            case "sculk_catalyst" -> "sculk_catalyst_top";
            case "sculk_shrieker" -> "sculk_shrieker_top";
            case "sniffer_egg" -> "sniffer_egg";
            case "trial_spawner" -> "trial_spawner_top_inactive";
            case "vault" -> "vault_top";
            case "honey_block" -> "honey_block_side";
            case "dried_kelp_block" -> "dried_kelp_side";
            case "dried_ghast" -> "dried_ghast_hydration_0_top";
            case "scaffolding" -> "scaffolding_top";
            case "magma_block" -> "magma";
            case "quartz_block" -> "quartz_block_side";
            case "smooth_quartz" -> "quartz_block_bottom";
            case "sandstone" -> "sandstone";
            case "smooth_sandstone" -> "sandstone_top";
            case "red_sandstone" -> "red_sandstone";
            case "smooth_red_sandstone" -> "red_sandstone_top";
            case "light_weighted_pressure_plate" -> "gold_block";
            case "heavy_weighted_pressure_plate" -> "iron_block";
            case "mangrove_roots" -> "mangrove_roots_side";
            case "muddy_mangrove_roots" -> "muddy_mangrove_roots_side";
            case "suspicious_sand" -> "suspicious_sand_0";
            case "suspicious_gravel" -> "suspicious_gravel_0";
            case "pointed_dripstone" -> "pointed_dripstone_down_tip";
            case "conduit" -> "conduit";
            case "chipped_anvil" -> "chipped_anvil_top";
            case "damaged_anvil" -> "damaged_anvil_top";
            case "sunflower" -> "sunflower_front";
            case "lilac" -> "lilac_top";
            case "rose_bush" -> "rose_bush_top";
            case "peony" -> "peony_top";
            case "tall_grass" -> "tall_grass_top";
            case "large_fern" -> "large_fern_top";
            case "pitcher_plant" -> "pitcher_plant_top";
            case "snow_block" -> "snow";
            case "target" -> "target_top";
            case "command_block" -> "command_block_front";
            case "repeating_command_block" -> "repeating_command_block_front";
            case "chain_command_block" -> "chain_command_block_front";
            case "jigsaw" -> "jigsaw_side";
            case "decorated_pot" -> "terracotta";
            case "reinforced_deepslate" -> "reinforced_deepslate_top";
            case "copper_bars" -> "copper_grate";
            case "copper_chain" -> "chain";
            case "copper_lantern" -> "lantern";
            case "copper_bulb" -> "copper_bulb";
            case "ochre_froglight" -> "ochre_froglight_side";
            case "verdant_froglight" -> "verdant_froglight_side";
            case "pearlescent_froglight" -> "pearlescent_froglight_side";
            default -> null;
        };
    }

    private static String extractBaseBlock(String clean) {
        if (clean.endsWith("_slab")) {
            return clean.substring(0, clean.length() - 5);
        } else if (clean.endsWith("_stairs")) {
            return clean.substring(0, clean.length() - 7);
        } else if (clean.endsWith("_fence_gate")) {
            return clean.substring(0, clean.length() - 11);
        } else if (clean.endsWith("_fence")) {
            return clean.substring(0, clean.length() - 6);
        } else if (clean.endsWith("_wall")) {
            return clean.substring(0, clean.length() - 5);
        } else if (clean.endsWith("_button")) {
            return clean.substring(0, clean.length() - 7);
        } else if (clean.endsWith("_pressure_plate")) {
            return clean.substring(0, clean.length() - 15);
        }
        return null;
    }

    private static String resolveBasePlankOrStone(String base) {
        return switch (base) {
            case "oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
                 "mangrove", "cherry", "crimson", "warped", "pale_oak" -> base + "_planks";
            case "petrified_oak" -> "oak_planks";
            case "bamboo" -> "bamboo_planks";
            case "bamboo_mosaic" -> "bamboo_mosaic";
            case "brick" -> "bricks";
            case "purpur" -> "purpur_block";
            case "quartz" -> "quartz_block_side";
            case "smooth_quartz" -> "quartz_block_bottom";
            case "sandstone" -> "sandstone";
            case "smooth_sandstone" -> "sandstone_top";
            case "red_sandstone" -> "red_sandstone";
            case "smooth_red_sandstone" -> "red_sandstone_top";
            default -> {
                if (base.endsWith("_brick")) {
                    yield base + "s";
                }
                if (base.endsWith("_tile")) {
                    yield base + "s";
                }
                yield base;
            }
        };
    }

    public static boolean isEnchantable(Material material) {
        return getEnchantmentCategory(material) != null;
    }

    public static String getEnchantmentCategory(Material material) {
        if (material == null) return null;
        String name = material.name();
        if (name.endsWith("_SWORD")) return "sword";
        if (name.endsWith("_HELMET") || name.equals("TURTLE_HELMET")) return "helmet";
        if (name.endsWith("_CHESTPLATE")) return "chestplate";
        if (name.endsWith("_LEGGINGS")) return "leggings";
        if (name.endsWith("_BOOTS")) return "boots";
        if (name.equals("ELYTRA")) return "elytra";
        if (name.equals("BOW")) return "bow";
        if (name.equals("CROSSBOW")) return "crossbow";
        if (name.equals("FISHING_ROD")) return "fishing_rod";
        if (name.endsWith("_SHOVEL")) return "shovel";
        if (name.endsWith("_PICKAXE")) return "pickaxe";
        if (name.endsWith("_AXE")) return "axe";
        if (name.endsWith("_HOE")) return "hoe";
        if (name.equals("SHIELD")) return "shield";
        if (name.equals("TRIDENT")) return "trident";
        if (name.equals("MACE")) return "mace";
        if (name.equals("SHEARS") || name.equals("FLINT_AND_STEEL") || name.equals("CARROT_ON_A_STICK")
                || name.equals("WARPED_FUNGUS_ON_A_STICK") || name.equals("BRUSH")) {
            return "utility";
        }
        if (name.equals("BOOK") || name.equals("ENCHANTED_BOOK")) return "book";
        return null;
    }

    public static List<EnchantmentSpec> getEnchantmentSpecs(UltimateDonutSmp2 plugin, Material material) {
        if (material == null) return Collections.emptyList();
        String category = getEnchantmentCategory(material);
        if (category == null) return Collections.emptyList();

        List<EnchantmentSpec> specs = new ArrayList<>();
        switch (category) {
            case "sword" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "sharpness", 5);
                addSpec(specs, "smite", 5);
                addSpec(specs, "bane_of_arthropods", 5);
                addSpec(specs, "sweeping_edge", 3);
                addSpec(specs, "fire_aspect", 2);
                addSpec(specs, "knockback", 2);
                addSpec(specs, "looting", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "helmet" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "protection", 4);
                addSpec(specs, "fire_protection", 4);
                addSpec(specs, "blast_protection", 4);
                addSpec(specs, "projectile_protection", 4);
                addSpec(specs, "respiration", 3);
                addSpec(specs, "aqua_affinity", 1);
                addSpec(specs, "thorns", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "chestplate" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "protection", 4);
                addSpec(specs, "fire_protection", 4);
                addSpec(specs, "blast_protection", 4);
                addSpec(specs, "projectile_protection", 4);
                addSpec(specs, "thorns", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "leggings" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "protection", 4);
                addSpec(specs, "fire_protection", 4);
                addSpec(specs, "blast_protection", 4);
                addSpec(specs, "projectile_protection", 4);
                addSpec(specs, "swift_sneak", 3);
                addSpec(specs, "thorns", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "boots" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "protection", 4);
                addSpec(specs, "fire_protection", 4);
                addSpec(specs, "blast_protection", 4);
                addSpec(specs, "projectile_protection", 4);
                addSpec(specs, "feather_falling", 4);
                addSpec(specs, "depth_strider", 3);
                addSpec(specs, "frost_walker", 2);
                addSpec(specs, "soul_speed", 3);
                addSpec(specs, "thorns", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "pickaxe", "shovel", "hoe" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "efficiency", 5);
                addSpec(specs, "fortune", 3);
                addSpec(specs, "silk_touch", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "axe" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "sharpness", 5);
                addSpec(specs, "smite", 5);
                addSpec(specs, "bane_of_arthropods", 5);
                addSpec(specs, "efficiency", 5);
                addSpec(specs, "fortune", 3);
                addSpec(specs, "silk_touch", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "bow" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "power", 5);
                addSpec(specs, "punch", 2);
                addSpec(specs, "flame", 1);
                addSpec(specs, "infinity", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "crossbow" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "quick_charge", 3);
                addSpec(specs, "multishot", 1);
                addSpec(specs, "piercing", 4);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "fishing_rod" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "luck_of_the_sea", 3);
                addSpec(specs, "lure", 3);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "trident" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "impaling", 5);
                addSpec(specs, "loyalty", 3);
                addSpec(specs, "riptide", 3);
                addSpec(specs, "channeling", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "mace" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "density", 5);
                addSpec(specs, "breach", 4);
                addSpec(specs, "wind_burst", 3);
                addSpec(specs, "smite", 5);
                addSpec(specs, "bane_of_arthropods", 5);
                addSpec(specs, "fire_aspect", 2);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "elytra" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "shield", "utility" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
            case "book" -> {
                addSpec(specs, "curse_of_vanishing", 1);
                addSpec(specs, "curse_of_binding", 1);
                addSpec(specs, "sharpness", 5);
                addSpec(specs, "smite", 5);
                addSpec(specs, "protection", 4);
                addSpec(specs, "efficiency", 5);
                addSpec(specs, "fortune", 3);
                addSpec(specs, "silk_touch", 1);
                addSpec(specs, "looting", 3);
                addSpec(specs, "power", 5);
                addSpec(specs, "unbreaking", 3);
                addSpec(specs, "mending", 1);
            }
        }

        if (plugin != null && plugin.getConfigManager() != null) {
            try {
                var encCfg = plugin.getConfigManager().getEnchantments();
                if (encCfg != null && encCfg.isConfigurationSection(category)) {
                    var sec = encCfg.getConfigurationSection(category);
                    if (sec != null) {
                        for (String childKey : sec.getKeys(false)) {
                            String encVal = sec.getString(childKey + ".enchantment");
                            if (encVal != null && !encVal.isBlank()) {
                                String enchId = encVal.split(";")[0].trim().toLowerCase(Locale.ROOT);
                                if (enchId.startsWith("minecraft:")) enchId = enchId.substring(10);
                                if (enchId.equals("vanishing_curse")) enchId = "curse_of_vanishing";
                                if (enchId.equals("binding_curse")) enchId = "curse_of_binding";
                                final String finalId = enchId;
                                if (specs.stream().noneMatch(s -> s.key().equalsIgnoreCase(finalId))) {
                                    addSpec(specs, finalId, 1);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}
        }

        return specs;
    }

    private static void addSpec(List<EnchantmentSpec> list, String key, int fallbackMaxLevel) {
        Enchantment ench = null;
        try {
            if (org.bukkit.Bukkit.getServer() != null) {
                ench = QuickBuyEntry.findEnchantment(key);
            }
        } catch (Throwable ignored) {}
        boolean isCurse = key.contains("curse") || (ench != null && isCurseEnchantment(ench));
        String name = prettifyEnchantmentName(key);
        int maxLvl = fallbackMaxLevel;
        if (ench != null) {
            try {
                int bukkitMax = ench.getMaxLevel();
                if (bukkitMax > 0) {
                    maxLvl = bukkitMax;
                }
            } catch (Throwable ignored) {}
        }
        list.add(new EnchantmentSpec(key, ench, name, isCurse, maxLvl));
    }

    public static boolean isCurseEnchantment(Enchantment ench) {
        if (ench == null) return false;
        try {
            if (ench.isCursed()) return true;
        } catch (Throwable ignored) {}
        try {
            String key = ench.getKey().getKey().toLowerCase(Locale.ROOT);
            return key.contains("curse");
        } catch (Throwable ignored) {}
        return false;
    }

    public static String prettifyEnchantment(Enchantment ench) {
        if (ench == null) return "";
        return prettifyEnchantmentName(ench.getKey().getKey());
    }

    public static String prettifyEnchantmentName(String key) {
        if (key == null || key.isBlank()) return "";
        String clean = key.trim().toLowerCase(Locale.ROOT);
        if (clean.startsWith("minecraft:")) {
            clean = clean.substring("minecraft:".length());
        }
        return switch (clean) {
            case "curse_of_vanishing", "vanishing_curse" -> "Curse of Vanishing";
            case "curse_of_binding", "binding_curse" -> "Curse of Binding";
            case "sharpness" -> "Sharpness";
            case "smite" -> "Smite";
            case "bane_of_arthropods" -> "Bane of Arthropods";
            case "sweeping_edge", "sweeping" -> "Sweeping Edge";
            case "fire_aspect" -> "Fire Aspect";
            case "knockback" -> "Knockback";
            case "looting" -> "Looting";
            case "unbreaking" -> "Unbreaking";
            case "mending" -> "Mending";
            case "protection" -> "Protection";
            case "fire_protection" -> "Fire Protection";
            case "blast_protection" -> "Blast Protection";
            case "projectile_protection" -> "Projectile Protection";
            case "feather_falling" -> "Feather Falling";
            case "respiration" -> "Respiration";
            case "aqua_affinity" -> "Aqua Affinity";
            case "thorns" -> "Thorns";
            case "depth_strider" -> "Depth Strider";
            case "frost_walker" -> "Frost Walker";
            case "soul_speed" -> "Soul Speed";
            case "swift_sneak" -> "Swift Sneak";
            case "efficiency" -> "Efficiency";
            case "silk_touch" -> "Silk Touch";
            case "fortune" -> "Fortune";
            case "power" -> "Power";
            case "punch" -> "Punch";
            case "flame" -> "Flame";
            case "infinity" -> "Infinity";
            case "luck_of_the_sea" -> "Luck of the Sea";
            case "lure" -> "Lure";
            case "loyalty" -> "Loyalty";
            case "impaling" -> "Impaling";
            case "riptide" -> "Riptide";
            case "channeling" -> "Channeling";
            case "multishot" -> "Multishot";
            case "quick_charge" -> "Quick Charge";
            case "piercing" -> "Piercing";
            case "density" -> "Density";
            case "breach" -> "Breach";
            case "wind_burst" -> "Wind Burst";
            default -> {
                String[] parts = clean.split("_");
                StringBuilder sb = new StringBuilder();
                for (String p : parts) {
                    if (p.isEmpty()) continue;
                    if (!sb.isEmpty()) sb.append(" ");
                    if (p.equalsIgnoreCase("of") || p.equalsIgnoreCase("the")) {
                        sb.append(p.toLowerCase(Locale.ROOT));
                    } else {
                        sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1).toLowerCase(Locale.ROOT));
                    }
                }
                yield sb.toString();
            }
        };
    }

    public static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(number);
        };
    }
}