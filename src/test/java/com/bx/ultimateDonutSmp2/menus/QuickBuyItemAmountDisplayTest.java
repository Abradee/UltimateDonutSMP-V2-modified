package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickBuyItemAmountDisplayTest {

    private static final Path QUICK_BUY_MENU = Path.of("src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyMenu.java");

    @Test
    void quickBuyMenuDoesNotHardcodeSingleItemDisplay() throws Exception {
        String source = Files.readString(QUICK_BUY_MENU, StandardCharsets.UTF_8);
        assertFalse(source.contains("entry.createItem(1)"),
                "QuickBuyMenu must not hardcode 1 as the display item amount");
        assertTrue(source.contains("entry.createItem(displayAmount)"),
                "QuickBuyMenu must pass displayAmount based on entry.buyAmount() to createItem");
    }

    @Test
    void quickBuyEntryRetainsConfiguredAmount() {
        QuickBuyEntry glowstoneEntry = new QuickBuyEntry(0, Material.GLOWSTONE, 64);
        assertEquals(64, glowstoneEntry.buyAmount());

        QuickBuyEntry pearlEntry = new QuickBuyEntry(1, Material.ENDER_PEARL, 16);
        assertEquals(16, pearlEntry.buyAmount());

        QuickBuyEntry swordEntry = new QuickBuyEntry(2, Material.DIAMOND_SWORD, 1);
        assertEquals(1, swordEntry.buyAmount());
    }
}
