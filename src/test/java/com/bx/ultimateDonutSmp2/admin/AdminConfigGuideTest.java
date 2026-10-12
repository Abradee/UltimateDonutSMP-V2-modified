package com.bx.ultimateDonutSmp2.admin;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminConfigGuideTest {

    @Test
    void labelIncludesWhatTheKeyDoesAndAnExample() {
        String label = AdminConfigGuide.inputLabel(
                "HOME-DEFAULT",
                List.of("Homes every player gets when no HOME-PERMISSIONS entry applies to them",
                        "Available options: Any valid integer"),
                AdminConfigValues.Kind.INTEGER,
                "3"
        );
        assertTrue(label.contains("Home Default"));
        assertTrue(label.contains("3"));
        assertFalse(label.contains("\n"));
        assertFalse(label.toLowerCase().contains("homes every player"));
        assertFalse(label.toLowerCase().contains("available options"));
    }

    @Test
    void longExplanationStaysOffTheInputLabel() {
        String label = AdminConfigGuide.inputLabel(
                "AFTER-ACTION",
                List.of("Keep this on NONE. With CLOSE the client drops back to the world and recenters the mouse."),
                AdminConfigValues.Kind.STRING,
                "NONE"
        );
        assertEquals("&fAfter Action &8— &fNONE", label);
        assertFalse(label.contains("Keep this on"));
    }

    @Test
    void booleanLabelStaysShortEnoughForTheCheckbox() {
        String label = AdminConfigGuide.inputLabel(
                "ENABLED",
                List.of("Determines whether Enabled is enabled or disabled. Available options: true, false"),
                AdminConfigValues.Kind.BOOLEAN,
                "true"
        );
        assertTrue(label.contains("Enabled"));
        assertFalse(label.contains("on or off"));
        assertFalse(label.contains("\n"));
        assertFalse(label.toLowerCase().contains("determines whether"));
    }

    @Test
    void teleportGraceDoesNotUseAPortExample() {
        String example = AdminConfigGuide.example(
                "POST-TELEPORT-GRACE-MS",
                List.of("The numerical value for Post Teleport Grace Ms. Available options: Any valid integer"),
                AdminConfigValues.Kind.INTEGER,
                "2000"
        );
        assertEquals("2000", example);
        String empty = AdminConfigGuide.example(
                "POST-TELEPORT-GRACE-MS",
                List.of(),
                AdminConfigValues.Kind.INTEGER,
                ""
        );
        assertFalse(empty.contains("3306"));
    }

    @Test
    void fileButtonNameDropsTheYmlSuffix() {
        assertEquals("Shop", AdminConfigCatalog.displayName("shop.yml"));
        assertEquals("Death Messages", AdminConfigCatalog.displayName("death-messages.yml"));
        assertFalse(AdminConfigCatalog.displayName("config.yml").contains(".yml"));
        assertFalse(AdminConfigCatalog.tooltip(AdminConfigCatalog.describe("shop.yml")).startsWith("&fshop.yml"));
    }

    @Test
    void colorCodesBecomeAPreviewOnTheLabel() {
        String label = AdminConfigGuide.inputLabel(
                "SHARDS-KILL-MESSAGE",
                List.of(),
                AdminConfigValues.Kind.STRING,
                "&#A303F9+(shards) Shard"
        );
        assertEquals("&fShards Kill Message", label);
        assertEquals("&7Shards Kill Message&8: &r&#A303F9+(shards) Shard",
                AdminConfigGuide.colorPreview("Shards Kill Message", "&#A303F9+(shards) Shard"));
        assertFalse(label.contains("\n"));
        assertFalse(AdminConfigGuide.hasColorCodes("false&allowPublicKeyRetrieval"));
        assertTrue(AdminConfigGuide.hasColorCodes("&aHello"));
        assertTrue(AdminConfigGuide.hasColorCodes("&#00FC00$"));
    }

    @Test
    void secretExampleDoesNotRepeatTheLivePassword() {
        String example = AdminConfigGuide.example(
                "PASSWORD",
                List.of("The text or value for Password. Available options: Any valid string text"),
                AdminConfigValues.Kind.STRING,
                "super-secret"
        );
        assertEquals("change-me", example);
        assertFalse(example.contains("super-secret"));
    }

    @Test
    void catalogListsShopAndLeavesLanguagesOut() {
        assertTrue(AdminConfigCatalog.editable("shop.yml"));
        assertTrue(AdminConfigCatalog.editable("offenses.yml"));
        assertFalse(AdminConfigCatalog.editable("languages/en_US.yml"));
        assertFalse(AdminConfigCatalog.editable("../server.properties"));
        assertTrue(AdminConfigCatalog.files().stream().anyMatch(file -> file.name().equals("shop.yml")));
        assertTrue(AdminConfigCatalog.files().stream().noneMatch(file -> file.name().contains("languages")));
        assertTrue(AdminConfigCatalog.tooltip(AdminConfigCatalog.describe("shop.yml")).contains("Example:"));
    }
}
