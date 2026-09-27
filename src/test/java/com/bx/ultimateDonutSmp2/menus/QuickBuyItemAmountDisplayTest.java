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

    @Test
    void quickBuyPricesThePinnedStackRatherThanOneItem() throws Exception {
        String menu = Files.readString(QUICK_BUY_MENU, StandardCharsets.UTF_8);
        assertFalse(menu.contains("quote.unitPrice()"),
                "Quick buy lore and sort must use the stack price");
        assertFalse(menu.contains("q.unitPrice()"),
                "Cheapest and most expensive must sort by the stack price");
        assertTrue(menu.contains("ShopManager.pricePaidFor(quote)"));
        assertTrue(menu.contains("ShopManager.pricePaidFor(q)"));

        String shop = Files.readString(
                Path.of("src/main/java/com/bx/ultimateDonutSmp2/managers/ShopManager.java"),
                StandardCharsets.UTF_8);
        assertFalse(shop.contains("purchaseListing"),
                "Quick buy must charge the pinned amount instead of buying one auction listing");
        assertTrue(shop.contains("double price = pricePaidFor(quote);"));
    }
}
