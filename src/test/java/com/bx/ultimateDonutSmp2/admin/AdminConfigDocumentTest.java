package com.bx.ultimateDonutSmp2.admin;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminConfigDocumentTest {

    @Test
    void editsShopTitleWithoutDroppingTheFileBanner() throws Exception {
        String original = Files.readString(Path.of("src/main/resources/shop.yml"));
        String banner = original.lines()
                .filter(line -> line.startsWith("# UltimateDonutSMP"))
                .findFirst()
                .orElseThrow();
        AdminConfigDocument document = AdminConfigDocument.parse("shop.yml", original);
        AdminConfigDocument.ScalarNode title = assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                document.resolve(List.of("SHARD-MENU", "TITLE"))
        );
        assertEquals("&8Shard Shop", title.raw());

        String updated = document.apply(List.of(
                AdminConfigDocument.Edit.scalar(List.of("SHARD-MENU", "TITLE"), "&8New Shop")
        ));
        assertTrue(updated.contains(banner));
        assertTrue(updated.contains("TITLE: \"&8New Shop\""));
        assertFalse(updated.contains("TITLE: \"&8Shard Shop\""));

        AdminConfigDocument again = AdminConfigDocument.parse("shop.yml", updated);
        assertEquals("&8New Shop", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                again.resolve(List.of("SHARD-MENU", "TITLE"))
        ).raw());
        assertEquals("54", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                again.resolve(List.of("SHARD-MENU", "SIZE"))
        ).raw());
    }

    @Test
    void editsLoreListAndNestedSpawnerMaterial() throws Exception {
        String shop = Files.readString(Path.of("src/main/resources/shop.yml"));
        AdminConfigDocument document = AdminConfigDocument.parse("shop.yml", shop);
        AdminConfigDocument.ListNode lore = assertInstanceOf(
                AdminConfigDocument.ListNode.class,
                document.resolve(List.of("SHARD-MENU", "SHARD-PICKAXE-SILK-ITEM", "LORE"))
        );
        assertTrue(lore.scalarElements());
        assertEquals("&fBreaks 9 Blocks at Once", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class, lore.elements().get(0)).raw());

        String updated = document.apply(List.of(AdminConfigDocument.Edit.list(
                List.of("SHARD-MENU", "SHARD-PICKAXE-SILK-ITEM", "LORE"),
                List.of("&fBreaks 9 Blocks at Once", "&7New line")
        )));
        AdminConfigDocument again = AdminConfigDocument.parse("shop.yml", updated);
        AdminConfigDocument.ListNode edited = assertInstanceOf(
                AdminConfigDocument.ListNode.class,
                again.resolve(List.of("SHARD-MENU", "SHARD-PICKAXE-SILK-ITEM", "LORE"))
        );
        assertEquals(2, edited.elements().size());
        assertEquals("&7New line", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class, edited.elements().get(1)).raw());
        assertTrue(updated.contains("# Shard Shop Menu"));

        String stash = Files.readString(Path.of("src/main/resources/spawn-stash.yml"));
        AdminConfigDocument spawn = AdminConfigDocument.parse("spawn-stash.yml", stash);
        assertEquals("SPAWNER", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                spawn.resolve(List.of("TYPES", "1", "BLOCKS", "1", "MATERIAL"))
        ).raw());
        assertEquals("SKELETON", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                spawn.resolve(List.of("TYPES", "1", "BLOCKS", "1", "SPAWNER_TYPE"))
        ).raw());
        String changed = spawn.apply(List.of(AdminConfigDocument.Edit.scalar(
                List.of("TYPES", "1", "BLOCKS", "1", "MATERIAL"), "STONE"
        )));
        assertEquals("STONE", assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                AdminConfigDocument.parse("spawn-stash.yml", changed)
                        .resolve(List.of("TYPES", "1", "BLOCKS", "1", "MATERIAL"))
        ).raw());
        assertTrue(changed.contains("DISPLAY_NAME:"));
    }

    @Test
    void rejectsASizeThatIsNotAWholeNumber() throws Exception {
        String original = Files.readString(Path.of("src/main/resources/shop.yml"));
        AdminConfigDocument document = AdminConfigDocument.parse("shop.yml", original);
        AdminConfigEditException error = assertThrows(AdminConfigEditException.class, () -> document.apply(List.of(
                AdminConfigDocument.Edit.scalar(List.of("SHARD-MENU", "SIZE"), "big")
        )));
        assertTrue(error.getMessage().contains("Example:"));
        assertTrue(error.getMessage().contains("whole number") || error.getMessage().contains("Size"));
    }

    @Test
    void keepsWrappedQuotesAndBlockText() throws Exception {
        String hide = Files.readString(Path.of("src/main/resources/hide.yml"));
        AdminConfigDocument document = AdminConfigDocument.parse("hide.yml", hide);
        AdminConfigDocument.ScalarNode wrapped = assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                document.resolve(List.of("MESSAGES", "PERMISSION-REMOVED"))
        );
        assertTrue(wrapped.raw().contains("longer available"));
        assertFalse(wrapped.raw().contains("\n"));

        AdminConfigDocument.ScalarNode check = assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                document.resolve(List.of("MESSAGES", "CHECK"))
        );
        assertTrue(check.block());
        assertTrue(check.raw().contains("hide check"));
        assertTrue(check.raw().contains("real name"));

        String updated = document.apply(List.of(
                AdminConfigDocument.Edit.scalar(List.of("MESSAGES", "CHECK"), "&bhide check\n&7alias: &f{alias}")
        ));
        assertTrue(updated.contains("PERMISSION-REMOVED:"));
        AdminConfigDocument.ScalarNode edited = assertInstanceOf(
                AdminConfigDocument.ScalarNode.class,
                AdminConfigDocument.parse("hide.yml", updated).resolve(List.of("MESSAGES", "CHECK"))
        );
        assertTrue(edited.raw().contains("alias"));
    }

    @Test
    void parsesEveryBundledConfigExceptLanguages() throws Exception {
        Path resources = Path.of("src/main/resources");
        try (Stream<Path> paths = Files.list(resources)) {
            for (Path path : paths.filter(candidate -> {
                String name = candidate.getFileName().toString();
                return name.endsWith(".yml") && !name.equals("plugin.yml");
            }).toList()) {
                String text = Files.readString(path);
                AdminConfigDocument.parse(path.getFileName().toString(), text);
            }
        }
    }
}
