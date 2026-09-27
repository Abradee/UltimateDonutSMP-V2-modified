package com.bx.ultimateDonutSmp2.models;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuctionIntelligentSearchTest {

    @Test
    void nethBootProt4MatchesNetheriteBootsWithProtectionFourOrHigher() {
        Map<String, Integer> protectionFour = Map.of("protection", 4);
        assertTrue(AuctionIntelligentSearch.matches(
                "neth boot prot 4",
                "netherite boots",
                "Netherite Boots",
                protectionFour
        ));
        assertTrue(AuctionIntelligentSearch.matches(
                "prot4",
                "netherite boots",
                "Netherite Boots",
                protectionFour
        ));
        assertTrue(AuctionIntelligentSearch.matches(
                "prot iv neth boot",
                "netherite boots",
                "Netherite Boots",
                Map.of("protection", 5)
        ));
        assertFalse(AuctionIntelligentSearch.matches(
                "neth boot prot 4",
                "netherite boots",
                "Netherite Boots",
                Map.of("protection", 3)
        ));
        assertFalse(AuctionIntelligentSearch.matches(
                "neth boot prot 4",
                "diamond boots",
                "Diamond Boots",
                protectionFour
        ));
    }

    @Test
    void aWordWithoutALevelStaysPartOfTheItemName() {
        assertTrue(AuctionIntelligentSearch.matches(
                "blast furnace",
                "blast furnace",
                "Blast Furnace",
                Map.of()
        ));
        assertFalse(AuctionIntelligentSearch.needsEnchantLevels("blast furnace"));
        assertTrue(AuctionIntelligentSearch.needsEnchantLevels("blast 4"));
    }

    @Test
    void diamondSwordStillMatchesEitherWordOrder() {
        assertTrue(AuctionIntelligentSearch.matches(
                "diamond sword",
                "diamond sword",
                "diamond sword",
                Map.of()
        ));
        assertTrue(AuctionIntelligentSearch.matches(
                "sword diamond",
                "diamond sword",
                "diamond sword",
                Map.of()
        ));
    }
}
