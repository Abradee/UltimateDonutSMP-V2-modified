package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class BaseMenu implements InventoryHolder {

    protected final UltimateDonutSmp2 plugin;
    protected Inventory inventory;

    public BaseMenu(UltimateDonutSmp2 plugin, String title, int size) {
        this.plugin = plugin;
        this.inventory = Bukkit.getServer() != null ? Bukkit.createInventory(this, size, ColorUtils.toComponent(title)) : null;
    }

    public BaseMenu(UltimateDonutSmp2 plugin, String title, org.bukkit.event.inventory.InventoryType type) {
        this.plugin = plugin;
        this.inventory = Bukkit.getServer() != null ? Bukkit.createInventory(this, type, ColorUtils.toComponent(title)) : null;
    }

    public abstract void build(Player player);

    /** Override this to handle clicks with click-type awareness. */
    public void handleClick(int slot, Player player, ClickType clickType) {
        handleClick(slot, player); // default: delegate to simple version
    }

    /** Legacy simple click handler — override if you don't need click type. */
    public void handleClick(int slot, Player player) {}

    public void onClose(Player player) {}

    public void open(Player player) {
        if (player != null) {
            DialogSupport.forget(player.getUniqueId());
        }
        MenuNavigationTracker tracker = plugin != null ? plugin.getMenuNavigationTracker() : null;
        if (tracker != null) {
            tracker.recordOpen(player, this);
            tracker.setTransitioning(player.getUniqueId(), true);
        }
        try {
            build(player);
            player.openInventory(inventory);
        } finally {
            if (tracker != null) {
                tracker.setTransitioning(player.getUniqueId(), false);
            }
        }
    }

    /**
     * Explicitly closes the menu for the player and clears their menu navigation history.
     */
    public void close(Player player) {
        if (player == null) {
            return;
        }
        if (plugin != null && plugin.getMenuNavigationTracker() != null) {
            plugin.getMenuNavigationTracker().clear(player.getUniqueId());
        }
        player.closeInventory();
    }

    /**
     * Determines whether another menu is considered the "same screen" (e.g. page turns or refreshes),
     * which replaces the current entry in navigation history rather than pushing a new parent.
     */
    public boolean isSameScreen(BaseMenu other) {
        if (other == null) {
            return false;
        }
        return this.getClass().equals(other.getClass());
    }

    /**
     * Whether Escape may reopen this menu as a parent. One-shot escrow screens return false so a
     * listed item cannot be confirmed or refunded a second time from history.
     */
    public boolean isEscBackTarget() {
        return true;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected Material getPlaceholderMaterial(String menuPath, Material defaultMaterial) {
        if (menuPath == null || menuPath.isBlank()) {
            return defaultMaterial;
        }
        org.bukkit.configuration.file.FileConfiguration menus = plugin.getConfigManager().getMenus();
        if (menus == null) {
            return defaultMaterial;
        }
        String configured = menus.getString(menuPath + ".PLACEHOLDER-MATERIAL");
        if (configured == null || configured.isBlank()) {
            return defaultMaterial;
        }
        return ItemUtils.parseMaterial(configured, defaultMaterial);
    }

    protected void fill(Material material) {
        ItemUtils.fillInventory(inventory, material);
    }

    protected void fill(String menuPath, Material defaultMaterial) {
        fill(getPlaceholderMaterial(menuPath, defaultMaterial));
    }

    protected void set(int slot, ItemStack item) {
        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, item);
        }
    }

    protected void clear() {
        inventory.clear();
    }

    protected boolean isPlaceholder(ItemStack item) {
        if (item == null) return true;
        return item.getType() == Material.GRAY_STAINED_GLASS_PANE
                || item.getType() == Material.BLACK_STAINED_GLASS_PANE;
    }

    protected void playPageTurn(Player player) {
        SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.PAGE-TURN"));
    }

    protected void playButtonClick(Player player) {
        SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
    }
}
