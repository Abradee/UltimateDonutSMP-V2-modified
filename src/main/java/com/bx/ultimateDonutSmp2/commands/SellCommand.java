package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.menus.SellAllConfirmMenu;
import com.bx.ultimateDonutSmp2.menus.SellHistoryMenu;
import com.bx.ultimateDonutSmp2.menus.SellMenu;
import com.bx.ultimateDonutSmp2.menus.SellStatsAdminMenu;
import com.bx.ultimateDonutSmp2.utils.CommandLabelUtils;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SellCommand implements CommandExecutor, TabCompleter {

    private final UltimateDonutSmp2 plugin;

    public SellCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage("Player only."); return true; }

        String sub = CommandLabelUtils.normalizeLabel(label, command);

        if (sub.equals("topsell") || sub.equals("sellstats")) {
            return new SellStatsCommand(plugin).onCommand(sender, command, label, args);
        }

        if (args.length > 0 && (args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("top"))) {
            return new SellStatsCommand(plugin).onCommand(sender, command, label, args);
        }

        switch (sub) {
            case "sell" -> new SellMenu(plugin).open(player);
            case "sellmulti", "sellmultiplier", "sellprogress" -> {
                player.sendMessage(ColorUtils.toComponent("&cThis command is temporarily disabled"));
            }
            case "sellhand" -> {
                double total = plugin.getShopManager().sellInventory(player, true);
                if (total <= 0) player.sendMessage(ColorUtils.toComponent(
                        plugin.getConfigManager().getMessage("WORTH.NO-SELLABLE")));
            }
            case "sellall" -> new SellAllConfirmMenu(plugin).open(player);
            case "sellhistory" -> {
                if (args.length > 0 && args[0].equalsIgnoreCase("admin")) {
                    new SellStatsAdminMenu(plugin).open(player);
                } else {
                    new SellHistoryMenu(plugin).open(player);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}