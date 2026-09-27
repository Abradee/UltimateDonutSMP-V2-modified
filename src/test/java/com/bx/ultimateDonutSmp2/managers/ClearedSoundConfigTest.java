package com.bx.ultimateDonutSmp2.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClearedSoundConfigTest {

    @Test
    void emptyStringStaysSilentAfterTheFileIsReread() throws Exception {
        YamlConfiguration sounds = bundledSounds();
        String[] cleared = {
                "ORDERS.SUCCESS",
                "ORDERS.OPEN",
                "ORDERS.CLICK",
                "QUICK_BUY.OPEN",
                "QUICK_BUY.CLICK",
                "AUCTION_HOUSE.OPEN",
                "AUCTION_HOUSE.SUCCESS",
                "PAY.SUCCESS",
                "SELL.SUCCESS",
                "SELL.LEVEL-UP",
                "BUY.SUCCESS"
        };
        for (String path : cleared) {
            sounds.set(path, "");
        }
        sounds.set("ORDERS.FAIL", " ");

        YamlConfiguration reread = YamlConfiguration.loadConfiguration(new StringReader(sounds.saveToString()));
        ConfigManager manager = managerWith(reread);
        for (String path : cleared) {
            assertEquals("", manager.getSound(path), path);
        }
        assertEquals("", manager.getSound("ORDERS.FAIL"));
        assertEquals(
                "minecraft:entity.player.levelup|0.85|1.35",
                manager.getSound("ORDERS.CREATE")
        );
    }

    @Test
    void missingKeyStillUsesTheBuiltInCue() throws Exception {
        YamlConfiguration sounds = bundledSounds();
        sounds.set("ORDERS.SUCCESS", null);
        sounds.set("QUICK_BUY.OPEN", null);
        sounds.set("AUCTION_HOUSE.OPEN", null);
        sounds.set("PAY.SUCCESS", null);
        sounds.set("SELL.SUCCESS", null);
        sounds.set("SELL.LEVEL-UP", null);

        ConfigManager manager = managerWith(sounds);
        assertEquals("minecraft:entity.experience_orb.pickup|1.0|1.2", manager.getSound("ORDERS.SUCCESS"));
        assertEquals("minecraft:block.chest.open|0.8|1.15", manager.getSound("QUICK_BUY.OPEN"));
        assertEquals("minecraft:block.chest.open|0.8|1.15", manager.getSound("AUCTION_HOUSE.OPEN"));
        assertEquals("minecraft:entity.player.levelup|1.0|1.2", manager.getSound("PAY.SUCCESS"));
        assertEquals("minecraft:entity.experience_orb.pickup|1.0|1.0", manager.getSound("SELL.SUCCESS"));
        assertEquals("minecraft:entity.player.levelup|1.0|2.0", manager.getSound("SELL.LEVEL-UP"));
    }

    private static YamlConfiguration bundledSounds() {
        var stream = ClearedSoundConfigTest.class.getClassLoader().getResourceAsStream("sounds.yml");
        assertNotNull(stream, "sounds.yml must exist in resources");
        return YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private static ConfigManager managerWith(YamlConfiguration sounds) throws Exception {
        ConfigManager manager = new ConfigManager(null);
        var field = ConfigManager.class.getDeclaredField("sounds");
        field.setAccessible(true);
        field.set(manager, sounds);
        return manager;
    }
}
