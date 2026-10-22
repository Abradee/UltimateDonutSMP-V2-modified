package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import sun.reflect.ReflectionFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RTPCommandTest {

    private UltimateDonutSmp2 createMockPlugin(YamlConfiguration rtpConfig) throws Exception {
        Constructor<Object> objectConstructor = Object.class.getConstructor();
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor<?> newConstructor = reflectionFactory.newConstructorForSerialization(UltimateDonutSmp2.class, objectConstructor);
        UltimateDonutSmp2 plugin = (UltimateDonutSmp2) newConstructor.newInstance();

        ConfigManager configManager = new ConfigManager(plugin);
        Field rtpField = ConfigManager.class.getDeclaredField("rtp");
        rtpField.setAccessible(true);
        rtpField.set(configManager, rtpConfig);

        Field configField = ConfigManager.class.getDeclaredField("config");
        configField.setAccessible(true);
        configField.set(configManager, new YamlConfiguration());

        Field cmField = UltimateDonutSmp2.class.getDeclaredField("configManager");
        cmField.setAccessible(true);
        cmField.set(plugin, configManager);

        return plugin;
    }

    @Test
    void menuEnabledDefaultsToTrueWhenUnset() throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        UltimateDonutSmp2 plugin = createMockPlugin(config);
        RTPCommand command = new RTPCommand(plugin);

        assertTrue(command.isMenuEnabled(), "RTP menu should be enabled by default when unconfigured");
    }

    @Test
    void menuEnabledReflectsConfigSetting() throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        config.set("RTP-MENU.ENABLED", false);
        UltimateDonutSmp2 plugin = createMockPlugin(config);
        RTPCommand command = new RTPCommand(plugin);

        assertFalse(command.isMenuEnabled(), "RTP menu should be disabled when configured as false");

        config.set("RTP-MENU.ENABLED", true);
        assertTrue(command.isMenuEnabled(), "RTP menu should be enabled when configured as true");
    }

    @Test
    void shippedRtpConfigEnablesMenuAndDocumentsIt() throws Exception {
        YamlConfiguration shipped = YamlConfiguration.loadConfiguration(new File("src/main/resources/rtp.yml"));
        assertTrue(shipped.getBoolean("RTP-MENU.ENABLED"), "bundled rtp.yml should enable RTP-MENU.ENABLED");

        String wiki = Files.readString(Path.of("docs/wiki/Config-rtp.yml.md"));
        assertTrue(wiki.contains("RTP-MENU.ENABLED"), "docs/wiki/Config-rtp.yml.md must document RTP-MENU.ENABLED");
    }
}
