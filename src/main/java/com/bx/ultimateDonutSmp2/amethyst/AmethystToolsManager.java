package com.bx.ultimateDonutSmp2.amethyst;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.FeatureManager;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.meta.BlockStateMeta;
import com.bx.ultimateDonutSmp2.utils.ShulkerBoxSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AmethystToolsManager {

    public final NamespacedKey KEY_TYPE;
    public final NamespacedKey KEY_EXPIRY;
    public final NamespacedKey KEY_OWNER;
    public final NamespacedKey KEY_ID;
    public final NamespacedKey KEY_LORE_TEMPLATE;

    private static final long DEFAULT_USE_COOLDOWN_MS = 250L;
    private static final long DEFAULT_VISUAL_SYNC_SUPPRESSION_MS = 3000L;

    private final UltimateDonutSmp2 plugin;
    private final AmethystPendingGrants pendingGrants;
    private final Map<UUID, Long> useCooldowns = new java.util.HashMap<>();
    private final Map<UUID, Long> visualSyncSuppressions = new java.util.HashMap<>();

    public enum GrantOutcome {
        ONLINE,
        QUEUED,
        FAILED
    }

    public AmethystToolsManager(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
        KEY_TYPE = new NamespacedKey(plugin, "amethyst_tool_type");
        KEY_EXPIRY = new NamespacedKey(plugin, "amethyst_tool_expiry");
        KEY_OWNER = new NamespacedKey(plugin, "amethyst_tool_owner");
        KEY_ID = new NamespacedKey(plugin, "amethyst_tool_id");
        KEY_LORE_TEMPLATE = new NamespacedKey(plugin, "amethyst_tool_lore");
        this.pendingGrants = new AmethystPendingGrants(
                new File(plugin.getDataFolder(), "amethyst-pending.yml"),
                plugin.getLogger()
        );
        this.pendingGrants.load();
    }

    public enum ShardToolVariant {
        PICKAXE_SILK(
                "SHARD-PICKAXE-SILK-ITEM",
                "Shard Pickaxe (Silk Touch)",
                "&#A303F9Shard Pickaxe",
                AmethystToolType.DRILL,
                Material.NETHERITE_PICKAXE,
                List.of("EFFICIENCY:5", "SILK_TOUCH:1", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fBreaks 9 Blocks at Once",
                        "&7Silk Touch",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        PICKAXE_FORTUNE(
                "SHARD-PICKAXE-FORTUNE-ITEM",
                "Shard Pickaxe (Fortune III)",
                "&#A303F9Shard Pickaxe",
                AmethystToolType.DRILL,
                Material.NETHERITE_PICKAXE,
                List.of("EFFICIENCY:5", "FORTUNE:3", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fBreaks 9 Blocks at Once",
                        "&7Fortune III",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        AXE(
                "SHARD-AXE-ITEM",
                "Shard Axe",
                "&#A303F9Shard Axe",
                AmethystToolType.CHOPPER,
                Material.NETHERITE_AXE,
                List.of("EFFICIENCY:5", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fBreaks Full Tree",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        SELL_AXE(
                "SHARD-SELL-AXE-ITEM",
                "Shard Sell Axe",
                "&#A303F9Shard Sell Axe",
                AmethystToolType.SELL_AXE,
                Material.NETHERITE_AXE,
                List.of("EFFICIENCY:5", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fSell Full Chests",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        SHOVEL_SILK(
                "SHARD-SHOVEL-SILK-ITEM",
                "Shard Shovel (Silk Touch)",
                "&#A303F9Shard Shovel",
                AmethystToolType.SHOVEL,
                Material.NETHERITE_SHOVEL,
                List.of("EFFICIENCY:5", "SILK_TOUCH:1", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fBreaks 9 Blocks at Once",
                        "&7Silk Touch",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        SHOVEL_FORTUNE(
                "SHARD-SHOVEL-FORTUNE-ITEM",
                "Shard Shovel (Fortune III)",
                "&#A303F9Shard Shovel",
                AmethystToolType.SHOVEL,
                Material.NETHERITE_SHOVEL,
                List.of("EFFICIENCY:5", "FORTUNE:3", "UNBREAKING:3", "MENDING:1"),
                List.of(
                        "&fBreaks 9 Blocks at Once",
                        "&7Fortune III",
                        "&7Efficiency V",
                        "&7Unbreaking III",
                        "&7Mending",
                        "&cSelf Destruct: {time}"
                ),
                List.of(),
                null,
                86400L
        ),
        HASTE_POTION(
                "SHARD-HASTE-POTION-ITEM",
                "Shard Potion of Haste",
                "&#A303F9Shard Potion of Haste",
                AmethystToolType.HASTE_POTION,
                Material.POTION,
                List.of(),
                List.of("&cSelf Destructs in {time}"),
                List.of("haste:1:9600"),
                "A303F9",
                86400L
        ),
        BUCKET(
                null,
                "Amethyst Bucket",
                "&#9B59B6&lAmethyst Bucket",
                AmethystToolType.BUCKET,
                Material.BUCKET,
                List.of(),
                List.of(),
                List.of(),
                null,
                86400L
        ),
        SHARD_BOOSTER(
                null,
                "Shard Booster",
                "&#9B59B6&lShard Booster",
                AmethystToolType.SHARD_BOOSTER,
                Material.POTION,
                List.of(),
                List.of(),
                List.of(),
                null,
                3600L
        );

        private final String shopKey;
        private final String friendlyName;
        private final String defaultDisplayName;
        private final AmethystToolType baseType;
        private final Material defaultMaterial;
        private final List<String> defaultEnchantments;
        private final List<String> defaultLoreTemplate;
        private final List<String> defaultPotionEffects;
        private final String defaultPotionColor;
        private final long defaultDuration;

        ShardToolVariant(
                String shopKey,
                String friendlyName,
                String defaultDisplayName,
                AmethystToolType baseType,
                Material defaultMaterial,
                List<String> defaultEnchantments,
                List<String> defaultLoreTemplate,
                List<String> defaultPotionEffects,
                String defaultPotionColor,
                long defaultDuration
        ) {
            this.shopKey = shopKey;
            this.friendlyName = friendlyName;
            this.defaultDisplayName = defaultDisplayName;
            this.baseType = baseType;
            this.defaultMaterial = defaultMaterial;
            this.defaultEnchantments = defaultEnchantments;
            this.defaultLoreTemplate = defaultLoreTemplate;
            this.defaultPotionEffects = defaultPotionEffects;
            this.defaultPotionColor = defaultPotionColor;
            this.defaultDuration = defaultDuration;
        }

        public String getShopKey() { return shopKey; }
        public String getFriendlyName() { return friendlyName; }
        public String getDefaultDisplayName() { return defaultDisplayName; }
        public AmethystToolType getBaseType() { return baseType; }
        public Material getDefaultMaterial() { return defaultMaterial; }
        public List<String> getDefaultEnchantments() { return defaultEnchantments; }
        public List<String> getDefaultLoreTemplate() { return defaultLoreTemplate; }
        public List<String> getDefaultPotionEffects() { return defaultPotionEffects; }
        public String getDefaultPotionColor() { return defaultPotionColor; }
        public long getDefaultDuration() { return defaultDuration; }

        public String getId() {
            return name().toLowerCase(Locale.ROOT).replace('_', '-');
        }
    }

    public static ShardToolVariant resolveVariant(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        String s = input.toLowerCase(Locale.ROOT).replace('_', '-').trim();
        return switch (s) {
            case "pickaxe-silk", "pickaxesilk", "drill-silk", "drillsilk" -> ShardToolVariant.PICKAXE_SILK;
            case "pickaxe-fortune", "pickaxefortune", "drill-fortune", "drillfortune", "pickaxe", "drill" -> ShardToolVariant.PICKAXE_FORTUNE;
            case "axe", "chopper", "tree-chopper", "treechopper" -> ShardToolVariant.AXE;
            case "sell-axe", "sellaxe" -> ShardToolVariant.SELL_AXE;
            case "shovel-silk", "shovelsilk" -> ShardToolVariant.SHOVEL_SILK;
            case "shovel-fortune", "shovelfortune", "shovel" -> ShardToolVariant.SHOVEL_FORTUNE;
            case "haste-potion", "hastepotion", "potion" -> ShardToolVariant.HASTE_POTION;
            case "bucket", "amethyst-bucket", "amethystbucket" -> ShardToolVariant.BUCKET;
            case "shard-booster", "shardbooster", "booster" -> ShardToolVariant.SHARD_BOOSTER;
            default -> {
                AmethystToolType type = AmethystToolType.fromString(input);
                if (type == null) {
                    yield null;
                }
                yield switch (type) {
                    case DRILL -> ShardToolVariant.PICKAXE_FORTUNE;
                    case CHOPPER -> ShardToolVariant.AXE;
                    case SELL_AXE -> ShardToolVariant.SELL_AXE;
                    case SHOVEL -> ShardToolVariant.SHOVEL_FORTUNE;
                    case HASTE_POTION -> ShardToolVariant.HASTE_POTION;
                    case BUCKET -> ShardToolVariant.BUCKET;
                    case SHARD_BOOSTER -> ShardToolVariant.SHARD_BOOSTER;
                };
            }
        };
    }

    public ItemStack createTool(ShardToolVariant variant, UUID ownerUuid, long durationSeconds) {
        if (variant == null) {
            return null;
        }

        if (variant.getShopKey() == null) {
            return createTool(variant.getBaseType(), ownerUuid, durationSeconds);
        }

        ConfigurationSection shopSec = null;
        if (plugin.getConfigManager() != null && plugin.getConfigManager().getShop() != null) {
            shopSec = plugin.getConfigManager().getShop().getConfigurationSection("SHARD-MENU." + variant.getShopKey());
        }

        String displayName = shopSec != null && shopSec.isString("DISPLAY-NAME")
                ? shopSec.getString("DISPLAY-NAME")
                : variant.getDefaultDisplayName();

        List<String> enchantments = shopSec != null && shopSec.isList("ENCHANTMENTS")
                ? shopSec.getStringList("ENCHANTMENTS")
                : variant.getDefaultEnchantments();

        List<String> loreTemplate;
        if (shopSec != null && shopSec.isList("LORE")) {
            loreTemplate = AmethystToolAppearance.ownedLoreTemplate(shopSec.getStringList("LORE"));
        } else {
            loreTemplate = variant.getDefaultLoreTemplate();
        }

        long duration = durationSeconds > 0L
                ? durationSeconds
                : (shopSec != null ? shopSec.getLong("AMETHYST-DURATION", variant.getDefaultDuration()) : variant.getDefaultDuration());

        Material material = variant.getDefaultMaterial();
        if (shopSec != null && shopSec.contains("MATERIAL")) {
            Material parsed = ItemUtils.parseMaterial(shopSec.getString("MATERIAL"));
            if (parsed != null && parsed != Material.AIR) {
                material = parsed;
            }
        }

        AmethystToolAppearance appearance = new AmethystToolAppearance(displayName, loreTemplate, enchantments);
        ItemStack item = createTool(variant.getBaseType(), ownerUuid, duration, appearance);
        if (item == null) {
            return null;
        }

        if (item.getType() != material) {
            item.setType(material);
        }

        if (variant.getBaseType() == AmethystToolType.HASTE_POTION && item.getItemMeta() instanceof PotionMeta potionMeta) {
            List<String> effects = shopSec != null && shopSec.isList("POTION-EFFECTS")
                    ? shopSec.getStringList("POTION-EFFECTS")
                    : variant.getDefaultPotionEffects();
            for (String eff : effects) {
                PotionEffect parsed = ItemUtils.parsePotionEffect(eff);
                if (parsed != null) {
                    potionMeta.addCustomEffect(parsed, true);
                }
            }
            String colorHex = shopSec != null && shopSec.isString("POTION-COLOR")
                    ? shopSec.getString("POTION-COLOR")
                    : variant.getDefaultPotionColor();
            if (colorHex != null) {
                Color c = ItemUtils.parseHexColor(colorHex);
                if (c != null) {
                    potionMeta.setColor(c);
                }
            }
            item.setItemMeta(potionMeta);
        }

        return item;
    }

    public ItemStack createTool(String typeOrVariant, UUID ownerUuid, long durationSeconds) {
        ShardToolVariant variant = resolveVariant(typeOrVariant);
        if (variant == null) {
            return null;
        }
        return createTool(variant, ownerUuid, durationSeconds);
    }

    public GrantOutcome grant(UUID targetId, ShardToolVariant variant, long durationSeconds) {
        if (targetId == null || variant == null) {
            return GrantOutcome.FAILED;
        }
        long duration = durationSeconds > 0L ? durationSeconds : variant.getDefaultDuration();
        if (duration <= 0L) {
            duration = 86_400L;
        }
        Player online = Bukkit.getPlayer(targetId);
        if (online != null && online.isOnline()) {
            ItemStack item = createTool(variant, targetId, duration);
            if (item == null) {
                return GrantOutcome.FAILED;
            }
            long grantedDuration = duration;
            plugin.getSpigotScheduler().runEntity(online, () -> {
                if (!online.isOnline()) {
                    pendingGrants.queue(targetId, variant.getId(), grantedDuration);
                    return;
                }
                deliverTo(online, item, variant, grantedDuration);
            });
            return GrantOutcome.ONLINE;
        }
        pendingGrants.queue(targetId, variant.getId(), duration);
        return GrantOutcome.QUEUED;
    }

    public void deliverPending(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        for (AmethystPendingGrants.Grant grant : pendingGrants.take(player.getUniqueId())) {
            ShardToolVariant variant = resolveVariant(grant.typeId());
            if (variant == null) {
                continue;
            }
            ItemStack item = createTool(variant, player.getUniqueId(), grant.durationSeconds());
            if (item == null) {
                continue;
            }
            deliverTo(player, item, variant, grant.durationSeconds());
        }
    }

    public int pendingCount(UUID playerId) {
        return pendingGrants.count(playerId);
    }

    public Material getDialogMaterial(ShardToolVariant variant) {
        if (variant == null) {
            return Material.NETHERITE_PICKAXE;
        }
        ConfigurationSection shop = shopSection(variant);
        if (shop != null && shop.contains("MATERIAL")) {
            Material parsed = ItemUtils.parseMaterial(shop.getString("MATERIAL"));
            if (parsed != null && parsed != Material.AIR) {
                return parsed;
            }
        }
        ConfigurationSection tool = getToolSection(variant.getBaseType());
        if (tool != null) {
            Material parsed = ItemUtils.parseMaterial(tool.getString("MATERIAL", variant.getDefaultMaterial().name()));
            if (parsed != null && parsed != Material.AIR) {
                return parsed;
            }
        }
        return variant.getDefaultMaterial();
    }

    public String getDialogName(ShardToolVariant variant) {
        if (variant == null) {
            return "Amethyst Tool";
        }
        ConfigurationSection shop = shopSection(variant);
        if (shop != null && shop.isString("DISPLAY-NAME")) {
            return shop.getString("DISPLAY-NAME");
        }
        ConfigurationSection tool = getToolSection(variant.getBaseType());
        if (tool != null && tool.isString("NAME")) {
            return tool.getString("NAME");
        }
        return variant.getDefaultDisplayName();
    }

    public List<String> getDialogLore(ShardToolVariant variant) {
        if (variant == null) {
            return List.of();
        }
        ConfigurationSection shop = shopSection(variant);
        if (shop != null && shop.isList("LORE")) {
            return AmethystToolAppearance.ownedLoreTemplate(shop.getStringList("LORE"));
        }
        if (!variant.getDefaultLoreTemplate().isEmpty()) {
            return variant.getDefaultLoreTemplate();
        }
        ConfigurationSection tool = getToolSection(variant.getBaseType());
        if (tool != null && tool.isList("LORE")) {
            return tool.getStringList("LORE");
        }
        return List.of();
    }

    private ConfigurationSection shopSection(ShardToolVariant variant) {
        if (variant == null || variant.getShopKey() == null) {
            return null;
        }
        if (plugin.getConfigManager() == null || plugin.getConfigManager().getShop() == null) {
            return null;
        }
        return plugin.getConfigManager().getShop().getConfigurationSection("SHARD-MENU." + variant.getShopKey());
    }

    private void deliverTo(Player player, ItemStack item, ShardToolVariant variant, long durationSeconds) {
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        leftovers.values().forEach(leftover ->
                player.getWorld().dropItemNaturally(player.getLocation(), leftover));
        player.sendMessage(ColorUtils.toComponent(getMessage(
                "RECEIVED",
                "{type}", getDialogName(variant),
                "{time}", AmethystDuration.formatDhM(durationSeconds),
                "{player}", player.getName()
        )));
    }

    public ItemStack createTool(AmethystToolType type, UUID ownerUuid, long durationSeconds) {
        return createTool(type, ownerUuid, durationSeconds, null);
    }

    public ItemStack createTool(
            AmethystToolType type,
            UUID ownerUuid,
            long durationSeconds,
            AmethystToolAppearance appearance
    ) {
        ConfigurationSection cfg = getToolSection(type);
        if (cfg == null) {
            return null;
        }

        Material material = ItemUtils.parseMaterial(cfg.getString("MATERIAL", "IRON_PICKAXE"));
        long duration = durationSeconds > 0 ? durationSeconds : cfg.getLong("DURATION", 86400L);
        long expiryEpoch = (System.currentTimeMillis() / 1000L) + duration;

        String name = appearance != null && appearance.hasName()
                ? appearance.name()
                : cfg.getString("NAME", "&e&lamethyst tool");
        List<String> loreTemplate = appearance != null && appearance.hasLore()
                ? appearance.loreTemplate()
                : cfg.getStringList("LORE");
        List<String> resolvedLore = AmethystToolAppearance.resolveLore(loreTemplate, duration);

        ItemStack item = ItemUtils.createItem(material, name, resolvedLore);
        item.setAmount(1);

        List<String> enchants = appearance != null && appearance.hasEnchantments()
                ? appearance.enchantments()
                : cfg.getStringList("ENCHANTMENTS");
        if (!enchants.isEmpty()) {
            ItemUtils.addEnchantments(item, enchants);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        if (type == AmethystToolType.SHARD_BOOSTER && meta instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(PotionType.WATER);
            meta = potionMeta;
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_TYPE, PersistentDataType.STRING, type.name());
        pdc.set(KEY_EXPIRY, PersistentDataType.LONG, expiryEpoch);
        pdc.set(KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
        if (!loreTemplate.isEmpty()) {
            pdc.set(KEY_LORE_TEMPLATE, PersistentDataType.STRING, AmethystToolAppearance.encodeTemplate(loreTemplate));
        }
        if (ownerUuid != null) {
            pdc.set(KEY_OWNER, PersistentDataType.STRING, ownerUuid.toString());
        }

        item.setItemMeta(meta);
        return item;
    }

    public boolean isEnabled() {
        return plugin.getFeatureManager().isEnabled(FeatureManager.Feature.AMETHYST_TOOLS);
    }

    public boolean hasAmethystMetadata(ItemStack item) {
        return item != null
                && item.hasItemMeta()
                && item.getItemMeta().getPersistentDataContainer().has(KEY_TYPE, PersistentDataType.STRING);
    }

    public boolean isAmethystTool(ItemStack item) {
        return isEnabled() && hasAmethystMetadata(item);
    }

    public boolean hasValidSignature(ItemStack item) {
        if (!hasAmethystMetadata(item)) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(KEY_EXPIRY, PersistentDataType.LONG)) {
            return false;
        }
        if (requiresItemId() && !pdc.has(KEY_ID, PersistentDataType.STRING)) {
            return false;
        }

        AmethystToolType type = getToolType(item);
        if (type == null) {
            return false;
        }

        ConfigurationSection cfg = getToolSection(type);
        if (cfg == null) {
            return false;
        }

        Material expected = ItemUtils.parseMaterial(cfg.getString("MATERIAL", item.getType().name()));
        return item.getType() == expected;
    }

    public AmethystToolType getToolType(ItemStack item) {
        if (!hasAmethystMetadata(item)) {
            return null;
        }
        String raw = item.getItemMeta().getPersistentDataContainer().get(KEY_TYPE, PersistentDataType.STRING);
        return AmethystToolType.fromString(raw);
    }

    public String getItemId(ItemStack item) {
        if (!isAmethystTool(item)) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(KEY_ID, PersistentDataType.STRING);
    }

    public long getExpiryEpoch(ItemStack item) {
        if (!hasAmethystMetadata(item)) {
            return 0L;
        }
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(KEY_EXPIRY, PersistentDataType.LONG, 0L);
    }

    public long getRemainingSeconds(ItemStack item) {
        long expiry = getExpiryEpoch(item);
        if (expiry <= 0L) {
            return 0L;
        }
        return expiry - (System.currentTimeMillis() / 1000L);
    }

    public boolean isExpired(ItemStack item) {
        return getRemainingSeconds(item) <= 0L;
    }

    public ItemStack createRewardCopy(ItemStack template, UUID ownerUuid, long durationSeconds) {
        if (!hasAmethystMetadata(template)) {
            return null;
        }

        ItemStack reward = template.clone();
        reward.setAmount(1);

        ItemMeta meta = reward.getItemMeta();
        if (meta == null) {
            return null;
        }

        String rawType = meta.getPersistentDataContainer().get(KEY_TYPE, PersistentDataType.STRING);
        AmethystToolType type = AmethystToolType.fromString(rawType);
        if (type == null) {
            return null;
        }

        long duration = durationSeconds > 0L ? durationSeconds : getConfiguredDuration(type);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_EXPIRY, PersistentDataType.LONG, (System.currentTimeMillis() / 1000L) + Math.max(1L, duration));
        pdc.set(KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
        if (ownerUuid != null) {
            pdc.set(KEY_OWNER, PersistentDataType.STRING, ownerUuid.toString());
        }

        reward.setItemMeta(meta);
        updateLoreCountdown(reward);
        return reward;
    }

    public boolean ensureIdentity(ItemStack item, UUID defaultOwner, boolean forceNewId) {
        if (!isAmethystTool(item)) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        AmethystToolType type = getToolType(item);
        if (type == null) {
            return false;
        }

        boolean changed = false;

        if (!pdc.has(KEY_EXPIRY, PersistentDataType.LONG)) {
            long defaultDuration = getToolSection(type) != null
                    ? getToolSection(type).getLong("DURATION", 86400L)
                    : 86400L;
            pdc.set(KEY_EXPIRY, PersistentDataType.LONG, (System.currentTimeMillis() / 1000L) + defaultDuration);
            changed = true;
        }

        if (forceNewId || !pdc.has(KEY_ID, PersistentDataType.STRING)) {
            pdc.set(KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
            changed = true;
        }

        if (defaultOwner != null && !pdc.has(KEY_OWNER, PersistentDataType.STRING)) {
            pdc.set(KEY_OWNER, PersistentDataType.STRING, defaultOwner.toString());
            changed = true;
        }

        if (changed) {
            item.setItemMeta(meta);
        }

        return changed;
    }

    public boolean isOwnedBy(Player player, ItemStack item) {
        if (!isAmethystTool(item) || !isOwnerBindingEnabled()) {
            return true;
        }

        String owner = item.getItemMeta().getPersistentDataContainer().get(KEY_OWNER, PersistentDataType.STRING);
        return owner == null || owner.equalsIgnoreCase(player.getUniqueId().toString());
    }

    public boolean isOnCooldown(UUID uuid) {
        Long last = useCooldowns.get(uuid);
        return last != null && System.currentTimeMillis() - last < getUseCooldownMs();
    }

    public void stampCooldown(UUID uuid) {
        useCooldowns.put(uuid, System.currentTimeMillis());
    }

    public void removeCooldown(UUID uuid) {
        useCooldowns.remove(uuid);
    }

    public boolean updateLoreCountdown(ItemStack item) {
        if (!hasAmethystMetadata(item)) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }

        AmethystToolType type = getToolType(item);
        if (type == null) {
            return false;
        }
        ConfigurationSection cfg = getToolSection(type);
        if (cfg == null) {
            return false;
        }

        List<String> templateLore = loreTemplateOf(meta, cfg);
        if (templateLore.isEmpty() || templateLore.stream().noneMatch(line -> line != null && line.contains("{time}"))) {
            return false;
        }

        long remaining = getRemainingSeconds(item);
        List<String> newLore = ColorUtils.colorizeList(
                ItemUtils.cleanLoreList(AmethystToolAppearance.resolveLore(templateLore, remaining))
        );
        if (loreEquals(lore, newLore)) {
            return false;
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return true;
    }

    private List<String> loreTemplateOf(ItemMeta meta, ConfigurationSection cfg) {
        String encoded = meta.getPersistentDataContainer().get(KEY_LORE_TEMPLATE, PersistentDataType.STRING);
        if (encoded != null && !encoded.isEmpty()) {
            return AmethystToolAppearance.decodeTemplate(encoded);
        }
        return cfg.getStringList("LORE");
    }

    private static boolean loreEquals(List<String> current, List<String> next) {
        if (current.size() != next.size()) {
            return false;
        }
        for (int i = 0; i < current.size(); i++) {
            if (!ColorUtils.strip(current.get(i)).equals(ColorUtils.strip(next.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public long getConfiguredDuration(AmethystToolType type) {
        ConfigurationSection section = getToolSection(type);
        return section == null ? 86400L : Math.max(1L, section.getLong("DURATION", 86400L));
    }

    public long getToolDuration(ItemStack item) {
        if (!hasAmethystMetadata(item)) {
            return 0L;
        }
        AmethystToolType type = getToolType(item);
        if (type == null) {
            return 0L;
        }
        return getConfiguredDuration(type);
    }

    public ItemStack createDisplayCopy(ItemStack template, long durationSeconds) {
        if (!hasAmethystMetadata(template)) {
            return template;
        }

        ItemStack display = template.clone();
        ItemMeta meta = display.getItemMeta();
        if (meta == null) {
            return display;
        }

        AmethystToolType type = getToolType(display);
        if (type == null) {
            return display;
        }

        long duration = durationSeconds > 0L ? durationSeconds : getConfiguredDuration(type);
        meta.getPersistentDataContainer().set(KEY_EXPIRY, PersistentDataType.LONG, (System.currentTimeMillis() / 1000L) + Math.max(1L, duration));
        display.setItemMeta(meta);
        updateLoreCountdown(display);
        return display;
    }

    public boolean refreshAmethystItemsInShulker(ItemStack shulkerItem, UUID ownerUuid, long durationSeconds) {
        if (!ShulkerBoxSupport.isShulkerBox(shulkerItem)) {
            return false;
        }

        ItemMeta itemMeta = shulkerItem.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta bsm)) {
            return false;
        }

        if (!(bsm.getBlockState() instanceof ShulkerBox box)) {
            return false;
        }

        ItemStack[] contents = box.getInventory().getContents();
        boolean changed = false;
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current == null || current.getType() == Material.AIR) {
                continue;
            }

            if (hasAmethystMetadata(current)) {
                ItemStack fresh = createRewardCopy(current, ownerUuid, durationSeconds);
                if (fresh != null) {
                    contents[i] = fresh;
                    changed = true;
                }
            } else if (ShulkerBoxSupport.isShulkerBox(current)) {
                if (refreshAmethystItemsInShulker(current, ownerUuid, durationSeconds)) {
                    contents[i] = current;
                    changed = true;
                }
            }
        }

        if (changed) {
            box.getInventory().setContents(contents);
            bsm.setBlockState(box);
            shulkerItem.setItemMeta(bsm);
        }
        return changed;
    }

    public boolean prepareCrateDisplayShulker(ItemStack shulkerItem, long durationSeconds) {
        if (!ShulkerBoxSupport.isShulkerBox(shulkerItem)) {
            return false;
        }

        ItemMeta itemMeta = shulkerItem.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta bsm)) {
            return false;
        }

        if (!(bsm.getBlockState() instanceof ShulkerBox box)) {
            return false;
        }

        ItemStack[] contents = box.getInventory().getContents();
        boolean changed = false;
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current == null || current.getType() == Material.AIR) {
                continue;
            }

            if (hasAmethystMetadata(current)) {
                contents[i] = createDisplayCopy(current, durationSeconds);
                changed = true;
            } else if (ShulkerBoxSupport.isShulkerBox(current)) {
                if (prepareCrateDisplayShulker(current, durationSeconds)) {
                    contents[i] = current;
                    changed = true;
                }
            }
        }

        if (changed) {
            box.getInventory().setContents(contents);
            bsm.setBlockState(box);
            shulkerItem.setItemMeta(bsm);
        }
        return changed;
    }

    public void sanitizePlayerInventory(Player player, boolean notifyExpired) {
        PlayerInventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            sanitizeInventorySlot(player, slot, notifyExpired);
        }
    }

    public boolean sanitizeInventorySlot(Player player, int slot, boolean notifyExpired) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItem(slot);
        if (!isAmethystTool(item)) {
            return false;
        }

        boolean changed = ensureIdentity(item, player.getUniqueId(), false);

        if (item.getAmount() > 1) {
            splitStack(player, slot, item);
            changed = true;
            item = inventory.getItem(slot);
            if (item == null) {
                return true;
            }
        }

        if (!hasValidSignature(item)) {
            inventory.setItem(slot, null);
            return true;
        }

        if (isExpired(item)) {
            expireItem(player, slot, item, notifyExpired);
            return true;
        }

        if (changed) {
            inventory.setItem(slot, item);
        }

        return changed;
    }

    public boolean sanitizeHeldItem(Player player, boolean notifyExpired) {
        return sanitizeInventorySlot(player, player.getInventory().getHeldItemSlot(), notifyExpired);
    }

    public boolean sanitizeExternalInventorySlot(Player player, Inventory inventory, int slot, boolean notifyExpired) {
        if (inventory == null) {
            return false;
        }

        ItemStack item = inventory.getItem(slot);
        if (!isAmethystTool(item)) {
            return false;
        }

        boolean changed = ensureIdentity(item, null, false);

        if (item.getAmount() > 1) {
            splitExternalStack(player, inventory, slot, item);
            changed = true;
            item = inventory.getItem(slot);
            if (item == null) {
                return true;
            }
        }

        if (!hasValidSignature(item)) {
            inventory.setItem(slot, null);
            return true;
        }

        if (isExpired(item)) {
            AmethystToolType type = getToolType(item);
            inventory.setItem(slot, null);
            sendExpireFeedback(player, type, notifyExpired);
            return true;
        }

        if (changed) {
            inventory.setItem(slot, item);
        }

        return changed;
    }

    private void splitStack(Player player, int slot, ItemStack stack) {
        if (!isAmethystTool(stack) || stack.getAmount() <= 1) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        int amount = stack.getAmount();

        ItemStack base = stack.clone();
        base.setAmount(1);
        ensureIdentity(base, player.getUniqueId(), true);
        inventory.setItem(slot, base);

        for (int i = 1; i < amount; i++) {
            ItemStack extra = stack.clone();
            extra.setAmount(1);
            ensureIdentity(extra, player.getUniqueId(), true);
            Map<Integer, ItemStack> leftovers = inventory.addItem(extra);
            leftovers.values().forEach(leftover ->
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover));
        }
    }

    /** Expires whatever amethyst tool sits in the given slot, main hand or off hand alike. */
    public void expireItemInSlot(Player player, int slot) {
        ItemStack item = player.getInventory().getItem(slot);
        if (isAmethystTool(item)) {
            expireItem(player, slot, item, true);
        }
    }

    public boolean sanitizeCursorItem(Player player, boolean notifyExpired) {
        ItemStack cursor = player.getItemOnCursor();
        if (!isAmethystTool(cursor)) {
            return false;
        }

        boolean changed = ensureIdentity(cursor, player.getUniqueId(), false);

        if (cursor.getAmount() > 1) {
            splitCursorStack(player, cursor);
            changed = true;
            cursor = player.getItemOnCursor();
            if (cursor == null) {
                return true;
            }
        }

        if (!hasValidSignature(cursor)) {
            player.setItemOnCursor(null);
            return true;
        }

        if (isExpired(cursor)) {
            expireCursorItem(player, cursor, notifyExpired);
            return true;
        }

        if (changed) {
            player.setItemOnCursor(cursor);
        }

        return changed;
    }

    public void expireItem(Player player, int slot, ItemStack item, boolean sendFeedback) {
        if (!isAmethystTool(item)) {
            return;
        }

        AmethystToolType type = getToolType(item);
        player.getInventory().setItem(slot, null);

        sendExpireFeedback(player, type, sendFeedback);
    }

    private void splitCursorStack(Player player, ItemStack stack) {
        if (!isAmethystTool(stack) || stack.getAmount() <= 1) {
            return;
        }

        int amount = stack.getAmount();

        ItemStack base = stack.clone();
        base.setAmount(1);
        ensureIdentity(base, player.getUniqueId(), true);
        player.setItemOnCursor(base);

        for (int i = 1; i < amount; i++) {
            ItemStack extra = stack.clone();
            extra.setAmount(1);
            ensureIdentity(extra, player.getUniqueId(), true);
            Map<Integer, ItemStack> leftovers = player.getInventory().addItem(extra);
            leftovers.values().forEach(leftover ->
                    player.getWorld().dropItemNaturally(player.getLocation(), leftover));
        }
    }

    private void splitExternalStack(Player player, Inventory inventory, int slot, ItemStack stack) {
        if (!isAmethystTool(stack) || stack.getAmount() <= 1) {
            return;
        }

        int amount = stack.getAmount();

        ItemStack base = stack.clone();
        base.setAmount(1);
        ensureIdentity(base, null, true);
        inventory.setItem(slot, base);

        for (int i = 1; i < amount; i++) {
            ItemStack extra = stack.clone();
            extra.setAmount(1);
            ensureIdentity(extra, null, true);
            player.getWorld().dropItemNaturally(player.getLocation(), extra);
        }
    }

    private void expireCursorItem(Player player, ItemStack item, boolean sendFeedback) {
        if (!isAmethystTool(item)) {
            return;
        }

        AmethystToolType type = getToolType(item);
        player.setItemOnCursor(null);
        sendExpireFeedback(player, type, sendFeedback);
    }

    private void sendExpireFeedback(Player player, AmethystToolType type, boolean sendFeedback) {
        if (!sendFeedback) {
            return;
        }

        String toolName = type != null ? type.getDisplayName() : "Amethyst Tool";

        SoundUtils.play(player, getSound("EXPIRE"));
        spawnAmethystParticles(player.getLocation().add(0, 1, 0));
        String msg = getMessage("EXPIRED", "{tool}", toolName);
        player.sendMessage(ColorUtils.toComponent(msg));
    }

    public ConfigurationSection getToolSection(AmethystToolType type) {
        ConfigurationSection root = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-TOOLS");
        if (root == null) {
            return null;
        }
        return root.getConfigurationSection(type.getConfigKey());
    }

    public List<String> getExcludedWorlds() {
        ConfigurationSection root = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-TOOLS");
        if (root == null) {
            return Collections.emptyList();
        }
        return root.getStringList("EXCLUDED-WORLDS");
    }

    public boolean isExcludedWorld(String worldName) {
        return getExcludedWorlds().stream().anyMatch(excluded -> excluded.equalsIgnoreCase(worldName));
    }

    public String getMessage(String key) {
        ConfigurationSection msgs = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-MESSAGES");
        if (msgs == null) {
            return key;
        }
        String prefix = msgs.getString("PREFIX", "&#9B59B6[amethyst] &r");
        String raw = msgs.getString(key, key);
        return raw.replace("{prefix}", prefix);
    }

    public String getMessage(String key, String... replacements) {
        String msg = getMessage(key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return msg;
    }

    public String getSound(String key) {
        ConfigurationSection root = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-TOOLS.SOUNDS");
        if (root == null) {
            return "";
        }
        return root.getString(key, "");
    }

    public String getToolPermission(AmethystToolType type) {
        ConfigurationSection section = getToolSection(type);
        if (section == null) {
            return "";
        }
        return section.getString("PERMISSION", "").trim();
    }

    public boolean isOwnerBindingEnabled() {
        return getSecuritySection().getBoolean("BIND-TO-OWNER", false);
    }

    public boolean requiresItemId() {
        return getSecuritySection().getBoolean("REQUIRE-ITEM-ID", true);
    }

    public boolean shouldBlockAutomationPickup() {
        return getSecuritySection().getBoolean("BLOCK-HOPPER-PICKUP", true);
    }

    public long getUseCooldownMs() {
        return Math.max(0L, getSecuritySection().getLong("CLICK-COOLDOWN-MS", DEFAULT_USE_COOLDOWN_MS));
    }

    public void suppressVisualSync(UUID uuid) {
        suppressVisualSync(uuid, DEFAULT_VISUAL_SYNC_SUPPRESSION_MS);
    }

    public void suppressVisualSync(UUID uuid, long durationMs) {
        if (uuid == null || durationMs <= 0L) {
            return;
        }
        visualSyncSuppressions.put(uuid, System.currentTimeMillis() + durationMs);
    }

    public boolean isVisualSyncSuppressed(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        Long until = visualSyncSuppressions.get(uuid);
        if (until == null) {
            return false;
        }

        if (until <= System.currentTimeMillis()) {
            visualSyncSuppressions.remove(uuid);
            return false;
        }

        return true;
    }

    public long getShardBoosterDurationSeconds() {
        ConfigurationSection section = getToolSection(AmethystToolType.SHARD_BOOSTER);
        if (section == null) {
            return 86400L;
        }
        return Math.max(1L, section.getLong("BOOSTER-DURATION", 86400L));
    }

    public Set<Material> getDisabledBlocks() {
        return parseMaterialSet(getToolSection(AmethystToolType.DRILL), "DISABLED-BLOCKS");
    }

    public Set<Material> getAllowedBlocks() {
        return parseMaterialSet(getToolSection(AmethystToolType.SHOVEL), "ALLOWED-BLOCKS");
    }

    public Set<Material> getLogBlocks() {
        return parseMaterialSet(getToolSection(AmethystToolType.CHOPPER), "LOG-BLOCKS");
    }

    private Set<Material> parseMaterialSet(ConfigurationSection section, String key) {
        if (section == null) {
            return EnumSet.noneOf(Material.class);
        }

        Set<Material> set = EnumSet.noneOf(Material.class);
        for (String name : section.getStringList(key)) {
            try {
                set.add(Material.valueOf(name.toUpperCase(Locale.ROOT).trim()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return set;
    }

    public void spawnAmethystParticles(Location location) {
        ConfigurationSection root = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-TOOLS.PARTICLES");
        if (root == null || !root.getBoolean("ENABLED", true) || location.getWorld() == null) {
            return;
        }

        int count = root.getInt("COUNT", 12);
        double spread = root.getDouble("SPREAD", 0.4D);
        String particleName = root.getString("TYPE", "BLOCK").toUpperCase(Locale.ROOT);
        Material blockMaterial = ItemUtils.parseMaterial(root.getString("BLOCK-MATERIAL", "PURPLE_CONCRETE_POWDER"));

        try {
            Particle particle = Particle.valueOf(particleName);
            if (particle.getDataType() == BlockData.class) {
                BlockData blockData = blockMaterial.createBlockData();
                location.getWorld().spawnParticle(
                        particle,
                        location.clone().add(0.5, 0.5, 0.5),
                        count,
                        spread,
                        spread,
                        spread,
                        0.0D,
                        blockData
                );
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0x9B, 0x59, 0xB6), 1.2f);
        location.getWorld().spawnParticle(
                Particle.DUST,
                location.clone().add(0.5, 0.5, 0.5),
                count,
                spread,
                spread,
                spread,
                0.0D,
                dust
        );
    }

    private ConfigurationSection getSecuritySection() {
        ConfigurationSection root = plugin.getConfigManager().getAmethystTools()
                .getConfigurationSection("AMETHYST-TOOLS.SECURITY");
        if (root != null) {
            return root;
        }
        return plugin.getConfigManager().getAmethystTools().createSection("AMETHYST-TOOLS.SECURITY");
    }
}
