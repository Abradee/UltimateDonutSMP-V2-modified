package com.bx.ultimateDonutSmp2.menus;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersExtraEnchantmentLoreTest {

    private static final Path ORDERS_YAML = Path.of("src/main/resources/orders.yml");
    private static final Path SUPPORT_SOURCE = Path.of(
            "src/main/java/com/bx/ultimateDonutSmp2/menus/OrdersMenuSupport.java");
    private static final Path MANAGER_SOURCE = Path.of(
            "src/main/java/com/bx/ultimateDonutSmp2/managers/OrdersManager.java");

    @Test
    @DisplayName("orders.yml defines SETTINGS.EXTRA_ENCHANTMENT_LORE defaulting to true")
    void testOrdersYamlDefault() throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        config.load(ORDERS_YAML.toFile());

        assertTrue(config.contains("SETTINGS.EXTRA_ENCHANTMENT_LORE"),
                "orders.yml must contain SETTINGS.EXTRA_ENCHANTMENT_LORE");
        assertTrue(config.getBoolean("SETTINGS.EXTRA_ENCHANTMENT_LORE"),
                "SETTINGS.EXTRA_ENCHANTMENT_LORE must default to true");
    }

    @Test
    @DisplayName("OrdersMenuSupport guards enchantment lore addition with isExtraEnchantmentLoreEnabled")
    void testSupportSourceGuard() throws Exception {
        String source = Files.readString(SUPPORT_SOURCE);

        assertTrue(source.contains("isExtraEnchantmentLoreEnabled("),
                "OrdersMenuSupport must define and use isExtraEnchantmentLoreEnabled");
        assertTrue(source.contains("if (isExtraEnchantmentLoreEnabled(plugin)) {"),
                "OrdersMenuSupport.decorateItem must check isExtraEnchantmentLoreEnabled before adding required enchantments");
    }

    @Test
    @DisplayName("OrdersManager defines isExtraEnchantmentLoreEnabled checking SETTINGS and GUI paths")
    void testManagerSourceToggle() throws Exception {
        String source = Files.readString(MANAGER_SOURCE);

        assertTrue(source.contains("public boolean isExtraEnchantmentLoreEnabled()"),
                "OrdersManager must have public boolean isExtraEnchantmentLoreEnabled()");
        assertTrue(source.contains("SETTINGS.EXTRA_ENCHANTMENT_LORE"),
                "OrdersManager must check SETTINGS.EXTRA_ENCHANTMENT_LORE");
        assertTrue(source.contains("GUI.EXTRA_ENCHANTMENT_LORE"),
                "OrdersManager must check GUI.EXTRA_ENCHANTMENT_LORE as fallback");
    }

    @Test
    @DisplayName("Configuration keys parse correctly for enabled and disabled states")
    void testConfigToggleParsing() {
        YamlConfiguration enabledConfig = new YamlConfiguration();
        enabledConfig.set("SETTINGS.EXTRA_ENCHANTMENT_LORE", true);
        assertTrue(enabledConfig.getBoolean("SETTINGS.EXTRA_ENCHANTMENT_LORE", true));

        YamlConfiguration disabledConfig = new YamlConfiguration();
        disabledConfig.set("SETTINGS.EXTRA_ENCHANTMENT_LORE", false);
        assertFalse(disabledConfig.getBoolean("SETTINGS.EXTRA_ENCHANTMENT_LORE", true));

        YamlConfiguration fallbackGuiConfig = new YamlConfiguration();
        fallbackGuiConfig.set("GUI.EXTRA_ENCHANTMENT_LORE", false);
        assertFalse(fallbackGuiConfig.getBoolean("GUI.EXTRA_ENCHANTMENT_LORE", true));
    }
}
