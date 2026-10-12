package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog;
import com.bx.ultimateDonutSmp2.managers.QuickBuyBedrockManager;
import com.bx.ultimateDonutSmp2.managers.QuickBuySearchPrompt;
import com.bx.ultimateDonutSmp2.models.QuickBuyEntry;
import com.bx.ultimateDonutSmp2.utils.BedrockPlayers;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SignInputUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuickBuyItemSelectMenu extends BaseMenu {

    private static final int ITEMS_PER_PAGE = 45;
    private static final int PREV_SLOT = 45;
    private static final int CANCEL_SLOT = 49;
    private static final int SEARCH_SLOT = 48;
    private static final int NEXT_SLOT = 53;

    private final int targetSlot;
    private final int page;
    private final String query;
    private final List<Material> matchedMaterials = new ArrayList<>();
    private final Map<Integer, Material> slotToMaterial = new HashMap<>();

    public QuickBuyItemSelectMenu(UltimateDonutSmp2 plugin, int targetSlot, int page, String query) {
        super(plugin, "&8Choose Item", 54);
        this.targetSlot = targetSlot;
        this.page = Math.max(0, page);
        this.query = query == null ? "" : query;
    }

    @Override
    public void build(Player player) {
        clear();
        slotToMaterial.clear();
        matchedMaterials.clear();

        matchedMaterials.addAll(QuickBuyItemDialog.getSelectableMaterials(plugin, query));

        int totalPages = Math.max(1, (int) Math.ceil((double) matchedMaterials.size() / ITEMS_PER_PAGE));
        int effectivePage = Math.min(page, totalPages - 1);
        int startIndex = effectivePage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, matchedMaterials.size());

        for (int i = startIndex; i < endIndex; i++) {
            int slot = i - startIndex;
            Material mat = matchedMaterials.get(i);
            slotToMaterial.put(slot, mat);
            set(slot, ItemUtils.createItem(
                    mat,
                    "&f" + plugin.getWorthManager().prettifyMaterial(mat),
                    List.of("&eClick to select this item")
            ));
        }

        fillBottomRow(effectivePage, totalPages);
    }

    private void fillBottomRow(int currentPage, int totalPages) {
        for (int slot = 45; slot < 54; slot++) {
            set(slot, ItemUtils.createPlaceholder(Material.GRAY_STAINED_GLASS_PANE));
        }

        if (currentPage > 0) {
            set(PREV_SLOT, ItemUtils.createItem(Material.ARROW, "&aPrevious Page", List.of("&7Go to page " + currentPage)));
        }

        List<String> searchLore = new ArrayList<>();
        searchLore.add("&7Click to search items");
        if (!query.isBlank()) {
            searchLore.add("&7Current: &e" + query);
            searchLore.add("&8Right-click to clear");
        }
        set(SEARCH_SLOT, ItemUtils.createItem(Material.OAK_SIGN, "&fSearch", searchLore));

        set(CANCEL_SLOT, ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, "&cCancel", List.of("&7Return to Quick Buy")));

        if (currentPage < totalPages - 1) {
            set(NEXT_SLOT, ItemUtils.createItem(Material.ARROW, "&aNext Page", List.of("&7Go to page " + (currentPage + 2))));
        }
    }

    @Override
    public void handleClick(int slot, Player player, ClickType clickType) {
        if (slot == CANCEL_SLOT) {
            QuickBuySounds.play(player, plugin, QuickBuySounds.CANCEL);
            new QuickBuyMenu(plugin).open(player);
            return;
        }

        int totalPages = Math.max(1, (int) Math.ceil((double) matchedMaterials.size() / ITEMS_PER_PAGE));
        if (slot == PREV_SLOT && page > 0) {
            QuickBuySounds.pageTurn(player, plugin);
            new QuickBuyItemSelectMenu(plugin, targetSlot, page - 1, query).open(player);
            return;
        }

        if (slot == NEXT_SLOT && page < totalPages - 1) {
            QuickBuySounds.pageTurn(player, plugin);
            new QuickBuyItemSelectMenu(plugin, targetSlot, page + 1, query).open(player);
            return;
        }

        if (slot == SEARCH_SLOT) {
            if (clickType.isRightClick() && !query.isBlank()) {
                QuickBuySounds.play(player, plugin, QuickBuySounds.SEARCH);
                new QuickBuyItemSelectMenu(plugin, targetSlot, 0, "").open(player);
                return;
            }
            click(player);
            if (BedrockPlayers.isBedrock(player)) {
                QuickBuyBedrockManager bedrock = plugin.getQuickBuyBedrockManager();
                if (bedrock == null || !bedrock.openItemSearch(player, targetSlot, query)) {
                    QuickBuySearchPrompt prompt = plugin.getQuickBuySearchPrompt();
                    if (prompt != null) {
                        prompt.askPicker(player, targetSlot, query);
                    }
                }
                return;
            }
            if (DialogSupport.isAvailable() && plugin.getDialogManager() != null) {
                QuickBuyItemDialog dialog = plugin.getDialogManager().getScreen(QuickBuyItemDialog.class);
                if (dialog != null) {
                    player.closeInventory();
                    dialog.openAuctionSearch(player, query, "select_" + targetSlot);
                    return;
                }
            }
            SignInputUtil.open(plugin, player, List.of("", "↑↑↑↑↑", "Search", ""), 0, input -> {
                String q = input == null || input.isBlank() ? "" : input.trim();
                QuickBuySounds.play(player, plugin, QuickBuySounds.SEARCH);
                new QuickBuyItemSelectMenu(plugin, targetSlot, 0, q).open(player);
            });
            return;
        }

        Material selected = slotToMaterial.get(slot);
        if (selected != null && !QuickBuyItemDialog.isBlacklisted(plugin, selected)) {
            click(player);
            if (!BedrockPlayers.isBedrock(player)
                    && DialogSupport.isAvailable() && plugin.getDialogManager() != null
                    && QuickBuyItemDialog.isEnchantable(selected)) {
                QuickBuyItemDialog dialog = plugin.getDialogManager().getScreen(QuickBuyItemDialog.class);
                if (dialog != null) {
                    player.closeInventory();
                    dialog.openEnchantments(player, targetSlot, selected);
                    return;
                }
            }
            new QuickBuyItemAmountMenu(plugin, targetSlot, selected, 1).open(player);
        }
    }

    private void click(Player player) {
        QuickBuySounds.click(player, plugin);
    }
}
