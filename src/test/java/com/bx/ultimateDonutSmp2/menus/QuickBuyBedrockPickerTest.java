package com.bx.ultimateDonutSmp2.menus;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickBuyBedrockPickerTest {

    @Test
    void emptySlotDoesNotOpenTheMaterialDialogForBedrock() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyMenu.java"));
        int start = source.indexOf("private void openItemPicker(");
        int end = source.indexOf("private void click(", start);
        assertTrue(start >= 0 && end > start);
        String method = source.substring(start, end);
        int bedrock = method.indexOf("BedrockPlayers.isBedrock");
        int dialog = method.indexOf("openSelection");
        assertTrue(bedrock >= 0 && dialog > bedrock,
                "Bedrock has to leave this method before the Choose Item dialog is built");
        assertTrue(method.contains("QuickBuyItemSelectMenu"),
                "Without a Floodgate form, Bedrock still needs the paged chest catalogue");
    }

    @Test
    void chestCatalogueDoesNotContinueIntoDialogsForBedrock() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyItemSelectMenu.java"));
        assertTrue(source.contains("!BedrockPlayers.isBedrock(player)"),
                "Search and enchant clicks on the chest catalogue must not open a dialog for Bedrock");
    }

    @Test
    void mainGridSearchDoesNotOpenAJavaDialogForBedrock() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyMenu.java"));
        int search = source.indexOf("if (slot == searchSlot)");
        int dialog = source.indexOf("openAuctionSearch", search);
        int bedrock = source.indexOf("BedrockPlayers.isBedrock", search);
        assertTrue(search >= 0 && bedrock > search && dialog > bedrock,
                "Bedrock has to leave the Quick Buy search button before the Java search dialog");
    }

    @Test
    void chestCatalogueSearchDoesNotOpenASignForBedrock() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyItemSelectMenu.java"));
        int search = source.indexOf("if (slot == SEARCH_SLOT)");
        int selected = source.indexOf("Material selected", search);
        assertTrue(search >= 0 && selected > search);
        String region = source.substring(search, selected);
        int bedrock = region.indexOf("BedrockPlayers.isBedrock");
        int sign = region.indexOf("SignInputUtil.open");
        int leave = region.indexOf("return;", bedrock);
        assertTrue(bedrock >= 0 && leave > bedrock && sign > leave,
                "Bedrock search on the chest catalogue must return before the sign editor");
    }

    @Test
    void catalogueSearchSkipsClosingTheInventoryWhenAFormIsAlreadyOpen() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/bx/ultimateDonutSmp2/managers/QuickBuyBedrockManager.java"));
        int start = source.indexOf("public boolean openCatalogue(");
        int end = source.indexOf("public boolean openItemSearch(", start);
        assertTrue(start >= 0 && end > start);
        String method = source.substring(start, end);
        int formOpen = method.indexOf("formOpen");
        int close = method.indexOf("closeMenu");
        assertTrue(formOpen >= 0 && close > formOpen,
                "Submitting search must replace the Floodgate form without closeInventory");
        assertTrue(source.contains("CHEST_AFTER_FORM_TICKS"),
                "A chest after a Floodgate form has to wait, or Bedrock cannot open the shop again");
    }
}
