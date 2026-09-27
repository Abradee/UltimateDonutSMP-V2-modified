package com.bx.ultimateDonutSmp2.commands;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.managers.OffenseManager;
import com.bx.ultimateDonutSmp2.managers.PlayerWipeManager;
import com.bx.ultimateDonutSmp2.managers.PunishmentManager;
import com.bx.ultimateDonutSmp2.models.PunishmentQuery;
import com.bx.ultimateDonutSmp2.models.PunishmentRecord;
import com.bx.ultimateDonutSmp2.models.PunishmentScope;
import com.bx.ultimateDonutSmp2.models.PunishmentType;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import com.bx.ultimateDonutSmp2.utils.NumberUtils;
import com.bx.ultimateDonutSmp2.utils.PermissionUtils;
import com.bx.ultimateDonutSmp2.utils.PunishmentExemptPolicy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class OffendCommand implements CommandExecutor, TabCompleter {

    private static final String OFFEND_PERMISSION = "ultimatedonutsmp2.staff.punishments.offend";
    private static final String CREATE_PERMISSION = "ultimatedonutsmp2.staff.punishments.create";

    private final UltimateDonutSmp2 plugin;
    private final PunishmentMessages messages;

    public OffendCommand(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
        this.messages = new PunishmentMessages(plugin, false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionUtils.has(sender, OFFEND_PERMISSION) && !PermissionUtils.has(sender, CREATE_PERMISSION)) {
            sendMessage(sender, plugin.getConfigManager().getMessageOrDefault(
                    "PUNISHMENTS.NO-CREATE-PERMISSION",
                    "&cYou do not have permission to create punishments."
            ));
            return true;
        }

        if (args.length < 2) {
            sendMessage(sender, "&cUsage: /offend <player> <reason> [time] [wipe]");
            return true;
        }

        String targetInput = args[0];
        String reasonKeyInput = args[1].toLowerCase(Locale.ROOT);

        // Resolve Target Player
        ResolvedTarget target = resolveTarget(targetInput);
        if (target == null || target.uuid() == null) {
            sendMessage(sender, plugin.getConfigManager().getMessageOrDefault("PUNISHMENTS.NOT-FOUND", "&cPlayer not found."));
            return true;
        }

        Player onlineTarget = Bukkit.getPlayer(target.uuid());
        if (!PunishmentExemptPolicy.canPunish(sender, onlineTarget)) {
            sendMessage(sender, plugin.getConfigManager().getMessageOrDefault(
                    "PUNISHMENTS.TARGET-EXEMPT",
                    "&cYou cannot punish that player."
            ));
            return true;
        }

        OffenseManager offenseManager = plugin.getOffenseManager();
        Optional<OffenseManager.OffenseRule> ruleOpt = offenseManager.getOffenseRule(reasonKeyInput);

        PunishmentType type = PunishmentType.BAN;
        String reasonDisplay = args[1];
        String durationStr = null;

        if (args.length >= 3) {
            durationStr = args[2];
        }

        Boolean wipeOverride = null;
        if (args.length >= 4) {
            wipeOverride = parseWipeOverride(args[3]);
            if (wipeOverride == null) {
                sendMessage(sender, "&cInvalid wipe option: '" + args[3] + "'. Use wipe, true, nowipe, or false.");
                return true;
            }
        }

        if (ruleOpt.isPresent()) {
            OffenseManager.OffenseRule rule = ruleOpt.get();
            type = rule.type();
            reasonDisplay = rule.name() + " (" + rule.key() + ")";

            if (durationStr == null) {
                // Calculate tier based on previous infractions for this player
                int previousInfractions = plugin.getPunishmentManager().countHistory(
                        target.uuid(),
                        target.name(),
                        new PunishmentQuery(null, null, null)
                );
                durationStr = rule.getDurationForTier(previousInfractions);
            }
        }

        Long durationMillis = OffenseManager.parseDurationToMillis(durationStr);
        Long expiresAt = durationMillis != null && durationMillis > 0L ? System.currentTimeMillis() + durationMillis : null;

        if (durationMillis != null && durationMillis == 0L && type != PunishmentType.WARN) {
            type = PunishmentType.WARN;
        }

        Actor actor = resolveActor(sender);
        PunishmentRecord record = plugin.getPunishmentManager().createRecord(new PunishmentManager.PunishmentCreateRequest(
                target.uuid(),
                target.name(),
                type,
                reasonDisplay,
                actor.uuid(),
                actor.name(),
                System.currentTimeMillis(),
                expiresAt,
                "local",
                PunishmentScope.SERVER
        ));

        if (record == null) {
            sendMessage(sender, plugin.getConfigManager().getMessageOrDefault(
                    "PUNISHMENTS.CREATE-FAILED",
                    "&cFailed to create punishment record."
            ));
            return true;
        }

        messages.applyRuntimeEffect(type, onlineTarget, record);

        if (plugin.getDiscordWebhookManager() != null) {
            plugin.getDiscordWebhookManager().sendPunishment(record);
        }

        String durationDisplay = expiresAt == null ? "Permanent" : NumberUtils.formatCountdown(Math.max(0L, (expiresAt - System.currentTimeMillis()) / 1000L));
        sendMessage(sender, "&aSuccessfully issued &f" + type.name() + " &apunishment to &b" + target.name() + "&a! [Duration: &f" + durationDisplay + "&a, Reason: &f" + reasonDisplay + "&a]");

        boolean presetWipe = ruleOpt.isPresent() && ruleOpt.get().wipe();
        if (shouldWipe(wipeOverride, presetWipe, type)) {
            wipeTarget(sender, target);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String partial = args[1].toLowerCase(Locale.ROOT);
            return plugin.getOffenseManager().getOffenseKeys().stream()
                    .filter(key -> key.toLowerCase(Locale.ROOT).startsWith(partial))
                    .sorted()
                    .collect(Collectors.toList());
        }

        if (args.length == 3) {
            String partial = args[2].toLowerCase(Locale.ROOT);
            List<String> durations = List.of("24h", "3d", "7d", "14d", "30d", "60d", "180d", "360d", "perm");
            return durations.stream()
                    .filter(d -> d.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 4) {
            String partial = args[3].toLowerCase(Locale.ROOT);
            List<String> options = List.of("wipe", "true", "nowipe", "false");
            return options.stream()
                    .filter(d -> d.startsWith(partial))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    static Boolean parseWipeOverride(String input) {
        if (input == null) {
            return null;
        }
        String normalized = input.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("wipe") || normalized.equals("true")) {
            return Boolean.TRUE;
        }
        if (normalized.equals("nowipe") || normalized.equals("false")) {
            return Boolean.FALSE;
        }
        return null;
    }

    static boolean shouldWipe(Boolean wipeOverride, boolean presetWipe, PunishmentType type) {
        boolean requested = wipeOverride != null ? wipeOverride : presetWipe;
        return requested && isBan(type);
    }

    private static boolean isBan(PunishmentType type) {
        return type == PunishmentType.BAN || type == PunishmentType.BLACKLIST;
    }

    /**
     * Runs the wipe an offense asked for. It happens after the kick has already gone out, so the
     * save the quit handler makes on the way out cannot put the old totals back over the rows the
     * wipe is deleting.
     */
    private void wipeTarget(CommandSender sender, ResolvedTarget target) {
        PlayerWipeManager wipeManager = plugin.getPlayerWipeManager();
        if (wipeManager == null) {
            return;
        }

        PlayerWipeManager.WipeResult result = wipeManager.wipe(
                new PlayerWipeManager.Target(target.uuid(), target.name()),
                resolveActor(sender).name()
        );

        if (result.busy()) {
            sendMessage(sender, "&cCould not wipe &f" + target.name() + "&c: a player wipe is already running.");
            return;
        }
        if (!result.success()) {
            String error = result.errorMessage() == null || result.errorMessage().isBlank()
                    ? "unknown error"
                    : result.errorMessage();
            sendMessage(sender, "&cCould not wipe &f" + target.name() + "&c: &f" + error);
            return;
        }

        sendMessage(sender, "&aWiped &f" + target.name() + "&a. Records removed: &f" + result.counts().total() + "&a.");
    }

    private ResolvedTarget resolveTarget(String input) {
        Player online = Bukkit.getPlayerExact(input);
        if (online == null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(input)) {
                    online = player;
                    break;
                }
            }
        }

        if (online != null) {
            return new ResolvedTarget(online.getUniqueId(), online.getName());
        }

        UUID knownUuid = plugin.getPunishmentManager().resolveTargetUuid(input, true).orElse(null);
        if (knownUuid != null) {
            return new ResolvedTarget(knownUuid, plugin.getPunishmentManager().resolveTargetName(knownUuid, input));
        }
        return null;
    }

    private Actor resolveActor(CommandSender sender) {
        if (sender instanceof Player player) {
            return new Actor(player.getUniqueId(), player.getName());
        }
        return new Actor(null, "console");
    }

    private void sendMessage(CommandSender sender, String message) {
        if (sender != null && message != null && !message.isBlank()) {
            sender.sendMessage(ColorUtils.toComponent(message));
        }
    }

    private record ResolvedTarget(UUID uuid, String name) {}
    private record Actor(UUID uuid, String name) {}
}
