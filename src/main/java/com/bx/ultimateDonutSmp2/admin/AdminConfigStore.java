package com.bx.ultimateDonutSmp2.admin;

import com.bx.ultimateDonutSmp2.UltimateDonutSmp2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Reads and writes one editable yaml file, keeping a backup next to the sync backups. */
public final class AdminConfigStore {

    private static final DateTimeFormatter BACKUP_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private AdminConfigStore() {
    }

    public static AdminConfigDocument load(UltimateDonutSmp2 plugin, String name) throws IOException {
        File file = file(plugin, name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        if (!file.isFile()) {
            throw new IOException("Missing config file " + name);
        }
        return AdminConfigDocument.parse(name, Files.readString(file.toPath(), StandardCharsets.UTF_8));
    }

    public static void save(UltimateDonutSmp2 plugin, String name, String text) throws IOException {
        AdminConfigDocument.parse(name, text);
        File file = file(plugin, name);
        Path target = file.toPath();
        Files.createDirectories(target.getParent());
        if (Files.exists(target)) {
            Path backup = plugin.getDataFolder().toPath()
                    .resolve("config-backups")
                    .resolve("uds2a-" + LocalDateTime.now().format(BACKUP_TIMESTAMP))
                    .resolve(name);
            Files.createDirectories(backup.getParent());
            Files.copy(target, backup, StandardCopyOption.REPLACE_EXISTING);
        }
        Path temporary = Files.createTempFile(target.getParent(), "." + name + ".", ".tmp");
        try {
            Files.writeString(temporary, text, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException atomicMoveFailed) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static File file(UltimateDonutSmp2 plugin, String name) throws IOException {
        if (!AdminConfigCatalog.editable(name)) {
            throw new IOException("That file cannot be edited in game.");
        }
        File folder = plugin.getDataFolder().getCanonicalFile();
        File target = new File(folder, name).getCanonicalFile();
        if (!target.toPath().startsWith(folder.toPath())) {
            throw new IOException("That file cannot be edited in game.");
        }
        return target;
    }
}
