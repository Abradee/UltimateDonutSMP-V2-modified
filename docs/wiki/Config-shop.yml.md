# `shop.yml`

The server shop, in two halves. `SHOP-GUI` and `QUICK-BUY` are the DonutSMP-style quick buy
flow, which fills orders straight from the Auction House rather than from an infinite admin
stock. `SHARD-MENU` is the separate premium shop bought with Shards.

Shard shop entries can do more than give an item: `COMMAND` runs a console command,
`AMETHYST-TOOL` together with `AMETHYST-DURATION` grants one of the timed tools from
`amethyst-tools.yml`, and the quantity keys control how many a player may buy in one go.
Sell prices are not set here — they come from `worth.yml`.

Comment at the top of the shipped file:

```text
UltimateDonutSMP2 Shop Configuration
/shop is modern DonutSMP Quick Buy with full Auction House integration.
```

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/shop.yml` |
| **Commands** | `/shop`, `/shardshop`, `/sell` |
| **Player-facing text** | Edit `CONFIG.SHOP` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`SHARD-MENU`](#section-shard-menu) | section | 25 keys |
| [`SHOP-GUI`](#section-shop-gui) | section | 4 keys |
| [`QUICK-BUY`](#section-quick-buy) | section | 10 keys |
| [`YOUR-ITEMS`](#section-your-items) | section | 11 keys |
| [`INSERT-ITEM`](#section-insert-item) | section | 4 keys |
| [`CONFIRM-LISTING`](#section-confirm-listing) | section | 5 keys |
| [`TRANSACTIONS`](#section-transactions) | section | 4 keys |

---

## Section: `SHARD-MENU`

Shard Shop Menu (/shardshop) — layout locked to Design/Shards Shop

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHARD-MENU.TITLE` | `string` | Any text | `&8Shard Shop` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `SHARD-MENU.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `SHARD-MENU.CURRENCY` | `string` | Any text | `SHARD` | `MONEY` or `SHARD`. |

### `SHARD-MENU.BUTTONS`

#### `SHARD-MENU.BUTTONS.BALANCE`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHARD-MENU.BUTTONS.BALANCE.SLOT` | `integer` | Any integer | `49` | Inventory slot, `0` is the top-left cell. |
| `SHARD-MENU.BUTTONS.BALANCE.MATERIAL` | `string` | Any text | `AMETHYST_SHARD` | Bukkit `Material` name for the icon. |
| `SHARD-MENU.BUTTONS.BALANCE.NAME` | `string` | Any text | `&fShard Shop` | Display name shown to players. |
| `SHARD-MENU.BUTTONS.BALANCE.LORE` | `list` | A list of values | _list of 3 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SHARD-MENU.BUTTONS.BALANCE.LORE</code> (3 items)</summary>

```yaml
LORE:
  - '&fYou have &#A303F9{shards} Shards'
  - ''
  - '&fVisit our website to purchase shards'
```

</details>

#### `SHARD-MENU.BUTTONS.RANKS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHARD-MENU.BUTTONS.RANKS.SLOT` | `integer` | Any integer | `50` | Inventory slot, `0` is the top-left cell. |
| `SHARD-MENU.BUTTONS.RANKS.MATERIAL` | `string` | Any text | `PLAYER_HEAD` | Bukkit `Material` name for the icon. |
| `SHARD-MENU.BUTTONS.RANKS.HEAD-TEXTURE` | `string` | Any text | `''` | Leave HEAD-TEXTURE empty so the icon stays the default Steve head. |
| `SHARD-MENU.BUTTONS.RANKS.NAME` | `string` | Any text | `&fRanks` | Display name shown to players. |
| `SHARD-MENU.BUTTONS.RANKS.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SHARD-MENU.BUTTONS.RANKS.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fClick to view'
```

</details>

### Entry schema (21 entries)

Each entry under `SHARD-MENU` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `DISPLAY-NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `SHARD-PICKAXE-SILK-ITEM` | `&#A303F9Shard Pickaxe` | `NETHERITE_PICKAXE` | `0` |
| `SHARD-PICKAXE-FORTUNE-ITEM` | `&#A303F9Shard Pickaxe` | `NETHERITE_PICKAXE` | `1` |
| `SHARD-AXE-ITEM` | `&#A303F9Shard Axe` | `NETHERITE_AXE` | `2` |
| `SHARD-SELL-AXE-ITEM` | `&#A303F9Shard Sell Axe` | `NETHERITE_AXE` | `3` |
| `SHARD-SHOVEL-SILK-ITEM` | `&#A303F9Shard Shovel` | `NETHERITE_SHOVEL` | `4` |
| `SHARD-SHOVEL-FORTUNE-ITEM` | `&#A303F9Shard Shovel` | `NETHERITE_SHOVEL` | `5` |
| `SHARD-HASTE-POTION-ITEM` | `&#A303F9Shard Potion of Haste` | `POTION` | `6` |
| `NETHERITE-HELMET-ITEM` | `&fNetherite Helmet` | `NETHERITE_HELMET` | `7` |
| `NETHERITE-CHESTPLATE-ITEM` | `&fNetherite Chestplate` | `NETHERITE_CHESTPLATE` | `8` |
| `NETHERITE-LEGGINGS-ITEM` | `&fNetherite Leggings` | `NETHERITE_LEGGINGS` | `9` |
| `NETHERITE-BOOTS-ITEM` | `&fNetherite Boots` | `NETHERITE_BOOTS` | `10` |
| `NETHERITE-SWORD-ITEM` | `&fNetherite Sword` | `NETHERITE_SWORD` | `11` |
| `NETHERITE-PICKAXE-SILK-ITEM` | `&fNetherite Pickaxe` | `NETHERITE_PICKAXE` | `12` |
| `NETHERITE-PICKAXE-FORTUNE-ITEM` | `&fNetherite Pickaxe` | `NETHERITE_PICKAXE` | `13` |
| `NETHERITE-SHOVEL-ITEM` | `&fNetherite Shovel` | `NETHERITE_SHOVEL` | `14` |
| `NETHERITE-AXE-ITEM` | `&fNetherite Axe` | `NETHERITE_AXE` | `15` |
| `MACE-DENSITY-ITEM` | `&fMace` | `MACE` | `16` |
| `NETHERITE-SPEAR-ITEM` | `&fNetherite Spear` | `NETHERITE_SPEAR` | `17` |
| `NETHERITE-HOE-ITEM` | `&fNetherite Hoe` | `NETHERITE_HOE` | `18` |
| `CROSSBOW-PIERCING-ITEM` | `&fCrossbow` | `CROSSBOW` | `19` |
| `BOW-ITEM` | `&fBow` | `BOW` | `20` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CURRENCY` | `string` | Any text | Required | `MONEY` or `SHARD`. |
| `DEFAULT-QUANTITY` | `integer` | Any integer | Required | Default quantity. |
| `DISPLAY-NAME` | `string` | Any text | Required | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `HIDE-QUANTITY-BUTTONS` | `boolean` | `true`, `false` | Required | On/off for hide quantity buttons. |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `MAX-QUANTITY` | `integer` | Any integer | Required | Max quantity. |
| `MIN-QUANTITY` | `integer` | Any integer | Required | Min quantity. |
| `PRICE-PER-UNIT` | `integer` | Any integer | Required | Price for one item. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `ENCHANTMENTS` | `list` | A list of values | Optional (20/21) | The enchantments list. |
| `VANILLA-NAME` | `boolean` | `true`, `false` | Optional (14/21) | On/off for vanilla name. |
| `AMETHYST-DURATION` | `integer` | Any integer | Optional (7/21) | Amethyst duration. |
| `AMETHYST-TOOL` | `string` | Any text | Optional (7/21) | Amethyst tool. |
| `GIVE-ITEM` | `boolean` | `true`, `false` | Optional (7/21) | On/off for give item. |
| `HIDE-ENCHANTS` | `boolean` | `true`, `false` | Optional (7/21) | On/off for hide enchants. |
| `KEEP-DISPLAY` | `boolean` | `true`, `false` | Optional (7/21) | On/off for keep display. |
| `COMMAND` | `string` | Any text | Optional (6/21) | Console command, without a leading slash. Empty means none. |
| `POTION-COLOR` | `string` | Any text | Optional (1/21) | Potion color. |
| `POTION-EFFECTS` | `list` | A list of values | Optional (1/21) | The potion effects list. |

<details>
<summary>Default <code>SHARD-MENU</code> block as shipped</summary>

```yaml
# Shard Shop Menu (/shardshop) — layout locked to Design/Shards Shop
SHARD-MENU:
  TITLE: "&8Shard Shop"
  SIZE: 54
  CURRENCY: SHARD

  BUTTONS:
    BALANCE:
      SLOT: 49
      MATERIAL: AMETHYST_SHARD
      NAME: "&fShard Shop"
      LORE:
        - "&fYou have &#A303F9{shards} Shards"
        - ""
        - "&fVisit our website to purchase shards"
    RANKS:
      SLOT: 50
      MATERIAL: PLAYER_HEAD
      # Leave HEAD-TEXTURE empty so the icon stays the default Steve head.
      HEAD-TEXTURE: ""
      NAME: "&fRanks"
      LORE:
        - "&fClick to view"

  SHARD-PICKAXE-SILK-ITEM:
    CURRENCY: SHARD
    MATERIAL: "NETHERITE_PICKAXE"
    HIDE-ENCHANTS: true
    ENCHANTMENTS:
      - "EFFICIENCY:5"
      - "SILK_TOUCH:1"
      - "UNBREAKING:3"
      - "MENDING:1"
    DISPLAY-NAME: "&#A303F9Shard Pickaxe"
    SLOT: 0
    PRICE-PER-UNIT: 1500
    LORE:
      - "&fBreaks 9 Blocks at Once"
      - "&7Silk Touch"
      - "&7Efficiency V"
      - "&7Unbreaking III"
      - "&7Mending"
      - "&cSelf Destruct: 24h"
      - "&#A303F91.5K Shards"
    COMMAND: ""
    AMETHYST-TOOL: DRILL
    AMETHYST-DURATION: 86400
    GIVE-ITEM: true
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `SHOP-GUI`

Configuration section for Shop Web Analytics Server

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHOP-GUI.SHOW-AUCTION-PRICE` | `boolean` | `true`, `false` | `true` | On/off for show auction price. |

### `SHOP-GUI.ITEM`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHOP-GUI.ITEM.LORE` | `list` | A list of values | _list of 7 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>SHOP-GUI.ITEM.LORE</code> (7 items)</summary>

```yaml
LORE:
  - '&7Shop price: {shop_price}'
  - '{auction_line}'
  - ''
  - '{favorite_line}'
  - '&eLeft-click to buy from the shop'
  - '{auction_action}'
  - '{favorite_action}'
```

</details>

### `SHOP-GUI.FAVORITES`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHOP-GUI.FAVORITES.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `FAVORITES` section on or off. |

### `SHOP-GUI.WEB-SERVER`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHOP-GUI.WEB-SERVER.ENABLED` | `boolean` | `true`, `false` | `false` | Set to true to enable the embedded Shop Analytics web server |
| `SHOP-GUI.WEB-SERVER.PORT` | `integer` | Any integer | `8080` | Port for the embedded web server (e.g. 8080 or 25580) |
| `SHOP-GUI.WEB-SERVER.PUBLIC-URL` | `string` | Any text | `''` | Optional custom domain or public URL (e.g. "https://stats.ultimatedonutsmp2mc.net" or "http://192.168.1.15:8080/stats") If left empty (""), it automatically resolves to http://localhost:&lt;PORT&gt;/stats |

<details>
<summary>Default <code>SHOP-GUI</code> block as shipped</summary>

```yaml
# Configuration section for Shop Web Analytics Server
SHOP-GUI:
  SHOW-AUCTION-PRICE: true
  # Configuration section for Item.
  ITEM:
    # Configuration section for Lore.
    LORE:
    - '&7Shop price: {shop_price}'
    - '{auction_line}'
    - ''
    - '{favorite_line}'
    - '&eLeft-click to buy from the shop'
    - '{auction_action}'
    - '{favorite_action}'
  FAVORITES:
    ENABLED: true
  WEB-SERVER:
    # Set to true to enable the embedded Shop Analytics web server
    ENABLED: false
    # Port for the embedded web server (e.g. 8080 or 25580)
    PORT: 8080
    # Optional custom domain or public URL (e.g. "https://stats.ultimatedonutsmp2mc.net" or "http://192.168.1.15:8080/stats")
    # If left empty (""), it automatically resolves to http://localhost:<PORT>/stats
    PUBLIC-URL: ""
```

</details>

---

## Section: `QUICK-BUY`

Quick Buy configuration (/shop overhaul)

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK-BUY.ENABLED` | `boolean` | `true`, `false` | `true` | Master toggle for Quick Buy system |
| `QUICK-BUY.ACTION-BAR` | `string` | Any text | `&fYou bought {amount} {item} for &a$ &f{price}` | Action bar message displayed above the hotbar when purchasing an item Placeholders: {amount}, {item}, {price}, {price_formatted} |
| `QUICK-BUY.PURCHASE-SOUND` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Optional override for the purchase sound. Leave blank to use sounds.yml QUICK_BUY.BUY-SUCCESS. |
| `QUICK-BUY.TITLE` | `string` | Any text | `&8Quick Buy` | Main GUI title and inventory size |
| `QUICK-BUY.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `QUICK-BUY.PRICING`

Dynamic pricing settings for Quick Buy and market price estimation

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK-BUY.PRICING.USE-AUCTION-HOUSE` | `boolean` | `true`, `false` | `true` | Whether to follow the lowest active Auction House price when available |
| `QUICK-BUY.PRICING.WORTH-MULTIPLIER` | `decimal` | Any decimal number | `1.0` | Multiplier applied when using baseline price from worth.yml (default: 1.0) |
| `QUICK-BUY.PRICING.AUTO-BALANCE-MISSING` | `boolean` | `true`, `false` | `true` | Automatically calculate a fair balanced price for items missing from worth.yml |
| `QUICK-BUY.PRICING.AUTO-BALANCE-MULTIPLIER` | `decimal` | Any decimal number | `1.0` | Multiplier applied to automatically balanced prices (default: 1.0) |

### `QUICK-BUY.CHOOSE-ITEM`

Item selection dialog configuration (Choose Item screen)

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK-BUY.CHOOSE-ITEM.MODE` | `string` | Any text | `VANILLA` | Mode for obtaining selectable items: VANILLA: all items/blocks/armor/swords directly from vanilla Minecraft matching the server version (default) CUSTOM: items configured in worth.yml |
| `QUICK-BUY.CHOOSE-ITEM.MAX-ITEMS` | `integer` | Any integer | `2000` | Maximum number of items to display in the selection list (default: 2000) |
| `QUICK-BUY.CHOOSE-ITEM.BLACKLIST` | `list` | A list of values | _list of 30 items_ | Extra materials hidden from Quick Buy's Choose Item screen. Does not affect /orders. Wildcards such as *_SPAWN_EGG are allowed. Command-block variants, infested blocks, and spawn eggs are always hidden even if omitted here. |

<details>
<summary>Default contents of <code>QUICK-BUY.CHOOSE-ITEM.BLACKLIST</code> (30 items)</summary>

```yaml
BLACKLIST:
  - 'BEDROCK'
  - 'BARRIER'
  - 'COMMAND_BLOCK'
  - 'CHAIN_COMMAND_BLOCK'
  - 'REPEATING_COMMAND_BLOCK'
  - 'COMMAND_BLOCK_MINECART'
  - 'STRUCTURE_BLOCK'
  - 'STRUCTURE_VOID'
  - 'JIGSAW'
  - 'LIGHT'
  - 'DEBUG_STICK'
  - 'KNOWLEDGE_BOOK'
  - 'TEST_BLOCK'
  - 'TEST_INSTANCE_BLOCK'
  - 'END_PORTAL_FRAME'
  - 'SPAWNER'
  - 'TRIAL_SPAWNER'
  - 'VAULT'
  - 'SPAWNER_EGG'
  - 'REINFORCED_DEEPSLATE'
  - 'BUDDING_AMETHYST'
  - 'FARMLAND'
  - 'DIRT_PATH'
  - 'CHORUS_PLANT'
  - 'PETRIFIED_OAK_SLAB'
  - 'FROGSPAWN'
  - 'SUSPICIOUS_SAND'
  - 'SUSPICIOUS_GRAVEL'
  - '*_SPAWN_EGG'
  - 'INFESTED_*'
```

</details>

### `QUICK-BUY.EMPTY-SLOT`

Configuration for empty grid slots (slots 0-44)

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK-BUY.EMPTY-SLOT.MATERIAL` | `string` | Any text | `GRAY_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `QUICK-BUY.EMPTY-SLOT.NAME` | `string` | Any text | `&fEmpty` | Display name shown to players. |
| `QUICK-BUY.EMPTY-SLOT.LORE` | `list` | A list of values | _list of 2 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>QUICK-BUY.EMPTY-SLOT.LORE</code> (2 items)</summary>

```yaml
LORE:
  - '&o&7Click to choose'
  - '&o&7an item to buy'
```

</details>

### `QUICK-BUY.ITEM`

Item display and pricing lore formats for pinned quick buy items

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK-BUY.ITEM.PRICE-FORMAT` | `string` | Any text | `&a$ &f{price}` | Lore for the pinned item. `{price}` is the cost of the whole pin, unit price times the amount. |
| `QUICK-BUY.ITEM.OUT-OF-STOCK` | `string` | Any text | `&cOut of stock` | Out of stock. |
| `QUICK-BUY.ITEM.EDIT-REMOVE-LORE` | `string` | Any text | `&o&7Click to remove` | Edit remove lore. |
| `QUICK-BUY.ITEM.EDIT-RESTORE-LORE` | `string` | Any text | `&o&7Click to add back` | Edit restore lore. |

### `QUICK-BUY.BUTTONS`

Bottom navigation buttons in Quick Buy GUI

#### Entry schema (6 entries)

Each entry under `QUICK-BUY.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `FILTER` | `&fFilter` | `HOPPER` | `47` |
| `REFRESH` | `&fQuick Buy` | `ENDER_CHEST` | `48` |
| `AUCTION` | `&fAuction` | `ANVIL` | `49` |
| `SEARCH` | `&fSearch` | `OAK_SIGN` | `50` |
| `YOUR-ITEMS` | `&fYour Items` | `CHEST` | `51` |
| `EDIT` | `&fEdit` | `STICK` | `53` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `LORE` | `string` | Any text | Optional (5/6) | Tooltip lines under the item name. |
| `ACTIVE-QUERY-LORE` | `list` | A list of values | Optional (1/6) | The active query lore list. |
| `LORE-EDITING` | `string` | Any text | Optional (1/6) | Lore editing. |
| `LORE-NORMAL` | `string` | Any text | Optional (1/6) | Lore normal. |

<details>
<summary>Default <code>QUICK-BUY</code> block as shipped</summary>

```yaml
# Quick Buy configuration (/shop overhaul)
QUICK-BUY:
  # Master toggle for Quick Buy system
  ENABLED: true

  # Action bar message displayed above the hotbar when purchasing an item
  # Placeholders: {amount}, {item}, {price}, {price_formatted}
  ACTION-BAR: "&fYou bought {amount} {item} for &a$ &f{price}"

  # Optional override for the purchase sound. Leave blank to use sounds.yml QUICK_BUY.BUY-SUCCESS.
  PURCHASE-SOUND: "minecraft:entity.experience_orb.pickup|1.0|1.0"

  # Dynamic pricing settings for Quick Buy and market price estimation
  PRICING:
    # Whether to follow the lowest active Auction House price when available
    USE-AUCTION-HOUSE: true
    # Multiplier applied when using baseline price from worth.yml (default: 1.0)
    WORTH-MULTIPLIER: 1.0
    # Automatically calculate a fair balanced price for items missing from worth.yml
    AUTO-BALANCE-MISSING: true
    # Multiplier applied to automatically balanced prices (default: 1.0)
    AUTO-BALANCE-MULTIPLIER: 1.0

  # Main GUI title and inventory size
  TITLE: '&8Quick Buy'
  SIZE: 54

  # Item selection dialog configuration (Choose Item screen)
  CHOOSE-ITEM:
    # Mode for obtaining selectable items:
    # VANILLA: all items/blocks/armor/swords directly from vanilla Minecraft matching the server version (default)
    # CUSTOM: items configured in worth.yml
    MODE: VANILLA
    # Maximum number of items to display in the selection list (default: 2000)
    MAX-ITEMS: 2000
    # Extra materials hidden from Quick Buy's Choose Item screen. Does not affect /orders.
    # Wildcards such as *_SPAWN_EGG are allowed.
    # Command-block variants, infested blocks, and spawn eggs are always hidden even if omitted here.
    BLACKLIST:
      - BEDROCK
      - BARRIER
      - COMMAND_BLOCK
      - CHAIN_COMMAND_BLOCK
      - REPEATING_COMMAND_BLOCK
      - COMMAND_BLOCK_MINECART
      - STRUCTURE_BLOCK
      - STRUCTURE_VOID
      - JIGSAW
      - LIGHT
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `YOUR-ITEMS`

Auction -> Your Items menu configuration Page 1 (free rank): 18 List slots + 27 Donut+ locks. Page 2: 45 Donut++ locks (all five content rows), Previous page on the left. Visual slots follow rank, not listing limit.

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.TITLE` | `string` | Any text | `Auction -&gt; Your Items` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `YOUR-ITEMS.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `YOUR-ITEMS.UNLOCKED-SLOTS` | `integer` | Any integer | `18` | Unlocked slots. |
| `YOUR-ITEMS.DONUT-PLUS-SLOTS` | `integer` | Any integer | `45` | Donut plus slots. |
| `YOUR-ITEMS.PLUS-PAGE-SLOTS` | `integer` | Any integer | `45` | Plus page slots. |

### `YOUR-ITEMS.SELL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.SELL-BUTTON.SLOT` | `integer` | Any integer | `0` | Inventory slot, `0` is the top-left cell. |
| `YOUR-ITEMS.SELL-BUTTON.MATERIAL` | `string` | Any text | `GRAY_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `YOUR-ITEMS.SELL-BUTTON.NAME` | `string` | Any text | `&fList` | Display name shown to players. |
| `YOUR-ITEMS.SELL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>YOUR-ITEMS.SELL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&o&7Click to sell an item'
```

</details>

### `YOUR-ITEMS.EMPTY-SLOT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.EMPTY-SLOT.MATERIAL` | `string` | Any text | `GRAY_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `YOUR-ITEMS.EMPTY-SLOT.NAME` | `string` | Any text | `&fList` | Display name shown to players. |
| `YOUR-ITEMS.EMPTY-SLOT.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>YOUR-ITEMS.EMPTY-SLOT.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&o&7Click to sell an item'
```

</details>

### `YOUR-ITEMS.LOCKED-SLOT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.LOCKED-SLOT.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `YOUR-ITEMS.LOCKED-SLOT.NAME` | `string` | Any text | `&cLocked` | Display name shown to players. |
| `YOUR-ITEMS.LOCKED-SLOT.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>YOUR-ITEMS.LOCKED-SLOT.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fBuy Donut&b+&f for more auction slots'
```

</details>

### `YOUR-ITEMS.LOCKED-PLUS-SLOT`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.LOCKED-PLUS-SLOT.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `YOUR-ITEMS.LOCKED-PLUS-SLOT.NAME` | `string` | Any text | `&cLocked` | Display name shown to players. |
| `YOUR-ITEMS.LOCKED-PLUS-SLOT.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>YOUR-ITEMS.LOCKED-PLUS-SLOT.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fBuy Donut&b++&f for even more auction slots'
```

</details>

### `YOUR-ITEMS.ACTIVE-ITEM`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `YOUR-ITEMS.ACTIVE-ITEM.PRICE-FORMAT` | `string` | Any text | `&7Price: &a${price}` | Price format. |
| `YOUR-ITEMS.ACTIVE-ITEM.EXPIRES-FORMAT` | `string` | Any text | `&7Expires in: &e{time}` | Expires format. |
| `YOUR-ITEMS.ACTIVE-ITEM.CANCEL-LORE` | `string` | Any text | `&cClick to cancel listing` | Cancel lore. |

### `YOUR-ITEMS.BUTTONS`

#### Entry schema (7 entries)

Each entry under `YOUR-ITEMS.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `PREV` | `&fPrevious page` | `ARROW` | `45` |
| `FILTER` | `&fFilter` | `HOPPER` | `47` |
| `QUICK-BUY` | `&fQuick Buy` | `ENDER_CHEST` | `48` |
| `AUCTION` | `&fAuction` | `ANVIL` | `49` |
| `SEARCH` | `&fSearch` | `OAK_SIGN` | `50` |
| `TRANSACTIONS` | `&fTransactions` | `WRITABLE_BOOK` | `51` |
| `NEXT` | `&fNext page` | `ARROW` | `53` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `LORE` | `list` | A list of values | Required | Tooltip lines under the item name. |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `ACTIVE-QUERY-LORE` | `list` | A list of values | Optional (1/7) | The active query lore list. |

<details>
<summary>Default <code>YOUR-ITEMS</code> block as shipped</summary>

```yaml
# Auction -> Your Items menu configuration
# Page 1 (free rank): 18 List slots + 27 Donut+ locks. Page 2: 45 Donut++ locks
# (all five content rows), Previous page on the left. Visual slots follow rank, not listing limit.
YOUR-ITEMS:
  TITLE: 'Auction -> Your Items'
  SIZE: 54
  UNLOCKED-SLOTS: 18
  DONUT-PLUS-SLOTS: 45
  PLUS-PAGE-SLOTS: 45
  SELL-BUTTON:
    SLOT: 0
    MATERIAL: GRAY_STAINED_GLASS_PANE
    NAME: '&fList'
    LORE:
    - '&o&7Click to sell an item'
  EMPTY-SLOT:
    MATERIAL: GRAY_STAINED_GLASS_PANE
    NAME: '&fList'
    LORE:
    - '&o&7Click to sell an item'
  LOCKED-SLOT:
    MATERIAL: RED_STAINED_GLASS_PANE
    NAME: '&cLocked'
    LORE:
    - '&fBuy Donut&b+&f for more auction slots'
  LOCKED-PLUS-SLOT:
    MATERIAL: RED_STAINED_GLASS_PANE
    NAME: '&cLocked'
    LORE:
    - '&fBuy Donut&b++&f for even more auction slots'
  ACTIVE-ITEM:
    PRICE-FORMAT: '&7Price: &a${price}'
    EXPIRES-FORMAT: '&7Expires in: &e{time}'
    CANCEL-LORE: '&cClick to cancel listing'
  BUTTONS:
    PREV:
      SLOT: 45
      MATERIAL: ARROW
      NAME: '&fPrevious page'
      LORE:
      - '&o&7Click to view previous page'
    FILTER:
      SLOT: 47
      MATERIAL: HOPPER
      NAME: '&fFilter'
      LORE:
      - '&o&7Click to change'
    QUICK-BUY:
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `INSERT-ITEM`

Click to sell an item (5-slot hopper menu) configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `INSERT-ITEM.TITLE` | `string` | Any text | `Click to sell an item` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `INSERT-ITEM.ESTIMATED-PRICE-FORMAT` | `string` | Any text | `&a$ &f{price}` | Estimated price format. |

### `INSERT-ITEM.CANCEL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `INSERT-ITEM.CANCEL-BUTTON.SLOT` | `integer` | Any integer | `0` | Inventory slot, `0` is the top-left cell. |
| `INSERT-ITEM.CANCEL-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `INSERT-ITEM.CANCEL-BUTTON.NAME` | `string` | Any text | `&cCancel` | Display name shown to players. |
| `INSERT-ITEM.CANCEL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>INSERT-ITEM.CANCEL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&7Click to cancel and return'
```

</details>

### `INSERT-ITEM.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `INSERT-ITEM.CONFIRM-BUTTON.SLOT` | `integer` | Any integer | `4` | Inventory slot, `0` is the top-left cell. |
| `INSERT-ITEM.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `INSERT-ITEM.CONFIRM-BUTTON.NAME` | `string` | Any text | `&fConfirm` | Display name shown to players. |
| `INSERT-ITEM.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>INSERT-ITEM.CONFIRM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&o&7Click to confirm'
```

</details>

<details>
<summary>Default <code>INSERT-ITEM</code> block as shipped</summary>

```yaml
# Click to sell an item (5-slot hopper menu) configuration
INSERT-ITEM:
  TITLE: 'Click to sell an item'
  CANCEL-BUTTON:
    SLOT: 0
    MATERIAL: RED_STAINED_GLASS_PANE
    NAME: '&cCancel'
    LORE:
    - '&7Click to cancel and return'
  CONFIRM-BUTTON:
    SLOT: 4
    MATERIAL: LIME_STAINED_GLASS_PANE
    NAME: '&fConfirm'
    LORE:
    - '&o&7Click to confirm'
  ESTIMATED-PRICE-FORMAT: '&a$ &f{price}'
```

</details>

---

## Section: `CONFIRM-LISTING`

Confirm Listing (27-slot chest menu) configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CONFIRM-LISTING.TITLE` | `string` | Any text | `Confirm Listing` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `CONFIRM-LISTING.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `CONFIRM-LISTING.CANCEL-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CONFIRM-LISTING.CANCEL-BUTTON.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `CONFIRM-LISTING.CANCEL-BUTTON.MATERIAL` | `string` | Any text | `RED_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `CONFIRM-LISTING.CANCEL-BUTTON.NAME` | `string` | Any text | `&cCancel` | Display name shown to players. |
| `CONFIRM-LISTING.CANCEL-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>CONFIRM-LISTING.CANCEL-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&o&7Click to cancel the sale'
```

</details>

### `CONFIRM-LISTING.CONFIRM-BUTTON`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CONFIRM-LISTING.CONFIRM-BUTTON.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `CONFIRM-LISTING.CONFIRM-BUTTON.MATERIAL` | `string` | Any text | `LIME_STAINED_GLASS_PANE` | Bukkit `Material` name for the icon. |
| `CONFIRM-LISTING.CONFIRM-BUTTON.NAME` | `string` | Any text | `&fConfirm` | Display name shown to players. |
| `CONFIRM-LISTING.CONFIRM-BUTTON.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>CONFIRM-LISTING.CONFIRM-BUTTON.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&o&7Click to sell'
```

</details>

### `CONFIRM-LISTING.PREVIEW-ITEM`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CONFIRM-LISTING.PREVIEW-ITEM.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `CONFIRM-LISTING.PREVIEW-ITEM.NOTICE-LINE-1` | `string` | Any text | `&fYou're going to sell` | Notice line 1. |
| `CONFIRM-LISTING.PREVIEW-ITEM.NOTICE-LINE-2` | `string` | Any text | `&fthis item for &#00FC00$ &#00FC00{price}` | Notice line 2. |

<details>
<summary>Default <code>CONFIRM-LISTING</code> block as shipped</summary>

```yaml
# Confirm Listing (27-slot chest menu) configuration
CONFIRM-LISTING:
  TITLE: 'Confirm Listing'
  SIZE: 27
  CANCEL-BUTTON:
    SLOT: 11
    MATERIAL: RED_STAINED_GLASS_PANE
    NAME: '&cCancel'
    LORE:
    - '&o&7Click to cancel the sale'
  CONFIRM-BUTTON:
    SLOT: 15
    MATERIAL: LIME_STAINED_GLASS_PANE
    NAME: '&fConfirm'
    LORE:
    - '&o&7Click to sell'
  PREVIEW-ITEM:
    SLOT: 13
    NOTICE-LINE-1: '&fYou''re going to sell'
    NOTICE-LINE-2: '&fthis item for &#00FC00$ &#00FC00{price}'
```

</details>

---

## Section: `TRANSACTIONS`

Transactions log (54-slot chest menu) configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TRANSACTIONS.TITLE` | `string` | Any text | `Transactions` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `TRANSACTIONS.SIZE` | `integer` | Any integer | `54` | Chest size. Must be a multiple of 9 between 9 and 54. |

### `TRANSACTIONS.END-OF-LIST`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TRANSACTIONS.END-OF-LIST.MATERIAL` | `string` | Any text | `PAPER` | Bukkit `Material` name for the icon. |
| `TRANSACTIONS.END-OF-LIST.NAME` | `string` | Any text | `&cYou've reached the end` | Display name shown to players. |
| `TRANSACTIONS.END-OF-LIST.LORE` | `list` | A list of values | _list of 1 item_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>TRANSACTIONS.END-OF-LIST.LORE</code> (1 item)</summary>

```yaml
LORE:
  - '&fTransactions only last 90d'
```

</details>

### `TRANSACTIONS.BUTTONS`

#### Entry schema (6 entries)

Each entry under `TRANSACTIONS.BUTTONS` is keyed by a name you choose, and every entry accepts the same options:

#### Shipped entries

| Entry key | `NAME` | `MATERIAL` | `SLOT` |
| :--- | :--- | :--- | :--- |
| `STATS` | `&fStats` | `BOOK` | `48` |
| `REFRESH` | `&fTransactions` | `WRITABLE_BOOK` | `49` |
| `SEARCH` | `&fSearch` | `OAK_SIGN` | `50` |
| `PREV-PAGE` | `&fPrevious Page` | `ARROW` | `45` |
| `NEXT-PAGE` | `&fNext Page` | `ARROW` | `53` |
| `BACK` | `&fBack` | `ARROW` | `-1` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MATERIAL` | `string` | Any text | Required | Bukkit `Material` name for the icon. |
| `NAME` | `string` | Any text | Required | Display name shown to players. |
| `SLOT` | `integer` | Any integer | Required | Inventory slot, `0` is the top-left cell. |
| `LORE` | `list` | A list of values | Optional (5/6) | Tooltip lines under the item name. |
| `ACTIVE-QUERY-LORE` | `list` | A list of values | Optional (1/6) | The active query lore list. |
| `LORE-MADE` | `string` | Any text | Optional (1/6) | Lore made. |
| `LORE-SPENT` | `string` | Any text | Optional (1/6) | Lore spent. |

<details>
<summary>Default <code>TRANSACTIONS</code> block as shipped</summary>

```yaml
# Transactions log (54-slot chest menu) configuration
TRANSACTIONS:
  TITLE: 'Transactions'
  SIZE: 54
  END-OF-LIST:
    MATERIAL: PAPER
    NAME: '&cYou''ve reached the end'
    LORE:
    - '&fTransactions only last 90d'
  BUTTONS:
    STATS:
      SLOT: 48
      MATERIAL: BOOK
      NAME: '&fStats'
      LORE-SPENT: '&fTotal Spent: &a${spent}'
      LORE-MADE: '&fTotal Made: &a${made}'
    REFRESH:
      SLOT: 49
      MATERIAL: WRITABLE_BOOK
      NAME: '&fTransactions'
      LORE:
      - '&o&7Click to refresh'
    SEARCH:
      SLOT: 50
      MATERIAL: OAK_SIGN
      NAME: '&fSearch'
      LORE:
      - '&o&7Click to search'
      ACTIVE-QUERY-LORE:
      - '&7Current: &e{query}'
      - '&8Right-click to clear'
    PREV-PAGE:
      SLOT: 45
      MATERIAL: ARROW
      NAME: '&fPrevious Page'
      LORE:
      - '&7Page {page}'
    NEXT-PAGE:
      SLOT: 53
      MATERIAL: ARROW
      NAME: '&fNext Page'
      LORE:
      - '&7Page {page}'
    BACK:
      SLOT: -1
      MATERIAL: ARROW
      NAME: '&fBack'
      LORE:
# ... section continues, see the file on disk for the full block
```

</details>

---

Defaults above match the file shipped in the jar.
