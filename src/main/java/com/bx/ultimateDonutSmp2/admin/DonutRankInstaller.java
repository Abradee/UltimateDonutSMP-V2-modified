package com.bx.ultimateDonutSmp2.admin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.WeightNode;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Writes the built-in Donut ranks into LuckPerms: the permission nodes this plugin already
 * checks, plus a prefix so the rank shows up in the tab and through {@code %luckperms_prefix%}.
 */
public final class DonutRankInstaller {

    public void installGroups() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        for (DonutRankSetup.Rank rank : DonutRankSetup.ranks()) {
            Group group = loadOrCreate(luckPerms, rank.group());
            group.data().clear(node -> replaceable(node));
            for (String permission : rank.permissions()) {
                group.data().add(PermissionNode.builder(permission).build());
            }
            group.data().add(WeightNode.builder(rank.weight()).build());
            if (rank.parent() != null) {
                group.data().add(InheritanceNode.builder(rank.parent()).build());
            }
            luckPerms.getGroupManager().saveGroup(group).join();
        }
    }

    public int deleteGroups() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        int removed = 0;
        List<DonutRankSetup.Rank> ranks = new ArrayList<>(DonutRankSetup.ranks());
        Collections.reverse(ranks);
        for (DonutRankSetup.Rank rank : ranks) {
            Group group = luckPerms.getGroupManager().getGroup(rank.group());
            if (group == null) {
                group = luckPerms.getGroupManager().loadGroup(rank.group()).join().orElse(null);
            }
            if (group == null) {
                continue;
            }
            luckPerms.getGroupManager().deleteGroup(group).join();
            removed++;
        }
        return removed;
    }

    public boolean groupsInstalled() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        for (DonutRankSetup.Rank rank : DonutRankSetup.ranks()) {
            if (luckPerms.getGroupManager().getGroup(rank.group()) != null) {
                return true;
            }
        }
        return false;
    }

    /** Drops +, ++, and +++ prefixes from the built-in groups. The tab already draws that badge. */
    public int clearBuiltInBadgePrefixes() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        int cleared = 0;
        for (DonutRankSetup.Rank rank : DonutRankSetup.ranks()) {
            Group group = luckPerms.getGroupManager().getGroup(rank.group());
            if (group == null) {
                group = luckPerms.getGroupManager().loadGroup(rank.group()).join().orElse(null);
            }
            if (group == null) {
                continue;
            }
            boolean removed = group.data().toCollection().stream().anyMatch(node -> node instanceof PrefixNode prefix
                    && DonutRankSetup.isBuiltInBadgePrefix(prefix.getMetaValue()));
            if (removed) {
                group.data().clear(node -> node instanceof PrefixNode prefix
                        && DonutRankSetup.isBuiltInBadgePrefix(prefix.getMetaValue()));
                luckPerms.getGroupManager().saveGroup(group).join();
                cleared++;
            }
        }
        return cleared;
    }

    public String give(String playerName, DonutRankSetup.Rank rank) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Type a player name. Example: Steve");
        }
        if (rank == null) {
            throw new IllegalArgumentException("Pick +, ++, or +++.");
        }
        LuckPerms luckPerms = LuckPermsProvider.get();
        String name = playerName.trim();
        UUID uuid = null;
        var online = Bukkit.getPlayerExact(name);
        if (online != null) {
            uuid = online.getUniqueId();
        } else {
            uuid = luckPerms.getUserManager().lookupUniqueId(name).join();
        }
        if (uuid == null) {
            throw new IllegalArgumentException("LuckPerms does not know &f" + name + "&c. They need to join once.");
        }
        if (luckPerms.getGroupManager().getGroup(rank.group()) == null) {
            throw new IllegalArgumentException("Install the rank groups first, then give one to a player.");
        }
        User user = luckPerms.getUserManager().loadUser(uuid).join();
        if (user == null) {
            throw new IllegalArgumentException("Could not load &f" + name + "&c from LuckPerms.");
        }
        user.data().clear(node -> node instanceof InheritanceNode inheritance
                && DonutRankSetup.isRankGroup(inheritance.getGroupName()));
        user.data().add(InheritanceNode.builder(rank.group()).build());
        luckPerms.getUserManager().saveUser(user).join();
        return user.getUsername() == null || user.getUsername().isBlank() ? name : user.getUsername();
    }

    public String remove(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Type a player name. Example: Steve");
        }
        LuckPerms luckPerms = LuckPermsProvider.get();
        String name = playerName.trim();
        UUID uuid;
        var online = Bukkit.getPlayerExact(name);
        if (online != null) {
            uuid = online.getUniqueId();
        } else {
            uuid = luckPerms.getUserManager().lookupUniqueId(name).join();
        }
        if (uuid == null) {
            throw new IllegalArgumentException("LuckPerms does not know &f" + name + "&c. They need to join once.");
        }
        User user = luckPerms.getUserManager().loadUser(uuid).join();
        if (user == null) {
            throw new IllegalArgumentException("Could not load &f" + name + "&c from LuckPerms.");
        }
        user.data().clear(node -> node instanceof InheritanceNode inheritance
                && DonutRankSetup.isRankGroup(inheritance.getGroupName()));
        luckPerms.getUserManager().saveUser(user).join();
        return user.getUsername() == null || user.getUsername().isBlank() ? name : user.getUsername();
    }

    /** Sets a prefix on a group the server already uses, or creates that group. */
    public boolean setCustomPrefix(String groupName, String prefix) {
        String group = DonutRankSetup.normalizeGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Group name can use letters, numbers, . _ and -. Example: admin");
        }
        if (DonutRankSetup.isRankGroup(group)) {
            throw new IllegalArgumentException("donutplus, donutplusplus, and donutplusplusplus already show +, ++, and +++. A prefix is only for your own LuckPerms group.");
        }
        String formatted = DonutRankSetup.prefixWithSpace(prefix);
        if (formatted == null) {
            throw new IllegalArgumentException("Type a prefix. Example: &cAdmin or <#00A4FC>+");
        }
        LuckPerms luckPerms = LuckPermsProvider.get();
        Group loaded = luckPerms.getGroupManager().getGroup(group);
        boolean created = false;
        if (loaded == null) {
            Optional<Group> stored = luckPerms.getGroupManager().loadGroup(group).join();
            if (stored.isPresent()) {
                loaded = stored.get();
            } else {
                loaded = luckPerms.getGroupManager().createAndLoadGroup(group).join();
                created = true;
            }
        }
        loaded.data().clear(node -> node instanceof PrefixNode);
        loaded.data().add(PrefixNode.builder(formatted, 40).build());
        luckPerms.getGroupManager().saveGroup(loaded).join();
        return created;
    }

    public void clearCustomPrefix(String groupName) {
        String group = DonutRankSetup.normalizeGroup(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Group name can use letters, numbers, . _ and -. Example: admin");
        }
        if (DonutRankSetup.isRankGroup(group)) {
            throw new IllegalArgumentException("donutplus, donutplusplus, and donutplusplusplus already show +, ++, and +++. A prefix is only for your own LuckPerms group.");
        }
        LuckPerms luckPerms = LuckPermsProvider.get();
        Group loaded = luckPerms.getGroupManager().getGroup(group);
        if (loaded == null) {
            loaded = luckPerms.getGroupManager().loadGroup(group).join().orElse(null);
        }
        if (loaded == null) {
            throw new IllegalArgumentException("LuckPerms has no group named &f" + group + "&c.");
        }
        loaded.data().clear(node -> node instanceof PrefixNode);
        luckPerms.getGroupManager().saveGroup(loaded).join();
    }

    private static Group loadOrCreate(LuckPerms luckPerms, String name) {
        Group loaded = luckPerms.getGroupManager().getGroup(name);
        if (loaded != null) {
            return loaded;
        }
        Optional<Group> fromStorage = luckPerms.getGroupManager().loadGroup(name).join();
        return fromStorage.orElseGet(() -> luckPerms.getGroupManager().createAndLoadGroup(name).join());
    }

    private static boolean replaceable(Node node) {
        if (node instanceof PrefixNode || node instanceof WeightNode) {
            return true;
        }
        if (node instanceof PermissionNode permission) {
            return DonutRankSetup.isManagedPermission(permission.getPermission());
        }
        if (node instanceof InheritanceNode inheritance) {
            return DonutRankSetup.isRankGroup(inheritance.getGroupName());
        }
        return false;
    }
}
