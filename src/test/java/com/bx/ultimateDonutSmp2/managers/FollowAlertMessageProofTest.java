package com.bx.ultimateDonutSmp2.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FollowAlertMessageProofTest {

    private static final List<String> BUNDLED_LOCALES = List.of(
            "en_US", "es_ES", "id_ID", "pt_BR", "de_DE", "fr_FR", "ru_RU", "zh_CN"
    );

    @Test
    void allBundledLanguagesContainFollowAlertReceivedKey() {
        for (String locale : BUNDLED_LOCALES) {
            YamlConfiguration language = YamlConfiguration.loadConfiguration(
                    new File("src/main/resources/languages/" + locale + ".yml")
            );
            assertTrue(
                    language.contains("MESSAGES.FRIENDS.FOLLOW_ALERT_RECEIVED"),
                    locale + ".yml is missing MESSAGES.FRIENDS.FOLLOW_ALERT_RECEIVED"
            );
            String message = language.getString("MESSAGES.FRIENDS.FOLLOW_ALERT_RECEIVED", "");
            assertTrue(
                    message.contains("{player}"),
                    locale + ".yml FOLLOW_ALERT_RECEIVED should contain {player} placeholder: " + message
            );
        }
    }

    @Test
    void friendsManagerPassesPlayerPlaceholderToMessageLookup() throws Exception {
        String source = Files.readString(
                Path.of("src/main/java/com/bx/ultimateDonutSmp2/managers/FriendsManager.java"),
                StandardCharsets.UTF_8
        );
        assertTrue(
                source.contains("\"FRIENDS.FOLLOW_ALERT_RECEIVED\""),
                "FriendsManager must read FRIENDS.FOLLOW_ALERT_RECEIVED"
        );
        assertTrue(
                source.contains("\"{player}\", senderName"),
                "FriendsManager must pass {player} placeholder to getMessageOrDefault"
        );
        assertFalse(
                source.contains("\"&e&lFollow &7» &b\" + senderName"),
                "FriendsManager should not hardcode senderName concatenation into message fallback"
        );
    }
}
