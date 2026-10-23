package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuickBuyItemAmountMenu extends BaseMenu {

    private static final int ITEM_SLOT = 13;
    private static final int MINUS_TEN_SLOT = 10;
    private static final int MINUS_ONE_SLOT = 11;
    private static final int PLUS_ONE_SLOT = 15;
    private static final int PLUS_TEN_SLOT = 16;
    private static final int CANCEL_SLOT = 18;
    private static final int CONFIRM_SLOT = 26;

    private final int targetSlot;
    private final Material material;
    private final String enchants;
    private int amount;

    public QuickBuyItemAmountMenu(UltimateDonutSmp2 plugin, int targetSlot, Material material, int amount) {
        this(plugin, targetSlot, material, amount, "");
    }

    public QuickBuyItemAmountMenu(
            UltimateDonutSmp2 plugin,
            int targetSlot,
            Material material,
            int amount,
            String enchants
    ) {
        super(plugin, "&8How many to buy?", 27);
        this.targetSlot = targetSlot;
        this.material = material;
        this.enchants = enchants == null ? "" : enchants;
        this.amount = Math.max(1, Math.min(material.getMaxStackSize(), amount));
    }

    @Override
    public void build(Player player) {
        fill("QUICK-BUY-MENU", Material.GRAY_STAINED_GLASS_PANE);

        int max = material.getMaxStackSize();
        ItemStack displayStack = new ItemStack(material, Math.min(max, amount));
        if (!enchants.isBlank()) {
            displayStack = new QuickBuyEntry(targetSlot, material, amount, enchants, true).applyTo(displayStack);
        }
        List<String> lore = new ArrayList<>();
        lore.add("&7Selected Amount: &e" + amount);
        lore.add("&7Max per purchase: &f" + max);
        lore.addAll(QuickBuyBedrockEnchantMenu.enchantLore(enchants));
        set(ITEM_SLOT, ItemUtils.withDisplay(
                displayStack,
                "&f" + plugin.getWorthManager().prettifyMaterial(material),
                lore
        ));

        set(MINUS_TEN_SLOT, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&c-10", List.of("&7Decrease by 10")));
        set(MINUS_ONE_SLOT, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&c-1", List.of("&7Decrease by 1")));
        set(PLUS_ONE_SLOT, ItemUtils.createItem(Material.LIME_STAINED_GLASS_PANE, "&a+1", List.of("&7Increase by 1")));
        set(PLUS_TEN_SLOT, ItemUtils.createItem(Material.LIME_STAINED_GLASS_PANE, "&a+10", List.of("&7Increase by 10")));

        set(CANCEL_SLOT, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", List.of("&7Return to Item Selection")));
        set(CONFIRM_SLOT, ItemUtils.createItem(Material.LIME_STAINED_GLASS_PANE, "&aAdd to Quick Buy", List.of("&7Pin to slot " + targetSlot)));
    }

    @Override
    public void handleClick(int slot, Player player, ClickType clickType) {
        int max = material.getMaxStackSize();

        if (slot == CANCEL_SLOT) {
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            new QuickBuyItemSelectMenu(plugin, targetSlot, 0, "").open(player);
            return;
        }

        if (slot == CONFIRM_SLOT) {
            if (QuickBuyItemDialog.isBlacklisted(plugin, material)) {
                QuickBuySounds.fail(player, plugin);
                new QuickBuyItemSelectMenu(plugin, targetSlot, 0, "").open(player);
                return;
            }
            QuickBuySounds.play(player, plugin, QuickBuySounds.PIN);
            QuickBuyEntry entry = new QuickBuyEntry(targetSlot, material, amount, enchants, true);
            plugin.getShopManager().setQuickBuyEntry(player.getUniqueId(), entry);
            new QuickBuyMenu(plugin).open(player);
            return;
        }

        if (slot == MINUS_TEN_SLOT) {
            click(player);
            amount = Math.max(1, amount - 10);
            build(player);
            return;
        }

        if (slot == MINUS_ONE_SLOT) {
            click(player);
            amount = Math.max(1, amount - 1);
            build(player);
            return;
        }

        if (slot == PLUS_ONE_SLOT) {
            click(player);
            amount = Math.min(max, amount + 1);
            build(player);
            return;
        }

        if (slot == PLUS_TEN_SLOT) {
            click(player);
            amount = Math.min(max, amount + 10);
            build(player);
        }
    }

    private void click(Player player) {
        QuickBuySounds.click(player, plugin);
    }
}
