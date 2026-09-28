package com.bx.ultimateDonutSmp2.managers;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpawnerBreakPickupTest {

    private static final Path MANAGER = Path.of(
            "src/main/java/com/bx/ultimateDonutSmp2/managers/SpawnerManager.java");
    private static final Path LISTENER = Path.of(
            "src/main/java/com/bx/ultimateDonutSmp2/listeners/SpawnerBlockListener.java");

    @Test
    void aNormalBreakTakesOneAndCrouchTakesOneVanillaStack() {
        assertEquals(1L, SpawnerManager.breakAmountFor(false, 70L));
        assertEquals(1L, SpawnerManager.breakAmountFor(false, 1L));
        assertEquals(64L, SpawnerManager.breakAmountFor(true, 70L));
        assertEquals(12L, SpawnerManager.breakAmountFor(true, 12L));
        assertEquals(0L, SpawnerManager.breakAmountFor(true, 0L));
    }

    @Test
    void breakStoresTheItemBeforeItShrinksThePlacedStack() throws Exception {
        String source = Files.readString(MANAGER);
        int start = source.indexOf("public ActionResult breakSpawner(");
        int end = source.indexOf("public ActionResult collectLootEntry(");
        assertTrue(start >= 0 && end > start, "breakSpawner should still sit above collectLootEntry");

        String method = source.substring(start, end);
        int silk = method.indexOf("hasSilkTouchAccess");
        assertTrue(silk >= 0, "breakSpawner should still check silk touch before paying out");
        String payout = method.substring(silk);
        int give = payout.indexOf("giveSpawnerItem(");
        int shrink = payout.indexOf("setStackAmount(");
        int unregister = payout.indexOf("unregisterSpawner(");
        assertTrue(give >= 0 && shrink >= 0 && unregister >= 0,
                "breakSpawner should give the item, then shrink or unregister the placed stack");
        assertTrue(give < shrink && give < unregister,
                "the placed stack is removed before the item is stored. A failed give still deletes the spawners, "
                        + "and the break then drops whatever is left as one item.");
        assertFalse(payout.contains("addItem("),
                "addItem hands back a spawner it cannot stack, and that leftover is what hits the ground");
    }

    @Test
    void spawnerItemsClearTheBlockEntityTagThatStopsThemStacking() throws Exception {
        String source = Files.readString(MANAGER);
        int start = source.indexOf("private void applySpawnerStack(");
        int end = source.indexOf("private static void clearCopiedSpawnerBlockState(");
        assertTrue(start >= 0 && end > start, "applySpawnerStack should sit above clearCopiedSpawnerBlockState");
        String method = source.substring(start, end);
        assertTrue(method.contains("clearCopiedSpawnerBlockState"),
                "spawner item meta copies the block entity, and that tag is the single NBT spawner players pick up");
        assertTrue(method.contains("setMaxStackSize(SPAWNER_ITEM_STACK_LIMIT)"),
                "a crouch break has to be a normal stack of 64, not one item");
        assertTrue(method.contains("setMaxStackSize(1)"),
                "a count stored on the item must not stack, or the tag is counted once per item in the pile");
        int helper = source.indexOf("getMethod(\"clearBlockState\")");
        assertTrue(helper > end, "clearBlockState has to be invoked on Paper, where BlockStateMeta actually has it");
    }

    @Test
    void theBreakIsCancelledBeforeTheBlockCanDropItsOwnSpawner() throws Exception {
        String source = Files.readString(LISTENER);
        int air = source.indexOf("block.setType(Material.AIR)");
        assertTrue(air >= 0, "a fully mined spawner has to be removed by the plugin");
        int cancel = source.lastIndexOf("event.setCancelled(true)", air);
        assertTrue(cancel >= 0 && cancel < air,
                "the break has to be cancelled before the block is cleared, or the spawner still drops");
        assertTrue(source.contains("event.setDropItems(false)"),
                "drops from the break itself have to stay off");
    }
}
