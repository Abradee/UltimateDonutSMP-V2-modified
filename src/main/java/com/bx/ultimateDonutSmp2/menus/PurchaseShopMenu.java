package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.CurrencyManager;
import com.bx.ultimateDonutSmp2.managers.ShopManager;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PurchaseShopMenu extends BaseMenu {

    private static final Pattern SHARD_PRICE_SUFFIX = Pattern.compile(
            "(?i)\\$\\{price\\}x\\s*(?:&l)?shards"
                    + "|\\{price\\}x\\s*(?:&l)?shards"
                    + "|\\$\\{price\\}\\s+shards"
                    + "|\\{price\\}\\s+shards"
                    + "|%price%\\s+shards"
    );

    private final ShopManager.ShopItem item;
    private final String originMenuSection;
    private final int originPage;
    private final boolean originFavorites;
    private int quantity;

    public PurchaseShopMenu(
            UltimateDonutSmp2 plugin,
            ShopManager.ShopItem item,
            String originMenuSection,
            int originPage
    ) {
        this(plugin, item, originMenuSection, originPage, false);
    }

    public PurchaseShopMenu(
            UltimateDonutSmp2 plugin,
            ShopManager.ShopItem item,
            String originMenuSection,
            int originPage,
            boolean originFavorites
    ) {
        super(
                plugin,
                plugin.getConfigManager().getMenus().getString("PURCHASE-SHOP-MENU.TITLE", "&8Confirmation menu"),
                plugin.getConfigManager().getMenus().getInt("PURCHASE-SHOP-MENU.SIZE", 27)
        );
        this.item = item;
        this.originMenuSection = originMenuSection;
        this.originPage = Math.max(0, originPage);
        this.originFavorites = originFavorites;
    }

    @Override
    public void build(Player player) {
        clear();

        ShopManager.ShopRestriction restriction = plugin.getShopManager().getPurchaseRestriction(item);
        if (quantity <= 0) {
            quantity = restriction.defaultQuantity();
        }
        quantity = restriction.clamp(quantity);
        fill("PURCHASE-SHOP-MENU", Material.AIR);

        buildPreviewItem(restriction);
        buildCancelButton();
        buildConfirmButton();

        if (restriction.adjustable()) {
            buildQuantityButtons();
        }
    }

    @Override
    public void handleClick(int slot, Player player) {
        ShopManager.ShopRestriction restriction = plugin.getShopManager().getPurchaseRestriction(item);

        if (slot == getCancelSlot()) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
            returnToOriginMenu(player);
            return;
        }

        if (slot == getConfirmSlot()) {
            ShopManager.PurchaseResult result = plugin.getShopManager().purchase(player, item, quantity);
            if (result.success()) {
                playSuccessSound(player);
                player.sendMessage(ColorUtils.toComponent(resolveSuccessMessage(result)));
                plugin.getShopManager().sendPurchaseActionBar(
                        player,
                        resolveItemName(),
                        quantity,
                        result.totalPrice(),
                        item.currency()
                );
                returnToOriginMenu(player);
            } else {
                playErrorSound(player);
                player.sendMessage(ColorUtils.toComponent(resolveErrorMessage(result)));
                quantity = restriction.clamp(quantity);
        fill("PURCHASE-SHOP-MENU", Material.AIR);
                build(player);
            }
            return;
        }

        int updatedQuantity = quantity;
        updatedQuantity = applyAddButtons(slot, updatedQuantity);
        updatedQuantity = applyRemoveButtons(slot, updatedQuantity);
        updatedQuantity = restriction.clamp(updatedQuantity);

        if (updatedQuantity != quantity) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
            quantity = updatedQuantity;
            build(player);
        }
    }

    private void returnToOriginMenu(Player player) {
        if (originFavorites) {
            new ShopMenu(plugin, true, originPage).open(player);
            return;
        }
        if (ShardShopMenu.MENU_SECTION.equalsIgnoreCase(originAfterPurchase(originMenuSection, false))) {
            new ShardShopMenu(plugin).open(player);
            return;
        }
        new ShopMenu(plugin, originMenuSection, originPage).open(player);
    }

    static String originAfterPurchase(String originMenuSection, boolean originFavorites) {
        if (originFavorites) {
            return "FAVORITES";
        }
        if (originMenuSection != null && ShardShopMenu.MENU_SECTION.equalsIgnoreCase(originMenuSection)) {
            return ShardShopMenu.MENU_SECTION;
        }
        return originMenuSection == null ? "" : originMenuSection;
    }

    private void buildPreviewItem(ShopManager.ShopRestriction restriction) {
        ConfigurationSection section = itemSection();
        boolean vanillaName = section != null && section.getBoolean("VANILLA-NAME", false);
        boolean hideEnchants = section != null && section.getBoolean("HIDE-ENCHANTS", false);
        boolean potion = item.material() == Material.POTION
                || item.material() == Material.SPLASH_POTION
                || item.material() == Material.LINGERING_POTION;

        List<String> lore = previewLore(
                item.lore(),
                replaceCommonPlaceholders(getCurrencyPreviewLore()),
                restriction.adjustable(),
                quantity,
                restriction.minQuantity(),
                restriction.maxQuantity(),
                plugin.getCurrencyManager().plural(currencyType())
        );

        ItemStack custom = plugin.getShopManager().createCustomItem(item);
        ItemStack preview;
        if (custom != null) {
            preview = custom;
        } else {
            preview = new ItemStack(item.material());
            if (item.enchantments() != null && !item.enchantments().isEmpty()) {
                ItemUtils.addEnchantments(preview, item.enchantments());
            }
            if (item.glint() != null) {
                ItemUtils.setGlint(preview, item.glint());
            }
        }
        ItemUtils.applyConfiguredPotion(preview, section);
        if (vanillaName) {
            preview = ItemUtils.withLore(preview, lore);
        } else {
            preview = ItemUtils.withDisplay(preview, item.displayName(), lore);
        }
        ItemUtils.hideMenuNoise(preview, !potion);
        if (hideEnchants) {
            ItemUtils.hideEnchantments(preview);
        }
        preview.setAmount(Math.min(quantity, preview.getMaxStackSize()));
        set(getPreviewSlot(), preview);
    }

    static List<String> previewLore(
            List<String> shopLore,
            String resolvedBuyPriceLine,
            boolean includePurchaseDetails,
            int quantity,
            int minQuantity,
            int maxQuantity,
            String currencyLabel
    ) {
        List<String> lore = new ArrayList<>();
        boolean shopHasPrice = false;
        if (shopLore != null) {
            for (String line : shopLore) {
                if (line == null) {
                    continue;
                }
                if (isRedundantBuyPriceLine(line)) {
                    shopHasPrice = true;
                    continue;
                }
                if (isShopPriceLine(line)) {
                    shopHasPrice = true;
                }
                lore.add(line);
            }
        }

        boolean addBuyPrice = resolvedBuyPriceLine != null && !resolvedBuyPriceLine.isBlank() && !shopHasPrice;
        if ((addBuyPrice || includePurchaseDetails) && !lore.isEmpty()) {
            lore.add("");
        }
        if (addBuyPrice) {
            lore.add(resolvedBuyPriceLine);
        }
        if (includePurchaseDetails) {
            lore.add("&7Quantity: &f" + quantity);
            lore.add("&7Allowed: &f" + minQuantity + "&7 - &f" + maxQuantity);
            lore.add("&7Currency: &f" + currencyLabel);
        }
        return lore;
    }

    static boolean isRedundantBuyPriceLine(String line) {
        String plain = ColorUtils.normalizeLabel(ColorUtils.strip(line));
        return plain.contains("buy price")
                || plain.contains("buyprice")
                || plain.contains("harga beli");
    }

    static boolean isShopPriceLine(String line) {
        String plain = ColorUtils.normalizeLabel(ColorUtils.strip(line));
        if (plain.isEmpty() || plain.contains("self destruct")) {
            return false;
        }
        return plain.contains("shards") && plain.matches(".*\\d.*");
    }

    static String collapseRedundantShardSuffix(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return SHARD_PRICE_SUFFIX.matcher(text).replaceAll("{price_formatted}");
    }

    private ConfigurationSection itemSection() {
        if (plugin.getConfigManager() == null || plugin.getConfigManager().getShop() == null || item == null) {
            return null;
        }
        return plugin.getConfigManager().getShop().getConfigurationSection(item.menuSection() + "." + item.key());
    }

    private void buildCancelButton() {
        set(getCancelSlot(), ItemUtils.createItem(
                ItemUtils.parseMaterial(getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.CANCEL.MATERIAL", "RED_STAINED_GLASS_PANE")),
                getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.CANCEL.NAME", "&cCancel"),
                replaceCommonPlaceholders(readLines("PURCHASE-SHOP-MENU.BUTTONS.CANCEL.LORE"))
        ));
    }

    private void buildConfirmButton() {
        set(getConfirmSlot(), ItemUtils.createItem(
                ItemUtils.parseMaterial(getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.MATERIAL", "LIME_STAINED_GLASS_PANE")),
                replaceCommonPlaceholders(getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.NAME", "&aConfirm")),
                replaceCommonPlaceholders(readLines("PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.LORE"))
        ));
    }

    private void buildQuantityButtons() {
        Material addMaterial = ItemUtils.parseMaterial(
                getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.MATERIAL", "LIME_STAINED_GLASS_PANE")
        );
        Material removeMaterial = ItemUtils.parseMaterial(
                getMenus().getString("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.MATERIAL", "RED_STAINED_GLASS_PANE")
        );

        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64",
                removeMaterial
        );
        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10",
                removeMaterial
        );
        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1",
                removeMaterial
        );
        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1",
                addMaterial
        );
        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10",
                addMaterial
        );
        buildQuantityButton(
                "PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64",
                addMaterial
        );
    }

    private void buildQuantityButton(String path, Material material) {
        int slot = getMenus().getInt(path + ".SLOT", -1);
        if (slot < 0) {
            return;
        }

        List<String> lore = List.of(
                "&7Current quantity: &f" + quantity,
                "&eClick to adjust the quantity"
        );
        set(slot, ItemUtils.createItem(
                material,
                replaceCommonPlaceholders(getMenus().getString(path + ".NAME", "&fAdjust")),
                lore
        ));
    }

    private int applyAddButtons(int slot, int currentQuantity) {
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1.SLOT", -1)) {
            return currentQuantity + getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1.INCREMENT", 1);
        }
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10.SLOT", -1)) {
            return currentQuantity + getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10.INCREMENT", 10);
        }
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64.SLOT", -1)) {
            return getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64.INCREMENT", 64);
        }
        return currentQuantity;
    }

    private int applyRemoveButtons(int slot, int currentQuantity) {
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1.SLOT", -1)) {
            return currentQuantity - getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1.DECREMENT", 1);
        }
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10.SLOT", -1)) {
            return currentQuantity - getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10.DECREMENT", 10);
        }
        if (slot == getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64.SLOT", -1)) {
            return currentQuantity - getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64.DECREMENT", 64);
        }
        return currentQuantity;
    }

    private String resolveSuccessMessage(ShopManager.PurchaseResult result) {
        String path = result.currency() == ShopManager.Currency.SHARD
                ? "PURCHASE-SHOP-MENU.MESSAGES.SUCCESS.SHARDS"
                : "PURCHASE-SHOP-MENU.MESSAGES.SUCCESS.MONEY";
        String fallback = result.currency() == ShopManager.Currency.SHARD
                ? "&7You bought &e{quantity} {item-name}&7 for {price_formatted}"
                : "&7You bought &e{quantity} {item-name}&7 for {price_formatted}";
        return replaceMessagePlaceholders(getMenus().getString(path, fallback));
    }

    private String resolveErrorMessage(ShopManager.PurchaseResult result) {
        return switch (result.reason()) {
            case NO_MONEY -> getMenus().getString(
                    "PURCHASE-SHOP-MENU.MESSAGES.ERROR.NO_MONEY",
                    "&cYou don't have enough "
                            + plugin.getCurrencyManager().plural(CurrencyManager.CurrencyType.MONEY)
                            + "."
            );
            case NO_SHARDS -> getMenus().getString(
                    "PURCHASE-SHOP-MENU.MESSAGES.ERROR.NO_SHARDS",
                    "&cYou don't have enough "
                            + plugin.getCurrencyManager().plural(CurrencyManager.CurrencyType.SHARDS)
                            + "."
            );
            case INVENTORY_FULL -> getMenus().getString(
                    "PURCHASE-SHOP-MENU.MESSAGES.ERROR.FULL_INVENTORY",
                    "&cYour inventory is full."
            );
            case NO_PERMISSION -> "&cYou do not have permission to buy this item.";
            case INVALID_QUANTITY -> "&cThe selected quantity is not allowed for this item.";
            case INVALID_ITEM -> "&cThis item cannot be purchased right now.";
            case NO_PLAYER_DATA -> "&cYour player data could not be loaded. Try again.";
            case REWARD_FAILED -> getMenus().getString(
                    "PURCHASE-SHOP-MENU.MESSAGES.ERROR.REWARD_FAILED",
                    "&cPurchase failed because the reward could not be delivered."
            );
        };
    }

    private String getCurrencyPreviewLore() {
        String path = "PURCHASE-SHOP-MENU.BUTTONS.MAIN.LORE.";
        String currencyKey = item.currency() == ShopManager.Currency.SHARD ? "SHARD" : "MONEY";
        return getMenus().getString(path + currencyKey, getMenus().getString(path + "DEFAULT", ""));
    }

    private String replaceMessagePlaceholders(String text) {
        CurrencyManager.CurrencyType currencyType = currencyType();
        double totalPrice = item.currency() == ShopManager.Currency.SHARD
                ? Math.round(item.pricePerUnit() * quantity)
                : item.pricePerUnit() * quantity;
        String amount = plugin.getCurrencyManager().formatAmount(currencyType, totalPrice);
        String formattedPrice = plugin.getCurrencyManager().format(currencyType, totalPrice);
        String resolved = replaceCommonPlaceholders(text);
        if (currencyType == CurrencyManager.CurrencyType.SHARDS) {
            resolved = resolved
                    .replace("{amount} shards", "{price_formatted}")
                    .replace("%amount% shards", "{price_formatted}")
                    .replace("${amount} shards", "{price_formatted}");
        }
        resolved = replacePricePlaceholders(resolved, amount, formattedPrice);
        return resolved
                .replace("{amount}", amount)
                .replace("${amount}", formattedPrice)
                .replace("%amount%", amount)
                .replace("{currency}", formattedPrice)
                .replace("{currency_name}", plugin.getCurrencyManager().name(currencyType, totalPrice))
                .replace("{currency_name_singular}", plugin.getCurrencyManager().singular(currencyType))
                .replace("{currency_name_plural}", plugin.getCurrencyManager().plural(currencyType))
                .replace("{item-name}", resolveItemName())
                .replace("{quantity}", String.valueOf(quantity))
                .replace("{Quantity}", String.valueOf(quantity));
    }

    private String replaceCommonPlaceholders(String text) {
        if (text == null) {
            return "";
        }

        CurrencyManager.CurrencyType currencyType = currencyType();
        double totalPrice = item.currency() == ShopManager.Currency.SHARD
                ? Math.round(item.pricePerUnit() * quantity)
                : item.pricePerUnit() * quantity;
        String amount = plugin.getCurrencyManager().formatAmount(currencyType, totalPrice);
        String formattedPrice = plugin.getCurrencyManager().format(currencyType, totalPrice);
        String resolved = text;
        if (currencyType == CurrencyManager.CurrencyType.SHARDS) {
            resolved = collapseRedundantShardSuffix(resolved);
        }
        return replacePricePlaceholders(resolved, amount, formattedPrice)
                .replace("{currency}", formattedPrice)
                .replace("{currency_name}", plugin.getCurrencyManager().name(currencyType, totalPrice))
                .replace("{currency_name_singular}", plugin.getCurrencyManager().singular(currencyType))
                .replace("{currency_name_plural}", plugin.getCurrencyManager().plural(currencyType))
                .replace("%quantity%", String.valueOf(quantity))
                .replace("{quantity}", String.valueOf(quantity))
                .replace("{Quantity}", String.valueOf(quantity))
                .replace("{item-name}", resolveItemName())
                .replace("{item_name}", resolveItemName());
    }

    static String replacePricePlaceholders(String text, String compactPrice, String formattedPrice) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text
                .replace("${price_formatted}", formattedPrice)
                .replace("{price_formatted}", formattedPrice)
                .replace("%price_formatted%", formattedPrice)
                .replace("${price}", formattedPrice)
                .replace("%price%", compactPrice)
                .replace("{price}", compactPrice);
    }

    private CurrencyManager.CurrencyType currencyType() {
        return item.currency() == ShopManager.Currency.SHARD
                ? CurrencyManager.CurrencyType.SHARDS
                : CurrencyManager.CurrencyType.MONEY;
    }

    private List<String> replaceCommonPlaceholders(List<String> lines) {
        List<String> replaced = new ArrayList<>();
        for (String line : lines) {
            replaced.add(replaceCommonPlaceholders(line));
        }
        return replaced;
    }

    private String resolveItemName() {
        if (item.displayName() != null && !item.displayName().isBlank()) {
            return ColorUtils.strip(item.displayName());
        }
        return plugin.getWorthManager().prettifyMaterial(item.material());
    }

    private List<String> readLines(String path) {
        if (getMenus().isList(path)) {
            return getMenus().getStringList(path);
        }

        String singleLine = getMenus().getString(path);
        if (singleLine == null || singleLine.isBlank()) {
            return List.of();
        }
        return List.of(singleLine);
    }

    private void playSuccessSound(Player player) {
        String sound = getMenus().getString(
                "PURCHASE-SHOP-MENU.SOUNDS.SUCCESS",
                plugin.getConfigManager().getSound("SHOP.BUY-SUCCESS")
        );
        SoundUtils.play(player, sound);
    }

    private void playErrorSound(Player player) {
        String sound = getMenus().getString(
                "PURCHASE-SHOP-MENU.SOUNDS.ERROR",
                plugin.getConfigManager().getSound("SHOP.NO-MONEY")
        );
        SoundUtils.play(player, sound);
    }

    private int getPreviewSlot() {
        return getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.MAIN.SLOT", 13);
    }

    private int getCancelSlot() {
        return getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.CANCEL.SLOT", 11);
    }

    private int getConfirmSlot() {
        return getMenus().getInt("PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.SLOT", 15);
    }

    private FileConfiguration getMenus() {
        return plugin.getConfigManager().getMenus();
    }
}
