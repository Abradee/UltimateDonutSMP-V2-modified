package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.PaymentUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class PayConfirmMenu extends BaseMenu {

    public enum PaymentType {
        MONEY,
        SHARDS
    }

    private final String targetName;
    private final PaymentType paymentType;
    private final double moneyAmount;
    private final long shardAmount;

    public PayConfirmMenu(UltimateDonutSmp2 plugin, String targetName, double amount) {
        super(
                plugin,
                plugin.getConfigManager().getMenus().getString("PAY-CONFIRM-MENU.TITLE", "&8Confirm payment"),
                plugin.getConfigManager().getMenus().getInt("PAY-CONFIRM-MENU.SIZE", 27)
        );
        this.targetName = targetName;
        this.paymentType = PaymentType.MONEY;
        this.moneyAmount = amount;
        this.shardAmount = 0L;
    }

    public PayConfirmMenu(UltimateDonutSmp2 plugin, String targetName, long amount) {
        super(
                plugin,
                plugin.getConfigManager().getMenus().getString("PAY-CONFIRM-MENU.TITLE", "&8Confirm payment"),
                plugin.getConfigManager().getMenus().getInt("PAY-CONFIRM-MENU.SIZE", 27)
        );
        this.targetName = targetName;
        this.paymentType = PaymentType.SHARDS;
        this.moneyAmount = 0.0;
        this.shardAmount = amount;
    }

    public int getCancelSlot() {
        return getMenus() != null ? getMenus().getInt("PAY-CONFIRM-MENU.CANCEL-BUTTON.SLOT", 11) : 11;
    }

    public int getConfirmSlot() {
        return getMenus() != null ? getMenus().getInt("PAY-CONFIRM-MENU.CONFIRM-BUTTON.SLOT", 15) : 15;
    }

    public int getPlayerSlot() {
        return getMenus() != null ? getMenus().getInt("PAY-CONFIRM-MENU.PLAYER-BUTTON.SLOT", 13) : 13;
    }

    private org.bukkit.configuration.file.FileConfiguration getMenus() {
        return plugin != null && plugin.getConfigManager() != null ? plugin.getConfigManager().getMenus() : null;
    }

    @Override
    public void build(Player player) {
        clear();
        fill("PAY-CONFIRM-MENU", Material.GRAY_STAINED_GLASS_PANE);

        org.bukkit.configuration.file.FileConfiguration menus = getMenus();

        String amountText = paymentType == PaymentType.MONEY
                ? (plugin != null && plugin.getCurrencyManager() != null
                        ? plugin.getCurrencyManager().formatMoney(moneyAmount)
                        : String.format("$%,.2f", moneyAmount))
                : (plugin != null && plugin.getCurrencyManager() != null
                        ? plugin.getCurrencyManager().formatShards(shardAmount)
                        : String.format("%,d shards", shardAmount));

        String cancelTitle = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.CANCEL-BUTTON.TITLE", "&#FC0000Cancel")
                : "&#FC0000Cancel";
        String cancelMatStr = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.CANCEL-BUTTON.MATERIAL", "RED_STAINED_GLASS_PANE")
                : "RED_STAINED_GLASS_PANE";
        Material cancelMaterial = ItemUtils.parseMaterial(cancelMatStr);
        if (cancelMaterial == null) cancelMaterial = Material.RED_STAINED_GLASS_PANE;
        List<String> cancelLore = menus != null
                ? ItemUtils.readLore(menus, "PAY-CONFIRM-MENU.CANCEL-BUTTON.LORE", List.of("&7Click to cancel"))
                : List.of("&7Click to cancel");

        String confirmTitle = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.CONFIRM-BUTTON.TITLE", "&#00FC00Confirm")
                : "&#00FC00Confirm";
        String confirmMatStr = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.CONFIRM-BUTTON.MATERIAL", "LIME_STAINED_GLASS_PANE")
                : "LIME_STAINED_GLASS_PANE";
        Material confirmMaterial = ItemUtils.parseMaterial(confirmMatStr);
        if (confirmMaterial == null) confirmMaterial = Material.LIME_STAINED_GLASS_PANE;
        List<String> confirmLore = menus != null
                ? ItemUtils.readLore(menus, "PAY-CONFIRM-MENU.CONFIRM-BUTTON.LORE", List.of("&7Click to confirm to pay {amount}!"))
                : List.of("&7Click to confirm to pay {amount}!");

        String playerTitle = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.PLAYER-BUTTON.TITLE", "&#00FC00{player}")
                : "&#00FC00{player}";
        String playerMatStr = menus != null
                ? menus.getString("PAY-CONFIRM-MENU.PLAYER-BUTTON.MATERIAL", "PLAYER_HEAD")
                : "PLAYER_HEAD";
        Material playerMaterial = ItemUtils.parseMaterial(playerMatStr);
        if (playerMaterial == null) playerMaterial = Material.PLAYER_HEAD;
        List<String> defaultPlayerLore = List.of(
                "&7Target: &f{player}",
                "&7Amount: {amount}"
        );
        List<String> playerLore = menus != null
                ? ItemUtils.readLore(menus, "PAY-CONFIRM-MENU.PLAYER-BUTTON.LORE", defaultPlayerLore)
                : defaultPlayerLore;

        set(getCancelSlot(), ItemUtils.createItem(
                cancelMaterial,
                applyPlaceholders(cancelTitle, amountText),
                applyPlaceholders(cancelLore, amountText)
        ));

        set(getConfirmSlot(), ItemUtils.createItem(
                confirmMaterial,
                applyPlaceholders(confirmTitle, amountText),
                applyPlaceholders(confirmLore, amountText)
        ));

        set(getPlayerSlot(), createTargetItem(
                playerMaterial,
                applyPlaceholders(playerTitle, amountText),
                applyPlaceholders(playerLore, amountText)
        ));
    }

    @Override
    public void handleClick(int slot, Player player) {
        int cancelSlot = getCancelSlot();
        int confirmSlot = getConfirmSlot();

        if (slot != cancelSlot && slot != confirmSlot) return;

        SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
        player.closeInventory();

        if (slot == cancelSlot) {
            return;
        }

        if (paymentType == PaymentType.MONEY) {
            PaymentUtils.transferMoney(plugin, player, targetName, moneyAmount);
        } else {
            PaymentUtils.transferShards(plugin, player, targetName, shardAmount);
        }
    }

    private String applyPlaceholders(String text, String amountText) {
        if (text == null) return "";
        return text.replace("{player}", targetName != null ? targetName : "")
                   .replace("{amount}", amountText != null ? amountText : "");
    }

    private List<String> applyPlaceholders(List<String> lines, String amountText) {
        if (lines == null) return List.of();
        java.util.List<String> formatted = new java.util.ArrayList<>(lines.size());
        for (String line : lines) {
            formatted.add(applyPlaceholders(line, amountText));
        }
        return formatted;
    }

    private ItemStack createTargetItem(Material material, String title, List<String> lore) {
        ItemStack item = ItemUtils.createItem(material != null ? material : Material.PLAYER_HEAD, title, lore);
        if (targetName != null && !targetName.isBlank() && item.getItemMeta() instanceof SkullMeta meta) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            meta.setOwningPlayer(target);
            item.setItemMeta(meta);
        }
        return item;
    }
}
