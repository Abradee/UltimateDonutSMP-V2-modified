# `menus.yml`

The layout file for every chest-style GUI in the plugin — roughly forty menus, from the
settings screen to the leaderboards, bounty board, profile viewer and confirmation
dialogs. Each menu section defines its title, size and the items placed in it, with slot
numbers counted from `0` in the top-left.

Text here is localized. If you only want to translate a menu, edit `MENUS` in
`languages/<locale>.yml` instead — that overlay is applied on top of this file, so your
layout survives a plugin update while the wording stays translated.

`GLOBAL` holds the shared filler, navigation and close-button definitions that individual
menus inherit, so a change there is felt everywhere. Note also that the plugin will reset a
menu layout it recognises as a stale pre-update version, so keep a copy of heavy
customisations.

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/menus.yml` |
| **Commands** | `/menu`, `/settings`, `/rules`, `/ranks`, `/stats`, `/leaderboard`, and most GUI commands |
| **Player-facing text** | Edit `MENUS` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`GLOBAL`](#section-global) | section | 2 keys |
| [`TEAM-MENUS`](#section-team-menus) | section | 5 keys |
| [`MEDIA-MENU`](#section-media-menu) | section | 3 keys |
| [`RANKS-MENU`](#section-ranks-menu) | section | 3 keys |
| [`STATS-MENU`](#section-stats-menu) | section | 4 keys |
| [`SETTINGS-MENU`](#section-settings-menu) | section | 3 keys |
| [`LEADERBOARDS-MENU`](#section-leaderboards-menu) | section | 5 keys |
| [`PROGRESS-MENU`](#section-progress-menu) | section | 6 keys |
| [`SELL-MENU`](#section-sell-menu) | section | 2 keys |
| [`WORTH-MENU`](#section-worth-menu) | section | 3 keys |
| [`TPA-CONFIRM-MENU`](#section-tpa-confirm-menu) | section | 3 keys |
| [`BOUNTIES-MENU`](#section-bounties-menu) | section | 9 keys |
| [`BOUNTY-CONFIRM-MENU`](#section-bounty-confirm-menu) | section | 5 keys |
| [`SELL-HISTORY-MENU`](#section-sell-history-menu) | section | 4 keys |
| [`PURCHASE-SHOP-MENU`](#section-purchase-shop-menu) | section | 6 keys |
| [`PAY-CONFIRM-MENU`](#section-pay-confirm-menu) | section | 5 keys |
| [`SELLALL-CONFIRM-MENU`](#section-sellall-confirm-menu) | section | 5 keys |
| [`SERVER-INFO-MENU`](#section-server-info-menu) | section | 5 keys |
| [`RULES-MENU`](#section-rules-menu) | section | 3 keys |
| [`AFK-MENU`](#section-afk-menu) | section | 4 keys |
| [`SPAWN-MENU`](#section-spawn-menu) | section | 4 keys |
| [`PROFILE-VIEWER-MENU`](#section-profile-viewer-menu) | section | 3 keys |
| [`PROFILE-VIEWER-HOMES-MENU`](#section-profile-viewer-homes-menu) | section | 9 keys |
| [`PUNISHMENT-HISTORY-MENU`](#section-punishment-history-menu) | section | 6 keys |
| [`PUNISHMENTS-LIST-MENU`](#section-punishments-list-menu) | section | 8 keys |
| [`STATS-WIPE-MENU`](#section-stats-wipe-menu) | section | 6 keys |
| [`STATS-WIPE-CONFIRM-MENU`](#section-stats-wipe-confirm-menu) | section | 5 keys |
| [`SERVERS-MENU`](#section-servers-menu) | section | 6 keys |
| [`SPAWNER-MENUS`](#section-spawner-menus) | section | 6 keys |
| [`VOICE-CHAT-CONSENT-MENU`](#section-voice-chat-consent-menu) | section | 5 keys |

---

## Section: `GLOBAL`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `GLOBAL.ESC-BACK-TO-MENU` | `boolean` | `true`, `false` | `true` | On/off for esc back to menu. |

### `GLOBAL.PAGE-MENU`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `GLOBAL.PAGE-MENU.MATERIAL` | `string` | Any text | `ARROW` | Bukkit `Material` name for the icon. |
| `GLOBAL.PAGE-MENU.NEXT-BUTTON` | `string` | Any text | `&aNEXT` | Next button. |
| `GLOBAL.PAGE-MENU.BACK-BUTTON` | `string` | Any text | `&aBACK` | Back button. |
| `GLOBAL.PAGE-MENU.FIRST-PAGE-BUTTON` | `string` | Any text | `&aFIRST PAGE` | First page button. |
| `GLOBAL.PAGE-MENU.LAST-PAGE-BUTTON` | `string` | Any text | `&aLAST PAGE` | Last page button. |
| `GLOBAL.PAGE-MENU.NEXT-LORE` | `list` | A list of values | _list of 1 item_ | The next lore list. |
| `GLOBAL.PAGE-MENU.BACK-LORE` | `list` | A list of values | _list of 1 item_ | The back lore list. |
| `GLOBAL.PAGE-MENU.FIRST-PAGE-LORE` | `list` | A list of values | _list of 1 item_ | The first page lore list. |
| `GLOBAL.PAGE-MENU.LAST-PAGE-LORE` | `list` | A list of values | _list of 1 item_ | The last page lore list. |

<details>
<summary>Default contents of <code>GLOBAL.PAGE-MENU.NEXT-LORE</code> (1 item)</summary>

```yaml
NEXT-LORE:
  - '&fClick to go to the next page'
```

</details>

<details>
<summary>Default contents of <code>GLOBAL.PAGE-MENU.BACK-LORE</code> (1 item)</summary>

```yaml
BACK-LORE:
  - '&fClick to go to the previous page'
```

</details>

<details>
<summary>Default contents of <code>GLOBAL.PAGE-MENU.FIRST-PAGE-LORE</code> (1 item)</summary>

```yaml
FIRST-PAGE-LORE:
  - '&fJump to the first page'
```

</details>

<details>
<summary>Default contents of <code>GLOBAL.PAGE-MENU.LAST-PAGE-LORE</code> (1 item)</summary>

```yaml
LAST-PAGE-LORE:
  - '&fJump to the last page'
```

</details>

<details>
<summary>Default <code>GLOBAL</code> block as shipped</summary>

```yaml
GLOBAL:
  ESC-BACK-TO-MENU: true
  PAGE-MENU:
    MATERIAL: ARROW
    NEXT-BUTTON: '&aNEXT'
    BACK-BUTTON: '&aBACK'
    FIRST-PAGE-BUTTON: '&aFIRST PAGE'
    LAST-PAGE-BUTTON: '&aLAST PAGE'
    NEXT-LORE:
    - '&fClick to go to the next page'
    BACK-LORE:
    - '&fClick to go to the previous page'
    FIRST-PAGE-LORE:
    - '&fJump to the first page'
    LAST-PAGE-LORE:
    - '&fJump to the last page'
```

</details>

---

## Section: `TEAM-MENUS`

### Entry schema (5 entries)

Each entry under `TEAM-MENUS` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `TITLE` |
| :--- | :--- |
| `TEAM` | `&8Team` |
| `TEAM-INFO` | `&8Team {team_name}` |
| `TEAM-EDIT-MEMBER` | `&8Edit {player}` |
| `TEAM-KICK-MEMBER` | `&8Confirm Kicking {player}` |
| `TEAM-DISBAND` | `&8Confirm Disbanding Team` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SIZE` | `integer` | Any integer | Required | Chest size. Must be a multiple of 9 between 9 and 54. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PLACEHOLDER` | `boolean` | `true`, `false` | Optional (3/5) | On/off for placeholder. |
| `CANCEL-BUTTON` | `section` | — | Optional (2/5) | Options for cancel button, listed below. |
| `CONFIRM-BUTTON` | `section` | — | Optional (2/5) | Options for confirm button, listed below. |
| `MAX-ITEMS-PER-PAGE` | `integer` | Any integer | Optional (2/5) | Max items per page. |
| `PLAYER-BUTTON` | `section` | — | Optional (2/5) | Options for player button, listed below. |
| `PVP-BUTTON` | `section` | — | Optional (2/5) | Options for pvp button, listed below. |
| `BACK-BUTTON` | `section` | — | Optional (1/5) | Options for back button, listed below. |
| `EDIT-HOME-BUTTON` | `section` | — | Optional (1/5) | Options for edit home button, listed below. |
| `HOME-BUTTON` | `section` | — | Optional (1/5) | Options for home button, listed below. |
| `KICK-BUTTON` | `section` | — | Optional (1/5) | Options for kick button, listed below. |
| `MANAGE-TEAMMATES-BUTTON` | `section` | — | Optional (1/5) | Options for manage teammates button, listed below. |
| `MESSAGES` | `section` | — | Optional (1/5) | Options for messages, listed below. |
| `PAGE-BUTTON` | `section` | — | Optional (1/5) | Options for page button, listed below. |
| `PLACEHOLDER-MATERIAL` | `string` | Any text | Optional (1/5) | Placeholder material. |
| `REFRESH-BUTTON` | `section` | — | Optional (1/5) | Options for refresh button, listed below. |
| `SEARCH-BUTTON` | `section` | — | Optional (1/5) | Options for search button, listed below. |
| `SORT-BUTTON` | `section` | — | Optional (1/5) | Options for sort button, listed below. |
| `SUMMARY-BUTTON` | `section` | — | Optional (1/5) | Options for summary button, listed below. |
| `TEAM-CHAT-BUTTON` | `section` | — | Optional (1/5) | Options for team chat button, listed below. |
| `VISIT-HOME-BUTTON` | `section` | — | Optional (1/5) | Options for visit home button, listed below. |

#### `TEAM-MENUS.<entry>.CANCEL-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.CONFIRM-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.PLAYER-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `OFFLINE-SYMBOL` | `string` | Any text | Required | Offline symbol. |
| `ONLINE-SYMBOL` | `string` | Any text | Required | Online symbol. |
| `LEADER-LORE` | `string` | Any text | Optional (1/2) | Leader lore. |
| `LORE` | `string` | Any text | Optional (1/2) | Tooltip lines under the item name. |

#### `TEAM-MENUS.<entry>.PVP-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `string` | Any text | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.BACK-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.EDIT-HOME-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.HOME-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `HOME-LORE` | `string` | Any text | Required | Home lore. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NO-HOME-LORE` | `string` | Any text | Required | No home lore. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.KICK-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.MANAGE-TEAMMATES-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.MESSAGES`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CANT-EDIT-SELF` | `string` | Any text | Required | Cant edit self. |
| `NO-PERMISSION` | `string` | Any text | Required | Permission node. Leave empty to allow everyone. |
| `NOT-IN-TEAM` | `string` | Any text | Required | Not in team. |

#### `TEAM-MENUS.<entry>.PAGE-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.REFRESH-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.SEARCH-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.SORT-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SELECTED-PREFIX` | `string` | Any text | Required | Selected prefix. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `SYMBOL` | `string` | Any text | Required | Symbol. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `UNSELECTED-PREFIX` | `string` | Any text | Required | Unselected prefix. |

#### `TEAM-MENUS.<entry>.SUMMARY-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.TEAM-CHAT-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `TEAM-MENUS.<entry>.VISIT-HOME-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `OFF-STATE` | `string` | Any text | Required | Off state. |
| `ON-STATE` | `string` | Any text | Required | On state. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

<details>
<summary>Default <code>TEAM-MENUS</code> block as shipped</summary>

```yaml
TEAM-MENUS:
  TEAM:
    TITLE: '&8Team'
    SIZE: 54
    MAX-ITEMS-PER-PAGE: 45
    PLAYER-BUTTON:
      ONLINE-SYMBOL: "&a■"
      OFFLINE-SYMBOL: "&4■"
      LORE: '&fClick to edit'
    SEARCH-BUTTON:
      TITLE: '&#6BF18DSearch'
      MATERIAL: OAK_SIGN
      SLOT: 45
      LORE:
      - '&fSearch for team members'
      - '&cIn development.'
    SORT-BUTTON:
      TITLE: '&aSort'
      MATERIAL: HOPPER
      SLOT: 46
      SELECTED-PREFIX: '&a'
      UNSELECTED-PREFIX: '&f'
      SYMBOL: "▪"
    REFRESH-BUTTON:
      TITLE: '&#6BF18DTeam {team_name}'
      MATERIAL: IRON_HELMET
      SLOT: 49
      LORE:
      - '&fClick to refresh'
      - '&7Add up to {max_members} members'
    HOME-BUTTON:
      TITLE: '&#6BF18DTeam Home'
      MATERIAL: WHITE_BANNER
      SLOT: 52
      HOME-LORE: '&fClick to teleport to your team''s home'
      NO-HOME-LORE: '&fSet the team home with /home'
    PVP-BUTTON:
      TITLE: '&#6BF18DPVP'
      MATERIAL: IRON_SWORD
      SLOT: 53
      ON-STATE: '&a&lON'
      OFF-STATE: '&c&lOFF'
      LORE: '&fCurrently: {state}'
    MESSAGES:
      NOT-IN-TEAM: '&cYou are not part of the team.'
      NO-PERMISSION: '&cYou don''t have permissions to do this.'
      CANT-EDIT-SELF: '&cYou can''t do this yourself!'
  TEAM-INFO:
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `MEDIA-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MEDIA-MENU.TITLE` | `string` | Any text | `&8Media Rank` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `MEDIA-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `MEDIA-MENU.MEDIA-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MEDIA-MENU.MEDIA-BUTTON.DISPLAY-NAME` | `string` | Any text | `&eMedia Rank` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `MEDIA-MENU.MEDIA-BUTTON.MATERIAL` | `string` | Any text | `PINK_DYE` | Bukkit `Material` name for the icon. |
| `MEDIA-MENU.MEDIA-BUTTON.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `MEDIA-MENU.MEDIA-BUTTON.LORE` | `list` | A list of values | _list of 12 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>MEDIA-MENU.MEDIA-BUTTON.LORE</code> (12 items)</summary>

```yaml
LORE:
  - '&eReQuirements: (only one needed)'
  - '&e- &f25 average viewers on Stream'
  - '&e- &f5k views on a YouTube Video'
  - '&e- &f25k views on a TikTok'
  - '&e- &f50k views on YouTube Short'
  - ''
  - '&eReminders:'
  - '&8- &7Must have the IP on screen'
  - '&8- &7Must be from the new season'
  - '&8- &7Create ticket in discord for the rank'
  - '&8- &7It lasts 90 days and has all top ranks perks'
  - ''
```

</details>

<details>
<summary>Default <code>MEDIA-MENU</code> block as shipped</summary>

```yaml
MEDIA-MENU:
  TITLE: '&8Media Rank'
  SIZE: 27
  MEDIA-BUTTON:
    DISPLAY-NAME: '&eMedia Rank'
    MATERIAL: PINK_DYE
    SLOT: 13
    LORE:
    - '&eReQuirements: (only one needed)'
    - '&e- &f25 average viewers on Stream'
    - '&e- &f5k views on a YouTube Video'
    - '&e- &f25k views on a TikTok'
    - '&e- &f50k views on YouTube Short'
    - ''
    - '&eReminders:'
    - '&8- &7Must have the IP on screen'
    - '&8- &7Must be from the new season'
    - '&8- &7Create ticket in discord for the rank'
    - '&8- &7It lasts 90 days and has all top ranks perks'
    - ''
```

</details>

---

## Section: `RANKS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RANKS-MENU.TITLE` | `string` | Any text | `&8Ranks` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `RANKS-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `RANKS-MENU.BUTTONS`

Every button takes an optional HEAD-TEXTURE, used when its MATERIAL is PLAYER_HEAD. It accepts a skin url or a base64 texture value. Uncomment the line inside a button to give that rank its own head; leaving it commented renders a plain player head.

#### `RANKS-MENU.BUTTONS.DEFAULT`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RANKS-MENU.BUTTONS.DEFAULT.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `RANKS-MENU.BUTTONS.DEFAULT.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `RANKS-MENU.BUTTONS.DEFAULT.DISPLAY-NAME` | `string` | Any text | `&fDefault` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RANKS-MENU.BUTTONS.DEFAULT.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `RANKS-MENU.BUTTONS.DEFAULT.COMMAND` | `string` | Any text | `''` | HEAD-TEXTURE: 'https://textures.minecraft.net/texture/&lt;texture id&gt;' |

<details>
<summary>Default contents of <code>RANKS-MENU.BUTTONS.DEFAULT.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&73 Homes'
  - '&718 Auction Slots'
  - '&718 Order Slots'
```

</details>

#### `RANKS-MENU.BUTTONS.DONUT_PLUS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RANKS-MENU.BUTTONS.DONUT_PLUS.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS.DISPLAY-NAME` | `string` | Any text | `&fDonut&#00A4FC+` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS.COMMAND` | `string` | Any text | `''` | HEAD-TEXTURE: 'https://textures.minecraft.net/texture/&lt;texture id&gt;' |

<details>
<summary>Default contents of <code>RANKS-MENU.BUTTONS.DONUT_PLUS.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&79 Homes'
  - '&745 Auction Slots'
  - '&745 Order Slots'
```

</details>

#### `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.SLOT` | `integer` | Any integer | `14` | Inventory slot, `0` is the top-left cell. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.DISPLAY-NAME` | `string` | Any text | `&fDonut&#00A4FC++` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.COMMAND` | `string` | Any text | `''` | HEAD-TEXTURE: 'https://textures.minecraft.net/texture/&lt;texture id&gt;' |

<details>
<summary>Default contents of <code>RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&727 Homes'
  - '&790 Auction Slots'
  - '&790 Order Slots'
```

</details>

#### `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.DISPLAY-NAME` | `string` | Any text | `&fDonut&#00A4FC+++` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.COMMAND` | `string` | Any text | `''` | HEAD-TEXTURE: 'https://textures.minecraft.net/texture/&lt;texture id&gt;' |

<details>
<summary>Default contents of <code>RANKS-MENU.BUTTONS.DONUT_PLUS_PLUS_PLUS.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&790 Homes'
  - '&790 Auction Slots'
  - '&790 Order Slots'
```

</details>

<details>
<summary>Default <code>RANKS-MENU</code> block as shipped</summary>

```yaml
RANKS-MENU:
  TITLE: '&8Ranks'
  SIZE: 27
  # Every button takes an optional HEAD-TEXTURE, used when its MATERIAL is PLAYER_HEAD. It accepts a
  # skin url or a base64 texture value. Uncomment the line inside a button to give that rank its own
  # head; leaving it commented renders a plain player head.
  BUTTONS:
    DEFAULT:
      MATERIAL: PLAYER_HEAD
      SLOT: 11
      DISPLAY-NAME: '&fDefault'
      LORE:
      - '&73 Homes'
      - '&718 Auction Slots'
      - '&718 Order Slots'
      # HEAD-TEXTURE: 'https://textures.minecraft.net/texture/<texture id>'
      COMMAND: ''
    DONUT_PLUS:
      MATERIAL: PLAYER_HEAD
      SLOT: 13
      DISPLAY-NAME: '&fDonut&#00A4FC+'
      LORE:
      - '&79 Homes'
      - '&745 Auction Slots'
      - '&745 Order Slots'
      # HEAD-TEXTURE: 'https://textures.minecraft.net/texture/<texture id>'
      COMMAND: ''
    DONUT_PLUS_PLUS:
      MATERIAL: PLAYER_HEAD
      SLOT: 14
      DISPLAY-NAME: '&fDonut&#00A4FC++'
      LORE:
      - '&727 Homes'
      - '&790 Auction Slots'
      - '&790 Order Slots'
      # HEAD-TEXTURE: 'https://textures.minecraft.net/texture/<texture id>'
      COMMAND: ''
    DONUT_PLUS_PLUS_PLUS:
      MATERIAL: PLAYER_HEAD
      SLOT: 15
      DISPLAY-NAME: '&fDonut&#00A4FC+++'
      LORE:
      - '&790 Homes'
      - '&790 Auction Slots'
      - '&790 Order Slots'
      # HEAD-TEXTURE: 'https://textures.minecraft.net/texture/<texture id>'
      COMMAND: ''
```

</details>

---

## Section: `STATS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-MENU.TITLE` | `string` | Any text | `&8{username} Stats` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-MENU.SIZE` | `integer` | Any integer | `36` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `STATS-MENU.BUTTONS`

Eight icons only — Design/Dialog API/Stats/View Full Profile. Extra keys are ignored.

#### Entry schema (8 entries)

Each entry under `STATS-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `MONEY` | `&#00FC88Money` | `EMERALD` | `10` |
| `SHARDS` | `&#00FC88Shards` | `AMETHYST_SHARD` | `11` |
| `KILLS` | `&#00FC88Kills` | `DIAMOND_SWORD` | `12` |
| `DEATHS` | `&#00FC88Deaths` | `SKELETON_SKULL` | `13` |
| `PLAYTIME` | `&#00FC88Playtime` | `CLOCK` | `14` |
| `BLOCKS_PLACED` | `&#00FC88Blocks Placed` | `STONE` | `15` |
| `BLOCKS_BROKEN` | `&#00FC88Blocks Broken` | `COBBLESTONE` | `16` |
| `MOBS_KILLED` | `&#00FC88Mobs Killed` | `ZOMBIE_HEAD` | `19` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

### `STATS-MENU.CLOSE-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-MENU.CLOSE-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `STATS-MENU.CLOSE-BUTTON.SLOT` | `integer` | Any integer | `27` | Inventory slot, `0` is the top-left cell. |
| `STATS-MENU.CLOSE-BUTTON.DISPLAY-NAME` | `string` | Any text | `&cBack` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-MENU.CLOSE-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>STATS-MENU.CLOSE-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fClick to return'
```

</details>

<details>
<summary>Default <code>STATS-MENU</code> block as shipped</summary>

```yaml
STATS-MENU:
  TITLE: '&8{username} Stats'
  SIZE: 36
  # Eight icons only — Design/Dialog API/Stats/View Full Profile. Extra keys are ignored.
  BUTTONS:
    MONEY:
      DISPLAY-NAME: '&#00FC88Money'
      MATERIAL: EMERALD
      SLOT: 10
      LORE:
      - '&7{value}'
    SHARDS:
      DISPLAY-NAME: '&#00FC88Shards'
      MATERIAL: AMETHYST_SHARD
      SLOT: 11
      LORE:
      - '&7{value}'
    KILLS:
      DISPLAY-NAME: '&#00FC88Kills'
      MATERIAL: DIAMOND_SWORD
      SLOT: 12
      LORE:
      - '&7{value}'
    DEATHS:
      DISPLAY-NAME: '&#00FC88Deaths'
      MATERIAL: SKELETON_SKULL
      SLOT: 13
      LORE:
      - '&7{value}'
    PLAYTIME:
      DISPLAY-NAME: '&#00FC88Playtime'
      MATERIAL: CLOCK
      SLOT: 14
      LORE:
      - '&7{value}'
    BLOCKS_PLACED:
      DISPLAY-NAME: '&#00FC88Blocks Placed'
      MATERIAL: STONE
      SLOT: 15
      LORE:
      - '&7{value}'
    BLOCKS_BROKEN:
      DISPLAY-NAME: '&#00FC88Blocks Broken'
      MATERIAL: COBBLESTONE
      SLOT: 16
      LORE:
      - '&7{value}'
    MOBS_KILLED:
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `SETTINGS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS-MENU.TITLE` | `string` | Any text | `&8Settings` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SETTINGS-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `SETTINGS-MENU.BUTTONS`

#### Entry schema (51 entries)

Each entry under `SETTINGS-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `PUBLIC_CHAT` | `&#6BF18DPublic Chat` | `OAK_SIGN` | `0` |
| `PRIVATE_MESSAGES` | `&#6BF18DPrivate Messages` | `DARK_OAK_SIGN` | `1` |
| `SERVER_BROADCASTS` | `&#6BF18DServer Broadcasts` | `WARPED_SIGN` | `2` |
| `HOTBAR_MESSAGES` | `&#6BF18DHotbar Messages` | `CRIMSON_SIGN` | `3` |
| `DEATH_MESSAGES` | `&#6BF18DDeath Messages` | `SPRUCE_SIGN` | `4` |
| `ADVANCEMENT_MESSAGES` | `&#6BF18DAdvancement Messages` | `BIRCH_SIGN` | `5` |
| `JOIN_LEAVE_MESSAGES` | `&#6BF18DJoin/Leave Messages` | `JUNGLE_SIGN` | `6` |
| `TEAM_CHAT_VISIBILITY` | `&#6BF18DTeam Chat Visibility` | `MANGROVE_SIGN` | `7` |
| `AMETHYST_BREAK_MESSAGES` | `&#6BF18DAmethyst Break Messages` | `CHERRY_SIGN` | `8` |
| `PAY_ALERTS` | `&#6BF18DPay Alerts` | `GOLD_NUGGET` | `9` |
| `TELEPORT_ALERTS` | `&#6BF18DTeleport Alerts` | `ENDER_PEARL` | `10` |
| `BOUNTY_ALERTS` | `&#6BF18DBounty Alerts` | `BAMBOO_SIGN` | `11` |
| `AUCTION_NOTIFICATIONS` | `&#6BF18DAuction Alerts` | `ACACIA_SIGN` | `12` |
| `ORDER_NOTIFICATIONS` | `&#6BF18DOrder Alerts` | `MAP` | `13` |
| `NOTIFICATION_SOUNDS` | `&#6BF18DNotification Sounds` | `NOTE_BLOCK` | `14` |
| `FOLLOW_ALERT_SETTINGS` | `&#6BF18DFollow Alerts` | `PLAYER_HEAD` | `15` |
| `KEY_ALL_NOTIFICATIONS` | `&#6BF18DKey All Notifications` | `TRIPWIRE_HOOK` | `16` |
| `RTP_COORDINATES` | `&#6BF18DRTP Coordinates` | `COMPASS` | `17` |
| `FAST_CRYSTALS` | `&#6BF18DFast Crystals` | `END_CRYSTAL` | `18` |
| `EXPLOSION_PARTICLES` | `&#6BF18DExplosion Particles` | `TNT` | `19` |
| `EXPLOSION_SOUNDS` | `&#6BF18DExplosion Sounds` | `GOAT_HORN` | `20` |
| `COMBAT_TIMER` | `&#6BF18DCombat Timer` | `DIAMOND_SWORD` | `21` |
| `DISPLAY_DONUT_PLUS` | `&#6BF18DDisplay Donut+` | `FEATHER` | `22` |
| `MONEY_NAMETAGS` | `&#6BF18DMoney Nametags` | `NAME_TAG` | `23` |
| `WORTH_DISPLAY` | `&#6BF18DWorth Display` | `DIAMOND` | `24` |
| `TPA_CONFIRM_MENUS` | `&#6BF18DTpa Confirm Menus` | `COMPASS` | `25` |
| `TPA_REQUESTS` | `&#6BF18DTpa Requests` | `ENDER_PEARL` | `27` |
| `TPA_HERE_REQUESTS` | `&#6BF18DTpa Here Requests` | `ENDER_EYE` | `28` |
| `PAYMENTS` | `&#6BF18DPayments` | `EMERALD` | `29` |
| `RANDOMIZED_COORDS` | `&#6BF18DRandomized Coords` | `NETHERITE_INGOT` | `30` |
| `DUEL_REQUESTS` | `&#6BF18DDuel Requests` | `IRON_SWORD` | `31` |
| `PAY_CONFIRM_MENUS` | `&#6BF18DPay Confirm Menus` | `PAPER` | `32` |
| `AUTO_CONFIRM_TPAS` | `&#6BF18DAuto-Confirm Tpas` | `CLOCK` | `33` |
| `TEAM_INVITES` | `&#6BF18DTeam Invites` | `SHIELD` | `34` |
| `DUEL_MUSIC` | `&#6BF18DDuel Music` | `JUKEBOX` | `35` |
| `SCOREBOARD_VISIBILITY` | `&#6BF18DShow Scoreboard` | `LECTERN` | `36` |
| `SHOW_MONEY` | `&#6BF18DShow Money` | `GOLD_INGOT` | `37` |
| `SHOW_SHARDS` | `&#6BF18DShow Shards` | `AMETHYST_SHARD` | `38` |
| `SHOW_KILLS` | `&#6BF18DShow Kills` | `IRON_SWORD` | `39` |
| `SHOW_DEATHS` | `&#6BF18DShow Deaths` | `SKELETON_SKULL` | `40` |
| `SHOW_PLAYTIME` | `&#6BF18DShow Playtime` | `CLOCK` | `41` |
| `CLEAR_ENTITIES_MESSAGES` | `&#6BF18DClear Entities Messages` | `HOPPER` | `42` |
| `QUICK_AUCTION_PURCHASE` | `&#6BF18DQuick Auction Purchase` | `GOLD_INGOT` | `45` |
| `QUICK_AUCTION_SELL` | `&#6BF18DQuick Auction Sell` | `GOLD_BLOCK` | `46` |
| `DISABLE_MOB_SPAWN` | `&#6BF18DDisable Mob Spawns` | `ZOMBIE_HEAD` | `47` |
| `DISABLE_PHANTOM_SPAWN` | `&#6BF18DDisable Phantom Spawn` | `PHANTOM_MEMBRANE` | `48` |
| `NIGHT_VISION` | `&#6BF18DNight Vision` | `GOLDEN_CARROT` | `49` |
| `DESTROY_PEARL_ON_DEATH` | `&#6BF18DDestroy Pearl on Death` | `PAPER` | `50` |
| `HIDE_ALL_PLAYERS` | `&#6BF18DHide All Players` | `PLAYER_HEAD` | `51` |
| `TOTEM_PARTICLES` | `&#6BF18DTotem Particles` | `TOTEM_OF_UNDYING` | `52` |
| `QUIET_SPAWN` | `&#6BF18DQuiet Spawn Teleportation` | `RESPAWN_ANCHOR` | `53` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

<details>
<summary>Default <code>SETTINGS-MENU</code> block as shipped</summary>

```yaml
SETTINGS-MENU:
  TITLE: '&8Settings'
  SIZE: 54
  BUTTONS:
    # Buttons are grouped a row at a time: chat and messages on the first row,
    # alerts on the second, gameplay and display on the third, who may reach you on
    # the fourth, the scoreboard on the fifth, and the world around you on the last.
    # Every setting below accepts two optional keys:
    #   DEFAULT: <value>  Starting value for players who never touched the setting.
    #                     On/off buttons take true or false. The privacy buttons
    #                     (PRIVATE_MESSAGES, TPA_REQUESTS, TPA_HERE_REQUESTS, PAYMENTS)
    #                     take ANYONE, FRIENDS_FOLLOWED or OFF. DEATH_MESSAGES,
    #                     JOIN_LEAVE_MESSAGES and ADVANCEMENT_MESSAGES take OFF or
    #                     FRIENDS_FOLLOWED (true/false also work as shortcuts).
    #   ENABLED: false    Removes the option from /settings and pins every player to the
    #                     DEFAULT above. Use this instead of deleting the block - deleted
    #                     blocks are restored from the bundled defaults on the next start.
    # Example, hide advancement messages and keep them off for everyone:
    # ADVANCEMENT_MESSAGES:
    #   DEFAULT: OFF
    #   ENABLED: false
    # Custom redirect buttons can also be added here:
    # EXTERNAL_FLY:
    #   DISPLAY-NAME: '&bFlight Mode'
    #   MATERIAL: FEATHER
    #   SLOT: 25
    #   COMMAND: '[player] /fly'
    #   STATUS-PLACEHOLDER: '%cmi_user_flying%'
    #   LORE:
    #   - '&7Toggle flight via external plugin'
    #   - '&fCurrently: {status}'
    # Row 1 - chat and messages
    PUBLIC_CHAT:
      DISPLAY-NAME: '&#6BF18DPublic Chat'
      MATERIAL: OAK_SIGN
      SLOT: 0
      LORE:
      - '&7Receive public chat messages'
      - '&fCurrently: {status}'
    PRIVATE_MESSAGES:
      DISPLAY-NAME: '&#6BF18DPrivate Messages'
      MATERIAL: DARK_OAK_SIGN
      SLOT: 1
      LORE:
      - '&7Private messages privacy settings'
      - '&fCurrently: {status}'
    SERVER_BROADCASTS:
      DISPLAY-NAME: '&#6BF18DServer Broadcasts'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `LEADERBOARDS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARDS-MENU.TITLE` | `string` | Any text | `&8Leaderboards` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `LEADERBOARDS-MENU.SIZE` | `integer` | Any integer | `36` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `LEADERBOARDS-MENU.TYPE-MENU`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARDS-MENU.TYPE-MENU.TITLE` | `string` | Any text | `&8{type}` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `LEADERBOARDS-MENU.TYPE-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

#### `LEADERBOARDS-MENU.TYPE-MENU.BUTTON`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARDS-MENU.TYPE-MENU.BUTTON.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `LEADERBOARDS-MENU.TYPE-MENU.BUTTON.DISPLAY-NAME` | `string` | Any text | `&#6BF18D{player}` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LEADERBOARDS-MENU.TYPE-MENU.BUTTON.LORE` | `string` | Any text | `&f{type}: &7{value} &#6BF18D(#{position})` | Tooltip lines under the item name. |

### `LEADERBOARDS-MENU.TYPE-NAMES`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LEADERBOARDS-MENU.TYPE-NAMES.money` | `string` | Any text | `Money` | Money. |
| `LEADERBOARDS-MENU.TYPE-NAMES.moneySpent` | `string` | Any text | `Money Spent` | Moneyspent. |
| `LEADERBOARDS-MENU.TYPE-NAMES.moneyMade` | `string` | Any text | `Money Made` | Moneymade. |
| `LEADERBOARDS-MENU.TYPE-NAMES.kills` | `string` | Any text | `Kills` | Kills. |
| `LEADERBOARDS-MENU.TYPE-NAMES.deaths` | `string` | Any text | `Deaths` | Deaths. |
| `LEADERBOARDS-MENU.TYPE-NAMES.playtime` | `string` | Any text | `Playtime` | Playtime. |
| `LEADERBOARDS-MENU.TYPE-NAMES.blocksPlaced` | `string` | Any text | `Blocks Placed` | Blocksplaced. |
| `LEADERBOARDS-MENU.TYPE-NAMES.blocksBroken` | `string` | Any text | `Blocks Broken` | Blocksbroken. |
| `LEADERBOARDS-MENU.TYPE-NAMES.mobsKilled` | `string` | Any text | `Mobs Killed` | Mobskilled. |
| `LEADERBOARDS-MENU.TYPE-NAMES.killStreak` | `string` | Any text | `Kill Streak` | Killstreak. |
| `LEADERBOARDS-MENU.TYPE-NAMES.highestKillStreak` | `string` | Any text | `Highest Kill Streak` | Highestkillstreak. |
| `LEADERBOARDS-MENU.TYPE-NAMES.shards` | `string` | Any text | `Shards` | Shards. |
| `LEADERBOARDS-MENU.TYPE-NAMES.bounties` | `string` | Any text | `Bounties` | Bounties. |

### `LEADERBOARDS-MENU.BUTTONS`

#### Entry schema (13 entries)

Each entry under `LEADERBOARDS-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `TYPE` | `MATERIAL` |
| :--- | :--- | :--- | :--- |
| `MONEY` | `&#6BF18DMoney Leaderboard` | `money` | `EMERALD` |
| `SHARDS` | `&#6BF18DShards Leaderboard` | `shards` | `AMETHYST_SHARD` |
| `KILLS` | `&#6BF18DKills Leaderboard` | `kills` | `DIAMOND_SWORD` |
| `DEATHS` | `&#6BF18DDeaths Leaderboard` | `deaths` | `SKELETON_SKULL` |
| `PLAYTIME` | `&#6BF18DPlaytime Leaderboard` | `playtime` | `CLOCK` |
| `BLOCKS_PLACED` | `&#6BF18DBlocks Placed Leaderboard` | `blocksPlaced` | `STONE` |
| `BLOCKS_BROKEN` | `&#6BF18DBlocks Broken Leaderboard` | `blocksBroken` | `COBBLESTONE` |
| `MOBS_KILLED` | `&#6BF18DMobs Killed Leaderboard` | `mobsKilled` | `ZOMBIE_HEAD` |
| `KILL_STREAK` | `&#6BF18DKill Streak Leaderboard` | `killStreak` | `DIAMOND_AXE` |
| `HIGHEST_KILL_STREAK` | `&#6BF18DHighest Kill Streak Leaderboard` | `highestKillStreak` | `NETHERITE_SWORD` |
| `MONEY_SPENT` | `&#6BF18DMoney Spent On Shop` | `moneySpent` | `GOLD_NUGGET` |
| `MONEY_MADE` | `&#6BF18DMoney Made On /Sell` | `moneyMade` | `IRON_NUGGET` |
| `BOUNTIES` | `&#6BF18DBounties Leaderboard` | `bounties` | `WITHER_SKELETON_SKULL` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TYPE` | `string` | Any text | Required | Which option this section uses. |

<details>
<summary>Default <code>LEADERBOARDS-MENU</code> block as shipped</summary>

```yaml
LEADERBOARDS-MENU:
  TITLE: '&8Leaderboards'
  SIZE: 36
  TYPE-MENU:
    TITLE: '&8{type}'
    SIZE: 54
    BUTTON:
      MATERIAL: PLAYER_HEAD
      DISPLAY-NAME: '&#6BF18D{player}'
      LORE: '&f{type}: &7{value} &#6BF18D(#{position})'
  TYPE-NAMES:
    money: Money
    moneySpent: Money Spent
    moneyMade: Money Made
    kills: Kills
    deaths: Deaths
    playtime: Playtime
    blocksPlaced: Blocks Placed
    blocksBroken: Blocks Broken
    mobsKilled: Mobs Killed
    killStreak: Kill Streak
    highestKillStreak: Highest Kill Streak
    shards: Shards
    bounties: Bounties
  BUTTONS:
    MONEY:
      TYPE: money
      DISPLAY-NAME: '&#6BF18DMoney Leaderboard'
      MATERIAL: EMERALD
      SLOT: 10
      LORE:
      - '&fClick to view MONEY leaderboard'
    SHARDS:
      TYPE: shards
      DISPLAY-NAME: '&#6BF18DShards Leaderboard'
      MATERIAL: AMETHYST_SHARD
      SLOT: 11
      LORE:
      - '&fClick to view SHARDS leaderboard'
    KILLS:
      TYPE: kills
      DISPLAY-NAME: '&#6BF18DKills Leaderboard'
      MATERIAL: DIAMOND_SWORD
      SLOT: 12
      LORE:
      - '&fClick to view KILLS leaderboard'
    DEATHS:
      TYPE: deaths
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `PROGRESS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.ENABLED` | `boolean` | `true`, `false` | `true` | When `true`, sell category multipliers scale payouts from progress milestones. When `false`, all sell and spawner payouts use base worth (`1.0x`), milestone progress is paused, and `/sellmulti` commands report multipliers disabled. |
| `PROGRESS-MENU.PROGRESS-BAR` | `string` | Any text | `■` | Progress bar. |
| `PROGRESS-MENU.LEVEL` | `list` | A list of values | _list of 20 items_ | The level list. |

<details>
<summary>Default contents of <code>PROGRESS-MENU.LEVEL</code> (20 items)</summary>

```yaml
LEVEL:
  - 25000
  - 150000
  - 500000
  - 1000000
  - 5000000
  - 25000000
  - 250000000
  - 550000000
  - 850000000
  - 1000000000
  - 2000000000
  - 4000000000
  - 8000000000
  - 10000000000
  - 20000000000
  - 40000000000
  - 80000000000
  - 160000000000
  - 320000000000
  - 640000000000
```

</details>

### `PROGRESS-MENU.TITLE`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.TITLE.CROPS` | `string` | Any text | `&8CROPS PROGRESS` | Crops. |
| `PROGRESS-MENU.TITLE.ORES` | `string` | Any text | `&8ORE PROGRESS` | Ores. |
| `PROGRESS-MENU.TITLE.MOBS` | `string` | Any text | `&8MOB DROPS PROGRESS` | Mobs. |
| `PROGRESS-MENU.TITLE.NATURAL` | `string` | Any text | `&8NATURAL ITEMS PROGRESS` | Natural. |
| `PROGRESS-MENU.TITLE.ARMOR_AND_TOOLS` | `string` | Any text | `&8ARMOR AND TOOLS PROGRESS` | Armor and tools. |
| `PROGRESS-MENU.TITLE.FISH` | `string` | Any text | `&8FISH PROGRESS` | Fish. |
| `PROGRESS-MENU.TITLE.BOOK` | `string` | Any text | `&8ENCHANTED BOOK PROGRESS` | Book. |
| `PROGRESS-MENU.TITLE.POTIONS` | `string` | Any text | `&8POTIONS PROGRESS` | Potions. |
| `PROGRESS-MENU.TITLE.BLOCKS` | `string` | Any text | `&8BLOCKS PROGRESS` | Blocks. |

### `PROGRESS-MENU.TYPE-BUTTON`

#### `PROGRESS-MENU.TYPE-BUTTON.TITLE`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.CROPS` | `string` | Any text | `&#6BF18DCROPS` | Crops. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.ORES` | `string` | Any text | `&#6BF18DORE` | Ores. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.MOBS` | `string` | Any text | `&#6BF18DMOB` | Mobs. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.NATURAL` | `string` | Any text | `&#6BF18DNATURAL ITEMS` | Natural. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.ARMOR_AND_TOOLS` | `string` | Any text | `&#6BF18DARMOR AND TOOLS` | Armor and tools. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.FISH` | `string` | Any text | `&#6BF18DFISH` | Fish. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.BOOK` | `string` | Any text | `&#6BF18DENCHANTED BOOK` | Book. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.POTIONS` | `string` | Any text | `&#6BF18DPOTIONS` | Potions. |
| `PROGRESS-MENU.TYPE-BUTTON.TITLE.BLOCKS` | `string` | Any text | `&#6BF18DBLOCKS` | Blocks. |

#### `PROGRESS-MENU.TYPE-BUTTON.LORE`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.CROPS` | `list` | A list of values | _list of 4 items_ | The crops list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.ORES` | `list` | A list of values | _list of 4 items_ | The ores list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.MOBS` | `list` | A list of values | _list of 4 items_ | The mobs list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.NATURAL` | `list` | A list of values | _list of 4 items_ | The natural list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.ARMOR_AND_TOOLS` | `list` | A list of values | _list of 4 items_ | The armor and tools list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.FISH` | `list` | A list of values | _list of 4 items_ | The fish list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.BOOK` | `list` | A list of values | _list of 4 items_ | The book list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.POTIONS` | `list` | A list of values | _list of 4 items_ | The potions list. |
| `PROGRESS-MENU.TYPE-BUTTON.LORE.BLOCKS` | `list` | A list of values | _list of 4 items_ | The blocks list. |

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.CROPS</code> (4 items)</summary>

```yaml
CROPS:
  - '&7Sell crops and farming materials to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.ORES</code> (4 items)</summary>

```yaml
ORES:
  - '&7Sell ores and mining materials to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.MOBS</code> (4 items)</summary>

```yaml
MOBS:
  - '&7Sell mob drops and combat materials to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.NATURAL</code> (4 items)</summary>

```yaml
NATURAL:
  - '&7Sell natural materials and trees to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.ARMOR_AND_TOOLS</code> (4 items)</summary>

```yaml
ARMOR_AND_TOOLS:
  - '&7Sell armor and tools to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.FISH</code> (4 items)</summary>

```yaml
FISH:
  - '&7Sell fish and other fishing loot to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.BOOK</code> (4 items)</summary>

```yaml
BOOK:
  - '&7Sell books and enchanted books to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.POTIONS</code> (4 items)</summary>

```yaml
POTIONS:
  - '&7Sell potions and brewing materials to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

<details>
<summary>Default contents of <code>PROGRESS-MENU.TYPE-BUTTON.LORE.BLOCKS</code> (4 items)</summary>

```yaml
BLOCKS:
  - '&7Sell blocks and placeable items to'
  - '&7upgrade your sell multiplier!'
  - ''
  - '&e► Click to view items in this category'
```

</details>

#### `PROGRESS-MENU.TYPE-BUTTON.MATERIAL`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.CROPS` | `string` | Any text | `WHEAT` | Crops. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.ORES` | `string` | Any text | `DIAMOND` | Ores. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.MOBS` | `string` | Any text | `BONE` | Mobs. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.NATURAL` | `string` | Any text | `OAK_LEAVES` | Natural. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.ARMOR_AND_TOOLS` | `string` | Any text | `NETHERITE_HELMET` | Armor and tools. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.FISH` | `string` | Any text | `TROPICAL_FISH` | Fish. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.BOOK` | `string` | Any text | `BOOK` | Book. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.POTIONS` | `string` | Any text | `BREWING_STAND` | Potions. |
| `PROGRESS-MENU.TYPE-BUTTON.MATERIAL.BLOCKS` | `string` | Any text | `BRICK` | Blocks. |

### `PROGRESS-MENU.WORKING-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.WORKING-BUTTON.TITLE` | `string` | Any text | `&eWorking` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PROGRESS-MENU.WORKING-BUTTON.MATERIAL` | `string` | Any text | `YELLOW_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PROGRESS-MENU.WORKING-BUTTON.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PROGRESS-MENU.WORKING-BUTTON.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '{porcentage_level} &f{next_multiplier} {porcentage}%'
  - '&7{current_earned}/{next_goal}'
```

</details>

### `PROGRESS-MENU.COMPLETED-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROGRESS-MENU.COMPLETED-BUTTON.TITLE` | `string` | Any text | `&aCompleted` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PROGRESS-MENU.COMPLETED-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PROGRESS-MENU.COMPLETED-BUTTON.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PROGRESS-MENU.COMPLETED-BUTTON.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '{porcentage_level} &f{next_multiplier} {porcentage}%'
  - '&7{current_earned}/{next_goal}'
```

</details>

<details>
<summary>Default <code>PROGRESS-MENU</code> block as shipped</summary>

```yaml
PROGRESS-MENU:
  ENABLED: true
  PROGRESS-BAR: "■"
  LEVEL:
  - 25000
  - 150000
  - 500000
  - 1000000
  - 5000000
  - 25000000
  - 250000000
  - 550000000
  - 850000000
  - 1000000000
  - 2000000000
  - 4000000000
  - 8000000000
  - 10000000000
  - 20000000000
  - 40000000000
  - 80000000000
  - 160000000000
  - 320000000000
  - 640000000000
  TITLE:
    CROPS: '&8CROPS PROGRESS'
    ORES: '&8ORE PROGRESS'
    MOBS: '&8MOB DROPS PROGRESS'
    NATURAL: '&8NATURAL ITEMS PROGRESS'
    ARMOR_AND_TOOLS: '&8ARMOR AND TOOLS PROGRESS'
    FISH: '&8FISH PROGRESS'
    BOOK: '&8ENCHANTED BOOK PROGRESS'
    POTIONS: '&8POTIONS PROGRESS'
    BLOCKS: '&8BLOCKS PROGRESS'
  TYPE-BUTTON:
    TITLE:
      CROPS: '&#6BF18DCROPS'
      ORES: '&#6BF18DORE'
      MOBS: '&#6BF18DMOB'
      NATURAL: '&#6BF18DNATURAL ITEMS'
      ARMOR_AND_TOOLS: '&#6BF18DARMOR AND TOOLS'
      FISH: '&#6BF18DFISH'
      BOOK: '&#6BF18DENCHANTED BOOK'
      POTIONS: '&#6BF18DPOTIONS'
      BLOCKS: '&#6BF18DBLOCKS'
    LORE:
      CROPS:
      - '&7Sell crops and farming materials to'
      - '&7upgrade your sell multiplier!'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `SELL-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL-MENU.TITLE` | `string` | Any text | `&8Place Items In Here To Sell` | Title text. Supports `&` colours and `&#RRGGBB` hex. |

### `SELL-MENU.SELL-BUTTON`

Selling is always confirmed. Items sit in the grid until the player clicks the sell button, and closing the menu hands them straight back. Everything before SLOT is the sellable grid, so lowering SLOT shrinks the menu.

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL-MENU.SELL-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `SELL-MENU.SELL-BUTTON.SLOT` | `integer` | Any integer | `53` | Inventory slot, `0` is the top-left cell. |
| `SELL-MENU.SELL-BUTTON.TITLE` | `string` | Any text | `&#00FC00$ &f{worth}` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELL-MENU.SELL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SELL-MENU.SELL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to sell items'
```

</details>

<details>
<summary>Default <code>SELL-MENU</code> block as shipped</summary>

```yaml
SELL-MENU:
  TITLE: '&8Place Items In Here To Sell'
  # Selling is always confirmed. Items sit in the grid until the player clicks the sell button,
  # and closing the menu hands them straight back. Everything before SLOT is the sellable grid,
  # so lowering SLOT shrinks the menu.
  SELL-BUTTON:
    MATERIAL: 'LIME_STAINED_GLASS_PANE'
    SLOT: 53
    TITLE: '&#00FC00$ &f{worth}'
    LORE:
    - '&7Click to sell items'
```

</details>

---

## Section: `WORTH-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `WORTH-MENU.TITLE` | `string` | Any text | `&8Item Prices` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `WORTH-MENU.FORMAT` | `string` | Any text | `&7Worth: &a${price}` | How the value is printed. Placeholders are listed on the feature page. |

### `WORTH-MENU.SORT-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `WORTH-MENU.SORT-BUTTON.TITLE` | `string` | Any text | `&aSort` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `WORTH-MENU.SORT-BUTTON.MATERIAL` | `string` | Any text | `CAULDRON` | Bukkit `Material` name for the icon. |

<details>
<summary>Default <code>WORTH-MENU</code> block as shipped</summary>

```yaml
WORTH-MENU:
  TITLE: '&8Item Prices'
  FORMAT: '&7Worth: &a${price}'
  SORT-BUTTON:
    TITLE: '&aSort'
    MATERIAL: CAULDRON
```

</details>

---

## Section: `TPA-CONFIRM-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TPA-CONFIRM-MENU.TITLE` | `string` | Any text | `&8Confirm TPA {here}` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `TPA-CONFIRM-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `TPA-CONFIRM-MENU.BUTTONS`

#### Entry schema (5 entries)

Each entry under `TPA-CONFIRM-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `NAME` | `MATERIAL` |
| :--- | :--- | :--- |
| `CANCEL` | `&cCancel` | `RED_STAINED_GLASS_PANE` |
| `CONFIRM` | `&aConfirm` | `LIME_STAINED_GLASS_PANE` |
| `PLAYER` | `&#00FC00Player` | `PLAYER_HEAD` |
| `LOCATION` | `&#6BF18DLocation` | `GRASS_BLOCK` |
| `REGION` | `&#6BF18DRegion` | `FEATHER` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |

<details>
<summary>Default <code>TPA-CONFIRM-MENU</code> block as shipped</summary>

```yaml
TPA-CONFIRM-MENU:
  TITLE: '&8Confirm TPA {here}'
  SIZE: 27
  BUTTONS:
    CANCEL:
      MATERIAL: RED_STAINED_GLASS_PANE
      NAME: '&cCancel'
      LORE:
      - '&fCLICK TO CANCEL'
    CONFIRM:
      MATERIAL: LIME_STAINED_GLASS_PANE
      NAME: '&aConfirm'
      LORE:
      - '&fCLICK TO CONFIRM'
    PLAYER:
      MATERIAL: PLAYER_HEAD
      NAME: '&#00FC00Player'
      LORE:
      - '&7{player}'
    LOCATION:
      NAME: '&#6BF18DLocation'
      MATERIAL: GRASS_BLOCK
      LORE:
      - '&7{world}'
    REGION:
      NAME: '&#6BF18DRegion'
      MATERIAL: FEATHER
      LORE:
      - '&7NA East (&#0069D6${ping}ms&7)'
```

</details>

---

## Section: `BOUNTIES-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOUNTIES-MENU.TITLE` | `string` | Any text | `&8Bounties (Page {page})` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `BOUNTIES-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `BOUNTIES-MENU.MAX-ITEMS-PER-PAGE` | `integer` | Any integer | `45` | Max items per page. |

### Entry schema (6 entries)

Each entry under `BOUNTIES-MENU` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `BOUNTY-BUTTON` | `&#00fc88{player}` | `PLAYER_HEAD` | — |
| `PREVIOUS-PAGE-BUTTON` | `&fPrevious page` | `ARROW` | `45` |
| `SORT-BUTTON` | `&fSort` | `HOPPER` | `48` |
| `REFRESH-BUTTON` | `&fBounties` | `SKELETON_SKULL` | `49` |
| `SEARCH-BUTTON` | `&fSearch` | `OAK_SIGN` | `50` |
| `NEXT-PAGE-BUTTON` | `&fNext page` | `ARROW` | `53` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `LORE` | `list` | A list of values | Optional (5/6) | Tooltip lines under the item name. |
| `SLOT` | `integer` | Any integer | Optional (5/6) | Inventory slot, `0` is the top-left cell. |
| `LORE-AMOUNT` | `list` | A list of values | Optional (1/6) | The lore amount list. |
| `LORE-RECENT` | `list` | A list of values | Optional (1/6) | The lore recent list. |

<details>
<summary>Default <code>BOUNTIES-MENU</code> block as shipped</summary>

```yaml
BOUNTIES-MENU:
  TITLE: '&8Bounties (Page {page})'
  SIZE: 54
  MAX-ITEMS-PER-PAGE: 45
  BOUNTY-BUTTON:
    MATERIAL: PLAYER_HEAD
    NAME: '&#00fc88{player}'
    LORE:
    - '&fBounty: &7${price}'
  PREVIOUS-PAGE-BUTTON:
    SLOT: 45
    MATERIAL: ARROW
    NAME: '&fPrevious page'
    LORE:
    - '&o&7Click to view previous page'
  SORT-BUTTON:
    SLOT: 48
    MATERIAL: HOPPER
    NAME: '&fSort'
    LORE-AMOUNT:
    - '&fClick to sort (Amount)'
    LORE-RECENT:
    - '&fClick to sort (Recently Set)'
  REFRESH-BUTTON:
    SLOT: 49
    MATERIAL: SKELETON_SKULL
    NAME: '&fBounties'
    LORE:
    - '&fClick to refresh'
    - ''
    - '&7Set a bounty using this:'
    - '&7/bounty add (player) (amount)'
  SEARCH-BUTTON:
    SLOT: 50
    MATERIAL: OAK_SIGN
    NAME: '&fSearch'
    LORE:
    - '&7Click to search'
  NEXT-PAGE-BUTTON:
    SLOT: 53
    MATERIAL: ARROW
    NAME: '&fNext page'
    LORE:
    - '&o&7Click to view next page'
```

</details>

---

## Section: `BOUNTY-CONFIRM-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOUNTY-CONFIRM-MENU.TITLE` | `string` | Any text | `&8Confirm Bounty` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `BOUNTY-CONFIRM-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `BOUNTY-CONFIRM-MENU.CANCEL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOUNTY-CONFIRM-MENU.CANCEL-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `BOUNTY-CONFIRM-MENU.CANCEL-BUTTON.NAME` | `string` | Any text | `&#FC0000Cancel` | Display name shown to players. |
| `BOUNTY-CONFIRM-MENU.CANCEL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>BOUNTY-CONFIRM-MENU.CANCEL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to cancel the bounty adding!'
```

</details>

### `BOUNTY-CONFIRM-MENU.PLAYER-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOUNTY-CONFIRM-MENU.PLAYER-BUTTON.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `BOUNTY-CONFIRM-MENU.PLAYER-BUTTON.NAME` | `string` | Any text | `&#00FC00{player}` | Display name shown to players. |
| `BOUNTY-CONFIRM-MENU.PLAYER-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>BOUNTY-CONFIRM-MENU.PLAYER-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fYou''re going to set &#00FC00{amount}'
```

</details>

### `BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON.NAME` | `string` | Any text | `&#00FC00Confirm` | Display name shown to players. |
| `BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>BOUNTY-CONFIRM-MENU.CONFIRM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to confirm to add {amount} bounty!'
```

</details>

<details>
<summary>Default <code>BOUNTY-CONFIRM-MENU</code> block as shipped</summary>

```yaml
BOUNTY-CONFIRM-MENU:
  TITLE: '&8Confirm Bounty'
  SIZE: 27
  CANCEL-BUTTON:
    MATERIAL: RED_STAINED_GLASS_PANE
    NAME: '&#FC0000Cancel'
    LORE:
    - '&7Click to cancel the bounty adding!'
  PLAYER-BUTTON:
    MATERIAL: PLAYER_HEAD
    NAME: '&#00FC00{player}'
    LORE:
    - '&fYou''re going to set &#00FC00{amount}'
  CONFIRM-BUTTON:
    MATERIAL: LIME_STAINED_GLASS_PANE
    NAME: '&#00FC00Confirm'
    LORE:
    - '&7Click to confirm to add {amount} bounty!'
```

</details>

---

## Section: `SELL-HISTORY-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL-HISTORY-MENU.TITLE` | `string` | Any text | `&8Sell History` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELL-HISTORY-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `SELL-HISTORY-MENU.MAX-ITEMS-PER-PAGE` | `integer` | Any integer | `45` | Max items per page. |

### `SELL-HISTORY-MENU.BUTTONS`

#### `SELL-HISTORY-MENU.BUTTONS.SORT`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL-HISTORY-MENU.BUTTONS.SORT.MATERIAL` | `string` | Any text | `ANVIL` | Bukkit `Material` name for the icon. |
| `SELL-HISTORY-MENU.BUTTONS.SORT.NAME` | `string` | Any text | `&aSort` | Display name shown to players. |
| `SELL-HISTORY-MENU.BUTTONS.SORT.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `SELL-HISTORY-MENU.BUTTONS.SORT.SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |

<details>
<summary>Default contents of <code>SELL-HISTORY-MENU.BUTTONS.SORT.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&fClick to sort'
  - ''
  - '&7({sort_state})'
```

</details>

#### `SELL-HISTORY-MENU.BUTTONS.MATERIAL-ITEM`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL-HISTORY-MENU.BUTTONS.MATERIAL-ITEM.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SELL-HISTORY-MENU.BUTTONS.MATERIAL-ITEM.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&fTotal price: &a{price}'
  - '&fTotal amount: {amount}'
```

</details>

<details>
<summary>Default <code>SELL-HISTORY-MENU</code> block as shipped</summary>

```yaml
SELL-HISTORY-MENU:
  TITLE: '&8Sell History'
  SIZE: 54
  MAX-ITEMS-PER-PAGE: 45
  BUTTONS:
    SORT:
      MATERIAL: ANVIL
      NAME: '&aSort'
      LORE:
      - '&fClick to sort'
      - ''
      - '&7({sort_state})'
      SLOT: 49
    MATERIAL-ITEM:
      LORE:
      - '&fTotal price: &a{price}'
      - '&fTotal amount: {amount}'
```

</details>

---

## Section: `PURCHASE-SHOP-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.TITLE` | `string` | Any text | `&8Confirmation Menu` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PURCHASE-SHOP-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `PURCHASE-SHOP-MENU.BUTTONS`

#### `PURCHASE-SHOP-MENU.BUTTONS.MAIN`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.MAIN.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |

##### `PURCHASE-SHOP-MENU.BUTTONS.MAIN.LORE`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.MAIN.LORE.MONEY` | `string` | Any text | `&fBUY PRICE: &a${price}` | Money. |
| `PURCHASE-SHOP-MENU.BUTTONS.MAIN.LORE.SHARD` | `string` | Any text | `&fBUY PRICE: &#A303F9{price_formatted}` | Shard. |
| `PURCHASE-SHOP-MENU.BUTTONS.MAIN.LORE.DEFAULT` | `string` | Any text | `&fBUY PRICE: &a${price}` | Default. |

#### `PURCHASE-SHOP-MENU.BUTTONS.CANCEL`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.CANCEL.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.CANCEL.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PURCHASE-SHOP-MENU.BUTTONS.CANCEL.NAME` | `string` | Any text | `&cCancel` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.CANCEL.LORE` | `string` | Any text | `&fCLICK TO CANCEL` | Tooltip lines under the item name. |

#### `PURCHASE-SHOP-MENU.BUTTONS.CONFIRM`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.NAME` | `string` | Any text | `&aConfirm` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.CONFIRM.LORE` | `string` | Any text | `&fCLICK TO BUY` | Tooltip lines under the item name. |

#### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST`

##### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1.SLOT` | `integer` | Any integer | `24` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1.NAME` | `string` | Any text | `&aAdd 1` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_1.INCREMENT` | `integer` | Any integer | `1` | Increment. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10.SLOT` | `integer` | Any integer | `25` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10.NAME` | `string` | Any text | `&aAdd 10` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.ADD_10.INCREMENT` | `integer` | Any integer | `10` | Increment. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64.SLOT` | `integer` | Any integer | `26` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64.NAME` | `string` | Any text | `&aSet To 64` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.ADD.SET_64.INCREMENT` | `integer` | Any integer | `64` | Increment. |

##### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1.SLOT` | `integer` | Any integer | `20` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1.NAME` | `string` | Any text | `&cRemove 1` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_1.DECREMENT` | `integer` | Any integer | `1` | Decrement. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10.SLOT` | `integer` | Any integer | `19` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10.NAME` | `string` | Any text | `&cRemove 10` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_10.DECREMENT` | `integer` | Any integer | `10` | Decrement. |

###### `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64`

###### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64.SLOT` | `integer` | Any integer | `18` | Inventory slot, `0` is the top-left cell. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64.NAME` | `string` | Any text | `&cRemove 64` | Display name shown to players. |
| `PURCHASE-SHOP-MENU.BUTTONS.QUANTITY_ADJUST.REMOVE.REMOVE_64.DECREMENT` | `integer` | Any integer | `64` | Decrement. |

### `PURCHASE-SHOP-MENU.RESTRICTIONS`

#### `PURCHASE-SHOP-MENU.RESTRICTIONS.TOTEM_OF_UNDYING`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.TOTEM_OF_UNDYING.MAX_QUANTITY` | `integer` | Any integer | `1` | Max quantity. |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.TOTEM_OF_UNDYING.MIN_QUANTITY` | `integer` | Any integer | `1` | Min quantity. |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.TOTEM_OF_UNDYING.HIDE_QUANTITY_BUTTONS` | `boolean` | `true`, `false` | `true` | On/off for hide quantity buttons. |

#### `PURCHASE-SHOP-MENU.RESTRICTIONS.ENDER_PEARL`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.ENDER_PEARL.MAX_QUANTITY` | `integer` | Any integer | `16` | Max quantity. |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.ENDER_PEARL.MIN_QUANTITY` | `integer` | Any integer | `1` | Min quantity. |

#### `PURCHASE-SHOP-MENU.RESTRICTIONS.DEFAULT`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.DEFAULT.MAX_QUANTITY` | `integer` | Any integer | `64` | Max quantity. |
| `PURCHASE-SHOP-MENU.RESTRICTIONS.DEFAULT.MIN_QUANTITY` | `integer` | Any integer | `1` | Min quantity. |

### `PURCHASE-SHOP-MENU.MESSAGES`

#### `PURCHASE-SHOP-MENU.MESSAGES.SUCCESS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.MESSAGES.SUCCESS.MONEY` | `string` | Any text | `&7You bought &e{Quantity} {item-name}&7 for &a${amount}` | Money. |
| `PURCHASE-SHOP-MENU.MESSAGES.SUCCESS.SHARDS` | `string` | Any text | `&7You bought {item-name}&7 for &5{amount} shards` | Shards. |

#### `PURCHASE-SHOP-MENU.MESSAGES.ERROR`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.MESSAGES.ERROR.NO_MONEY` | `string` | Any text | `&cYOU DON'T HAVE ENOUGH MONEY.` | No money. |
| `PURCHASE-SHOP-MENU.MESSAGES.ERROR.NO_SHARDS` | `string` | Any text | `&cYOU DON'T HAVE ENOUGH SHARDS.` | No shards. |
| `PURCHASE-SHOP-MENU.MESSAGES.ERROR.FULL_INVENTORY` | `string` | Any text | `&cYOUR INVENTORY IS FULL.` | Full inventory. |

### `PURCHASE-SHOP-MENU.SOUNDS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PURCHASE-SHOP-MENU.SOUNDS.SUCCESS` | `string` | Any text | `ENTITY_EXPERIENCE_ORB_PICKUP` | Success. |
| `PURCHASE-SHOP-MENU.SOUNDS.ERROR` | `string` | Any text | `ENTITY_VILLAGER_NO` | Error. |

<details>
<summary>Default <code>PURCHASE-SHOP-MENU</code> block as shipped</summary>

```yaml
PURCHASE-SHOP-MENU:
  TITLE: '&8Confirmation Menu'
  SIZE: 27
  BUTTONS:
    MAIN:
      SLOT: 13
      LORE:
        MONEY: '&fBUY PRICE: &a${price}'
        SHARD: '&fBUY PRICE: &#A303F9{price_formatted}'
        DEFAULT: '&fBUY PRICE: &a${price}'
    CANCEL:
      SLOT: 11
      MATERIAL: RED_STAINED_GLASS_PANE
      NAME: '&cCancel'
      LORE: '&fCLICK TO CANCEL'
    CONFIRM:
      SLOT: 15
      MATERIAL: LIME_STAINED_GLASS_PANE
      NAME: '&aConfirm'
      LORE: '&fCLICK TO BUY'
    QUANTITY_ADJUST:
      ADD:
        MATERIAL: LIME_STAINED_GLASS_PANE
        ADD_1:
          SLOT: 24
          NAME: '&aAdd 1'
          INCREMENT: 1
        ADD_10:
          SLOT: 25
          NAME: '&aAdd 10'
          INCREMENT: 10
        SET_64:
          SLOT: 26
          NAME: '&aSet To 64'
          INCREMENT: 64
      REMOVE:
        MATERIAL: RED_STAINED_GLASS_PANE
        REMOVE_1:
          SLOT: 20
          NAME: '&cRemove 1'
          DECREMENT: 1
        REMOVE_10:
          SLOT: 19
          NAME: '&cRemove 10'
          DECREMENT: 10
        REMOVE_64:
          SLOT: 18
          NAME: '&cRemove 64'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `PAY-CONFIRM-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY-CONFIRM-MENU.TITLE` | `string` | Any text | `&8Confirm Payment` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY-CONFIRM-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `PAY-CONFIRM-MENU.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY-CONFIRM-MENU.CONFIRM-BUTTON.TITLE` | `string` | Any text | `&#00FC00Confirm` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY-CONFIRM-MENU.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PAY-CONFIRM-MENU.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PAY-CONFIRM-MENU.CONFIRM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to confirm to pay {amount}!'
```

</details>

### `PAY-CONFIRM-MENU.CANCEL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY-CONFIRM-MENU.CANCEL-BUTTON.TITLE` | `string` | Any text | `&#FC0000Cancel` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY-CONFIRM-MENU.CANCEL-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `PAY-CONFIRM-MENU.CANCEL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PAY-CONFIRM-MENU.CANCEL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to cancel'
```

</details>

### `PAY-CONFIRM-MENU.PLAYER-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY-CONFIRM-MENU.PLAYER-BUTTON.TITLE` | `string` | Any text | `&#00FC00{player}` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PAY-CONFIRM-MENU.PLAYER-BUTTON.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |

<details>
<summary>Default <code>PAY-CONFIRM-MENU</code> block as shipped</summary>

```yaml
PAY-CONFIRM-MENU:
  TITLE: '&8Confirm Payment'
  SIZE: 27
  CONFIRM-BUTTON:
    TITLE: '&#00FC00Confirm'
    MATERIAL: LIME_STAINED_GLASS_PANE
    LORE:
    - '&7Click to confirm to pay {amount}!'
  CANCEL-BUTTON:
    TITLE: '&#FC0000Cancel'
    MATERIAL: RED_STAINED_GLASS_PANE
    LORE:
    - '&7Click to cancel'
  PLAYER-BUTTON:
    TITLE: '&#00FC00{player}'
    MATERIAL: PLAYER_HEAD
```

</details>

---

## Section: `SELLALL-CONFIRM-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELLALL-CONFIRM-MENU.TITLE` | `string` | Any text | `&8Confirm Sell All` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELLALL-CONFIRM-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `SELLALL-CONFIRM-MENU.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELLALL-CONFIRM-MENU.CONFIRM-BUTTON.TITLE` | `string` | Any text | `&#00FC00Confirm` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELLALL-CONFIRM-MENU.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `SELLALL-CONFIRM-MENU.CONFIRM-BUTTON.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `SELLALL-CONFIRM-MENU.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SELLALL-CONFIRM-MENU.CONFIRM-BUTTON.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Click to confirm to sell all'
  - '&7sellable items in your inventory.'
```

</details>

### `SELLALL-CONFIRM-MENU.CANCEL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELLALL-CONFIRM-MENU.CANCEL-BUTTON.TITLE` | `string` | Any text | `&#FC0000Cancel` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELLALL-CONFIRM-MENU.CANCEL-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `SELLALL-CONFIRM-MENU.CANCEL-BUTTON.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `SELLALL-CONFIRM-MENU.CANCEL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SELLALL-CONFIRM-MENU.CANCEL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to cancel'
```

</details>

### `SELLALL-CONFIRM-MENU.INFO-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELLALL-CONFIRM-MENU.INFO-BUTTON.TITLE` | `string` | Any text | `&#E69F00Sell All Items` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SELLALL-CONFIRM-MENU.INFO-BUTTON.MATERIAL` | `string` | Any text | `CHEST` | Bukkit `Material` name for the icon. |
| `SELLALL-CONFIRM-MENU.INFO-BUTTON.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `SELLALL-CONFIRM-MENU.INFO-BUTTON.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SELLALL-CONFIRM-MENU.INFO-BUTTON.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7This will sell all sellable'
  - '&7items in your inventory.'
```

</details>

<details>
<summary>Default <code>SELLALL-CONFIRM-MENU</code> block as shipped</summary>

```yaml
SELLALL-CONFIRM-MENU:
  TITLE: '&8Confirm Sell All'
  SIZE: 27
  CONFIRM-BUTTON:
    TITLE: '&#00FC00Confirm'
    MATERIAL: LIME_STAINED_GLASS_PANE
    SLOT: 15
    LORE:
    - '&7Click to confirm to sell all'
    - '&7sellable items in your inventory.'
  CANCEL-BUTTON:
    TITLE: '&#FC0000Cancel'
    MATERIAL: RED_STAINED_GLASS_PANE
    SLOT: 11
    LORE:
    - '&7Click to cancel'
  INFO-BUTTON:
    TITLE: '&#E69F00Sell All Items'
    MATERIAL: CHEST
    SLOT: 13
    LORE:
    - '&7This will sell all sellable'
    - '&7items in your inventory.'
```

</details>

---

## Section: `SERVER-INFO-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVER-INFO-MENU.TITLE` | `string` | Any text | `&8Server Info` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVER-INFO-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `SERVER-INFO-MENU.BUTTONS`

#### Entry schema (7 entries)

Each entry under `SERVER-INFO-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `SERVER` | `&#00A4FCDonutSMP` | `LANTERN` | `10` |
| `ECONOMY` | `&#00A4FCEconomy` | `GOLD_INGOT` | `11` |
| `RULES` | `&#00A4FCRules` | `KNOWLEDGE_BOOK` | `12` |
| `LEADERBOARDS` | `&#00A4FCLeaderboards` | `CLOCK` | `13` |
| `SHARDS` | `&#00A4FCAFK Shards` | `AMETHYST_SHARD` | `14` |
| `SETTINGS` | `&#00A4FCSettings` | `GRAY_DYE` | `15` |
| `RTP` | `&#00A4FCRandom Teleport` | `OAK_SAPLING` | `16` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

### `SERVER-INFO-MENU.NAVIGATION`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVER-INFO-MENU.NAVIGATION.BACK-SLOT` | `integer` | Any integer | `18` | Inventory slot, `0` is the top-left cell. |
| `SERVER-INFO-MENU.NAVIGATION.PAGE-INFO-SLOT` | `integer` | Any integer | `22` | Inventory slot, `0` is the top-left cell. |
| `SERVER-INFO-MENU.NAVIGATION.NEXT-SLOT` | `integer` | Any integer | `26` | Inventory slot, `0` is the top-left cell. |
| `SERVER-INFO-MENU.NAVIGATION.PAGE-INFO-MATERIAL` | `string` | Any text | `BOOK` | Page info material. |
| `SERVER-INFO-MENU.NAVIGATION.PAGE-INFO-NAME` | `string` | Any text | `&#00A4FCHelp Pages` | Page info name. |

### `SERVER-INFO-MENU.PAGES`

#### `SERVER-INFO-MENU.PAGES.1`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVER-INFO-MENU.PAGES.1.TITLE` | `string` | Any text | `&8Server Info` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVER-INFO-MENU.PAGES.1.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

##### `SERVER-INFO-MENU.PAGES.1.BUTTONS`

###### Entry schema (7 entries)

Each entry under `SERVER-INFO-MENU.PAGES.1.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

###### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `SERVER` | `&#00A4FCDonutSMP` | `LANTERN` | `10` |
| `ECONOMY` | `&#00A4FCEconomy` | `GOLD_INGOT` | `11` |
| `RULES` | `&#00A4FCRules` | `KNOWLEDGE_BOOK` | `12` |
| `LEADERBOARDS` | `&#00A4FCLeaderboards` | `CLOCK` | `13` |
| `SHARDS` | `&#00A4FCAFK Shards` | `AMETHYST_SHARD` | `14` |
| `SETTINGS` | `&#00A4FCSettings` | `GRAY_DYE` | `15` |
| `RTP` | `&#00A4FCRandom Teleport` | `OAK_SAPLING` | `16` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `COMMAND` | `string` | Any text | Optional (5/7) | Console command, without a leading slash. Empty means none. |

#### `SERVER-INFO-MENU.PAGES.2`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVER-INFO-MENU.PAGES.2.TITLE` | `string` | Any text | `&8Getting Started` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVER-INFO-MENU.PAGES.2.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

##### `SERVER-INFO-MENU.PAGES.2.BUTTONS`

###### Entry schema (7 entries)

Each entry under `SERVER-INFO-MENU.PAGES.2.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

###### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `START_HERE` | `&#00A4FCStart Here` | `BOOK` | `10` |
| `MONEY` | `&#00A4FCMake Money` | `GOLD_INGOT` | `11` |
| `HOMES` | `&#00A4FCHomes` | `RED_BED` | `12` |
| `TEAMS` | `&#00A4FCTeams` | `IRON_HELMET` | `13` |
| `COMBAT` | `&#00A4FCCombat Tips` | `DIAMOND_SWORD` | `14` |
| `SPAWN` | `&#00A4FCSpawn` | `COMPASS` | `15` |
| `RTP_GUIDE` | `&#00A4FCRTP Guide` | `OAK_SAPLING` | `16` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `COMMAND` | `string` | Any text | Optional (5/7) | Console command, without a leading slash. Empty means none. |
| `CLICK-MESSAGE` | `list` | A list of values | Optional (2/7) | The click message list. |

#### `SERVER-INFO-MENU.PAGES.3`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVER-INFO-MENU.PAGES.3.TITLE` | `string` | Any text | `&8Useful Commands` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVER-INFO-MENU.PAGES.3.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

##### `SERVER-INFO-MENU.PAGES.3.BUTTONS`

###### Entry schema (7 entries)

Each entry under `SERVER-INFO-MENU.PAGES.3.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

###### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `SHOP` | `&#00A4FCShop` | `EMERALD` | `10` |
| `SELL` | `&#00A4FCSell` | `CHEST` | `11` |
| `TPA` | `&#00A4FCTeleport ReQuests` | `ENDER_PEARL` | `12` |
| `LEADERBOARDS_GUIDE` | `&#00A4FCLeaderboards` | `CLOCK` | `13` |
| `SETTINGS_GUIDE` | `&#00A4FCSettings` | `GRAY_DYE` | `14` |
| `RULES_GUIDE` | `&#00A4FCRules` | `KNOWLEDGE_BOOK` | `15` |
| `SOCIAL_GUIDE` | `&#00A4FCSocial & Media` | `PINK_DYE` | `16` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `COMMAND` | `string` | Any text | Optional (6/7) | Console command, without a leading slash. Empty means none. |
| `CLICK-MESSAGE` | `list` | A list of values | Optional (1/7) | The click message list. |

<details>
<summary>Default <code>SERVER-INFO-MENU</code> block as shipped</summary>

```yaml
SERVER-INFO-MENU:
  TITLE: '&8Server Info'
  SIZE: 27
  BUTTONS:
    SERVER:
      MATERIAL: LANTERN
      SLOT: 10
      NAME: '&#00A4FCDonutSMP'
      LORE:
      - '&fBuild a base, fight players,'
      - '&fand become the richest.'
      - ''
      - '&#00A4FCPVP: &cEnabled'
      - '&#00A4FCDifficulty: &cHard'
      - '&#00A4FCkeepInventory: &cFalse'
    ECONOMY:
      MATERIAL: GOLD_INGOT
      SLOT: 11
      NAME: '&#00A4FCEconomy'
      LORE:
      - '&fYou can earn server money by selling items in game.'
      - '&fThis allows you to buy better things like armor and pvp gear.'
      - ''
      - '&#00A4FCCommands: &f/sell and /auction'
    RULES:
      MATERIAL: KNOWLEDGE_BOOK
      SLOT: 12
      NAME: '&#00A4FCRules'
      LORE:
      - '&fTo keep the community safe, everyone must'
      - '&ffollow the rules or be subject to punishment.'
      - ''
      - '&#00A4FCCommand: &f/rules'
    LEADERBOARDS:
      MATERIAL: CLOCK
      SLOT: 13
      NAME: '&#00A4FCLeaderboards'
      LORE:
      - '&fLook at the top players and'
      - '&fcompete for the top spots.'
      - ''
      - '&#00A4FCCommand: &f/leaderboards'
    SHARDS:
      MATERIAL: AMETHYST_SHARD
      SLOT: 14
      NAME: '&#00A4FCAFK Shards'
      LORE:
      - '&fYou can earn shards by sitting idle, then'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `RULES-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RULES-MENU.TITLE` | `string` | Any text | `&8Rules` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `RULES-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `RULES-MENU.BUTTONS`

#### `RULES-MENU.BUTTONS.RULE_1`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RULES-MENU.BUTTONS.RULE_1.MATERIAL` | `string` | Any text | `KNOWLEDGE_BOOK` | Bukkit `Material` name for the icon. |
| `RULES-MENU.BUTTONS.RULE_1.SLOT` | `integer` | Any integer | `12` | Inventory slot, `0` is the top-left cell. |
| `RULES-MENU.BUTTONS.RULE_1.NAME` | `string` | Any text | `&#00A4FCServer Rules` | Display name shown to players. |
| `RULES-MENU.BUTTONS.RULE_1.LORE` | `list` | A list of values | _list of 21 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>RULES-MENU.BUTTONS.RULE_1.LORE</code> (21 items)</summary>

```yaml
LORE:
  - '&#00A4FC● &fNo Hacked Clients'
  - '&#00A4FC● &fNo Movement Mods'
  - '&#00A4FC● &fNo Inventory Mods'
  - '&#00A4FC● &fNo Health Indicators'
  - '&#00A4FC● &fNo Radar'
  - '&#00A4FC● &fNo Freecam'
  - '&#00A4FC● &fNo Auto Place'
  - '&#00A4FC● &fNo Easy Place'
  - '&#00A4FC● &fNo Macros or Scripts'
  - '&#00A4FC● &fNo Abusing bugs'
  - '&#00A4FC● &fNo Using dupe methods'
  - '&#00A4FC● &fNo Irl trading'
  - '&#00A4FC● &fNo Invite Rewards'
  - '&#00A4FC● &fNo Cross Server Trading'
  - '&#00A4FC● &fNo Finding or Using the seed'
  - '&#00A4FC● &fNo Using More Than 5 Accounts'
  - '&#00A4FC● &fNo Mouse Tweaks / Scrollers'
  - '&#00A4FC● &fNo Nitro Boost Rewards'
  - '&#00A4FC● &fNo Crafting Modifications'
  - '&#00A4FC● &fNo Staff Impersonation'
  - '&#00A4FC● &fNo EXternal Gambling'
```

</details>

#### `RULES-MENU.BUTTONS.RULE_2`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RULES-MENU.BUTTONS.RULE_2.MATERIAL` | `string` | Any text | `KNOWLEDGE_BOOK` | Bukkit `Material` name for the icon. |
| `RULES-MENU.BUTTONS.RULE_2.SLOT` | `integer` | Any integer | `14` | Inventory slot, `0` is the top-left cell. |
| `RULES-MENU.BUTTONS.RULE_2.NAME` | `string` | Any text | `&#00A4FC&lChat Rules` | Display name shown to players. |
| `RULES-MENU.BUTTONS.RULE_2.LORE` | `list` | A list of values | _list of 12 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>RULES-MENU.BUTTONS.RULE_2.LORE</code> (12 items)</summary>

```yaml
LORE:
  - ''
  - '&#00A4FC● &fNo Spamming or getting others to spam'
  - '&#00A4FC● &fNo Harassing'
  - '&#00A4FC● &fNo Advertising or Promotion'
  - '&#00A4FC● &fNo Discrimination or Hate Speech'
  - '&#00A4FC● &fNo Death Threats'
  - '&#00A4FC● &fNo Sharing Others Private Information'
  - '&#00A4FC● &fNo Pretending to be someone else'
  - '&#00A4FC● &fNo Ban Evasion'
  - '&#00A4FC● &fNo Lying to Staff Members'
  - '&#00A4FC● &fReport All Bugs, Glitches, and Cheaters'
  - '  &fin the discord &&#F97603(discord.gg/donutsmp)'
```

</details>

<details>
<summary>Default <code>RULES-MENU</code> block as shipped</summary>

```yaml
RULES-MENU:
  TITLE: '&8Rules'
  SIZE: 27
  BUTTONS:
    RULE_1:
      MATERIAL: KNOWLEDGE_BOOK
      SLOT: 12
      NAME: '&#00A4FCServer Rules'
      LORE:
      - "&#00A4FC● &fNo Hacked Clients"
      - "&#00A4FC● &fNo Movement Mods"
      - "&#00A4FC● &fNo Inventory Mods"
      - "&#00A4FC● &fNo Health Indicators"
      - "&#00A4FC● &fNo Radar"
      - "&#00A4FC● &fNo Freecam"
      - "&#00A4FC● &fNo Auto Place"
      - "&#00A4FC● &fNo Easy Place"
      - "&#00A4FC● &fNo Macros or Scripts"
      - "&#00A4FC● &fNo Abusing bugs"
      - "&#00A4FC● &fNo Using dupe methods"
      - "&#00A4FC● &fNo Irl trading"
      - "&#00A4FC● &fNo Invite Rewards"
      - "&#00A4FC● &fNo Cross Server Trading"
      - "&#00A4FC● &fNo Finding or Using the seed"
      - "&#00A4FC● &fNo Using More Than 5 Accounts"
      - "&#00A4FC● &fNo Mouse Tweaks / Scrollers"
      - "&#00A4FC● &fNo Nitro Boost Rewards"
      - "&#00A4FC● &fNo Crafting Modifications"
      - "&#00A4FC● &fNo Staff Impersonation"
      - "&#00A4FC● &fNo EXternal Gambling"
    RULE_2:
      MATERIAL: KNOWLEDGE_BOOK
      SLOT: 14
      NAME: '&#00A4FC&lChat Rules'
      LORE:
      - ''
      - "&#00A4FC● &fNo Spamming or getting others to spam"
      - "&#00A4FC● &fNo Harassing"
      - "&#00A4FC● &fNo Advertising or Promotion"
      - "&#00A4FC● &fNo Discrimination or Hate Speech"
      - "&#00A4FC● &fNo Death Threats"
      - "&#00A4FC● &fNo Sharing Others Private Information"
      - "&#00A4FC● &fNo Pretending to be someone else"
      - "&#00A4FC● &fNo Ban Evasion"
      - "&#00A4FC● &fNo Lying to Staff Members"
      - "&#00A4FC● &fReport All Bugs, Glitches, and Cheaters"
      - '  &fin the discord &&#F97603(discord.gg/donutsmp)'
```

</details>

---

## Section: `AFK-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AFK-MENU.TITLE` | `string` | Any text | `&8AFK Areas` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `AFK-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `AFK-MENU.RANDOM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AFK-MENU.RANDOM-BUTTON.SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |
| `AFK-MENU.RANDOM-BUTTON.MATERIAL` | `string` | Any text | `AMETHYST_BLOCK` | Bukkit `Material` name for the icon. |
| `AFK-MENU.RANDOM-BUTTON.DISPLAY-NAME` | `string` | Any text | `&#A303F9AFK` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `AFK-MENU.RANDOM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>AFK-MENU.RANDOM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fClick to teleport to a random afk area'
```

</details>

### `AFK-MENU.AREAS`

#### `AFK-MENU.AREAS.1`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AFK-MENU.AREAS.1.SLOT` | `integer` | Any integer | `0` | Inventory slot, `0` is the top-left cell. |
| `AFK-MENU.AREAS.1.MATERIAL` | `string` | Any text | `ITEM_FRAME` | Bukkit `Material` name for the icon. |
| `AFK-MENU.AREAS.1.DISPLAY-NAME` | `string` | Any text | `&#A303F9AFK #1` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `AFK-MENU.AREAS.1.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `AFK-MENU.AREAS.1.CUBOID` | `string` | Any text | `afk1` | Cuboid. |
| `AFK-MENU.AREAS.1.LOCATION` | `integer` | Any integer | `1` | Location. |

<details>
<summary>Default contents of <code>AFK-MENU.AREAS.1.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&8{players}/200'
  - '&7Click to go to this'
  - '&7AFK zone area.'
```

</details>

#### `AFK-MENU.AREAS.2`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AFK-MENU.AREAS.2.SLOT` | `integer` | Any integer | `1` | Inventory slot, `0` is the top-left cell. |
| `AFK-MENU.AREAS.2.MATERIAL` | `string` | Any text | `ITEM_FRAME` | Bukkit `Material` name for the icon. |
| `AFK-MENU.AREAS.2.DISPLAY-NAME` | `string` | Any text | `&#A303F9AFK #2` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `AFK-MENU.AREAS.2.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `AFK-MENU.AREAS.2.CUBOID` | `string` | Any text | `afk2` | Cuboid. |
| `AFK-MENU.AREAS.2.LOCATION` | `integer` | Any integer | `2` | Location. |

<details>
<summary>Default contents of <code>AFK-MENU.AREAS.2.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&8{players}/200'
  - '&7Click to go to this'
  - '&7AFK zone area.'
```

</details>

<details>
<summary>Default <code>AFK-MENU</code> block as shipped</summary>

```yaml
AFK-MENU:
  TITLE: '&8AFK Areas'
  SIZE: 54
  RANDOM-BUTTON:
    SLOT: 49
    MATERIAL: AMETHYST_BLOCK
    DISPLAY-NAME: '&#A303F9AFK'
    LORE:
    - '&fClick to teleport to a random afk area'
  AREAS:
    '1':
      SLOT: 0
      MATERIAL: ITEM_FRAME
      DISPLAY-NAME: '&#A303F9AFK #1'
      LORE:
      - '&8{players}/200'
      - '&7Click to go to this'
      - '&7AFK zone area.'
      CUBOID: afk1
      LOCATION: 1
    '2':
      SLOT: 1
      MATERIAL: ITEM_FRAME
      DISPLAY-NAME: '&#A303F9AFK #2'
      LORE:
      - '&8{players}/200'
      - '&7Click to go to this'
      - '&7AFK zone area.'
      CUBOID: afk2
      LOCATION: 2
```

</details>

---

## Section: `SPAWN-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWN-MENU.TITLE` | `string` | Any text | `&8Spawn Areas` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SPAWN-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `SPAWN-MENU.RANDOM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWN-MENU.RANDOM-BUTTON.SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |
| `SPAWN-MENU.RANDOM-BUTTON.MATERIAL` | `string` | Any text | `LIGHT_BLUE_GLAZED_TERRACOTTA` | Bukkit `Material` name for the icon. |
| `SPAWN-MENU.RANDOM-BUTTON.DISPLAY-NAME` | `string` | Any text | `&#00A4FCSpawn` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `SPAWN-MENU.RANDOM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SPAWN-MENU.RANDOM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fClick to teleport to a random spawn area'
```

</details>

### `SPAWN-MENU.AREAS`

#### `SPAWN-MENU.AREAS.1`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWN-MENU.AREAS.1.SLOT` | `integer` | Any integer | `0` | Inventory slot, `0` is the top-left cell. |
| `SPAWN-MENU.AREAS.1.MATERIAL` | `string` | Any text | `ITEM_FRAME` | Bukkit `Material` name for the icon. |
| `SPAWN-MENU.AREAS.1.DISPLAY-NAME` | `string` | Any text | `&#00A4FCSpawn #1` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `SPAWN-MENU.AREAS.1.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `SPAWN-MENU.AREAS.1.CUBOID` | `string` | Any text | `spawn1` | Cuboid. |
| `SPAWN-MENU.AREAS.1.LOCATION` | `integer` | Any integer | `1` | Location. |

<details>
<summary>Default contents of <code>SPAWN-MENU.AREAS.1.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&8{players}/200'
  - '&7Click to go to this'
  - '&7Spawn area.'
```

</details>

#### `SPAWN-MENU.AREAS.2`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWN-MENU.AREAS.2.SLOT` | `integer` | Any integer | `1` | Inventory slot, `0` is the top-left cell. |
| `SPAWN-MENU.AREAS.2.MATERIAL` | `string` | Any text | `ITEM_FRAME` | Bukkit `Material` name for the icon. |
| `SPAWN-MENU.AREAS.2.DISPLAY-NAME` | `string` | Any text | `&#00A4FCSpawn #2` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `SPAWN-MENU.AREAS.2.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |
| `SPAWN-MENU.AREAS.2.CUBOID` | `string` | Any text | `spawn2` | Cuboid. |
| `SPAWN-MENU.AREAS.2.LOCATION` | `integer` | Any integer | `2` | Location. |

<details>
<summary>Default contents of <code>SPAWN-MENU.AREAS.2.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&8{players}/200'
  - '&7Click to go to this'
  - '&7Spawn area.'
```

</details>

<details>
<summary>Default <code>SPAWN-MENU</code> block as shipped</summary>

```yaml
SPAWN-MENU:
  TITLE: '&8Spawn Areas'
  SIZE: 54
  RANDOM-BUTTON:
    SLOT: 49
    MATERIAL: LIGHT_BLUE_GLAZED_TERRACOTTA
    DISPLAY-NAME: '&#00A4FCSpawn'
    LORE:
    - '&fClick to teleport to a random spawn area'
  AREAS:
    '1':
      SLOT: 0
      MATERIAL: ITEM_FRAME
      DISPLAY-NAME: '&#00A4FCSpawn #1'
      LORE:
      - '&8{players}/200'
      - '&7Click to go to this'
      - '&7Spawn area.'
      CUBOID: spawn1
      LOCATION: 1
    '2':
      SLOT: 1
      MATERIAL: ITEM_FRAME
      DISPLAY-NAME: '&#00A4FCSpawn #2'
      LORE:
      - '&8{players}/200'
      - '&7Click to go to this'
      - '&7Spawn area.'
      CUBOID: spawn2
      LOCATION: 2
```

</details>

---

## Section: `PROFILE-VIEWER-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROFILE-VIEWER-MENU.TITLE` | `string` | Any text | `&8{username}'s Profile` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PROFILE-VIEWER-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `PROFILE-VIEWER-MENU.BUTTONS`

#### Entry schema (5 entries)

Each entry under `PROFILE-VIEWER-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `SUMMARY` | `&#6BF18D{username}` | `PLAYER_HEAD` | `4` |
| `HOMES` | `&#6BF18DHomes` | `RED_BED` | `40` |
| `CURRENT-LOCATION` | `&#6BF18DCurrent Location` | `COMPASS` | `41` |
| `PUNISHMENTS` | `&#6BF18DPunishments` | `IRON_BARS` | `42` |
| `REFRESH` | `&#6BF18DRefresh` | `CLOCK` | `49` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `DISPLAY-NAME-OFFLINE` | `string` | Any text | Optional (1/5) | Display name offline. |
| `LORE-OFFLINE` | `list` | A list of values | Optional (1/5) | The lore offline list. |

<details>
<summary>Default <code>PROFILE-VIEWER-MENU</code> block as shipped</summary>

```yaml
PROFILE-VIEWER-MENU:
  TITLE: '&8{username}''s Profile'
  SIZE: 54
  BUTTONS:
    SUMMARY:
      SLOT: 4
      MATERIAL: PLAYER_HEAD
      DISPLAY-NAME: '&#6BF18D{username}'
      LORE:
      - '&7Status: &f{status}'
      - '&7Team: &f{team}'
      - '&7Homes: &f{homes}'
      - '&7AFK: &f{afk}'
      - '&7Location: &f{world} ({x}, {y}, {z})'
    HOMES:
      SLOT: 40
      MATERIAL: RED_BED
      DISPLAY-NAME: '&#6BF18DHomes'
      LORE:
      - '&7View and navigate through this player''s homes'
      - '&7Homes saved: &f{homes}'
      - '&aClick to open'
    CURRENT-LOCATION:
      SLOT: 41
      MATERIAL: COMPASS
      DISPLAY-NAME: '&#6BF18DCurrent Location'
      DISPLAY-NAME-OFFLINE: '&cCurrent Location'
      LORE:
      - '&7{world} ({x}, {y}, {z})'
      - '&aClick to teleport'
      LORE-OFFLINE:
      - '&7This player is offline right now.'
    PUNISHMENTS:
      SLOT: 42
      MATERIAL: IRON_BARS
      DISPLAY-NAME: '&#6BF18DPunishments'
      LORE:
      - '&7View this player''s punishment history'
      - '&aClick to open'
    REFRESH:
      SLOT: 49
      MATERIAL: CLOCK
      DISPLAY-NAME: '&#6BF18DRefresh'
      LORE:
      - '&7Reload this player''s profile'
```

</details>

---

## Section: `PROFILE-VIEWER-HOMES-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PROFILE-VIEWER-HOMES-MENU.TITLE` | `string` | Any text | `&8{username}'s Homes` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PROFILE-VIEWER-HOMES-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `PROFILE-VIEWER-HOMES-MENU.MAX-ITEMS-PER-PAGE` | `integer` | Any integer | `45` | Max items per page. |

### Entry schema (6 entries)

Each entry under `PROFILE-VIEWER-HOMES-MENU` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` |
| :--- | :--- | :--- |
| `HOME-BUTTON` | `&b{name}` | `LIGHT_BLUE_BED` |
| `INVALID-HOME-BUTTON` | `&c{name}` | `BARRIER` |
| `EMPTY-BUTTON` | `&cNo Homes` | `BARRIER` |
| `BACK-BUTTON` | `&cBack` | `RED_STAINED_GLASS_PANE` |
| `CLOSE-BUTTON` | `&cClose` | `RED_STAINED_GLASS_PANE` |
| `REFRESH-BUTTON` | `&#6BF18DRefresh` | `CLOCK` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |

<details>
<summary>Default <code>PROFILE-VIEWER-HOMES-MENU</code> block as shipped</summary>

```yaml
PROFILE-VIEWER-HOMES-MENU:
  TITLE: '&8{username}''s Homes'
  SIZE: 54
  MAX-ITEMS-PER-PAGE: 45
  HOME-BUTTON:
    MATERIAL: LIGHT_BLUE_BED
    DISPLAY-NAME: '&b{name}'
    LORE:
    - '&7World: &f{world}'
    - '&7X: &f{x} &7Y: &f{y} &7Z: &f{z}'
    - '&aClick to teleport'
  INVALID-HOME-BUTTON:
    MATERIAL: BARRIER
    DISPLAY-NAME: '&c{name}'
    LORE:
    - '&7This home points to an unavailable world.'
  EMPTY-BUTTON:
    MATERIAL: BARRIER
    DISPLAY-NAME: '&cNo Homes'
    LORE:
    - '&7This player has no homes saved.'
  BACK-BUTTON:
    MATERIAL: RED_STAINED_GLASS_PANE
    DISPLAY-NAME: '&cBack'
    LORE:
    - '&7Return to the main profile viewer'
  CLOSE-BUTTON:
    MATERIAL: RED_STAINED_GLASS_PANE
    DISPLAY-NAME: '&cClose'
    LORE:
    - '&7Close this home list'
  REFRESH-BUTTON:
    MATERIAL: CLOCK
    DISPLAY-NAME: '&#6BF18DRefresh'
    LORE:
    - '&7Reload this player''s homes'
```

</details>

---

## Section: `PUNISHMENT-HISTORY-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.TITLE` | `string` | Any text | `&8Punishments ({player})` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `PUNISHMENT-HISTORY-MENU.MAX-ITEMS-PER-PAGE` | `integer` | Any integer | `45` | Max items per page. |

### `PUNISHMENT-HISTORY-MENU.BUTTONS`

#### `PUNISHMENT-HISTORY-MENU.BUTTONS.BACK`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.BACK.MATERIAL` | `string` | Any text | `ARROW` | Bukkit `Material` name for the icon. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.BACK.DISPLAY-NAME` | `string` | Any text | `&cBack` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.BACK.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.BUTTONS.BACK.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Return to the previous menu'
```

</details>

#### `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-STATE`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-STATE.MATERIAL` | `string` | Any text | `HOPPER` | Bukkit `Material` name for the icon. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-STATE.DISPLAY-NAME` | `string` | Any text | `&#6BF18DState Filter` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-STATE.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-STATE.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Current: &f{state_filter}'
  - '&aClick to change'
```

</details>

#### `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-TYPE`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-TYPE.MATERIAL` | `string` | Any text | `BOOK` | Bukkit `Material` name for the icon. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-TYPE.DISPLAY-NAME` | `string` | Any text | `&#6BF18DType Filter` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-TYPE.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.BUTTONS.FILTER-TYPE.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Current: &f{type_filter}'
  - '&aClick to change'
```

</details>

#### `PUNISHMENT-HISTORY-MENU.BUTTONS.REFRESH`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.REFRESH.MATERIAL` | `string` | Any text | `CLOCK` | Bukkit `Material` name for the icon. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.REFRESH.DISPLAY-NAME` | `string` | Any text | `&#6BF18DRefresh` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.BUTTONS.REFRESH.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.BUTTONS.REFRESH.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Reload this player''s punishment history'
```

</details>

### `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.DISPLAY-NAME` | `string` | Any text | `{status_color}{type}` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.LORE` | `list` | A list of values | _list of 11 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.LORE</code> (11 items)</summary>

```yaml
LORE:
  - '&7Reason: &f{reason}'
  - '&7Issued by: &f{issuer}'
  - '&7Date: &f{issued_at}'
  - '&7Expires: &f{expires_at}'
  - '&7Status: {status_color}{status}'
  - '&7Removed by: &f{removed_by}'
  - '&7Removal reason: &f{removal_reason}'
  - '&7Removed at: &f{removed_at}'
  - '&7ID: &f#{id}'
  - ''
  - '&cShift-right-click to delete this record'
```

</details>

#### `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.BAN` | `string` | Any text | `IRON_BARS` | Ban. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.MUTE` | `string` | Any text | `PAPER` | Mute. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.VOICE_MUTE` | `string` | Any text | `NOTE_BLOCK` | Voice mute. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.WARN` | `string` | Any text | `YELLOW_DYE` | Warn. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.KICK` | `string` | Any text | `LEATHER_BOOTS` | Kick. |
| `PUNISHMENT-HISTORY-MENU.PUNISHMENT-ITEM.MATERIALS.BLACKLIST` | `string` | Any text | `BARRIER` | Blacklist. |

### `PUNISHMENT-HISTORY-MENU.EMPTY-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENT-HISTORY-MENU.EMPTY-BUTTON.MATERIAL` | `string` | Any text | `BARRIER` | Bukkit `Material` name for the icon. |
| `PUNISHMENT-HISTORY-MENU.EMPTY-BUTTON.DISPLAY-NAME` | `string` | Any text | `&cNo Punishment History` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENT-HISTORY-MENU.EMPTY-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENT-HISTORY-MENU.EMPTY-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7This player has no punishment records.'
```

</details>

<details>
<summary>Default <code>PUNISHMENT-HISTORY-MENU</code> block as shipped</summary>

```yaml
PUNISHMENT-HISTORY-MENU:
  TITLE: '&8Punishments ({player})'
  SIZE: 54
  MAX-ITEMS-PER-PAGE: 45
  BUTTONS:
    BACK:
      MATERIAL: ARROW
      DISPLAY-NAME: '&cBack'
      LORE:
      - '&7Return to the previous menu'
    FILTER-STATE:
      MATERIAL: HOPPER
      DISPLAY-NAME: '&#6BF18DState Filter'
      LORE:
      - '&7Current: &f{state_filter}'
      - '&aClick to change'
    FILTER-TYPE:
      MATERIAL: BOOK
      DISPLAY-NAME: '&#6BF18DType Filter'
      LORE:
      - '&7Current: &f{type_filter}'
      - '&aClick to change'
    REFRESH:
      MATERIAL: CLOCK
      DISPLAY-NAME: '&#6BF18DRefresh'
      LORE:
      - '&7Reload this player''s punishment history'
  PUNISHMENT-ITEM:
    MATERIALS:
      BAN: IRON_BARS
      MUTE: PAPER
      VOICE_MUTE: NOTE_BLOCK
      WARN: YELLOW_DYE
      KICK: LEATHER_BOOTS
      BLACKLIST: BARRIER
    DISPLAY-NAME: '{status_color}{type}'
    LORE:
    - '&7Reason: &f{reason}'
    - '&7Issued by: &f{issuer}'
    - '&7Date: &f{issued_at}'
    - '&7Expires: &f{expires_at}'
    - '&7Status: {status_color}{status}'
    - '&7Removed by: &f{removed_by}'
    - '&7Removal reason: &f{removal_reason}'
    - '&7Removed at: &f{removed_at}'
    - '&7ID: &f#{id}'
    - ''
    - '&cShift-right-click to delete this record'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `PUNISHMENTS-LIST-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.TITLE` | `string` | Any text | `&8All Punishments` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENTS-LIST-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `PUNISHMENTS-LIST-MENU.MAX-ITEMS-PER-PAGE` | `integer` | Any integer | `45` | Max items per page. |

### `PUNISHMENTS-LIST-MENU.BUTTONS`

#### Entry schema (6 entries)

Each entry under `PUNISHMENTS-LIST-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` |
| :--- | :--- | :--- |
| `BACK` | `&cClose` | `ARROW` |
| `FILTER-STATE` | `&#6BF18DState Filter` | `HOPPER` |
| `FILTER-TYPE` | `&#6BF18DType Filter` | `BOOK` |
| `SORT` | `&#6BF18DSort Order` | `COMPARATOR` |
| `SEARCH` | `&#6BF18DSearch` | `NAME_TAG` |
| `REFRESH` | `&#6BF18DRefresh` | `CLOCK` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |

### `PUNISHMENTS-LIST-MENU.SEARCH-SIGN`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.SEARCH-SIGN.INPUT-LINE` | `integer` | Any integer | `0` | Input line. |
| `PUNISHMENTS-LIST-MENU.SEARCH-SIGN.LINES` | `list` | A list of values | _list of 4 items_ | The lines list. |

<details>
<summary>Default contents of <code>PUNISHMENTS-LIST-MENU.SEARCH-SIGN.LINES</code> (4 items)</summary>

```yaml
LINES:
  - ''
  - '↑↑↑↑↑'
  - 'Player Name'
  - ''
```

</details>

### `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.DISPLAY-NAME` | `string` | Any text | `{status_color}{player} &8- &f{type}` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.LORE` | `list` | A list of values | _list of 9 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.LORE</code> (9 items)</summary>

```yaml
LORE:
  - '&7Reason: &f{reason}'
  - '&7Issued by: &f{issuer}'
  - '&7Date: &f{issued_at}'
  - '&7Expires: &f{expires_at}'
  - '&7Status: {status_color}{status}'
  - '&7ID: &f#{id}'
  - ''
  - '&aLeft-click to view full history'
  - '&cShift-right-click to delete this record'
```

</details>

#### `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.BAN` | `string` | Any text | `IRON_BARS` | Ban. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.MUTE` | `string` | Any text | `PAPER` | Mute. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.VOICE_MUTE` | `string` | Any text | `NOTE_BLOCK` | Voice mute. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.WARN` | `string` | Any text | `YELLOW_DYE` | Warn. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.KICK` | `string` | Any text | `LEATHER_BOOTS` | Kick. |
| `PUNISHMENTS-LIST-MENU.PUNISHMENT-ITEM.MATERIALS.BLACKLIST` | `string` | Any text | `BARRIER` | Blacklist. |

### `PUNISHMENTS-LIST-MENU.LOADING-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.LOADING-BUTTON.MATERIAL` | `string` | Any text | `CLOCK` | Bukkit `Material` name for the icon. |
| `PUNISHMENTS-LIST-MENU.LOADING-BUTTON.DISPLAY-NAME` | `string` | Any text | `&eLoading Punishments` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENTS-LIST-MENU.LOADING-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENTS-LIST-MENU.LOADING-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Reading the punishment table.'
```

</details>

### `PUNISHMENTS-LIST-MENU.EMPTY-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PUNISHMENTS-LIST-MENU.EMPTY-BUTTON.MATERIAL` | `string` | Any text | `BARRIER` | Bukkit `Material` name for the icon. |
| `PUNISHMENTS-LIST-MENU.EMPTY-BUTTON.DISPLAY-NAME` | `string` | Any text | `&cNo Punishments Found` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `PUNISHMENTS-LIST-MENU.EMPTY-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>PUNISHMENTS-LIST-MENU.EMPTY-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7No punishments match the current filters.'
```

</details>

<details>
<summary>Default <code>PUNISHMENTS-LIST-MENU</code> block as shipped</summary>

```yaml
PUNISHMENTS-LIST-MENU:
  TITLE: '&8All Punishments'
  SIZE: 54
  MAX-ITEMS-PER-PAGE: 45
  BUTTONS:
    BACK:
      MATERIAL: ARROW
      DISPLAY-NAME: '&cClose'
      LORE:
      - '&7Close this menu'
    FILTER-STATE:
      MATERIAL: HOPPER
      DISPLAY-NAME: '&#6BF18DState Filter'
      LORE:
      - '&7Current: &f{state_filter}'
      - '&aClick to change'
    FILTER-TYPE:
      MATERIAL: BOOK
      DISPLAY-NAME: '&#6BF18DType Filter'
      LORE:
      - '&7Current: &f{type_filter}'
      - '&aClick to change'
    SORT:
      MATERIAL: COMPARATOR
      DISPLAY-NAME: '&#6BF18DSort Order'
      LORE:
      - '&7Current: &f{sort_order}'
      - '&aClick to change'
    SEARCH:
      MATERIAL: NAME_TAG
      DISPLAY-NAME: '&#6BF18DSearch'
      LORE:
      - '&7Current: &f{search}'
      - '&aLeft-click to search a player'
      - '&cRight-click to clear'
    REFRESH:
      MATERIAL: CLOCK
      DISPLAY-NAME: '&#6BF18DRefresh'
      LORE:
      - '&7Reload the punishment list'
  SEARCH-SIGN:
    INPUT-LINE: 0
    LINES:
    - ''
    - '↑↑↑↑↑'
    - 'Player Name'
    - ''
  PUNISHMENT-ITEM:
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `STATS-WIPE-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-MENU.TITLE` | `string` | Any text | `&8Stats Wipe` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `STATS-WIPE-MENU.PLACEHOLDER` | `boolean` | `true`, `false` | `true` | On/off for placeholder. |
| `STATS-WIPE-MENU.PLACEHOLDER-MATERIAL` | `string` | Any text | `BLACK_STAINED_GLASS_PANE` | Placeholder material. |

### `STATS-WIPE-MENU.STATUS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-MENU.STATUS.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `STATS-WIPE-MENU.STATUS.MATERIAL` | `string` | Any text | `BARRIER` | Bukkit `Material` name for the icon. |
| `STATS-WIPE-MENU.STATUS.DISPLAY-NAME` | `string` | Any text | `&cWipe In Progress` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-MENU.STATUS.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>STATS-WIPE-MENU.STATUS.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Another wipe is currently running.'
  - '&7Wait for it to finish, then refresh.'
```

</details>

### `STATS-WIPE-MENU.BUTTONS`

#### Entry schema (9 entries)

Each entry under `STATS-WIPE-MENU.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `PLAYER_STATS` | `&#6BF18DPlayer Stats` | `PAPER` | `10` |
| `TEAM_DOCUMENTS` | `&#6BF18DTeam Documents` | `LIGHT_BLUE_BANNER` | `11` |
| `HOME_DOCUMENTS` | `&#6BF18DHome Documents` | `RED_BED` | `12` |
| `BOUNTIES` | `&#6BF18DBounties` | `PLAYER_HEAD` | `14` |
| `SELL_DOCUMENTS` | `&#6BF18DSell Documents` | `CHEST` | `15` |
| `MONEY` | `&#6BF18DPlayer Money` | `GOLD_INGOT` | `9` |
| `SHARDS` | `&#6BF18DPlayer Shards` | `PRISMARINE_SHARD` | `16` |
| `REFRESH` | `&#6BF18DRefresh` | `CLOCK` | `22` |
| `CLOSE` | `&cClose` | `BARRIER` | `26` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

<details>
<summary>Default <code>STATS-WIPE-MENU</code> block as shipped</summary>

```yaml
STATS-WIPE-MENU:
  TITLE: '&8Stats Wipe'
  SIZE: 27
  PLACEHOLDER: true
  PLACEHOLDER-MATERIAL: BLACK_STAINED_GLASS_PANE
  STATUS:
    SLOT: 13
    MATERIAL: BARRIER
    DISPLAY-NAME: '&cWipe In Progress'
    LORE:
    - '&7Another wipe is currently running.'
    - '&7Wait for it to finish, then refresh.'
  BUTTONS:
    PLAYER_STATS:
      SLOT: 10
      MATERIAL: PAPER
      DISPLAY-NAME: '&#6BF18DPlayer Stats'
      LORE:
      - '&7Players with tracked stats: &f{count}'
      - '&cReset kills, deaths, playtime,'
      - '&cblocks, mobs, streaks, and money flow stats.'
    TEAM_DOCUMENTS:
      SLOT: 11
      MATERIAL: LIGHT_BLUE_BANNER
      DISPLAY-NAME: '&#6BF18DTeam Documents'
      LORE:
      - '&7Teams stored: &f{count}'
      - '&cDelete all teams and team members.'
    HOME_DOCUMENTS:
      SLOT: 12
      MATERIAL: RED_BED
      DISPLAY-NAME: '&#6BF18DHome Documents'
      LORE:
      - '&7Homes stored: &f{count}'
      - '&cDelete all saved player homes.'
    BOUNTIES:
      SLOT: 14
      MATERIAL: PLAYER_HEAD
      DISPLAY-NAME: '&#6BF18DBounties'
      LORE:
      - '&7Active bounties: &f{count}'
      - '&cDelete all active bounties.'
    SELL_DOCUMENTS:
      SLOT: 15
      MATERIAL: CHEST
      DISPLAY-NAME: '&#6BF18DSell Documents'
      LORE:
      - '&7Sell docs stored: &f{count}'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `STATS-WIPE-CONFIRM-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-CONFIRM-MENU.TITLE` | `string` | Any text | `&8Confirm {target}` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-CONFIRM-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `STATS-WIPE-CONFIRM-MENU.PLACEHOLDER` | `boolean` | `true`, `false` | `true` | On/off for placeholder. |
| `STATS-WIPE-CONFIRM-MENU.PLACEHOLDER-MATERIAL` | `string` | Any text | `BLACK_STAINED_GLASS_PANE` | Placeholder material. |

### `STATS-WIPE-CONFIRM-MENU.BUTTONS`

#### `STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET.MATERIAL` | `string` | Any text | `PAPER` | Bukkit `Material` name for the icon. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET.DISPLAY-NAME` | `string` | Any text | `&#6BF18D{target}` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>STATS-WIPE-CONFIRM-MENU.BUTTONS.TARGET.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&7Affected records: &f{count}'
  - '&cThis action cannot be undone.'
  - '&7Click confirm only if you are sure.'
```

</details>

#### `STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL.DISPLAY-NAME` | `string` | Any text | `&cCancel` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>STATS-WIPE-CONFIRM-MENU.BUTTONS.CANCEL.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Return to the Stats Wipe menu.'
```

</details>

#### `STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM.DISPLAY-NAME` | `string` | Any text | `&aConfirm` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>STATS-WIPE-CONFIRM-MENU.BUTTONS.CONFIRM.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Run wipe for &f{target}&7.'
  - '&7Affected records: &f{count}'
```

</details>

<details>
<summary>Default <code>STATS-WIPE-CONFIRM-MENU</code> block as shipped</summary>

```yaml
STATS-WIPE-CONFIRM-MENU:
  TITLE: '&8Confirm {target}'
  SIZE: 27
  PLACEHOLDER: true
  PLACEHOLDER-MATERIAL: BLACK_STAINED_GLASS_PANE
  BUTTONS:
    TARGET:
      SLOT: 13
      MATERIAL: PAPER
      DISPLAY-NAME: '&#6BF18D{target}'
      LORE:
      - '&7Affected records: &f{count}'
      - '&cThis action cannot be undone.'
      - '&7Click confirm only if you are sure.'
    CANCEL:
      SLOT: 11
      MATERIAL: RED_STAINED_GLASS_PANE
      DISPLAY-NAME: '&cCancel'
      LORE:
      - '&7Return to the Stats Wipe menu.'
    CONFIRM:
      SLOT: 15
      MATERIAL: LIME_STAINED_GLASS_PANE
      DISPLAY-NAME: '&aConfirm'
      LORE:
      - '&7Run wipe for &f{target}&7.'
      - '&7Affected records: &f{count}'
```

</details>

---

## Section: `SERVERS-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVERS-MENU.TITLE` | `string` | Any text | `&8Ongoing Servers` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVERS-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `SERVERS-MENU.REFRESH-TICKS` | `integer` | Any integer | `40` | Refresh ticks. Ticks (20 = 1 second). |
| `SERVERS-MENU.PLACEHOLDER-MATERIAL` | `string` | Any text | `BLACK_STAINED_GLASS_PANE` | Placeholder material. |

### `SERVERS-MENU.SERVER_STATUS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVERS-MENU.SERVER_STATUS.TITLE` | `string` | Any text | `&8Ongoing Servers` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SERVERS-MENU.SERVER_STATUS.SERVER_NAME` | `string` | Any text | `&b%server%` | Server name. |
| `SERVERS-MENU.SERVER_STATUS.LORE` | `list` | A list of values | _list of 6 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SERVERS-MENU.SERVER_STATUS.LORE</code> (6 items)</summary>

```yaml
LORE:
  - '&8&m---------------------'
  - '&bStatus: %status%'
  - '&aPlayers: &a%players% online'
  - '&eSoftware: &a%software%'
  - '&6Performance: %performance%'
  - '&8&m---------------------'
```

</details>

#### `SERVERS-MENU.SERVER_STATUS.MATERIALS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVERS-MENU.SERVER_STATUS.MATERIALS.ONLINE` | `string` | Any text | `LIME_CONCRETE` | Online. |
| `SERVERS-MENU.SERVER_STATUS.MATERIALS.OFFLINE` | `string` | Any text | `RED_CONCRETE` | Offline. |

### `SERVERS-MENU.SERVERS`

#### `SERVERS-MENU.SERVERS.crystal`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SERVERS-MENU.SERVERS.crystal.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |

<details>
<summary>Default <code>SERVERS-MENU</code> block as shipped</summary>

```yaml
SERVERS-MENU:
  TITLE: '&8Ongoing Servers'
  SIZE: 27
  REFRESH-TICKS: 40
  PLACEHOLDER-MATERIAL: BLACK_STAINED_GLASS_PANE
  SERVER_STATUS:
    TITLE: '&8Ongoing Servers'
    SERVER_NAME: '&b%server%'
    LORE:
    - '&8&m---------------------'
    - '&bStatus: %status%'
    - '&aPlayers: &a%players% online'
    - '&eSoftware: &a%software%'
    - '&6Performance: %performance%'
    - '&8&m---------------------'
    MATERIALS:
      ONLINE: LIME_CONCRETE
      OFFLINE: RED_CONCRETE
  SERVERS:
    crystal:
      SLOT: 13
```

</details>

---

## Section: `SPAWNER-MENUS`

### Entry schema (6 entries)

Each entry under `SPAWNER-MENUS` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `TITLE` |
| :--- | :--- |
| `MAIN-MENU` | `{type} Spawners` |
| `STORAGE-MENU` | `{type} Spawners` |
| `FILTER-MENU` | `&8{mob} Filter Settings` |
| `SELL-CONFIRM-MENU` | `Confirm Sell` |
| `PANEL-MENU` | `&8Spawners` |
| `WORLD-LIST-MENU` | `&8Spawners Panel` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SIZE` | `integer` | Any integer | Required | Chest size. Must be a multiple of 9 between 9 and 54. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `ITEM-META` | `section` | — | Optional (2/6) | Options for item meta, listed below. |
| `BACK-BUTTON` | `section` | — | Optional (1/6) | Options for back button, listed below. |
| `CANCEL-BUTTON` | `section` | — | Optional (1/6) | Options for cancel button, listed below. |
| `CONFIRM-BUTTON` | `section` | — | Optional (1/6) | Options for confirm button, listed below. |
| `DISABLE-ALL-BUTTON` | `section` | — | Optional (1/6) | Options for disable all button, listed below. |
| `DROP-LOOT-BUTTON` | `section` | — | Optional (1/6) | Options for drop loot button, listed below. |
| `ENABLE-ALL-BUTTON` | `section` | — | Optional (1/6) | Options for enable all button, listed below. |
| `FILLER-MATERIAL` | `string` | Any text | Optional (1/6) | Filler material. |
| `INFO-ICON` | `section` | — | Optional (1/6) | Options for info icon, listed below. |
| `ITEMS-PER-PAGE` | `integer` | Any integer | Optional (1/6) | Items per page. |
| `MOB-HEAD-BUTTON` | `section` | — | Optional (1/6) | Options for mob head button, listed below. |
| `NEXT-PAGE-BUTTON` | `section` | — | Optional (1/6) | Options for next page button, listed below. |
| `PREVIOUS-PAGE-BUTTON` | `section` | — | Optional (1/6) | Options for previous page button, listed below. |
| `SELL-ALL-BUTTON` | `section` | — | Optional (1/6) | Options for sell all button, listed below. |

#### `SPAWNER-MENUS.<entry>.ITEM-META`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `FILTER-DISABLED` | `string` | Any text | Optional (1/2) | Filter disabled. |
| `FILTER-ENABLED` | `string` | Any text | Optional (1/2) | Filter enabled. |

#### `SPAWNER-MENUS.<entry>.BACK-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.CANCEL-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.CONFIRM-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.DISABLE-ALL-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.DROP-LOOT-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.ENABLE-ALL-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.INFO-ICON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

#### `SPAWNER-MENUS.<entry>.MOB-HEAD-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.NEXT-PAGE-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.PREVIOUS-PAGE-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

#### `SPAWNER-MENUS.<entry>.SELL-ALL-BUTTON`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |

<details>
<summary>Default <code>SPAWNER-MENUS</code> block as shipped</summary>

```yaml
SPAWNER-MENUS:
  MAIN-MENU:
    TITLE: '{type} Spawners'
    SIZE: 54
    FILLER-MATERIAL: AIR
  STORAGE-MENU:
    TITLE: '{type} Spawners'
    SIZE: 54
    ITEMS-PER-PAGE: 45
    ITEM-META:
      TITLE: '&f{material}'
      LORE:
      - '&a$ &f{price}'
    SELL-ALL-BUTTON:
      SLOT: 48
      MATERIAL: GOLD_INGOT
      TITLE: '&fSell All'
      LORE:
      - '&7&oClick to sell all loot'
    MOB-HEAD-BUTTON:
      SLOT: 49
      TITLE: '&a{amount} {type} Spawner'
    DROP-LOOT-BUTTON:
      SLOT: 50
      MATERIAL: DROPPER
      TITLE: '&fDrop Loot'
      LORE:
      - '&7&oClick to drop all loot on the page'
    NEXT-PAGE-BUTTON:
      SLOT: 53
      MATERIAL: ARROW
      TITLE: '&fNext page'
      LORE:
      - '&7&oClick to view next page'
    PREVIOUS-PAGE-BUTTON:
      SLOT: 45
      MATERIAL: ARROW
      TITLE: '&fPrevious page'
      LORE:
      - '&7&oClick to view previous page'
  FILTER-MENU:
    TITLE: '&8{mob} Filter Settings'
    SIZE: 27
    ITEM-META:
      TITLE: '&b{material}'
      LORE:
      - '&7Filter Status: {filter_status}'
      - '&7Drop Chance: &e{chance}%'
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `VOICE-CHAT-CONSENT-MENU`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `VOICE-CHAT-CONSENT-MENU.TITLE` | `string` | Any text | `&8Confirm Voice Chat` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `VOICE-CHAT-CONSENT-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `VOICE-CHAT-CONSENT-MENU.INFO-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `VOICE-CHAT-CONSENT-MENU.INFO-BUTTON.DISPLAY-NAME` | `string` | Any text | `&bVoice Chat Policy` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `VOICE-CHAT-CONSENT-MENU.INFO-BUTTON.MATERIAL` | `string` | Any text | `JUKEBOX` | Bukkit `Material` name for the icon. |
| `VOICE-CHAT-CONSENT-MENU.INFO-BUTTON.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `VOICE-CHAT-CONSENT-MENU.INFO-BUTTON.LORE` | `list` | A list of values | _list of 12 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>VOICE-CHAT-CONSENT-MENU.INFO-BUTTON.LORE</code> (12 items)</summary>

```yaml
LORE:
  - '&7Your voice is recorded while you are'
  - '&7talking in a voice channel.'
  - ''
  - '&7Recordings are thrown away unless somebody'
  - '&7reports you. A reported recording is kept'
  - '&7as proof for a mute or a ban, and the'
  - '&7moderation team reviews it.'
  - ''
  - '&7You have to be 13 or older to talk.'
  - ''
  - '&7Changed your mind later? Run'
  - '&f/voicechatconsent revoke'
```

</details>

### `VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON.DISPLAY-NAME` | `string` | Any text | `&aConfirm` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>VOICE-CHAT-CONSENT-MENU.CONFIRM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fClick to turn voice chat on'
```

</details>

### `VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON.DISPLAY-NAME` | `string` | Any text | `&cDecline` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>VOICE-CHAT-CONSENT-MENU.DECLINE-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fVoice chat will stay disabled'
```

</details>

<details>
<summary>Default <code>VOICE-CHAT-CONSENT-MENU</code> block as shipped</summary>

```yaml
VOICE-CHAT-CONSENT-MENU:
  TITLE: '&8Confirm Voice Chat'
  SIZE: 27
  INFO-BUTTON:
    DISPLAY-NAME: '&bVoice Chat Policy'
    MATERIAL: JUKEBOX
    SLOT: 13
    LORE:
    - '&7Your voice is recorded while you are'
    - '&7talking in a voice channel.'
    - ''
    - '&7Recordings are thrown away unless somebody'
    - '&7reports you. A reported recording is kept'
    - '&7as proof for a mute or a ban, and the'
    - '&7moderation team reviews it.'
    - ''
    - '&7You have to be 13 or older to talk.'
    - ''
    - '&7Changed your mind later? Run'
    - '&f/voicechatconsent revoke'
  CONFIRM-BUTTON:
    DISPLAY-NAME: '&aConfirm'
    MATERIAL: LIME_STAINED_GLASS_PANE
    SLOT: 11
    LORE:
    - '&fClick to turn voice chat on'
  DECLINE-BUTTON:
    DISPLAY-NAME: '&cDecline'
    MATERIAL: RED_STAINED_GLASS_PANE
    SLOT: 15
    LORE:
    - '&fVoice chat will stay disabled'
```

</details>

---

Defaults above match the file shipped in the jar.
