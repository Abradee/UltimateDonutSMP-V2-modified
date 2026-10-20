package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.models.SellCategory;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuickBuySellArbitrageFloorTest {

    private ShopManager shopManager;
    private WorthManager worthManager;
    private Object previousServer;

    @BeforeEach
    void setup() throws Exception {
        Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        previousServer = serverField.get(null);

        Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new HashMap<>();
        org.bukkit.inventory.meta.ItemMeta mockItemMeta = (org.bukkit.inventory.meta.ItemMeta) Proxy.newProxyInstance(
                org.bukkit.inventory.meta.ItemMeta.class.getClassLoader(),
                new Class<?>[]{org.bukkit.inventory.meta.ItemMeta.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("addEnchant")) {
                        enchants.put((org.bukkit.enchantments.Enchantment) args[0], (Integer) args[1]);
                        return true;
                    }
                    if (name.equals("getEnchants")) {
                        return enchants;
                    }
                    if (name.equals("getEnchantLevel")) {
                        return enchants.getOrDefault(args[0], 0);
                    }
                    if (name.equals("hasEnchant")) {
                        return enchants.containsKey(args[0]);
                    }
                    if (name.equals("clone")) {
                        return proxy;
                    }
                    if (name.equals("equals")) {
                        return proxy == args[0];
                    }
                    if (name.equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    }
                    if (method.getReturnType() == boolean.class) {
                        return false;
                    }
                    if (method.getReturnType() == int.class) {
                        return 0;
                    }
                    return null;
                }
        );

        Object itemFactory = Proxy.newProxyInstance(
                org.bukkit.inventory.ItemFactory.class.getClassLoader(),
                new Class<?>[]{org.bukkit.inventory.ItemFactory.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getItemMeta")) {
                        return mockItemMeta;
                    }
                    if (method.getName().equals("hasItemMeta") || method.getName().equals("isApplicable")) {
                        return true;
                    }
                    if (method.getName().equals("asMetaFor")) {
                        return args[0];
                    }
                    if (method.getName().equals("equals") && args.length == 2) {
                        return Objects.equals(args[0], args[1]);
                    }
                    return null;
                }
        );

        Object[] registry = new Object[1];
        org.bukkit.Server server = (org.bukkit.Server) Proxy.newProxyInstance(
                org.bukkit.Server.class.getClassLoader(),
                new Class<?>[]{org.bukkit.Server.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getItemFactory")) {
                        return itemFactory;
                    }
                    if (method.getName().equals("getRegistry")) {
                        if (registry[0] == null) {
                            Class<?> registryClass = Class.forName("org.bukkit.Registry");
                            registry[0] = Proxy.newProxyInstance(
                                    registryClass.getClassLoader(),
                                    new Class<?>[]{registryClass},
                                    (rProxy, rMethod, rArgs) -> null
                            );
                        }
                        return registry[0];
                    }
                    return null;
                }
        );
        serverField.set(null, server);

        YamlConfiguration worthConfig = new YamlConfiguration();
        worthConfig.set("TYPE.ORES.NETHERITE_BLOCK", 225_000.0);

        YamlConfiguration menus = new YamlConfiguration();
        menus.set("PROGRESS-MENU.ENABLED", true);
        menus.set("PROGRESS-MENU.LEVEL", List.of(
                25_000L, 150_000L, 500_000L, 1_000_000L, 5_000_000L, 25_000_000L
        ));

        Constructor<Object> objectConstructor = Object.class.getConstructor();
        sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
        Constructor<?> pluginConstructor = reflectionFactory.newConstructorForSerialization(UltimateDonutSmp2.class, objectConstructor);
        UltimateDonutSmp2 plugin = (UltimateDonutSmp2) pluginConstructor.newInstance();

        Field descField = JavaPlugin.class.getDeclaredField("description");
        descField.setAccessible(true);
        descField.set(plugin, new PluginDescriptionFile("UltimateDonutSmp2", "1.0", "com.bx.ultimateDonutSmp2.UltimateDonutSmp2"));

        ConfigManager configManager = new ConfigManager(plugin);
        setField(configManager, "worth", worthConfig);
        setField(configManager, "menus", menus);
        setField(plugin, "configManager", configManager);

        worthManager = new WorthManager(plugin);
        setField(plugin, "worthManager", worthManager);

        Constructor<?> shopManagerConstructor = reflectionFactory.newConstructorForSerialization(ShopManager.class, objectConstructor);
        shopManager = (ShopManager) shopManagerConstructor.newInstance();
        setField(shopManager, "plugin", plugin);
    }

    @AfterEach
    void tearDown() throws Exception {
        Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        serverField.set(null, previousServer);
    }

    @Test
    void minimumQuickBuyUnitPriceUsesBaseWorthWithoutBuyer() {
        double floor = shopManager.minimumQuickBuyUnitPrice(null, new ItemStack(Material.NETHERITE_BLOCK));
        assertEquals(225_000.0, floor, 0.001);
    }

    @Test
    void oreSellMultiplierAtIssueReportLevelRaisesBuyFloorToSellPayout() {
        Map<SellCategory, Double> progress = Map.of(SellCategory.ORES, 30_000_000.0);
        double multiplier = shopManager.getCurrentSellMultiplier(progress, SellCategory.ORES);
        assertEquals(1.6, multiplier, 0.001);

        double worth = worthManager.resolveWorth(new ItemStack(Material.NETHERITE_BLOCK)).unitWorth();
        assertEquals(360_000.0, worth * multiplier, 0.001);
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
