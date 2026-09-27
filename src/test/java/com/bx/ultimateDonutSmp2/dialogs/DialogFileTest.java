package com.bx.ultimateDonutSmp2.dialogs;

import com.bx.ultimateDonutSmp2.managers.PlayerSettingToggles;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the shipped {@code dialog.yml} against the two ways a button silently stops working:
 * an action id the router does not answer for, or one a resource location cannot hold.
 */
class DialogFileTest {

    private static YamlConfiguration dialog;

    @BeforeAll
    static void loadConfig() throws Exception {
        dialog = new YamlConfiguration();
        dialog.load(Path.of("src/main/resources/dialog.yml").toFile());
    }

    /** Every ACTION in the file, with its placeholder tokens still in place. */
    private static List<String> actions() {
        List<String> actions = new ArrayList<>();
        collectActions(dialog, actions);
        return actions;
    }

    private static void collectActions(ConfigurationSection section, List<String> actions) {
        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            if (value instanceof ConfigurationSection child) {
                collectActions(child, actions);
            } else if (value instanceof List<?> list) {
                collectFromList(list, actions);
            } else if (isActionKey(key) && value != null) {
                actions.add(String.valueOf(value));
            }
        }
    }

    private static void collectFromList(List<?> list, List<String> actions) {
        for (Object element : list) {
            if (!(element instanceof Map<?, ?> entry)) {
                continue;
            }
            for (Map.Entry<?, ?> field : entry.entrySet()) {
                if (field.getValue() instanceof List<?> nested) {
                    collectFromList(nested, actions);
                } else if (isActionKey(String.valueOf(field.getKey())) && field.getValue() != null) {
                    actions.add(String.valueOf(field.getValue()));
                }
            }
        }
    }

    private static boolean isActionKey(String key) {
        // AFTER-ACTION names a DialogAfterAction enum constant, not a button target.
        return !key.equals("AFTER-ACTION") && (key.equals("ACTION") || key.endsWith("-ACTION"));
    }

    @Test
    void theFileActuallyDeclaresButtons() {
        assertFalse(actions().isEmpty(), "a dialog.yml with no actions would silently render dead menus");
    }

    @Test
    void everyActionUsesThisPluginsNamespace() {
        for (String action : actions()) {
            if ("custom".equals(action)) {
                // The pay amount grid marks its free-entry button this way rather than with an id.
                continue;
            }
            assertTrue(
                    action.startsWith(DialogActions.NAMESPACE + ':'),
                    "action '" + action + "' would be routed to another plugin's handler"
            );
        }
    }

    @Test
    void everyActionIdSurvivesBeingAResourceLocation() {
        for (String action : actions()) {
            if ("custom".equals(action)) {
                continue;
            }
            // Tokens are substituted before the id becomes a key, so they are stripped here to
            // check the literal part of the id on its own.
            String id = action.substring(action.indexOf(':') + 1).replaceAll("%[a-z_]+%", "");
            assertEquals(
                    id,
                    DialogActions.sanitise(id),
                    "action '" + action + "' carries characters a resource location drops"
            );
        }
    }

    /**
     * Every id a screen answers for, so an action that reaches nothing shows up as a failure here
     * rather than as a dead button on a live server.
     */
    private static final List<String> EXACT_IDS = List.of(
            DialogActions.MAIN_MENU, DialogActions.SETTINGS, DialogActions.CLOSE,
            DialogActions.PAY_MENU, DialogActions.PAY_ADD_PROMPT, DialogActions.PAY_ADD_EXECUTE,
            DialogActions.STATS_MENU, DialogActions.STATS_ADD_PROMPT, DialogActions.STATS_ADD_EXECUTE,
            DialogActions.LEADERBOARDS_MENU,
            DialogActions.HOMES, DialogActions.HOMES_MENU,
            DialogActions.HOME_CREATE_PROMPT, DialogActions.HOME_CREATE_EXECUTE, DialogActions.HOME_TEAM,
            DialogActions.FRIENDS, DialogActions.FRIENDS_FILTER,
            DialogActions.FRIENDS_SEARCH_PROMPT, DialogActions.FRIENDS_SEARCH_EXECUTE,
            DialogActions.FRIENDS_FOLLOW_PROMPT, DialogActions.FRIENDS_FOLLOW_EXECUTE,
            DialogActions.TPA, DialogActions.TPA_EXECUTE, DialogActions.TPA_ADD_PROMPT, DialogActions.TPA_ADD_EXECUTE,
            DialogActions.RTP_QUEUE, DialogActions.RTP_QUEUE_ACCEPT, DialogActions.RTP_QUEUE_DENY,
            DialogActions.ORDERS, DialogActions.ORDERS_MENU,
            DialogActions.AMETHYST_MENU, DialogActions.AMETHYST_TYPES, DialogActions.AMETHYST_PLAYERS,
            DialogActions.AMETHYST_SEARCH, DialogActions.AMETHYST_SEARCH_GO, DialogActions.AMETHYST_SEARCH_CAN,
            DialogActions.AMETHYST_TIMES, DialogActions.AMETHYST_TIME_CUSTOM, DialogActions.AMETHYST_TIME_CUSTOM_GO
    );

    private static final List<String> PREFIXES = List.of(
            DialogActions.SETTINGS_CATEGORY, DialogActions.TOGGLE,
            DialogActions.PAY_TARGET, DialogActions.PAY_CUSTOM, DialogActions.PAY_CUSTOM_CONTINUE,
            DialogActions.PAY_AMOUNT, DialogActions.PAY_EXECUTE,
            DialogActions.STATS_VIEW, DialogActions.STATS_VIEW_FULL,
            DialogActions.LEADERBOARD_CATEGORY, DialogActions.LEADERBOARD_FULL,
            DialogActions.HOMES_PAGE, DialogActions.HOME_MANAGE, DialogActions.HOME_TELEPORT,
            DialogActions.HOME_RENAME_PROMPT, DialogActions.HOME_RENAME_EXECUTE,
            DialogActions.HOME_DELETE_PROMPT, DialogActions.HOME_DELETE_EXECUTE,
            DialogActions.HOME_ICON_PROMPT, DialogActions.HOME_ICON_SEARCH,
            DialogActions.HOME_ICON_SET, DialogActions.HOME_ICON_DEFAULT,
            DialogActions.FRIENDS_FOLLOW_ADD, DialogActions.FRIENDS_VIEW,
            DialogActions.FRIENDS_UNFOLLOW, DialogActions.FRIENDS_SETTINGS,
            DialogActions.FRIENDS_SETTING_TOGGLE,
            DialogActions.TPA_TARGET, DialogActions.TPA_TO, DialogActions.TPA_HERE,
            DialogActions.AMETHYST_TYPE, DialogActions.AMETHYST_PLAYER,
            DialogActions.AMETHYST_PAGE, DialogActions.AMETHYST_TIME
    );

    @Test
    void everyActionReachesAScreenThatAnswersForIt() {
        for (String action : actions()) {
            if ("custom".equals(action)) {
                continue;
            }
            String id = action.substring(action.indexOf(':') + 1);
            boolean routed = EXACT_IDS.contains(id)
                    || PREFIXES.stream().anyMatch(id::startsWith);
            assertTrue(routed, "nothing handles the action '" + action + "'");
        }
    }

    @Test
    void everySettingsToggleNamesASettingThatExists() {
        PlayerSettingToggles toggles = new PlayerSettingToggles(null);

        for (String action : actions()) {
            String id = action.startsWith(DialogActions.NAMESPACE + ':')
                    ? action.substring(DialogActions.NAMESPACE.length() + 1)
                    : action;
            if (!id.startsWith(DialogActions.TOGGLE)) {
                continue;
            }
            String setting = id.substring(DialogActions.TOGGLE.length());
            assertTrue(
                    toggles.has(setting),
                    "the settings dialog offers '" + setting + "' but nothing knows how to toggle it"
            );
        }
    }

    @Test
    void everySettingsCategoryLabelsItsButtonsWithAPlaceholder() {
        List<Map<?, ?>> categories = dialog.getMapList("SETTINGS_DIALOG.CATEGORIES");
        assertFalse(categories.isEmpty(), "the settings dialog needs at least one category");

        for (Map<?, ?> category : categories) {
            assertNotNull(category.get("ID"), "a category with no ID cannot be opened");
            Object buttons = category.get("BUTTONS");
            assertTrue(buttons instanceof List<?> list && !list.isEmpty(),
                    "category " + category.get("ID") + " has no buttons");
            if (buttons instanceof List<?> list) {
                for (Object element : list) {
                    assertTrue(element instanceof Map<?, ?>);
                    Map<?, ?> button = (Map<?, ?>) element;
                    String label = String.valueOf(button.get("LABEL"));
                    assertTrue(
                            label.contains("%ultimatedonutsmp2_setting_"),
                            "category " + category.get("ID") + " button '" + label
                                    + "' is missing a setting placeholder"
                    );
                }
            }
        }
    }

    @Test
    void settingsDialogMatchesTheReferenceCategoryLayout() {
        assertEquals("Settings", dialog.getString("SETTINGS_DIALOG.TITLE"));
        assertEquals("Choose a category to change your UltimateDonutSMP V2 settings",
                dialog.getString("SETTINGS_DIALOG.TEXT"));
        assertEquals(2, dialog.getInt("SETTINGS_DIALOG.CATEGORIES-COLUMNS"));
        assertEquals("Settings - %category%", dialog.getString("SETTINGS_DIALOG.CATEGORY-TITLE"));
        assertEquals("Back", dialog.getString("SETTINGS_DIALOG.CATEGORY-BACK-LABEL"));

        List<Map<?, ?>> categories = dialog.getMapList("SETTINGS_DIALOG.CATEGORIES");
        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Integer> buttonCounts = new ArrayList<>();
        for (Map<?, ?> category : categories) {
            ids.add(String.valueOf(category.get("ID")));
            names.add(String.valueOf(category.get("NAME")));
            labels.add(String.valueOf(category.get("LABEL")));
            buttonCounts.add(category.get("BUTTONS") instanceof List<?> buttons ? buttons.size() : 0);
        }

        assertEquals(List.of("chat", "notifications", "pvp", "visuals", "privacy", "scoreboard", "general"), ids);
        assertEquals(List.of("Chat", "Notifications", "PvP", "Visuals", "Privacy", "Scoreboard", "General"), names);
        assertEquals(List.of(
                "<item:oak_sign> Chat",
                "<item:bell> Notifications",
                "<item:diamond_sword> PvP",
                "<item:spyglass> Visuals",
                "<item:barrier> Privacy",
                "<item:paper> Scoreboard",
                "<item:paper> General"
        ), labels);
        assertEquals(List.of(7, 7, 5, 4, 5, 6, 6), buttonCounts);

        assertEquals(List.of(
                "Public Chat",
                "Private Messages",
                "Server Chat Messages",
                "Server Hotbar Messages",
                "Death Messages",
                "Advancement Messages",
                "Join/Leave Messages"
        ), buttonNames("chat"));
        assertEquals(List.of(
                "Pay Alerts",
                "Teleport Alerts",
                "Bounty Alerts",
                "Auction Alerts",
                "Order Alerts",
                "Server Sounds",
                "Follow Alerts"
        ), buttonNames("notifications"));
        assertEquals(List.of(
                "Fast Crystals",
                "Totem Particles",
                "Explosion Particles",
                "Explosion Sounds",
                "Combat Timer"
        ), buttonNames("pvp"));
        assertEquals(List.of(
                "Display Donut+",
                "Money Nametags",
                "Item Worth Lore",
                "Teleport Confirm Menus"
        ), buttonNames("visuals"));
        assertEquals(List.of(
                "Teleport Requests",
                "Teleport-Here Requests",
                "Allow Payments",
                "Randomized Coords",
                "Private Transactions"
        ), buttonNames("privacy"));
        assertEquals(List.of(
                "Scoreboard",
                "Show Money",
                "Show Shards",
                "Show Kills",
                "Show Deaths",
                "Show Playtime"
        ), buttonNames("scoreboard"));
        assertEquals(List.of(
                "Auction Quick Buy",
                "Auction Quick Sell",
                "Mob Spawns",
                "Phantom Spawning",
                "Night Vision",
                "Destroy Pearl on Death"
        ), buttonNames("general"));
    }

    private List<String> buttonNames(String categoryId) {
        for (Map<?, ?> category : dialog.getMapList("SETTINGS_DIALOG.CATEGORIES")) {
            if (!categoryId.equals(String.valueOf(category.get("ID")))) {
                continue;
            }
            List<String> names = new ArrayList<>();
            if (category.get("BUTTONS") instanceof List<?> buttons) {
                for (Object element : buttons) {
                    if (element instanceof Map<?, ?> button) {
                        String label = String.valueOf(button.get("LABEL"));
                        int colon = label.indexOf(':');
                        names.add(colon < 0 ? label : label.substring(0, colon).trim());
                    }
                }
            }
            return names;
        }
        return List.of();
    }

    @Test
    void theMainMenuCanReachEveryScreenTheFileDefines() {
        List<Map<?, ?>> buttons = dialog.getMapList("DONUT_SMP_DIALOG.BUTTONS");
        List<String> ids = new ArrayList<>();
        for (Map<?, ?> button : buttons) {
            Object action = button.get("ACTION");
            Object command = button.get("COMMAND");
            assertTrue(action != null || command != null,
                    "main menu button '" + button.get("LABEL") + "' does nothing");
            if (action != null) {
                ids.add(String.valueOf(action));
            }
        }

        for (String expected : List.of("homes", "friends", "pay_menu", "stats_menu",
                "leaderboards_menu", "settings", "tpa", "rtp_queue", "orders")) {
            assertTrue(
                    ids.contains(DialogActions.NAMESPACE + ':' + expected),
                    "the main menu no longer opens " + expected
            );
        }
    }

    @Test
    void settingsDialogTextMentionsPluginName() {
        String text = dialog.getString("SETTINGS_DIALOG.TEXT");
        assertNotNull(text);
        assertTrue(
                text.contains("UltimateDonutSMP V2")
                        || text.contains("UltimateDonutSmp V2")
                        || text.contains("Donut SMP")
        );
        assertFalse(text.contains("Economy SMP"));
    }

    @Test
    void categoryButtonsWithItemTagsRenderAsSprites() {
        List<Map<?, ?>> categories = dialog.getMapList("SETTINGS_DIALOG.CATEGORIES");
        assertFalse(categories.isEmpty());

        for (Map<?, ?> category : categories) {
            String label = String.valueOf(category.get("LABEL"));
            if (label.contains("<item:")) {
                net.kyori.adventure.text.Component component = DialogText.of(label, null);
                String json = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serialize(component);
                assertTrue(json.contains("\"atlas\":\"minecraft:items\""), "expected items atlas in json: " + json);
                assertTrue(json.contains("\"sprite\":\"minecraft:item/"), "expected sprite in json: " + json);
                assertFalse(json.contains("<item:"), "raw <item: tag should not leak into text: " + json);
            }
        }
    }

    @Test
    void dialogTextParsesItemAndSpriteTagsProperly() {
        var comp = DialogText.of("<item:oak_sign> Chat", null);
        String json = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serialize(comp);
        assertTrue(json.contains("\"atlas\":\"minecraft:items\""));
        assertTrue(json.contains("\"sprite\":\"minecraft:item/oak_sign\""));
        assertFalse(json.contains("<item:oak_sign>"));

        var compWithColor = DialogText.of("&e<item:diamond_sword> PvP", null);
        String jsonColor = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serialize(compWithColor);
        assertTrue(jsonColor.contains("\"atlas\":\"minecraft:items\""));
        assertTrue(jsonColor.contains("\"sprite\":\"minecraft:item/diamond_sword\""));
        assertFalse(jsonColor.contains("<item:diamond_sword>"));

        var compBlock = DialogText.of("<sprite:block/stone> Block", null);
        String jsonBlock = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().serialize(compBlock);
        assertTrue(jsonBlock.contains("\"sprite\":\"minecraft:block/stone\""));
    }

    @Test
    void homeEntryIconsPointAtASpriteTheClientCanDraw() {
        // A home icon is stored as a material name, and most of those own no flat item texture, so
        // <item:white_bed> draws nothing. The label has to carry a resolved tag instead.
        String tag = com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog
                .getSpriteTag(org.bukkit.Material.WHITE_BED);
        assertEquals("<item:block/white_wool>", tag);

        String label = DialogConfig.apply(
                dialog.getString("HOMES_DIALOG.ENTRY-FORMAT"),
                DialogConfig.tokens("icon", tag, "name", "Home 1")
        );
        String json = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson()
                .serialize(DialogText.of(label, null));
        assertTrue(json.contains("\"sprite\":\"minecraft:block/white_wool\""), json);
        assertFalse(json.contains("<item:"), "raw tag leaked into the label: " + json);

        // A material this server no longer knows falls back to a format with no tag to fill.
        String plain = dialog.getString("HOMES_DIALOG.ENTRY-FORMAT-NO-ICON");
        assertNotNull(plain);
        assertFalse(plain.contains("%icon%"), "the iconless format still asks for an icon");
    }

    @Test
    void headTagsRenderAsAPlayerHeadWearingTheGivenTexture() {
        String tag = com.bx.ultimateDonutSmp2.dialogs.screens.QuickBuyItemDialog
                .getSpriteTag(org.bukkit.Material.CREEPER_HEAD);
        assertEquals("<head:entity/creeper/creeper>", tag);

        String json = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson()
                .serialize(DialogText.of(tag + " Creeper Head", null));
        assertTrue(json.contains("\"player\":{\"texture\":\"minecraft:entity/creeper/creeper\"}"), json);
        assertFalse(json.contains("<head:"), "raw tag leaked into the label: " + json);
        // The hat layer belongs to another body part on a mob texture, so it has to stay off.
        assertTrue(json.contains("\"hat\":false"), json);
    }

    @Test
    void statsDialogMatchesThePlayerPickerReference() {
        assertEquals("Player Stats", dialog.getString("STATS_SEARCH_DIALOG.TITLE"));
        assertEquals("Click a player to view their stats", dialog.getString("STATS_SEARCH_DIALOG.DESCRIPTION"));
        assertEquals(2, dialog.getInt("STATS_SEARCH_DIALOG.COLUMNS"));
        assertEquals("NONE", dialog.getString("STATS_SEARCH_DIALOG.AFTER-ACTION"));
        assertEquals("NONE", dialog.getString("ADD_STATS_PLAYER_DIALOG.AFTER-ACTION"));
        assertEquals("NONE", dialog.getString("PLAYER_STATS_DIALOG.AFTER-ACTION"));
        assertEquals(100, dialog.getInt("STATS_SEARCH_DIALOG.BUTTON-WIDTH"));
        assertEquals(200, dialog.getInt("STATS_SEARCH_DIALOG.ADD-BUTTON-WIDTH"));
        assertEquals(5, dialog.getInt("STATS_SEARCH_DIALOG.LIST-SIZE"));
        assertEquals("+ Add Player to List", dialog.getString("STATS_SEARCH_DIALOG.ADD-BUTTON-LABEL"));
        assertEquals("You", dialog.getString("STATS_SEARCH_DIALOG.YOU-TOOLTIP"));
        assertEquals("Recommended", dialog.getString("STATS_SEARCH_DIALOG.RECOMMENDED-TOOLTIP"));
        assertTrue(dialog.getString("STATS_SEARCH_DIALOG.REQUEST-TOOLTIP").contains("%time%"));
        assertTrue(dialog.getString("STATS_SEARCH_DIALOG.BACK-LABEL") == null
                || dialog.getString("STATS_SEARCH_DIALOG.BACK-LABEL").isBlank());

        assertEquals("Type a name to add to your pay list", dialog.getString("ADD_STATS_PLAYER_DIALOG.DESCRIPTION"));
        assertEquals(250, dialog.getInt("ADD_STATS_PLAYER_DIALOG.BUTTON-WIDTH"));
        String addLabel = String.valueOf(dialog.getMapList("ADD_STATS_PLAYER_DIALOG.BUTTONS").get(0).get("LABEL"));
        assertTrue(addLabel.contains("Add to List"), addLabel);
        assertTrue(addLabel.contains("00FC00"), "the add button is green in the reference: " + addLabel);

        List<String> lines = dialog.getStringList("PLAYER_STATS_DIALOG.STATS-FORMAT");
        assertEquals(4, lines.size(), "the summary sheet only shows money, kills, deaths and playtime");
        assertEquals(1, dialog.getInt("PLAYER_STATS_DIALOG.COLUMNS"));
        assertEquals(200, dialog.getInt("PLAYER_STATS_DIALOG.BUTTON-WIDTH"));
        assertEquals("LIME_CONCRETE", dialog.getString("PLAYER_STATS_DIALOG.ONLINE-ITEM"));
        assertEquals("GRAY_CONCRETE", dialog.getString("PLAYER_STATS_DIALOG.OFFLINE-ITEM"));

        String viewFull = String.valueOf(dialog.getMapList("PLAYER_STATS_DIALOG.BUTTONS").get(0).get("ACTION"));
        assertTrue(viewFull.contains("stats_view_full_%player_uuid%"), viewFull);
    }

    @Test
    void theMainMenuDoesNotCloseItselfBeforeTheNextScreenArrives() {
        // CLOSE drops the client back to the world between screens, and Minecraft warps the mouse
        // to the middle of the window whenever a GUI opens from the world.
        assertEquals("NONE", dialog.getString("DONUT_SMP_DIALOG.AFTER-ACTION"));
    }

    @Test
    void homesDialogHasCorrectTiersAndDimensions() {
        assertEquals(4, dialog.getInt("HOMES_DIALOG.COLUMNS"));
        assertEquals(6, dialog.getInt("HOMES_DIALOG.EXPANDED-COLUMNS"));
        assertEquals(65, dialog.getInt("HOMES_DIALOG.BUTTON-WIDTH"));
        assertEquals(List.of(3, 9, 27, 90), dialog.getIntegerList("HOMES_DIALOG.TIERS"));
        assertEquals(90, dialog.getInt("HOMES_DIALOG.TOTAL-SLOTS"));
        assertEquals("Show More", dialog.getString("HOMES_DIALOG.SHOW-MORE-LABEL"));
    }

    @Test
    void rtpQueueDialogMatchesTheConfirmationLayout() {
        assertEquals("RTP Queue", dialog.getString("RTP_QUEUE_DIALOG.TITLE"));
        assertEquals(
                "Are you sure you want to randomly teleport with another player?",
                dialog.getString("RTP_QUEUE_DIALOG.QUESTION")
        );
        assertEquals(2, dialog.getInt("RTP_QUEUE_DIALOG.COLUMNS"));
        assertEquals(100, dialog.getInt("RTP_QUEUE_DIALOG.BUTTON-WIDTH"));
        assertEquals(200, dialog.getInt("RTP_QUEUE_DIALOG.TEXT-WIDTH"));
        assertEquals("&cNo", dialog.getString("RTP_QUEUE_DIALOG.NO-LABEL"));
        assertEquals("&#00FC00Yes", dialog.getString("RTP_QUEUE_DIALOG.YES-LABEL"));
        assertEquals(
                DialogActions.NAMESPACE + ':' + DialogActions.RTP_QUEUE_DENY,
                dialog.getString("RTP_QUEUE_DIALOG.NO-ACTION")
        );
        assertEquals(
                DialogActions.NAMESPACE + ':' + DialogActions.RTP_QUEUE_ACCEPT,
                dialog.getString("RTP_QUEUE_DIALOG.YES-ACTION")
        );
    }

    @Test
    void payDialogsMatchTheReferenceLayout() {
        assertEquals("Pay", dialog.getString("PAY_DIALOG.TITLE"));
        assertEquals("&7Click a player to pay", dialog.getString("PAY_DIALOG.DESCRIPTION"));
        assertEquals(2, dialog.getInt("PAY_DIALOG.COLUMNS"));
        assertEquals(150, dialog.getInt("PAY_DIALOG.BUTTON-WIDTH"));
        assertEquals(150, dialog.getInt("PAY_DIALOG.ADD-BUTTON-WIDTH"));

        assertEquals("Add a Player", dialog.getString("ADD_PAY_PLAYER_DIALOG.TITLE"));
        assertEquals("&7Type a name to add to your pay list", dialog.getString("ADD_PAY_PLAYER_DIALOG.DESCRIPTION"));
        assertEquals(1, dialog.getInt("ADD_PAY_PLAYER_DIALOG.COLUMNS"));
        assertEquals(250, dialog.getInt("ADD_PAY_PLAYER_DIALOG.BUTTON-WIDTH"));
        List<Map<?, ?>> addInputs = dialog.getMapList("ADD_PAY_PLAYER_DIALOG.INPUTS");
        assertEquals(1, addInputs.size());
        assertEquals(250, addInputs.get(0).get("WIDTH"));

        assertEquals("Pay %target%", dialog.getString("PAY_AMOUNTS_DIALOG.TITLE"));
        assertEquals("&7Choose an amount to pay", dialog.getString("PAY_AMOUNTS_DIALOG.DESCRIPTION"));
        assertEquals(2, dialog.getInt("PAY_AMOUNTS_DIALOG.COLUMNS"));
        assertEquals(150, dialog.getInt("PAY_AMOUNTS_DIALOG.BUTTON-WIDTH"));
        assertEquals("&7Back", dialog.getString("PAY_AMOUNTS_DIALOG.BACK-LABEL"));
        List<Map<?, ?>> amounts = dialog.getMapList("PAY_AMOUNTS_DIALOG.AMOUNTS");
        assertEquals(7, amounts.size());

        assertEquals("Pay %target%", dialog.getString("PAY_CUSTOM_DIALOG.TITLE"));
        assertEquals("&7Type the amount to pay %target%", dialog.getString("PAY_CUSTOM_DIALOG.DESCRIPTION"));
        assertEquals(1, dialog.getInt("PAY_CUSTOM_DIALOG.COLUMNS"));
        assertEquals(250, dialog.getInt("PAY_CUSTOM_DIALOG.BUTTON-WIDTH"));
        List<Map<?, ?>> customInputs = dialog.getMapList("PAY_CUSTOM_DIALOG.INPUTS");
        assertEquals(1, customInputs.size());
        assertEquals(250, customInputs.get(0).get("WIDTH"));
        List<Map<?, ?>> customButtons = dialog.getMapList("PAY_CUSTOM_DIALOG.BUTTONS");
        assertEquals(2, customButtons.size());
        assertEquals("&aContinue", customButtons.get(0).get("LABEL"));
        assertEquals("&7Back", customButtons.get(1).get("LABEL"));

        assertEquals("Are you sure you want to pay %target%?", dialog.getString("PAY_CONFIRM_DIALOG.TITLE"));
        assertEquals(2, dialog.getInt("PAY_CONFIRM_DIALOG.COLUMNS"));
        assertEquals(150, dialog.getInt("PAY_CONFIRM_DIALOG.BUTTON-WIDTH"));
    }
}
