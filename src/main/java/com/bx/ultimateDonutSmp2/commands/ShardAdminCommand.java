package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.CurrencyManager;
import com.bx.ultimateDonutSmp2.managers.EconomyManager;
import com.bx.ultimateDonutSmp2.managers.LeaderboardManager;
import com.bx.ultimateDonutSmp2.models.PlayerData;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.NumberUtils;
import com.bx.ultimateDonutSmp2.utils.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShardAdminCommand implements CommandExecutor {

    private static final String PERMISSION = "ultimatedonutsmp2.admin.shards";

    private final UltimateDonutSmp2 plugin;

    public ShardAdminCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionUtils.has(sender, PERMISSION)) {
            sender.sendMessage(ColorUtils.toComponent("&cNo permission."));
            return true;
        }

        Mode mode = Mode.fromCommand(command.getName());
        if (mode == null) {
            sender.sendMessage(ColorUtils.toComponent("&cUnknown shard admin command."));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ColorUtils.toComponent("&cUsage: /" + label + " <player> <amount>"));
            return true;
        }

        long amount;
        try {
            amount = NumberUtils.parseLong(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.ADMIN.INVALID-AMOUNT")));
            return true;
        }

        if (mode == Mode.SET) {
            if (amount < 0L) {
                sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.ADMIN.MUST-BE-NON-NEGATIVE")));
                return true;
            }
        } else if (amount <= 0L) {
            sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.ADMIN.MUST-BE-POSITIVE")));
            return true;
        }

        EconomyManager.AccountReference account = plugin.getEconomyManager().resolveAccount(args[0]);
        if (account == null) {
            sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.ADMIN.PLAYER-NOT-FOUND")));
            return true;
        }

        PlayerData data = loadData(account);
        if (data == null) {
            sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage("BALANCE.ADMIN.PLAYER-NOT-FOUND")));
            return true;
        }

        long before = data.getShards();
        if (mode == Mode.REMOVE && before < amount) {
            sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage(
                    "BALANCE.ADMIN.TARGET-NOT-ENOUGH-SHARDS",
                    "{player}", account.displayName(),
                    "{shards}", formatShards(before),
                    "{shards_compact}", formatShardsCompact(before),
                    "{balance}", formatShards(before),
                    "{amount}", String.valueOf(before)
            )));
            return true;
        }

        switch (mode) {
            case ADD -> data.addShards(amount);
            case REMOVE -> data.removeShards(amount);
            case SET -> data.setShards(amount);
        }

        plugin.getDatabaseManager().savePlayer(data);
        if (plugin.getLeaderboardManager() != null) {
            plugin.getLeaderboardManager().invalidate(LeaderboardManager.LeaderboardType.SHARDS);
        }

        long after = data.getShards();
        String successKey = switch (mode) {
            case ADD -> "BALANCE.ADMIN.ADD-SHARDS-SUCCESS";
            case REMOVE -> "BALANCE.ADMIN.REMOVE-SHARDS-SUCCESS";
            case SET -> "BALANCE.ADMIN.SET-SHARDS-SUCCESS";
        };
        sender.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage(
                successKey,
                "{player}", account.displayName(),
                "{amount}", compactAmount(amount),
                "{amount_full}", fullAmount(amount),
                "{amount_shards}", formatShards(amount),
                "{amount_raw}", String.valueOf(amount),
                "{shards}", formatShards(after),
                "{shards_compact}", formatShardsCompact(after),
                "{balance}", formatShards(after),
                "{balance_raw}", String.valueOf(after),
                "{previous_balance}", formatShards(before),
                "{previous_balance_raw}", String.valueOf(before),
                "{previous_shards}", formatShards(before),
                "{previous_shards_compact}", formatShardsCompact(before)
        )));

        Player target = Bukkit.getPlayer(account.uuid());
        if (target != null && !target.equals(sender)) {
            String receivedKey = switch (mode) {
                case ADD -> "BALANCE.ADMIN.ADD-SHARDS-RECEIVED";
                case REMOVE -> "BALANCE.ADMIN.REMOVE-SHARDS-RECEIVED";
                case SET -> "BALANCE.ADMIN.SET-SHARDS-RECEIVED";
            };
            target.sendMessage(ColorUtils.toComponent(plugin.getConfigManager().getMessage(
                    receivedKey,
                    "{admin}", sender.getName(),
                    "{player}", account.displayName(),
                    "{amount}", compactAmount(amount),
                    "{amount_full}", fullAmount(amount),
                    "{amount_shards}", formatShards(amount),
                    "{amount_raw}", String.valueOf(amount),
                    "{shards}", formatShards(after),
                    "{shards_compact}", formatShardsCompact(after),
                    "{balance}", formatShards(after),
                    "{balance_raw}", String.valueOf(after),
                    "{previous_balance}", formatShards(before),
                    "{previous_balance_raw}", String.valueOf(before),
                    "{previous_shards}", formatShards(before),
                    "{previous_shards_compact}", formatShardsCompact(before)
            )));
        }

        return true;
    }

    private PlayerData loadData(EconomyManager.AccountReference account) {
        if (account.onlinePlayer() != null) {
            PlayerData data = plugin.getPlayerDataManager().get(account.onlinePlayer());
            return data == null ? plugin.getPlayerDataManager().loadOrCreate(account.onlinePlayer()) : data;
        }
        return plugin.getDatabaseManager().loadPlayer(account.uuid());
    }

    private String formatShards(long amount) {
        return plugin.getCurrencyManager().formatShards(amount);
    }

    private String formatShardsCompact(long amount) {
        return plugin.getCurrencyManager().formatShardsCompact(amount);
    }

    private String compactAmount(long amount) {
        return plugin.getCurrencyManager().formatCompactAmount(CurrencyManager.CurrencyType.SHARDS, amount);
    }

    private String fullAmount(long amount) {
        return plugin.getCurrencyManager().formatAmount(CurrencyManager.CurrencyType.SHARDS, amount);
    }

    private enum Mode {
        ADD("addshards", "Added"),
        REMOVE("removeshards", "Removed"),
        SET("setshards", "Set");

        private final String commandName;
        private final String pastTense;

        Mode(String commandName, String pastTense) {
            this.commandName = commandName;
            this.pastTense = pastTense;
        }

        static Mode fromCommand(String commandName) {
            for (Mode mode : values()) {
                if (mode.commandName.equalsIgnoreCase(commandName)) {
                    return mode;
                }
            }
            return null;
        }

        String pastTense() {
            return pastTense;
        }
    }
}
