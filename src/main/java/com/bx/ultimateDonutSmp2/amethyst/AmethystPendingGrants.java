package com.bx.ultimateDonutSmp2.amethyst;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Queues amethyst tools for players who are offline. Each grant is written to
 * {@code amethyst-pending.yml} so a restart does not drop it; {@link #take} removes the
 * player's list when they join and the items are created.
 */
public final class AmethystPendingGrants {

    public record Grant(String typeId, long durationSeconds, long queuedAtMillis) {
    }

    private final File file;
    private final Logger logger;
    private final Map<UUID, List<Grant>> grants = new ConcurrentHashMap<>();

    public AmethystPendingGrants(File file, Logger logger) {
        this.file = file;
        this.logger = logger;
    }

    public synchronized void load() {
        grants.clear();
        if (file == null || !file.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection root = yaml.getConfigurationSection("players");
        if (root == null) {
            return;
        }
        for (String key : root.getKeys(false)) {
            UUID uuid = parseUuid(key);
            if (uuid == null) {
                continue;
            }
            List<Grant> loaded = new ArrayList<>();
            for (Map<?, ?> entry : root.getMapList(key)) {
                Object type = entry.get("type");
                if (type == null) {
                    continue;
                }
                long duration = number(entry.get("duration"));
                long queuedAt = number(entry.get("queued-at"));
                if (duration <= 0L) {
                    continue;
                }
                loaded.add(new Grant(String.valueOf(type), duration, queuedAt));
            }
            if (!loaded.isEmpty()) {
                grants.put(uuid, loaded);
            }
        }
    }

    public void queue(UUID playerId, String typeId, long durationSeconds) {
        if (playerId == null || typeId == null || typeId.isBlank() || durationSeconds <= 0L) {
            return;
        }
        grants.compute(playerId, (ignored, existing) -> {
            List<Grant> next = existing == null ? new ArrayList<>() : new ArrayList<>(existing);
            next.add(new Grant(typeId, durationSeconds, System.currentTimeMillis()));
            return next;
        });
        save();
    }

    public List<Grant> take(UUID playerId) {
        if (playerId == null) {
            return List.of();
        }
        List<Grant> removed = grants.remove(playerId);
        if (removed == null || removed.isEmpty()) {
            return List.of();
        }
        save();
        return List.copyOf(removed);
    }

    public int count(UUID playerId) {
        if (playerId == null) {
            return 0;
        }
        List<Grant> existing = grants.get(playerId);
        return existing == null ? 0 : existing.size();
    }

    public synchronized void save() {
        if (file == null) {
            return;
        }
        YamlConfiguration yaml = new YamlConfiguration();
        for (Map.Entry<UUID, List<Grant>> entry : grants.entrySet()) {
            List<Map<String, Object>> rows = new ArrayList<>();
            for (Grant grant : entry.getValue()) {
                rows.add(Map.of(
                        "type", grant.typeId(),
                        "duration", grant.durationSeconds(),
                        "queued-at", grant.queuedAtMillis()
                ));
            }
            yaml.set("players." + entry.getKey(), rows);
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            log("Could not create " + parent.getPath());
            return;
        }
        try {
            yaml.save(file);
        } catch (IOException exception) {
            log("Failed to save pending amethyst grants: " + exception.getMessage());
        }
    }

    private void log(String message) {
        if (logger != null) {
            logger.log(Level.WARNING, message);
        }
    }

    private static UUID parseUuid(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static long number(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value != null) {
            try {
                return Long.parseLong(String.valueOf(value).trim());
            } catch (NumberFormatException ignored) {
                return 0L;
            }
        }
        return 0L;
    }
}
