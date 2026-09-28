# Crates & Spawners

Keys and crates, stacked spawners that store drops, timed Amethyst Tools.
The enchantment picker has **no command** — it opens from Orders.

[crates.yml](Config-crates.yml) · [spawners.yml](Config-spawners.yml) ·
[amethyst-tools.yml](Config-amethyst-tools.yml) · [enchantments.yml](Config-enchantments.yml) ·
[Commands](Commands-and-Permissions)

---

## Crates

Players open `/crates` to preview and open crates they have keys for, and `/keys` to see their
balances. Administrators use `/crate`. All three share `CrateCommand`. The feature toggle is
`FEATURES.CRATES` (legacy `COMMANDS.CRATE`).

| Command | Permission | What it does |
| :--- | :--- | :--- |
| `/crates` | `ultimatedonutsmp2.command.crates` | Open the crates menu |
| `/keys` | `ultimatedonutsmp2.command.keys` (falls back to `.command.crates`) | Open the keys menu |
| `/crate open <id>` | Command gate | Open a crate you hold keys for |
| `/crate keys [player]` | `.admin.crate` to view others | Show key balances |
| `/crate info` | Command gate | Show crate information |
| `/crate create <id>` | `ultimatedonutsmp2.admin.crate` | Create a crate |
| `/crate delete <id>` | `.admin.crate` | Delete a crate |
| `/crate type <id> <type>` | `.admin.crate` | Set the opening type (`gacha`, `choose_one` or `choose-one`) |
| `/crate key <player> <id> <amount>` | `.admin.crate` | Add keys |
| `/crate take <player> <id> <amount>` | `.admin.crate` | Remove keys |
| `/crate set <player> <id> <amount>` | `.admin.crate` | Set a key balance |
| `/crate keyall <id> <amount>` | `ultimatedonutsmp2.admin.crate.keyall` | Give keys to everyone online |
| `/crate add <id>` | `.admin.crate` | Open the GUI editor |
| `/crate add <id> <slot>` | `.admin.crate` | Add the held item as a reward |
| `/crate add <id> <slot> command <cmd…>` | `.admin.crate` | Add a console-command reward |
| `/crate add <id> <slot> money <amount>` | `.admin.crate` | Add a money reward |
| `/crate add <id> <slot> shards <amount>` | `.admin.crate` | Add a shard reward |
| `/crate edit <id> [slot]` | `.admin.crate` | Edit rewards (GUI if no slot) |
| `/crate remove <id> <slot>` | `.admin.crate` | Remove a reward |
| `/crate bind <id>` | `.admin.crate` | Bind the crate to the chest you are looking at |
| `/crate unbind [id]` | `.admin.crate` | Remove a chest binding |
| `/crate listbound` | `.admin.crate` | List every bound chest |
| `/crate reload` | `ultimatedonutsmp2.admin.crate.reload` | Reload `crates.yml` |

`listbound` exists in Java and is not listed in the `plugin.yml` usage string.

Rewards are **weighted**, not percentages. A reward's chance is its weight divided by the total
weight of that crate's pool, so adding a reward dilutes everything else unless you rebalance.

The scheduled Key-All broadcast is a separate task (`KeyAllManager`) configured under `KEY-ALL` in
[config.yml](Config-config.yml). `/crate keyall` is the manual version of the same idea.

### GUI shorthand for non-item rewards

In the crate editor, naming a placeholder item with one of these prefixes creates the matching
reward type when you place it in a slot:

| Item name starts with | Creates |
| :--- | :--- |
| `[CMD] <console command…>` | A `COMMAND` reward |
| `[MONEY] <amount>` | A `MONEY` reward |
| `[SHARDS] <amount>` | A `SHARDS` reward |

CLI and GUI writes both save `crates.yml` and reload crate data. The `CRATES` subtree is treated as
yours — config merging never rewrites crates you have defined.

---

## Spawners

Managed spawners accumulate drops on a timer and hand them over when collected. They do not keep
hundreds of entities loaded. Stacking is placing a matching spawner onto an existing one.

`/spawner` is gated by `ultimatedonutsmp2.command.spawner` (default true). Destructive and inspect
actions re-check `ultimatedonutsmp2.admin.spawner`.

| Subcommand | Who | Effect |
| :--- | :--- | :--- |
| `/spawner info` | Owner / team / public, or admin | Inspect the targeted spawner |
| `/spawner split <amount>` | Player holding a stacked item | Split the stack in hand |
| `/spawner` or `/spawner panel` | Admin | Open the admin panel |
| `/spawner give <player> <type> [amount]` | Admin | Give a managed spawner item |
| `/spawner remove` / `forcebreak` | Admin | Remove the targeted block |
| `/spawner reload` | Admin | Reload [spawners.yml](Config-spawners.yml) |

Breaking a managed spawner puts it in your inventory. Without crouching, that is one spawner.
Crouching takes a stack of up to 64, and anything past that stays on the block.
`SETTINGS.DROP_ON_BREAK_IF_INVENTORY_FULL` (default `true` in [spawners.yml](Config-spawners.yml))
drops only what does not fit.

`SETTINGS.REQUIRE_SILK_TOUCH` defaults to `true`. `ultimatedonutsmp2.spawner.bypass` (default
`false`) waives it; operators are not exempt. Creative breaks delete the spawner instead of
returning the item.

Placed spawners are stored in the database. The offence `wipe` flag deliberately leaves them in
place when a player is banned. See [offenses.yml](Config-offenses.yml).

---

## Amethyst Tools

Timed premium tools sold from the Shard Shop or given with `/amethysttool give <player> <type>
[duration]`. `AmethystToolsTask` removes the item when `DURATION` runs out.

| Type | What it does |
| :--- | :--- |
| `DRILL` | Breaks a 3×3 face per strike |
| `CHOPPER` | Fells a whole tree, up to `MAX-LOGS` |
| `SELL_AXE` | Sells a chest's contents at worth prices |
| `SHOVEL` | Area dig |
| `BUCKET` | Drains nearby water |
| `SHARD-BOOSTER` | Multiplies shard income for its duration |
| `HASTE-POTION` | Timed haste |

`/amethysttool reload` reloads [amethyst-tools.yml](Config-amethyst-tools.yml). A Shard Shop entry
in [shop.yml](Config-shop.yml) can set `AMETHYST-TOOL` and `AMETHYST-DURATION` instead of giving a
plain item.

---

## Enchantment picker

[enchantments.yml](Config-enchantments.yml) backs the picker that opens from **Orders** when a
listed item accepts enchantments. There is no standalone command. Each item type (`helmet`,
`sword`, `pickaxe`, …) is a page of `name;level` entries mapped to slots.
