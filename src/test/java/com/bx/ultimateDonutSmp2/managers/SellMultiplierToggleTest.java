package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.models.SellCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellMultiplierToggleTest {

    @Test
    void multiplierDefaultsToEnabled() throws Exception {
        YamlConfiguration menus = new YamlConfiguration();
        menus.set("PROGRESS-MENU.ENABLED", true);
        menus.set("PROGRESS-MENU.LEVEL", List.of(100L, 500L, 1000L));

        ShopManager shopManager = createShopManager(menus);

        assertTrue(shopManager.isSellMultiplierEnabled());

        Map<SellCategory, Double> progress = Map.of(SellCategory.CROPS, 600.0);
        // Level 2 reached (earned 600 >= 500) -> 1.0 + (2 * 0.1) = 1.2
        assertEquals(1.2, shopManager.getCurrentSellMultiplier(progress, SellCategory.CROPS), 0.001);

        ShopManager.SellProgressInfo info = shopManager.getSellProgressInfo(progress, SellCategory.CROPS);
        assertEquals(1.2, info.currentMultiplier(), 0.001);
        assertEquals("1.3x", info.nextMultiplierDisplay());
    }

    @Test
    void multiplierDisabledPinsMultiplierToOneAndPausesProgression() throws Exception {
        YamlConfiguration menus = new YamlConfiguration();
        menus.set("PROGRESS-MENU.ENABLED", false);
        menus.set("PROGRESS-MENU.LEVEL", List.of(100L, 500L, 1000L));

        ShopManager shopManager = createShopManager(menus);

        assertFalse(shopManager.isSellMultiplierEnabled());

        Map<SellCategory, Double> progress = Map.of(SellCategory.CROPS, 600.0);
        // With ENABLED: false, multiplier must always be 1.0
        assertEquals(1.0, shopManager.getCurrentSellMultiplier(progress, SellCategory.CROPS), 0.001);

        ShopManager.SellProgressInfo info = shopManager.getSellProgressInfo(progress, SellCategory.CROPS);
        assertEquals(1.0, info.currentMultiplier(), 0.001);
        assertEquals("1.0x", info.nextMultiplierDisplay());
    }

    private ShopManager createShopManager(YamlConfiguration menusConfig) throws Exception {
        Constructor<Object> objectConstructor = Object.class.getConstructor();
        sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
        Constructor<?> pluginConstructor = reflectionFactory.newConstructorForSerialization(UltimateDonutSmp2.class, objectConstructor);
        UltimateDonutSmp2 plugin = (UltimateDonutSmp2) pluginConstructor.newInstance();

        ConfigManager configManager = new ConfigManager(plugin);
        Field menusField = ConfigManager.class.getDeclaredField("menus");
        menusField.setAccessible(true);
        menusField.set(configManager, menusConfig);

        Field cmField = UltimateDonutSmp2.class.getDeclaredField("configManager");
        cmField.setAccessible(true);
        cmField.set(plugin, configManager);

        Field descField = JavaPlugin.class.getDeclaredField("description");
        descField.setAccessible(true);
        PluginDescriptionFile pdf = new PluginDescriptionFile("UltimateDonutSmp2", "1.0", "com.bx.ultimateDonutSmp2.UltimateDonutSmp2");
        descField.set(plugin, pdf);

        Constructor<?> shopManagerConstructor = reflectionFactory.newConstructorForSerialization(ShopManager.class, objectConstructor);
        ShopManager shopManager = (ShopManager) shopManagerConstructor.newInstance();
        Field smPluginField = ShopManager.class.getDeclaredField("plugin");
        smPluginField.setAccessible(true);
        smPluginField.set(shopManager, plugin);

        return shopManager;
    }
}
