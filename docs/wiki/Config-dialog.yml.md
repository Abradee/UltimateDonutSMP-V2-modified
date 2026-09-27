# `dialog.yml`

Configures the native dialog screens — the server-driven UI introduced in Minecraft 1.21.6,
used here for the pause-screen entries, the pay flow, homes, stats, teleport and the rest
of the `*_DIALOG` screens listed below.

This is a client-capability feature. `DialogManager` only initialises when the running
server exposes the Paper dialog API; on older servers, or on Spigot, the plugin quietly
falls back to the chest-based menus in `menus.yml`. Setting `ENABLED` to `false` forces
that fallback everywhere.

That fallback is **server-wide**, not per player. A current Paper server still sends native
dialogs to everyone. Players on **Minecraft 1.21.5 or older** (ViaVersion / ViaBackwards)
cannot render those screens — menus look broken, and commands such as `/ah` will not present
a usable UI. Mojang added the Dialog API in 1.21.6; the old client has no renderer, so this
cannot be patched in the plugin. Require Java clients 1.21.6+ and block older protocol
versions on the proxy. Full write-up: [Dialog API & older clients](Dialog-API-and-Older-Clients).

`PAUSE-SCREEN` is written out as a datapack during `onLoad()`, before worlds load, so
changes there need a full server restart rather than a reload.

Comment at the top of the shipped file:

```text
UltimateDonutSMP V2 - Dialog Configuration

Dialogs are the client-side menus introduced in Minecraft 1.21.6. They need a
Paper server (or a Paper fork) running 1.21.6 or newer. On Spigot, or on an
older server, every screen below silently falls back to the chest menus in
menus.yml, so this file is safe to ship either way.

BUTTONS take one of:
  ACTION:  "ultimatedonutsmp2:<id>"  a screen or toggle handled by the plugin
  COMMAND: "auctionhouse"            closes the dialog, then runs the command
ACTION wins when both are present. The namespace must stay lowercase; only
a-z, 0-9, '.', '_' and '-' are legal in a dialog action id.

A COMMAND that reads one of this dialog's own inputs — "msg $(player_name) hi"
— is handed to the client as a command template instead, and the dialog stays
open. Anything without a $(...) in it is dispatched by the plugin after the
dialog closes, so a command that opens no screen of its own does not leave the
menu sitting over the world.

WIDTH values are in GUI pixels and are clamped to 1..1024 by the client.
COLUMNS controls how many buttons sit side by side.
```

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/dialog.yml` |
| **Player-facing text** | Edit `DIALOG` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`ENABLED`](#section-enabled) | boolean | `true` |
| [`PAUSE-SCREEN`](#section-pause-screen) | section | 3 keys |
| [`SCREENS`](#section-screens) | section | 10 keys |
| [`DONUT_SMP_DIALOG`](#section-donut-smp-dialog) | section | 8 keys |
| [`SETTINGS_DIALOG`](#section-settings-dialog) | section | 15 keys |
| [`PAY_DIALOG`](#section-pay-dialog) | section | 10 keys |
| [`ADD_PAY_PLAYER_DIALOG`](#section-add-pay-player-dialog) | section | 7 keys |
| [`PAY_AMOUNTS_DIALOG`](#section-pay-amounts-dialog) | section | 8 keys |
| [`PAY_CUSTOM_DIALOG`](#section-pay-custom-dialog) | section | 7 keys |
| [`PAY_CONFIRM_DIALOG`](#section-pay-confirm-dialog) | section | 9 keys |
| [`LEADERBOARD_MENU_DIALOG`](#section-leaderboard-menu-dialog) | section | 6 keys |
| [`LEADERBOARD_DETAIL_DIALOG`](#section-leaderboard-detail-dialog) | section | 10 keys |
| [`HOMES_DIALOG`](#section-homes-dialog) | section | 20 keys |
| [`CREATE_HOME_DIALOG`](#section-create-home-dialog) | section | 5 keys |
| [`HOME_MANAGE_DIALOG`](#section-home-manage-dialog) | section | 5 keys |
| [`CHANGE_HOME_ICON_DIALOG`](#section-change-home-icon-dialog) | section | 10 keys |
| [`RENAME_HOME_DIALOG`](#section-rename-home-dialog) | section | 5 keys |
| [`DELETE_HOME_CONFIRM_DIALOG`](#section-delete-home-confirm-dialog) | section | 6 keys |
| [`STATS_SEARCH_DIALOG`](#section-stats-search-dialog) | section | 15 keys |
| [`ADD_STATS_PLAYER_DIALOG`](#section-add-stats-player-dialog) | section | 7 keys |
| [`PLAYER_STATS_DIALOG`](#section-player-stats-dialog) | section | 9 keys |
| [`TELEPORT_DIALOG`](#section-teleport-dialog) | section | 10 keys |
| [`ADD_TELEPORT_PLAYER_DIALOG`](#section-add-teleport-player-dialog) | section | 5 keys |
| [`TELEPORT_CONFIRM_DIALOG`](#section-teleport-confirm-dialog) | section | 7 keys |
| [`TELEPORT_REQUEST_DIALOG`](#section-teleport-request-dialog) | section | 5 keys |
| [`FRIENDS_MENU_DIALOG`](#section-friends-menu-dialog) | section | 26 keys |
| [`FRIENDS_SEARCH_DIALOG`](#section-friends-search-dialog) | section | 7 keys |
| [`FOLLOW_PROMPT_DIALOG`](#section-follow-prompt-dialog) | section | 7 keys |
| [`FOLLOW_SEARCH_RESULTS_DIALOG`](#section-follow-search-results-dialog) | section | 9 keys |
| [`FRIEND_DETAILS_DIALOG`](#section-friend-details-dialog) | section | 20 keys |
| [`FRIEND_SETTINGS_DIALOG`](#section-friend-settings-dialog) | section | 15 keys |
| [`RTP_QUEUE_DIALOG`](#section-rtp-queue-dialog) | section | 14 keys |

---

## Section: `ENABLED`

Master switch. false keeps every command on the old chest menus even on Paper.

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ENABLED` | `boolean` | `true`, `false` | `true` | Master switch. false keeps every command on the old chest menus even on Paper. |

<details>
<summary>Default <code>ENABLED</code> block as shipped</summary>

```yaml
# Master switch. false keeps every command on the old chest menus even on Paper.
ENABLED: true
```

</details>

---

## Section: `PAUSE-SCREEN`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAUSE-SCREEN.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `PAUSE-SCREEN` section on or off. |
| `PAUSE-SCREEN.KEY` | `string` | Any text | `ultimatedonutsmp2:main_menu` | Key. |
| `PAUSE-SCREEN.PACK-FORMAT` | `integer` | Any integer | `80` | Only read by a loader that ignores supported_formats. Raise it if the server ever logs the generated pack as incompatible. |

<details>
<summary>Default <code>PAUSE-SCREEN</code> block as shipped</summary>

```yaml
# The ESC screen. The vanilla #minecraft:pause_screen_additions tag lives in a
# frozen registry, so only a datapack can add to it. With this on, the plugin
# writes a small datapack into the main world folder mirroring DONUT_SMP_DIALOG,
# and the button appears after the next restart (or /datapack reload). Turning it
# off deletes the generated pack again.
#
# Only the button labels are a snapshot: every button is a click that lands back
# in the plugin, so what it opens always follows the live config below.
PAUSE-SCREEN:
  ENABLED: true
  KEY: "ultimatedonutsmp2:main_menu"
  # Only read by a loader that ignores supported_formats. Raise it if the server
  # ever logs the generated pack as incompatible.
  PACK-FORMAT: 80
```

</details>

---

## Section: `SCREENS`

Per-screen overrides. Set one to false to force that command back to its chest menu while leaving the rest of the dialog interface alone.

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SCREENS.MAIN` | `boolean` | `true`, `false` | `true` | On/off for main. |
| `SCREENS.SETTINGS` | `boolean` | `true`, `false` | `true` | On/off for settings. |
| `SCREENS.PAY` | `boolean` | `true`, `false` | `true` | On/off for pay. |
| `SCREENS.STATS` | `boolean` | `true`, `false` | `true` | On/off for stats. |
| `SCREENS.LEADERBOARDS` | `boolean` | `true`, `false` | `true` | On/off for leaderboards. |
| `SCREENS.HOMES` | `boolean` | `true`, `false` | `true` | On/off for homes. |
| `SCREENS.FRIENDS` | `boolean` | `true`, `false` | `true` | On/off for friends. |
| `SCREENS.TELEPORT` | `boolean` | `true`, `false` | `true` | On/off for teleport. |
| `SCREENS.RTP-QUEUE` | `boolean` | `true`, `false` | `true` | On/off for rtp queue. |
| `SCREENS.AMETHYST` | `boolean` | `true`, `false` | `true` | On/off for the `/amethysttool` Dialog API give flow. |

<details>
<summary>Default <code>SCREENS</code> block as shipped</summary>

```yaml
# Per-screen overrides. Set one to false to force that command back to its chest
# menu while leaving the rest of the dialog interface alone.
SCREENS:
  MAIN: true
  SETTINGS: true
  PAY: true
  STATS: true
  LEADERBOARDS: true
  HOMES: true
  FRIENDS: true
  TELEPORT: true
  RTP-QUEUE: true
  AMETHYST: true
```

</details>

---

## Section: `DONUT_SMP_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DONUT_SMP_DIALOG.TITLE` | `string` | Any text | `UltimateDonutSmp V2` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `DONUT_SMP_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `UltimateDonutSmp V2` | External title. |
| `DONUT_SMP_DIALOG.CAN-CLOSE-WITH-ESCAPE` | `boolean` | `true`, `false` | `true` | On/off for can close with escape. |
| `DONUT_SMP_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | Keep this on NONE. With CLOSE the client drops back to the world the moment a button is pressed and only then receives the next screen, and Minecraft warps the mouse to the middle of the window every time a GUI opens from the world -- so every click threw the cursor to the centre. A button that opens nothing is closed by the plugin itself, so nothing needs CLOSE. |
| `DONUT_SMP_DIALOG.PAUSE` | `boolean` | `true`, `false` | `false` | On/off for pause. |
| `DONUT_SMP_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `DONUT_SMP_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `DONUT_SMP_DIALOG.BUTTONS` | `list` | A list of values | _list of 14 items_ | The buttons list. |

<details>
<summary>Default contents of <code>DONUT_SMP_DIALOG.BUTTONS</code> (14 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:homes', LABEL: Homes}
  - {COMMAND: rtp, LABEL: RTP}
  - {COMMAND: ah, LABEL: Auction}
  - {ACTION: 'ultimatedonutsmp2:rtp_queue', LABEL: RTP Queue}
  - {COMMAND: shop, LABEL: Quick Buy}
  - {ACTION: 'ultimatedonutsmp2:friends', LABEL: Friends}
  - {ACTION: 'ultimatedonutsmp2:orders', LABEL: Orders}
  - {COMMAND: shardshop, LABEL: Shard Shop}
  - {COMMAND: sell, LABEL: Sell}
  - {ACTION: 'ultimatedonutsmp2:pay_menu', LABEL: Pay}
  - {ACTION: 'ultimatedonutsmp2:tpa', LABEL: Teleport}
  - {ACTION: 'ultimatedonutsmp2:stats_menu', LABEL: Stats}
  - {ACTION: 'ultimatedonutsmp2:leaderboards_menu', LABEL: Leaderboards}
  - {ACTION: 'ultimatedonutsmp2:settings', LABEL: Settings}
```

</details>

<details>
<summary>Default <code>DONUT_SMP_DIALOG</code> block as shipped</summary>

```yaml
DONUT_SMP_DIALOG:
  TITLE: "UltimateDonutSmp V2"
  EXTERNAL-TITLE: "UltimateDonutSmp V2"
  CAN-CLOSE-WITH-ESCAPE: true
  # Keep this on NONE. With CLOSE the client drops back to the world the moment a button is
  # pressed and only then receives the next screen, and Minecraft warps the mouse to the middle
  # of the window every time a GUI opens from the world -- so every click threw the cursor to the
  # centre. A button that opens nothing is closed by the plugin itself, so nothing needs CLOSE.
  AFTER-ACTION: NONE
  PAUSE: false
  COLUMNS: 2
  BUTTON-WIDTH: 150
  BUTTONS:
    - LABEL: "Homes"
      ACTION: "ultimatedonutsmp2:homes"
    - LABEL: "RTP"
      COMMAND: "rtp"
    - LABEL: "Auction"
      COMMAND: "ah"
    - LABEL: "RTP Queue"
      ACTION: "ultimatedonutsmp2:rtp_queue"
    - LABEL: "Quick Buy"
      COMMAND: "shop"
    - LABEL: "Friends"
      ACTION: "ultimatedonutsmp2:friends"
    - LABEL: "Orders"
      ACTION: "ultimatedonutsmp2:orders"
    - LABEL: "Shard Shop"
      COMMAND: "shardshop"
    - LABEL: "Sell"
      COMMAND: "sell"
    - LABEL: "Pay"
      ACTION: "ultimatedonutsmp2:pay_menu"
    - LABEL: "Teleport"
      ACTION: "ultimatedonutsmp2:tpa"
    - LABEL: "Stats"
      ACTION: "ultimatedonutsmp2:stats_menu"
    - LABEL: "Leaderboards"
      ACTION: "ultimatedonutsmp2:leaderboards_menu"
    - LABEL: "Settings"
      ACTION: "ultimatedonutsmp2:settings"
```

</details>

---

## Section: `SETTINGS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS_DIALOG.TITLE` | `string` | Any text | `Settings` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SETTINGS_DIALOG.TEXT` | `string` | Any text | `Choose a category to change your UltimateDonutSMP V2 sett…` | Text. |
| `SETTINGS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Settings` | External title. |
| `SETTINGS_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `SETTINGS_DIALOG.ITEM` | `string` | Any text | `''` | Item. |
| `SETTINGS_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `SETTINGS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `250` | Button width. |
| `SETTINGS_DIALOG.CATEGORIES-COLUMNS` | `integer` | Any integer | `2` | Categories columns. |
| `SETTINGS_DIALOG.CATEGORIES-BUTTON-WIDTH` | `integer` | Any integer | `150` | Categories button width. |
| `SETTINGS_DIALOG.CATEGORY-TITLE` | `string` | Any text | `Settings - %category%` | Category title. |
| `SETTINGS_DIALOG.BACK-LABEL` | `string` | Any text | `''` | Back label. |
| `SETTINGS_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:main_menu` | Back action. |
| `SETTINGS_DIALOG.CATEGORY-BACK-LABEL` | `string` | Any text | `Back` | Category back label. |
| `SETTINGS_DIALOG.CATEGORY-BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:settings` | Category back action. |
| `SETTINGS_DIALOG.CATEGORIES` | `list` | A list of values | _list of 7 items_ | The categories list. |

<details>
<summary>Default contents of <code>SETTINGS_DIALOG.CATEGORIES</code> (7 items)</summary>

```yaml
CATEGORIES:
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_publicchat', LABEL: 'Public Chat: %ultimatedonutsmp2_setting_publicchat%'},
    {ACTION: 'ultimatedonutsmp2:toggle_privatemessages', LABEL: 'Private Messages:
        %ultimatedonutsmp2_setting_privatemessages%'}, {ACTION: 'ultimatedonutsmp2:toggle_servermessages',
      LABEL: 'Server Chat Messages: %ultimatedonutsmp2_setting_servermessages%'},
    {ACTION: 'ultimatedonutsmp2:toggle_hotbarmessages', LABEL: 'Server Hotbar Messages:
        %ultimatedonutsmp2_setting_hotbarmessages%'}, {ACTION: 'ultimatedonutsmp2:toggle_deathmessages',
      LABEL: 'Death Messages: %ultimatedonutsmp2_setting_deathmessages%'}, {ACTION: 'ultimatedonutsmp2:toggle_advancementmessages',
      LABEL: 'Advancement Messages: %ultimatedonutsmp2_setting_advancementmessages%'},
    {ACTION: 'ultimatedonutsmp2:toggle_joinleavemessages', LABEL: 'Join/Leave Messages:
        %ultimatedonutsmp2_setting_joinleavemessages%'}], ID: chat, LABEL: '<item:oak_sign>
    Chat', NAME: Chat}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_payalerts', LABEL: 'Pay Alerts: %ultimatedonutsmp2_setting_payalerts%'},
    {ACTION: 'ultimatedonutsmp2:toggle_tpaalerts', LABEL: 'Teleport Alerts: %ultimatedonutsmp2_setting_tpaalerts%'},
    {ACTION: 'ultimatedonutsmp2:toggle_bountyalerts', LABEL: 'Bounty Alerts: %ultimatedonutsmp2_setting_bountyalerts%'},
    {ACTION: 'ultimatedonutsmp2:toggle_auctionalerts', LABEL: 'Auction Alerts: %ultimatedonutsmp2_setting_auctionalerts%'},
    {ACTION: 'ultimatedonutsmp2:toggle_orderalerts', LABEL: 'Order Alerts: %ultimatedonutsmp2_setting_orderalerts%'},
    {ACTION: 'ultimatedonutsmp2:toggle_serversounds', LABEL: 'Server Sounds: %ultimatedonutsmp2_setting_serversounds%'},
    {ACTION: 'ultimatedonutsmp2:toggle_followalerts', LABEL: 'Follow Alerts: %ultimatedonutsmp2_setting_followalerts%'}],
  ID: notifications, LABEL: '<item:bell> Notifications', NAME: Notifications}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_fastcrystals', LABEL: 'Fast Crystals:
        %ultimatedonutsmp2_setting_fastcrystals%'}, {ACTION: 'ultimatedonutsmp2:toggle_totemparticles',
      LABEL: 'Totem Particles: %ultimatedonutsmp2_setting_totemparticles%'}, {ACTION: 'ultimatedonutsmp2:toggle_explosionparticles',
      LABEL: 'Explosion Particles: %ultimatedonutsmp2_setting_explosionparticles%'},
    {ACTION: 'ultimatedonutsmp2:toggle_explosionsounds', LABEL: 'Explosion Sounds:
        %ultimatedonutsmp2_setting_explosionsounds%'}, {ACTION: 'ultimatedonutsmp2:toggle_combattimer',
      LABEL: 'Combat Timer: %ultimatedonutsmp2_setting_combattimer%'}], ID: pvp, LABEL: '<item:diamond_sword>
    PvP', NAME: PvP}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_displaydonutplus', LABEL: 'Display Donut+:
        %ultimatedonutsmp2_setting_displaydonutplus%'}, {ACTION: 'ultimatedonutsmp2:toggle_moneynametags',
      LABEL: 'Money Nametags: %ultimatedonutsmp2_setting_moneynametags%'}, {ACTION: 'ultimatedonutsmp2:toggle_worthdisplay',
      LABEL: 'Item Worth Lore: %ultimatedonutsmp2_setting_worthdisplay%'}, {ACTION: 'ultimatedonutsmp2:toggle_tpaconfirm',
      LABEL: 'Teleport Confirm Menus: %ultimatedonutsmp2_setting_tpaconfirm%'}], ID: visuals,
  LABEL: '<item:spyglass> Visuals', NAME: Visuals}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_tparequests', LABEL: 'Teleport Requests:
        %ultimatedonutsmp2_setting_tparequests%'}, {ACTION: 'ultimatedonutsmp2:toggle_tpahere',
      LABEL: 'Teleport-Here Requests: %ultimatedonutsmp2_setting_tpahere%'}, {ACTION: 'ultimatedonutsmp2:toggle_payments',
      LABEL: 'Allow Payments: %ultimatedonutsmp2_setting_payments%'}, {ACTION: 'ultimatedonutsmp2:toggle_randomizedcoords',
      LABEL: 'Randomized Coords: %ultimatedonutsmp2_setting_randomizedcoords%'}, {
      ACTION: 'ultimatedonutsmp2:toggle_privatetransactions', LABEL: 'Private Transactions:
        %ultimatedonutsmp2_setting_privatetransactions%'}], ID: privacy, LABEL: '<item:barrier>
    Privacy', NAME: Privacy}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_scoreboard', LABEL: 'Scoreboard: %ultimatedonutsmp2_setting_scoreboard%'},
    {ACTION: 'ultimatedonutsmp2:toggle_showmoney', LABEL: 'Show Money: %ultimatedonutsmp2_setting_showmoney%'},
    {ACTION: 'ultimatedonutsmp2:toggle_showshards', LABEL: 'Show Shards: %ultimatedonutsmp2_setting_showshards%'},
    {ACTION: 'ultimatedonutsmp2:toggle_showkills', LABEL: 'Show Kills: %ultimatedonutsmp2_setting_showkills%'},
    {ACTION: 'ultimatedonutsmp2:toggle_showdeaths', LABEL: 'Show Deaths: %ultimatedonutsmp2_setting_showdeaths%'},
    {ACTION: 'ultimatedonutsmp2:toggle_showplaytime', LABEL: 'Show Playtime: %ultimatedonutsmp2_setting_showplaytime%'}],
  ID: scoreboard, LABEL: '<item:paper> Scoreboard', NAME: Scoreboard}
  - {BUTTONS: [{ACTION: 'ultimatedonutsmp2:toggle_auctionquickbuy', LABEL: 'Auction Quick
        Buy: %ultimatedonutsmp2_setting_auctionquickbuy%'}, {ACTION: 'ultimatedonutsmp2:toggle_auctionquicksell',
      LABEL: 'Auction Quick Sell: %ultimatedonutsmp2_setting_auctionquicksell%'},
    {ACTION: 'ultimatedonutsmp2:toggle_mobspawns', LABEL: 'Mob Spawns: %ultimatedonutsmp2_setting_mobspawns%'},
    {ACTION: 'ultimatedonutsmp2:toggle_phantomspawning', LABEL: 'Phantom Spawning:
        %ultimatedonutsmp2_setting_phantomspawning%'}, {ACTION: 'ultimatedonutsmp2:toggle_nightvision',
      LABEL: 'Night Vision: %ultimatedonutsmp2_setting_nightvision%'}, {ACTION: 'ultimatedonutsmp2:toggle_destroypearlondeath',
      LABEL: 'Destroy Pearl on Death: %ultimatedonutsmp2_setting_destroypearlondeath%'}],
  ID: general, LABEL: '<item:paper> General', NAME: General}
```

</details>

<details>
<summary>Default <code>SETTINGS_DIALOG</code> block as shipped</summary>

```yaml
SETTINGS_DIALOG:
  TITLE: "Settings"
  TEXT: "Choose a category to change your UltimateDonutSMP V2 settings"
  EXTERNAL-TITLE: "Settings"
  AFTER-ACTION: NONE
  ITEM: ""
  COLUMNS: 1
  BUTTON-WIDTH: 250
  CATEGORIES-COLUMNS: 2
  CATEGORIES-BUTTON-WIDTH: 150
  CATEGORY-TITLE: "Settings - %category%"
  BACK-LABEL: ""
  BACK-ACTION: "ultimatedonutsmp2:main_menu"
  CATEGORY-BACK-LABEL: "Back"
  CATEGORY-BACK-ACTION: "ultimatedonutsmp2:settings"
  CATEGORIES:
    - ID: "chat"
      NAME: "Chat"
      LABEL: "<item:oak_sign> Chat"
      BUTTONS:
        - LABEL: "Public Chat: %ultimatedonutsmp2_setting_publicchat%"
          ACTION: "ultimatedonutsmp2:toggle_publicchat"
        - LABEL: "Private Messages: %ultimatedonutsmp2_setting_privatemessages%"
          ACTION: "ultimatedonutsmp2:toggle_privatemessages"
        - LABEL: "Server Chat Messages: %ultimatedonutsmp2_setting_servermessages%"
          ACTION: "ultimatedonutsmp2:toggle_servermessages"
        - LABEL: "Server Hotbar Messages: %ultimatedonutsmp2_setting_hotbarmessages%"
          ACTION: "ultimatedonutsmp2:toggle_hotbarmessages"
        - LABEL: "Death Messages: %ultimatedonutsmp2_setting_deathmessages%"
          ACTION: "ultimatedonutsmp2:toggle_deathmessages"
        - LABEL: "Advancement Messages: %ultimatedonutsmp2_setting_advancementmessages%"
          ACTION: "ultimatedonutsmp2:toggle_advancementmessages"
        - LABEL: "Join/Leave Messages: %ultimatedonutsmp2_setting_joinleavemessages%"
          ACTION: "ultimatedonutsmp2:toggle_joinleavemessages"
    - ID: "notifications"
      NAME: "Notifications"
      LABEL: "<item:bell> Notifications"
      BUTTONS:
        - LABEL: "Pay Alerts: %ultimatedonutsmp2_setting_payalerts%"
          ACTION: "ultimatedonutsmp2:toggle_payalerts"
        - LABEL: "Teleport Alerts: %ultimatedonutsmp2_setting_tpaalerts%"
          ACTION: "ultimatedonutsmp2:toggle_tpaalerts"
        - LABEL: "Bounty Alerts: %ultimatedonutsmp2_setting_bountyalerts%"
          ACTION: "ultimatedonutsmp2:toggle_bountyalerts"
        - LABEL: "Auction Alerts: %ultimatedonutsmp2_setting_auctionalerts%"
          ACTION: "ultimatedonutsmp2:toggle_auctionalerts"
        - LABEL: "Order Alerts: %ultimatedonutsmp2_setting_orderalerts%"
          ACTION: "ultimatedonutsmp2:toggle_orderalerts"
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `PAY_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY_DIALOG.TITLE` | `string` | Any text | `Pay` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Pay` | External title. |
| `PAY_DIALOG.DESCRIPTION` | `string` | Any text | `&7Click a player to pay` | Description. |
| `PAY_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `PAY_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `PAY_DIALOG.ADD-BUTTON-LABEL` | `string` | Any text | `&7+ Add Player to List` | Add button label. |
| `PAY_DIALOG.ADD-BUTTON-WIDTH` | `integer` | Any integer | `150` | Add button width. |
| `PAY_DIALOG.BACK-LABEL` | `string` | Any text | `''` | Back label. |
| `PAY_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:main_menu` | Back action. |
| `PAY_DIALOG.EMPTY-TEXT` | `string` | Any text | `&7Nobody on your list yet.` | Empty text. |

<details>
<summary>Default <code>PAY_DIALOG</code> block as shipped</summary>

```yaml
PAY_DIALOG:
  TITLE: "Pay"
  EXTERNAL-TITLE: "Pay"
  DESCRIPTION: "&7Click a player to pay"
  COLUMNS: 2
  BUTTON-WIDTH: 150
  ADD-BUTTON-LABEL: "&7+ Add Player to List"
  ADD-BUTTON-WIDTH: 150
  BACK-LABEL: ""
  BACK-ACTION: "ultimatedonutsmp2:main_menu"
  EMPTY-TEXT: "&7Nobody on your list yet."
```

</details>

---

## Section: `ADD_PAY_PLAYER_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ADD_PAY_PLAYER_DIALOG.TITLE` | `string` | Any text | `Add a Player` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `ADD_PAY_PLAYER_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Add a Player` | External title. |
| `ADD_PAY_PLAYER_DIALOG.DESCRIPTION` | `string` | Any text | `&7Type a name to add to your pay list` | Description. |
| `ADD_PAY_PLAYER_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `ADD_PAY_PLAYER_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `250` | Button width. |
| `ADD_PAY_PLAYER_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `ADD_PAY_PLAYER_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>ADD_PAY_PLAYER_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 250}
```

</details>

<details>
<summary>Default contents of <code>ADD_PAY_PLAYER_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_add_pay_player', LABEL: '&aAdd to List'}
  - {ACTION: 'ultimatedonutsmp2:pay_menu', LABEL: '&7Back'}
```

</details>

<details>
<summary>Default <code>ADD_PAY_PLAYER_DIALOG</code> block as shipped</summary>

```yaml
ADD_PAY_PLAYER_DIALOG:
  TITLE: "Add a Player"
  EXTERNAL-TITLE: "Add a Player"
  DESCRIPTION: "&7Type a name to add to your pay list"
  COLUMNS: 1
  BUTTON-WIDTH: 250
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 250
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&aAdd to List"
      ACTION: "ultimatedonutsmp2:execute_add_pay_player"
    - LABEL: "&7Back"
      ACTION: "ultimatedonutsmp2:pay_menu"
```

</details>

---

## Section: `PAY_AMOUNTS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY_AMOUNTS_DIALOG.TITLE` | `string` | Any text | `Pay %target%` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY_AMOUNTS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Pay %target%` | External title. |
| `PAY_AMOUNTS_DIALOG.DESCRIPTION` | `string` | Any text | `&7Choose an amount to pay` | Description. |
| `PAY_AMOUNTS_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `PAY_AMOUNTS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `PAY_AMOUNTS_DIALOG.BACK-LABEL` | `string` | Any text | `&7Back` | Back label. |
| `PAY_AMOUNTS_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:pay_menu` | Back action. |
| `PAY_AMOUNTS_DIALOG.AMOUNTS` | `list` | A list of values | _list of 7 items_ | The amounts list. |

<details>
<summary>Default contents of <code>PAY_AMOUNTS_DIALOG.AMOUNTS</code> (7 items)</summary>

```yaml
AMOUNTS:
  - {AMOUNT: 100, LABEL: '&#00FC00$ &f100'}
  - {AMOUNT: 250, LABEL: '&#00FC00$ &f250'}
  - {AMOUNT: 1000, LABEL: '&#00FC00$ &f1K'}
  - {AMOUNT: 1300, LABEL: '&#00FC00$ &f1.3K'}
  - {AMOUNT: 2500, LABEL: '&#00FC00$ &f2.5K'}
  - {AMOUNT: 10000, LABEL: '&#00FC00$ &f10K'}
  - {ACTION: custom, LABEL: Custom}
```

</details>

<details>
<summary>Default <code>PAY_AMOUNTS_DIALOG</code> block as shipped</summary>

```yaml
PAY_AMOUNTS_DIALOG:
  TITLE: "Pay %target%"
  EXTERNAL-TITLE: "Pay %target%"
  DESCRIPTION: "&7Choose an amount to pay"
  COLUMNS: 2
  BUTTON-WIDTH: 150
  BACK-LABEL: "&7Back"
  BACK-ACTION: "ultimatedonutsmp2:pay_menu"
  AMOUNTS:
    - LABEL: "&#00FC00$ &f100"
      AMOUNT: 100
    - LABEL: "&#00FC00$ &f250"
      AMOUNT: 250
    - LABEL: "&#00FC00$ &f1K"
      AMOUNT: 1000
    - LABEL: "&#00FC00$ &f1.3K"
      AMOUNT: 1300
    - LABEL: "&#00FC00$ &f2.5K"
      AMOUNT: 2500
    - LABEL: "&#00FC00$ &f10K"
      AMOUNT: 10000
    - LABEL: "Custom"
      ACTION: "custom"
```

</details>

---

## Section: `PAY_CUSTOM_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY_CUSTOM_DIALOG.TITLE` | `string` | Any text | `Pay %target%` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY_CUSTOM_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Pay %target%` | External title. |
| `PAY_CUSTOM_DIALOG.DESCRIPTION` | `string` | Any text | `&7Type the amount to pay %target%` | Description. |
| `PAY_CUSTOM_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `PAY_CUSTOM_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `250` | Button width. |
| `PAY_CUSTOM_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `PAY_CUSTOM_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>PAY_CUSTOM_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: amount, LABEL: Amount, MAX-LENGTH: 16, WIDTH: 250}
```

</details>

<details>
<summary>Default contents of <code>PAY_CUSTOM_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:pay_custom_continue_%target_uuid%', LABEL: '&aContinue'}
  - {ACTION: 'ultimatedonutsmp2:pay_target_%target_uuid%', LABEL: '&7Back'}
```

</details>

<details>
<summary>Default <code>PAY_CUSTOM_DIALOG</code> block as shipped</summary>

```yaml
PAY_CUSTOM_DIALOG:
  TITLE: "Pay %target%"
  EXTERNAL-TITLE: "Pay %target%"
  DESCRIPTION: "&7Type the amount to pay %target%"
  COLUMNS: 1
  BUTTON-WIDTH: 250
  INPUTS:
    - ID: "amount"
      LABEL: "Amount"
      WIDTH: 250
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&aContinue"
      ACTION: "ultimatedonutsmp2:pay_custom_continue_%target_uuid%"
    - LABEL: "&7Back"
      ACTION: "ultimatedonutsmp2:pay_target_%target_uuid%"
```

</details>

---

## Section: `PAY_CONFIRM_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY_CONFIRM_DIALOG.TITLE` | `string` | Any text | `Are you sure you want to pay %target%?` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY_CONFIRM_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Are you sure you want to pay %target%?` | External title. |
| `PAY_CONFIRM_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `PAY_CONFIRM_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `PAY_CONFIRM_DIALOG.AMOUNT-LABEL` | `string` | Any text | `&#00FC00$ &f%amount%` | Amount label. |
| `PAY_CONFIRM_DIALOG.NO-LABEL` | `string` | Any text | `&cNo` | No label. |
| `PAY_CONFIRM_DIALOG.NO-ACTION` | `string` | Any text | `ultimatedonutsmp2:pay_target_%target_uuid%` | No action. |
| `PAY_CONFIRM_DIALOG.YES-LABEL` | `string` | Any text | `&#00FC00Yes` | Yes label. |
| `PAY_CONFIRM_DIALOG.YES-ACTION` | `string` | Any text | `ultimatedonutsmp2:pay_execute_%target_uuid%_%raw_amount%` | Yes action. |

<details>
<summary>Default <code>PAY_CONFIRM_DIALOG</code> block as shipped</summary>

```yaml
PAY_CONFIRM_DIALOG:
  TITLE: "Are you sure you want to pay %target%?"
  EXTERNAL-TITLE: "Are you sure you want to pay %target%?"
  COLUMNS: 2
  BUTTON-WIDTH: 150
  AMOUNT-LABEL: "&#00FC00$ &f%amount%"
  NO-LABEL: "&cNo"
  NO-ACTION: "ultimatedonutsmp2:pay_target_%target_uuid%"
  YES-LABEL: "&#00FC00Yes"
  YES-ACTION: "ultimatedonutsmp2:pay_execute_%target_uuid%_%raw_amount%"
```

</details>

---

## Section: `LEADERBOARD_MENU_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARD_MENU_DIALOG.TITLE` | `string` | Any text | `Leaderboards` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `LEADERBOARD_MENU_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Leaderboards` | External title. |
| `LEADERBOARD_MENU_DIALOG.ITEM` | `string` | Any text | `''` | Item. |
| `LEADERBOARD_MENU_DIALOG.COLUMNS` | `integer` | Any integer | `3` | Columns. |
| `LEADERBOARD_MENU_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `LEADERBOARD_MENU_DIALOG.BUTTONS` | `list` | A list of values | _list of 6 items_ | The buttons list. |

<details>
<summary>Default contents of <code>LEADERBOARD_MENU_DIALOG.BUTTONS</code> (6 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:lb_category_money', LABEL: Money}
  - {ACTION: 'ultimatedonutsmp2:lb_category_deaths', LABEL: Deaths}
  - {ACTION: 'ultimatedonutsmp2:lb_category_playtime', LABEL: Playtime}
  - {ACTION: 'ultimatedonutsmp2:lb_category_blocksplaced', LABEL: Blocks Placed}
  - {ACTION: 'ultimatedonutsmp2:lb_category_blocksbroken', LABEL: Blocks Broken}
  - {ACTION: 'ultimatedonutsmp2:lb_category_mobskilled', LABEL: Mobs Killed}
```

</details>

<details>
<summary>Default <code>LEADERBOARD_MENU_DIALOG</code> block as shipped</summary>

```yaml
LEADERBOARD_MENU_DIALOG:
  TITLE: "Leaderboards"
  EXTERNAL-TITLE: "Leaderboards"
  ITEM: ""
  COLUMNS: 3
  BUTTON-WIDTH: 100
  BUTTONS:
    - LABEL: "Money"
      ACTION: "ultimatedonutsmp2:lb_category_money"
    - LABEL: "Deaths"
      ACTION: "ultimatedonutsmp2:lb_category_deaths"
    - LABEL: "Playtime"
      ACTION: "ultimatedonutsmp2:lb_category_playtime"
    - LABEL: "Blocks Placed"
      ACTION: "ultimatedonutsmp2:lb_category_blocksplaced"
    - LABEL: "Blocks Broken"
      ACTION: "ultimatedonutsmp2:lb_category_blocksbroken"
    - LABEL: "Mobs Killed"
      ACTION: "ultimatedonutsmp2:lb_category_mobskilled"
```

</details>

---

## Section: `LEADERBOARD_DETAIL_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARD_DETAIL_DIALOG.TITLE` | `string` | Any text | `Top %category%` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `LEADERBOARD_DETAIL_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Top %category%` | External title. |
| `LEADERBOARD_DETAIL_DIALOG.ENTRY-FORMAT` | `string` | Any text | `%rank_color%#%rank% %skin% &f%player% &7- &#00FC00%value%` | Entry format. |
| `LEADERBOARD_DETAIL_DIALOG.NO-DATA` | `string` | Any text | `&cNo data available.` | No data. |
| `LEADERBOARD_DETAIL_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `LEADERBOARD_DETAIL_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `LEADERBOARD_DETAIL_DIALOG.BUTTONS-10` | `list` | A list of values | _list of 2 items_ | Shown on the top 10 view. |
| `LEADERBOARD_DETAIL_DIALOG.BUTTONS-100` | `list` | A list of values | _list of 1 item_ | Shown on the expanded view. |
| `LEADERBOARD_DETAIL_DIALOG.SHORT-LIMIT` | `integer` | Any integer | `10` | How many rows each view shows. |
| `LEADERBOARD_DETAIL_DIALOG.FULL-LIMIT` | `integer` | Any integer | `200` | Full limit. |

<details>
<summary>Default contents of <code>LEADERBOARD_DETAIL_DIALOG.BUTTONS-10</code> (2 items)</summary>

```yaml
BUTTONS-10:
  - {ACTION: 'ultimatedonutsmp2:leaderboards_menu', LABEL: Back}
  - {ACTION: 'ultimatedonutsmp2:lb_full_%category_lower%', LABEL: View Full Leaderboard}
```

</details>

<details>
<summary>Default contents of <code>LEADERBOARD_DETAIL_DIALOG.BUTTONS-100</code> (1 item)</summary>

```yaml
BUTTONS-100:
  - {ACTION: 'ultimatedonutsmp2:lb_category_%category_lower%', LABEL: Back}
```

</details>

<details>
<summary>Default <code>LEADERBOARD_DETAIL_DIALOG</code> block as shipped</summary>

```yaml
LEADERBOARD_DETAIL_DIALOG:
  TITLE: "Top %category%"
  EXTERNAL-TITLE: "Top %category%"
  ENTRY-FORMAT: "%rank_color%#%rank% %skin% &f%player% &7- &#00FC00%value%"
  NO-DATA: "&cNo data available."
  COLUMNS: 2
  BUTTON-WIDTH: 150
  # Shown on the top 10 view.
  BUTTONS-10:
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:leaderboards_menu"
    - LABEL: "View Full Leaderboard"
      ACTION: "ultimatedonutsmp2:lb_full_%category_lower%"
  # Shown on the expanded view.
  BUTTONS-100:
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:lb_category_%category_lower%"
  # How many rows each view shows.
  SHORT-LIMIT: 10
  FULL-LIMIT: 200
```

</details>

---

## Section: `HOMES_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `HOMES_DIALOG.TITLE` | `string` | Any text | `Homes` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `HOMES_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Homes` | External title. |
| `HOMES_DIALOG.ITEM` | `string` | Any text | `WHITE_BED` | Item. |
| `HOMES_DIALOG.COLUMNS` | `integer` | Any integer | `4` | Columns. |
| `HOMES_DIALOG.EXPANDED-COLUMNS` | `integer` | Any integer | `6` | Expanded columns. |
| `HOMES_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `65` | Button width. |
| `HOMES_DIALOG.ENTRY-FORMAT` | `string` | Any text | `%icon% %name%` | %icon% is the whole icon tag for the home's icon: &lt;item:block/white_wool&gt; for a bed, &lt;head:entity/creeper/creeper&gt; for a creeper head. A material this server no longer knows leaves it blank, and the entry falls back to ENTRY-FORMAT-NO-ICON. |
| `HOMES_DIALOG.ENTRY-FORMAT-NO-ICON` | `string` | Any text | `%name%` | Entry format no icon. |
| `HOMES_DIALOG.HOME-TOOLTIP` | `string` | Any text | `%name% Click to manage` | Home tooltip. |
| `HOMES_DIALOG.NEW-HOME-LABEL` | `string` | Any text | `New Home` | New home label. |
| `HOMES_DIALOG.NEW-HOME-TOOLTIP` | `string` | Any text | `Click to set new home` | New home tooltip. |
| `HOMES_DIALOG.TEAM-HOME-LABEL` | `string` | Any text | `Team Home` | Team home label. |
| `HOMES_DIALOG.LOCKED-LABEL` | `string` | Any text | `&cLocked` | Locked label. |
| `HOMES_DIALOG.LOCKED-TOOLTIP` | `string` | Any text | `Buy Donut&#00A4FC+ &7for more home slots` | Locked tooltip. |
| `HOMES_DIALOG.LOCKED-TOOLTIP-PLUS` | `string` | Any text | `Buy Donut&#00A4FC+ &7for more home slots` | Locked tooltip plus. |
| `HOMES_DIALOG.LOCKED-TOOLTIP-PLUS-PLUS` | `string` | Any text | `Buy Donut&#00A4FC++ &7for more home slots` | Locked tooltip plus plus. |
| `HOMES_DIALOG.LOCKED-TOOLTIP-PLUS-PLUS-PLUS` | `string` | Any text | `Buy Donut&#00A4FC+++ &7for more home slots` | Locked tooltip plus plus plus. |
| `HOMES_DIALOG.SHOW-MORE-LABEL` | `string` | Any text | `Show More` | Show more label. |
| `HOMES_DIALOG.TOTAL-SLOTS` | `integer` | Any integer | `90` | Total slots. |
| `HOMES_DIALOG.TIERS` | `list` | A list of values | _list of 4 items_ | The tiers list. |

<details>
<summary>Default contents of <code>HOMES_DIALOG.TIERS</code> (4 items)</summary>

```yaml
TIERS:
  - 3
  - 9
  - 27
  - 90
```

</details>

<details>
<summary>Default <code>HOMES_DIALOG</code> block as shipped</summary>

```yaml
HOMES_DIALOG:
  TITLE: "Homes"
  EXTERNAL-TITLE: "Homes"
  ITEM: "WHITE_BED"
  COLUMNS: 4
  EXPANDED-COLUMNS: 6
  BUTTON-WIDTH: 65
  # %icon% is the whole icon tag for the home's icon: <item:block/white_wool> for a bed,
  # <head:entity/creeper/creeper> for a creeper head. A material this server no longer knows
  # leaves it blank, and the entry falls back to ENTRY-FORMAT-NO-ICON.
  ENTRY-FORMAT: "%icon% %name%"
  ENTRY-FORMAT-NO-ICON: "%name%"
  HOME-TOOLTIP: "%name%\nClick to manage"
  NEW-HOME-LABEL: "New Home"
  NEW-HOME-TOOLTIP: "Click to set new home"
  TEAM-HOME-LABEL: "Team Home"
  LOCKED-LABEL: "&cLocked"
  LOCKED-TOOLTIP: "Buy Donut&#00A4FC+ &7for more home slots"
  LOCKED-TOOLTIP-PLUS: "Buy Donut&#00A4FC+ &7for more home slots"
  LOCKED-TOOLTIP-PLUS-PLUS: "Buy Donut&#00A4FC++ &7for more home slots"
  LOCKED-TOOLTIP-PLUS-PLUS-PLUS: "Buy Donut&#00A4FC+++ &7for more home slots"
  SHOW-MORE-LABEL: "Show More"
  TOTAL-SLOTS: 90
  TIERS:
    - 3
    - 9
    - 27
    - 90
```

</details>

---

## Section: `CREATE_HOME_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CREATE_HOME_DIALOG.TITLE` | `string` | Any text | `Create Home` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `CREATE_HOME_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Create Home` | External title. |
| `CREATE_HOME_DIALOG.ITEM` | `string` | Any text | `WHITE_BED` | Item. |
| `CREATE_HOME_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `CREATE_HOME_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>CREATE_HOME_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: home_name, LABEL: Home Name, MAX-LENGTH: 16, WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>CREATE_HOME_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_create_home', LABEL: '&#00FC00Save Home'}
  - {ACTION: 'ultimatedonutsmp2:homes_menu', LABEL: Cancel}
```

</details>

<details>
<summary>Default <code>CREATE_HOME_DIALOG</code> block as shipped</summary>

```yaml
CREATE_HOME_DIALOG:
  TITLE: "Create Home"
  EXTERNAL-TITLE: "Create Home"
  ITEM: "WHITE_BED"
  INPUTS:
    - ID: "home_name"
      LABEL: "Home Name"
      WIDTH: 200
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&#00FC00Save Home"
      ACTION: "ultimatedonutsmp2:execute_create_home"
    - LABEL: "Cancel"
      ACTION: "ultimatedonutsmp2:homes_menu"
```

</details>

---

## Section: `HOME_MANAGE_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `HOME_MANAGE_DIALOG.TITLE` | `string` | Any text | `%name%` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `HOME_MANAGE_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `%name%` | External title. |
| `HOME_MANAGE_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `HOME_MANAGE_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `HOME_MANAGE_DIALOG.BUTTONS` | `list` | A list of values | _list of 5 items_ | The buttons list. |

<details>
<summary>Default contents of <code>HOME_MANAGE_DIALOG.BUTTONS</code> (5 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:teleport_home_%slot%', LABEL: Teleport}
  - {ACTION: 'ultimatedonutsmp2:change_icon_prompt_%slot%', LABEL: Change Icon}
  - {ACTION: 'ultimatedonutsmp2:rename_home_prompt_%slot%', LABEL: Rename}
  - {ACTION: 'ultimatedonutsmp2:delete_home_prompt_%slot%', LABEL: '&cDelete'}
  - {ACTION: 'ultimatedonutsmp2:homes_menu', LABEL: Back}
```

</details>

<details>
<summary>Default <code>HOME_MANAGE_DIALOG</code> block as shipped</summary>

```yaml
HOME_MANAGE_DIALOG:
  TITLE: "%name%"
  EXTERNAL-TITLE: "%name%"
  COLUMNS: 2
  BUTTON-WIDTH: 150
  BUTTONS:
    - LABEL: "Teleport"
      ACTION: "ultimatedonutsmp2:teleport_home_%slot%"
    - LABEL: "Change Icon"
      ACTION: "ultimatedonutsmp2:change_icon_prompt_%slot%"
    - LABEL: "Rename"
      ACTION: "ultimatedonutsmp2:rename_home_prompt_%slot%"
    - LABEL: "&cDelete"
      ACTION: "ultimatedonutsmp2:delete_home_prompt_%slot%"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:homes_menu"
```

</details>

---

## Section: `CHANGE_HOME_ICON_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CHANGE_HOME_ICON_DIALOG.TITLE` | `string` | Any text | `Choose Icon` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `CHANGE_HOME_ICON_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Choose Icon` | External title. |
| `CHANGE_HOME_ICON_DIALOG.INPUT-LABEL` | `string` | Any text | `Search` | Input label. |
| `CHANGE_HOME_ICON_DIALOG.SEARCH-LABEL` | `string` | Any text | `Search` | Search label. |
| `CHANGE_HOME_ICON_DIALOG.DEFAULT-LABEL` | `string` | Any text | `Default` | Default label. |
| `CHANGE_HOME_ICON_DIALOG.BACK-LABEL` | `string` | Any text | `Back` | Back label. |
| `CHANGE_HOME_ICON_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `96` | BUTTON-WIDTH sizes the three controls above the results; the results themselves are wider so the longest item names still fit on one line. |
| `CHANGE_HOME_ICON_DIALOG.RESULT-WIDTH` | `integer` | Any integer | `128` | Result width. |
| `CHANGE_HOME_ICON_DIALOG.COLUMNS` | `integer` | Any integer | `4` | Columns. |
| `CHANGE_HOME_ICON_DIALOG.MAX-RESULTS` | `integer` | Any integer | `2000` | Icon results shown per search. The default lists every item and lets the client scroll. |

<details>
<summary>Default <code>CHANGE_HOME_ICON_DIALOG</code> block as shipped</summary>

```yaml
CHANGE_HOME_ICON_DIALOG:
  TITLE: "Choose Icon"
  EXTERNAL-TITLE: "Choose Icon"
  INPUT-LABEL: "Search"
  SEARCH-LABEL: "Search"
  DEFAULT-LABEL: "Default"
  BACK-LABEL: "Back"
  # BUTTON-WIDTH sizes the three controls above the results; the results themselves are wider so
  # the longest item names still fit on one line.
  BUTTON-WIDTH: 96
  RESULT-WIDTH: 128
  COLUMNS: 4
  # Icon results shown per search. The default lists every item and lets the client scroll.
  MAX-RESULTS: 2000
```

</details>

---

## Section: `RENAME_HOME_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RENAME_HOME_DIALOG.TITLE` | `string` | Any text | `Rename` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `RENAME_HOME_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Rename` | External title. |
| `RENAME_HOME_DIALOG.ITEM` | `string` | Any text | `''` | Item. |
| `RENAME_HOME_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `RENAME_HOME_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>RENAME_HOME_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: new_name, LABEL: New Name, MAX-LENGTH: 16, WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>RENAME_HOME_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_rename_home_%slot%', LABEL: Save}
  - {ACTION: 'ultimatedonutsmp2:manage_home_%slot%', LABEL: Cancel}
```

</details>

<details>
<summary>Default <code>RENAME_HOME_DIALOG</code> block as shipped</summary>

```yaml
RENAME_HOME_DIALOG:
  TITLE: "Rename"
  EXTERNAL-TITLE: "Rename"
  ITEM: ""
  INPUTS:
    - ID: "new_name"
      LABEL: "New Name"
      WIDTH: 200
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "Save"
      ACTION: "ultimatedonutsmp2:execute_rename_home_%slot%"
    - LABEL: "Cancel"
      ACTION: "ultimatedonutsmp2:manage_home_%slot%"
```

</details>

---

## Section: `DELETE_HOME_CONFIRM_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DELETE_HOME_CONFIRM_DIALOG.TITLE` | `string` | Any text | `&cDelete %name%?` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `DELETE_HOME_CONFIRM_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `&cDelete %name%?` | External title. |
| `DELETE_HOME_CONFIRM_DIALOG.ITEM` | `string` | Any text | `''` | Item. |
| `DELETE_HOME_CONFIRM_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `DELETE_HOME_CONFIRM_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `DELETE_HOME_CONFIRM_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>DELETE_HOME_CONFIRM_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:manage_home_%slot%', LABEL: Cancel}
  - {ACTION: 'ultimatedonutsmp2:execute_delete_home_%slot%', LABEL: '&cDelete'}
```

</details>

<details>
<summary>Default <code>DELETE_HOME_CONFIRM_DIALOG</code> block as shipped</summary>

```yaml
DELETE_HOME_CONFIRM_DIALOG:
  TITLE: "&cDelete %name%?"
  EXTERNAL-TITLE: "&cDelete %name%?"
  ITEM: ""
  COLUMNS: 2
  BUTTON-WIDTH: 100
  BUTTONS:
    - LABEL: "Cancel"
      ACTION: "ultimatedonutsmp2:manage_home_%slot%"
    - LABEL: "&cDelete"
      ACTION: "ultimatedonutsmp2:execute_delete_home_%slot%"
```

</details>

---

## Section: `STATS_SEARCH_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS_SEARCH_DIALOG.TITLE` | `string` | Any text | `Player Stats` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS_SEARCH_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Player Stats` | External title. |
| `STATS_SEARCH_DIALOG.DESCRIPTION` | `string` | Any text | `Click a player to view their stats` | Description. |
| `STATS_SEARCH_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `STATS_SEARCH_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `STATS_SEARCH_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `STATS_SEARCH_DIALOG.ADD-BUTTON-WIDTH` | `integer` | Any integer | `200` | Wider than the name cells so "+ Add Player to List" is not clipped. 200 matches two 100-wide columns, so the add row sits full-width under the grid. |
| `STATS_SEARCH_DIALOG.LIST-SIZE` | `integer` | Any integer | `5` | How many player buttons sit in the grid before the Add button. Self, pinned names, incoming follow requests, people the player follows, then other online players fill it. |
| `STATS_SEARCH_DIALOG.ADD-BUTTON-LABEL` | `string` | Any text | `+ Add Player to List` | Add button label. |
| `STATS_SEARCH_DIALOG.YOU-TOOLTIP` | `string` | Any text | `You` | You tooltip. |
| `STATS_SEARCH_DIALOG.RECOMMENDED-TOOLTIP` | `string` | Any text | `Recommended` | Recommended tooltip. |
| `STATS_SEARCH_DIALOG.REQUEST-TOOLTIP` | `string` | Any text | `Sent you a request %time% ago` | Request tooltip. |
| `STATS_SEARCH_DIALOG.BACK-LABEL` | `string` | Any text | `''` | Leave blank: the reference has no Back on this screen. Escape still closes it. |
| `STATS_SEARCH_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:main_menu` | Back action. |
| `STATS_SEARCH_DIALOG.EMPTY-TEXT` | `string` | Any text | `&7Nobody on your list yet.` | Empty text. |

<details>
<summary>Default <code>STATS_SEARCH_DIALOG</code> block as shipped</summary>

```yaml
STATS_SEARCH_DIALOG:
  TITLE: "Player Stats"
  EXTERNAL-TITLE: "Player Stats"
  DESCRIPTION: "Click a player to view their stats"
  AFTER-ACTION: NONE
  COLUMNS: 2
  BUTTON-WIDTH: 100
  # Wider than the name cells so "+ Add Player to List" is not clipped. 200 matches
  # two 100-wide columns, so the add row sits full-width under the grid.
  ADD-BUTTON-WIDTH: 200
  # How many player buttons sit in the grid before the Add button. Self, pinned names,
  # incoming follow requests, people the player follows, then other online players fill it.
  LIST-SIZE: 5
  ADD-BUTTON-LABEL: "+ Add Player to List"
  YOU-TOOLTIP: "You"
  RECOMMENDED-TOOLTIP: "Recommended"
  REQUEST-TOOLTIP: "Sent you a request %time% ago"
  # Leave blank: the reference has no Back on this screen. Escape still closes it.
  BACK-LABEL: ""
  BACK-ACTION: "ultimatedonutsmp2:main_menu"
  EMPTY-TEXT: "&7Nobody on your list yet."
```

</details>

---

## Section: `ADD_STATS_PLAYER_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ADD_STATS_PLAYER_DIALOG.TITLE` | `string` | Any text | `Add a Player` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `ADD_STATS_PLAYER_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Add a Player` | External title. |
| `ADD_STATS_PLAYER_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `ADD_STATS_PLAYER_DIALOG.DESCRIPTION` | `string` | Any text | `Type a name to add to your pay list` | Description. |
| `ADD_STATS_PLAYER_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `250` | Button width. |
| `ADD_STATS_PLAYER_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `ADD_STATS_PLAYER_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>ADD_STATS_PLAYER_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 250}
```

</details>

<details>
<summary>Default contents of <code>ADD_STATS_PLAYER_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_add_stats_player', LABEL: '&#00FC00Add to List'}
  - {ACTION: 'ultimatedonutsmp2:stats_menu', LABEL: Back}
```

</details>

<details>
<summary>Default <code>ADD_STATS_PLAYER_DIALOG</code> block as shipped</summary>

```yaml
ADD_STATS_PLAYER_DIALOG:
  TITLE: "Add a Player"
  EXTERNAL-TITLE: "Add a Player"
  AFTER-ACTION: NONE
  DESCRIPTION: "Type a name to add to your pay list"
  BUTTON-WIDTH: 250
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 250
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&#00FC00Add to List"
      ACTION: "ultimatedonutsmp2:execute_add_stats_player"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:stats_menu"
```

</details>

---

## Section: `PLAYER_STATS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PLAYER_STATS_DIALOG.TITLE` | `string` | Any text | `%player%` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PLAYER_STATS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `%player%` | External title. |
| `PLAYER_STATS_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `PLAYER_STATS_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `PLAYER_STATS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `200` | Button width. |
| `PLAYER_STATS_DIALOG.ONLINE-ITEM` | `string` | Any text | `LIME_CONCRETE` | The banner is the player's current head (SkinsRestorer included). These materials are only used when that head cannot be built. |
| `PLAYER_STATS_DIALOG.OFFLINE-ITEM` | `string` | Any text | `GRAY_CONCRETE` | Offline item. |
| `PLAYER_STATS_DIALOG.STATS-FORMAT` | `list` | A list of values | _list of 4 items_ | The stats format list. |
| `PLAYER_STATS_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>PLAYER_STATS_DIALOG.STATS-FORMAT</code> (4 items)</summary>

```yaml
STATS-FORMAT:
  - '&7Money: &#00FC00$&f %money%'
  - '&7Kills: &f%kills%'
  - '&7Deaths: &f%deaths%'
  - '&7Playtime: &f%playtime%'
```

</details>

<details>
<summary>Default contents of <code>PLAYER_STATS_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:stats_view_full_%player_uuid%', LABEL: View Full Profile}
  - {ACTION: 'ultimatedonutsmp2:stats_menu', LABEL: Back}
```

</details>

<details>
<summary>Default <code>PLAYER_STATS_DIALOG</code> block as shipped</summary>

```yaml
PLAYER_STATS_DIALOG:
  TITLE: "%player%"
  EXTERNAL-TITLE: "%player%"
  AFTER-ACTION: NONE
  COLUMNS: 1
  BUTTON-WIDTH: 200
  # The banner is the player's current head (SkinsRestorer included). These materials are
  # only used when that head cannot be built.
  ONLINE-ITEM: "LIME_CONCRETE"
  OFFLINE-ITEM: "GRAY_CONCRETE"
  STATS-FORMAT:
    - "&7Money: &#00FC00$&f %money%"
    - "&7Kills: &f%kills%"
    - "&7Deaths: &f%deaths%"
    - "&7Playtime: &f%playtime%"
  BUTTONS:
    - LABEL: "View Full Profile"
      ACTION: "ultimatedonutsmp2:stats_view_full_%player_uuid%"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:stats_menu"
```

</details>

---

## Section: `TELEPORT_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TELEPORT_DIALOG.TITLE` | `string` | Any text | `Teleport` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `TELEPORT_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Teleport` | External title. |
| `TELEPORT_DIALOG.DESCRIPTION` | `string` | Any text | `Click a player to teleport` | Description. |
| `TELEPORT_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `TELEPORT_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `TELEPORT_DIALOG.ADD-BUTTON-LABEL` | `string` | Any text | `&7+ Add Player to List` | Add button label. |
| `TELEPORT_DIALOG.ADD-BUTTON-TOOLTIP` | `string` | Any text | `Type a player name to add` | Add button tooltip. |
| `TELEPORT_DIALOG.ADD-BUTTON-WIDTH` | `integer` | Any integer | `200` | Add button width. |
| `TELEPORT_DIALOG.BACK-LABEL` | `string` | Any text | `''` | Back label. |
| `TELEPORT_DIALOG.EMPTY-TEXT` | `string` | Any text | `&7Nobody on your list yet.` | Empty text. |

<details>
<summary>Default <code>TELEPORT_DIALOG</code> block as shipped</summary>

```yaml
TELEPORT_DIALOG:
  TITLE: "Teleport"
  EXTERNAL-TITLE: "Teleport"
  DESCRIPTION: "Click a player to teleport"
  COLUMNS: 2
  BUTTON-WIDTH: 100
  ADD-BUTTON-LABEL: "&7+ Add Player to List"
  ADD-BUTTON-TOOLTIP: "Type a player name to add"
  ADD-BUTTON-WIDTH: 200
  BACK-LABEL: ""
  EMPTY-TEXT: "&7Nobody on your list yet."
```

</details>

---

## Section: `ADD_TELEPORT_PLAYER_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ADD_TELEPORT_PLAYER_DIALOG.TITLE` | `string` | Any text | `Add a Player` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `ADD_TELEPORT_PLAYER_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Add a Player` | External title. |
| `ADD_TELEPORT_PLAYER_DIALOG.DESCRIPTION` | `string` | Any text | `Type a name to add to your teleport list` | Description. |
| `ADD_TELEPORT_PLAYER_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `ADD_TELEPORT_PLAYER_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>ADD_TELEPORT_PLAYER_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>ADD_TELEPORT_PLAYER_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_add_tpa_player', LABEL: '&#00FC00Add to List'}
  - {ACTION: 'ultimatedonutsmp2:tpa', LABEL: Back}
```

</details>

<details>
<summary>Default <code>ADD_TELEPORT_PLAYER_DIALOG</code> block as shipped</summary>

```yaml
ADD_TELEPORT_PLAYER_DIALOG:
  TITLE: "Add a Player"
  EXTERNAL-TITLE: "Add a Player"
  DESCRIPTION: "Type a name to add to your teleport list"
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 200
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&#00FC00Add to List"
      ACTION: "ultimatedonutsmp2:execute_add_tpa_player"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:tpa"
```

</details>

---

## Section: `TELEPORT_CONFIRM_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TELEPORT_CONFIRM_DIALOG.TITLE` | `string` | Any text | `Teleport with %target%?` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `TELEPORT_CONFIRM_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Teleport with %target%?` | External title. |
| `TELEPORT_CONFIRM_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `TELEPORT_CONFIRM_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `150` | Button width. |
| `TELEPORT_CONFIRM_DIALOG.TELEPORT-TO-LABEL` | `string` | Any text | `Teleport to` | Teleport to label. |
| `TELEPORT_CONFIRM_DIALOG.TELEPORT-HERE-LABEL` | `string` | Any text | `Teleport here` | Teleport here label. |
| `TELEPORT_CONFIRM_DIALOG.BACK-LABEL` | `string` | Any text | `Back` | Back label. |

<details>
<summary>Default <code>TELEPORT_CONFIRM_DIALOG</code> block as shipped</summary>

```yaml
TELEPORT_CONFIRM_DIALOG:
  TITLE: "Teleport with %target%?"
  EXTERNAL-TITLE: "Teleport with %target%?"
  COLUMNS: 1
  BUTTON-WIDTH: 150
  TELEPORT-TO-LABEL: "Teleport to"
  TELEPORT-HERE-LABEL: "Teleport here"
  BACK-LABEL: "Back"
```

</details>

---

## Section: `TELEPORT_REQUEST_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TELEPORT_REQUEST_DIALOG.TITLE` | `string` | Any text | `Teleport Request` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `TELEPORT_REQUEST_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Teleport Request` | External title. |
| `TELEPORT_REQUEST_DIALOG.ITEM` | `string` | Any text | `ENDER_PEARL` | Item. |
| `TELEPORT_REQUEST_DIALOG.INPUTS` | `list` | A list of values | _list of 2 items_ | The inputs list. |
| `TELEPORT_REQUEST_DIALOG.BUTTONS` | `list` | A list of values | _list of 1 item_ | The buttons list. |

<details>
<summary>Default contents of <code>TELEPORT_REQUEST_DIALOG.INPUTS</code> (2 items)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 200}
  - {ID: direction, LABEL: Direction, OPTIONS: [{DEFAULT: true, ID: tpa, LABEL: 'Direction:
        Teleport to player'}, {DEFAULT: false, ID: tpahere, LABEL: 'Direction: Teleport
        player to me'}], WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>TELEPORT_REQUEST_DIALOG.BUTTONS</code> (1 item)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_tpa_request', LABEL: '&aSend Request'}
```

</details>

<details>
<summary>Default <code>TELEPORT_REQUEST_DIALOG</code> block as shipped</summary>

```yaml
TELEPORT_REQUEST_DIALOG:
  TITLE: "Teleport Request"
  EXTERNAL-TITLE: "Teleport Request"
  ITEM: "ENDER_PEARL"
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 200
      MAX-LENGTH: 16
    - ID: "direction"
      LABEL: "Direction"
      WIDTH: 200
      OPTIONS:
        - ID: "tpa"
          LABEL: "Direction: Teleport to player"
          DEFAULT: true
        - ID: "tpahere"
          LABEL: "Direction: Teleport player to me"
          DEFAULT: false
  BUTTONS:
    - LABEL: "&aSend Request"
      ACTION: "ultimatedonutsmp2:execute_tpa_request"
```

</details>

---

## Section: `FRIENDS_MENU_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIENDS_MENU_DIALOG.TITLE` | `string` | Any text | `Friends {friends} friends / {following} following` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FRIENDS_MENU_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Friends {friends} friends / {following} following` | External title. |
| `FRIENDS_MENU_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FRIENDS_MENU_DIALOG.COLUMNS` | `integer` | Any integer | `3` | Columns. |
| `FRIENDS_MENU_DIALOG.FILTER-WIDTH` | `integer` | Any integer | `100` | Filter width. |
| `FRIENDS_MENU_DIALOG.SEARCH-WIDTH` | `integer` | Any integer | `100` | Search width. |
| `FRIENDS_MENU_DIALOG.FRIEND-WIDTH` | `integer` | Any integer | `100` | Friend width. |
| `FRIENDS_MENU_DIALOG.FOLLOW-WIDTH` | `integer` | Any integer | `200` | Wider than a name cell so "+ Follow" sits under Filter + Search on the empty / 1-friend rows. |
| `FRIENDS_MENU_DIALOG.FILTER-LABEL` | `string` | Any text | `Filter` | Filter label. |
| `FRIENDS_MENU_DIALOG.SEARCH-LABEL` | `string` | Any text | `Search` | Search label. |
| `FRIENDS_MENU_DIALOG.FOLLOW-LABEL` | `string` | Any text | `&#00FC00+ Follow` | Follow label. |
| `FRIENDS_MENU_DIALOG.FOLLOW-TOOLTIP` | `string` | Any text | `&7Search for a player to follow` | Follow tooltip. |
| `FRIENDS_MENU_DIALOG.BACK-LABEL` | `string` | Any text | `''` | The reference has no Back on this screen. Escape still closes it. |
| `FRIENDS_MENU_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:main_menu` | Back action. |
| `FRIENDS_MENU_DIALOG.EMPTY-ALL` | `string` | Any text | `&7No friends or following` | Empty all. |
| `FRIENDS_MENU_DIALOG.EMPTY-FRIENDS` | `string` | Any text | `&7No friends` | Empty friends. |
| `FRIENDS_MENU_DIALOG.EMPTY-FOLLOWING` | `string` | Any text | `&7No following` | Empty following. |
| `FRIENDS_MENU_DIALOG.EMPTY-FOLLOWERS` | `string` | Any text | `&7No followers` | Empty followers. |
| `FRIENDS_MENU_DIALOG.EMPTY-SEARCH` | `string` | Any text | `&7No players matched that name.` | Empty search. |
| `FRIENDS_MENU_DIALOG.SEARCH-TOOLTIP` | `string` | Any text | `&7Click to search` | Search tooltip. |
| `FRIENDS_MENU_DIALOG.SEARCH-ACTIVE-TOOLTIP` | `string` | Any text | `&7Currently: &f%query% &7Click to search` | Search active tooltip. |
| `FRIENDS_MENU_DIALOG.FILTER-TOOLTIP-TEMPLATE` | `string` | Any text | `&7Click to change %all% %friends% %following% %followers%` | Filter tooltip template. |
| `FRIENDS_MENU_DIALOG.ACTIVE-FORMAT` | `string` | Any text | `&f- %label%` | Active format. |
| `FRIENDS_MENU_DIALOG.INACTIVE-FORMAT` | `string` | Any text | `&7%label%` | Inactive format. |

### `FRIENDS_MENU_DIALOG.FILTER-LABELS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIENDS_MENU_DIALOG.FILTER-LABELS.ALL` | `string` | Any text | `All` | All. |
| `FRIENDS_MENU_DIALOG.FILTER-LABELS.FRIENDS` | `string` | Any text | `Friends` | Friends. |
| `FRIENDS_MENU_DIALOG.FILTER-LABELS.FOLLOWING` | `string` | Any text | `Following` | Following. |
| `FRIENDS_MENU_DIALOG.FILTER-LABELS.FOLLOWERS` | `string` | Any text | `Followers` | Followers. |

### `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.FRIENDS` | `string` | Any text | `&fFriends` | Friends. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.FOLLOWING` | `string` | Any text | `&fFollowing` | Following. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.FOLLOWERS` | `string` | Any text | `&fFollows you` | Followers. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.ONLINE` | `string` | Any text | `&#00FC00Online` | Online. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.OFFLINE` | `string` | Any text | `&7Offline` | Offline. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.CLICK-TO-EDIT` | `string` | Any text | `&7Click to edit` | Click to edit. |
| `FRIENDS_MENU_DIALOG.RELATIONSHIP-TOOLTIP.CLICK-TO-FOLLOW` | `string` | Any text | `&7Click to follow` | Click to follow. |

<details>
<summary>Default <code>FRIENDS_MENU_DIALOG</code> block as shipped</summary>

```yaml
FRIENDS_MENU_DIALOG:
  TITLE: "Friends {friends} friends / {following} following"
  EXTERNAL-TITLE: "Friends {friends} friends / {following} following"
  AFTER-ACTION: NONE
  COLUMNS: 3
  FILTER-WIDTH: 100
  SEARCH-WIDTH: 100
  FRIEND-WIDTH: 100
  # Wider than a name cell so "+ Follow" sits under Filter + Search on the empty / 1-friend rows.
  FOLLOW-WIDTH: 200
  FILTER-LABEL: "Filter"
  SEARCH-LABEL: "Search"
  FOLLOW-LABEL: "&#00FC00+ Follow"
  FOLLOW-TOOLTIP: "&7Search for a player to follow"
  # The reference has no Back on this screen. Escape still closes it.
  BACK-LABEL: ""
  BACK-ACTION: "ultimatedonutsmp2:main_menu"
  EMPTY-ALL: "&7No friends or following"
  EMPTY-FRIENDS: "&7No friends"
  EMPTY-FOLLOWING: "&7No following"
  EMPTY-FOLLOWERS: "&7No followers"
  EMPTY-SEARCH: "&7No players matched that name."
  SEARCH-TOOLTIP: "&7Click to search"
  SEARCH-ACTIVE-TOOLTIP: "&7Currently: &f%query%\n&7Click to search"
  FILTER-TOOLTIP-TEMPLATE: "&7Click to change\n%all%\n%friends%\n%following%\n%followers%"
  ACTIVE-FORMAT: "&f- %label%"
  INACTIVE-FORMAT: "&7%label%"
  FILTER-LABELS:
    ALL: "All"
    FRIENDS: "Friends"
    FOLLOWING: "Following"
    FOLLOWERS: "Followers"
  RELATIONSHIP-TOOLTIP:
    FRIENDS: "&fFriends"
    FOLLOWING: "&fFollowing"
    FOLLOWERS: "&fFollows you"
    ONLINE: "&#00FC00Online"
    OFFLINE: "&7Offline"
    CLICK-TO-EDIT: "&7Click to edit"
    CLICK-TO-FOLLOW: "&7Click to follow"
```

</details>

---

## Section: `FRIENDS_SEARCH_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIENDS_SEARCH_DIALOG.TITLE` | `string` | Any text | `Search Friends` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FRIENDS_SEARCH_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Search Friends` | External title. |
| `FRIENDS_SEARCH_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FRIENDS_SEARCH_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `FRIENDS_SEARCH_DIALOG.DESCRIPTION` | `string` | Any text | `Type a name to search your friends` | Description. |
| `FRIENDS_SEARCH_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `FRIENDS_SEARCH_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>FRIENDS_SEARCH_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>FRIENDS_SEARCH_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_friends_search', LABEL: '&#00FC00Search'}
  - {ACTION: 'ultimatedonutsmp2:friends', LABEL: Back}
```

</details>

<details>
<summary>Default <code>FRIENDS_SEARCH_DIALOG</code> block as shipped</summary>

```yaml
FRIENDS_SEARCH_DIALOG:
  TITLE: "Search Friends"
  EXTERNAL-TITLE: "Search Friends"
  AFTER-ACTION: NONE
  COLUMNS: 1
  DESCRIPTION: "Type a name to search your friends"
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 200
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&#00FC00Search"
      ACTION: "ultimatedonutsmp2:execute_friends_search"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:friends"
```

</details>

---

## Section: `FOLLOW_PROMPT_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FOLLOW_PROMPT_DIALOG.TITLE` | `string` | Any text | `Follow Player` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FOLLOW_PROMPT_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Follow Player` | External title. |
| `FOLLOW_PROMPT_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FOLLOW_PROMPT_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `FOLLOW_PROMPT_DIALOG.DESCRIPTION` | `string` | Any text | `Type a name to search` | Description. |
| `FOLLOW_PROMPT_DIALOG.INPUTS` | `list` | A list of values | _list of 1 item_ | The inputs list. |
| `FOLLOW_PROMPT_DIALOG.BUTTONS` | `list` | A list of values | _list of 2 items_ | The buttons list. |

<details>
<summary>Default contents of <code>FOLLOW_PROMPT_DIALOG.INPUTS</code> (1 item)</summary>

```yaml
INPUTS:
  - {ID: player_name, LABEL: Player Name, MAX-LENGTH: 16, WIDTH: 200}
```

</details>

<details>
<summary>Default contents of <code>FOLLOW_PROMPT_DIALOG.BUTTONS</code> (2 items)</summary>

```yaml
BUTTONS:
  - {ACTION: 'ultimatedonutsmp2:execute_follow_search', LABEL: '&#00FC00Search'}
  - {ACTION: 'ultimatedonutsmp2:friends', LABEL: Back}
```

</details>

<details>
<summary>Default <code>FOLLOW_PROMPT_DIALOG</code> block as shipped</summary>

```yaml
FOLLOW_PROMPT_DIALOG:
  TITLE: "Follow Player"
  EXTERNAL-TITLE: "Follow Player"
  AFTER-ACTION: NONE
  COLUMNS: 1
  DESCRIPTION: "Type a name to search"
  INPUTS:
    - ID: "player_name"
      LABEL: "Player Name"
      WIDTH: 200
      MAX-LENGTH: 16
  BUTTONS:
    - LABEL: "&#00FC00Search"
      ACTION: "ultimatedonutsmp2:execute_follow_search"
    - LABEL: "Back"
      ACTION: "ultimatedonutsmp2:friends"
```

</details>

---

## Section: `FOLLOW_SEARCH_RESULTS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FOLLOW_SEARCH_RESULTS_DIALOG.TITLE` | `string` | Any text | `Follow Results` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `Follow Results` | External title. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.BACK-WIDTH` | `integer` | Any integer | `100` | Back width. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.BACK-LABEL` | `string` | Any text | `Back` | Back label. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.BACK-ACTION` | `string` | Any text | `ultimatedonutsmp2:friends_follow_prompt` | Back action. |
| `FOLLOW_SEARCH_RESULTS_DIALOG.NO-RESULTS` | `string` | Any text | `&7No players matched that name.` | No results. |

<details>
<summary>Default <code>FOLLOW_SEARCH_RESULTS_DIALOG</code> block as shipped</summary>

```yaml
FOLLOW_SEARCH_RESULTS_DIALOG:
  TITLE: "Follow Results"
  EXTERNAL-TITLE: "Follow Results"
  AFTER-ACTION: NONE
  COLUMNS: 2
  BUTTON-WIDTH: 100
  BACK-WIDTH: 100
  BACK-LABEL: "Back"
  BACK-ACTION: "ultimatedonutsmp2:friends_follow_prompt"
  NO-RESULTS: "&7No players matched that name."
```

</details>

---

## Section: `FRIEND_DETAILS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIEND_DETAILS_DIALOG.TITLE` | `string` | Any text | `%name% Following` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FRIEND_DETAILS_DIALOG.TITLE-FOLLOWING` | `string` | Any text | `%name% Following` | Title following. |
| `FRIEND_DETAILS_DIALOG.TITLE-FRIENDS` | `string` | Any text | `%name% Friends` | Title friends. |
| `FRIEND_DETAILS_DIALOG.TITLE-FOLLOWERS` | `string` | Any text | `%name% Followers` | Title followers. |
| `FRIEND_DETAILS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `%name% Following` | External title. |
| `FRIEND_DETAILS_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FRIEND_DETAILS_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `FRIEND_DETAILS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `300` | Button width. |
| `FRIEND_DETAILS_DIALOG.BACK-WIDTH` | `integer` | Any integer | `150` | Back width. |
| `FRIEND_DETAILS_DIALOG.VIEW-STATS-LABEL` | `string` | Any text | `View Stats` | View stats label. |
| `FRIEND_DETAILS_DIALOG.VIEW-STATS-TOOLTIP` | `string` | Any text | `See %name%'s profile` | View stats tooltip. |
| `FRIEND_DETAILS_DIALOG.PAY-LABEL` | `string` | Any text | `Pay` | Pay label. |
| `FRIEND_DETAILS_DIALOG.PAY-TOOLTIP` | `string` | Any text | `Send money to %name%` | Pay tooltip. |
| `FRIEND_DETAILS_DIALOG.TELEPORT-LABEL` | `string` | Any text | `Teleport` | Teleport label. |
| `FRIEND_DETAILS_DIALOG.TELEPORT-TOOLTIP` | `string` | Any text | `Teleport to or bring %name%` | Teleport tooltip. |
| `FRIEND_DETAILS_DIALOG.SETTINGS-LABEL` | `string` | Any text | `Settings` | Settings label. |
| `FRIEND_DETAILS_DIALOG.SETTINGS-TOOLTIP` | `string` | Any text | `Change settings for %name%` | Settings tooltip. |
| `FRIEND_DETAILS_DIALOG.FOLLOW-LABEL` | `string` | Any text | `&#00FC00Follow` | Follow label. |
| `FRIEND_DETAILS_DIALOG.UNFOLLOW-LABEL` | `string` | Any text | `&cUnfollow` | Unfollow label. |
| `FRIEND_DETAILS_DIALOG.BACK-LABEL` | `string` | Any text | `Back` | Back label. |

<details>
<summary>Default <code>FRIEND_DETAILS_DIALOG</code> block as shipped</summary>

```yaml
FRIEND_DETAILS_DIALOG:
  TITLE: "%name% Following"
  TITLE-FOLLOWING: "%name% Following"
  TITLE-FRIENDS: "%name% Friends"
  TITLE-FOLLOWERS: "%name% Followers"
  EXTERNAL-TITLE: "%name% Following"
  AFTER-ACTION: NONE
  COLUMNS: 1
  BUTTON-WIDTH: 300
  BACK-WIDTH: 150
  VIEW-STATS-LABEL: "View Stats"
  VIEW-STATS-TOOLTIP: "See %name%'s profile"
  PAY-LABEL: "Pay"
  PAY-TOOLTIP: "Send money to %name%"
  TELEPORT-LABEL: "Teleport"
  TELEPORT-TOOLTIP: "Teleport to or bring %name%"
  SETTINGS-LABEL: "Settings"
  SETTINGS-TOOLTIP: "Change settings for %name%"
  FOLLOW-LABEL: "&#00FC00Follow"
  UNFOLLOW-LABEL: "&cUnfollow"
  BACK-LABEL: "Back"
```

</details>

---

## Section: `FRIEND_SETTINGS_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FRIEND_SETTINGS_DIALOG.TITLE` | `string` | Any text | `%name% — Settings` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FRIEND_SETTINGS_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `%name% — Settings` | External title. |
| `FRIEND_SETTINGS_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `FRIEND_SETTINGS_DIALOG.COLUMNS` | `integer` | Any integer | `1` | Columns. |
| `FRIEND_SETTINGS_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `300` | Button width. |
| `FRIEND_SETTINGS_DIALOG.BACK-WIDTH` | `integer` | Any integer | `300` | Back width. |
| `FRIEND_SETTINGS_DIALOG.ACTIVITY-LABEL` | `string` | Any text | `See Activity: %state%` | Activity label. |
| `FRIEND_SETTINGS_DIALOG.TRANSACTIONS-LABEL` | `string` | Any text | `See Transactions: %state%` | Transactions label. |
| `FRIEND_SETTINGS_DIALOG.MESSAGE-LABEL` | `string` | Any text | `Can Message: %state%` | Message label. |
| `FRIEND_SETTINGS_DIALOG.TPA-LABEL` | `string` | Any text | `Can Teleport Request: %state%` | Tpa label. |
| `FRIEND_SETTINGS_DIALOG.AUTO-TPA-LABEL` | `string` | Any text | `Auto Accept TPAs: %state%` | Auto tpa label. |
| `FRIEND_SETTINGS_DIALOG.PAY-LABEL` | `string` | Any text | `Can Pay You: %state%` | Pay label. |
| `FRIEND_SETTINGS_DIALOG.BACK-LABEL` | `string` | Any text | `Back` | Back label. |
| `FRIEND_SETTINGS_DIALOG.STATE-TRUE` | `string` | Any text | `&aON` | State true. |
| `FRIEND_SETTINGS_DIALOG.STATE-FALSE` | `string` | Any text | `&cOFF` | State false. |

<details>
<summary>Default <code>FRIEND_SETTINGS_DIALOG</code> block as shipped</summary>

```yaml
FRIEND_SETTINGS_DIALOG:
  TITLE: "%name% — Settings"
  EXTERNAL-TITLE: "%name% — Settings"
  AFTER-ACTION: NONE
  COLUMNS: 1
  BUTTON-WIDTH: 300
  BACK-WIDTH: 300
  ACTIVITY-LABEL: "See Activity: %state%"
  TRANSACTIONS-LABEL: "See Transactions: %state%"
  MESSAGE-LABEL: "Can Message: %state%"
  TPA-LABEL: "Can Teleport Request: %state%"
  AUTO-TPA-LABEL: "Auto Accept TPAs: %state%"
  PAY-LABEL: "Can Pay You: %state%"
  BACK-LABEL: "Back"
  STATE-TRUE: "&aON"
  STATE-FALSE: "&cOFF"
```

</details>

---

## Section: `RTP_QUEUE_DIALOG`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP_QUEUE_DIALOG.TITLE` | `string` | Any text | `RTP Queue` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `RTP_QUEUE_DIALOG.EXTERNAL-TITLE` | `string` | Any text | `RTP Queue` | External title. |
| `RTP_QUEUE_DIALOG.CAN-CLOSE-WITH-ESCAPE` | `boolean` | `true`, `false` | `true` | On/off for can close with escape. |
| `RTP_QUEUE_DIALOG.AFTER-ACTION` | `string` | Any text | `NONE` | After action. |
| `RTP_QUEUE_DIALOG.PAUSE` | `boolean` | `true`, `false` | `false` | On/off for pause. |
| `RTP_QUEUE_DIALOG.ITEM` | `string` | Any text | `''` | Item. |
| `RTP_QUEUE_DIALOG.COLUMNS` | `integer` | Any integer | `2` | Columns. |
| `RTP_QUEUE_DIALOG.BUTTON-WIDTH` | `integer` | Any integer | `100` | Button width. |
| `RTP_QUEUE_DIALOG.TEXT-WIDTH` | `integer` | Any integer | `200` | Text width. |
| `RTP_QUEUE_DIALOG.QUESTION` | `string` | Any text | `Are you sure you want to randomly teleport with another p…` | Question. |
| `RTP_QUEUE_DIALOG.NO-LABEL` | `string` | Any text | `&cNo` | No label. |
| `RTP_QUEUE_DIALOG.NO-ACTION` | `string` | Any text | `ultimatedonutsmp2:rtp_queue_deny` | No action. |
| `RTP_QUEUE_DIALOG.YES-LABEL` | `string` | Any text | `&#00FC00Yes` | Yes label. |
| `RTP_QUEUE_DIALOG.YES-ACTION` | `string` | Any text | `ultimatedonutsmp2:rtp_queue_accept` | Yes action. |

<details>
<summary>Default <code>RTP_QUEUE_DIALOG</code> block as shipped</summary>

```yaml
RTP_QUEUE_DIALOG:
  TITLE: "RTP Queue"
  EXTERNAL-TITLE: "RTP Queue"
  CAN-CLOSE-WITH-ESCAPE: true
  AFTER-ACTION: NONE
  PAUSE: false
  ITEM: ""
  COLUMNS: 2
  BUTTON-WIDTH: 100
  TEXT-WIDTH: 200
  QUESTION: "Are you sure you want to randomly teleport with another player?"
  NO-LABEL: "&cNo"
  NO-ACTION: "ultimatedonutsmp2:rtp_queue_deny"
  YES-LABEL: "&#00FC00Yes"
  YES-ACTION: "ultimatedonutsmp2:rtp_queue_accept"
```

</details>

---

---

## Amethyst give flow

`/amethysttool` and `/shardtool` with no arguments open this stack when `SCREENS.AMETHYST` is on.

| Screen | What it is |
| :--- | :--- |
| `AMETHYST_TYPE_DIALOG` | One button per tool. Label is `%icon% %name%`; tooltip is that tool's lore. |
| `AMETHYST_PLAYER_DIALOG` | Every known player, online first. Button label is the 2D skin head plus nick. Lore is `PLAYER-ONLINE` or `PLAYER-OFFLINE` with `{time}` as `d`/`h`/`m`. |
| `AMETHYST_PLAYER_SEARCH_DIALOG` | Name filter for the player list. |
| `AMETHYST_DURATION_DIALOG` | Preset buttons labelled `1h`, `5h`, `10h`, `1d`, `5d` (not `3600`) plus Custom. |
| `AMETHYST_DURATION_CUSTOM_DIALOG` | Text input. Accepts `1h`, `5d`, `1d 5h`, or seconds. |

Offline grants are written to `plugins/UltimateDonutSmp2/amethyst-pending.yml` and given on join, with the same `RECEIVED` chat as an online grant.

```yaml
AMETHYST_DURATION_DIALOG:
  TITLE: "Duration for %target%"
  COLUMNS: 2
  DURATIONS:
    - {LABEL: 1h, SECONDS: 3600}
    - {LABEL: 5h, SECONDS: 18000}
    - {LABEL: 10h, SECONDS: 36000}
    - {LABEL: 1d, SECONDS: 86400}
    - {LABEL: 5d, SECONDS: 432000}
    - {LABEL: Custom, ACTION: custom}
```

---

Defaults above match the file shipped in the jar.
