package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.utils.PermissionUtils;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.amethyst.AmethystDuration;
import com.bx.ultimateDonutSmp2.amethyst.AmethystToolsManager;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandMap;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AmethystToolCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "ultimatedonutsmp2.admin.amethysttool";
    private static final List<String> SUBCOMMAND_COMPLETIONS = List.of("give", "reload");
    private static final List<String> TYPE_COMPLETIONS = List.of(
            "pickaxe-silk",
            "pickaxe-fortune",
            "axe",
            "sell-axe",
            "shovel-silk",
            "shovel-fortune",
            "haste-potion",
            "drill",
            "chopper",
            "shovel",
            "bucket",
            "shard-booster"
    );
    private static final List<String> DURATION_COMPLETIONS = List.of("1h", "5h", "10h", "1d", "5d");

    private final UltimateDonutSmp2 plugin;

    public AmethystToolCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var mgr = plugin.getAmethystToolsManager();

        if (args.length < 1) {
            if (sender instanceof Player player) {
                if (!PermissionUtils.has(sender, PERMISSION)) {
                    sender.sendMessage(ColorUtils.toComponent("&cNo permission."));
                    return true;
                }
                if (plugin.getDialogManager() != null && plugin.getDialogManager().openAmethyst(player)) {
                    return true;
                }
                sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("DIALOG-UNAVAILABLE")));
                return true;
            }
            sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("GIVE-USAGE")));
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        if (sub.equals("reload")) {
            if (!PermissionUtils.has(sender, PERMISSION)) {
                sender.sendMessage(ColorUtils.toComponent("&cNo permission."));
                return true;
            }
            plugin.getConfigManager().reloadAmethystTools();
            sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("RELOAD-SUCCESS")));
            return true;
        }

        if (sub.equals("give")) {
            if (!PermissionUtils.has(sender, PERMISSION)) {
                sender.sendMessage(ColorUtils.toComponent("&cNo permission."));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("GIVE-USAGE")));
                return true;
            }
            String targetName = args[1];
            String typeName = args[2];
            long duration = -1L;
            if (args.length >= 4) {
                duration = AmethystDuration.parseSeconds(joinDuration(args, 3));
                if (duration <= 0L) {
                    sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("DURATION-INVALID")));
                    return true;
                }
            }

            AmethystToolsManager.ShardToolVariant variant = mgr.resolveVariant(typeName);
            if (variant == null) {
                sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("GIVE-INVALID-TYPE")));
                return true;
            }

            var account = plugin.getEconomyManager() == null
                    ? null
                    : plugin.getEconomyManager().resolveAccount(targetName);
            if (account == null) {
                sender.sendMessage(ColorUtils.toComponent(mgr.getMessage(
                        "GIVE-PLAYER-NOT-FOUND", "{player}", targetName)));
                return true;
            }

            AmethystToolsManager.GrantOutcome outcome = mgr.grant(account.uuid(), variant, duration);
            long granted = duration > 0L ? duration : variant.getDefaultDuration();
            String key = switch (outcome) {
                case ONLINE -> "GIVE-SUCCESS";
                case QUEUED -> "GIVE-QUEUED";
                case FAILED -> "GIVE-FAILED";
            };
            sender.sendMessage(ColorUtils.toComponent(mgr.getMessage(
                    key,
                    "{type}", mgr.getDialogName(variant),
                    "{player}", account.displayName() == null ? targetName : account.displayName(),
                    "{time}", AmethystDuration.formatDhM(granted))));
            return true;
        }

        sender.sendMessage(ColorUtils.toComponent(mgr.getMessage("GIVE-USAGE")));
        return true;
    }

    public void registerDynamicAliases() {
        CommandMap commandMap = resolveCommandMap();
        if (commandMap == null) {
            return;
        }
        Command dynamicCommand = new Command("shardtool", "Shard tool admin command", "/shardtool [give <player> <type> [duration]]", List.of("shardtools", "stool")) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                return AmethystToolCommand.this.onCommand(sender, this, label, args);
            }

            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                return AmethystToolCommand.this.onTabComplete(sender, this, alias, args);
            }
        };
        dynamicCommand.setPermission(PERMISSION);
        commandMap.register(plugin.getDescription().getName().toLowerCase(Locale.ROOT), dynamicCommand);
    }

    private CommandMap resolveCommandMap() {
        try {
            Method method = Bukkit.getServer().getClass().getMethod("getCommandMap");
            Object commandMap = method.invoke(Bukkit.getServer());
            return commandMap instanceof CommandMap map ? map : null;
        } catch (ReflectiveOperationException ignored) {
            try {
                Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                Object commandMap = field.get(Bukkit.getServer());
                return commandMap instanceof CommandMap map ? map : null;
            } catch (ReflectiveOperationException ignoredAgain) {
                return null;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!PermissionUtils.has(sender, PERMISSION)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return partialMatches(args[0], SUBCOMMAND_COMPLETIONS);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return partialMatches(args[1], knownPlayerNames());
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return partialMatches(args[2], TYPE_COMPLETIONS);
        }

        if (args.length >= 4 && args[0].equalsIgnoreCase("give")) {
            return partialMatches(args[args.length - 1], DURATION_COMPLETIONS);
        }

        return Collections.emptyList();
    }

    private List<String> knownPlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        if (plugin.getDatabaseManager() != null) {
            for (String name : plugin.getDatabaseManager().loadKnownPlayerNames()) {
                if (name != null && !name.isBlank() && names.stream().noneMatch(existing -> existing.equalsIgnoreCase(name))) {
                    names.add(name);
                }
            }
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    private List<String> partialMatches(String token, List<String> completions) {
        List<String> matches = new ArrayList<>();
        StringUtil.copyPartialMatches(token, completions, matches);
        matches.sort(String.CASE_INSENSITIVE_ORDER);
        return matches;
    }

    private static String joinDuration(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int index = start; index < args.length; index++) {
            if (index > start) {
                builder.append(' ');
            }
            builder.append(args[index]);
        }
        return builder.toString();
    }
}
