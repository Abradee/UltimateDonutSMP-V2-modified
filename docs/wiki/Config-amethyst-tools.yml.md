# `amethyst-tools.yml`

Amethyst Tools are the timed premium tools sold in the Shard Shop: a drill that breaks a
3x3 face at once, a chopper that fells a whole tree, and the other variants defined here.
Each tool is handed out with a lifetime attached, and `AmethystToolsTask` removes it from
the player's inventory when that timer runs out — which is why every tool has a `DURATION`
as well as the usual enchantment and behaviour settings.

`AMETHYST-TOOLS` defines the tools themselves; `AMETHYST-MESSAGES` holds the chat and
action-bar strings the feature sends. Tools are granted by `/amethysttool` (Dialog API: pick type, player, duration) or by a Shard
Shop entry in `shop.yml` that sets `AMETHYST-TOOL` and `AMETHYST-DURATION`. `{type}` and `{time}`
are replaced in the received-chat line; `{time}` is `1h` / `5d`, never `3600`.

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/amethyst-tools.yml` |
| **Commands** | `/amethysttool` opens the Dialog API give flow. `/amethysttool give <player> <type> [duration]`, `/amethysttool reload`. `/shardtool` is the same command. Duration accepts `1h`, `5d`, `1d 5h`, or seconds. |
| **Player-facing text** | Edit `CONFIG.AMETHYST_TOOLS` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`AMETHYST-TOOLS`](#section-amethyst-tools) | section | 11 keys |
| [`AMETHYST-MESSAGES`](#section-amethyst-messages) | section | 27 keys |

---

## Section: `AMETHYST-TOOLS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST-TOOLS.EXCLUDED-WORLDS` | `list` | A list of values | _list of 1 item_ | The excluded worlds list. |

<details>
<summary>Default contents of <code>AMETHYST-TOOLS.EXCLUDED-WORLDS</code> (1 item)</summary>

```yaml
EXCLUDED-WORLDS:
  - 'duels'
```

</details>

### `AMETHYST-TOOLS.PARTICLES`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST-TOOLS.PARTICLES.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `PARTICLES` section on or off. |
| `AMETHYST-TOOLS.PARTICLES.TYPE` | `string` | Any text | `BLOCK` | Which option this section uses. |
| `AMETHYST-TOOLS.PARTICLES.BLOCK-MATERIAL` | `string` | Any text | `PURPLE_CONCRETE_POWDER` | Block material. |
| `AMETHYST-TOOLS.PARTICLES.COUNT` | `integer` | Any integer | `12` | Count. |
| `AMETHYST-TOOLS.PARTICLES.SPREAD` | `decimal` | Any decimal number | `0.4` | Spread. |

### `AMETHYST-TOOLS.SOUNDS`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST-TOOLS.SOUNDS.USE` | `string` | Any text | `minecraft:block.amethyst_block.hit\|1.0\|1.2` | Use. |
| `AMETHYST-TOOLS.SOUNDS.EXPIRE` | `string` | Any text | `minecraft:entity.lightning_bolt.impact\|0.5\|2.0` | Expire. |
| `AMETHYST-TOOLS.SOUNDS.BREAK` | `string` | Any text | `minecraft:block.amethyst_block.break\|1.0\|0.8` | Break. |
| `AMETHYST-TOOLS.SOUNDS.ACTIVATE` | `string` | Any text | `minecraft:block.amethyst_block.resonate\|1.0\|1.0` | Activate. |

### `AMETHYST-TOOLS.SECURITY`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST-TOOLS.SECURITY.REQUIRE-ITEM-ID` | `boolean` | `true`, `false` | `true` | On/off for require item id. |
| `AMETHYST-TOOLS.SECURITY.BIND-TO-OWNER` | `boolean` | `true`, `false` | `false` | On/off for bind to owner. |
| `AMETHYST-TOOLS.SECURITY.CLICK-COOLDOWN-MS` | `integer` | Any integer | `250` | Click cooldown ms. Milliseconds. |
| `AMETHYST-TOOLS.SECURITY.BLOCK-HOPPER-PICKUP` | `boolean` | `true`, `false` | `true` | On/off for block hopper pickup. |

### Entry schema (7 entries)

Each entry under `AMETHYST-TOOLS` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `NAME` | `MATERIAL` |
| :--- | :--- | :--- |
| `DRILL` | `&#9B59B6&lAmethyst Drill` | `NETHERITE_PICKAXE` |
| `CHOPPER` | `&#9B59B6&lAmethyst Tree Chopper` | `NETHERITE_AXE` |
| `SELL-AXE` | `&#9B59B6&lAmethyst Sell Axe` | `NETHERITE_AXE` |
| `SHOVEL` | `&#9B59B6&lAmethyst Shovel` | `NETHERITE_SHOVEL` |
| `BUCKET` | `&#9B59B6&lAmethyst Bucket` | `BUCKET` |
| `SHARD-BOOSTER` | `&#9B59B6&lShard Booster` | `POTION` |
| `HASTE-POTION` | `&#A303F9Shard Potion of Haste` | `POTION` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DURATION` | `integer` | Any integer | Required | Lifetime in seconds. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SHARD-SHOP` | `section` | — | Required | Options for shard shop, listed below. |
| `ENCHANTMENTS` | `list` | A list of values | Optional (4/7) | The enchantments list. |
| `RADIUS` | `integer` | Any integer | Optional (2/7) | Radius. Blocks. |
| `ALLOWED-BLOCKS` | `list` | A list of values | Optional (1/7) | The allowed blocks list. |
| `BOOSTER-DURATION` | `integer` | Any integer | Optional (1/7) | Booster duration. |
| `DISABLED-BLOCKS` | `list` | A list of values | Optional (1/7) | The disabled blocks list. |
| `DRAIN-RADIUS` | `integer` | Any integer | Optional (1/7) | Drain radius. Blocks. |
| `LOG-BLOCKS` | `list` | A list of values | Optional (1/7) | The log blocks list. |
| `MAX-DRAIN` | `integer` | Any integer | Optional (1/7) | Max drain. |
| `MAX-LOGS` | `integer` | Any integer | Optional (1/7) | Max logs. |

#### `AMETHYST-TOOLS.<entry>.SHARD-SHOP`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CURRENCY` | `string` | SHARD, MONEY | Required | The currency this tool is sold for. |
| `DEFAULT-QUANTITY` | `integer` | Any integer | Required | Default quantity. |
| `ENABLED` | `boolean` | `true`, `false` | Required | Turns the `SHARD-SHOP` section on or off. |
| `HIDE-QUANTITY-BUTTONS` | `boolean` | `true`, `false` | Required | On/off for hide quantity buttons. |
| `MAX-QUANTITY` | `integer` | Any integer | Required | Max quantity. |
| `MIN-QUANTITY` | `integer` | Any integer | Required | Min quantity. |
| `PRICE-PER-UNIT` | `decimal` | Any decimal number | Required | Price for one item. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |

<details>
<summary>Default <code>AMETHYST-TOOLS</code> block as shipped</summary>

```yaml
# Configuration section for Amethyst Tools.
AMETHYST-TOOLS:
  # Configuration section for Excluded Worlds.
  EXCLUDED-WORLDS:
  - duels
  # Configuration section for Particles.
  PARTICLES:
    # Determines whether Enabled is enabled or disabled. Available options: true, false
    ENABLED: true
    TYPE: BLOCK
    # The text or value for Block Material. Available options: Any valid string text
    BLOCK-MATERIAL: PURPLE_CONCRETE_POWDER
    # The numerical value for Count. Available options: Any valid integer
    COUNT: 12
    # The decimal value for Spread. Available options: Any decimal number
    SPREAD: 0.4
  # Configuration section for Sounds.
  SOUNDS:
    # The text or value for Use. Available options: Any valid string text
    USE: minecraft:block.amethyst_block.hit|1.0|1.2
    # The text or value for Expire. Available options: Any valid string text
    EXPIRE: minecraft:entity.lightning_bolt.impact|0.5|2.0
    # The text or value for Break. Available options: Any valid string text
    BREAK: minecraft:block.amethyst_block.break|1.0|0.8
    # The text or value for Activate. Available options: Any valid string text
    ACTIVATE: minecraft:block.amethyst_block.resonate|1.0|1.0
  # Configuration section for Security.
  SECURITY:
    # Determines whether Require Item Id is enabled or disabled. Available options: true, false
    REQUIRE-ITEM-ID: true
    # Determines whether Bind To Owner is enabled or disabled. Available options: true, false
    BIND-TO-OWNER: false
    # The numerical value for Click Cooldown Ms. Available options: Any valid integer
    CLICK-COOLDOWN-MS: 250
    # Determines whether Block Hopper Pickup is enabled or disabled. Available options: true, false
    BLOCK-HOPPER-PICKUP: true
  # Configuration section for Drill.
  DRILL:
    MATERIAL: NETHERITE_PICKAXE
    NAME: '&#9B59B6&lAmethyst Drill'
    LORE:
    - '&#BDC3C7Breaks &e9 blocks &7per strike'
    - '&#BDC3C7Powered by amethyst energy'
    - ''
    - '&#9B59B6✦ Self Destruct'
    - '&#BDC3C7{time}'
    # Configuration section for Enchantments.
    ENCHANTMENTS:
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `AMETHYST-MESSAGES`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST-MESSAGES.PREFIX` | `string` | Any text | `&#9B59B6[Amethyst] &r` | Text prepended to every message from this feature. |
| `AMETHYST-MESSAGES.EXPIRED` | `string` | Any text | `{prefix}&#BDC3C7Your &e{tool} &7has &cexpired &7and self-…` | Expired. |
| `AMETHYST-MESSAGES.EXCLUDED-WORLD` | `string` | Any text | `{prefix}&#BDC3C7Amethyst Tools cannot be used in this wor…` | Excluded world. |
| `AMETHYST-MESSAGES.DRILL-BREAK` | `string` | Any text | `{prefix}&#BDC3C7&eAmethyst Drill &7broke &e{count} &7bloc…` | Drill break. |
| `AMETHYST-MESSAGES.CHOP-BREAK` | `string` | Any text | `{prefix}&#BDC3C7&eAmethyst Tree Chopper &7chopped &e{coun…` | Chop break. |
| `AMETHYST-MESSAGES.SELL-SUCCESS` | `string` | Any text | `{prefix}&#BDC3C7Sold all chest contents for &a${amount}&7.` | Sell success. |
| `AMETHYST-MESSAGES.SELL-EMPTY` | `string` | Any text | `{prefix}&#BDC3C7That chest is empty or has no sellable it…` | Sell empty. |
| `AMETHYST-MESSAGES.SELL-FAILED` | `string` | Any text | `{prefix}&#BDC3C7The sale failed. no items were removed.` | Sell failed. |
| `AMETHYST-MESSAGES.SELL-NO-CHEST` | `string` | Any text | `{prefix}&#BDC3C7You must right-click a chest.` | Sell no chest. |
| `AMETHYST-MESSAGES.BUCKET-DRAIN` | `string` | Any text | `{prefix}&#BDC3C7Drained &e{count} &7water blocks.` | Bucket drain. |
| `AMETHYST-MESSAGES.BUCKET-NO-WATER` | `string` | Any text | `{prefix}&#BDC3C7No water blocks found nearby.` | Bucket no water. |
| `AMETHYST-MESSAGES.BOOSTER-ACTIVATED` | `string` | Any text | `{prefix}&#BDC3C7&eShard Booster &7activated! &e4x &7shard…` | Booster activated. |
| `AMETHYST-MESSAGES.BOOSTER-ALREADY` | `string` | Any text | `{prefix}&#BDC3C7You already have an active shard booster!` | Booster already. |
| `AMETHYST-MESSAGES.NO-PERMISSION` | `string` | Any text | `{prefix}&#BDC3C7You do not have permission to use this it…` | Permission node. Leave empty to allow everyone. |
| `AMETHYST-MESSAGES.WRONG-OWNER` | `string` | Any text | `{prefix}&#BDC3C7This Amethyst Tool is bound to another pl…` | Wrong owner. |
| `AMETHYST-MESSAGES.GIVE-SUCCESS` | `string` | Any text | `{prefix}&#BDC3C7Gave &e{type} &7to &e{player}&7. &8({time})` | Admin confirmation after an online grant. `{type}` `{player}` `{time}` |
| `AMETHYST-MESSAGES.GIVE-QUEUED` | `string` | Any text | `{prefix}&#BDC3C7Queued &e{type} &7for &e{player}&7. They w…` | Admin confirmation when the target is offline. `{type}` `{player}` `{time}` |
| `AMETHYST-MESSAGES.GIVE-FAILED` | `string` | Any text | `{prefix}&#BDC3C7Failed to create &e{type}&7. Check the am…` | Item creation failed. `{type}` |
| `AMETHYST-MESSAGES.GIVE-PLAYER-NOT-FOUND` | `string` | Any text | `{prefix}&#BDC3C7Player not found: &e{player}` | Unknown player. `{player}` |
| `AMETHYST-MESSAGES.RECEIVED` | `string` | Any text | `{prefix}&#BDC3C7You received &e{type} &7for &e{time}&7.` | Chat to the player who got the tool, including on join. `{type}` `{time}` |
| `AMETHYST-MESSAGES.PLAYER-ONLINE` | `string` | Any text | `&aOnline` | Player-picker lore while the target is online. |
| `AMETHYST-MESSAGES.PLAYER-OFFLINE` | `string` | Any text | `&7Offline ({time} last online)` | Player-picker lore while offline. `{time}` is `d`/`h`/`m`. |
| `AMETHYST-MESSAGES.DURATION-INVALID` | `string` | Any text | `{prefix}&#BDC3C7Invalid duration. Use &e1h&7, &e5h&7, &e1…` | Custom duration could not be parsed. |
| `AMETHYST-MESSAGES.DIALOG-UNAVAILABLE` | `string` | Any text | `{prefix}&#BDC3C7Dialogs are unavailable. Usage: &e/amethy…` | Shown when `/amethysttool` is used without Dialog API. |
| `AMETHYST-MESSAGES.GIVE-USAGE` | `string` | Any text | `{prefix}&#BDC3C7Usage: &e/amethysttool &7or &e/amethystto…` | Chat usage. |
| `AMETHYST-MESSAGES.GIVE-INVALID-TYPE` | `string` | Any text | `{prefix}&#BDC3C7Invalid tool type. Types: DRILL, CHOPPER,…` | Give invalid type. |
| `AMETHYST-MESSAGES.RELOAD-SUCCESS` | `string` | Any text | `{prefix}&#BDC3C7Configuration reloaded.` | Reload success. |

<details>
<summary>Default <code>AMETHYST-MESSAGES</code> block as shipped</summary>

```yaml
# Configuration section for Amethyst Messages.
AMETHYST-MESSAGES:
  # The text or value for Prefix. Available options: Any valid string text
  PREFIX: '&#9B59B6[Amethyst] &r'
  # The text or value for Expired. Available options: Any valid string text
  EXPIRED: '{prefix}&#BDC3C7Your &e{tool} &7has &cexpired &7and self-destructed!'
  # The text or value for Excluded World. Available options: Any valid string text
  EXCLUDED-WORLD: '{prefix}&#BDC3C7Amethyst Tools cannot be used in this world.'
  # The text or value for Drill Break. Available options: Any valid string text
  DRILL-BREAK: '{prefix}&#BDC3C7&eAmethyst Drill &7broke &e{count} &7blocks.'
  # The text or value for Chop Break. Available options: Any valid string text
  CHOP-BREAK: '{prefix}&#BDC3C7&eAmethyst Tree Chopper &7chopped &e{count} &7logs.'
  # The text or value for Sell Success. Available options: Any valid string text
  SELL-SUCCESS: '{prefix}&#BDC3C7Sold all chest contents for &a${amount}&7.'
  # The text or value for Sell Empty. Available options: Any valid string text
  SELL-EMPTY: '{prefix}&#BDC3C7That chest is empty or has no sellable items.'
  # The text or value for Sell Failed. Available options: Any valid string text
  SELL-FAILED: '{prefix}&#BDC3C7The sale failed. no items were removed.'
  # The text or value for Sell No Chest. Available options: Any valid string text
  SELL-NO-CHEST: '{prefix}&#BDC3C7You must right-click a chest.'
  # The text or value for Bucket Drain. Available options: Any valid string text
  BUCKET-DRAIN: '{prefix}&#BDC3C7Drained &e{count} &7water blocks.'
  # The text or value for Bucket No Water. Available options: Any valid string text
  BUCKET-NO-WATER: '{prefix}&#BDC3C7No water blocks found nearby.'
  # The text or value for Booster Activated. Available options: Any valid string text
  BOOSTER-ACTIVATED: '{prefix}&#BDC3C7&eShard Booster &7activated! &e4x &7shards for
    &e60 minutes&7.'
  # The text or value for Booster Already. Available options: Any valid string text
  BOOSTER-ALREADY: '{prefix}&#BDC3C7You already have an active shard booster!'
  # The text or value for No Permission. Available options: Any valid string text
  NO-PERMISSION: '{prefix}&#BDC3C7You do not have permission to use this item.'
  # The text or value for Wrong Owner. Available options: Any valid string text
  WRONG-OWNER: '{prefix}&#BDC3C7This Amethyst Tool is bound to another player.'
  # The text or value for Give Success. Available options: Any valid string text
  GIVE-SUCCESS: '{prefix}&#BDC3C7Gave &e{type} &7to &e{player}&7. &8({time})'
  GIVE-QUEUED: '{prefix}&#BDC3C7Queued &e{type} &7for &e{player}&7. They will receive it for &e{time} &7when they join.'
  GIVE-FAILED: '{prefix}&#BDC3C7Failed to create &e{type}&7. Check the amethyst-tools config.'
  GIVE-PLAYER-NOT-FOUND: '{prefix}&#BDC3C7Player not found: &e{player}'
  RECEIVED: '{prefix}&#BDC3C7You received &e{type} &7for &e{time}&7.'
  PLAYER-ONLINE: '&aOnline'
  PLAYER-OFFLINE: '&7Offline ({time} last online)'
  DURATION-INVALID: '{prefix}&#BDC3C7Invalid duration. Use &e1h&7, &e5h&7, &e1d&7, or a number of seconds.'
  DIALOG-UNAVAILABLE: '{prefix}&#BDC3C7Dialogs are unavailable. Usage: &e/amethysttool give <player> <type> [duration]'
  GIVE-USAGE: '{prefix}&#BDC3C7Usage: &e/amethysttool &7or &e/amethysttool give <player> <type> [duration]'
  GIVE-INVALID-TYPE: '{prefix}&#BDC3C7Invalid tool type. Types: DRILL, CHOPPER, SELL_AXE,
    SHOVEL, BUCKET, SHARD_BOOSTER, HASTE_POTION'
  RELOAD-SUCCESS: '{prefix}&#BDC3C7Configuration reloaded.'
```

</details>

---

Defaults above match the file shipped in the jar.
