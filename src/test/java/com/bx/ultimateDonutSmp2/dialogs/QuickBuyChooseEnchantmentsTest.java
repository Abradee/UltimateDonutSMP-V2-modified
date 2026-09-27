package com.bx.ultimateDonutSmp2.dialogs;

import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import org.bukkit.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickBuyChooseEnchantmentsTest {

    @Test
    @DisplayName("isEnchantable correctly detects swords, armor, tools, ranged weapons, and utility items")
    void testIsEnchantable() {
        // Swords
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.NETHERITE_SWORD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.DIAMOND_SWORD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.IRON_SWORD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.GOLDEN_SWORD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.STONE_SWORD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.WOODEN_SWORD));

        // Armor
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.DIAMOND_HELMET));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.TURTLE_HELMET));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.NETHERITE_CHESTPLATE));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.IRON_LEGGINGS));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.GOLDEN_BOOTS));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.ELYTRA));

        // Tools
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.DIAMOND_PICKAXE));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.NETHERITE_AXE));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.IRON_SHOVEL));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.GOLDEN_HOE));

        // Ranged / Weapons / Utility
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.BOW));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.CROSSBOW));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.TRIDENT));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.MACE));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.SHIELD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.FISHING_ROD));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.SHEARS));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.FLINT_AND_STEEL));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.BRUSH));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.BOOK));
        assertTrue(QuickBuyItemDialog.isEnchantable(Material.ENCHANTED_BOOK));

        // Non-enchantables
        assertFalse(QuickBuyItemDialog.isEnchantable(Material.DIRT));
        assertFalse(QuickBuyItemDialog.isEnchantable(Material.COBBLESTONE));
        assertFalse(QuickBuyItemDialog.isEnchantable(Material.COOKED_BEEF));
        assertFalse(QuickBuyItemDialog.isEnchantable(Material.GOLDEN_APPLE));
        assertFalse(QuickBuyItemDialog.isEnchantable(Material.DIAMOND));
        assertFalse(QuickBuyItemDialog.isEnchantable(null));
    }

    @Test
    @DisplayName("Sword enchantment specs match main.png 100% in count, ordering, curses, and max levels")
    void testSwordEnchantmentSpecsMatchDesign() {
        List<QuickBuyItemDialog.EnchantmentSpec> specs = QuickBuyItemDialog.getEnchantmentSpecs(null, Material.NETHERITE_SWORD);

        assertNotNull(specs);
        assertEquals(10, specs.size(), "Sword must have exactly 10 enchantments matching main.png");

        // Row 1: Curse of Vanishing (max 1, curse)
        assertEquals("curse_of_vanishing", specs.get(0).key());
        assertEquals("Curse of Vanishing", specs.get(0).displayName());
        assertTrue(specs.get(0).isCurse());
        assertTrue(specs.get(0).maxLevel() >= 1);

        // Row 2: Sharpness (max 5)
        assertEquals("sharpness", specs.get(1).key());
        assertEquals("Sharpness", specs.get(1).displayName());
        assertFalse(specs.get(1).isCurse());
        assertEquals(5, specs.get(1).maxLevel());

        // Row 3: Smite (max 5)
        assertEquals("smite", specs.get(2).key());
        assertEquals("Smite", specs.get(2).displayName());
        assertFalse(specs.get(2).isCurse());
        assertEquals(5, specs.get(2).maxLevel());

        // Row 4: Bane of Arthropods (max 5)
        assertEquals("bane_of_arthropods", specs.get(3).key());
        assertEquals("Bane of Arthropods", specs.get(3).displayName());
        assertFalse(specs.get(3).isCurse());
        assertEquals(5, specs.get(3).maxLevel());

        // Row 5: Sweeping Edge (max 3)
        assertEquals("sweeping_edge", specs.get(4).key());
        assertEquals("Sweeping Edge", specs.get(4).displayName());
        assertFalse(specs.get(4).isCurse());
        assertEquals(3, specs.get(4).maxLevel());

        // Row 6: Fire Aspect (max 2)
        assertEquals("fire_aspect", specs.get(5).key());
        assertEquals("Fire Aspect", specs.get(5).displayName());
        assertFalse(specs.get(5).isCurse());
        assertEquals(2, specs.get(5).maxLevel());

        // Row 7: Knockback (max 2)
        assertEquals("knockback", specs.get(6).key());
        assertEquals("Knockback", specs.get(6).displayName());
        assertFalse(specs.get(6).isCurse());
        assertEquals(2, specs.get(6).maxLevel());

        // Row 8: Looting (max 3)
        assertEquals("looting", specs.get(7).key());
        assertEquals("Looting", specs.get(7).displayName());
        assertFalse(specs.get(7).isCurse());
        assertEquals(3, specs.get(7).maxLevel());

        // Row 9: Unbreaking (max 3)
        assertEquals("unbreaking", specs.get(8).key());
        assertEquals("Unbreaking", specs.get(8).displayName());
        assertFalse(specs.get(8).isCurse());
        assertEquals(3, specs.get(8).maxLevel());

        // Row 10: Mending (max 1)
        assertEquals("mending", specs.get(9).key());
        assertEquals("Mending", specs.get(9).displayName());
        assertFalse(specs.get(9).isCurse());
        assertTrue(specs.get(9).maxLevel() >= 1);
    }

    @Test
    @DisplayName("toRoman correctly converts level integers 1 to 5")
    void testToRoman() {
        assertEquals("I", QuickBuyItemDialog.toRoman(1));
        assertEquals("II", QuickBuyItemDialog.toRoman(2));
        assertEquals("III", QuickBuyItemDialog.toRoman(3));
        assertEquals("IV", QuickBuyItemDialog.toRoman(4));
        assertEquals("V", QuickBuyItemDialog.toRoman(5));
    }

    @Test
    @DisplayName("prettifyEnchantmentName produces correct humanized titles")
    void testPrettifyEnchantmentName() {
        assertEquals("Curse of Vanishing", QuickBuyItemDialog.prettifyEnchantmentName("curse_of_vanishing"));
        assertEquals("Curse of Binding", QuickBuyItemDialog.prettifyEnchantmentName("curse_of_binding"));
        assertEquals("Sharpness", QuickBuyItemDialog.prettifyEnchantmentName("sharpness"));
        assertEquals("Sweeping Edge", QuickBuyItemDialog.prettifyEnchantmentName("sweeping_edge"));
        assertEquals("Bane of Arthropods", QuickBuyItemDialog.prettifyEnchantmentName("bane_of_arthropods"));
        assertEquals("Fire Aspect", QuickBuyItemDialog.prettifyEnchantmentName("fire_aspect"));
        assertEquals("Luck of the Sea", QuickBuyItemDialog.prettifyEnchantmentName("luck_of_the_sea"));
        assertEquals("Aqua Affinity", QuickBuyItemDialog.prettifyEnchantmentName("aqua_affinity"));
    }

    @Test
    @DisplayName("DialogSession manages quick buy scratch state correctly")
    void testDialogSessionQuickBuyState() {
        DialogSession session = new DialogSession();
        assertEquals(-1, session.getQuickBuySlot());
        assertNull(session.getQuickBuyMaterial());

        session.startQuickBuy(4, Material.DIAMOND_SWORD);
        assertEquals(4, session.getQuickBuySlot());
        assertEquals(Material.DIAMOND_SWORD, session.getQuickBuyMaterial());
        assertTrue(session.getQuickBuyEnchantments().isEmpty());

        session.clearQuickBuyEnchantments();
        assertEquals(4, session.getQuickBuySlot());
        assertEquals(Material.DIAMOND_SWORD, session.getQuickBuyMaterial());
        assertTrue(session.getQuickBuyEnchantments().isEmpty());

        session.clearQuickBuy();
        assertEquals(-1, session.getQuickBuySlot());
        assertNull(session.getQuickBuyMaterial());
    }

    @Test
    @DisplayName("QuickBuyEntry enchantment helper methods operate properly")
    void testQuickBuyEntryEnchantments() {
        QuickBuyEntry emptyEntry = new QuickBuyEntry(0, Material.NETHERITE_SWORD, 1);
        assertFalse(emptyEntry.hasEnchantments());
        assertEquals("", emptyEntry.customItemData());

        QuickBuyEntry custom = new QuickBuyEntry(0, Material.NETHERITE_SWORD, 1, "ENCHANTS:sharpness=5;unbreaking=3", true);
        assertEquals("ENCHANTS:sharpness=5;unbreaking=3", custom.customItemData());
        assertEquals(1, custom.buyAmount());

        QuickBuyEntry withAmt = custom.withAmount(2);
        assertEquals(2, withAmt.buyAmount());
        assertEquals("ENCHANTS:sharpness=5;unbreaking=3", withAmt.customItemData());
    }

    @Test
    @DisplayName("Choose Enchantments layout dimensions match main.png 100%")
    void testEnchantmentDialogLayoutDimensions() {
        assertEquals(6, QuickBuyItemDialog.ENCHANT_COLUMNS, "Grid must have 6 columns (1 label + 5 levels)");
        assertEquals(145, QuickBuyItemDialog.ENCHANT_NAME_WIDTH, "Col 0 width is 145 units (290px at scale 2)");
        assertEquals(42, QuickBuyItemDialog.ENCHANT_LEVEL_WIDTH, "Cols 1-5 width is 42 units (84px at scale 2)");
        assertEquals(1, QuickBuyItemDialog.ENCHANT_INERT_WIDTH, "Inert separator width is 1 unit");
        assertEquals(160, QuickBuyItemDialog.ENCHANT_BOTTOM_WIDTH, "Bottom row buttons are 160 units (320px at scale 2)");
    }
}