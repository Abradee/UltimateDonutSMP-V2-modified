package com.bx.ultimateDonutSmp2.managers;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BedrockSearchPromptTest {

    @Test
    void aMissingPlayerIsNotAskedInChat() {
        assertFalse(BedrockChatPrompt.askIfBedrock(null, null, "search", (Consumer<String>) text -> {
        }));
    }

    @Test
    void auctionBrowseSearchAsksInChatBeforeTheDialogAndTheSign() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/AuctionHouseBrowseMenu.java"),
                "slot == controlSlot(\"SEARCH\", 50)",
                "PLAYER_ITEMS"
        );
        assertChatBeforeDialogAndSign(region);
        assertTrue(region.contains("AUCTION_HOUSE.BEDROCK_SEARCH_PROMPT"));
    }

    @Test
    void yourItemsSearchAsksInChatBeforeTheDialogAndTheSign() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/AuctionYourItemsMenu.java"),
                "if (slot == searchSlot)",
                "if (slot == transSlot)"
        );
        assertChatBeforeDialogAndSign(region);
        assertTrue(region.contains("input == null"));
    }

    @Test
    void transactionSearchAsksInChatBeforeTheDialogAndTheSign() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyTransactionsMenu.java"),
                "if (slot == searchSlot)",
                "private void click("
        );
        assertChatBeforeDialogAndSign(region);
        assertTrue(region.contains("input == null"));
    }

    @Test
    void bountySearchAsksInChatBeforeTheDialogAndTheSign() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/BountyMenu.java"),
                "if (slot == searchSlot)",
                "if (slot == nextSlot"
        );
        assertChatBeforeDialogAndSign(region);
        assertTrue(region.contains("BOUNTY.BEDROCK_SEARCH_PROMPT"));
    }

    @Test
    void punishmentSearchAsksInChatBeforeTheSign() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/PunishmentsListMenu.java"),
                "private void handleSearchClick",
                "private void handleRecordClick"
        );
        int chat = region.indexOf("askIfBedrock");
        int sign = region.indexOf("SignInputUtil.open");
        assertTrue(chat >= 0 && sign > chat, "punishment search");
        assertTrue(region.contains("PUNISHMENTS.BEDROCK_SEARCH_PROMPT"));
    }

    @Test
    void orderSearchesAskInChatBeforeTheSign() throws Exception {
        String source = source("src/main/java/com/bx/ultimateDonutSmp2/managers/OrdersManager.java");
        assertChatBeforeSign(source, "public void promptOrderSearchInput", "public void promptOrdersMenuSearch");
        assertChatBeforeSign(source, "public void promptOrdersMenuSearch", "public void promptEditOrderSearchInput");
        assertChatBeforeSign(source, "public void promptEditOrderSearchInput", "public boolean hasPendingInput");
        assertTrue(source.contains("ORDERS.PROMPT_SEARCH"));
    }

    @Test
    void orderMenuSearchSkipsTheJavaDialogForBedrock() throws Exception {
        String region = between(
                source("src/main/java/com/bx/ultimateDonutSmp2/menus/OrdersMenuSupport.java"),
                "static void openSearch(",
                "static void click("
        );
        int bedrock = region.indexOf("BedrockPlayers.isBedrock");
        int dialog = region.indexOf("DialogSupport");
        assertTrue(bedrock >= 0 && dialog > bedrock, "orders menu search");
    }

    private static void assertChatBeforeDialogAndSign(String region) {
        int chat = region.indexOf("askIfBedrock");
        int dialog = region.indexOf("DialogSupport");
        int sign = region.indexOf("SignInputUtil");
        assertTrue(chat >= 0 && dialog > chat && sign > dialog, "chat, then dialog, then sign");
    }

    private static void assertChatBeforeSign(String source, String start, String end) {
        String method = between(source, start, end);
        int chat = method.indexOf("askIfBedrock");
        int sign = method.indexOf("SignInputUtil.openFromConfig");
        assertTrue(chat >= 0 && sign > chat, start);
    }

    private static String between(String source, String start, String end) {
        int from = source.indexOf(start);
        assertTrue(from >= 0, start);
        int to = source.indexOf(end, from + start.length());
        assertTrue(to > from, end);
        return source.substring(from, to);
    }

    private static String source(String path) throws Exception {
        return Files.readString(Path.of(path));
    }
}
