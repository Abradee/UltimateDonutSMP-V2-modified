package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.dialogs.DialogSupport;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.PermissionUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Opens the in-game config editor. Saving there reloads the plugin, so this is the dialog
 * alternative to editing a yaml file and then running a reload command.
 */
public final class AdminConfigCommand implements CommandExecutor {

    private static final String PERMISSION = "ultimatedonutsmp2.command.uds2a";

    private final UltimateDonutSmp2 plugin;

    public AdminConfigCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionUtils.has(sender, PERMISSION)) {
            sender.sendMessage(ColorUtils.toComponent("&cYou do not have permission to edit the config in game."));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorUtils.toComponent(
                    "&c/uds2a opens a dialog, so run it as a player. You can still edit the yaml files, then use /uds reload."
            ));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("ranks")) {
            if (!DialogSupport.isAvailable() || plugin.getDialogManager() == null) {
                player.sendMessage(ColorUtils.toComponent(
                        "&cRank setup needs Paper dialogs, PlaceholderAPI, and LuckPerms."
                ));
                return true;
            }
            plugin.getDialogManager().openDonutRanks(player);
            return true;
        }
        if (!DialogSupport.isAvailable() || plugin.getDialogManager() == null) {
            player.sendMessage(ColorUtils.toComponent(
                    "&cIn-game config needs Paper with dialogs. Edit the yaml files, then use /uds reload."
            ));
            return true;
        }
        if (!plugin.getDialogManager().openAdminConfig(player)) {
            player.sendMessage(ColorUtils.toComponent("&cCould not open the config editor."));
        }
        return true;
    }
}
