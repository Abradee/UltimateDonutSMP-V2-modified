# `orders.yml`

The buy-orders board: the mirror image of the auction house, where players post what they
want to buy and at what price, and anyone holding that item can fill the order.

`MATCHING` and `DELIVERY` are the heart of it — how a held item is matched against an open
order, and how the goods and money change hands afterwards. `CATEGORY_FILTERS` draws its
category buttons from `filter.yml`. The `*_SIGN` sections configure the sign prompts. Search on the chest uses `SEARCH_SIGN`. Amount and price use `AMOUNT_SIGN` and `PRICE_SIGN` for Java players. Bedrock players on that chest type the amount and the price in chat, because a sign editor there closes and then no menu will open. `BEDROCK` sends native forms when Floodgate is installed. `NETWORK` syncs orders between servers over
Redis.

Comment at the top of the shipped file:

```text
UltimateDonutSMP2 - Player Item Orders Configuration
```

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/orders.yml` |
| **Commands** | `/orders` |
| **Player-facing text** | Edit `CONFIG.ORDERS` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`SETTINGS`](#section-settings) | section | 7 keys |
| [`PRICING`](#section-pricing) | section | 4 keys |
| [`DELIVERY`](#section-delivery) | section | 3 keys |
| [`MATCHING`](#section-matching) | section | 1 keys |
| [`CATEGORY_FILTERS`](#section-category-filters) | section | 9 keys |
| [`GUI`](#section-gui) | section | 9 keys |
| [`SORTING`](#section-sorting) | section | 2 keys |
| [`BEDROCK`](#section-bedrock) | section | 1 keys |
| [`NETWORK`](#section-network) | section | 2 keys |
| [`SEARCH_SIGN`](#section-search-sign) | section | 2 keys |
| [`AMOUNT_SIGN`](#section-amount-sign) | section | 2 keys |
| [`PRICE_SIGN`](#section-price-sign) | section | 2 keys |
| [`BOTS`](#section-bots) | section | 9 keys |

---

## Section: `SETTINGS`

General Order system settings

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.ENABLED` | `boolean` | `true`, `false` | `true` | Enable or disable the Orders system globally (true / false) |
| `SETTINGS.ORDER_DURATION_HOURS` | `integer` | Any integer | `168` | Duration in hours before an active order expires automatically |
| `SETTINGS.MAX_ACTIVE_ORDERS_DEFAULT` | `integer` | Any integer | `5` | Default maximum active orders a player can create simultaneously |
| `SETTINGS.MAX_QUANTITY_PER_ORDER` | `integer` | Any integer | `2304` | Maximum quantity of items requested per single order listing |
| `SETTINGS.CLICK_COOLDOWN_MS` | `integer` | Any integer | `750` | Anti-spam click cooldown between menu interactions (in milliseconds) |
| `SETTINGS.EXPIRE_CHECK_SECONDS` | `integer` | Any integer | `30` | Interval in seconds to check for expired orders |

### `SETTINGS.MAX_ACTIVE_ORDERS_BY_PERMISSION`

Maximum active order limits granted by permission node

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.MAX_ACTIVE_ORDERS_BY_PERMISSION.ultimatedonutsmp2.orders.limit.10` | `integer` | Any integer | `10` | Ultimatedonutsmp2.orders.limit.10. |
| `SETTINGS.MAX_ACTIVE_ORDERS_BY_PERMISSION.ultimatedonutsmp2.orders.limit.15` | `integer` | Any integer | `15` | Ultimatedonutsmp2.orders.limit.15. |

<details>
<summary>Default <code>SETTINGS</code> block as shipped</summary>

```yaml
# General Order system settings
SETTINGS:
  # Enable or disable the Orders system globally (true / false)
  ENABLED: true
  # Duration in hours before an active order expires automatically
  ORDER_DURATION_HOURS: 168
  # Default maximum active orders a player can create simultaneously
  MAX_ACTIVE_ORDERS_DEFAULT: 5
  # Maximum active order limits granted by permission node
  MAX_ACTIVE_ORDERS_BY_PERMISSION:
    ultimatedonutsmp2.orders.limit.10: 10
    ultimatedonutsmp2.orders.limit.15: 15
  # Maximum quantity of items requested per single order listing
  MAX_QUANTITY_PER_ORDER: 2304
  # Anti-spam click cooldown between menu interactions (in milliseconds)
  CLICK_COOLDOWN_MS: 750
  # Interval in seconds to check for expired orders
  EXPIRE_CHECK_SECONDS: 30
```

</details>

---

## Section: `PRICING`

Pricing, budget, and fee limits

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PRICING.MIN_PRICE_EACH` | `integer` | Any integer | `10` | Minimum price per individual item offered in an order |
| `PRICING.MAX_PRICE_EACH` | `integer` | Any integer | `1000000` | Maximum price per individual item offered in an order |
| `PRICING.MAX_TOTAL_BUDGET` | `integer` | Any integer | `250000000` | Maximum total money budget allowed for a single order listing |
| `PRICING.ORDER_CREATION_FEE` | `integer` | Any integer | `0` | Flat creation fee charged when listing a new item order |

<details>
<summary>Default <code>PRICING</code> block as shipped</summary>

```yaml
# Pricing, budget, and fee limits
PRICING:
  # Minimum price per individual item offered in an order
  MIN_PRICE_EACH: 10
  # Maximum price per individual item offered in an order
  MAX_PRICE_EACH: 1000000
  # Maximum total money budget allowed for a single order listing
  MAX_TOTAL_BUDGET: 250000000
  # Flat creation fee charged when listing a new item order
  ORDER_CREATION_FEE: 0
```

</details>

---

## Section: `DELIVERY`

Delivery system configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DELIVERY.MAX_DELIVER_PER_CLICK` | `integer` | Any integer | `64` | Maximum item quantity delivered in a single menu click |
| `DELIVERY.MODE` | `string` | Any text | `DEPOSIT_GUI` | Delivery interaction mode: DEPOSIT_GUI or DIRECT |
| `DELIVERY.MAX_DELIVER_PER_TRANSACTION` | `integer` | Any integer | `2304` | Maximum item quantity delivered per single transaction |

<details>
<summary>Default <code>DELIVERY</code> block as shipped</summary>

```yaml
# Delivery system configuration
DELIVERY:
  # Maximum item quantity delivered in a single menu click
  MAX_DELIVER_PER_CLICK: 64
  # Delivery interaction mode: DEPOSIT_GUI or DIRECT
  MODE: DEPOSIT_GUI
  # Maximum item quantity delivered per single transaction
  MAX_DELIVER_PER_TRANSACTION: 2304
```

</details>

---

## Section: `MATCHING`

Item matching and restriction filters

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MATCHING.BLOCKED_MATERIALS` | `list` | A list of values | _list of 9 items_ | Materials blocked from being requested via item orders. The Quick Buy blacklist in shop.yml does not apply here. |

<details>
<summary>Default contents of <code>MATCHING.BLOCKED_MATERIALS</code> (9 items)</summary>

```yaml
BLOCKED_MATERIALS:
  - 'BEDROCK'
  - 'BARRIER'
  - 'COMMAND_BLOCK'
  - 'CHAIN_COMMAND_BLOCK'
  - 'REPEATING_COMMAND_BLOCK'
  - 'STRUCTURE_BLOCK'
  - 'STRUCTURE_VOID'
  - 'JIGSAW'
  - 'LIGHT'
```

</details>

<details>
<summary>Default <code>MATCHING</code> block as shipped</summary>

```yaml
# Item matching and restriction filters
MATCHING:
  # Materials blocked from being requested via item orders. The Quick Buy blacklist in shop.yml does not apply here.
  BLOCKED_MATERIALS:
    - BEDROCK
    - BARRIER
    - COMMAND_BLOCK
    - CHAIN_COMMAND_BLOCK
    - REPEATING_COMMAND_BLOCK
    - STRUCTURE_BLOCK
    - STRUCTURE_VOID
    - JIGSAW
    - LIGHT
```

</details>

---

## Section: `CATEGORY_FILTERS`

Item categories shown in order creation & browser menus

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CATEGORY_FILTERS.ALL` | `list` | A list of values | _(empty list)_ | The all list. |
| `CATEGORY_FILTERS.BLOCKS` | `list` | A list of values | _list of 6 items_ | The blocks list. |
| `CATEGORY_FILTERS.TOOLS` | `list` | A list of values | _list of 4 items_ | The tools list. |
| `CATEGORY_FILTERS.FOOD` | `list` | A list of values | _list of 5 items_ | The food list. |
| `CATEGORY_FILTERS.COMBAT` | `list` | A list of values | _list of 4 items_ | The combat list. |
| `CATEGORY_FILTERS.POTIONS` | `list` | A list of values | _list of 3 items_ | The potions list. |
| `CATEGORY_FILTERS.BOOKS` | `list` | A list of values | _list of 3 items_ | The books list. |
| `CATEGORY_FILTERS.INGREDIENTS` | `list` | A list of values | _list of 5 items_ | The ingredients list. |
| `CATEGORY_FILTERS.UTILITIES` | `list` | A list of values | _list of 5 items_ | The utilities list. |

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.BLOCKS</code> (6 items)</summary>

```yaml
BLOCKS:
  - 'STONE'
  - 'COBBLESTONE'
  - 'DIRT'
  - 'OAK_LOG'
  - 'GLASS'
  - 'OBSIDIAN'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.TOOLS</code> (4 items)</summary>

```yaml
TOOLS:
  - 'IRON_PICKAXE'
  - 'DIAMOND_PICKAXE'
  - 'DIAMOND_AXE'
  - 'DIAMOND_SHOVEL'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.FOOD</code> (5 items)</summary>

```yaml
FOOD:
  - 'APPLE'
  - 'BREAD'
  - 'COOKED_BEEF'
  - 'COOKED_CHICKEN'
  - 'GOLDEN_CARROT'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.COMBAT</code> (4 items)</summary>

```yaml
COMBAT:
  - 'ARROW'
  - 'SHIELD'
  - 'DIAMOND_SWORD'
  - 'NETHERITE_SWORD'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.POTIONS</code> (3 items)</summary>

```yaml
POTIONS:
  - 'GLASS_BOTTLE'
  - 'FERMENTED_SPIDER_EYE'
  - 'BLAZE_POWDER'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.BOOKS</code> (3 items)</summary>

```yaml
BOOKS:
  - 'BOOK'
  - 'BOOKSHELF'
  - 'LECTERN'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.INGREDIENTS</code> (5 items)</summary>

```yaml
INGREDIENTS:
  - 'WHEAT'
  - 'SUGAR_CANE'
  - 'BLAZE_ROD'
  - 'ENDER_PEARL'
  - 'SLIME_BALL'
```

</details>

<details>
<summary>Default contents of <code>CATEGORY_FILTERS.UTILITIES</code> (5 items)</summary>

```yaml
UTILITIES:
  - 'HOPPER'
  - 'PISTON'
  - 'OBSERVER'
  - 'REDSTONE'
  - 'DISPENSER'
```

</details>

<details>
<summary>Default <code>CATEGORY_FILTERS</code> block as shipped</summary>

```yaml
# Item categories shown in order creation & browser menus
CATEGORY_FILTERS:
  ALL: []
  BLOCKS:
    - STONE
    - COBBLESTONE
    - DIRT
    - OAK_LOG
    - GLASS
    - OBSIDIAN
  TOOLS:
    - IRON_PICKAXE
    - DIAMOND_PICKAXE
    - DIAMOND_AXE
    - DIAMOND_SHOVEL
  FOOD:
    - APPLE
    - BREAD
    - COOKED_BEEF
    - COOKED_CHICKEN
    - GOLDEN_CARROT
  COMBAT:
    - ARROW
    - SHIELD
    - DIAMOND_SWORD
    - NETHERITE_SWORD
  POTIONS:
    - GLASS_BOTTLE
    - FERMENTED_SPIDER_EYE
    - BLAZE_POWDER
  BOOKS:
    - BOOK
    - BOOKSHELF
    - LECTERN
  INGREDIENTS:
    - WHEAT
    - SUGAR_CANE
    - BLAZE_ROD
    - ENDER_PEARL
    - SLIME_BALL
  UTILITIES:
    - HOPPER
    - PISTON
    - OBSERVER
    - REDSTONE
    - DISPENSER
```

</details>

---

## Section: `GUI`

GUI Inventory Titles and Sizes

### Entry schema (9 entries)

Each entry under `GUI` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `TITLE` |
| :--- | :--- |
| `MAIN` | `Orders (Page {page})` |
| `MY_ORDERS` | `Orders -&gt; Your Orders` |
| `NEW_ORDER` | `&8Orders -&gt; New Order` |
| `SELECT_ITEM` | `&8Orders -&gt; Select Item` |
| `EDIT_ORDER` | `Orders -&gt; Edit Order` |
| `CANCEL_ORDER` | `Orders -&gt; Cancel Order` |
| `DELIVER_CONFIRM` | `&8Orders -&gt; Deliver` |
| `COLLECT` | `&8Orders -&gt; Collect` |
| `DELIVERY_DEPOSIT` | `Orders -&gt; Deliver Items` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SIZE` | `integer` | Any integer | Required | Chest size. Must be a multiple of 9 between 9 and 54. |
| `TITLE` | `string` | Any text | Required | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `BUTTONS` | `section` | — | Optional (4/9) | Options for buttons, listed below. |
| `ITEMS_PER_PAGE` | `integer` | Any integer | Optional (4/9) | Items per page. |
| `DONUT_PLUS_SLOTS` | `integer` | Any integer | Optional (1/9) | Donut plus slots. |
| `PLUS_PAGE_SLOTS` | `integer` | Any integer | Optional (1/9) | Page 2 fills all five content rows with Donut++ locks (Design/Orders/Your Orders/Next Page). |
| `VISIBLE_SLOTS` | `integer` | Any integer | Optional (1/9) | Visible slots. |

#### `GUI.<entry>.BUTTONS`

#### `GUI.<entry>.BUTTONS` — one block per key

Keys under `GUI.<entry>.BUTTONS` are identifiers rather than fixed options; every one of them takes the same block described below. The shipped file defines 15:

`BACK`, `CANCEL`, `COLLECT`, `CONFIRM`, `DROP`, `FILTER`, `INFO`, `ITEM`, `LOCKED`, `MY_ORDERS`, `NEW`, `NEXT`, `PREV`, `SEARCH`, `SHARD_SHOP`

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SLOT` | `integer` | Any integer | Optional (15/16) | Inventory slot, `0` is the top-left cell. |
| `MATERIAL` | `string` | Any text | Optional (14/16) | Bukkit `Material` name for the icon. |

<details>
<summary>Default <code>GUI</code> block as shipped</summary>

```yaml
# GUI Inventory Titles and Sizes
GUI:
  MAIN:
    TITLE: 'Orders (Page {page})'
    SIZE: 54
    ITEMS_PER_PAGE: 45
    BUTTONS:
      PREV:
        SLOT: 45
        MATERIAL: ARROW
      FILTER:
        SLOT: 47
        MATERIAL: HOPPER
      SHARD_SHOP:
        SLOT: 48
        MATERIAL: AMETHYST_SHARD
      INFO:
        SLOT: 49
        MATERIAL: BOOK
      SEARCH:
        SLOT: 50
        MATERIAL: OAK_SIGN
      MY_ORDERS:
        SLOT: 51
        MATERIAL: CHEST
      NEXT:
        SLOT: 53
        MATERIAL: ARROW
  MY_ORDERS:
    TITLE: 'Orders -> Your Orders'
    SIZE: 54
    ITEMS_PER_PAGE: 45
    VISIBLE_SLOTS: 18
    DONUT_PLUS_SLOTS: 45
    # Page 2 fills all five content rows with Donut++ locks (Design/Orders/Your Orders/Next Page).
    PLUS_PAGE_SLOTS: 45
    BUTTONS:
      NEW:
        SLOT: 4
        MATERIAL: GRAY_STAINED_GLASS_PANE
      LOCKED:
        MATERIAL: RED_STAINED_GLASS_PANE
  NEW_ORDER:
    TITLE: '&8Orders -> New Order'
    SIZE: 27
  SELECT_ITEM:
    TITLE: '&8Orders -> Select Item'
    SIZE: 54
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `SORTING`

Order sorting options in GUI

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SORTING.DEFAULT` | `string` | Any text | `MOST_PAID` | Default sorting: MOST_PAID, MOST_MONEY_PER_ITEM, RECENTLY_LISTED |
| `SORTING.ALLOWED` | `list` | A list of values | _list of 3 items_ | The allowed list. |

<details>
<summary>Default contents of <code>SORTING.ALLOWED</code> (3 items)</summary>

```yaml
ALLOWED:
  - 'MOST_MONEY_PER_ITEM'
  - 'MOST_PAID'
  - 'RECENTLY_LISTED'
```

</details>

<details>
<summary>Default <code>SORTING</code> block as shipped</summary>

```yaml
# Order sorting options in GUI
SORTING:
  # Default sorting: MOST_PAID, MOST_MONEY_PER_ITEM, RECENTLY_LISTED
  DEFAULT: MOST_PAID
  ALLOWED:
    - MOST_MONEY_PER_ITEM
    - MOST_PAID
    - RECENTLY_LISTED
```

</details>

---

## Section: `BEDROCK`

Geyser/Floodgate Bedrock player compatibility

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BEDROCK.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `BEDROCK` section on or off. |

<details>
<summary>Default <code>BEDROCK</code> block as shipped</summary>

```yaml
# Geyser/Floodgate Bedrock player compatibility
BEDROCK:
  ENABLED: true
```

</details>

---

## Section: `NETWORK`

Cross-server Redis sync for orders

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `NETWORK.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `NETWORK` section on or off. |
| `NETWORK.REDIS_CHANNEL` | `string` | Any text | `ultimate-donut-smp:orders` | Redis channel. |

<details>
<summary>Default <code>NETWORK</code> block as shipped</summary>

```yaml
# Cross-server Redis sync for orders
NETWORK:
  ENABLED: true
  REDIS_CHANNEL: ultimate-donut-smp:orders
```

</details>

---

## Section: `SEARCH_SIGN`

Sign input prompts for search, quantity, and price

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SEARCH_SIGN.lines` | `list` | A list of values | _list of 4 items_ | The lines list. |
| `SEARCH_SIGN.input-line` | `integer` | Any integer | `0` | Input line. |

<details>
<summary>Default contents of <code>SEARCH_SIGN.lines</code> (4 items)</summary>

```yaml
lines:
  - ''
  - '↑↑↑↑↑'
  - 'Search'
  - ''
```

</details>

<details>
<summary>Default <code>SEARCH_SIGN</code> block as shipped</summary>

```yaml
# Sign input prompts for search, quantity, and price
SEARCH_SIGN:
  lines:
    - ""
    - "↑↑↑↑↑"
    - "Search"
    - ""
  input-line: 0
```

</details>

---

## Section: `AMOUNT_SIGN`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMOUNT_SIGN.lines` | `list` | A list of values | _list of 4 items_ | The lines list. |
| `AMOUNT_SIGN.input-line` | `integer` | Any integer | `0` | Input line. |

<details>
<summary>Default contents of <code>AMOUNT_SIGN.lines</code> (4 items)</summary>

```yaml
lines:
  - ''
  - '↑↑↑↑↑'
  - 'Amount'
  - ''
```

</details>

<details>
<summary>Default <code>AMOUNT_SIGN</code> block as shipped</summary>

```yaml
AMOUNT_SIGN:
  lines:
    - ""
    - "↑↑↑↑↑"
    - "Amount"
    - ""
  input-line: 0
```

</details>

---

## Section: `PRICE_SIGN`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PRICE_SIGN.lines` | `list` | A list of values | _list of 4 items_ | The lines list. |
| `PRICE_SIGN.input-line` | `integer` | Any integer | `0` | Input line. |

<details>
<summary>Default contents of <code>PRICE_SIGN.lines</code> (4 items)</summary>

```yaml
lines:
  - ''
  - '↑↑↑↑↑'
  - 'Price'
  - ''
```

</details>

<details>
<summary>Default <code>PRICE_SIGN</code> block as shipped</summary>

```yaml
PRICE_SIGN:
  lines:
    - ""
    - "↑↑↑↑↑"
    - "Price"
    - ""
  input-line: 0
```

</details>

---

## Section: `BOTS`

Automated Order Bot System Configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOTS.ENABLED` | `boolean` | `true`, `false` | `false` | Enable or disable automated bot item orders (true / false) |
| `BOTS.MIN_CHECK_INTERVAL_SECONDS` | `integer` | Any integer | `60` | Minimum interval in seconds between bot order checks |
| `BOTS.MAX_CHECK_INTERVAL_SECONDS` | `integer` | Any integer | `300` | Maximum interval in seconds between bot order checks |
| `BOTS.CHANCE` | `decimal` | Any decimal number | `0.5` | Chance (0.0 to 1.0) for a bot to post an order on each interval check |
| `BOTS.MAX_ACTIVE_BOT_ORDERS` | `integer` | Any integer | `10` | Maximum active bot orders allowed concurrently |
| `BOTS.MIN_DURATION_HOURS` | `integer` | Any integer | `24` | Minimum duration in hours for bot order listings |
| `BOTS.MAX_DURATION_HOURS` | `integer` | Any integer | `72` | Maximum duration in hours for bot order listings |
| `BOTS.BOT_NAMES` | `list` | A list of values | _list of 3 items_ | List of bot names displayed as order buyers |
| `BOTS.ITEMS` | `list` | A list of values | _list of 3 items_ | Items that bots can request in orders |

<details>
<summary>Default contents of <code>BOTS.BOT_NAMES</code> (3 items)</summary>

```yaml
BOT_NAMES:
  - 'OrderBot'
  - 'BuyerBot'
  - 'ItemCollector'
```

</details>

<details>
<summary>Default contents of <code>BOTS.ITEMS</code> (3 items)</summary>

```yaml
ITEMS:
  - {MATERIAL: DIAMOND, MAX_AMOUNT: 64, MAX_PRICE_EACH: 1000, MIN_AMOUNT: 16, MIN_PRICE_EACH: 500}
  - {MATERIAL: OAK_LOG, MAX_AMOUNT: 512, MAX_PRICE_EACH: 25, MIN_AMOUNT: 64, MIN_PRICE_EACH: 10}
  - {MATERIAL: NETHERITE_INGOT, MAX_AMOUNT: 4, MAX_PRICE_EACH: 50000, MIN_AMOUNT: 1, MIN_PRICE_EACH: 25000}
```

</details>

<details>
<summary>Default <code>BOTS</code> block as shipped</summary>

```yaml
# Automated Order Bot System Configuration
BOTS:
  # Enable or disable automated bot item orders (true / false)
  ENABLED: false
  # Minimum interval in seconds between bot order checks
  MIN_CHECK_INTERVAL_SECONDS: 60
  # Maximum interval in seconds between bot order checks
  MAX_CHECK_INTERVAL_SECONDS: 300
  # Chance (0.0 to 1.0) for a bot to post an order on each interval check
  CHANCE: 0.5
  # Maximum active bot orders allowed concurrently
  MAX_ACTIVE_BOT_ORDERS: 10
  # Minimum duration in hours for bot order listings
  MIN_DURATION_HOURS: 24
  # Maximum duration in hours for bot order listings
  MAX_DURATION_HOURS: 72
  # List of bot names displayed as order buyers
  BOT_NAMES:
    - "OrderBot"
    - "BuyerBot"
    - "ItemCollector"
  # Items that bots can request in orders
  ITEMS:
    - MATERIAL: DIAMOND
      MIN_AMOUNT: 16
      MAX_AMOUNT: 64
      MIN_PRICE_EACH: 500
      MAX_PRICE_EACH: 1000
    - MATERIAL: OAK_LOG
      MIN_AMOUNT: 64
      MAX_AMOUNT: 512
      MIN_PRICE_EACH: 10
      MAX_PRICE_EACH: 25
    - MATERIAL: NETHERITE_INGOT
      MIN_AMOUNT: 1
      MAX_AMOUNT: 4
      MIN_PRICE_EACH: 25000
      MAX_PRICE_EACH: 50000
```

</details>

---

Defaults above match the file shipped in the jar.
