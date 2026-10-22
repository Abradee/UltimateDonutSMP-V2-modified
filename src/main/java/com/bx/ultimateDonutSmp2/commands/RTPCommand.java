package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.menus.RTPMenu;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {

    private final UltimateDonutSmp2 plugin;

    public RTPCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Player only.");
            return true;
        }

        if (!plugin.getConfigManager().isCommandEnabled("RTP")) {
            player.sendMessage(ColorUtils.toComponent("&cRTP command is currently disabled."));
            return true;
        }

        if (!plugin.getRtpManager().isEnabled()) {
            player.sendMessage(ColorUtils.toComponent(
                    plugin.getConfigManager().getRtp().getString("MESSAGES.DISABLED", "&cRTP is disabled.")));
            return true;
        }

        if (plugin.getCombatManager() != null && plugin.getCombatManager().isInCombat(player.getUniqueId())) {
            player.sendMessage(ColorUtils.toComponent(plugin.getCombatManager().getBlockMessage()));
            return true;
        }

        if (args.length == 0) {
            if (isMenuEnabled() && plugin.getRtpManager().hasMenuDestinations()) {
                new RTPMenu(plugin).open(player);
                return true;
            }

            plugin.getRtpManager().queueCommandTeleport(player, player.getWorld().getName());
            return true;
        }

        plugin.getRtpManager().queueCommandTeleport(player, args[0]);
        return true;
    }

    public boolean isMenuEnabled() {
        return plugin.getConfigManager().getRtp().getBoolean("RTP-MENU.ENABLED", true);
    }
}
