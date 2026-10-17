package com.bx.ultimateDonutSmp2.managers;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BedrockPricePromptTest {

    @Test
    void cancelAndABlankLineAreNotAPrice() {
        assertEquals(BedrockChatPrompt.Intake.CANCEL, BedrockChatPrompt.intake(" cancel "));
        assertEquals(BedrockChatPrompt.Intake.CANCEL, BedrockChatPrompt.intake("CANCEL"));
        assertEquals(BedrockChatPrompt.Intake.REASK, BedrockChatPrompt.intake("  "));
        assertEquals(BedrockChatPrompt.Intake.REASK, BedrockChatPrompt.intake(null));
        assertEquals(BedrockChatPrompt.Intake.SUBMIT, BedrockChatPrompt.intake("30k"));
        assertEquals("30k", BedrockChatPrompt.submitted(" 30k "));
    }

    @Test
    void auctionSellAsksInChatBeforeOpeningThePriceSign() throws Exception {
        String source = source("src/main/java/com/bx/ultimateDonutSmp2/menus/QuickBuyInsertItemMenu.java");
        int ask = source.indexOf("getBedrockChatPrompt()");
        int sign = source.indexOf("SignInputUtil.open");
        assertTrue(ask >= 0 && sign > ask, "Bedrock chat has to run before the listing price sign");
    }

    @Test
    void newOrderAmountAndPriceAskInChatBeforeTheSign() throws Exception {
        String source = source("src/main/java/com/bx/ultimateDonutSmp2/menus/OrdersNewMenu.java");
        String helper = between(source, "private void askOrSign", "private String resolveFailureMessage");
        assertTrue(helper.indexOf("prompt.ask") < helper.indexOf("SignInputUtil.openFromConfig"));
        assertTrue(source.contains("ORDERS.PROMPT_QUANTITY"));
        assertTrue(source.contains("ORDERS.PROMPT_PRICE"));
        int amount = source.indexOf("AMOUNT_SIGN");
        int price = source.indexOf("PRICE_SIGN");
        assertTrue(amount >= 0 && source.indexOf("askOrSign", amount) > amount);
        assertTrue(price > amount && source.indexOf("askOrSign", price) > price);
    }

    @Test
    void orderEditsSkipTheSignWhenThePlayerIsOnBedrock() throws Exception {
        String source = source("src/main/java/com/bx/ultimateDonutSmp2/managers/OrdersManager.java");
        assertChatBeforeSign(source, "public void promptEditOrderQuantityInput", "public void promptEditOrderPriceInput");
        assertChatBeforeSign(source, "public void promptEditOrderPriceInput", "public synchronized EditOrderResult updateOrderItem");
        assertChatBeforeSign(source, "private void resendEditQuantityPrompt", "private void resendEditPricePrompt");
        assertChatBeforeSign(source, "private void resendEditPricePrompt", "private boolean promptBedrockInChat");
    }

    @Test
    void chatListenerReadsThePricePromptBeforeOrdinaryChat() throws Exception {
        String source = source("src/main/java/com/bx/ultimateDonutSmp2/listeners/ChatListener.java");
        int prompt = source.indexOf("getBedrockChatPrompt()");
        int orders = source.indexOf("hasPendingInput");
        assertTrue(prompt >= 0 && orders > prompt);
    }

    private static void assertChatBeforeSign(String source, String start, String end) {
        String method = between(source, start, end);
        int chat = method.indexOf("promptBedrockInChat");
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
