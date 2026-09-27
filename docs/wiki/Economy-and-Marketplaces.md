# Economy & Marketplaces

Two currencies: **money** (sell / shop / AH / orders) and **shards** (shard shop only).
Balances live in the database, not a YAML file.

| File | Use it for |
| :--- | :--- |
| [worth.yml](Config-worth.yml) | What items sell for |
| [shop.yml](Config-shop.yml) | Quick Buy + shard shop |
| [auction-house.yml](Config-auction-house.yml) | `/ah` rules |
| [orders.yml](Config-orders.yml) | Buy-order board |
| [filter.yml](Config-filter.yml) | Browse categories (not chat) |
| [config.yml](Config-config.yml) | Starting money, shard zones |

---

## The two currencies

### Money and Vault

Money balances live in the plugin's own database. During startup the plugin registers
`VaultEconomyHook` as a Vault `Economy` provider at `ServicePriority.Highest`, so once Vault is
installed every other plugin that asks Vault for a balance is reading and writing this plugin's money.
The provider reports two fractional digits, formats amounts through the plugin's own currency
formatter, and declares no bank support.

Only money is published through Vault. Shards are internal: no Vault service exposes them, so an
external plugin cannot read or change a shard balance. If you need shard values elsewhere, use the
PlaceholderAPI expansions described in [Placeholders & Integrations](Placeholders-and-Integrations).

| Command | Purpose |
| :--- | :--- |
| `/balance [player]` (`/bal`, `/money`) | Check a money balance |
| `/pay <player> <amount>` | Transfer money to another player |
| `/addmoney <player> <amount>` | Add money (`ultimatedonutsmp2.admin.addmoney`) |
| `/removemoney <player> <amount>` | Remove money (`ultimatedonutsmp2.admin.removemoney`) |
| `/setmoney <player> <amount>` | Set a balance outright (`ultimatedonutsmp2.admin.setmoney`) |
| `/logs` | Browse a player's logged shop, auction and admin activity (`ultimatedonutsmp2.admin.logs`) |
| `/leaderboard [type]` (`/baltop`, `/top`) | Open the leaderboard menus, including balance |

New accounts start on `SETTINGS.MONEY-PER-DEFAULT` in `config.yml`.

### Shards

Shards are configured under `SHARDS` in `config.yml` and have four sources:

- **Shard zones.** Each entry under `SHARDS.CUBOIDS.REGIONS.<region>` binds a cuboid (or a radius
  around a point) that pays `AMOUNT` shards every `INTERVAL` seconds while a player stands in it. The
  bundled `spawn` region ships with `ENABLED: false`, so nothing pays out until you configure one. Each
  region has its own AFK handling (`AFK-TIME`, `TELEPORT-ON-AFK`), a movement requirement
  (`MIN-MOVEMENT-BLOCKS` within `RECENT-MOVEMENT-WINDOW`) and an `EXCLUDED-WORLDS` list. The
  `SHARDS.EVERY`/`AMOUNT`/`COUNTDOWN` keys above the regions are only a fallback for installs with no
  region defined at all.
- **Shards Everywhere.** `SHARDS.EVERYWHERE` pays shards anywhere on the server to players holding
  `ultimatedonutsmp2.shards.everywhere`, which is `false` by default so it behaves as a rank perk.
- **Player kills.** `SETTINGS.SHARDS-PER-KILL` pays on a kill, with
  `SETTINGS.SHARDS-KILL-COOLDOWN-SECONDS` blocking repeat rewards against the same victim so two
  players cannot trade kills for shards.
- **Crates.** A crate reward with a `SHARDS` grant type pays directly into the balance. See
  [Crates & Spawners](Crates-and-Spawners).

`SHARDS.BOOSTER-MULTIPLIER` multiplies zone and Everywhere payouts while a booster is running, and
`BOOSTER-APPLIES-TO-KILLS` decides whether kill rewards are boosted too.

| Command | Purpose |
| :--- | :--- |
| `/shards [player]` | Check a shard balance |
| `/shards everywhere <status\|debug> [player]` | Inspect Shards Everywhere eligibility (`ultimatedonutsmp2.admin.shards`) |
| `/shardpay <player> <amount>` | Transfer shards to another player |
| `/addshards`, `/removeshards`, `/setshards` | Administrative shard adjustments (`ultimatedonutsmp2.admin.shards`) |

`/shardpay` is disabled by default through `COMMANDS.SHARDPAY: false` in `config.yml`; enable it if
you want players trading shards between themselves.

---

## Shops

### `/shop` — Quick Buy

`/shop` opens the DonutSMP-style Quick Buy grid, configured under `QUICK-BUY` in `shop.yml`. Players
pin items into the grid themselves rather than browsing a fixed catalogue: an empty slot opens the
Choose Item screen, which by default lists every material the server version has
(`QUICK-BUY.CHOOSE-ITEM.MODE: VANILLA`, capped by `MAX-ITEMS` and narrowed by `BLACKLIST`). That
blacklist is Quick Buy only, so an entry there does not disappear from `/orders`. Setting `MODE` to
`CUSTOM` restricts the picker to items priced in `worth.yml`.

Bedrock clients freeze on that screen, because Geyser has to turn one dialog button per material
into a single form. Floodgate players get a paged list with a search box instead, 20 items a page,
under `QUICK-BUY.BEDROCK` in `shop.yml`. If Floodgate is not installed the same slot opens the chest
catalogue (45 items a page) and does not continue into the enchant dialog.

Prices are not written in `shop.yml`. `QUICK-BUY.PRICING` decides where each one comes from, in this
order:

1. With `USE-AUCTION-HOUSE: true`, the lowest active Auction House listing that matches the pinned
   item sets the price, and buying goes through the Auction House purchase path so the money reaches
   the player who listed it.
2. If no listing matches, the `worth.yml` price is used, multiplied by `WORTH-MULTIPLIER`.
3. If the item has no `worth.yml` entry either and `AUTO-BALANCE-MISSING` is on, a price is derived
   automatically and multiplied by `AUTO-BALANCE-MULTIPLIER`.

If a slot has no price from any of the three, it shows as out of stock instead of selling at zero.
This is what makes `/shop` a front end for the player economy rather than an infinite item source:
with `USE-AUCTION-HOUSE` on, most of what players buy is bought from each other.

`SHOP-GUI` holds the shared item lore (`SHOP-GUI.ITEM.LORE`, with `{shop_price}` and `{auction_line}`
placeholders), the favourites toggle, and `SHOP-GUI.WEB-SERVER`, which controls the embedded sell
analytics HTTP server. `/shop reload` reloads `shop.yml`, `menus.yml` and `sounds.yml` and requires
`ultimatedonutsmp2.admin.shop`.

`/shop legacy` opens the older category-based shop menu, which reads a `CATEGORIES` section from
`shop.yml`. That section is not shipped, so the legacy menu is empty until you add one.

### `/shardshop` — the premium shop

`/shardshop` is a separate menu with its own layout under `SHARD-MENU` in `shop.yml`, and everything
in it is paid for in shards (`CURRENCY: SHARD`). Each entry sets its own `SLOT`, `PRICE-PER-UNIT`,
`MATERIAL`, `ENCHANTMENTS`, `DISPLAY-NAME` and `LORE`, and quantity limits through `MIN-QUANTITY`,
`MAX-QUANTITY`, `DEFAULT-QUANTITY` and `HIDE-QUANTITY-BUTTONS`. An entry can hand out an ordinary item
(`GIVE-ITEM`), run a console command (`COMMAND`), or grant a timed amethyst tool through
`AMETHYST-TOOL` and `AMETHYST-DURATION` — the bundled shard pickaxes and axes use the last of those,
which is why their lore mentions a self-destruct timer.

---

## Selling and prices

Sell prices come from [worth.yml](Config-worth.yml), never from `shop.yml`. Prices are grouped by
category under `TYPE.<CATEGORY>.<MATERIAL>`, and `SETTINGS.WORTH-DEFAULT-VALUE` in `config.yml` covers
anything unpriced.

| Command | Purpose |
| :--- | :--- |
| `/sell` | Open the sell container; items placed inside are sold when it closes |
| `/sellhand [amount]` | Sell the item in the main hand |
| `/sellall` | Sell every sellable item in the inventory |
| `/sellmulti [category]`, `/sellmultiplier [category]` | Open the sell multiplier menu |
| `/sellprogress [category]` | Open the multiplier progress menu |
| `/sellhistory` | Review recent sales |
| `/worth [hand]` (`/prices`) | Open the price browser, or price the held item |
| `/topsell [gui\|items\|volume\|sellers\|export]` (`/sellstats`) | Admin sell analytics (`ultimatedonutsmp2.admin.sellstats`) |
| `/meta` (`/farmingmeta`) | Show the current farming meta item |

Selling is not a flat rate. Each sale accumulates progress in that item's sell category, and the
category's current multiplier is applied to the payout, which is what `/sellprogress` and
`/sellmulti` display. The same multiplier path is used when a spawner's stored loot is sold, so a
player cannot dodge progression by farming through a spawner. To disable multipliers server-wide
and pin every payout to base worth (`1.0x`), set `PROGRESS-MENU.ENABLED: false` in `menus.yml`. While
disabled, payouts never scale, sales pause progress recording, and `/sellmulti` informs players the
system is off.

`CONTAINER` in `worth.yml` decides how shulker boxes and other containers are priced:
`INCLUDE-CONTAINER-BASE-PRICE` adds the container itself on top of its contents, and
`ALLOW-NESTED-CONTAINERS` with `MAX-CONTAINER-DEPTH` limits how far the plugin looks inside. The
`BROWSER` section controls the `/worth` menu — its title, size, page length, default sort and the
`CATEGORY-SORT` order. `DISPLAY.FORMAT` and `WORTH-LORE` in `config.yml` control the worth line the
plugin draws on items.

### Farming meta rotation

`META` in `worth.yml` makes one item at a time worth more than its listed price. The item is taken in
turn from `META.ITEMS`, its price is multiplied by `META.MULTIPLIER`, and the rotation advances every
`META.INTERVAL_DAYS` plus `META.INTERVAL_HOURS`. `META.ENABLED` ships as `false`, so updating the
plugin never moves prices on its own. The boost applies wherever a price is shown or paid — `/worth`,
the item worth line, the sell menus and spawner loot sales — and the countdown is persisted to
`farming-meta-data.yml` so a restart does not reset it. `META.ANNOUNCE_ON_ROTATE` broadcasts each
change.

---

## The Auction House

`/auctionhouse` (`/ah`) is the player-to-player marketplace: a seller sets a fixed price and the first
buyer takes the item. The browser uses native Dialog API screens, so Java clients older than 1.21.6
cannot open it — see [Dialog API & older clients](Dialog-API-and-Older-Clients). It is configured in
[auction-house.yml](Config-auction-house.yml) and is the
price source Quick Buy reads from, so its health directly affects what `/shop` costs.

| Command | Purpose |
| :--- | :--- |
| `/ah` | Open the browser |
| `/ah search <item>` | Open the browser filtered to that item |
| `/ah sell <price>` | List the held item |
| `/ah my` | View your own listings |
| `/ah claims` | Collect sale proceeds and expired items |
| `/ah cancel` | Cancel a listing |
| `/ah limit` | Show your current listing limit |
| `/ah fastbuy`, `/ah fastsell` | Skip the confirmation steps (`ultimatedonutsmp2.auctionhouse.fastbuy` / `.fastsell`) |
| `/ah reload` | Reload auction settings (`ultimatedonutsmp2.admin.auctionhouse`) |

`/ah neth boot prot 4` is that search without the word `search`. Each word has to show up inside the item name, so `neth` matches netherite and `boot` matches boots. An enchant abbreviation followed by a number, such as `prot 4` or `prot iv`, keeps items with that enchant at that level or higher. `/ah search` uses the same rules.

The `ultimatedonutsmp2.auctionhouse.*` nodes gate individual actions and default to `true`; the
`donutauction.*` family mirrors them as legacy aliases.

**Listing limits are permission-tiered.** `SETTINGS.MAX_ACTIVE_LISTINGS_DEFAULT` is the baseline, and
`SETTINGS.MAX_ACTIVE_LISTINGS_BY_PERMISSION` maps explicit permission nodes to totals. On top of that,
any node of the form `ultimatedonutsmp2.auctionhouse.limit.<N>` grants `N` listings without needing a
config entry, so `ultimatedonutsmp2.auctionhouse.limit.25` is enough to give a rank 25 slots. Each
value is a total rather than a bonus, the highest value the player holds wins, and a negative value in
the config means unlimited. A wildcard permission does not grant a limit — the node has to be set on
the player or their group, otherwise `*` would silently hand everybody the largest tier.

The rest of the file covers `SETTINGS.LISTING_DURATION_HOURS` and `EXPIRE_CHECK_SECONDS`; `PRICING`
with `MIN_PRICE`, `MAX_PRICE`, an upfront `LISTING_FEE` and a `TAX_PERCENT` cut of each sale;
`RESTRICTIONS.BLOCKED_MATERIALS` and `RESTRICTIONS.BLOCKED_IF_HAS_LORE_CONTAINS` for keeping items off
the market; `CLAIMS`; `SORTING` with the default and allowed sort orders; and the `GUI` layouts.

### Bots

`BOTS` in `auction-house.yml` generates synthetic listings so a new or quiet server still has
something to buy. When `BOTS.ENABLED` is on, a background task posts listings under the names in
`BOT_NAMES`, drawn from the `BOTS.ITEMS` table where each entry defines a material, an amount range, a
price range and optional enchantments. `MIN_CHECK_INTERVAL_SECONDS`, `MAX_CHECK_INTERVAL_SECONDS` and
`CHANCE` control how often that happens, `MAX_ACTIVE_BOT_LISTINGS` caps how many exist at once, and
`MIN_DURATION_HOURS`/`MAX_DURATION_HOURS` set how long they last. It ships disabled, and it is worth
leaving disabled once real players are listing: Quick Buy prices follow the lowest active listing, so
bot listings set the price players pay.

---

## The Orders board

`/orders` is the mirror image of the Auction House. Instead of sellers advertising items, buyers post
what they want and how much they will pay per item, and anyone can deliver against a posted order.
It is configured in [orders.yml](Config-orders.yml).

| Command | Purpose |
| :--- | :--- |
| `/orders` | Open the board |
| `/orders my` | Manage your own orders |
| `/orders collect` | Collect items delivered to your orders |
| `/orders reload` | Reload order settings (`ultimatedonutsmp2.admin.orders`) |

`/orders collect` opens a chest. Drop page throws the item claims on the page you are looking at onto the ground at your feet. Drop all does that for every pending claim in the menu, including claims on later pages. Money refunds in that batch still go to your balance. On Bedrock, Drop Item Claims already drops the whole queue.

`SETTINGS` covers `ORDER_DURATION_HOURS`, `MAX_ACTIVE_ORDERS_DEFAULT`,
`MAX_ACTIVE_ORDERS_BY_PERMISSION` and `MAX_QUANTITY_PER_ORDER`. `PRICING` bounds what a buyer may
offer with `MIN_PRICE_EACH`, `MAX_PRICE_EACH` and `MAX_TOTAL_BUDGET`, plus an optional
`ORDER_CREATION_FEE`. `DELIVERY.MODE` chooses between `DEPOSIT_GUI`, where the supplier drops items
into a deposit window, and `DIRECT`; `MAX_DELIVER_PER_CLICK` and `MAX_DELIVER_PER_TRANSACTION` cap a
single delivery. `MATCHING.BLOCKED_MATERIALS` keeps materials off the board entirely. The Choose Item
blacklist in `shop.yml` does not; put an order block in `orders.yml` if that is what you want.

**Category buttons come from `filter.yml`.** The item catalogue behind the browse and order-creation
menus is built from the categories in [filter.yml](Config-filter.yml) — `Blocks`, `Tools`, `Food`,
`Combat`, `Potions`, `Books`, `Ingredients` and `Utilities` — which is the same list the rest of the
plugin uses for item categorisation. `CATEGORY_FILTERS` in `orders.yml` is only consulted if
`filter.yml` produces nothing usable, and a single hardcoded `BLOCKS` category is the last resort. If
you want to change what the category buttons show, edit `filter.yml`.

Bedrock players are handled separately. With `BEDROCK.ENABLED` on and Floodgate installed, the plugin
detects Bedrock clients and sends them native Bedrock forms instead of chest menus, because the sign
inputs the Java flow uses for search, amount and price prompts do not work on Bedrock. Those prompts
are configured in `SEARCH_SIGN`, `AMOUNT_SIGN` and `PRICE_SIGN`.

`NETWORK.ENABLED` with `NETWORK.REDIS_CHANNEL` publishes order events to other servers, which requires
Redis to be enabled in [database.yml](Config-database.yml). `BOTS` works the same way as the auction
bots, posting synthetic buy orders from `BOT_NAMES` and `BOTS.ITEMS`; it also ships disabled.

### The enchantment picker

[enchantments.yml](Config-enchantments.yml) defines a paged GUI that lets a player specify which
enchantments an item must have. It has **no command of its own**. It opens from the Orders flow: when a
player selects an enchantable material for a new order, the picker appears so they can say which
enchantments and levels they want delivered, and then the normal order menu follows. The file's `gui`
section sets the title, row count and the slots for the item, cancel, confirm and page buttons, and
each per-material section maps an enchantment and level to a slot and page.

---

## Billford

`/billford` is declared in `plugin.yml` and holds the permission
`ultimatedonutsmp2.command.billford`, but **the feature does not exist in this build**. No Java class
registers or handles it, there is no `billford.yml` anywhere in the plugin, and nothing reads a
Billford config key. What remains is dead weight from a removed or planned feature: an unused
`BILLFORD_REWARD` enum constant, orphaned `BILLFORD` sound keys in `sounds.yml`, and stale
`MENUS.BILLFORD-MENU` and `MESSAGES.BILLFORD` strings in the language files. Running the command does
nothing, and any older documentation describing rotating Billford NPC trades is describing a feature
that is not present. Either remove the command from `plugin.yml` or leave it as a no-op; there is
nothing to configure.

---

## Configuration files

| File | What it drives |
| :--- | :--- |
| [config.yml](Config-config.yml) | `SETTINGS` economy defaults, `SHARDS`, `WORTH-LORE`, `MONEY-NAMETAGS`, feature toggles |
| [shop.yml](Config-shop.yml) | `QUICK-BUY`, `SHARD-MENU`, `SHOP-GUI`, the listing and transaction menus |
| [worth.yml](Config-worth.yml) | Sell prices, the `/worth` browser, container pricing, farming meta |
| [auction-house.yml](Config-auction-house.yml) | Listing limits, pricing and tax, restrictions, sorting, bots |
| [orders.yml](Config-orders.yml) | Order limits, pricing, delivery mode, Bedrock forms, Redis sync, bots |
| [filter.yml](Config-filter.yml) | The item categories the Orders catalogue is built from |
| [enchantments.yml](Config-enchantments.yml) | The enchantment picker opened from Orders |
| [menus.yml](Config-menus.yml) | Shared menu titles and lore for economy menus |
| [sounds.yml](Config-sounds.yml) | Shop, auction and order sound effects |
| [database.yml](Config-database.yml) | Where balances, listings, orders and sell history are stored |

For the full command and permission inventory see [Commands & Permissions](Commands-and-Permissions).
