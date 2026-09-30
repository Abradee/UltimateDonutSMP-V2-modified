package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.menus.QuickBuyItemSelectMenu;
import com.bx.ultimateDonutSmp2.menus.QuickBuyMenu;
import com.bx.ultimateDonutSmp2.menus.QuickBuySounds;
import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Paged Floodgate catalogue for pinning a Quick Buy item. The Java Choose Item dialog sends one
 * button per material, and that form freezes a Bedrock client.
 */
public final class QuickBuyBedrockManager {

    private final UltimateDonutSmp2 plugin;
    private final Map<UUID, Draft> drafts = new ConcurrentHashMap<>();

    public QuickBuyBedrockManager(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    public boolean openCatalogue(Player player, int targetSlot, String query, int page) {
        if (!accepts(player)) {
            return false;
        }
        Draft draft = draft(player);
        draft.slot = targetSlot;
        draft.query = query == null ? "" : query.trim();
        draft.page = Math.max(0, page);
        draft.material = null;
        draft.enchants.clear();
        closeMenu(player);
        plugin.getSpigotScheduler().runEntityLater(player, () -> {
            if (!player.isOnline()) {
                return;
            }
            if (!send(player, catalogueForm(player, draft))) {
                new QuickBuyItemSelectMenu(plugin, draft.slot, 0, draft.query).open(player);
            }
        }, 1L);
        return true;
    }

    private SimpleForm catalogueForm(Player player, Draft draft) {
        List<Material> items = QuickBuyItemDialog.getSelectableMaterials(plugin, draft.query);
        int pageSize = pageSize();
        int pages = QuickBuyBedrockPages.pageCount(items.size(), pageSize);
        int page = QuickBuyBedrockPages.clampPage(draft.page, pages);
        draft.page = page;
        List<Material> shown = QuickBuyBedrockPages.slice(items, page, pageSize);
        int shownPage = page + 1;

        String content;
        if (items.isEmpty() && !draft.query.isBlank()) {
            content = text("QUICK_BUY.BEDROCK.CATALOGUE_EMPTY", "Nothing matches {query}.",
                    "{query}", draft.query);
        } else if (!draft.query.isBlank()) {
            content = text("QUICK_BUY.BEDROCK.CATALOGUE_SEARCHING",
                    "Search: {query}. Page {page}/{pages}.",
                    "{query}", draft.query,
                    "{page}", String.valueOf(shownPage),
                    "{pages}", String.valueOf(pages));
        } else {
            content = text("QUICK_BUY.BEDROCK.CATALOGUE_PAGE", "Page {page}/{pages}.",
                    "{page}", String.valueOf(shownPage),
                    "{pages}", String.valueOf(pages));
        }

        SimpleForm.Builder form = SimpleForm.builder()
                .title(text("QUICK_BUY.BEDROCK.CATALOGUE_TITLE", "Choose Item"))
                .content(content);
        List<Runnable> actions = new ArrayList<>();

        form.button(text("QUICK_BUY.BEDROCK.BUTTON_SEARCH", "Search"));
        actions.add(() -> openSearch(player));

        form.button(text("QUICK_BUY.BEDROCK.BUTTON_BACK", "Back"));
        actions.add(() -> returnToShop(player));

        if (page > 0) {
            form.button(text("QUICK_BUY.BEDROCK.BUTTON_PREVIOUS", "Previous page"));
            actions.add(() -> {
                QuickBuySounds.pageTurn(player, plugin);
                openCatalogue(player, draft.slot, draft.query, page - 1);
            });
        }
        if (page + 1 < pages && !shown.isEmpty()) {
            form.button(text("QUICK_BUY.BEDROCK.BUTTON_NEXT", "Next page"));
            actions.add(() -> {
                QuickBuySounds.pageTurn(player, plugin);
                openCatalogue(player, draft.slot, draft.query, page + 1);
            });
        }
        for (Material material : shown) {
            Material chosen = material;
            form.button(itemLabel(player, chosen));
            actions.add(() -> select(player, chosen));
        }

        bind(player, draft, form, actions, () -> returnToShop(player));
        return form.build();
    }

    private void openSearch(Player player) {
        Draft draft = draft(player);
        CustomForm.Builder form = CustomForm.builder()
                .title(text("QUICK_BUY.BEDROCK.SEARCH_TITLE", "Search items"))
                .input(text("QUICK_BUY.BEDROCK.SEARCH_INPUT", "Item name"), "", draft.query);
        int generation = draft.generation;
        form.validResultHandler(response -> {
            draft.generation++;
            schedule(player, () -> {
                String typed = response.asInput(0);
                QuickBuySounds.play(player, plugin, QuickBuySounds.SEARCH);
                openCatalogue(player, draft.slot, typed == null ? "" : typed, 0);
            });
        });
        form.closedResultHandler(() -> {
            if (draft.generation != generation) {
                return;
            }
            schedule(player, () -> openCatalogue(player, draft.slot, draft.query, draft.page));
        });
        send(player, form.build());
    }

    private void select(Player player, Material material) {
        if (material == null || QuickBuyItemDialog.isBlacklisted(plugin, material)) {
            QuickBuySounds.fail(player, plugin);
            Draft draft = draft(player);
            openCatalogue(player, draft.slot, draft.query, draft.page);
            return;
        }
        Draft draft = draft(player);
        draft.material = material;
        draft.enchants.clear();
        QuickBuySounds.click(player, plugin);
        if (QuickBuyItemDialog.isEnchantable(material)) {
            openEnchantments(player);
        } else {
            openAmount(player);
        }
    }

    private void openEnchantments(Player player) {
        Draft draft = draft(player);
        Material material = draft.material;
        if (material == null) {
            openCatalogue(player, draft.slot, draft.query, draft.page);
            return;
        }
        List<QuickBuyItemDialog.EnchantmentSpec> specs = QuickBuyItemDialog.getEnchantmentSpecs(plugin, material);
        if (specs.isEmpty()) {
            openAmount(player);
            return;
        }

        SimpleForm.Builder form = SimpleForm.builder()
                .title(text("QUICK_BUY.BEDROCK.ENCHANT_TITLE", "Choose Enchantments"))
                .content(text("QUICK_BUY.BEDROCK.ENCHANT_CONTENT", "Tap an enchantment to change its level.")
                        + "\n" + itemName(material));
        List<Runnable> actions = new ArrayList<>();
        for (QuickBuyItemDialog.EnchantmentSpec spec : specs) {
            QuickBuyItemDialog.EnchantmentSpec chosen = spec;
            form.button(enchantLabel(draft, chosen));
            actions.add(() -> cycleEnchant(player, chosen));
        }
        form.button(text("QUICK_BUY.BEDROCK.BUTTON_SKIP", "Skip enchantments"));
        actions.add(() -> {
            draft.enchants.clear();
            QuickBuySounds.click(player, plugin);
            openAmount(player);
        });
        if (draft.enchants.values().stream().anyMatch(level -> level != null && level > 0)) {
            form.button(text("QUICK_BUY.BEDROCK.BUTTON_CONFIRM", "Confirm enchantments"));
            actions.add(() -> {
                QuickBuySounds.click(player, plugin);
                openAmount(player);
            });
        }
        form.button(text("QUICK_BUY.BEDROCK.BUTTON_BACK", "Back"));
        actions.add(() -> {
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            openCatalogue(player, draft.slot, draft.query, draft.page);
        });
        bind(player, draft, form, actions, () -> openCatalogue(player, draft.slot, draft.query, draft.page));
        send(player, form.build());
    }

    private void cycleEnchant(Player player, QuickBuyItemDialog.EnchantmentSpec spec) {
        Draft draft = draft(player);
        Enchantment enchantment = spec.enchantment() != null
                ? spec.enchantment()
                : QuickBuyEntry.findEnchantment(spec.key());
        if (enchantment == null) {
            openEnchantments(player);
            return;
        }
        int current = draft.enchants.getOrDefault(enchantment, 0);
        int next = current >= spec.maxLevel() ? 0 : current + 1;
        if (next <= 0) {
            draft.enchants.remove(enchantment);
        } else {
            draft.enchants.put(enchantment, next);
        }
        QuickBuySounds.click(player, plugin);
        openEnchantments(player);
    }

    private void openAmount(Player player) {
        Draft draft = draft(player);
        Material material = draft.material;
        if (material == null || QuickBuyItemDialog.isBlacklisted(plugin, material)) {
            openCatalogue(player, draft.slot, draft.query, draft.page);
            return;
        }
        int max = maxStack(material);
        CustomForm.Builder form = CustomForm.builder()
                .title(text("QUICK_BUY.BEDROCK.AMOUNT_TITLE", "How many to buy?"))
                .label(text("QUICK_BUY.BEDROCK.BUTTON_ADD", "Add to Quick Buy") + "\n" + itemName(material))
                .input(text("QUICK_BUY.BEDROCK.AMOUNT_INPUT", "Amount (max {max})",
                        "{max}", String.valueOf(max)), "1", "1");
        int generation = draft.generation;
        form.validResultHandler(response -> {
            draft.generation++;
            String typed = response.asInput(1);
            schedule(player, () -> confirmAmount(player, typed));
        });
        form.closedResultHandler(() -> {
            if (draft.generation != generation) {
                return;
            }
            schedule(player, () -> {
                if (QuickBuyItemDialog.isEnchantable(material)) {
                    openEnchantments(player);
                } else {
                    openCatalogue(player, draft.slot, draft.query, draft.page);
                }
            });
        });
        send(player, form.build());
    }

    private void confirmAmount(Player player, String raw) {
        Draft draft = draft(player);
        Material material = draft.material;
        if (material == null || QuickBuyItemDialog.isBlacklisted(plugin, material)) {
            QuickBuySounds.fail(player, plugin);
            returnToShop(player);
            return;
        }
        int max = maxStack(material);
        int amount;
        try {
            if (raw == null || raw.isBlank()) {
                throw new NumberFormatException("blank");
            }
            amount = Integer.parseInt(raw.trim());
            if (amount < 1) {
                throw new NumberFormatException("below one");
            }
        } catch (NumberFormatException exception) {
            QuickBuySounds.fail(player, plugin);
            player.sendMessage(ColorUtils.toComponent("&c" + text(
                    "QUICK_BUY.BEDROCK.AMOUNT_INVALID", "Enter a whole number.")));
            openAmount(player);
            return;
        }
        amount = Math.min(amount, max);
        String serialized = QuickBuyEntry.serializeEnchantments(draft.enchants);
        plugin.getShopManager().setQuickBuyEntry(
                player.getUniqueId(),
                new QuickBuyEntry(draft.slot, material, amount, serialized, true));
        drafts.remove(player.getUniqueId());
        QuickBuySounds.play(player, plugin, QuickBuySounds.PIN);
        new QuickBuyMenu(plugin).open(player);
    }

    private void returnToShop(Player player) {
        drafts.remove(player.getUniqueId());
        QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
        new QuickBuyMenu(plugin).open(player);
    }

    private String itemLabel(Player player, Material material) {
        String name = itemName(material);
        WorthManager worth = plugin.getWorthManager();
        if (worth == null || !worth.isWorthLoreEnabled()) {
            return name;
        }
        if (player != null
                && plugin.getPlayerDataManager() != null
                && !worth.isWorthDisplayEnabledFor(player)) {
            return name;
        }
        String lore = worth.getWorthLoreLine(new ItemStack(material));
        if (lore == null || lore.isBlank()) {
            return name;
        }
        String plainLore = ColorUtils.strip(lore).trim();
        if (plainLore.isEmpty()) {
            return name;
        }
        return name + "\n" + plainLore;
    }

    private String enchantLabel(Draft draft, QuickBuyItemDialog.EnchantmentSpec spec) {
        Enchantment enchantment = spec.enchantment() != null
                ? spec.enchantment()
                : QuickBuyEntry.findEnchantment(spec.key());
        int level = enchantment == null ? 0 : draft.enchants.getOrDefault(enchantment, 0);
        if (level <= 0) {
            return text("QUICK_BUY.BEDROCK.ENCHANT_OFF", "{name}", "{name}", spec.displayName());
        }
        return text("QUICK_BUY.BEDROCK.ENCHANT_ON", "{name} {level}",
                "{name}", spec.displayName(),
                "{level}", QuickBuyItemDialog.toRoman(level));
    }

    private String itemName(Material material) {
        if (material == null) {
            return "";
        }
        WorthManager worth = plugin.getWorthManager();
        String raw = worth == null ? material.name() : worth.prettifyMaterial(material);
        String plain = ColorUtils.strip(raw).trim();
        return plain.isEmpty() ? material.name() : plain;
    }

    private static int maxStack(Material material) {
        try {
            return Math.max(1, material.getMaxStackSize());
        } catch (Throwable ignored) {
            return 64;
        }
    }

    private void bind(Player player, Draft draft, SimpleForm.Builder form, List<Runnable> actions, Runnable onClose) {
        int generation = draft.generation;
        form.validResultHandler(response -> {
            draft.generation++;
            int index = response.clickedButtonId();
            schedule(player, () -> runAction(actions, index));
        });
        form.closedResultHandler(() -> {
            if (draft.generation != generation) {
                return;
            }
            schedule(player, onClose);
        });
    }

    private static void runAction(List<Runnable> actions, int index) {
        if (index >= 0 && index < actions.size()) {
            actions.get(index).run();
        }
    }

    private Draft draft(Player player) {
        return drafts.computeIfAbsent(player.getUniqueId(), ignored -> new Draft());
    }

    private boolean accepts(Player player) {
        if (player == null || !enabled()) {
            return false;
        }
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Throwable ignored) {
            return false;
        }
    }

    private boolean enabled() {
        boolean configured = true;
        if (plugin.getConfigManager() != null && plugin.getConfigManager().getShop() != null) {
            configured = plugin.getConfigManager().getShop().getBoolean("QUICK-BUY.BEDROCK.ENABLED", true);
        }
        return configured && plugin.getServer().getPluginManager().isPluginEnabled("floodgate");
    }

    private int pageSize() {
        int configured = QuickBuyBedrockPages.DEFAULT_PAGE_SIZE;
        if (plugin.getConfigManager() != null && plugin.getConfigManager().getShop() != null) {
            configured = plugin.getConfigManager().getShop().getInt(
                    "QUICK-BUY.BEDROCK.PAGE-SIZE", QuickBuyBedrockPages.DEFAULT_PAGE_SIZE);
        }
        return QuickBuyBedrockPages.pageSize(configured);
    }

    private boolean send(Player player, Form form) {
        try {
            return FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
        } catch (Throwable throwable) {
            plugin.getLogger().warning("Could not send Quick Buy Bedrock form to " + player.getName()
                    + ": " + throwable.getMessage());
            return false;
        }
    }

    private void schedule(Player player, Runnable runnable) {
        plugin.getSpigotScheduler().runEntity(player, () -> {
            if (player.isOnline()) {
                runnable.run();
            }
        });
    }

    private void closeMenu(Player player) {
        try {
            if (player.getOpenInventory() == null) {
                return;
            }
            String type = player.getOpenInventory().getType().name();
            if (!"CRAFTING".equals(type) && !"CREATIVE".equals(type)) {
                player.closeInventory();
            }
        } catch (Throwable ignored) {
        }
    }

    private String text(String path, String fallback, String... placeholders) {
        String value = plugin.getLanguageManager().text(path, null, fallback, placeholders);
        return ColorUtils.strip(value == null ? fallback : value);
    }

    private static final class Draft {
        private int slot;
        private String query = "";
        private int page;
        private int generation;
        private Material material;
        private final Map<Enchantment, Integer> enchants = new LinkedHashMap<>();
    }
}
