package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Enchant step for the Quick Buy chest catalogue. Bedrock cannot open the Java enchant dialog,
 * and the Floodgate form is a different screen.
 */
public class QuickBuyBedrockEnchantMenu extends BaseMenu {

    static final int PAGE_SIZE = 45;
    private static final int BACK_SLOT = 45;
    private static final int PREV_SLOT = 47;
    private static final int SKIP_SLOT = 49;
    private static final int NEXT_SLOT = 51;
    private static final int CONFIRM_SLOT = 53;

    private final int targetSlot;
    private final Material material;
    private final String query;
    private final List<QuickBuyItemDialog.EnchantmentSpec> specs;
    private final Map<String, Integer> levels = new LinkedHashMap<>();
    private int page;

    public QuickBuyBedrockEnchantMenu(UltimateDonutSmp2 plugin, int targetSlot, Material material, String query) {
        super(plugin, "&8Choose Enchantments", 54);
        this.targetSlot = targetSlot;
        this.material = material;
        this.query = query == null ? "" : query;
        List<QuickBuyItemDialog.EnchantmentSpec> loaded = QuickBuyItemDialog.getEnchantmentSpecs(plugin, material);
        this.specs = loaded == null ? List.of() : List.copyOf(loaded);
    }

    public static int nextLevel(int current, int maxLevel) {
        int max = Math.max(1, maxLevel);
        int safe = Math.max(0, current);
        return safe >= max ? 0 : safe + 1;
    }

    public static int pageCount(int total) {
        if (total <= 0) {
            return 1;
        }
        return (total + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    public static String serialize(Map<String, Integer> levels) {
        if (levels == null || levels.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("ENCHANTS:");
        boolean any = false;
        for (Map.Entry<String, Integer> entry : levels.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank() || entry.getValue() == null || entry.getValue() <= 0) {
                continue;
            }
            if (any) {
                builder.append(';');
            }
            any = true;
            builder.append(entry.getKey().trim().toLowerCase(Locale.ROOT))
                    .append('=')
                    .append(entry.getValue());
        }
        return any ? builder.toString() : "";
    }

    public static List<String> enchantLore(String serialized) {
        List<String> lines = new ArrayList<>();
        if (serialized == null || !serialized.startsWith("ENCHANTS:")) {
            return lines;
        }
        String body = serialized.substring("ENCHANTS:".length());
        if (body.isBlank()) {
            return lines;
        }
        for (String part : body.split(";")) {
            int split = part.indexOf('=');
            if (split <= 0) {
                continue;
            }
            String key = part.substring(0, split).trim();
            String level = part.substring(split + 1).trim();
            if (key.isEmpty() || level.isEmpty()) {
                continue;
            }
            lines.add("&7" + QuickBuyItemDialog.prettifyEnchantmentName(key) + " " + level);
        }
        return lines;
    }

    @Override
    public void build(Player player) {
        if (inventory == null) {
            return;
        }
        fill(Material.GRAY_STAINED_GLASS_PANE);
        int pages = pageCount(specs.size());
        if (page >= pages) {
            page = pages - 1;
        }
        if (page < 0) {
            page = 0;
        }
        int start = page * PAGE_SIZE;
        for (int index = start; index < specs.size() && index < start + PAGE_SIZE; index++) {
            QuickBuyItemDialog.EnchantmentSpec spec = specs.get(index);
            int level = levels.getOrDefault(spec.key(), 0);
            List<String> lore = new ArrayList<>();
            lore.add(level <= 0 ? "&7Level: &8none" : "&7Level: &e" + level + "&7/&f" + spec.maxLevel());
            lore.add("&eClick to change");
            set(index - start, ItemUtils.createItem(
                    Material.ENCHANTED_BOOK,
                    "&f" + spec.displayName(),
                    lore
            ));
        }

        set(BACK_SLOT, ItemUtils.createItem(Material.ARROW, "&cBack", List.of("&7Return to items")));
        set(SKIP_SLOT, ItemUtils.createItem(Material.BARRIER, "&eSkip", List.of("&7Pin the item with no enchants")));
        set(CONFIRM_SLOT, ItemUtils.createItem(Material.LIME_STAINED_GLASS_PANE, "&aConfirm", List.of("&7Continue to the amount")));
        if (page > 0) {
            set(PREV_SLOT, ItemUtils.createItem(Material.ARROW, "&fPrevious", List.of("&7Page " + page + "/" + pages)));
        }
        if (page + 1 < pages) {
            set(NEXT_SLOT, ItemUtils.createItem(Material.ARROW, "&fNext", List.of("&7Page " + (page + 2) + "/" + pages)));
        }
    }

    @Override
    public void handleClick(int slot, Player player) {
        if (slot == BACK_SLOT) {
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            new QuickBuyItemSelectMenu(plugin, targetSlot, 0, query).open(player);
            return;
        }
        if (slot == SKIP_SLOT) {
            QuickBuySounds.click(player, plugin);
            new QuickBuyItemAmountMenu(plugin, targetSlot, material, 1, "").open(player);
            return;
        }
        if (slot == CONFIRM_SLOT) {
            QuickBuySounds.click(player, plugin);
            new QuickBuyItemAmountMenu(plugin, targetSlot, material, 1, serialize(levels)).open(player);
            return;
        }
        if (slot == PREV_SLOT && page > 0) {
            page--;
            QuickBuySounds.pageTurn(player, plugin);
            build(player);
            return;
        }
        int pages = pageCount(specs.size());
        if (slot == NEXT_SLOT && page + 1 < pages) {
            page++;
            QuickBuySounds.pageTurn(player, plugin);
            build(player);
            return;
        }
        if (slot < 0 || slot >= PAGE_SIZE) {
            return;
        }
        int index = page * PAGE_SIZE + slot;
        if (index < 0 || index >= specs.size()) {
            return;
        }
        QuickBuyItemDialog.EnchantmentSpec spec = specs.get(index);
        int next = nextLevel(levels.getOrDefault(spec.key(), 0), spec.maxLevel());
        if (next <= 0) {
            levels.remove(spec.key());
        } else {
            levels.put(spec.key(), next);
        }
        QuickBuySounds.click(player, plugin);
        build(player);
    }
}
