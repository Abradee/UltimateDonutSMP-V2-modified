package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.models.AuctionListing;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuickBuyPricingWorthFloorTest {

    private UltimateDonutSmp2 plugin;
    private WorthManager worthManager;
    private ShopManager shopManager;
    private Enchantment sharpness;

    @BeforeEach
    void setup() throws Exception {
        setupMockServer();

        YamlConfiguration worthConfig = new YamlConfiguration();
        worthConfig.set("TYPE.ARMOR_AND_TOOLS.DIAMOND_SWORD", 50.0);
        worthConfig.set("TYPE.MINERALS.DIAMOND", 100.0);
        worthConfig.set("TYPE.BOOK.ENCHANTED_BOOK:SHARPNESS:5", 1500.0);

        YamlConfiguration shopConfig = new YamlConfiguration();
        shopConfig.set("QUICK-BUY.PRICING.USE-AUCTION-HOUSE", true);
        shopConfig.set("QUICK-BUY.PRICING.WORTH-MULTIPLIER", 0.5); // intentionally below 1.0
        shopConfig.set("QUICK-BUY.PRICING.AUTO-BALANCE-MISSING", false);

        Constructor<Object> objectConstructor = Object.class.getConstructor();
        sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
        Constructor<?> pluginConstructor = reflectionFactory.newConstructorForSerialization(UltimateDonutSmp2.class, objectConstructor);
        plugin = (UltimateDonutSmp2) pluginConstructor.newInstance();

        ConfigManager configManager = new ConfigManager(plugin);
        Field worthField = ConfigManager.class.getDeclaredField("worth");
        worthField.setAccessible(true);
        worthField.set(configManager, worthConfig);

        Field shopField = ConfigManager.class.getDeclaredField("shop");
        shopField.setAccessible(true);
        shopField.set(configManager, shopConfig);

        YamlConfiguration mainConfig = new YamlConfiguration();
        mainConfig.set("FEATURES.AUCTION_HOUSE.ENABLED", true);
        Field cfgField = ConfigManager.class.getDeclaredField("config");
        cfgField.setAccessible(true);
        cfgField.set(configManager, mainConfig);

        YamlConfiguration ahConfig = new YamlConfiguration();
        ahConfig.set("SETTINGS.ENABLED", true);
        Field ahField = ConfigManager.class.getDeclaredField("auctionHouse");
        ahField.setAccessible(true);
        ahField.set(configManager, ahConfig);

        Field cmField = UltimateDonutSmp2.class.getDeclaredField("configManager");
        cmField.setAccessible(true);
        cmField.set(plugin, configManager);

        FeatureManager featureManager = new FeatureManager(plugin);
        Field fmField = UltimateDonutSmp2.class.getDeclaredField("featureManager");
        fmField.setAccessible(true);
        fmField.set(plugin, featureManager);

        Field descField = org.bukkit.plugin.java.JavaPlugin.class.getDeclaredField("description");
        descField.setAccessible(true);
        org.bukkit.plugin.PluginDescriptionFile pdf = new org.bukkit.plugin.PluginDescriptionFile("UltimateDonutSmp2", "1.0", "com.bx.ultimateDonutSmp2.UltimateDonutSmp2");
        descField.set(plugin, pdf);

        worthManager = new WorthManager(plugin);
        Field wmField = UltimateDonutSmp2.class.getDeclaredField("worthManager");
        wmField.setAccessible(true);
        wmField.set(plugin, worthManager);

        Constructor<?> shopManagerConstructor = reflectionFactory.newConstructorForSerialization(ShopManager.class, objectConstructor);
        shopManager = (ShopManager) shopManagerConstructor.newInstance();
        Field smPluginField = ShopManager.class.getDeclaredField("plugin");
        smPluginField.setAccessible(true);
        smPluginField.set(shopManager, plugin);

        sharpness = new TestEnchantment(NamespacedKey.minecraft("sharpness"));
    }

    @Test
    void quickBuyServerPriceFlooredAtWorthEvenWithSubOneMultiplier() {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        double price = shopManager.findServerShopPrice(diamond);

        // Worth is 100.0, multiplier is 0.5. Floor guarantees price is at least worth (100.0)
        assertEquals(100.0, price, 0.001);
    }

    @Test
    void quickBuyServerPriceEnchantedItemIncludesEnchantmentWorthFloor() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(sharpness, 5);

        double worth = worthManager.getWorth(sword);
        assertEquals(1550.0, worth, 0.001);

        double price = shopManager.findServerShopPrice(sword);
        assertTrue(price >= worth, "Server shop price (" + price + ") must be at least worth (" + worth + ")");
    }

    @Test
    void resolveQuickBuyQuoteFloorsAtServerWorth() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(sharpness, 5);

        ShopManager.QuickBuyQuote quote = shopManager.resolveQuickBuyQuote(null, sword, 1);
        assertTrue(quote.available(), "Quote should be available");
        assertTrue(quote.unitPrice() >= 1550.0, "Unit price must be at least server worth of 1550.0, but was " + quote.unitPrice());
    }

    @Test
    void resolveQuickBuyQuoteFiltersOutSubWorthAuctionListings() throws Exception {
        ItemStack cheapSword = new ItemStack(Material.DIAMOND_SWORD, 1);
        cheapSword.addUnsafeEnchantment(sharpness, 5);

        // Create an undercut AH listing below server worth ($200 vs $1550 worth)
        UUID seller = UUID.randomUUID();
        AuctionListing undercutListing = new AuctionListing(
                100,
                seller,
                "Seller",
                null,
                AuctionListing.Status.ACTIVE,
                200.0,
                0.0,
                cheapSword,
                System.currentTimeMillis(),
                System.currentTimeMillis() + 100_000L,
                0L,
                0L,
                0L,
                "ALL"
        );

        // Setup AuctionHouseManager to return this undercut listing
        Constructor<Object> objectConstructor = Object.class.getConstructor();
        sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
        Constructor<?> ahmConstructor = reflectionFactory.newConstructorForSerialization(AuctionHouseManager.class, objectConstructor);
        AuctionHouseManager ahm = (AuctionHouseManager) ahmConstructor.newInstance();

        Field ahmPluginField = AuctionHouseManager.class.getDeclaredField("plugin");
        ahmPluginField.setAccessible(true);
        ahmPluginField.set(ahm, plugin);

        Field listingCacheField = AuctionHouseManager.class.getDeclaredField("listingCache");
        listingCacheField.setAccessible(true);
        listingCacheField.set(ahm, new java.util.concurrent.atomic.AtomicReference<>(List.of(undercutListing)));

        Field ahmField = UltimateDonutSmp2.class.getDeclaredField("auctionHouseManager");
        ahmField.setAccessible(true);
        ahmField.set(plugin, ahm);

        // When resolving quick buy quote, the undercut AH listing must be ignored because unitPrice < serverWorth
        ShopManager.QuickBuyQuote quote = shopManager.resolveQuickBuyQuote(null, cheapSword, 1);
        assertTrue(quote.available(), "Quote should still find server fallback");
        assertFalse(quote.fromAuction(), "Quote must not use AH listing priced below server worth");
        assertTrue(quote.unitPrice() >= 1550.0, "Unit price must be floored at server worth");
    }

    @Test
    void worthPriceScalesWithPinnedAmount() {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ShopManager.QuickBuyQuote one = shopManager.resolveQuickBuyQuote(null, diamond, 1);
        ShopManager.QuickBuyQuote stack = shopManager.resolveQuickBuyQuote(null, diamond, 64);

        assertEquals(100.0, ShopManager.pricePaidFor(one), 0.001);
        assertEquals(6400.0, ShopManager.pricePaidFor(stack), 0.001);
    }

    @Test
    void auctionUnitPriceStillScalesWithPinnedAmount() throws Exception {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        long now = System.currentTimeMillis();
        AuctionListing listing = new AuctionListing(
                7,
                UUID.randomUUID(),
                "Seller",
                null,
                AuctionListing.Status.ACTIVE,
                200.0,
                0.0,
                diamond,
                now,
                now + 100_000L,
                0L,
                0L,
                0L,
                "ALL"
        );
        installListings(List.of(listing));

        ShopManager.QuickBuyQuote one = shopManager.resolveQuickBuyQuote(null, diamond, 1);
        ShopManager.QuickBuyQuote stack = shopManager.resolveQuickBuyQuote(null, diamond, 64);

        assertTrue(one.fromAuction());
        assertTrue(stack.fromAuction());
        assertEquals(200.0, one.listing().price(), 0.001);
        assertEquals(200.0, ShopManager.pricePaidFor(one), 0.001);
        assertEquals(12_800.0, ShopManager.pricePaidFor(stack), 0.001);
    }

    @Test
    void pricePaidForUsesStackTotalEvenWhenTheListingIsOneItem() {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        AuctionListing listing = new AuctionListing(
                8,
                UUID.randomUUID(),
                "Seller",
                null,
                AuctionListing.Status.ACTIVE,
                200.0,
                0.0,
                diamond,
                1L,
                2L,
                0L,
                0L,
                0L,
                "ALL"
        );
        ShopManager.QuickBuyQuote stack = new ShopManager.QuickBuyQuote(200.0, 12_800.0, listing, true, false);
        ShopManager.QuickBuyQuote missing = new ShopManager.QuickBuyQuote(200.0, 12_800.0, listing, true, true);

        assertEquals(12_800.0, ShopManager.pricePaidFor(stack), 0.001);
        assertEquals(0.0, ShopManager.pricePaidFor(missing), 0.001);
        assertEquals(0.0, ShopManager.pricePaidFor(null), 0.001);
    }

    private void installListings(List<AuctionListing> listings) throws Exception {
        Constructor<Object> objectConstructor = Object.class.getConstructor();
        sun.reflect.ReflectionFactory reflectionFactory = sun.reflect.ReflectionFactory.getReflectionFactory();
        Constructor<?> ahmConstructor = reflectionFactory.newConstructorForSerialization(AuctionHouseManager.class, objectConstructor);
        AuctionHouseManager ahm = (AuctionHouseManager) ahmConstructor.newInstance();

        Field ahmPluginField = AuctionHouseManager.class.getDeclaredField("plugin");
        ahmPluginField.setAccessible(true);
        ahmPluginField.set(ahm, plugin);

        Field listingCacheField = AuctionHouseManager.class.getDeclaredField("listingCache");
        listingCacheField.setAccessible(true);
        listingCacheField.set(ahm, new java.util.concurrent.atomic.AtomicReference<>(listings));

        Field ahmField = UltimateDonutSmp2.class.getDeclaredField("auctionHouseManager");
        ahmField.setAccessible(true);
        ahmField.set(plugin, ahm);
    }

    @Test
    void matchesRequiredEnchantsProperlyChecksStoredEnchants() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(sharpness, 5);

        assertTrue(ShopManager.matchesRequiredEnchants(sword, Map.of(sharpness, 5)));
        assertTrue(ShopManager.matchesRequiredEnchants(sword, Map.of(sharpness, 4)));
        assertFalse(ShopManager.matchesRequiredEnchants(sword, Map.of(sharpness, 6)));

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
        esm.addStoredEnchant(sharpness, 5, true);
        book.setItemMeta(esm);

        assertTrue(ShopManager.matchesRequiredEnchants(book, Map.of(sharpness, 5)));
        assertTrue(ShopManager.matchesRequiredEnchants(book, Map.of(sharpness, 3)));
        assertFalse(ShopManager.matchesRequiredEnchants(book, Map.of(sharpness, 6)));
    }

    private void setupMockServer() throws Exception {
        Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);

        Map<Enchantment, Integer> enchants = new HashMap<>();
        Map<Enchantment, Integer> storedEnchants = new HashMap<>();

        EnchantmentStorageMeta mockStorageMeta = (EnchantmentStorageMeta) Proxy.newProxyInstance(
                EnchantmentStorageMeta.class.getClassLoader(),
                new Class<?>[]{EnchantmentStorageMeta.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("addStoredEnchant")) {
                        storedEnchants.put((Enchantment) args[0], (Integer) args[1]);
                        return true;
                    }
                    if (name.equals("getStoredEnchants")) {
                        return storedEnchants;
                    }
                    if (name.equals("getStoredEnchantLevel")) {
                        return storedEnchants.getOrDefault(args[0], 0);
                    }
                    if (name.equals("hasStoredEnchant")) {
                        return storedEnchants.containsKey(args[0]);
                    }
                    if (name.equals("addEnchant")) {
                        enchants.put((Enchantment) args[0], (Integer) args[1]);
                        return true;
                    }
                    if (name.equals("getEnchants")) {
                        return enchants;
                    }
                    if (name.equals("getEnchantLevel")) {
                        return enchants.getOrDefault(args[0], 0);
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

        ItemMeta mockItemMeta = (ItemMeta) Proxy.newProxyInstance(
                ItemMeta.class.getClassLoader(),
                new Class<?>[]{ItemMeta.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("addEnchant")) {
                        enchants.put((Enchantment) args[0], (Integer) args[1]);
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
                        Material mat = (Material) args[0];
                        if (mat == Material.ENCHANTED_BOOK) {
                            return mockStorageMeta;
                        }
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
    }

    private static class TestEnchantment extends Enchantment {
        private final NamespacedKey key;

        public TestEnchantment(NamespacedKey key) {
            this.key = key;
        }

        @Override
        public NamespacedKey getKey() {
            return key;
        }

        @Override
        public String getName() {
            return key.getKey().toUpperCase(Locale.US);
        }

        @Override
        public int getMaxLevel() {
            return 10;
        }

        @Override
        public int getStartLevel() {
            return 1;
        }

        @Override
        public org.bukkit.enchantments.EnchantmentTarget getItemTarget() {
            return null;
        }

        @Override
        public boolean isTreasure() {
            return false;
        }

        @Override
        public boolean isCursed() {
            return false;
        }

        @Override
        public boolean conflictsWith(Enchantment other) {
            return false;
        }

        @Override
        public boolean canEnchantItem(ItemStack item) {
            return true;
        }

        @Override
        public String getTranslationKey() {
            return key.getKey();
        }

        @Override
        public boolean isRegistered() {
            return false;
        }

        @Override
        public NamespacedKey getKeyOrNull() {
            return key;
        }

        @Override
        public NamespacedKey getKeyOrThrow() {
            return key;
        }
    }
}
