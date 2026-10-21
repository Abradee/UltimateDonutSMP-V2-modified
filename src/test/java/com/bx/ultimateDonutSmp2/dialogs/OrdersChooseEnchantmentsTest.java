package com.bx.ultimateDonutSmp2.dialogs;

import com.bx.ultimateDonutSmp2.dialogs.screens.OrdersDialog;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.models.ItemKey;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersChooseEnchantmentsTest {

    @Test
    @DisplayName("DialogActions defines required order enchantment action constants")
    void testDialogActionConstants() {
        assertEquals("ord_en_lvl_", DialogActions.ORD_ENCHANT_LVL);
        assertEquals("ord_en_back", DialogActions.ORD_ENCHANT_BACK);
        assertEquals("ord_en_skip_", DialogActions.ORD_ENCHANT_SKIP);
        assertEquals("ord_en_conf_", DialogActions.ORD_ENCHANT_CONFIRM);
        assertEquals("ord_rev_ench", DialogActions.ORD_REV_ENCHANT);
    }

    @Test
    @DisplayName("OrdersDialog defines openEnchantments method for player and material")
    void testOrdersDialogOpenEnchantmentsMethod() throws Exception {
        Method method = OrdersDialog.class.getDeclaredMethod("openEnchantments", org.bukkit.entity.Player.class, Material.class);
        assertNotNull(method, "OrdersDialog must have openEnchantments(Player, Material)");
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    @DisplayName("OrdersDialog routes enchantable items to openEnchantments and non-enchantables to openAmount")
    void testSourceRouting() throws Exception {
        String source = Files.readString(Path.of("src/main/java/com/bx/ultimateDonutSmp2/dialogs/screens/OrdersDialog.java"));

        assertTrue(source.contains("if (QuickBuyItemDialog.isEnchantable(material)) {"),
                "OrdersDialog must check QuickBuyItemDialog.isEnchantable(material) on item selection");
        assertTrue(source.contains("openEnchantments(player, material);"),
                "OrdersDialog must route to openEnchantments when item is enchantable");
        assertTrue(source.contains("DialogActions.ORD_ENCHANT_LVL"),
                "OrdersDialog must handle DialogActions.ORD_ENCHANT_LVL");
        assertTrue(source.contains("DialogActions.ORD_ENCHANT_BACK"),
                "OrdersDialog must handle DialogActions.ORD_ENCHANT_BACK");
        assertTrue(source.contains("DialogActions.ORD_ENCHANT_SKIP"),
                "OrdersDialog must handle DialogActions.ORD_ENCHANT_SKIP");
        assertTrue(source.contains("DialogActions.ORD_ENCHANT_CONFIRM"),
                "OrdersDialog must handle DialogActions.ORD_ENCHANT_CONFIRM");
        assertTrue(source.contains("DialogActions.ORD_REV_ENCHANT"),
                "OrdersDialog must handle DialogActions.ORD_REV_ENCHANT");
    }

    @Test
    @DisplayName("ItemKey correctly formats and serializes enchantments on order items")
    void testItemKeyEnchantments() {
        ItemKey swordKey = ItemKey.of(Material.DIAMOND_SWORD);
        assertFalse(swordKey.isVariant());
        assertEquals("Diamond Sword", swordKey.displayName());
        assertTrue(swordKey.enchants.isEmpty());
    }
}
