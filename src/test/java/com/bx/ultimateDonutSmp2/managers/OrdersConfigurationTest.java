package com.bx.ultimateDonutSmp2.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersConfigurationTest {

    @Test
    void donutStyleDefaultsAndOptionalIntegrationsParse() throws Exception {
        YamlConfiguration orders = new YamlConfiguration();
        orders.load(Path.of("src/main/resources/orders.yml").toFile());

        assertEquals("DEPOSIT_GUI", orders.getString("DELIVERY.MODE"));
        assertEquals(2304, orders.getInt("DELIVERY.MAX_DELIVER_PER_TRANSACTION"));
        assertEquals("Orders (Page {page})", orders.getString("GUI.MAIN.TITLE"));
        assertEquals(54, orders.getInt("GUI.MAIN.SIZE"));
        assertEquals(45, orders.getInt("GUI.MAIN.ITEMS_PER_PAGE"));
        assertEquals(47, orders.getInt("GUI.MAIN.BUTTONS.FILTER.SLOT"));
        assertEquals(54, orders.getInt("GUI.MY_ORDERS.SIZE"));
        assertEquals(18, orders.getInt("GUI.MY_ORDERS.VISIBLE_SLOTS"));
        assertEquals(45, orders.getInt("GUI.MY_ORDERS.PLUS_PAGE_SLOTS"));
        assertEquals(36, orders.getInt("GUI.DELIVERY_DEPOSIT.SIZE"));
        assertEquals("MOST_PAID", orders.getString("SORTING.DEFAULT"));
        assertEquals(List.of("MOST_MONEY_PER_ITEM", "MOST_PAID", "RECENTLY_LISTED"), orders.getStringList("SORTING.ALLOWED"));
        assertTrue(orders.getBoolean("BEDROCK.ENABLED"));
        assertTrue(orders.getBoolean("NETWORK.ENABLED"));
        assertTrue(orders.getBoolean("SETTINGS.EXTRA_ENCHANTMENT_LORE"));
        assertEquals("ultimate-donut-smp:orders", orders.getString("NETWORK.REDIS_CHANNEL"));
        assertTrue(orders.isConfigurationSection("SEARCH_SIGN"));
        assertTrue(orders.isConfigurationSection("AMOUNT_SIGN"));
        assertTrue(orders.isConfigurationSection("PRICE_SIGN"));
    }

    @Test
    void testRootLevelFallback() throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(
                "BOTS:\n" +
                "  ENABLED: true\n" +
                "ITEMS:\n" +
                "  - MATERIAL: COBBLESTONE\n" +
                "    MIN_AMOUNT: 64\n"
        );
        org.bukkit.configuration.ConfigurationSection section = config.getConfigurationSection("BOTS");
        java.util.List<java.util.Map<?, ?>> itemsList = section.getMapList("ITEMS");
        if (itemsList == null || itemsList.isEmpty()) {
            itemsList = config.getMapList("ITEMS");
        }
        assertEquals(1, itemsList.size());
        assertEquals("COBBLESTONE", itemsList.get(0).get("MATERIAL"));
    }

    @Test
    void everyBundledLocaleProvidesOrdersOverridesAndEnglishFallbackCompletesIt() throws Exception {
        YamlConfiguration english = load("en_US");
        for (String locale : List.of("en_US", "id_ID", "de_DE", "es_ES", "fr_FR", "pt_BR", "ru_RU", "zh_CN")) {
            YamlConfiguration language = load(locale);
            assertTrue(language.isConfigurationSection("ORDERS"), locale);
            LanguageManager.mergeMissing(language, english);
            assertTrue(language.isString("ORDERS.GUI.MAIN.TITLE"), locale);
            assertTrue(language.isString("ORDERS.BEDROCK.CANCEL.CONTENT"), locale);
            assertTrue(language.isList("ORDERS.GUI.CONFIRM.SUMMARY.LORE"), locale);
            assertTrue(language.isString("ORDERS.GUI.DIALOG.SEARCH.TITLE"), locale);
            assertEquals("&cCancel", language.getString("ORDERS.GUI.DIALOG.SEARCH.CANCEL"), locale);
            assertEquals("&cCancel", language.getString("ORDERS.GUI.DIALOG.CHOOSE.CANCEL"), locale);
            assertEquals("&cCancel", language.getString("ORDERS.GUI.DIALOG.AMOUNT.CANCEL"), locale);
            assertEquals("&cCancel", language.getString("ORDERS.GUI.DIALOG.PRICE.CANCEL"), locale);
            assertEquals("&cCancel", language.getString("ORDERS.GUI.DIALOG.REVIEW.CANCEL"), locale);
            assertEquals("&7Amount: &f{amount}", language.getString("ORDERS.GUI.DIALOG.PRICE.AMOUNT"), locale);
            assertEquals("&7Minimum: &a$ &f{min}", language.getString("ORDERS.GUI.DIALOG.PRICE.MINIMUM"), locale);
            assertEquals("&7Item: &f{item}", language.getString("ORDERS.GUI.DIALOG.REVIEW.ITEM"), locale);
            assertEquals("&7Price: &a$ &f{price} each", language.getString("ORDERS.GUI.DIALOG.REVIEW.PRICE"), locale);
            assertEquals("&7Total: &a$ &f{total}", language.getString("ORDERS.GUI.DIALOG.REVIEW.TOTAL"), locale);
        }
    }

    private static YamlConfiguration load(String locale) throws Exception {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(Path.of("src/main/resources/languages", locale + ".yml").toFile());
        return configuration;
    }
}
