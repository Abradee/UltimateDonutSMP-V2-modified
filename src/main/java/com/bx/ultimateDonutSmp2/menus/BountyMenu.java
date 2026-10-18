package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.dialogs.screens.BountyDialog;
import com.bx.ultimateDonutSmp2.managers.BedrockChatPrompt;
import com.bx.ultimateDonutSmp2.managers.TablistManager;
import com.bx.ultimateDonutSmp2.models.Bounty;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.NumberUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Bounty GUI matching Design/Bounty specifications.
 */
public class BountyMenu extends BaseMenu {

    public static final int PREVIOUS_PAGE_SLOT = 45;
    public static final int SORT_SLOT = 48;
    public static final int REFRESH_SLOT = 49;
    public static final int SEARCH_SLOT = 50;
    public static final int NEXT_PAGE_SLOT = 53;
    public static final int MAX_ITEMS_PER_PAGE = 45;

    private final List<Bounty> displayedBounties = new ArrayList<>();
    private int page;
    private int totalPages = 1;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private boolean sortByRecent = true;
    private String searchQuery;

    public BountyMenu(UltimateDonutSmp2 plugin) {
        this(plugin, 0, null, true);
    }

    public BountyMenu(UltimateDonutSmp2 plugin, int page, String searchQuery, boolean sortByRecent) {
        super(
                plugin,
                formatTitle(plugin, page),
                plugin.getConfigManager().getMenus().getInt("BOUNTIES-MENU.SIZE", 54)
        );
        this.page = Math.max(0, page);
        this.searchQuery = searchQuery;
        this.sortByRecent = sortByRecent;
    }

    private static String formatTitle(UltimateDonutSmp2 plugin, int page) {
        String template = plugin.getConfigManager().getMenus().getString("BOUNTIES-MENU.TITLE", "&8Bounties (Page {page})");
        return template.replace("{page}", String.valueOf(page + 1));
    }

    @Override
    public void build(Player player) {
        clear();
        displayedBounties.clear();

        FileConfiguration menus = plugin.getConfigManager().getMenus();
        int maxItems = menus.getInt("BOUNTIES-MENU.MAX-ITEMS-PER-PAGE", MAX_ITEMS_PER_PAGE);

        List<Bounty> allBounties = new ArrayList<>(plugin.getBountyManager().getAllBounties());
        if (searchQuery != null && !searchQuery.isBlank()) {
            String lowerQuery = searchQuery.toLowerCase(java.util.Locale.ROOT);
            allBounties = allBounties.stream()
                    .filter(bounty -> {
                        String name = plugin.getBountyManager().getDisplayName(bounty.getTargetUuid());
                        return name.toLowerCase(java.util.Locale.ROOT).contains(lowerQuery);
                    })
                    .toList();
        }

        Comparator<Bounty> comparator;
        if (sortByRecent) {
            comparator = Comparator.comparingLong(Bounty::getTimestamp).reversed();
        } else {
            comparator = Comparator.comparingDouble(Bounty::getAmount).reversed();
        }
        allBounties.sort(comparator.thenComparing(bounty ->
                plugin.getBountyManager().getDisplayName(bounty.getTargetUuid()), String.CASE_INSENSITIVE_ORDER));

        totalPages = Math.max(1, (int) Math.ceil(allBounties.size() / (double) maxItems));
        if (page >= totalPages) {
            page = totalPages - 1;
        }
        if (page < 0) {
            page = 0;
        }

        int startIndex = page * maxItems;
        int endIndex = Math.min(startIndex + maxItems, allBounties.size());
        hasPreviousPage = hasPreviousPage(page);
        hasNextPage = hasNextPage(page, allBounties.size(), maxItems);

        for (int index = startIndex; index < endIndex; index++) {
            Bounty bounty = allBounties.get(index);
            int slot = index - startIndex;
            set(slot, createBountyItem(menus, bounty));
            displayedBounties.add(bounty);
        }

        buildNavigation(menus);
    }

    @Override
    public void handleClick(int slot, Player player, ClickType clickType) {
        FileConfiguration menus = plugin.getConfigManager().getMenus();
        int prevSlot = menus.getInt("BOUNTIES-MENU.PREVIOUS-PAGE-BUTTON.SLOT", PREVIOUS_PAGE_SLOT);
        int sortSlot = menus.getInt("BOUNTIES-MENU.SORT-BUTTON.SLOT", SORT_SLOT);
        int refreshSlot = menus.getInt("BOUNTIES-MENU.REFRESH-BUTTON.SLOT", REFRESH_SLOT);
        int searchSlot = menus.getInt("BOUNTIES-MENU.SEARCH-BUTTON.SLOT", SEARCH_SLOT);
        int nextSlot = menus.getInt("BOUNTIES-MENU.NEXT-PAGE-BUTTON.SLOT", NEXT_PAGE_SLOT);

        if (slot == prevSlot && hasPreviousPage) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.PAGE-TURN"));
            new BountyMenu(plugin, page - 1, searchQuery, sortByRecent).open(player);
            return;
        }

        if (slot == sortSlot) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
            sortByRecent = !sortByRecent;
            page = 0;
            build(player);
            return;
        }

        if (slot == refreshSlot) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
            build(player);
            return;
        }

        if (slot == searchSlot) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
            if (clickType != null && clickType.isRightClick() && searchQuery != null && !searchQuery.isBlank()) {
                searchQuery = null;
                page = 0;
                build(player);
                return;
            }

            Consumer<String> apply = text -> {
                if (text == null || text.isBlank() || text.equalsIgnoreCase("cancel")) {
                    new BountyMenu(plugin, page, searchQuery, sortByRecent).open(player);
                } else {
                    new BountyMenu(plugin, 0, text.trim(), sortByRecent).open(player);
                }
            };
            if (BedrockChatPrompt.askIfBedrock(
                    plugin,
                    player,
                    plugin.getConfigManager().getMessageOrDefault(
                            "BOUNTY.BEDROCK_SEARCH_PROMPT",
                            "&7Type a player name in chat. Type &ccancel &7to go back."
                    ),
                    apply
            )) {
                return;
            }

            if (DialogSupport.isAvailable() && plugin.getDialogManager() != null) {
                BountyDialog dialog = plugin.getDialogManager().getScreen(BountyDialog.class);
                if (dialog != null && dialog.openSearch(player, searchQuery, sortByRecent, page)) {
                    return;
                }
            }

            org.bukkit.configuration.ConfigurationSection config = plugin.getConfigManager().getMenus()
                    .getConfigurationSection("BOUNTIES-MENU.SEARCH_SIGN");
            com.bx.ultimateDonutSmp2.utils.SignInputUtil.openFromConfig(plugin, player, config, apply);
            return;
        }

        if (slot == nextSlot && hasNextPage) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.PAGE-TURN"));
            new BountyMenu(plugin, page + 1, searchQuery, sortByRecent).open(player);
            return;
        }

        if (slot < 0 || slot >= displayedBounties.size()) {
            return;
        }

        SoundUtils.play(player, plugin.getConfigManager().getSound("MENUS.BUTTON-CLICK"));
        Bounty bounty = displayedBounties.get(slot);
        String msg = plugin.getConfigManager().getMessage("BOUNTY.PLAYER-HAS-BOUNTY",
                "{player}", plugin.getBountyManager().getDisplayName(bounty.getTargetUuid()),
                "{amount}", NumberUtils.format(bounty.getAmount()),
                "{amount_formatted}", plugin.getCurrencyManager().formatMoney(bounty.getAmount()));
        player.sendMessage(ColorUtils.toComponent(msg));
    }

    private ItemStack createBountyItem(FileConfiguration menus, Bounty bounty) {
        String playerName = plugin.getBountyManager().getDisplayName(bounty.getTargetUuid());
        String displayName = menus.getString("BOUNTIES-MENU.BOUNTY-BUTTON.NAME", "&#00fc88{player}")
                .replace("{player}", playerName);

        String nicePrice = NumberUtils.formatNice(bounty.getAmount());
        String formattedPrice = "$" + nicePrice;

        List<String> rawLore = menus.getStringList("BOUNTIES-MENU.BOUNTY-BUTTON.LORE");
        if (rawLore.isEmpty()) {
            rawLore = List.of("&fBounty: &7${price}");
        }

        List<String> lore = rawLore.stream()
                .map(line -> line.replace("{player}", playerName)
                        .replace("{price}", nicePrice)
                        .replace("{price_formatted}", formattedPrice)
                        .replace("{price_raw}", NumberUtils.format(bounty.getAmount()))
                        .replace("{amount}", NumberUtils.format(bounty.getAmount())))
                .toList();

        Material material = ItemUtils.parseMaterial(
                menus.getString("BOUNTIES-MENU.BOUNTY-BUTTON.MATERIAL", "PLAYER_HEAD")
        );

        if (material != Material.PLAYER_HEAD) {
            return ItemUtils.createItem(material, displayName, lore);
        }

        ItemStack item = ItemUtils.createItem(material, displayName, lore);
        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            return item;
        }

        UUID targetUuid = bounty.getTargetUuid();
        meta.setOwningPlayer(resolveOfflinePlayer(targetUuid));

        if (plugin.getPlayerSkinManager() != null) {
            TablistManager.SkinTexture texture = plugin.getPlayerSkinManager().resolveSkinTexture(targetUuid, playerName);
            if (texture != null && texture.isValid()) {
                ItemUtils.applyTextureToSkullMeta(meta, texture.value());
            } else {
                plugin.getPlayerSkinManager().resolveSkinTextureAsync(targetUuid, playerName, resolved -> {
                    if (resolved != null && resolved.isValid()) {
                        updateBountySkullIfOpen(targetUuid, resolved.value());
                    }
                });
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    private void updateBountySkullIfOpen(UUID targetUuid, String textureValue) {
        if (textureValue == null || textureValue.isBlank() || targetUuid == null) {
            return;
        }
        for (int i = 0; i < displayedBounties.size(); i++) {
            Bounty bounty = displayedBounties.get(i);
            if (targetUuid.equals(bounty.getTargetUuid())) {
                ItemStack item = getInventory().getItem(i);
                if (item != null && item.getItemMeta() instanceof SkullMeta meta) {
                    ItemUtils.applyTextureToSkullMeta(meta, textureValue);
                    item.setItemMeta(meta);
                    getInventory().setItem(i, item);
                }
                break;
            }
        }
    }

    private void buildNavigation(FileConfiguration menus) {
        int prevSlot = menus.getInt("BOUNTIES-MENU.PREVIOUS-PAGE-BUTTON.SLOT", PREVIOUS_PAGE_SLOT);
        int sortSlot = menus.getInt("BOUNTIES-MENU.SORT-BUTTON.SLOT", SORT_SLOT);
        int refreshSlot = menus.getInt("BOUNTIES-MENU.REFRESH-BUTTON.SLOT", REFRESH_SLOT);
        int searchSlot = menus.getInt("BOUNTIES-MENU.SEARCH-BUTTON.SLOT", SEARCH_SLOT);
        int nextSlot = menus.getInt("BOUNTIES-MENU.NEXT-PAGE-BUTTON.SLOT", NEXT_PAGE_SLOT);

        // Slot 45: Previous page (only if page > 0)
        if (hasPreviousPage) {
            Material prevMaterial = ItemUtils.parseMaterial(menus.getString("BOUNTIES-MENU.PREVIOUS-PAGE-BUTTON.MATERIAL", "ARROW"));
            String prevName = menus.getString("BOUNTIES-MENU.PREVIOUS-PAGE-BUTTON.NAME", "&fPrevious page");
            List<String> prevLore = menus.getStringList("BOUNTIES-MENU.PREVIOUS-PAGE-BUTTON.LORE");
            if (prevLore.isEmpty()) {
                prevLore = List.of("&o&7Click to view previous page");
            }
            set(prevSlot, ItemUtils.createItem(prevMaterial, prevName, prevLore));
        }

        // Slot 48: Sort (Hopper) matching 1.png and 2.png
        Material sortMaterial = ItemUtils.parseMaterial(menus.getString("BOUNTIES-MENU.SORT-BUTTON.MATERIAL", "HOPPER"));
        String sortName = menus.getString("BOUNTIES-MENU.SORT-BUTTON.NAME", "&fSort");
        List<String> sortLore;
        if (sortByRecent) {
            sortLore = menus.getStringList("BOUNTIES-MENU.SORT-BUTTON.LORE-AMOUNT");
            if (sortLore.isEmpty()) {
                sortLore = List.of("&fClick to sort (Amount)");
            }
        } else {
            sortLore = menus.getStringList("BOUNTIES-MENU.SORT-BUTTON.LORE-RECENT");
            if (sortLore.isEmpty()) {
                sortLore = List.of("&fClick to sort (Recently Set)");
            }
        }
        set(sortSlot, ItemUtils.createItem(sortMaterial, sortName, sortLore));

        // Slot 49: Refresh / Bounties info (Skeleton Skull) matching 3.png
        Material refreshMaterial = ItemUtils.parseMaterial(menus.getString("BOUNTIES-MENU.REFRESH-BUTTON.MATERIAL", "SKELETON_SKULL"));
        String refreshName = menus.getString("BOUNTIES-MENU.REFRESH-BUTTON.NAME", "&fBounties");
        List<String> refreshLore = menus.getStringList("BOUNTIES-MENU.REFRESH-BUTTON.LORE");
        if (refreshLore.isEmpty()) {
            refreshLore = List.of(
                    "&fClick to refresh",
                    "",
                    "&7Set a bounty using this:",
                    "&7/bounty add (player) (amount)"
            );
        }
        set(refreshSlot, ItemUtils.createItem(refreshMaterial, refreshName, refreshLore));

        // Slot 50: Search (Oak Sign) matching 4.png
        Material searchMaterial = ItemUtils.parseMaterial(menus.getString("BOUNTIES-MENU.SEARCH-BUTTON.MATERIAL", "OAK_SIGN"));
        String searchName = menus.getString("BOUNTIES-MENU.SEARCH-BUTTON.NAME", "&fSearch");
        List<String> searchLore = new ArrayList<>();
        List<String> rawSearchLore = menus.getStringList("BOUNTIES-MENU.SEARCH-BUTTON.LORE");
        if (rawSearchLore.isEmpty()) {
            searchLore.add("&7Click to search");
        } else {
            searchLore.addAll(rawSearchLore);
        }
        if (searchQuery != null && !searchQuery.isBlank()) {
            searchLore.add("&7Current: &e" + searchQuery);
            searchLore.add("&8Right-click to clear");
        }
        set(searchSlot, ItemUtils.createItem(searchMaterial, searchName, searchLore));

        // Slot 53: Next page (Arrow) - only appears if there is a next page
        if (hasNextPage) {
            Material nextMaterial = ItemUtils.parseMaterial(menus.getString("BOUNTIES-MENU.NEXT-PAGE-BUTTON.MATERIAL", "ARROW"));
            String nextName = menus.getString("BOUNTIES-MENU.NEXT-PAGE-BUTTON.NAME", "&fNext page");
            List<String> nextLore = menus.getStringList("BOUNTIES-MENU.NEXT-PAGE-BUTTON.LORE");
            if (nextLore.isEmpty()) {
                nextLore = List.of("&o&7Click to view next page");
            }
            set(nextSlot, ItemUtils.createItem(nextMaterial, nextName, nextLore));
        }
    }

    private OfflinePlayer resolveOfflinePlayer(UUID targetUuid) {
        Player online = Bukkit.getPlayer(targetUuid);
        if (online != null) {
            return online;
        }
        return Bukkit.getOfflinePlayer(targetUuid);
    }

    public int getPage() {
        return page;
    }

    public boolean isSortByRecent() {
        return sortByRecent;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public List<Bounty> getDisplayedBounties() {
        return displayedBounties;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public static boolean hasNextPage(int page, int totalItems, int maxItems) {
        if (maxItems <= 0) {
            return false;
        }
        int startIndex = page * maxItems;
        int endIndex = Math.min(startIndex + maxItems, totalItems);
        return endIndex < totalItems;
    }

    public static boolean hasPreviousPage(int page) {
        return page > 0;
    }
}
