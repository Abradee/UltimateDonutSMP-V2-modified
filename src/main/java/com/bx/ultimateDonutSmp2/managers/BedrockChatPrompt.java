package com.bx.ultimateDonutSmp2.managers;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;
import com.bx.ultimateDonutSmp2.utils.BedrockPlayers;
import com.bx.ultimateDonutSmp2.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Chat stand-in for a sign editor. A sign on Bedrock closes itself and then
 * refuses every container until the player rejoins.
 */
public final class BedrockChatPrompt implements Listener {

    public enum Intake {
        CANCEL,
        REASK,
        SUBMIT
    }

    private record Pending(String message, Consumer<String> callback, Runnable onQuit) {
    }

    private final UltimateDonutSmp2 plugin;
    private final Map<UUID, Pending> pending = new ConcurrentHashMap<>();

    public BedrockChatPrompt(UltimateDonutSmp2 plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static Intake intake(String raw) {
        String input = raw == null ? "" : raw.trim();
        if (input.equalsIgnoreCase("cancel")) {
            return Intake.CANCEL;
        }
        if (input.isBlank()) {
            return Intake.REASK;
        }
        return Intake.SUBMIT;
    }

    public static String submitted(String raw) {
        return raw == null ? "" : raw.trim();
    }

    public boolean hasPending(UUID playerId) {
        return playerId != null && pending.containsKey(playerId);
    }

    /**
     * @return true when this player is on Bedrock and the next chat line is the answer
     */
    public boolean ask(Player player, String message, Consumer<String> callback, Runnable onQuit) {
        if (player == null || callback == null || message == null || message.isBlank()
                || !BedrockPlayers.isBedrock(player)) {
            return false;
        }
        pending.put(player.getUniqueId(), new Pending(message, callback, onQuit));
        plugin.getSpigotScheduler().runEntityLater(player, () -> deliver(player), 1L);
        return true;
    }

    public void handle(Player player, String raw) {
        if (player == null) {
            return;
        }
        Pending request = pending.get(player.getUniqueId());
        if (request == null) {
            return;
        }
        switch (intake(raw)) {
            case CANCEL -> {
                pending.remove(player.getUniqueId());
                request.callback.accept(null);
            }
            case REASK -> player.sendMessage(ColorUtils.toComponent(request.message));
            case SUBMIT -> {
                pending.remove(player.getUniqueId());
                request.callback.accept(submitted(raw));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Pending request = pending.remove(event.getPlayer().getUniqueId());
        if (request != null && request.onQuit != null) {
            request.onQuit.run();
        }
    }

    private void deliver(Player player) {
        Pending request = pending.get(player.getUniqueId());
        if (!player.isOnline() || request == null) {
            return;
        }
        try {
            player.closeInventory();
        } catch (Throwable ignored) {
        }
        player.sendMessage(ColorUtils.toComponent(request.message));
    }
}
