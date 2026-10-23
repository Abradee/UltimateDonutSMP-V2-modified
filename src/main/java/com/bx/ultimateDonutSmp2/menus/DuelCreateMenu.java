package com.bx.ultimateDonutSmp2.menus;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.DuelManager;
import com.bx.ultimateDonutSmp2.models.DuelMapSelection;
import com.bx.ultimateDonutSmp2.models.DuelPrivacyMode;
import com.bx.ultimateDonutSmp2.utils.ItemUtils;
import com.bx.ultimateDonutSmp2.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DuelCreateMenu extends BaseMenu {

    private final UUID targetUuid;
    private final DuelPrivacyMode privacyMode;

    public DuelCreateMenu(UltimateDonutSmp2 plugin, UUID targetUuid) {
        this(plugin, targetUuid, DuelPrivacyMode.INVITE_ONLY);
    }

    public DuelCreateMenu(UltimateDonutSmp2 plugin, UUID targetUuid, DuelPrivacyMode privacyMode) {
        super(plugin, plugin.getDuelManager().getCreateTitle(Bukkit.getPlayer(targetUuid)), plugin.getDuelManager().getCreateSize());
        this.targetUuid = targetUuid;
        this.privacyMode = privacyMode == null ? DuelPrivacyMode.INVITE_ONLY : privacyMode;
    }

    public static int resolvePrivacySlot(int size) {
        return (size - 9) + 5;
    }

    public DuelPrivacyMode getPrivacyMode() {
        return privacyMode;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    @Override
    public void build(Player player) {
        clear();
        fill("DUEL-CREATE-MENU", Material.GRAY_STAINED_GLASS_PANE);

        Player target = Bukkit.getPlayer(targetUuid);
        if (target == null) {
            set(13, ItemUtils.createItem(
                    Material.BARRIER,
                    plugin.getDuelManager().getGuiText("CREATE.ITEMS.TARGET_OFFLINE.NAME", "&ctarget offline"),
                    plugin.getDuelManager().getGuiTextList("CREATE.ITEMS.TARGET_OFFLINE.LORE",
                            List.of("&7this player is no longer online."))
            ));
            set(inventory.getSize() - 1, ItemUtils.createItem(
                    Material.BARRIER,
                    plugin.getDuelManager().getGuiText("CREATE.ITEMS.CLOSE.NAME", "&cclose")
            ));
            return;
        }

        String targetName = plugin.getHideManager() != null ? plugin.getHideManager().publicName(target) : target.getName();
        List<DuelManager.DuelMapOption> options = plugin.getDuelManager().getSelectableMapOptions(false);
        int[] slots = contentSlots();
        for (int i = 0; i < Math.min(options.size(), slots.length); i++) {
            DuelManager.DuelMapOption option = options.get(i);
            set(slots[i], ItemUtils.createItem(
                    materialFor(option.selection()),
                    plugin.getDuelManager().getGuiText("CREATE.ITEMS.MAP_OPTION.NAME",
                            "&a{map}", "{map}", option.displayName()),
                    plugin.getDuelManager().getGuiTextList("CREATE.ITEMS.MAP_OPTION.LORE",
                            List.of(
                                    "&7privacy: &f{privacy}",
                                    "&7target: &f{target}",
                                    "&7{description}",
                                    "&eclick to send challenge."
                            ),
                            "{privacy}", privacyMode.displayName(),
                            "{target}", targetName,
                            "{description}", option.description())
            ));
        }

        if (options.isEmpty()) {
            set(13, ItemUtils.createItem(
                    Material.BARRIER,
                    plugin.getDuelManager().getGuiText("CREATE.ITEMS.NO_MAPS.NAME", "&cno duel maps available"),
                    plugin.getDuelManager().getGuiTextList("CREATE.ITEMS.NO_MAPS.LORE",
                            List.of("&7configure arenas or enable random biomes."))
            ));
        }

        int lastRow = inventory.getSize() - 9;
        set(lastRow + 3, ItemUtils.createPlayerHead(
                target,
                plugin.getDuelManager().getGuiText("CREATE.ITEMS.TARGET_HEAD.NAME",
                        "&etarget: &f{target}", "{target}", targetName),
                plugin.getDuelManager().getGuiTextList("CREATE.ITEMS.TARGET_HEAD.LORE",
                        List.of("&7choose a map to send a duel request."))
        ));
        String privacyLore = privacyMode == DuelPrivacyMode.FRIENDS_ONLY
                ? plugin.getDuelManager().getGuiText("CREATE.ITEMS.PRIVACY_BUTTON.LORE_FRIENDS",
                        "&7only same-team members can accept this duel.")
                : plugin.getDuelManager().getGuiText("CREATE.ITEMS.PRIVACY_BUTTON.LORE_INVITE",
                        "&7direct invite duel.");
        set(resolvePrivacySlot(inventory.getSize()), ItemUtils.createItem(
                privacyMode == DuelPrivacyMode.FRIENDS_ONLY ? Material.OAK_SIGN : Material.PAPER,
                plugin.getDuelManager().getGuiText("CREATE.ITEMS.PRIVACY_BUTTON.NAME",
                        "&bprivacy: &f{privacy}", "{privacy}", privacyMode.displayName()),
                List.of(privacyLore)
        ));
        set(lastRow + 8, ItemUtils.createItem(
                Material.BARRIER,
                plugin.getDuelManager().getGuiText("CREATE.ITEMS.CLOSE.NAME", "&cclose")
        ));
    }

    @Override
    public void handleClick(int slot, Player player) {
        Player target = Bukkit.getPlayer(targetUuid);
        if (target == null) {
            player.closeInventory();
            return;
        }

        List<DuelManager.DuelMapOption> options = plugin.getDuelManager().getSelectableMapOptions(false);
        int index = optionIndex(slot);
        if (index >= 0 && index < options.size()) {
            SoundUtils.play(player, plugin.getConfigManager().getSound("DUELS.CLICK"));
            plugin.getDuelManager().sendChallenge(player, target, options.get(index).selection(), privacyMode);
            player.closeInventory();
            return;
        }

        if (slot == resolvePrivacySlot(inventory.getSize())) {
            DuelPrivacyMode nextMode = privacyMode == DuelPrivacyMode.FRIENDS_ONLY
                    ? DuelPrivacyMode.INVITE_ONLY
                    : DuelPrivacyMode.FRIENDS_ONLY;
            SoundUtils.play(player, plugin.getConfigManager().getSound("DUELS.CLICK"));
            new DuelCreateMenu(plugin, targetUuid, nextMode).open(player);
            return;
        }

        if (slot == inventory.getSize() - 1) {
            player.closeInventory();
        }
    }

    private int[] contentSlots() {
        int rows = inventory.getSize() / 9;
        List<Integer> slots = new ArrayList<>();
        for (int row = 0; row < rows - 1; row++) {
            for (int column = 0; column < 9; column++) {
                slots.add(row * 9 + column);
            }
        }
        return slots.stream().mapToInt(Integer::intValue).toArray();
    }

    private int optionIndex(int clickedSlot) {
        int[] slots = contentSlots();
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == clickedSlot) {
                return i;
            }
        }
        return -1;
    }

    private Material materialFor(DuelMapSelection selection) {
        return switch (selection.type()) {
            case STATIC_ARENA -> Material.IRON_SWORD;
            case RANDOM_STATIC -> Material.COMPASS;
            case BIOME -> Material.GRASS_BLOCK;
            case RANDOM_BIOME -> Material.MAP;
        };
    }
}
