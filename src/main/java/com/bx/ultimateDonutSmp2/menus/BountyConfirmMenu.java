package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.BountyManager;
import com.bx.ultimateDonutSmp2.managers.TablistManager;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class BountyConfirmMenu extends BaseMenu {

    private static final int CANCEL_SLOT = 11;
    private static final int PLAYER_SLOT = 13;
    private static final int CONFIRM_SLOT = 15;

    private final UUID targetUuid;
    private final String targetName;
    private final double amount;

    public BountyConfirmMenu(UltimateDonutSmp2 plugin, UUID targetUuid, String targetName, double amount) {
        super(
                plugin,
                plugin.getConfigManager().getMenus().getString("BOUNTY-CONFIRM-MENU.TITLE", "&8Confirm bounty"),
                plugin.getConfigManager().getMenus().getInt("BOUNTY-CONFIRM-MENU.SIZE", 27)
        );
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.amount = amount;
    }

    @Override
    public void build(Player player) {
        clear();
        fill("BOUNTY-CONFIRM-MENU", Material.GRAY_STAINED_GLASS_PANE);

        FileConfiguration menus = plugin.getConfigManager().getMenus();
        set(CANCEL_SLOT, buildConfigItem(
                menus,
                "BOUNTY-CONFIRM-MENU.CANCEL-BUTTON",
                "{player}", targetName,
                "{amount}", plugin.getCurrencyManager().formatMoney(amount)
        ));
        set(PLAYER_SLOT, createTargetItem(menus));
        set(CONFIRM_SLOT, buildConfigItem(
                menus,
                "BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON",
                "{player}", targetName,
                "{amount}", plugin.getCurrencyManager().formatMoney(amount)
        ));
    }

    @Override
    public void handleClick(int slot, Player player) {
        if (slot != CANCEL_SLOT && slot != CONFIRM_SLOT) {
            return;
        }

        SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
        player.closeInventory();

        if (slot == CANCEL_SLOT) {
            return;
        }

        BountyManager.PlacementResult result = plugin.getBountyManager().placeBounty(player, targetUuid, amount);
        if (result == BountyManager.PlacementResult.FAILED_SELF) {
            player.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BOUNTY.CANT-SELF-BOUNTY")));
            return;
        }
        if (result == BountyManager.PlacementResult.FAILED_FUNDS) {
            player.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.PAY.NOT-ENOUGH-MONEY")));
        }
    }

    private ItemStack buildConfigItem(
            FileConfiguration menus,
            String path,
            String... placeholders
    ) {
        Material material = ItemUtils.parseMaterial(menus.getString(path + ".MATERIAL", "STONE"));
        String name = applyPlaceholders(menus.getString(path + ".NAME", ""), placeholders);
        List<String> lore = menus.getStringList(path + ".LORE").stream()
                .map(line -> applyPlaceholders(line, placeholders))
                .toList();
        return ItemUtils.createItem(material, name, lore);
    }

    private ItemStack createTargetItem(FileConfiguration menus) {
        ItemStack item = buildConfigItem(
                menus,
                "BOUNTY-CONFIRM-MENU.PLAYER-BUTTON",
                "{player}", targetName,
                "{amount}", plugin.getCurrencyManager().formatMoney(amount)
        );

        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            return item;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUuid);
        meta.setOwningPlayer(target);

        if (plugin.getPlayerSkinManager() != null) {
            TablistManager.SkinTexture texture = plugin.getPlayerSkinManager().resolveSkinTexture(targetUuid, targetName);
            if (texture != null && texture.isValid()) {
                ItemUtils.applyTextureToSkullMeta(meta, texture.value());
            } else {
                int slot = menus.getInt("BOUNTY-CONFIRM-MENU.PLAYER-BUTTON.SLOT", 13);
                plugin.getPlayerSkinManager().resolveSkinTextureAsync(targetUuid, targetName, resolved -> {
                    if (resolved != null && resolved.isValid()) {
                        ItemStack current = getInventory().getItem(slot);
                        if (current != null && current.getItemMeta() instanceof SkullMeta currentMeta) {
                            ItemUtils.applyTextureToSkullMeta(currentMeta, resolved.value());
                            current.setItemMeta(currentMeta);
                            getInventory().setItem(slot, current);
                        }
                    }
                });
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    private String applyPlaceholders(String input, String... placeholders) {
        String output = input == null ? "" : input;
        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            output = output.replace(placeholders[i], placeholders[i + 1]);
        }
        return output;
    }
}
