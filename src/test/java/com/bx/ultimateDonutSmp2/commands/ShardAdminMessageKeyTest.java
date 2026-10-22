package com.bx.ultimateDonutSmp2.commands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShardAdminMessageKeyTest {

    private static final Pattern KEY = Pattern.compile("getMessage\\(\\s*\"(BALANCE\\.ADMIN\\.[A-Z0-9_-]+)\"");
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{[^{}]+}");
    private static final Pattern COLOR_CODE = Pattern.compile("&(?:#[0-9A-Fa-f]{6}|[0-9A-FK-ORa-fk-or])");

    private static final Path COMMAND =
            Path.of("src/main/java/com/bx/ultimateDonutSmp2/commands/ShardAdminCommand.java");

    private static final List<String> LOCALES = List.of(
            "en_US", "de_DE", "es_ES", "fr_FR", "id_ID", "pt_BR", "ru_RU", "zh_CN"
    );

    @Test
    void everyShardAdminMessageKeyReadByCommandExistsInAllLanguages() throws IOException {
        String code = Files.readString(COMMAND);
        Set<String> keys = new TreeSet<>();
        Matcher matcher = KEY.matcher(code);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }

        assertFalse(keys.isEmpty(), "ShardAdminCommand must read language message keys");

        for (String locale : LOCALES) {
            File file = new File("src/main/resources/languages/" + locale + ".yml");
            assertTrue(file.exists(), locale + ".yml must exist");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection messages = config.getConfigurationSection("MESSAGES");
            assertTrue(messages != null, "MESSAGES section must exist in " + locale);

            for (String key : keys) {
                assertTrue(messages.isString(key),
                        locale + ".yml is missing key MESSAGES." + key + " required by ShardAdminCommand");
            }
        }
    }

    @Test
    void shardAdminMessagesHavePlaceholderAndColorParity() {
        List<String> shardKeys = List.of(
                "TARGET-NOT-ENOUGH-SHARDS",
                "ADD-SHARDS-SUCCESS",
                "ADD-SHARDS-RECEIVED",
                "REMOVE-SHARDS-SUCCESS",
                "REMOVE-SHARDS-RECEIVED",
                "SET-SHARDS-SUCCESS",
                "SET-SHARDS-RECEIVED"
        );

        YamlConfiguration englishConfig = YamlConfiguration.loadConfiguration(
                new File("src/main/resources/languages/en_US.yml")
        );
        ConfigurationSection englishAdmin = englishConfig.getConfigurationSection("MESSAGES.BALANCE.ADMIN");
        assertTrue(englishAdmin != null, "en_US must contain MESSAGES.BALANCE.ADMIN");

        for (String locale : LOCALES) {
            if ("en_US".equals(locale)) continue;
            YamlConfiguration translatedConfig = YamlConfiguration.loadConfiguration(
                    new File("src/main/resources/languages/" + locale + ".yml")
            );
            ConfigurationSection translatedAdmin = translatedConfig.getConfigurationSection("MESSAGES.BALANCE.ADMIN");
            assertTrue(translatedAdmin != null, locale + " must contain MESSAGES.BALANCE.ADMIN");

            for (String key : shardKeys) {
                String englishText = englishAdmin.getString(key);
                String translatedText = translatedAdmin.getString(key);

                assertTrue(englishText != null && !englishText.isBlank(), "en_US missing " + key);
                assertTrue(translatedText != null && !translatedText.isBlank(), locale + " missing " + key);

                List<String> englishPlaceholders = extractMatches(englishText, PLACEHOLDER);
                List<String> translatedPlaceholders = extractMatches(translatedText, PLACEHOLDER);
                assertTrue(englishPlaceholders.equals(translatedPlaceholders),
                        locale + " placeholder mismatch for " + key + ": expected " + englishPlaceholders + " but got " + translatedPlaceholders);

                List<String> englishColors = extractMatches(englishText, COLOR_CODE);
                List<String> translatedColors = extractMatches(translatedText, COLOR_CODE);
                assertTrue(englishColors.equals(translatedColors),
                        locale + " color mismatch for " + key + ": expected " + englishColors + " but got " + translatedColors);
            }
        }
    }

    private static List<String> extractMatches(String text, Pattern pattern) {
        List<String> list = new java.util.ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        list.sort(String::compareTo);
        return list;
    }
}
