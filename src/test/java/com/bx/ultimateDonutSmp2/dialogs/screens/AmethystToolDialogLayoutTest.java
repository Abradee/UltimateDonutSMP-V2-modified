package com.bx.ultimateDonutSmp2.dialogs.screens;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmethystToolDialogLayoutTest {

    private static final Path SOURCE = Path.of(
            "src/main/java/com/bx/ultimateDonutSmp2/dialogs/screens/AmethystToolDialog.java");
    private static final Path DIALOG = Path.of("src/main/resources/dialog.yml");

    @Test
    void typePickerUsesItemIconAndLore() throws Exception {
        String source = Files.readString(SOURCE);
        int start = source.indexOf("private boolean openTypes");
        int end = source.indexOf("private boolean openPlayers");
        assertTrue(start >= 0 && end > start, "openTypes must exist");
        String method = source.substring(start, end);
        assertTrue(method.contains("QuickBuyItemDialog.getSpriteTag"), "type buttons need an item icon");
        assertTrue(method.contains("getDialogLore"), "type buttons need the amethyst lore");
        assertTrue(method.contains("DialogText.tooltip"), "lore must be the button tooltip");
    }

    @Test
    void playerPickerUsesHeadNickAndOnlineOfflineLore() throws Exception {
        String source = Files.readString(SOURCE);
        int start = source.indexOf("private boolean openPlayers");
        int end = source.indexOf("private boolean openSearch");
        assertTrue(start >= 0 && end > start, "openPlayers must exist");
        String method = source.substring(start, end);
        assertTrue(method.contains("DialogPlayerHeads.component"), "player buttons need a 2D skin head");
        assertTrue(method.contains("statusLore"), "player buttons need online/offline lore");
        assertFalse(method.contains(".item("), "player picker must not use a floating 3D item");
        String collect = source.substring(
                source.indexOf("private List<PlayerRow> collectPlayers"),
                source.indexOf("private List<PlayerRow> filterPlayers"));
        assertTrue(collect.contains("getOnlinePlayers"), "online nicknames must appear");
        assertTrue(collect.contains("getOfflinePlayers"), "last-online nicknames must appear");
        assertTrue(collect.contains("loadKnownPlayerIdentities"), "known accounts must appear even if Bukkit has no name");
    }

    @Test
    void durationButtonsAreHumanLabelsNotRawSeconds() throws Exception {
        YamlConfiguration dialog = new YamlConfiguration();
        dialog.load(DIALOG.toFile());
        for (java.util.Map<?, ?> entry : dialog.getMapList("AMETHYST_DURATION_DIALOG.DURATIONS")) {
            Object label = entry.get("LABEL");
            if (label == null || "custom".equalsIgnoreCase(String.valueOf(entry.get("ACTION")))) {
                continue;
            }
            String shown = String.valueOf(label).trim();
            assertFalse(shown.matches("\\d+"), "duration button '" + shown + "' must not be raw seconds");
            assertTrue(shown.matches("(?i)\\d+[dhm].*") || shown.equalsIgnoreCase("Custom"),
                    "duration button '" + shown + "' should use d/h/m");
        }
    }

    @Test
    void shippedDurationPresetsCoverTheRequestedHoursAndDays() throws Exception {
        YamlConfiguration dialog = new YamlConfiguration();
        dialog.load(DIALOG.toFile());
        java.util.Set<String> labels = new java.util.LinkedHashSet<>();
        for (java.util.Map<?, ?> entry : dialog.getMapList("AMETHYST_DURATION_DIALOG.DURATIONS")) {
            labels.add(String.valueOf(entry.get("LABEL")));
        }
        assertTrue(labels.contains("1h"));
        assertTrue(labels.contains("5h"));
        assertTrue(labels.contains("10h"));
        assertTrue(labels.contains("1d"));
        assertTrue(labels.contains("5d"));
        assertTrue(labels.contains("Custom"));
    }
}
