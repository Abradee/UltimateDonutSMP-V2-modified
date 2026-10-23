package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuPlaceholderMaterialTest {

    @Test
    void parseMaterialWithFallbackWorks() {
        assertEquals(Material.AIR, ItemUtils.parseMaterial("AIR", Material.GRAY_STAINED_GLASS_PANE));
        assertEquals(Material.BLACK_STAINED_GLASS_PANE, ItemUtils.parseMaterial("BLACK_STAINED_GLASS_PANE", Material.GRAY_STAINED_GLASS_PANE));
        assertEquals(Material.GRAY_STAINED_GLASS_PANE, ItemUtils.parseMaterial(null, Material.GRAY_STAINED_GLASS_PANE));
        assertEquals(Material.GRAY_STAINED_GLASS_PANE, ItemUtils.parseMaterial("", Material.GRAY_STAINED_GLASS_PANE));
        assertEquals(Material.GRAY_STAINED_GLASS_PANE, ItemUtils.parseMaterial("NON_EXISTENT_MATERIAL", Material.GRAY_STAINED_GLASS_PANE));
    }

    @Test
    void fillInventoryWithAirLeavesSlotsEmpty() {
        ItemStack[] slots = new ItemStack[27];
        Inventory mockInventory = (Inventory) Proxy.newProxyInstance(
                Inventory.class.getClassLoader(),
                new Class<?>[]{Inventory.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getSize" -> slots.length;
                    case "getItem" -> slots[(int) args[0]];
                    case "setItem" -> {
                        slots[(int) args[0]] = (ItemStack) args[1];
                        yield null;
                    }
                    default -> null;
                }
        );

        ItemUtils.fillInventory(mockInventory, Material.AIR);
        for (int i = 0; i < slots.length; i++) {
            assertNull(slots[i], "Slot " + i + " should remain null when filling with AIR");
        }

        ItemUtils.fillInventory(mockInventory, null);
        for (int i = 0; i < slots.length; i++) {
            assertNull(slots[i], "Slot " + i + " should remain null when filling with null");
        }
    }

    @Test
    void fillInventoryWithGlassFillsSlots() {
        ItemStack[] slots = new ItemStack[9];
        Inventory mockInventory = (Inventory) Proxy.newProxyInstance(
                Inventory.class.getClassLoader(),
                new Class<?>[]{Inventory.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getSize" -> slots.length;
                    case "getItem" -> slots[(int) args[0]];
                    case "setItem" -> {
                        slots[(int) args[0]] = (ItemStack) args[1];
                        yield null;
                    }
                    default -> null;
                }
        );

        ItemUtils.fillInventory(mockInventory, Material.GRAY_STAINED_GLASS_PANE);
        for (int i = 0; i < slots.length; i++) {
            assertNotNull(slots[i], "Slot " + i + " should be filled with gray glass pane");
            assertEquals(Material.GRAY_STAINED_GLASS_PANE, slots[i].getType());
        }
    }

    @Test
    void menusYmlConfiguresPlaceholderMaterial() throws Exception {
        YamlConfiguration menus;
        try (InputStream stream = getClass().getResourceAsStream("/menus.yml")) {
            assertNotNull(stream, "menus.yml resource must exist");
            menus = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }

        List<String> targetMenus = List.of(
                "SELLALL-CONFIRM-MENU",
                "TPA-CONFIRM-MENU",
                "WORTH-MENU",
                "SELL-HISTORY-MENU",
                "PURCHASE-SHOP-MENU",
                "SERVER-INFO-MENU",
                "MEDIA-MENU",
                "RULES-MENU",
                "CONFIRM-KILL-MENU"
        );

        for (String menuKey : targetMenus) {
            assertTrue(menus.contains(menuKey), "menus.yml must contain " + menuKey);
            String rawMat = menus.getString(menuKey + ".PLACEHOLDER-MATERIAL");
            assertNotNull(rawMat, menuKey + " must define PLACEHOLDER-MATERIAL");
            assertFalse(rawMat.isBlank(), menuKey + ".PLACEHOLDER-MATERIAL must not be blank");
            Material parsed = ItemUtils.parseMaterial(rawMat, null);
            assertNotNull(parsed, menuKey + ".PLACEHOLDER-MATERIAL (" + rawMat + ") must be a valid Bukkit Material");
        }
    }
}
