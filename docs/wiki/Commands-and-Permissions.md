# Commands & Permissions

143 commands. Almost all need `ultimatedonutsmp2.command.<name>` just to run. Changing data
usually needs a second node (`ultimatedonutsmp2.admin.*` or `ultimatedonutsmp2.staff.*`).
You can also turn a whole feature off in [config.yml](Config-config.yml) — that is not a
permission.

**On this page:** [how permissions work](#how-permissions-are-layered) ·
[feature toggles](#feature-toggles-and-disabled-commands) ·
[economy](#economy--currency) · [shop](#shop--selling) ·
[auction & orders](#auction-house--orders) · [teleport](#teleportation--homes) ·
[crates](#crates--spawners) · [PvP](#pvp-duels-ffa-and-ranked) ·
[social](#social--chat) · [settings](#player-settings--info) ·
[staff](#staff--moderation) · [punishments](#punishments) ·
[network](#network--maintenance) · [admin](#admin--utility) ·
[numbered tiers](#numbered-permission-tiers) ·
[full list](#permission-reference) ·
[quirks](#known-inconsistencies)

## How permissions are layered

There are four layers, applied in this order:

1. **Command gate.** The `permission:` field of the command in `plugin.yml`. Failure produces the command's `permission-message:` and the executor is never called. Almost all of these are `ultimatedonutsmp2.command.<name>`; the exceptions are the punishment commands, `/offend` and `/god`, which are gated by `ultimatedonutsmp2.staff.*` nodes directly.
2. **Feature gate.** The command's `FEATURES.<KEY>.ENABLED` toggle in `config.yml`. This is not a permission; see [Feature toggles and disabled commands](#feature-toggles-and-disabled-commands).
3. **Action check.** A runtime `hasPermission` check inside the handler, usually `ultimatedonutsmp2.admin.<feature>` for destructive or configuration actions, `ultimatedonutsmp2.staff.<feature>` for moderation actions, or a bare node such as `ultimatedonutsmp2.message` for ordinary player behaviour.
4. **Tier resolution.** For quotas and cooldowns, the plugin scans a numbered permission family such as `ultimatedonutsmp2.homes.<N>` and derives a value. See [Numbered permission tiers](#numbered-permission-tiers).

Parent and wildcard nodes worth knowing:

| Node | Default | Role |
|------|---------|------|
| `ultimatedonutsmp2.command.*` | op | Wildcard granting every declared `ultimatedonutsmp2.command.<name>` child. |
| `ultimatedonutsmp2.admin` | op | Parent granting `ultimatedonutsmp2.command.*` plus every `ultimatedonutsmp2.admin.*` child. Also short-circuits the `/topsell` check and enables the update-check notice on join. |
| `ultimatedonutsmp2.staff.mode` | op | Parent granting staff mode plus 27 further staff utility children, including `ultimatedonutsmp2.chat.color`. |
| `ultimatedonutsmp2.staff.punishments.create` | op | Parent granting `punishments.ban`, `punishments.blacklist`, `punishments.mute` and `staff.vcmute`. |
| `ultimatedonutsmp2.staff.punishments.remove` | op | Parent granting `punishments.unban`, `punishments.unblacklist`, `punishments.unmute` and `staff.vcunmute`. |
| `ultimatedonutsmp2.staff.alerts.receive` | op | Parent granting `staff.helpop.receive` and `staff.report.receive`. |
| `ultimatedonutsmp2.command.ban` and siblings | op | Each punishment command node is itself a parent that grants the matching `ultimatedonutsmp2.staff.punishments.*` child, which is declared with default `false`. |

Related pages: [Configuration Reference](Configuration-Reference), [Staff & Security](Staff-and-Security), [Economy & Marketplaces](Economy-and-Marketplaces), [config.yml](Config-config.yml).

## Feature toggles and disabled commands

Every command belongs to at most one feature group. A group is disabled by setting `FEATURES.<KEY>.ENABLED` to `false` in `config.yml`; if that path is absent the plugin falls back to the legacy `COMMANDS.<KEY>` boolean, and if neither path exists the feature is treated as enabled. `FEATURES.<KEY>.ENABLED` can also be toggled in-game with `/ultimatedonutsmp2 features <enable|disable|toggle> <key>` or through the feature GUI.

`FEATURES_SETTINGS.DISABLED_COMMAND_ACTION` controls what a disabled command does. It accepts three values:

| Value | Aliases accepted | Behaviour |
|-------|------------------|-----------|
| `MESSAGE` | default when unset or unrecognised | The command stays registered and replies with the `FEATURES.DISABLED` message. |
| `UNKNOWN` | `HIDE` | The command stays registered but behaves as though it does not exist. |
| `UNREGISTER` | `DISABLE`, `OFF`, `REMOVE` | The command is removed from the command map, so clients never see it. |

Tab completion is suppressed for any command whose feature group is disabled, regardless of the action setting. Feature keys include `HOMES`, `SELL`, `SHOP`, `CRATES`, `DUELS`, `FFA`, `PVP_ARENA`, `AUCTION_HOUSE`, `ORDERS`, `PUNISHMENTS`, `STAFF_MODE`, `SPAWN_STASH`, `HIDE`, `FRIENDS`, `SAFETY`, `MAINTENANCE` and roughly forty more; see [Configuration Reference](Configuration-Reference) for the complete list.

## Economy & Currency

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/balance` | `/bal`, `/money` | `/balance [player]` | `ultimatedonutsmp2.command.balance` | Show your own money balance, or another player's. |
| `/pay` | — | `/pay <player> <amount>` | `ultimatedonutsmp2.command.pay` | Transfer money to another player. |
| `/addmoney` | — | `/addmoney <player> <amount>` | `ultimatedonutsmp2.command.addmoney`; action check `ultimatedonutsmp2.admin.addmoney` | Add money to a player's balance. |
| `/removemoney` | — | `/removemoney <player> <amount>` | `ultimatedonutsmp2.command.removemoney`; action check `ultimatedonutsmp2.admin.removemoney` | Remove money from a player's balance. |
| `/setmoney` | — | `/setmoney <player> <amount>` | `ultimatedonutsmp2.command.setmoney`; action check `ultimatedonutsmp2.admin.setmoney` | Set a player's balance to an exact value. |
| `/shards` | — | `/shards [player]` or `/shards everywhere <status\|debug> [player]` | `ultimatedonutsmp2.command.shards`; `everywhere` requires `ultimatedonutsmp2.admin.shards` | Check shard balances, or inspect Shards Everywhere eligibility. |
| `/shardpay` | — | `/shardpay <player> <amount>` | `ultimatedonutsmp2.command.shardpay` | Transfer shards to another player. |
| `/addshards` | — | `/addshards <player> <amount>` | `ultimatedonutsmp2.command.addshards`; action check `ultimatedonutsmp2.admin.shards` | Add shards to a player's balance. |
| `/removeshards` | — | `/removeshards <player> <amount>` | `ultimatedonutsmp2.command.removeshards`; action check `ultimatedonutsmp2.admin.shards` | Remove shards from a player's balance. |
| `/setshards` | — | `/setshards <player> <amount>` | `ultimatedonutsmp2.command.setshards`; action check `ultimatedonutsmp2.admin.shards` | Set a player's shard balance to an exact value. |
| `/bounty` | — | `/bounty <list\|add\|set\|info> [player] [amount]` | `ultimatedonutsmp2.command.bounty` | Place, inspect and list bounties. With no arguments, behaves as `list`. |

`/addshards`, `/removeshards` and `/setshards` are all handled by the same executor and share the single action node `ultimatedonutsmp2.admin.shards`.

## Shop & Selling

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/shop` | — | `/shop [reload\|legacy]` | `ultimatedonutsmp2.command.shop`; `reload` requires `ultimatedonutsmp2.admin.shop` | Open the shop menu. `legacy` opens the pre-dialog chest interface. |
| `/shardshop` | — | `/shardshop [reload\|legacy]` | `ultimatedonutsmp2.command.shardshop`; `reload` requires `ultimatedonutsmp2.admin.shop` | Open the shard-currency shop. Shares the executor with `/shop`. |
| `/sell` | — | `/sell` | `ultimatedonutsmp2.command.sell` | Open the sell menu. |
| `/sellall` | — | `/sellall` | `ultimatedonutsmp2.command.sellall` | Open the confirmation menu to sell every sellable item in your inventory. |
| `/sellhand` | — | `/sellhand [amount]` | `ultimatedonutsmp2.command.sellhand` | Sell the item held in your main hand. Any `[amount]` argument is ignored at runtime and the command sells the entire held stack; see [Known inconsistencies](#known-inconsistencies). |
| `/sellhistory` | — | `/sellhistory` | `ultimatedonutsmp2.command.sellhistory` | View your recent sell transactions. |
| `/sellmulti` | — | `/sellmulti [category]` | `ultimatedonutsmp2.command.sellmulti` | Open the sell multiplier menu. |
| `/sellmultiplier` | — | `/sellmultiplier [category]` | `ultimatedonutsmp2.command.sellmulti` | Identical to `/sellmulti`; note that it reuses the `sellmulti` node rather than declaring its own. |
| `/sellprogress` | — | `/sellprogress [category]` | `ultimatedonutsmp2.command.sellprogress` | Open the sell multiplier progress menu. |
| `/topsell` | `/sellstats` | `/topsell [gui\|items\|volume\|sellers\|export\|web\|html\|reset]` | `ultimatedonutsmp2.command.topsell`; action check `ultimatedonutsmp2.admin.sellstats` or `ultimatedonutsmp2.admin` | Admin sell statistics and economy metrics. |
| `/worth` | `/prices` | `/worth [browse\|hand\|reload\|<amount> <item>]` | `ultimatedonutsmp2.command.worth`; `reload` requires `ultimatedonutsmp2.admin.worth` | Open the worth browser or price a specific item. |
| `/meta` | `/farmingmeta` | `/meta` | `ultimatedonutsmp2.command.meta` | Show the item that is currently the farming meta. |
| `/billford` | — | `/billford` | `ultimatedonutsmp2.command.billford` | Declared in `plugin.yml` as "Open Billford trade" but has no Java executor; see [Known inconsistencies](#known-inconsistencies). |

The five sell commands (`/sell`, `/sellall`, `/sellhand`, `/sellhistory`, `/sellmulti` and its aliases) are all served by one executor, which also accepts `admin`, `stats` and `top` as a first argument to redirect into the sell statistics view.

### `/topsell` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| *(none)*, `gui`, `menu` | — | `ultimatedonutsmp2.admin.sellstats` | Open the sell statistics GUI. |
| `items`, `revenue`, `topitems` | `[limit]` | `ultimatedonutsmp2.admin.sellstats` | Rank items by revenue. |
| `volume`, `topvolume` | `[limit]` | `ultimatedonutsmp2.admin.sellstats` | Rank items by quantity sold. |
| `sellers`, `topsellers`, `players` | `[limit]` | `ultimatedonutsmp2.admin.sellstats` | Rank players by sell revenue. |
| `export`, `report` | — | `ultimatedonutsmp2.admin.sellstats` | Write a statistics report to disk. |
| `web`, `site`, `url`, `paste` | — | `ultimatedonutsmp2.admin.sellstats` | Upload the report to a web paste service. |
| `html` | — | `ultimatedonutsmp2.admin.sellstats` | Export the report as HTML. |
| `reset`, `wipe`, `clear` | `confirm` | `ultimatedonutsmp2.admin.sellstats` | Clear stored sell statistics. Requires `confirm` as the second argument. |

### `/worth` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| *(none)*, `browse`, `prices` | — | Command gate only | Open the worth browser. The `/prices` alias always opens the browser. |
| `hand`, `held`, `item`, `check` | — | Command gate only | Price the item in your main hand. |
| `reload` | — | `ultimatedonutsmp2.admin.worth` | Reload `worth.yml`, the worth manager and the farming meta. |
| *(numeric)* | `<amount> <item>` | Command gate only | Price an arbitrary quantity of a named material. |

## Auction House & Orders

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/auctionhouse` | `/ah` | `/auctionhouse [sell\|my\|claims\|cancel\|search\|limit\|fastbuy\|fastsell\|reload]` | `ultimatedonutsmp2.command.auctionhouse`; per-action `ultimatedonutsmp2.auctionhouse.<action>` or `donutauction.<action>`; `reload` requires `ultimatedonutsmp2.admin.auctionhouse` | Open and operate the Auction House. |
| `/orders` | — | `/orders [my\|collect\|reload]` | `ultimatedonutsmp2.command.orders`; `reload` requires `ultimatedonutsmp2.admin.orders` | Open the Orders board, view your own orders, or collect fulfilled orders. |

### `/auctionhouse` subcommands

Each action is checked against `ultimatedonutsmp2.auctionhouse.<action>` **or** the legacy alias `donutauction.<action>`; holding `ultimatedonutsmp2.admin.auctionhouse` satisfies any of them.

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| *(none)* | — | `ultimatedonutsmp2.auctionhouse.use` / `donutauction.use` | Open the browse menu. |
| `search` | `<item>` | `ultimatedonutsmp2.auctionhouse.use` / `donutauction.use` | Open the browse menu already filtered to that item. Same 64 character cap as the sign search. |
| `sell` | `<price>` | `ultimatedonutsmp2.auctionhouse.sell` / `donutauction.sell` | List the held item. Subject to the listing limit tier. |
| `my` | — | `ultimatedonutsmp2.auctionhouse.my` / `donutauction.my` | View your active listings. |
| `claims` | — | `ultimatedonutsmp2.auctionhouse.claims` / `donutauction.claims` | View and collect sale proceeds and expired items. |
| `cancel` | — | `ultimatedonutsmp2.auctionhouse.cancel` / `donutauction.cancel` | Cancel one of your own listings. |
| `limit` | — | `ultimatedonutsmp2.auctionhouse.limit` / `donutauction.limit` | Report your current listing limit. Not listed in the `plugin.yml` usage string. |
| `fastbuy` | — | `ultimatedonutsmp2.auctionhouse.fastbuy` / `donutauction.fastbuy` | Toggle the confirmation-free buying preference. Not declared in `plugin.yml`. |
| `fastsell` | — | `ultimatedonutsmp2.auctionhouse.fastsell` / `donutauction.fastsell` | Toggle the confirmation-free selling preference. Not declared in `plugin.yml`. |
| `reload` | — | `ultimatedonutsmp2.admin.auctionhouse` | Reload Auction House settings. |

Words that are not one of those subcommands are a search as well. `/ah neth boot prot 4` opens the browser on netherite boots with Protection IV or higher, and `/ah search neth boot prot 4` does the same thing. `prot4` and `prot iv` count as Protection IV.

Buying from the browse menu is checked against `ultimatedonutsmp2.auctionhouse.buy` / `donutauction.buy`.

## Teleportation & Homes

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/home` | — | `/home [name]` | `ultimatedonutsmp2.command.home` | Teleport to one of your homes. |
| `/homes` | — | `/homes` | `ultimatedonutsmp2.command.homes` | Open the homes dialog. |
| `/sethome` | — | `/sethome [name]` | `ultimatedonutsmp2.command.sethome` | Create a home at your position, subject to your home slot tier. |
| `/delhome` | — | `/delhome <name>` | `ultimatedonutsmp2.command.delhome` | Delete one of your homes. |
| `/renamehome` | — | `/renamehome <old> <new>` | `ultimatedonutsmp2.command.renamehome` | Rename one of your homes. |
| `/spawn` | — | `/spawn [set\|setspawn\|setup]` | `ultimatedonutsmp2.command.spawn`; the sub-arguments require `ultimatedonutsmp2.command.setspawn` or `ultimatedonutsmp2.admin.setup` | Teleport to spawn, or set the spawn location. |
| `/setspawn` | — | `/setspawn` | `ultimatedonutsmp2.command.setspawn`; also accepts `ultimatedonutsmp2.admin.setup` | Set the server spawn location. |
| `/afk` | — | `/afk [set\|setafk\|setup]` | `ultimatedonutsmp2.command.afk`; the sub-arguments require `ultimatedonutsmp2.command.setafk` or `ultimatedonutsmp2.admin.setup` | Go to the AFK zone, or set its location. |
| `/setafk` | — | `/setafk` | `ultimatedonutsmp2.command.setafk`; also accepts `ultimatedonutsmp2.admin.setup` | Set the AFK zone location. |
| `/rtp` | — | `/rtp [world]` | `ultimatedonutsmp2.command.rtp` | Random teleport, with cooldown resolved from the RTP cooldown tier. |
| `/rtpq` | `/rtpqueue` | `/rtpq [join\|leave]` | `ultimatedonutsmp2.command.rtpq` | Join or leave the RTP matchmaking queue. Queue position uses the RTP priority tier. |
| `/warp` | — | `/warp [name]` | `ultimatedonutsmp2.command.warp` | Teleport to a public warp, or open the warp list. |
| `/warpmanager` | — | `/warpmanager <create\|delete\|list> [name]` | `ultimatedonutsmp2.command.warpmanager`; action checks `ultimatedonutsmp2.admin.warpmanager`, `.setwarp`, `.delwarp` | Manage public warps. |
| `/setwarp` | — | `/setwarp <name>` | `ultimatedonutsmp2.command.setwarp`; action check `ultimatedonutsmp2.admin.setwarp` | Create a warp at your position. Shares the executor with `/warpmanager`. |
| `/delwarp` | — | `/delwarp <name>` | `ultimatedonutsmp2.command.delwarp`; action check `ultimatedonutsmp2.admin.delwarp` | Delete a warp. Shares the executor with `/warpmanager`. |
| `/portalmanager` | — `/portalmanager <list\|info\|create\|delete\|setcuboid\|setdestination\|setdisplay\|toggle\|setpriority\|sethologramhere>` | see Usage | `ultimatedonutsmp2.command.portalmanager`; action check `ultimatedonutsmp2.admin.portalmanager` | Manage RTP portal triggers. |
| `/tpa` | — | `/tpa <player>` | `ultimatedonutsmp2.command.tpa` | Request to teleport to a player. |
| `/tpahere` | — | `/tpahere <player>` | `ultimatedonutsmp2.command.tpahere` | Request that a player teleports to you. |
| `/tpaccept` | — | `/tpaccept [player]` | `ultimatedonutsmp2.command.tpaccept` | Accept a pending teleport request. |
| `/tpadeny` | — | `/tpadeny [player]` | `ultimatedonutsmp2.command.tpadeny` | Deny a pending teleport request. |
| `/tpacancel` | — | `/tpacancel` | `ultimatedonutsmp2.command.tpacancel` | Cancel your outgoing teleport requests. |
| `/tpauto` | — | `/tpauto` | `ultimatedonutsmp2.command.tpauto` | Toggle automatic acceptance of `/tpa` requests. |
| `/tpahereauto` | — | `/tpahereauto` | `ultimatedonutsmp2.command.tpahereauto` | Toggle automatic acceptance of `/tpahere` requests. |

`/tpa`, `/tpahere`, `/tpaccept`, `/tpadeny` and `/tpacancel` are all served by a single executor that dispatches on the command label.

### `/portalmanager` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `list` | — | `ultimatedonutsmp2.admin.portalmanager` | List configured portal triggers. |
| `info` | `<portal>` | `ultimatedonutsmp2.admin.portalmanager` | Show one portal's configuration. |
| `create` | `<portal>` | `ultimatedonutsmp2.admin.portalmanager` | Create a portal trigger. |
| `delete` | `<portal>` | `ultimatedonutsmp2.admin.portalmanager` | Delete a portal trigger. |
| `setcuboid` | `<portal> <cuboid>` | `ultimatedonutsmp2.admin.portalmanager` | Bind the trigger to a cuboid region. |
| `setdestination` | `<portal> <destination>` | `ultimatedonutsmp2.admin.portalmanager` | Set where the portal sends players. |
| `setdisplay` | `<portal> <display...>` | `ultimatedonutsmp2.admin.portalmanager` | Set the portal's display name. |
| `toggle` | `<portal>` | `ultimatedonutsmp2.admin.portalmanager` | Enable or disable the trigger. |
| `setpriority` | `<portal> <number>` | `ultimatedonutsmp2.admin.portalmanager` | Set evaluation priority when regions overlap. |
| `sethologramhere` | `<portal>` | `ultimatedonutsmp2.admin.portalmanager` | Move the portal hologram to your position. |

### `/warpmanager` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `create` | `<name>` | `ultimatedonutsmp2.admin.setwarp` or `ultimatedonutsmp2.admin.warpmanager` | Create a warp at your position. |
| `delete` | `<name>` | `ultimatedonutsmp2.admin.delwarp` or `ultimatedonutsmp2.admin.warpmanager` | Delete a warp. |
| `list` | — | `ultimatedonutsmp2.admin.warpmanager` | List all warps. |

## Crates & Spawners

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/crate` | — | `/crate <create\|delete\|type\|open\|keys\|reload\|key\|take\|set\|keyall\|add\|edit\|remove\|bind\|unbind\|listbound\|info>` | `ultimatedonutsmp2.command.crate`; action checks `ultimatedonutsmp2.admin.crate`, `.crate.reload`, `.crate.keyall` | Full crate management. |
| `/crates` | — | `/crates` | `ultimatedonutsmp2.command.crates` | Open the crates menu. Same executor as `/crate`. |
| `/keys` | — | `/keys` | `ultimatedonutsmp2.command.keys`; falls back to `ultimatedonutsmp2.command.crates` | Open your crate keys menu. Same executor as `/crate`. |
| `/spawner` | `/spawners` | `/spawner [give\|info\|panel\|reload\|remove\|split]` | `ultimatedonutsmp2.command.spawner`; action check `ultimatedonutsmp2.admin.spawner` | Manage Donut-style spawners. |
| `/amethysttool` | — | `/amethysttool give <player> <type> [duration]` or `/amethysttool reload` | `ultimatedonutsmp2.command.amethysttool`; action check `ultimatedonutsmp2.admin.amethysttool` | Give and manage timed amethyst tools. |

### `/crate` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `create` | `<id>` | `ultimatedonutsmp2.admin.crate` | Create a crate definition. |
| `delete` | `<id>` | `ultimatedonutsmp2.admin.crate` | Delete a crate definition. |
| `type` | `<id> <type>` | `ultimatedonutsmp2.admin.crate` | Set the crate's opening type. |
| `open` | `<id>` | Command gate only | Open a crate you hold keys for. |
| `keys` | `[player]` | Command gate; `ultimatedonutsmp2.admin.crate` to view others | Show key balances. |
| `key` | `<player> <id> <amount>` | `ultimatedonutsmp2.admin.crate` | Add keys to a player. |
| `take` | `<player> <id> <amount>` | `ultimatedonutsmp2.admin.crate` | Remove keys from a player. |
| `set` | `<player> <id> <amount>` | `ultimatedonutsmp2.admin.crate` | Set a player's key balance. |
| `keyall` | `<id> <amount>` | `ultimatedonutsmp2.admin.crate.keyall` | Give keys to every online player. |
| `add` | `<id>` | `ultimatedonutsmp2.admin.crate` | Open the GUI editor. |
| `add` | `<id> <slot>` | `ultimatedonutsmp2.admin.crate` | Add the held item as a reward at that slot. |
| `add` | `<id> <slot> command <cmd…>` | `ultimatedonutsmp2.admin.crate` | Add a console-command reward. `{player}` is replaced when it runs. |
| `add` | `<id> <slot> money <amount>` | `ultimatedonutsmp2.admin.crate` | Add a money reward. |
| `add` | `<id> <slot> shards <amount>` | `ultimatedonutsmp2.admin.crate` | Add a shard reward. |
| `edit` | `<id> [slot]` | `ultimatedonutsmp2.admin.crate` | Edit rewards. No slot opens the GUI. |
| `remove` | `<id> <slot>` | `ultimatedonutsmp2.admin.crate` | Remove the reward in that slot. |
| `bind` | `<id>` | `ultimatedonutsmp2.admin.crate` | Bind a crate to the chest you are looking at. |
| `unbind` | `[id]` | `ultimatedonutsmp2.admin.crate` | Remove a chest binding. |
| `listbound` | — | `ultimatedonutsmp2.admin.crate` | List every bound chest. Not listed in the `plugin.yml` usage string. |
| `info` | — | Command gate only | Show crate information. |
| `reload` | — | `ultimatedonutsmp2.admin.crate.reload` | Reload crate configuration. |

In the GUI editor, naming a placeholder item `[CMD] …`, `[MONEY] <amount>` or `[SHARDS] <amount>` creates the matching non-item reward when you place it in a slot. See [Crates & Spawners](Crates-and-Spawners).

### `/spawner` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `give` | `<player> <type> [amount]` | `ultimatedonutsmp2.admin.spawner` | Give a managed spawner item. |
| `info` | — | `ultimatedonutsmp2.admin.spawner` | Inspect the spawner you are looking at. |
| `panel` | — | `ultimatedonutsmp2.admin.spawner` | Open the spawner management panel. |
| `split` | `<amount>` | `ultimatedonutsmp2.admin.spawner` | Split a stacked spawner. |
| `remove`, `forcebreak` | — | `ultimatedonutsmp2.admin.spawner` | Force-remove the targeted spawner. |
| `reload` | — | `ultimatedonutsmp2.admin.spawner` | Reload spawner configuration. |

Breaking a managed spawner normally requires a Silk Touch pickaxe when `REQUIRE_SILK_TOUCH` is enabled; `ultimatedonutsmp2.spawner.bypass` (default `false`) waives that requirement.

## PvP: Duels, FFA and Ranked

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/duel` | — | `/duel [player\|accept\|deny\|claims\|reload]` | `ultimatedonutsmp2.command.duel`; `reload` requires `ultimatedonutsmp2.admin.duels` | Challenge a player or manage your duel state. |
| `/create` | — | `/create <invite\|friends> <player> [map]` | `ultimatedonutsmp2.command.create` | Create a duel challenge with an explicit privacy mode. Belongs to the `DUELS` feature. |
| `/queue` | — | `/queue [join\|leave]` | `ultimatedonutsmp2.command.queue` | Open, join or leave the duel queue. |
| `/draw` | — | `/draw` | `ultimatedonutsmp2.command.draw` | Request or accept a duel draw. |
| `/leave` | — | `/leave` | `ultimatedonutsmp2.command.leave` | Leave a duel or FFA queue or match. |
| `/arena` | `/duelarena` | `/arena <create\|delete\|setpos1\|setpos2\|setreturn\|setdisplay\|enable\|disable\|queue\|list\|reload>` | `ultimatedonutsmp2.command.arena`; action check `ultimatedonutsmp2.admin.duels` | Manage duel arenas. |
| `/ffa` | — | `/ffa [join\|reload\|arena ...\|help]` | `ultimatedonutsmp2.command.ffa`; `reload` and `arena` require `ultimatedonutsmp2.admin.ffa` | Join the instanced FFA queue. |
| `/ffaarena` | — | `/ffaarena <create\|delete\|setpos\|setpos1\|setpos2\|setspawn1\|setspawn2\|setreturn\|setdisplay\|settings\|enable\|disable\|list\|reload>` | `ultimatedonutsmp2.command.ffaarena`; action check `ultimatedonutsmp2.admin.ffa` | Manage FFA arenas. |
| `/ffastats` | — | `/ffastats [player]` | `ultimatedonutsmp2.command.ffastats` | View FFA info (FFA currently does not track victories, defeats, draws, or streaks). |
| `/pvp` | — | see the subcommand table below | `ultimatedonutsmp2.command.pvp`; administrative subcommands require `ultimatedonutsmp2.admin.pvp` | Ranked PvP arena: play, inspect and administer. |

### `/pvp` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `join` | — | Command gate only | Join the ranked arena. |
| `leave` | — | Command gate only | Leave the arena or queue. |
| `kit` | `[kit]`, or `create\|edit\|delete\|icon\|permission\|slot\|display ...` | Editing requires `ultimatedonutsmp2.admin.pvp` | Select a kit, or manage kit definitions. |
| `stats` | `[player]` | Command gate only | Show ranked statistics. |
| `top` | `[page]` | Command gate only | Show the ranked ladder. |
| `leaderboard`, `lb` | — | Command gate only | Open the leaderboard menu. |
| `queue` | `[leave]` | Command gate only | Show or leave the ranked queue. |
| `history` | `[player]` | Command gate only | Show recent ranked matches. |
| `sync` | — | Command gate only | Generate a Discord sync code to link account. |
| `assign` | `[player] [player] [kit]` | `ultimatedonutsmp2.admin.pvp` | Start a match between two online players (or open assign menu). |
| `wand` | — | `ultimatedonutsmp2.admin.pvp` | Receive the arena selection wand. |
| `create` | `<name>` | `ultimatedonutsmp2.admin.pvp` | Create an arena from the current selection. |
| `setspawn`, `setspawn2` | — | `ultimatedonutsmp2.admin.pvp` | Set the two duel spawn points. |
| `setlobby` | — | `ultimatedonutsmp2.admin.pvp` | Set the arena lobby. |
| `setboundary` | — | `ultimatedonutsmp2.admin.pvp` | Set the arena boundary from the selection. |
| `schematic` | `<name>`, `load` | `ultimatedonutsmp2.admin.pvp` | Save or load an arena schematic. |
| `reset` | — | `ultimatedonutsmp2.admin.pvp` | Reset the arena structure from schematic. |
| `reload` | — | `ultimatedonutsmp2.admin.pvp` | Reload ranked arena configuration. |

Individual kits may additionally carry their own permission string, set with `/pvp kit permission <kit> <node|none>`.

### `/arena` and `/ffaarena` subcommands

Both commands require `ultimatedonutsmp2.admin.duels` and `ultimatedonutsmp2.admin.ffa` respectively for every subcommand, checked once on entry.

| Subcommand | Available on | Arguments | Effect |
|------------|--------------|-----------|--------|
| `list` | both | — | List arenas. |
| `create` | both | `<name>` | Create an arena. |
| `delete` | both | `<name>` | Delete an arena. |
| `enable` / `disable` | both | `<name>` | Toggle arena availability. |
| `setdisplay` | both | `<name> <display...>` | Set the arena display name. |
| `setpos1` / `setpos2` | both | `<name>` | Set the arena's corner positions. `/ffaarena` also accepts `setpos`. |
| `setreturn` | both | `<name>` | Set the return location used when a match ends. |
| `queue` | `/arena` | `<name>` | Inspect or configure the arena queue. |
| `setspawn1` / `setspawn2` | `/ffaarena` | `<name>` | Set FFA spawn points. |
| `settings` | `/ffaarena` | `<name> <nohunger\|noweather\|alwaysmorning\|nofalldamage> <on\|off>` | Toggle per-arena rules. |
| `reload` | both | — | Reload arena configuration. |

## Social & Chat

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/chat` | — | `/chat <help\|mute\|unmute\|delay\|clear>` | `ultimatedonutsmp2.command.chat`; each action has its own `ultimatedonutsmp2.staff.chat.*` node | Manage global chat. With no arguments, shows help. |
| `/msg` | `/message`, `/tell`, `/whisper`, `/w` | `/msg <player> <message>` | `ultimatedonutsmp2.command.msg`; action check `ultimatedonutsmp2.message` | Send a private message. |
| `/reply` | `/r` | `/reply <message>` | `ultimatedonutsmp2.command.reply`; action check `ultimatedonutsmp2.message` | Reply to your last private conversation. |
| `/pm` | `/togglepm`, `/privatemessages` | `/pm` | `ultimatedonutsmp2.command.pm`; action check `ultimatedonutsmp2.message.toggle` | Toggle whether you receive private messages. |
| `/ignore` | — | `/ignore <player\|list>` | `ultimatedonutsmp2.command.ignore`; action check `ultimatedonutsmp2.ignore` | Add a player to your ignore list, or list it. |
| `/unignore` | — | `/unignore <player>` | `ultimatedonutsmp2.command.unignore`; action check `ultimatedonutsmp2.ignore` | Remove a player from your ignore list. |
| `/helpop` | — | `/helpop <message>` | `ultimatedonutsmp2.command.helpop`; action check `ultimatedonutsmp2.helpop` | Request staff assistance. |
| `/report` | — | `/report <player> <reason>` | `ultimatedonutsmp2.command.report`; action check `ultimatedonutsmp2.report` | Report a player to online staff. |
| `/team` | — | `/team <create\|disband\|invite\|kick\|join\|leave\|home\|sethome\|delhome\|chat\|info\|pvp>` | `ultimatedonutsmp2.command.team` | Team creation, membership, team home and team chat. |
| `/friend` | — | `/friend [list\|friends\|following\|followers\|add\|remove\|search\|reload]` | `ultimatedonutsmp2.command.friend`; action check `ultimatedonutsmp2.friends`; `reload` requires `donutfriends.admin` | Friends and follows. Same executor as `/friends`. |
| `/friends` | — | `/friends [list\|friends\|following\|followers\|add\|remove\|search\|reload]` | `ultimatedonutsmp2.command.friends`; action check `ultimatedonutsmp2.friends`; `reload` requires `donutfriends.admin` | Friends and follows. |
| `/discord` | — | `/discord` | `ultimatedonutsmp2.command.discord` | Show the Discord link from `SOCIAL.DISCORD`. |
| `/twitter` | — | `/twitter` | `ultimatedonutsmp2.command.twitter` | Show the Twitter/X link from `SOCIAL.TWITTER`. |
| `/store` | — | `/store` | `ultimatedonutsmp2.command.store` | Show the store link from `SOCIAL.STORE`. |
| `/social` | `/media` | `/social` | `ultimatedonutsmp2.command.social` | Show all configured social links. |
| `/voicechatconsent` | `/vcconsent`, `/voiceconsent` | `/voicechatconsent [revoke]` | `ultimatedonutsmp2.command.voicechatconsent` | Open the voice chat consent menu, or revoke consent. |

`/discord`, `/twitter`, `/store` and `/social` share a single executor that selects the configuration key from the command label.

A received `/msg` is clickable when the sender is a player. The click puts `/msg <name> ` into the chat box, with a space after the name, so the reply can be typed immediately. That name is the one other players see, including a disguise alias. The receiver also hears `PRIVATE_MESSAGE.RECEIVED` from `sounds.yml`, unless notification sounds are off in `/settings` or that sound value is empty.

### `/chat` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| *(none)*, `help` | — | Any of the five `staff.chat.*` nodes below | Show the chat help text. |
| `mute` | — | `ultimatedonutsmp2.staff.chat.mute` | Mute global chat. |
| `unmute` | — | `ultimatedonutsmp2.staff.chat.unmute` | Unmute global chat. |
| `delay` | `<seconds\|off>` | `ultimatedonutsmp2.staff.chat.delay` | Set or clear the global chat delay, bounded by the configured maximum. |
| `clear` | — | `ultimatedonutsmp2.staff.chat.clear` | Visually clear global chat. |

Entry to `/chat` requires at least one of `ultimatedonutsmp2.staff.chat.use`, `.mute`, `.unmute`, `.delay` or `.clear`. The three bypass nodes `ultimatedonutsmp2.staff.chat.bypass.mute`, `.bypass.delay` and `.bypass.filter` exempt a player from the corresponding restriction.

### `/team` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `create` | `<name>` | Command gate only | Create a team. |
| `disband` | — | Command gate only | Disband your team (leader only). |
| `invite` | `<player>` | Command gate only | Invite a player. |
| `join` | `<team>` | Command gate only | Accept a pending invite. |
| `leave` | — | Command gate only | Leave your team. |
| `kick` | `<player>` | Command gate only | Remove a member. |
| `home` / `sethome` / `delhome` | — | Command gate only | Use, set or delete the team home. |
| `chat` | — | Command gate only | Toggle team chat. |
| `info` | `[team]` | Command gate only | Show team information. |
| `pvp` | — | Command gate only | Toggle team-internal PvP. |

## Player Settings & Info

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/menu` | `/donut`, `/dialog` | `/menu` | `ultimatedonutsmp2.command.menu` | Open the main dialog menu. |
| `/settings` | — | `/settings` | `ultimatedonutsmp2.command.settings` | Open your personal settings menu. |
| `/stats` | — | `/stats [player]` | `ultimatedonutsmp2.command.stats` | View player statistics. |
| `/ping` | — | `/ping [player]` | `ultimatedonutsmp2.command.ping` | Check your latency or another online player's. |
| `/playtime` | `/pt` | `/playtime [player]` | `ultimatedonutsmp2.command.playtime` | Check playtime. |
| `/leaderboard` | `/lb`, `/top`, `/leaderboards`, `/baltop` | `/leaderboard [type]` | `ultimatedonutsmp2.command.leaderboard` | View leaderboards. |
| `/ranks` | `/rank` | `/ranks` | `ultimatedonutsmp2.command.ranks` | View rank perks. |
| `/rules` | — | `/rules` | `ultimatedonutsmp2.command.rules` | View server rules. |
| `/help` | — | `/help` | `ultimatedonutsmp2.command.help` | View the plugin help menu. |
| `/nightvision` | `/nv` | `/nightvision` | `ultimatedonutsmp2.command.nightvision` | Toggle night vision. |
| `/phantom` | — | `/phantom` | `ultimatedonutsmp2.command.phantom` | Toggle phantom spawning for yourself. |
| `/enderchest` | `/ec` | `/enderchest [reload]` | `ultimatedonutsmp2.command.enderchest`; also referenced as `ultimatedonutsmp2.enderchest`; `reload` requires `ultimatedonutsmp2.admin.enderchest` | Open your custom Ender Chest, sized by your row tier. |
| `/safety` | — | `/safety [reload\|add\|give]` | `ultimatedonutsmp2.command.safety`; action checks `safety.use`, `safety.add`, `safety.reload` | Safety information and safety item handling. |

`/safety` is unusual in that its runtime nodes live in a bare `safety.*` namespace rather than under `ultimatedonutsmp2.`. None of `safety.use`, `safety.add` or `safety.reload` is declared in `plugin.yml`.

## Staff & Moderation

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/staffmode` | `/staff` | `/staffmode [player\|reload]` | `ultimatedonutsmp2.command.staffmode`; `reload` uses `ultimatedonutsmp2.admin.staffmode`; toggling others uses `ultimatedonutsmp2.staff.mode.others` | Toggle the staff moderation hotbar. |
| `/stafflist` | — | `/stafflist` | `ultimatedonutsmp2.command.stafflist`; hotbar equivalent `ultimatedonutsmp2.staff.mode.stafflist` | Open the online staff list. |
| `/staffchat` | `/sc` | `/staffchat <message>` | `ultimatedonutsmp2.command.staffchat`; action check `ultimatedonutsmp2.staff.chat.use` | Send a message to network staff chat. |
| `/vanish` | — | `/vanish` | `ultimatedonutsmp2.command.vanish`; hotbar equivalent `ultimatedonutsmp2.staff.mode.vanish` | Toggle vanish while in staff mode. |
| `/freeze` | — | `/freeze <player>` or `/freeze reload` | `ultimatedonutsmp2.command.freeze`; runtime nodes `ultimatedonutsmp2.staff.freeze`, `.freeze.alert`, `.freeze.exempt`; `reload` uses `ultimatedonutsmp2.admin.freeze` | Freeze or unfreeze a player. |
| `/invsee` | `/inventorysee` | `/invsee <player>` or `/invsee reload` | `ultimatedonutsmp2.command.invsee`; runtime nodes `ultimatedonutsmp2.staff.invsee`, `.invsee.modify`; `reload` uses `ultimatedonutsmp2.admin.invsee` | View another player's inventory. |
| `/ecsee` | — | `/ecsee <player>` | `ultimatedonutsmp2.command.ecsee`; editing requires `ultimatedonutsmp2.admin.ecsee.edit` | Inspect another player's custom Ender Chest. |
| `/profileviewer` | `/pv` | `/profileviewer <player>` | `ultimatedonutsmp2.command.profileviewer`; runtime node `ultimatedonutsmp2.staff.profileviewer` | View a player's profile and homes. |
| `/seehomes` | `/homesee` | `/seehomes <player>` | `ultimatedonutsmp2.command.seehomes`; action check `ultimatedonutsmp2.staff.seehomes` | Browse another player's homes and teleport to them. |
| `/alts` | — | `/alts <player>` | `ultimatedonutsmp2.command.alts`; action check `ultimatedonutsmp2.staff.alts` | View alternate accounts by shared IP history. |
| `/chatlog` | — | `/chatlog [player]` | `ultimatedonutsmp2.command.chatlog`; action check `ultimatedonutsmp2.admin.chatlog` | Browse logged public chat, server-wide or per player. |
| `/logs` | — | `/logs [player]` | `ultimatedonutsmp2.command.logs`; action check `ultimatedonutsmp2.admin.logs` | Browse logged shop, auction and admin activity. |
| `/teleport` | `/tp`, `/tphere`, `/tpall` | `/teleport <player>`, `/teleport here <player>`, `/teleport all`, `/teleport top`, `/teleport <x> <y> <z> [world]` | `ultimatedonutsmp2.command.teleport`; action check `ultimatedonutsmp2.staff.teleport` | Staff teleport suite. |
| `/randomteleport` | `/randomtp` | `/randomteleport` | `ultimatedonutsmp2.command.randomteleport`; hotbar equivalent `ultimatedonutsmp2.staff.mode.randomtp` | Teleport to a random online player. |
| `/findplayer` | `/fp` | `/findplayer <player>` | `ultimatedonutsmp2.command.findplayer` | Report a player's current location. |
| `/fly` | — | `/fly [player]` | `ultimatedonutsmp2.command.fly`; action check `ultimatedonutsmp2.staff.fly`; player-facing node configurable via `FLY-SYSTEM.PLAYER-FLY-PERMISSION`, default `ultimatedonutsmp2.player.fly` | Toggle flight for yourself or another player. |
| `/flyspeed` | `/fs` | `/flyspeed <1-10> [player]` | `ultimatedonutsmp2.command.flyspeed`; also accepts `ultimatedonutsmp2.staff.flyspeed` or `ultimatedonutsmp2.staff.fly`; affecting others needs `ultimatedonutsmp2.command.flyspeed.others` | Adjust flying speed. |
| `/heal` | — | `/heal [player]` | `ultimatedonutsmp2.command.heal`; action check `ultimatedonutsmp2.staff.heal` | Restore health. |
| `/feed` | — | `/feed [player]` | `ultimatedonutsmp2.command.feed`; action check `ultimatedonutsmp2.staff.feed` | Restore hunger. |
| `/gamemode` | `/gm`, `/gmc`, `/gms`, `/gma`, `/gmsp` | `/gamemode <survival\|creative\|adventure\|spectator\|0-3> [player]` | `ultimatedonutsmp2.command.gamemode`; action checks `ultimatedonutsmp2.staff.gamemode` and `.gamemode.others` | Change game mode. The short aliases imply the mode. |
| `/god` | `/godmode` | `/god [player]` | `ultimatedonutsmp2.staff.god` | Toggle god mode. This command has no `ultimatedonutsmp2.command.*` node. |
| `/rename` | — | `/rename <name...>` or `/rename <reset\|clear\|remove>` | `ultimatedonutsmp2.command.rename`; action check `ultimatedonutsmp2.staff.rename` | Rename the item in your hand. |
| `/hide` | — | `/hide [status\|check <player>\|scramble\|remove [player]\|list]` | `ultimatedonutsmp2.command.hide`; feature nodes `ultimatedonutsmp2.hide.scramble`, `.disguise`, `.admin`, `.bypass` | Persistent identity scrambling. With no arguments, opens the hide menu. |
| `/disguise` | — | `/disguise [<alias>] [skin]` | `ultimatedonutsmp2.command.disguise`; feature nodes as for `/hide` | Apply a configured disguise. With no arguments, opens the disguise alias menu. |
| `/fakeplayer` | `/fplayer` | `/fakeplayer` | `ultimatedonutsmp2.command.fakeplayer`; runtime check `ultimatedonutsmp.staff.fakeplayer` (legacy prefix) | Manage fake players used for bait and testing. |
| `/spawnstash` | `/stash` | `/spawnstash [spawn <type>\|list\|remove <id>\|reload]` | `ultimatedonutsmp2.command.spawnstash`; runtime checks `ultimatedonutsmp2.staff.spawnstash` and `ultimatedonutsmp2.admin.spawnstash` | Spawn and manage staff bait stashes. |
| `/amod` | — | `/amod` | `ultimatedonutsmp2.command.amod`; action check `anvilmod.admin` | Anvil moderation tooling. |

### `/hide` and `/disguise` subcommands

| Subcommand | Command | Arguments | Permission | Effect |
|------------|---------|-----------|------------|--------|
| *(none)* | `/hide` | — | Command gate only | Open the hide menu. |
| `status`, `check` | `/hide` | `[player]` | Own status: command gate. Another player: `ultimatedonutsmp2.hide.admin` | Show hide state. |
| `scramble` | `/hide` | — | `ultimatedonutsmp2.hide.scramble` | Generate and apply a scrambled identity. |
| `remove`, `removal` | `/hide` | `[player]` | Own: command gate. Another player: `ultimatedonutsmp2.hide.admin` | Clear a hide state. |
| `list` | `/hide` | — | `ultimatedonutsmp2.hide.admin` | List all hidden players. |
| *(none)* | `/disguise` | — | Command gate only | Open the disguise alias menu. |
| *(alias)* | `/disguise` | `<alias> [skin]` | `ultimatedonutsmp2.hide.disguise` | Apply a disguise, resolving a skin username or skin URL. |

`ultimatedonutsmp2.hide.bypass` lets a holder see through other players' hidden identities. None of the four `ultimatedonutsmp2.hide.*` nodes is declared in `plugin.yml`.

### `/spawnstash` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| *(none)* | — | `ultimatedonutsmp2.staff.spawnstash` | Show stash status and usage. |
| `spawn` | `<type>` | `ultimatedonutsmp2.staff.spawnstash` | Spawn a bait stash of the given type. |
| `list` | — | `ultimatedonutsmp2.staff.spawnstash` | List active stashes. |
| `remove` | `<id>` | `ultimatedonutsmp2.admin.spawnstash` | Remove a stash and roll back its blocks. |
| `reload` | — | `ultimatedonutsmp2.admin.spawnstash` | Reload SpawnStash configuration. |

`ultimatedonutsmp2.staff.spawnstash.alert` receives stash trip alerts, and `ultimatedonutsmp2.staff.spawnstash.bypass` prevents a holder from triggering one.

## Punishments

All punishment commands share one executor, which dispatches on the command label and checks a per-action node. Because each `ultimatedonutsmp2.command.<name>` node is declared as a parent of the matching `ultimatedonutsmp2.staff.punishments.*` child, granting the command node is sufficient in a default setup.

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/ban` | — | `/ban <player> [reason]` | `ultimatedonutsmp2.staff.punishments.ban` | Permanently ban a player and record the punishment. |
| `/tempban` | — | `/tempban <player> <time> [reason]` | `ultimatedonutsmp2.staff.punishments.ban` | Temporarily ban a player. Time accepts `30s`, `15m`, `2h`, `5d` or compound forms such as `5d 15m 30s`. |
| `/unban` | `/pardon` | `/unban <player> [reason]` | `ultimatedonutsmp2.staff.punishments.unban` | Remove active ban records. |
| `/mute` | — | `/mute <player> [reason]` | `ultimatedonutsmp2.staff.punishments.mute` | Permanently mute a player. |
| `/tempmute` | — | `/tempmute <player> <time> [reason]` | `ultimatedonutsmp2.staff.punishments.mute` | Temporarily mute a player. |
| `/unmute` | — | `/unmute <player> [reason]` | `ultimatedonutsmp2.staff.punishments.unmute` | Remove active mute records. |
| `/vcmute` | — | `/vcmute <player> [reason]` | `ultimatedonutsmp2.staff.vcmute` | Mute a player in voice chat. |
| `/vcunmute` | — | `/vcunmute <player> [reason]` | `ultimatedonutsmp2.staff.vcunmute` | Remove active voice chat mute records. |
| `/blacklist` | — | `/blacklist <player> [reason]` | `ultimatedonutsmp2.staff.punishments.blacklist` | Blacklist a player. |
| `/unblacklist` | — | `/unblacklist <player> [reason]` | `ultimatedonutsmp2.staff.punishments.unblacklist` | Remove active blacklist records. |
| `/warn` | — | `/warn <player> [reason]` | `ultimatedonutsmp2.staff.punishments.create` | Warn a player and record the punishment. |
| `/kick` | — | `/kick <player> [reason]` | `ultimatedonutsmp2.staff.punishments.create` | Kick a player and record the punishment. |
| `/offend` | — | `/offend <player> <reason> [time] [wipe]` | `ultimatedonutsmp2.staff.punishments.offend`; also checks `ultimatedonutsmp2.staff.punishments.create` | Apply a preset offence from `offenses.yml`, with an optional duration and wipe override (`wipe`/`true`/`nowipe`/`false`). |
| `/punishments` | `/phistory` | `/punishments [player]` | `ultimatedonutsmp2.command.punishments`; GUI actions use `ultimatedonutsmp2.staff.punishments.view` and `.delete` | Browse punishment history, server-wide or per player. |

Two nodes govern who may be punished: a target holding `ultimatedonutsmp2.admin.punishments.exempt` cannot be punished, unless the issuing staff member holds `ultimatedonutsmp2.admin.punishments.exempt.bypass`.

## Network & Maintenance

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/servers` | — | `/servers` | `ultimatedonutsmp2.command.servers`; action check `ultimatedonutsmp2.servers` | View network server status. |
| `/maintenance` | — | `/maintenance [on\|off\|status\|setlobby]` | `ultimatedonutsmp2.command.maintenance`; every subcommand requires `ultimatedonutsmp2.admin.maintenance` | Toggle maintenance mode and set the redirect lobby. |
| `/clearlag` | — | `/clearlag` | `ultimatedonutsmp2.command.clearlag`; action check `ultimatedonutsmp2.admin.clearlag` | Manually clear lag entities. |
| `/serverwipe` | — | `/serverwipe <preview\|status\|prepare\|confirm\|cancel>` | `ultimatedonutsmp2.command.serverwipe`; action check `ultimatedonutsmp2.admin.serverwipe` | Preview and run a full server data wipe. |
| `/playerwipe` | `/pwipe`, `/wipe` | `/playerwipe <player> [confirm]` | `ultimatedonutsmp2.command.playerwipe`; action check `ultimatedonutsmp2.admin.playerwipe` | Wipe everything the plugin stores about one player. |
| `/playerunwipe` | `/punwipe`, `/unwipe` | `/playerunwipe <player> [confirm]` | `ultimatedonutsmp2.command.playerunwipe`; action check `ultimatedonutsmp2.admin.playerunwipe` | Restore a wiped player from the backup their wipe created. |

### `/maintenance` subcommands

| Subcommand | Aliases | Arguments | Permission | Effect |
|------------|---------|-----------|------------|--------|
| `on` | `start`, `enable` | `[duration]` | `ultimatedonutsmp2.admin.maintenance` | Enter maintenance mode, optionally for a fixed period such as `15m`. |
| `off` | `stop`, `disable` | — | `ultimatedonutsmp2.admin.maintenance` | Leave maintenance mode. |
| `status` | — | — | `ultimatedonutsmp2.admin.maintenance` | Report maintenance state. |
| `setlobby` | — | `<server>` | `ultimatedonutsmp2.admin.maintenance` | Set the lobby that players are redirected to. |

### `/serverwipe` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `preview` | — | `ultimatedonutsmp2.admin.serverwipe` | Report what a wipe would delete. |
| `status` | — | `ultimatedonutsmp2.admin.serverwipe` | Report the state of any staged wipe. |
| `prepare` | — | `ultimatedonutsmp2.admin.serverwipe` | Stage a wipe and print the confirmation token. |
| `confirm` | `<token>` | `ultimatedonutsmp2.admin.serverwipe` | Execute the staged wipe using the token from `prepare`. |
| `cancel` | — | `ultimatedonutsmp2.admin.serverwipe` | Discard the staged wipe. |

`/playerwipe` and `/playerunwipe` both preview by default and require `confirm` as a second argument to act.

## Admin & Utility

| Command | Aliases | Usage | Permission | Description |
|---------|---------|-------|------------|-------------|
| `/ultimatedonutsmp2` | `/uds`, `/udsmp` | `/ultimatedonutsmp2 <reload\|statswipe\|optimize\|setup\|features\|maintenance>` | `ultimatedonutsmp2.command.ultimatedonutsmp2`; each subcommand has its own `ultimatedonutsmp2.admin.*` node | Plugin administration entry point. |
| `/cuboid` | — | `/cuboid <wand\|create\|delete\|list\|setspawn\|delspawn\|bind\|reload>` | `ultimatedonutsmp2.command.cuboid`; action check `ultimatedonutsmp2.admin.cuboid` | Manage cuboid regions and bind them to systems. |
| `/kill` | — | `/kill` | `ultimatedonutsmp2.command.kill` | Open a confirmation menu to kill yourself. Blocked while combat-tagged. |

### `/ultimatedonutsmp2` subcommands

| Subcommand | Arguments | Permission | Effect |
|------------|-----------|------------|--------|
| `reload` | — | `ultimatedonutsmp2.admin.reload` | Reload every plugin configuration file. |
| `statswipe` | *(none)*, `menu`, `gui`, or `<target> confirm` | `ultimatedonutsmp2.admin.statswipe` | Open the stats wipe GUI, or wipe a named target directly. |
| `optimize`, `optimization` | `[status\|reload\|reset]` | `ultimatedonutsmp2.admin.optimize` | View optimisation status, reload optimisation settings, or reset runtime counters. Not documented in the `plugin.yml` usage string beyond `optimize`. |
| `setup` | `status`, `setspawn`, `setafk`, `commands <group>`, `apply single-paper confirm` | `ultimatedonutsmp2.admin.setup` | Run the guided setup tools. Command groups are `starter`, `economy`, `market`, `pvp`, `staff` and `admin`. |
| `features` | *(none)*, `menu`, `gui`, or `<enable\|disable\|toggle> <key>` | `ultimatedonutsmp2.admin.features` | Manage feature toggles from chat or the GUI. |
| `maintenance` | `[on\|off\|status\|setlobby <server>]` | `ultimatedonutsmp2.admin.maintenance` | Maintenance control, mirroring `/maintenance`. |

### `/cuboid` subcommands

| Subcommand | Aliases | Arguments | Permission | Effect |
|------------|---------|-----------|------------|--------|
| `wand` | — | — | `ultimatedonutsmp2.admin.cuboid` | Receive the selection wand. Left click sets position 1, right click position 2. |
| `create` | `save` | `<name>` | `ultimatedonutsmp2.admin.cuboid` | Save the current selection as a named cuboid. |
| `delete` | — | `<name>` | `ultimatedonutsmp2.admin.cuboid` | Delete a cuboid. |
| `list` | — | — | `ultimatedonutsmp2.admin.cuboid` | List cuboids. |
| `bind` | `system` | `<cuboid> <spawn\|shard\|rtp-zone\|rtp-queue> <true\|false>` | `ultimatedonutsmp2.admin.cuboid` | Bind or unbind a cuboid to a plugin system. The role accepts variants such as `shards`, `rtpzone` and `rtpq`. |
| `setspawn` | — | `<cuboid>` | `ultimatedonutsmp2.admin.cuboid` | Set the cuboid's spawn point. |
| `delspawn` | — | `<cuboid>` | `ultimatedonutsmp2.admin.cuboid` | Clear the cuboid's spawn point. |

## Numbered permission tiers

Six permission families end in a number. The plugin does not read the number from a configuration file; it probes the permissible for each candidate node in turn and derives a value from whichever nodes are present. In every case the node must be granted exactly — inheriting a wildcard does not produce a tier value.

| Family | Scanned range | Resolution | Effect |
|--------|---------------|-----------|--------|
| `ultimatedonutsmp2.homes.<N>` | 1–100 | Highest granted N | Maximum home slots. |
| `ultimatedonutsmp2.homes.page.<N>` | 1–100 | Highest granted N, multiplied by 5 | Maximum home GUI pages; five homes per page. Combined with the previous family by taking the larger result. |
| `ultimatedonutsmp2.rtp.cooldown.<seconds>` | any integer ≥ 0 | **Lowest** granted value | RTP cooldown override in seconds. The shortest cooldown wins, so `cooldown.10` beats `cooldown.60`. |
| `ultimatedonutsmp2.rtp.priority.<N>` | any integer | Highest granted N | RTP queue priority; higher jumps ahead. |
| `ultimatedonutsmp2.enderchest.rows.<N>` | 1–54 | Highest granted N, clamped to 1–6 | Ender Chest row count. Values above 6 clamp to the largest chest rather than falling back to the default. |
| `ultimatedonutsmp2.auctionhouse.limit.<N>` | 1–100 | Highest granted N | Auction House listing limit. |

Notes on how the tiers combine with configuration:

- Home slots also honour arbitrary permission-to-number mappings under `SETTINGS.HOME-PERMISSIONS.PERMISSIONS`, and the whole mechanism can be disabled with `SETTINGS.HOME-PERMISSIONS.ENABLED: false`. When no tier applies, `SETTINGS.HOME-DEFAULT` is used.
- Ender Chest rows also honour `ENDER-CHEST.ROW-PERMISSIONS.PERMISSIONS`, and `ENDER-CHEST.ROW-PERMISSIONS.ON-DOWNGRADE` decides whether items are returned (`RETURN-ITEMS`) or the chest keeps its old size (`KEEP-SIZE`) when a player loses a tier.
- RTP cooldowns also honour `SETTINGS.RANK-COOLDOWNS.PERMISSIONS`, again taking the lowest matching value, and fall back to the per-world cooldown.
- The Auction House limit resolver additionally scans the bare numeric family `ultimatedonutsmp2.auctionhouse.<N>` and the legacy `donutauction.limit.<N>`, and a configured mapping can return an unlimited value.

## Permission reference

Every node below is declared in `src/main/resources/plugin.yml`, 260 in total. Defaults are reproduced exactly as declared: `op` means operators only, `true` means everyone, `false` means nobody until explicitly granted.

### `ultimatedonutsmp2.admin.*`

`ultimatedonutsmp2.admin` (default `op`) is a parent that grants `ultimatedonutsmp2.command.*` and all forty children listed here.

| Node | Default | Description |
|------|---------|-------------|
| `ultimatedonutsmp2.admin` | op | Full admin access; parent of the entire family. |
| `ultimatedonutsmp2.admin.addmoney` | op | Add money to player balances. |
| `ultimatedonutsmp2.admin.amethysttool` | op | Manage and give amethyst tools. |
| `ultimatedonutsmp2.admin.auctionhouse` | op | Reload Auction House settings; also satisfies every per-action AH check. |
| `ultimatedonutsmp2.admin.chatlog` | op | Browse logged public chat messages. |
| `ultimatedonutsmp2.admin.clearlag` | op | Manually clear lag. |
| `ultimatedonutsmp2.admin.crate` | op | Manage crate balances, settings and chest bindings. |
| `ultimatedonutsmp2.admin.crate.keyall` | op | Trigger crate key-all rewards manually. |
| `ultimatedonutsmp2.admin.crate.reload` | op | Reload crate settings. |
| `ultimatedonutsmp2.admin.cuboid` | op | Manage cuboid regions. |
| `ultimatedonutsmp2.admin.delwarp` | op | Delete warp points. |
| `ultimatedonutsmp2.admin.duels` | op | Manage the duel system and arenas. |
| `ultimatedonutsmp2.admin.ecsee` | op | Declared without a description; the Ender Chest inspection admin node. |
| `ultimatedonutsmp2.admin.enderchest` | op | Reload Ender Chest settings. |
| `ultimatedonutsmp2.admin.features` | op | Manage feature toggles. |
| `ultimatedonutsmp2.admin.ffa` | op | Manage the FFA system and arenas. |
| `ultimatedonutsmp2.admin.freeze` | op | Reload and administer Freeze settings. |
| `ultimatedonutsmp2.admin.invsee` | op | Reload and administer Invsee settings. |
| `ultimatedonutsmp2.admin.logs` | op | Browse a player's logged shop, auction and admin activity. |
| `ultimatedonutsmp2.admin.maintenance` | op | Toggle maintenance mode and manage the allowed player list. |
| `ultimatedonutsmp2.admin.optimize` | op | View and reload runtime optimisation controls. |
| `ultimatedonutsmp2.admin.orders` | op | Reload and manage Orders settings. |
| `ultimatedonutsmp2.admin.playerunwipe` | op | Preview and restore a wiped player's stored data. |
| `ultimatedonutsmp2.admin.playerwipe` | op | Preview and wipe a single player's stored data. |
| `ultimatedonutsmp2.admin.portalmanager` | op | Manage RTP portal triggers. |
| `ultimatedonutsmp2.admin.punishments.exempt` | op | Cannot be punished by staff who lack the exempt bypass. |
| `ultimatedonutsmp2.admin.punishments.exempt.bypass` | op | Punish players who are otherwise exempt. |
| `ultimatedonutsmp2.admin.pvp` | op | Manage the ranked PvP arena and its kits. |
| `ultimatedonutsmp2.admin.reload` | op | Reload all UltimateDonutSmp2 configuration files. |
| `ultimatedonutsmp2.admin.removemoney` | op | Remove money from player balances. |
| `ultimatedonutsmp2.admin.serverwipe` | op | Preview and run a full server data wipe. |
| `ultimatedonutsmp2.admin.setmoney` | op | Set player balances. |
| `ultimatedonutsmp2.admin.setup` | op | Run the setup tools and set the spawn and AFK zone locations. |
| `ultimatedonutsmp2.admin.setwarp` | op | Set warp points. |
| `ultimatedonutsmp2.admin.shards` | op | Shard administration and Shards Everywhere status. |
| `ultimatedonutsmp2.admin.shop` | op | Reload and validate shop settings. |
| `ultimatedonutsmp2.admin.spawner` | op | Manage internal spawner systems. |
| `ultimatedonutsmp2.admin.staffmode` | op | Reload and administer Staff Mode settings. |
| `ultimatedonutsmp2.admin.statswipe` | op | Open and execute Stats Wipe tools. |
| `ultimatedonutsmp2.admin.warpmanager` | op | Manage public warp points. |
| `ultimatedonutsmp2.admin.worth` | op | Reload worth settings. |

### `ultimatedonutsmp2.command.*`

`ultimatedonutsmp2.command.*` (default `op`) is a wildcard granting all 140 declared children. Defaults split into two groups.

Nodes with default `true` (available to everyone):

`afk`, `auctionhouse`, `balance`, `billford`, `bounty`, `chat`, `crates`, `delhome`, `discord`, `draw`, `duel`, `enderchest`, `ffa`, `ffastats`, `friend`, `friends`, `help`, `helpop`, `home`, `homes`, `ignore`, `keys`, `leaderboard`, `leave`, `menu`, `meta`, `msg`, `nightvision`, `orders`, `pay`, `phantom`, `ping`, `playtime`, `pm`, `pvp`, `queue`, `ranks`, `renamehome`, `reply`, `report`, `rtp`, `rtpq`, `rules`, `sell`, `sellall`, `sellhand`, `sellhistory`, `sellmulti`, `sellprogress`, `sethome`, `settings`, `shardpay`, `shards`, `shardshop`, `shop`, `social`, `spawn`, `spawner`, `stats`, `store`, `team`, `tpa`, `tpacancel`, `tpaccept`, `tpadeny`, `tpahere`, `tpahereauto`, `tpauto`, `twitter`, `unignore`, `voicechatconsent`, `warp`, `worth`.

Nodes with default `op`:

`addmoney`, `addshards`, `alts`, `amethysttool`, `amod`, `arena`, `ban`, `blacklist`, `chatlog`, `clearlag`, `crate`, `create`, `cuboid`, `delwarp`, `disguise`, `ecsee`, `fakeplayer`, `feed`, `ffaarena`, `findplayer`, `fly`, `flyspeed`, `flyspeed.others`, `freeze`, `gamemode`, `heal`, `hide`, `invsee`, `kick`, `kill`, `logs`, `maintenance`, `mute`, `offend`, `playerunwipe`, `playerwipe`, `portalmanager`, `profileviewer`, `punishments`, `randomteleport`, `removemoney`, `removeshards`, `rename`, `safety`, `seehomes`, `servers`, `serverwipe`, `setmoney`, `setshards`, `setwarp`, `spawnstash`, `staffchat`, `stafflist`, `staffmode`, `teleport`, `tempban`, `tempmute`, `topsell`, `ultimatedonutsmp2`, `unban`, `unblacklist`, `unmute`, `vanish`, `vcmute`, `vcunmute`, `warn`, `warpmanager`.

Ten of these nodes are declared as parents of a punishment child, so granting the command node also grants the action node:

| Command node | Child granted |
|--------------|---------------|
| `ultimatedonutsmp2.command.ban` | `ultimatedonutsmp2.staff.punishments.ban` |
| `ultimatedonutsmp2.command.tempban` | `ultimatedonutsmp2.staff.punishments.ban` |
| `ultimatedonutsmp2.command.unban` | `ultimatedonutsmp2.staff.punishments.unban` |
| `ultimatedonutsmp2.command.mute` | `ultimatedonutsmp2.staff.punishments.mute` |
| `ultimatedonutsmp2.command.tempmute` | `ultimatedonutsmp2.staff.punishments.mute` |
| `ultimatedonutsmp2.command.unmute` | `ultimatedonutsmp2.staff.punishments.unmute` |
| `ultimatedonutsmp2.command.blacklist` | `ultimatedonutsmp2.staff.punishments.blacklist` |
| `ultimatedonutsmp2.command.unblacklist` | `ultimatedonutsmp2.staff.punishments.unblacklist` |
| `ultimatedonutsmp2.command.vcmute` | `ultimatedonutsmp2.staff.vcmute` |
| `ultimatedonutsmp2.command.vcunmute` | `ultimatedonutsmp2.staff.vcunmute` |
| `ultimatedonutsmp2.command.warn` | `ultimatedonutsmp2.staff.punishments.create` |
| `ultimatedonutsmp2.command.kick` | `ultimatedonutsmp2.staff.punishments.create` |
| `ultimatedonutsmp2.command.offend` | `ultimatedonutsmp2.staff.punishments.offend` |

Note that `ultimatedonutsmp2.command.flyspeed.others` is the only command node that is not simply a command name; it gates the `[player]` argument of `/flyspeed` rather than the command itself.

### `ultimatedonutsmp2.staff.*`

| Node | Default | Description |
|------|---------|-------------|
| `ultimatedonutsmp2.staff.mode` | op | Parent: full staff access for Staff Mode and staff utility commands. Grants 28 children including `ultimatedonutsmp2.chat.color`. |
| `ultimatedonutsmp2.staff.mode.betterview` | op | Toggle better view from the staff mode hotbar. |
| `ultimatedonutsmp2.staff.mode.others` | op | Toggle staff mode for other players. |
| `ultimatedonutsmp2.staff.mode.randomtp` | op | Teleport to a random player from the hotbar. |
| `ultimatedonutsmp2.staff.mode.seevanished` | op | See vanished staff members. |
| `ultimatedonutsmp2.staff.mode.stafflist` | op | Open the staff list from the hotbar. |
| `ultimatedonutsmp2.staff.mode.vanish` | op | Toggle vanish from the hotbar. |
| `ultimatedonutsmp2.staff.alerts.receive` | op | Parent: receive helpop and report alerts. |
| `ultimatedonutsmp2.staff.alerts.bypass-cooldown` | op | Bypass helpop and report cooldowns. |
| `ultimatedonutsmp2.staff.helpop.receive` | op | Receive helpop alerts. |
| `ultimatedonutsmp2.staff.report.receive` | op | Receive report alerts. |
| `ultimatedonutsmp2.staff.alts` | op | View alternate accounts by shared IP history. |
| `ultimatedonutsmp2.staff.chat.use` | op | Send and receive network staff chat. |
| `ultimatedonutsmp2.staff.chat.mute` | op | Mute global chat. |
| `ultimatedonutsmp2.staff.chat.unmute` | op | Unmute global chat. |
| `ultimatedonutsmp2.staff.chat.delay` | op | Configure the global chat delay. |
| `ultimatedonutsmp2.staff.chat.clear` | op | Clear global chat visually. |
| `ultimatedonutsmp2.staff.chat.bypass.mute` | op | Bypass the global chat mute. |
| `ultimatedonutsmp2.staff.chat.bypass.delay` | op | Bypass the global chat delay. |
| `ultimatedonutsmp2.staff.chat.bypass.filter` | op | Bypass global chat filters. |
| `ultimatedonutsmp2.staff.feed` | op | Restore hunger for yourself or another player. |
| `ultimatedonutsmp2.staff.fly` | op | Toggle flight for yourself or another player. |
| `ultimatedonutsmp2.staff.flyspeed` | op | Adjust flying speed. |
| `ultimatedonutsmp2.staff.freeze` | op | Freeze and unfreeze players. |
| `ultimatedonutsmp2.staff.freeze.alert` | op | Receive freeze moderation alerts. |
| `ultimatedonutsmp2.staff.freeze.exempt` | op | Cannot be frozen by non-admin staff. |
| `ultimatedonutsmp2.staff.gamemode` | op | Change your own game mode. |
| `ultimatedonutsmp2.staff.gamemode.others` | op | Change another online player's game mode. |
| `ultimatedonutsmp2.staff.god` | op | Toggle god mode; also the command gate for `/god`. |
| `ultimatedonutsmp2.staff.heal` | op | Restore health for yourself or another player. |
| `ultimatedonutsmp2.staff.invsee` | op | View other players' inventories. |
| `ultimatedonutsmp2.staff.invsee.modify` | op | Reserved for future editable Invsee sessions. |
| `ultimatedonutsmp2.staff.profileviewer` | op | View player profiles and homes. |
| `ultimatedonutsmp2.staff.rename` | op | Rename held items. |
| `ultimatedonutsmp2.staff.seehomes` | op | Browse any player's homes and teleport to them. |
| `ultimatedonutsmp2.staff.teleport` | op | Access the staff teleport commands. |
| `ultimatedonutsmp2.staff.punishments.create` | op | Parent: create punishment records. Grants `punishments.ban`, `punishments.blacklist`, `punishments.mute`, `staff.vcmute`. |
| `ultimatedonutsmp2.staff.punishments.remove` | op | Parent: remove active punishments. Grants `punishments.unban`, `punishments.unblacklist`, `punishments.unmute`, `staff.vcunmute`. |
| `ultimatedonutsmp2.staff.punishments.view` | op | View punishment history. |
| `ultimatedonutsmp2.staff.punishments.delete` | op | Delete punishment history records from the GUI. |
| `ultimatedonutsmp2.staff.punishments.ban` | false | Issue bans. Declared without a description. |
| `ultimatedonutsmp2.staff.punishments.unban` | false | Remove bans. |
| `ultimatedonutsmp2.staff.punishments.mute` | false | Issue mutes. |
| `ultimatedonutsmp2.staff.punishments.unmute` | false | Remove mutes. |
| `ultimatedonutsmp2.staff.punishments.blacklist` | false | Issue blacklists. |
| `ultimatedonutsmp2.staff.punishments.unblacklist` | false | Remove blacklists. |
| `ultimatedonutsmp2.staff.vcmute` | false | Issue voice chat mutes. |
| `ultimatedonutsmp2.staff.vcunmute` | false | Remove voice chat mutes. |

### `ultimatedonutsmp2.auctionhouse.*`

| Node | Default | Description |
|------|---------|-------------|
| `ultimatedonutsmp2.auctionhouse.use` | true | Open the Auction House menu. |
| `ultimatedonutsmp2.auctionhouse.buy` | true | Buy items from the Auction House. |
| `ultimatedonutsmp2.auctionhouse.sell` | true | Sell items on the Auction House. |
| `ultimatedonutsmp2.auctionhouse.my` | true | View your own listings. |
| `ultimatedonutsmp2.auctionhouse.claims` | true | View and collect claims. |
| `ultimatedonutsmp2.auctionhouse.cancel` | true | Cancel your own listings. |
| `ultimatedonutsmp2.auctionhouse.limit` | true | Check listing limits. |

### `donutauction.*` (legacy aliases)

Every node here mirrors the `ultimatedonutsmp2.auctionhouse.*` node of the same name. The Auction House checks both families, so either one grants the action; the family exists so that permission groups written for the older DonutAuction plugin continue to work.

| Node | Default | Description |
|------|---------|-------------|
| `donutauction.use` | true | Open the Auction House menu (alias). |
| `donutauction.buy` | true | Buy items (alias). |
| `donutauction.sell` | true | Sell items (alias). |
| `donutauction.my` | true | View your own listings (alias). |
| `donutauction.claims` | true | View and collect claims (alias). |
| `donutauction.cancel` | true | Cancel your own listings (alias). |
| `donutauction.limit` | true | Check listing limits (alias). |

### `rank.*`

| Node | Default | Description |
|------|---------|-------------|
| `media` | false | Display the configurable Media tablist badge (default configured node). |
| `rank.media` | false | Display the configurable Media tablist badge (legacy alias). |
| `rank.media.plus` | false | Display the configurable Media+ tablist badge. |
| `rank.media.include` | false | Include the player in media badge handling. |

### Other `ultimatedonutsmp2` nodes

| Node | Default | Description |
|------|---------|-------------|
| `ultimatedonutsmp2.chat.color` | op | Use colour codes and gradients in private messages and staff chat. Granted by `ultimatedonutsmp2.staff.mode`. |
| `ultimatedonutsmp2.enderchest` | true | Open the custom Ender Chest. |
| `ultimatedonutsmp2.friends` | true | Access the friends feature. |
| `ultimatedonutsmp2.helpop` | true | Use `/helpop`. |
| `ultimatedonutsmp2.ignore` | true | Add and remove players from your ignore list. |
| `ultimatedonutsmp2.ignore.bypass` | op | Bypass private-message ignore checks. |
| `ultimatedonutsmp2.message` | true | Send and reply to private messages. |
| `ultimatedonutsmp2.message.toggle` | true | Toggle private message receiving. |
| `ultimatedonutsmp2.message.bypass-disabled` | op | Message a player who has private messages disabled. |
| `ultimatedonutsmp2.report` | true | Report players to staff. |
| `ultimatedonutsmp2.servers` | false | Access the network servers status menu. |
| `ultimatedonutsmp2.shards.everywhere` | false | Receive passive Shards Everywhere rewards. Overridable via `SHARDS.EVERYWHERE.REQUIRED-PERMISSION`. |
| `ultimatedonutsmp2.spawner.bypass` | false | Break spawners without a Silk Touch pickaxe while `REQUIRE_SILK_TOUCH` is enabled. |

### Nodes checked in Java but not declared in `plugin.yml`

These 38 nodes are enforced at runtime but have no declaration, so they have no Bukkit default and will not appear in permission-plugin tab completion. They must be granted explicitly. The five numbered prefixes are listed here in their raw form because they are only ever used as a prefix plus an integer.

| Node | Where it is used |
|------|------------------|
| `anvilmod.admin` | `/amod` |
| `safety.use`, `safety.add`, `safety.reload` | `/safety` (these three are additional to the 38 and likewise undeclared) |
| `donutauction.fastbuy` | Auction House browse menu |
| `donutauction.fastsell` | `/auctionhouse fastsell` |
| `donutfriends.admin` | `/friend reload`, `/friends reload` |
| `donutplus` | Economy placeholder expansion |
| `donutplusplus`, `donutplusplusplus` | Auction "your items" menu |
| `ultimatedonutsmp2.donutplus` | Economy placeholder expansion |
| `ultimatedonutsmp2.donutplusplus`, `ultimatedonutsmp2.donutplusplusplus` | Auction "your items" menu |
| `ultimatedonutsmp.staff.fakeplayer` | `/fakeplayer` — note the legacy prefix without `2` |
| `ultimatedonutsmp.staff.fakeplayer.alert` | Fake player alerts |
| `ultimatedonutsmp.staff.fakeplayer.bypass` | Fake player detection bypass |
| `ultimatedonutsmp.staff.alerts.receive` | Fake player alerts |
| `ultimatedonutsmp2.admin.ecsee.edit` | Editing another player's Ender Chest |
| `ultimatedonutsmp2.admin.sellstats` | `/topsell` |
| `ultimatedonutsmp2.admin.spawnstash` | `/spawnstash remove`, `/spawnstash reload` |
| `ultimatedonutsmp2.admin.teleportareas.delete` | Teleport area menu |
| `ultimatedonutsmp2.auctionhouse.fastbuy` | Auction House browse menu |
| `ultimatedonutsmp2.auctionhouse.fastsell` | `/auctionhouse fastsell` |
| `ultimatedonutsmp2.auctionhouse.limit.<N>` | Auction House listing limit tier |
| `ultimatedonutsmp2.command.setafk` | `/setafk` and `/afk set` |
| `ultimatedonutsmp2.command.setspawn` | `/setspawn` and `/spawn set` |
| `ultimatedonutsmp2.enderchest.rows.<N>` | Ender Chest row tier |
| `ultimatedonutsmp2.hide.scramble` | `/hide scramble` |
| `ultimatedonutsmp2.hide.disguise` | `/disguise <alias>` |
| `ultimatedonutsmp2.hide.admin` | Administering other players' hide states |
| `ultimatedonutsmp2.hide.bypass` | Seeing through hidden identities |
| `ultimatedonutsmp2.homes.page.<N>` | Home page tier |
| `ultimatedonutsmp2.player.fly` | Player-facing flight; configurable via `FLY-SYSTEM.PLAYER-FLY-PERMISSION` |
| `ultimatedonutsmp2.preventdrop.bypass` | Item drop prevention; configurable via `PREVENT-ITEM-DROP.BYPASS-PERMISSION` |
| `ultimatedonutsmp2.rtp.cooldown.<seconds>` | RTP cooldown tier |
| `ultimatedonutsmp2.rtp.priority.<N>` | RTP queue priority tier |
| `ultimatedonutsmp2.staff.punishments.offend` | `/offend` — this is the command's gate, declared on the command but not in the permissions block |
| `ultimatedonutsmp2.staff.spawnstash` | `/spawnstash` |
| `ultimatedonutsmp2.staff.spawnstash.alert` | SpawnStash trip alerts |
| `ultimatedonutsmp2.staff.spawnstash.bypass` | Not triggering a SpawnStash |
| `ultimatedonutsmp2.updatechecker` | Receiving update notices on join, alongside `ultimatedonutsmp2.admin` and operator status |

`ultimatedonutsmp2.homes.<N>` is also used only as a numbered prefix, though the plain `ultimatedonutsmp2.homes.` string appears in the source as a constant rather than a full node.

## Known inconsistencies

These are real, verified mismatches between `plugin.yml` and the Java source. They are documented here so that administrators building permission groups are not surprised by them; none of them prevents the plugin from starting.

**`/billford` does nothing.** The command is declared in `plugin.yml` with the description "Open Billford trade" and the permission `ultimatedonutsmp2.command.billford` (default `true`), and the node is also a child of `ultimatedonutsmp2.command.*`. There is no executor registered for it anywhere in the source. The Billford system itself exists in the language files, `sounds.yml` and the `BILLFORD_REWARD` economy reason, but the command is not wired to it, so running `/billford` produces Bukkit's default "unknown command" style response rather than a menu. Do not advertise it to players.

**`/fakeplayer` checks a legacy permission prefix.** The command gate is `ultimatedonutsmp2.command.fakeplayer`, but `FakePlayerManager` checks `ultimatedonutsmp.staff.fakeplayer` — the older namespace without the trailing `2`. The related alert and bypass nodes are `ultimatedonutsmp.staff.fakeplayer.alert` and `ultimatedonutsmp.staff.fakeplayer.bypass`, and fake player alerts are also delivered to holders of `ultimatedonutsmp.staff.alerts.receive`. Staff who need `/fakeplayer` therefore require nodes from both namespaces, and `ultimatedonutsmp2.*` wildcards will not cover them.

**`/spawnstash` command node and runtime node differ.** The command gate is `ultimatedonutsmp2.command.spawnstash` (default `op`), but every action inside the command checks `ultimatedonutsmp2.staff.spawnstash` for ordinary use and `ultimatedonutsmp2.admin.spawnstash` for `remove` and `reload`. Neither runtime node is declared in `plugin.yml`, and `ultimatedonutsmp2.admin.spawnstash` is not a child of `ultimatedonutsmp2.admin`. Grant all three explicitly.

**`/god` has no `command.` node.** Unlike every other staff command, `/god` is gated directly by `ultimatedonutsmp2.staff.god` in `plugin.yml`. There is no `ultimatedonutsmp2.command.god`, so `ultimatedonutsmp2.command.*` does not grant it; `ultimatedonutsmp2.staff.mode` does, because `staff.god` is one of its children.

**`/setspawn`, `/setafk` and `/offend` gates are undeclared.** These three commands name `ultimatedonutsmp2.command.setspawn`, `ultimatedonutsmp2.command.setafk` and `ultimatedonutsmp2.staff.punishments.offend` as their `permission:` value, but those nodes are absent from the `permissions:` block. As undeclared nodes they have no default and are not children of `ultimatedonutsmp2.command.*`, so operators pass via operator status while non-operator staff need an explicit grant. Note that `/spawn set` and `/afk set` accept `ultimatedonutsmp2.admin.setup` as an alternative, which *is* declared.

**The `donutauction.*` alias family is fully live.** All seven Auction House actions accept either `ultimatedonutsmp2.auctionhouse.<action>` or `donutauction.<action>`, and both families are declared with default `true`. The listing limit resolver additionally reads `donutauction.limit.<N>`. If you intend to restrict the Auction House, you must negate both families; revoking only the `ultimatedonutsmp2.` nodes leaves the aliases granting access.

**Some subcommands are absent from the declared usage strings.** `/auctionhouse` implements `limit`, `fastbuy` and `fastsell`; `/topsell` implements `web`, `html` and `reset`; `/pvp` implements `sync`; `/crate` implements `listbound`; `/ultimatedonutsmp2` implements `setup`, `features` and `maintenance` and accepts `optimization` as an alias of `optimize`. The `usage:` strings in `plugin.yml` predate these additions, so `/help`-style output derived from `plugin.yml` will understate what the commands can do.

**`/sellmultiplier` reuses another command's node.** It is declared as its own command but is gated by `ultimatedonutsmp2.command.sellmulti`, not a `command.sellmultiplier` node. There is no such node.

**`/safety` uses a bare namespace.** Its runtime checks are `safety.use`, `safety.add` and `safety.reload`, outside the plugin's own namespace and undeclared in `plugin.yml`.

**Twenty-three declared nodes never appear in Java.** Besides the `ultimatedonutsmp2.command.*` family, which Bukkit enforces from `plugin.yml` and which therefore does not need string literals, several feature nodes are declared but not referenced in the source: `ultimatedonutsmp2.admin.ecsee`, `ultimatedonutsmp2.staff.freeze`, `ultimatedonutsmp2.staff.freeze.alert`, `ultimatedonutsmp2.staff.freeze.exempt`, `ultimatedonutsmp2.staff.invsee.modify`, `ultimatedonutsmp2.staff.mode` and its six `mode.*` children, `ultimatedonutsmp2.enderchest`, and the `auctionhouse`/`donutauction` `cancel`, `claims`, `limit`, `my` and `sell` nodes, which are constructed dynamically as `"ultimatedonutsmp2.auctionhouse." + action` rather than written out. In the Auction House case the nodes do work; in the freeze and staff-mode cases they are consumed by their respective managers through parent relationships and hotbar wiring rather than direct literal checks, so treat them as advisory until you have tested your own group setup.

**`/sellhand [amount]` ignores the quantity argument.** `plugin.yml` advertises `/sellhand [amount]` and tab completion suggests quantity values, but the command executor always sells the complete item stack held in your main hand.

