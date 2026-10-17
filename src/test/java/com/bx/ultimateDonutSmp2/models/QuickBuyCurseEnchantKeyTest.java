package com.bx.ultimateDonutSmp2.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuickBuyCurseEnchantKeyTest {

    @Test
    void shopCurseNamesResolveToMinecraftIds() {
        assertEquals("vanishing_curse", QuickBuyEntry.canonicalEnchantKey("curse_of_vanishing"));
        assertEquals("vanishing_curse", QuickBuyEntry.canonicalEnchantKey("minecraft:curse_of_vanishing"));
        assertEquals("binding_curse", QuickBuyEntry.canonicalEnchantKey("Curse_Of_Binding"));
        assertEquals("sharpness", QuickBuyEntry.canonicalEnchantKey("sharpness"));
        assertEquals("", QuickBuyEntry.canonicalEnchantKey("  "));
    }
}
