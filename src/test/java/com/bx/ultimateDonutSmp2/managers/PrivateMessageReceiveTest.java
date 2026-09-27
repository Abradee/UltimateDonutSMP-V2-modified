package com.bx.ultimateDonutSmp2.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PrivateMessageReceiveTest {

    @Test
    void clickFillsMsgWithTheVisibleNameAndATrailingSpace() {
        assertEquals("/msg Steve ", PrivateMessageManager.replySuggestCommand("Steve"));
        assertEquals("/msg NotSteve ", PrivateMessageManager.replySuggestCommand("NotSteve"));
        assertEquals("", PrivateMessageManager.replySuggestCommand(" "));
        assertEquals("", PrivateMessageManager.replySuggestCommand(null));
    }

    @Test
    void receiveSoundShipsAndCanBeSilencedByAnEmptyValue() throws Exception {
        var stream = getClass().getClassLoader().getResourceAsStream("sounds.yml");
        assertNotNull(stream, "sounds.yml must exist in resources");
        YamlConfiguration sounds = YamlConfiguration.loadConfiguration(
                new InputStreamReader(stream, StandardCharsets.UTF_8)
        );

        assertEquals(
                "minecraft:block.note_block.bell|1.0|1.2",
                sounds.getString("PRIVATE_MESSAGE.RECEIVED")
        );

        ConfigManager unloaded = new ConfigManager(null);
        assertEquals(
                "minecraft:block.note_block.bell|1.0|1.2",
                unloaded.getSound("PRIVATE_MESSAGE.RECEIVED")
        );

        sounds.set("PRIVATE_MESSAGE.RECEIVED", "");
        ConfigManager silenced = new ConfigManager(null);
        var field = ConfigManager.class.getDeclaredField("sounds");
        field.setAccessible(true);
        field.set(silenced, sounds);
        assertEquals("", silenced.getSound("PRIVATE_MESSAGE.RECEIVED"));
    }
}
