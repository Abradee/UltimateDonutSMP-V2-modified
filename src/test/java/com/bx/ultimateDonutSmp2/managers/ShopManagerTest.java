package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.models.AuctionListing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopManagerTest {

    private static final long NOW = 1_000_000L;
    private static final UUID BUYER = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID SELLER = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void selectsLowestMatchingAuctionUnitPrice() {
        AuctionListing single = listing(1, SELLER, Material.DIAMOND, 1, 90D, NOW + 5_000L);
        AuctionListing stack = listing(2, SELLER, Material.DIAMOND, 10, 500D, NOW + 5_000L);
        AuctionListing different = listing(3, SELLER, Material.EMERALD, 64, 64D, NOW + 5_000L);

        ShopManager.AuctionQuote quote = ShopManager.findBestAuctionQuote(
                List.of(single, stack, different),
                BUYER,
                new ItemStack(Material.DIAMOND),
                NOW,
                (candidate, desired) -> candidate.getType() == desired.getType()
        );

        assertEquals(2L, quote.listing().id());
        assertEquals(50D, quote.unitPrice());
    }

    @Test
    void ignoresOwnExpiredInactiveAndDifferentListings() {
        AuctionListing own = listing(1, BUYER, Material.DIAMOND, 64, 64D, NOW + 5_000L);
        AuctionListing expired = listing(2, SELLER, Material.DIAMOND, 64, 64D, NOW - 1L);
        AuctionListing sold = new AuctionListing(
                3,
                SELLER,
                "Seller",
                BUYER,
                AuctionListing.Status.SOLD,
                64D,
                0D,
                new ItemStack(Material.DIAMOND, 64),
                NOW - 100L,
                NOW + 5_000L,
                NOW,
                0L,
                0L,
                "ALL"
        );
        AuctionListing different = listing(4, SELLER, Material.EMERALD, 64, 64D, NOW + 5_000L);

        assertNull(ShopManager.findBestAuctionQuote(
                List.of(own, expired, sold, different),
                BUYER,
                new ItemStack(Material.DIAMOND),
                NOW,
                (candidate, desired) -> candidate.getType() == desired.getType()
        ));
    }

    @Test
    void recognizesManagedSpawnerRewardCommands() {
        assertTrue(ShopManager.isManagedSpawnerRewardCommand(
                "spawner give {username} pig {amount}"
        ));
        assertTrue(ShopManager.isManagedSpawnerRewardCommand(
                "/SPAWNER GIVE {player} iron_golem {amount}"
        ));
        assertFalse(ShopManager.isManagedSpawnerRewardCommand(
                "give {username} spawner {amount}"
        ));
    }

    @Test
    void partialStackAcceptsABuyWhenNoSlotIsEmpty() throws Exception {
        withItemFactory(() -> {
            ItemStack[] storage = filled(Material.COBBLESTONE, new ItemStack(Material.END_CRYSTAL, 10));

            assertTrue(ShopManager.canFitQuickBuyStack(storage, new ItemStack(Material.END_CRYSTAL, 1), 64));
            assertTrue(ShopManager.canFitQuickBuyStack(storage, new ItemStack(Material.END_CRYSTAL, 54), 64));
            assertFalse(ShopManager.canFitQuickBuyStack(storage, new ItemStack(Material.END_CRYSTAL, 55), 64));
        });
    }

    @Test
    void fullStackAndUnrelatedItemsLeaveNoRoom() throws Exception {
        withItemFactory(() -> {
            ItemStack[] fullCrystals = filled(Material.COBBLESTONE, new ItemStack(Material.END_CRYSTAL, 64));
            ItemStack[] noCrystals = filled(Material.COBBLESTONE);

            assertFalse(ShopManager.canFitQuickBuyStack(fullCrystals, new ItemStack(Material.END_CRYSTAL, 1), 64));
            assertFalse(ShopManager.canFitQuickBuyStack(noCrystals, new ItemStack(Material.END_CRYSTAL, 1), 64));
        });
    }

    @Test
    void emptySlotFitsANewStackAndPartialStacksCombine() throws Exception {
        withItemFactory(() -> {
            ItemStack[] withGap = filled(Material.COBBLESTONE);
            withGap[0] = null;
            ItemStack[] split = filled(
                    Material.COBBLESTONE,
                    new ItemStack(Material.END_CRYSTAL, 40),
                    new ItemStack(Material.END_CRYSTAL, 40)
            );

            assertTrue(ShopManager.canFitQuickBuyStack(withGap, new ItemStack(Material.END_CRYSTAL, 64), 64));
            assertFalse(ShopManager.canFitQuickBuyStack(withGap, new ItemStack(Material.END_CRYSTAL, 65), 64));
            assertTrue(ShopManager.canFitQuickBuyStack(split, new ItemStack(Material.END_CRYSTAL, 48), 64));
            assertFalse(ShopManager.canFitQuickBuyStack(
                    filled(Material.TOTEM_OF_UNDYING),
                    new ItemStack(Material.TOTEM_OF_UNDYING, 1),
                    1
            ));
        });
    }

    private static ItemStack[] filled(Material filler, ItemStack... overrides) {
        ItemStack[] storage = new ItemStack[36];
        ItemStack fill = new ItemStack(filler, 64);
        for (int slot = 0; slot < storage.length; slot++) {
            storage[slot] = fill.clone();
        }
        for (int slot = 0; slot < overrides.length; slot++) {
            storage[slot] = overrides[slot];
        }
        return storage;
    }

    private static void withItemFactory(Runnable checks) throws Exception {
        Field serverField = org.bukkit.Bukkit.class.getDeclaredField("server");
        serverField.setAccessible(true);
        Object previous = serverField.get(null);
        serverField.set(null, itemFactoryServer());
        try {
            checks.run();
        } finally {
            serverField.set(null, previous);
        }
    }

    private static org.bukkit.Server itemFactoryServer() {
        Object itemFactory = Proxy.newProxyInstance(
                org.bukkit.inventory.ItemFactory.class.getClassLoader(),
                new Class<?>[]{org.bukkit.inventory.ItemFactory.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getItemMeta" -> null;
                    case "equals" -> args != null && args.length == 2 && java.util.Objects.equals(args[0], args[1]);
                    default -> defaultValue(method);
                });
        Object[] registry = new Object[1];
        return (org.bukkit.Server) Proxy.newProxyInstance(
                org.bukkit.Server.class.getClassLoader(),
                new Class<?>[]{org.bukkit.Server.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getItemFactory" -> itemFactory;
                    case "getRegistry" -> {
                        if (registry[0] == null) {
                            Class<?> registryClass = Class.forName("org.bukkit.Registry");
                            registry[0] = Proxy.newProxyInstance(
                                    registryClass.getClassLoader(),
                                    new Class<?>[]{registryClass},
                                    (registryProxy, registryMethod, registryArgs) -> defaultValue(registryMethod));
                        }
                        yield registry[0];
                    }
                    default -> defaultValue(method);
                });
    }

    private static Object defaultValue(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == int.class) {
            return 0;
        }
        return null;
    }

    @Test
    void managedSpawnerRewardNeverAlsoDeliversConfiguredVanillaItem() {
        ShopManager.ShopItem managedSpawner = new ShopManager.ShopItem(
                "PIG-SPAWNER-ITEM",
                "SHARD-MENU",
                Material.SPAWNER,
                "Pig Spawner",
                List.of(),
                9,
                250D,
                ShopManager.Currency.SHARD,
                "spawner give {username} pig {amount}",
                true,
                "",
                1,
                64,
                1,
                false,
                null,
                -1L
        );

        assertFalse(ShopManager.shouldDeliverConfiguredItem(managedSpawner));
    }

    @Test
    void readsCurrencyNames() {
        assertEquals(ShopManager.Currency.SHARD, ShopManager.parseCurrency("SHARD"));
        assertEquals(ShopManager.Currency.SHARD, ShopManager.parseCurrency("shards"));
        assertEquals(ShopManager.Currency.MONEY, ShopManager.parseCurrency("MONEY"));
        assertEquals(ShopManager.Currency.MONEY, ShopManager.parseCurrency("gold bars"));
        assertEquals(ShopManager.Currency.MONEY, ShopManager.parseCurrency(""));
        assertEquals(ShopManager.Currency.MONEY, ShopManager.parseCurrency(null));
    }

    private AuctionListing listing(
            long id,
            UUID seller,
            Material material,
            int amount,
            double price,
            long expiresAt
    ) {
        return new AuctionListing(
                id,
                seller,
                "Seller",
                null,
                AuctionListing.Status.ACTIVE,
                price,
                0D,
                new ItemStack(material, amount),
                NOW - id,
                expiresAt,
                0L,
                0L,
                0L,
                "ALL"
        );
    }
}
