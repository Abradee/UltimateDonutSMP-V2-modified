# `staff-mode.yml`

Staff mode and vanish. Entering staff mode stores the player's survival inventory, swaps in
the tool bar defined by `ITEMS` and `CUSTOM-ITEMS`, and restores everything on exit — so a
crash mid-session should not cost anyone their gear.

`FAKE-PLAYER` belongs to a related trick: `/fakeplayer` spawns a convincing decoy to draw
out players who behave differently when they think no staff are online. Note that the
fake player feature checks the legacy permission prefix `ultimatedonutsmp.staff.fakeplayer`
— without the `2` — which is easy to miss when setting up permissions.

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/staff-mode.yml` |
| **Commands** | `/staffmode`, `/vanish`, `/stafflist`, `/fakeplayer` |
| **Player-facing text** | Edit `CONFIG.STAFF_MODE` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`STAFF-MODE`](#section-staff-mode) | section | 20 keys |
| [`ITEMS`](#section-items) | section | 5 keys |
| [`CUSTOM-ITEMS`](#section-custom-items) | section | 1 keys |
| [`MENUS`](#section-menus) | section | 2 keys |
| [`MESSAGES`](#section-messages) | section | 18 keys |
| [`FAKE-PLAYER`](#section-fake-player) | section | 5 keys |

---

## Section: `STAFF-MODE`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `STAFF-MODE` section on or off. |
| `STAFF-MODE.AUTO-VANISH-ON-ENABLE` | `boolean` | `true`, `false` | `false` | On/off for auto vanish on enable. |
| `STAFF-MODE.PERSIST-ON-QUIT` | `boolean` | `true`, `false` | `true` | On/off for persist on quit. |
| `STAFF-MODE.PERSIST-ON-RESTART` | `boolean` | `true`, `false` | `true` | On/off for persist on restart. |
| `STAFF-MODE.LOCK-TOOLS` | `boolean` | `true`, `false` | `true` | On/off for lock tools. |
| `STAFF-MODE.RESTORE-INVENTORY-ON-DISABLE` | `boolean` | `true`, `false` | `true` | On/off for restore inventory on disable. |
| `STAFF-MODE.STAFF-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.ADMIN-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.admin.staffmode` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.VANISH-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.vanish` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.BETTER-VIEW-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.betterview` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.STAFF-LIST-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.stafflist` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.RANDOM-TELEPORT-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.randomtp` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.SPECTATOR-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.spectator` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.SEE-VANISHED-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.seevanished` | Permission node. Leave empty to allow everyone. |
| `STAFF-MODE.OTHERS-PERMISSION` | `string` | Any text | `ultimatedonutsmp2.staff.mode.others` | Permission node. Leave empty to allow everyone. |

### `STAFF-MODE.LUCKPERMS-CONTEXT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.LUCKPERMS-CONTEXT.ENABLED` | `boolean` | `true`, `false` | `true` | Determines whether LuckPerms Context Provider is enabled. |
| `STAFF-MODE.LUCKPERMS-CONTEXT.KEY` | `string` | Any text | `staffmode` | The context key registered with LuckPerms (e.g. staffmode=true/false) |

### `STAFF-MODE.VANISH-ACTIONBAR`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.VANISH-ACTIONBAR.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `VANISH-ACTIONBAR` section on or off. |
| `STAFF-MODE.VANISH-ACTIONBAR.INTERVAL-TICKS` | `integer` | Any integer | `40` | Interval ticks. Ticks (20 = 1 second). |
| `STAFF-MODE.VANISH-ACTIONBAR.MESSAGE` | `string` | Any text | `&aVANISHED &7&gt;&gt; &fYou are hidden from regular players` | Message. |

### `STAFF-MODE.VANISH-FAKE-MESSAGES`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.VANISH-FAKE-MESSAGES.FAKE-LEAVE-ON-VANISH` | `boolean` | `true`, `false` | `true` | Determines whether Fake Leave Message on Vanish is enabled. |
| `STAFF-MODE.VANISH-FAKE-MESSAGES.FAKE-JOIN-ON-UNVANISH` | `boolean` | `true`, `false` | `true` | Determines whether Fake Join Message on Unvanish is enabled. |
| `STAFF-MODE.VANISH-FAKE-MESSAGES.ONLY-TO-REGULAR-PLAYERS` | `boolean` | `true`, `false` | `true` | If true, fake join/leave messages are only sent to players who cannot see vanished staff. |

### `STAFF-MODE.HOTBAR-SLOTS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.HOTBAR-SLOTS.VANISH` | `integer` | Any integer | `0` | Vanish. |
| `STAFF-MODE.HOTBAR-SLOTS.FREEZE` | `integer` | Any integer | `1` | Freeze. |
| `STAFF-MODE.HOTBAR-SLOTS.STAFF_LIST` | `integer` | Any integer | `4` | Staff list. |
| `STAFF-MODE.HOTBAR-SLOTS.BETTER_VIEW` | `integer` | Any integer | `7` | Better view. |
| `STAFF-MODE.HOTBAR-SLOTS.RANDOM_TELEPORT` | `integer` | Any integer | `8` | Random teleport. |
| `STAFF-MODE.HOTBAR-SLOTS.SPECTATOR` | `integer` | Any integer | `6` | Spectator. |

### `STAFF-MODE.BETTER-VIEW`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.BETTER-VIEW.ENABLE-NIGHT-VISION` | `boolean` | `true`, `false` | `true` | On/off for enable night vision. |
| `STAFF-MODE.BETTER-VIEW.ENABLE-FLIGHT` | `boolean` | `true`, `false` | `true` | On/off for enable flight. |
| `STAFF-MODE.BETTER-VIEW.AUTO-FLY` | `boolean` | `true`, `false` | `true` | On/off for auto fly. |

### `STAFF-MODE.RANDOM-TELEPORT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `STAFF-MODE.RANDOM-TELEPORT.EXCLUDE-STAFF` | `boolean` | `true`, `false` | `true` | On/off for exclude staff. |
| `STAFF-MODE.RANDOM-TELEPORT.EXCLUDE-VANISHED` | `boolean` | `true`, `false` | `true` | On/off for exclude vanished. |
| `STAFF-MODE.RANDOM-TELEPORT.EXCLUDE-FROZEN` | `boolean` | `true`, `false` | `false` | On/off for exclude frozen. |
| `STAFF-MODE.RANDOM-TELEPORT.EXCLUDE-DUELS` | `boolean` | `true`, `false` | `true` | On/off for exclude duels. |
| `STAFF-MODE.RANDOM-TELEPORT.EXCLUDE-FFA` | `boolean` | `true`, `false` | `true` | On/off for exclude ffa. |
| `STAFF-MODE.RANDOM-TELEPORT.NOTIFY-TARGET` | `boolean` | `true`, `false` | `false` | On/off for notify target. |

<details>
<summary>Default <code>STAFF-MODE</code> block as shipped</summary>

```yaml
# Configuration section for Staff Mode.
STAFF-MODE:
  # Determines whether Enabled is enabled or disabled. Available options: true, false
  ENABLED: true
  # Determines whether Auto Vanish On Enable is enabled or disabled. Available options: true, false
  AUTO-VANISH-ON-ENABLE: false
  # Determines whether Persist On Quit is enabled or disabled. Available options: true, false
  PERSIST-ON-QUIT: true
  # Determines whether Persist On Restart is enabled or disabled. Available options: true, false
  PERSIST-ON-RESTART: true
  # Determines whether Lock Tools is enabled or disabled. Available options: true, false
  LOCK-TOOLS: true
  # Determines whether Restore Inventory On Disable is enabled or disabled. Available options: true, false
  RESTORE-INVENTORY-ON-DISABLE: true
  # Configuration section for LuckPerms Context.
  LUCKPERMS-CONTEXT:
    # Determines whether LuckPerms Context Provider is enabled. Available options: true, false
    ENABLED: true
    # The context key registered with LuckPerms (e.g. staffmode=true/false)
    KEY: "staffmode"
  # Configuration section for Vanish Actionbar.
  VANISH-ACTIONBAR:
    # Determines whether Enabled is enabled or disabled. Available options: true, false
    ENABLED: true
    # The numerical value for Interval Ticks. Available options: Any valid integer
    INTERVAL-TICKS: 40
    # The text or value for Message. Available options: Any valid string text
    MESSAGE: '&aVANISHED &7>> &fYou are hidden from regular players'
  # Configuration section for Vanish Fake Messages.
  VANISH-FAKE-MESSAGES:
    # Determines whether Fake Leave Message on Vanish is enabled. Available options: true, false
    FAKE-LEAVE-ON-VANISH: true
    # Determines whether Fake Join Message on Unvanish is enabled. Available options: true, false
    FAKE-JOIN-ON-UNVANISH: true
    # If true, fake join/leave messages are only sent to players who cannot see vanished staff. Available options: true, false
    ONLY-TO-REGULAR-PLAYERS: true
  # The text or value for Staff Permission. Available options: Any valid string text
  STAFF-PERMISSION: ultimatedonutsmp2.staff.mode
  # The text or value for Admin Permission. Available options: Any valid string text
  ADMIN-PERMISSION: ultimatedonutsmp2.admin.staffmode
  # The text or value for Vanish Permission. Available options: Any valid string text
  VANISH-PERMISSION: ultimatedonutsmp2.staff.mode.vanish
  # The text or value for Better View Permission. Available options: Any valid string text
  BETTER-VIEW-PERMISSION: ultimatedonutsmp2.staff.mode.betterview
  # The text or value for Staff List Permission. Available options: Any valid string text
  STAFF-LIST-PERMISSION: ultimatedonutsmp2.staff.mode.stafflist
  # The text or value for Random Teleport Permission. Available options: Any valid string text
  RANDOM-TELEPORT-PERMISSION: ultimatedonutsmp2.staff.mode.randomtp
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `ITEMS`

### `ITEMS.VANISH`

#### `ITEMS.VANISH.ENABLED`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.VANISH.ENABLED.MATERIAL` | `string` | Any text | `LIME_DYE` | Bukkit `Material` name for the icon. |
| `ITEMS.VANISH.ENABLED.NAME` | `string` | Any text | `&aVanished` | Display name shown to players. |
| `ITEMS.VANISH.ENABLED.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.VANISH.ENABLED.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to disable vanish'
```

</details>

#### `ITEMS.VANISH.DISABLED`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.VANISH.DISABLED.MATERIAL` | `string` | Any text | `GRAY_DYE` | Bukkit `Material` name for the icon. |
| `ITEMS.VANISH.DISABLED.NAME` | `string` | Any text | `&7Unvanished` | Display name shown to players. |
| `ITEMS.VANISH.DISABLED.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.VANISH.DISABLED.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to enable vanish'
```

</details>

### `ITEMS.FREEZE`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.FREEZE.MATERIAL` | `string` | Any text | `ICE` | Bukkit `Material` name for the icon. |
| `ITEMS.FREEZE.NAME` | `string` | Any text | `&bFreeze Player` | Display name shown to players. |
| `ITEMS.FREEZE.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.FREEZE.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&7Right-click a player to freeze them'
  - '&7Left-click to see frozen players'
```

</details>

### `ITEMS.STAFF_LIST`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.STAFF_LIST.MATERIAL` | `string` | Any text | `CLOCK` | Bukkit `Material` name for the icon. |
| `ITEMS.STAFF_LIST.NAME` | `string` | Any text | `&eStaff List` | Display name shown to players. |
| `ITEMS.STAFF_LIST.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.STAFF_LIST.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to view online staff members'
```

</details>

### `ITEMS.BETTER_VIEW`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.BETTER_VIEW.MATERIAL` | `string` | Any text | `ORANGE_CARPET` | Bukkit `Material` name for the icon. |
| `ITEMS.BETTER_VIEW.NAME` | `string` | Any text | `&eBetter View` | Display name shown to players. |
| `ITEMS.BETTER_VIEW.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.BETTER_VIEW.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to toggle better view mode'
```

</details>

### `ITEMS.RANDOM_TELEPORT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ITEMS.RANDOM_TELEPORT.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `ITEMS.RANDOM_TELEPORT.NAME` | `string` | Any text | `&eRandom Teleport` | Display name shown to players. |
| `ITEMS.RANDOM_TELEPORT.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>ITEMS.RANDOM_TELEPORT.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to teleport to a random player'
```

</details>

<details>
<summary>Default <code>ITEMS</code> block as shipped</summary>

```yaml
ITEMS:
  # Configuration section for Vanish.
  VANISH:
    # Configuration section for Enabled.
    ENABLED:
      MATERIAL: LIME_DYE
      NAME: '&aVanished'
      LORE:
      - '&7Click to disable vanish'
    # Configuration section for Disabled.
    DISABLED:
      MATERIAL: GRAY_DYE
      NAME: '&7Unvanished'
      LORE:
      - '&7Click to enable vanish'
  # Configuration section for Freeze.
  FREEZE:
    MATERIAL: ICE
    NAME: '&bFreeze Player'
    LORE:
    - '&7Right-click a player to freeze them'
    - '&7Left-click to see frozen players'
  # Configuration section for Staff List.
  STAFF_LIST:
    MATERIAL: CLOCK
    NAME: '&eStaff List'
    LORE:
    - '&7Click to view online staff members'
  # Configuration section for Better View.
  BETTER_VIEW:
    MATERIAL: ORANGE_CARPET
    NAME: '&eBetter View'
    LORE:
    - '&7Click to toggle better view mode'
  # Configuration section for Random Teleport.
  RANDOM_TELEPORT:
    MATERIAL: PLAYER_HEAD
    NAME: '&eRandom Teleport'
    LORE:
    - '&7Click to teleport to a random player'
```

</details>

---

## Section: `CUSTOM-ITEMS`

### `CUSTOM-ITEMS.EXAMPLE`

Example entry. Set ENABLED to true to hand it out, or delete the whole block to remove it.

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CUSTOM-ITEMS.EXAMPLE.ENABLED` | `boolean` | `true`, `false` | `false` | Turns the `EXAMPLE` section on or off. |
| `CUSTOM-ITEMS.EXAMPLE.SLOT` | `integer` | Any valid integer between 0 and 8 | `2` | Inventory slot, `0` is the top-left cell. |
| `CUSTOM-ITEMS.EXAMPLE.MATERIAL` | `string` | Any text | `DIRT` | Bukkit `Material` name for the icon. |
| `CUSTOM-ITEMS.EXAMPLE.NAME` | `string` | Any text | `&eCustom Command` | Display name shown to players. |
| `CUSTOM-ITEMS.EXAMPLE.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |
| `CUSTOM-ITEMS.EXAMPLE.EXECUTE-AS` | `string` | PLAYER, CONSOLE | `PLAYER` | Determines who runs the commands. |
| `CUSTOM-ITEMS.EXAMPLE.PERMISSION` | `string` | Any text | `''` | The permission needed to receive and use this item. Leave empty to allow every staff member. |
| `CUSTOM-ITEMS.EXAMPLE.REQUIRE-TARGET` | `boolean` | `true`, `false` | `false` | Determines whether the item must be right-clicked on a player. |
| `CUSTOM-ITEMS.EXAMPLE.COMMANDS` | `list` | A list of values | _list of 1 item_ | The commands to run, without a leading slash. Placeholders: {player}, {player_uuid}, {world}, and, when REQUIRE-TARGET is true, {target} and {target_uuid}. |

<details>
<summary>Default contents of <code>CUSTOM-ITEMS.EXAMPLE.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to execute custom command'
```

</details>

<details>
<summary>Default contents of <code>CUSTOM-ITEMS.EXAMPLE.COMMANDS</code> (1 item)</summary>

```yaml
COMMANDS:
  - 'say Hello from {player}'
```

</details>

<details>
<summary>Default <code>CUSTOM-ITEMS</code> block as shipped</summary>

```yaml
# Configuration section for Custom Items.
# Admin defined hotbar items that run commands. Each entry is free-form: the key is the item id,
# and the id is what identifies the item in-game, so keep it unique.
# Slots already taken by the tools above (see STAFF-MODE.HOTBAR-SLOTS) are refused, and so are
# duplicate slots, so move a built-in tool first if you need its slot.
# SECURITY: EXECUTE-AS: CONSOLE runs the command with full console rights, which means any staff
# member holding the item bypasses their own permissions. Always pair CONSOLE items with a
# PERMISSION so only the ranks you trust receive them.
CUSTOM-ITEMS:
  # Example entry. Set ENABLED to true to hand it out, or delete the whole block to remove it.
  EXAMPLE:
    # Determines whether Enabled is enabled or disabled. Available options: true, false
    ENABLED: false
    # The numerical value for Slot. Available options: Any valid integer between 0 and 8
    SLOT: 2
    MATERIAL: DIRT
    NAME: '&eCustom Command'
    LORE:
    - '&7Click to execute custom command'
    # Determines who runs the commands. Available options: PLAYER, CONSOLE
    EXECUTE-AS: PLAYER
    # The permission needed to receive and use this item. Leave empty to allow every staff member.
    PERMISSION: ''
    # Determines whether the item must be right-clicked on a player. Available options: true, false
    REQUIRE-TARGET: false
    # The commands to run, without a leading slash.
    # Placeholders: {player}, {player_uuid}, {world}, and, when REQUIRE-TARGET is true,
    # {target} and {target_uuid}.
    COMMANDS:
    - 'say Hello from {player}'
```

</details>

---

## Section: `MENUS`

### `MENUS.STAFF-LIST`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MENUS.STAFF-LIST.TITLE` | `string` | Any text | `&8Online Staff` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `MENUS.STAFF-LIST.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `MENUS.STAFF-LIST.REFRESH-SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |
| `MENUS.STAFF-LIST.PLACEHOLDER-MATERIAL` | `string` | Any text | `GRAY_STAINED_GLASS_PANE` | Placeholder material. |
| `MENUS.STAFF-LIST.CONTENT-SLOTS` | `list` | A list of values | _list of 21 items_ | The content slots list. |
| `MENUS.STAFF-LIST.EMPTY-MATERIAL` | `string` | Any text | `BARRIER` | Empty material. |
| `MENUS.STAFF-LIST.EMPTY-NAME` | `string` | Any text | `&cNo staff online` | Empty name. |
| `MENUS.STAFF-LIST.EMPTY-LORE` | `list` | A list of values | _list of 1 item_ | The empty lore list. |

<details>
<summary>Default contents of <code>MENUS.STAFF-LIST.CONTENT-SLOTS</code> (21 items)</summary>

```yaml
CONTENT-SLOTS:
  - 10
  - 11
  - 12
  - 13
  - 14
  - 15
  - 16
  - 19
  - 20
  - 21
  - 22
  - 23
  - 24
  - 25
  - 28
  - 29
  - 30
  - 31
  - 32
  - 33
  - 34
```

</details>

<details>
<summary>Default contents of <code>MENUS.STAFF-LIST.EMPTY-LORE</code> (1 item)</summary>

```yaml
EMPTY-LORE:
  - '&7No staff members are currently online.'
```

</details>

### `MENUS.FROZEN-PLAYERS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MENUS.FROZEN-PLAYERS.TITLE` | `string` | Any text | `&8Frozen Players` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `MENUS.FROZEN-PLAYERS.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `MENUS.FROZEN-PLAYERS.REFRESH-SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |
| `MENUS.FROZEN-PLAYERS.PLACEHOLDER-MATERIAL` | `string` | Any text | `LIGHT_BLUE_STAINED_GLASS_PANE` | Placeholder material. |
| `MENUS.FROZEN-PLAYERS.CONTENT-SLOTS` | `list` | A list of values | _list of 21 items_ | The content slots list. |
| `MENUS.FROZEN-PLAYERS.EMPTY-MATERIAL` | `string` | Any text | `BARRIER` | Empty material. |
| `MENUS.FROZEN-PLAYERS.EMPTY-NAME` | `string` | Any text | `&aNo frozen players` | Empty name. |
| `MENUS.FROZEN-PLAYERS.EMPTY-LORE` | `list` | A list of values | _list of 1 item_ | The empty lore list. |

<details>
<summary>Default contents of <code>MENUS.FROZEN-PLAYERS.CONTENT-SLOTS</code> (21 items)</summary>

```yaml
CONTENT-SLOTS:
  - 10
  - 11
  - 12
  - 13
  - 14
  - 15
  - 16
  - 19
  - 20
  - 21
  - 22
  - 23
  - 24
  - 25
  - 28
  - 29
  - 30
  - 31
  - 32
  - 33
  - 34
```

</details>

<details>
<summary>Default contents of <code>MENUS.FROZEN-PLAYERS.EMPTY-LORE</code> (1 item)</summary>

```yaml
EMPTY-LORE:
  - '&7There are no active frozen players.'
```

</details>

<details>
<summary>Default <code>MENUS</code> block as shipped</summary>

```yaml
# Configuration section for Menus.
MENUS:
  # Configuration section for Staff List.
  STAFF-LIST:
    TITLE: '&8Online Staff'
    SIZE: 54
    # The numerical value for Refresh Slot. Available options: Any valid integer
    REFRESH-SLOT: 49
    # The text or value for Placeholder Material. Available options: Any valid string text
    PLACEHOLDER-MATERIAL: GRAY_STAINED_GLASS_PANE
    # Configuration section for Content Slots.
    CONTENT-SLOTS:
    - 10
    - 11
    - 12
    - 13
    - 14
    - 15
    - 16
    - 19
    - 20
    - 21
    - 22
    - 23
    - 24
    - 25
    - 28
    - 29
    - 30
    - 31
    - 32
    - 33
    - 34
    # The text or value for Empty Material. Available options: Any valid string text
    EMPTY-MATERIAL: BARRIER
    # The text or value for Empty Name. Available options: Any valid string text
    EMPTY-NAME: '&cNo staff online'
    # Configuration section for Empty Lore.
    EMPTY-LORE:
    - '&7No staff members are currently online.'
  # Configuration section for Frozen Players.
  FROZEN-PLAYERS:
    TITLE: '&8Frozen Players'
    SIZE: 54
    # The numerical value for Refresh Slot. Available options: Any valid integer
    REFRESH-SLOT: 49
    # The text or value for Placeholder Material. Available options: Any valid string text
    PLACEHOLDER-MATERIAL: LIGHT_BLUE_STAINED_GLASS_PANE
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `MESSAGES`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MESSAGES.NO-PERMISSION` | `string` | Any text | `&cYou do not have permission.` | Permission node. Leave empty to allow everyone. |
| `MESSAGES.FEATURE-DISABLED` | `string` | Any text | `&cStaff mode is currently disabled.` | Feature disabled. |
| `MESSAGES.PLAYER-ONLY` | `string` | Any text | `&cOnly players can use this command.` | Player only. |
| `MESSAGES.ENABLED` | `string` | Any text | `&aStaff mode enabled.` | Enabled. |
| `MESSAGES.DISABLED` | `string` | Any text | `&cStaff mode disabled.` | Disabled. |
| `MESSAGES.ENABLED-ACTIONBAR` | `string` | Any text | `&aStaff mode enabled` | Enabled actionbar. |
| `MESSAGES.DISABLED-ACTIONBAR` | `string` | Any text | `&cStaff mode disabled` | Disabled actionbar. |
| `MESSAGES.VANISH-ON` | `string` | Any text | `&aVanish enabled.` | Vanish on. |
| `MESSAGES.VANISH-OFF` | `string` | Any text | `&cVanish disabled.` | Vanish off. |
| `MESSAGES.BETTER-VIEW-ON` | `string` | Any text | `&aBetter View enabled.` | Better view on. |
| `MESSAGES.BETTER-VIEW-OFF` | `string` | Any text | `&cBetter View disabled.` | Better view off. |
| `MESSAGES.RANDOM-TELEPORT-NO-TARGET` | `string` | Any text | `&cNo eligible player found.` | Random teleport no target. |
| `MESSAGES.RANDOM-TELEPORT-SUCCESS` | `string` | Any text | `&eTeleported to &f{player}&e.` | Random teleport success. |
| `MESSAGES.RESTORE-FAILED` | `string` | Any text | `&cStaff mode restore failed. Contact an admin.` | Restore failed. |
| `MESSAGES.RECOVERED-AFTER-RESTART` | `string` | Any text | `&eStaff mode was disabled because the server restarted. Y…` | Recovered after restart. |
| `MESSAGES.TOOL-LOCKED` | `string` | Any text | `&cYour staff tools are locked while Staff Mode is active.` | Tool locked. |
| `MESSAGES.CUSTOM-ITEM-NO-TARGET` | `string` | Any text | `&cRight-click a player to use this tool.` | Custom item no target. |
| `MESSAGES.RELOAD-SUCCESS` | `string` | Any text | `&aStaff mode config reloaded.` | Reload success. |

<details>
<summary>Default <code>MESSAGES</code> block as shipped</summary>

```yaml
MESSAGES:
  # The text or value for No Permission. Available options: Any valid string text
  NO-PERMISSION: '&cYou do not have permission.'
  # The text or value for Feature Disabled. Available options: Any valid string text
  FEATURE-DISABLED: '&cStaff mode is currently disabled.'
  # The text or value for Player Only. Available options: Any valid string text
  PLAYER-ONLY: '&cOnly players can use this command.'
  # The text or value for Enabled. Available options: Any valid string text
  ENABLED: '&aStaff mode enabled.'
  # The text or value for Disabled. Available options: Any valid string text
  DISABLED: '&cStaff mode disabled.'
  # The text or value for Enabled Actionbar. Available options: Any valid string text
  ENABLED-ACTIONBAR: '&aStaff mode enabled'
  # The text or value for Disabled Actionbar. Available options: Any valid string text
  DISABLED-ACTIONBAR: '&cStaff mode disabled'
  # The text or value for Vanish On. Available options: Any valid string text
  VANISH-ON: '&aVanish enabled.'
  # The text or value for Vanish Off. Available options: Any valid string text
  VANISH-OFF: '&cVanish disabled.'
  # The text or value for Better View On. Available options: Any valid string text
  BETTER-VIEW-ON: '&aBetter View enabled.'
  # The text or value for Better View Off. Available options: Any valid string text
  BETTER-VIEW-OFF: '&cBetter View disabled.'
  # The text or value for Random Teleport No Target. Available options: Any valid string text
  RANDOM-TELEPORT-NO-TARGET: '&cNo eligible player found.'
  # The text or value for Random Teleport Success. Available options: Any valid string text
  RANDOM-TELEPORT-SUCCESS: '&eTeleported to &f{player}&e.'
  # The text or value for Restore Failed. Available options: Any valid string text
  RESTORE-FAILED: '&cStaff mode restore failed. Contact an admin.'
  # The text or value for Recovered After Restart. Available options: Any valid string text
  RECOVERED-AFTER-RESTART: '&eStaff mode was disabled because the server restarted.
    Your inventory was restored.'
  # The text or value for Tool Locked. Available options: Any valid string text
  TOOL-LOCKED: '&cYour staff tools are locked while Staff Mode is active.'
  # The text or value for Custom Item No Target. Available options: Any valid string text
  CUSTOM-ITEM-NO-TARGET: '&cRight-click a player to use this tool.'
  # The text or value for Reload Success. Available options: Any valid string text
  RELOAD-SUCCESS: '&aStaff mode config reloaded.'
```

</details>

---

## Section: `FAKE-PLAYER`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `FAKE-PLAYER.USE-DEFAULT-SKIN` | `boolean` | `true`, `false` | `false` | When true, /fakeplayer uses Minecraft's default Steve/Alex skin instead of the staff member's. |
| `FAKE-PLAYER.HIDE-NAMETAG` | `boolean` | `true`, `false` | `true` | When true, /fakeplayer does not copy the staff member's username, so prefix, suffix, and money text stay off the head. |
| `FAKE-PLAYER.SNEAK` | `boolean` | `true`, `false` | `true` | When true, the bait crouches like DonutSMP. Crouching also hides leftover nametags on most clients. |
| `FAKE-PLAYER.SPAWN-AT-LOOK-TARGET` | `boolean` | `true`, `false` | `true` | When true, /fakeplayer appears at the block or point you are looking at instead of at your feet. |
| `FAKE-PLAYER.LOOK-RANGE` | `decimal` | Any decimal number 1.0 or greater | `32.0` | How far, in blocks, to search for the block you are looking at. |

<details>
<summary>Default <code>FAKE-PLAYER</code> block as shipped</summary>

```yaml
FAKE-PLAYER:
  # When true, /fakeplayer uses Minecraft's default Steve/Alex skin instead of the staff member's.
  # Available options: true, false
  USE-DEFAULT-SKIN: false
  # When true, /fakeplayer does not copy the staff member's username, so prefix, suffix, and money text stay off the head.
  # Available options: true, false
  HIDE-NAMETAG: true
  # When true, the bait crouches like DonutSMP. Crouching also hides leftover nametags on most clients.
  # Available options: true, false
  SNEAK: true
  # When true, /fakeplayer appears at the block or point you are looking at instead of at your feet.
  # Available options: true, false
  SPAWN-AT-LOOK-TARGET: true
  # How far, in blocks, to search for the block you are looking at.
  # Available options: Any decimal number 1.0 or greater
  LOOK-RANGE: 32.0
```

</details>

---

Defaults above match the file shipped in the jar.
