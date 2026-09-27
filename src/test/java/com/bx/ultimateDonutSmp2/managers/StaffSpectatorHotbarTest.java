package com.bx.ultimateDonutSmp2.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaffSpectatorHotbarTest {

    @Test
    void staffModeShipsASpectatorHotbarTool() {
        YamlConfiguration staff = YamlConfiguration.loadConfiguration(new File("src/main/resources/staff-mode.yml"));
        YamlConfiguration plugin = YamlConfiguration.loadConfiguration(new File("src/main/resources/plugin.yml"));

        assertEquals(6, staff.getInt("STAFF-MODE.HOTBAR-SLOTS.SPECTATOR"));
        assertEquals("ultimatedonutsmp2.staff.mode.spectator", staff.getString("STAFF-MODE.SPECTATOR-PERMISSION"));
        assertEquals("ENDER_EYE", staff.getString("ITEMS.SPECTATOR.DISABLED.MATERIAL"));
        assertEquals("&7Click to spectate", staff.getStringList("ITEMS.SPECTATOR.DISABLED.LORE").get(0));
        assertEquals("&7Sneak to go back to creative", staff.getStringList("ITEMS.SPECTATOR.ENABLED.LORE").get(0));
        assertTrue(plugin.getBoolean("permissions.ultimatedonutsmp2.staff.mode.children.ultimatedonutsmp2.staff.mode.spectator"));
    }
}
