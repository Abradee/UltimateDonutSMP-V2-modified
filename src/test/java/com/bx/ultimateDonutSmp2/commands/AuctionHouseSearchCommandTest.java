package com.bx.ultimateDonutSmp2.commands;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuctionHouseSearchCommandTest {

    @Test
    void joinsEveryWordAndStopsAtTheSignSearchLimit() {
        assertEquals("", AuctionHouseCommand.searchArgument(new String[]{"search"}));
        assertEquals("diamond", AuctionHouseCommand.searchArgument(new String[]{"search", " diamond "}));
        assertEquals("diamond sword", AuctionHouseCommand.searchArgument(new String[]{"search", "diamond", "sword"}));

        String longName = "netherite".repeat(10);
        assertEquals(64, AuctionHouseCommand.searchArgument(new String[]{"search", longName}).length());
        assertEquals(longName.substring(0, 64), AuctionHouseCommand.searchArgument(new String[]{"search", longName}));
    }

    @Test
    void tabCompleteOffersItemMaterialsThatStartWithTheToken() {
        List<String> matches = AuctionHouseCommand.searchTabComplete("diamond_s");
        assertTrue(matches.contains("diamond_sword"));
        assertTrue(matches.contains("diamond_shovel"));
        assertTrue(matches.stream().allMatch(name -> name.startsWith("diamond_s")));
        assertTrue(AuctionHouseCommand.searchTabComplete(" ").isEmpty());
    }
}
