package com.bx.ultimateDonutSmp2.menus;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickBuyBedrockEnchantMenuTest {

    @Test
    void levelWrapsBackToNoneAfterTheMax() {
        assertEquals(1, QuickBuyBedrockEnchantMenu.nextLevel(0, 5));
        assertEquals(5, QuickBuyBedrockEnchantMenu.nextLevel(4, 5));
        assertEquals(0, QuickBuyBedrockEnchantMenu.nextLevel(5, 5));
        assertEquals(1, QuickBuyBedrockEnchantMenu.nextLevel(0, 1));
        assertEquals(0, QuickBuyBedrockEnchantMenu.nextLevel(1, 1));
    }

    @Test
    void zeroLevelsAreLeftOutOfTheSavedString() {
        Map<String, Integer> levels = new LinkedHashMap<>();
        levels.put("sharpness", 5);
        levels.put("smite", 0);
        assertEquals("", QuickBuyBedrockEnchantMenu.serialize(Map.of()));
        assertEquals("ENCHANTS:sharpness=5", QuickBuyBedrockEnchantMenu.serialize(levels));
        assertEquals(java.util.List.of("&7Sharpness 5"), QuickBuyBedrockEnchantMenu.enchantLore("ENCHANTS:sharpness=5"));
    }

    @Test
    void aFullPageStaysOnePageAndTheNextEnchantStartsAnother() {
        assertEquals(1, QuickBuyBedrockEnchantMenu.pageCount(0));
        assertEquals(1, QuickBuyBedrockEnchantMenu.pageCount(45));
        assertEquals(2, QuickBuyBedrockEnchantMenu.pageCount(46));
    }

    @Test
    void chestCatalogueOpensTheEnchantChestBeforeTheAmountMenu() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyItemSelectMenu.java"));
        int selected = source.indexOf("Material selected");
        int enchant = source.indexOf("QuickBuyBedrockEnchantMenu", selected);
        int dialog = source.indexOf("openEnchantments", selected);
        int amount = source.indexOf("new QuickBuyItemAmountMenu", selected);
        assertTrue(selected >= 0 && enchant > selected && dialog > enchant && amount > dialog,
                "Bedrock uses the enchant chest, and the Java dialog stays behind that return");
    }
}
