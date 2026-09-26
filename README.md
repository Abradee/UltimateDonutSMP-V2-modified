<p align="center">
  <img src="images/mainn.png" alt="UltimateDonutSmp V2" width="720">
</p>

<h1 align="center">UltimateDonutSmp V2</h1>

<p align="center">
  <b>Modified for kepzy, by abradee.</b>
  Free Paper, Purpur, Pufferfish, Spigot, and Folia plugin for DonutSMP-style Minecraft servers.
  Economy, PvP, marketplace, staff tools, menus, and network utilities in one production-focused plugin.
</p>

<p align="center">
  <img alt="Java 21" src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white">
  <img alt="Platform" src="https://img.shields.io/badge/Platform-Paper%20%7C%20Purpur%20%7C%20Pufferfish%20%7C%20Spigot%20%7C%20Folia-2ea44f?style=for-the-badge">
  <img alt="Free" src="https://img.shields.io/badge/Distribution-Free-green?style=for-the-badge">
  <img alt="License" src="https://img.shields.io/badge/License-Proprietary-red?style=for-the-badge">
</p>

## Overview

UltimateDonutSmp V2 is a complete Paper Minecraft server plugin built for DonutSMP-style survival networks. It combines player economy, teams, homes, warps, random teleport, shop, sell, worth, crates, shards, PvP systems, staff utilities, network communication, and GUI-driven workflows into one plugin.

The goal is to reduce the number of separate plugins required for a modern SMP server while keeping configuration, player data, permissions, placeholders, and staff operations consistent across the entire server experience.

## Documentation

This README is the quick reference. The full documentation set lives in [`docs/wiki/`](docs/wiki):

| Page | Contents |
| --- | --- |
| [Home](docs/wiki/Home.md) | Documentation index and technical quick facts |
| [Installation & Setup](docs/wiki/Installation-and-Setup.md) | Server engine setup, SQLite/MySQL/MongoDB storage, and Redis networking |
| [Commands & Permissions](docs/wiki/Commands-and-Permissions.md) | Full command syntax, aliases, and permission nodes |
| [Configuration Reference](docs/wiki/Configuration-Reference.md) | In-depth setup guidance for the larger config files, alongside the per-file `Config-*.yml.md` guides |
| [Economy & Marketplaces](docs/wiki/Economy-and-Marketplaces.md) | Money, shards, shop, sell, Auction House, and Orders |
| [Duels & FFA](docs/wiki/Duels-and-FFA.md) | Duel arenas, queues, rollbacks, and instanced FFA |
| [Ranked PvP Arena](docs/wiki/Ranked-PvP-Arena.md) | Persistent arena, Elo rating, kit editor, and schematic reset |
| [Crates & Spawners](docs/wiki/Crates-and-Spawners.md) | Crate definitions, keys, and Donut-style spawners |
| [Cuboids & Portals](docs/wiki/Cuboids-and-Portals.md) | Region selection, feature zone binding, and portal triggers |
| [Staff & Security](docs/wiki/Staff-and-Security.md) | Staff mode, punishments, detection tools, and moderation |
| [Placeholders & Integrations](docs/wiki/Placeholders-and-Integrations.md) | PlaceholderAPI expansions and third-party plugin support |
| [Localization & Messages](docs/wiki/Localization-and-Messages.md) | 8-language translations, language selection, and message keys |
| [FAQ](docs/wiki/FAQ.md) | Common questions and troubleshooting |
| [Dialog API & older clients](docs/wiki/Dialog-API-and-Older-Clients.md) | Why 1.21.5-and-older Java clients cannot use the menus |

## Highlights

| Area | Included systems |
| --- | --- |
| Platforms | Separate Paper/Purpur/Pufferfish/Spigot and Folia builds with compatibility checks against the latest published APIs |
| Economy | Money, shards, player payments, Vault provider, shop, sell workflows, sell multipliers, worth browser, and sell history |
| Marketplaces | Auction House, Orders board, category filters, claims, delivery, and search |
| Player systems | Teams, friends/follows, homes, warps, private messages, ignore lists, profiles, settings, and custom Ender Chests |
| Progression | Stats, playtime, leaderboards, scoreboards, tablists, bounties, and PlaceholderAPI expansions |
| Teleportation | Spawn, AFK areas, TPA, RTP, portals, cuboid triggers, teleport areas, and safe-location checks |
| PvP | Duels, private invites, map queues, FFA instances, arena rollback, fast crystals, and combat handling |
| Custom content | Crates, virtual keys, Donut-style spawners, amethyst tools, enchantment GUI, filters, and configurable menus |
| Staff and moderation | Staff mode, freeze, vanish, hide/disguise, invsee, ecsee, punishments, alts, reports, helpop, and anvil moderation |
| Detection tools | Spawn-stash bait, fake-player bait, alerts, bypass permissions, and crash protection |
| Network | Redis staff chat and alerts, server-status menus, maintenance routing, Discord webhooks, and Lunar/Apollo support |
| Operations | Automatic configuration sync and backups, feature toggles, setup tools, optimization controls, stats wipe, and guarded server wipe |
| Localization | English, Spanish, Indonesian, Portuguese, German, French, Russian, and Simplified Chinese language packs |

## Screenshots

Feature comparisons (UltimateDonutSMP V2 vs Original DonutSMP):

| UltimateDonutSMP V2 | Original DonutSMP |
| :---: | :---: |
| <img src="images/1.png" alt="TAB - UltimateDonutSMP V2" width="420"> | <img src="images/2.png" alt="TAB - Original DonutSMP" width="420"> |
| <img src="images/3.png" alt="Scoreboards - UltimateDonutSMP V2" width="420"> | <img src="images/4.png" alt="Scoreboards - Original DonutSMP" width="420"> |
| <img src="images/5.png" alt="Dialogues - UltimateDonutSMP V2" width="420"> | <img src="images/6.png" alt="Dialogues - Original DonutSMP" width="420"> |
| <img src="images/7.png" alt="Auction House - UltimateDonutSMP V2" width="420"> | <img src="images/8.png" alt="Auction House - Original DonutSMP" width="420"> |
| <img src="images/9.png" alt="Bounty - UltimateDonutSMP V2" width="420"> | <img src="images/10.png" alt="Bounty - Original DonutSMP" width="420"> |
| <img src="images/11.png" alt="Friends - UltimateDonutSMP V2" width="420"> | <img src="images/12.png" alt="Friends - Original DonutSMP" width="420"> |
| <img src="images/13.png" alt="Homes - UltimateDonutSMP V2" width="420"> | <img src="images/14.png" alt="Homes - Original DonutSMP" width="420"> |
| <img src="images/15.png" alt="Leaderboards - UltimateDonutSMP V2" width="420"> | <img src="images/16.png" alt="Leaderboards - Original DonutSMP" width="420"> |
| <img src="images/17.png" alt="Orders - UltimateDonutSMP V2" width="420"> | <img src="images/18.png" alt="Orders - Original DonutSMP" width="420"> |
| <img src="images/19.png" alt="Pay - UltimateDonutSMP V2" width="420"> | <img src="images/20.png" alt="Pay - Original DonutSMP" width="420"> |
| <img src="images/21.png" alt="Quick Buy - UltimateDonutSMP V2" width="420"> | <img src="images/22.png" alt="Quick Buy - Original DonutSMP" width="420"> |
| <img src="images/23.png" alt="Rank Menu - UltimateDonutSMP V2" width="420"> | <img src="images/24.png" alt="Rank Menu - Original DonutSMP" width="420"> |
| <img src="images/25.png" alt="RTP Queue - UltimateDonutSMP V2" width="420"> | <img src="images/26.png" alt="RTP Queue - Original DonutSMP" width="420"> |
| <img src="images/27.png" alt="Sell - UltimateDonutSMP V2" width="420"> | <img src="images/28.png" alt="Sell - Original DonutSMP" width="420"> |
| <img src="images/29.png" alt="Settings - UltimateDonutSMP V2" width="420"> | <img src="images/30.png" alt="Settings - Original DonutSMP" width="420"> |
| <img src="images/31.png" alt="Shard Shop - UltimateDonutSMP V2" width="420"> | <img src="images/32.png" alt="Shard Shop - Original DonutSMP" width="420"> |
| <img src="images/33.png" alt="Spawners - UltimateDonutSMP V2" width="420"> | <img src="images/34.png" alt="Spawners - Original DonutSMP" width="420"> |
| <img src="images/35.png" alt="Stats - UltimateDonutSMP V2" width="420"> | <img src="images/36.png" alt="Stats - Original DonutSMP" width="420"> |
| <img src="images/37.png" alt="Teleport - UltimateDonutSMP V2" width="420"> | <img src="images/38.png" alt="Teleport - Original DonutSMP" width="420"> |

Feature panels and in-game menus:

|   |   |   |
| :---: | :---: | :---: |
| <img src="images/uds2.png" alt="UltimateDonutSmp V2 feature panel 2" width="270"> | <img src="images/uds3.png" alt="UltimateDonutSmp V2 feature panel 3" width="270"> | <img src="images/uds4.png" alt="UltimateDonutSmp V2 feature panel 4" width="270"> |
| <img src="images/uds5.png" alt="UltimateDonutSmp V2 feature panel 5" width="270"> | <img src="images/uds6.png" alt="UltimateDonutSmp V2 feature panel 6" width="270"> | <img src="images/uds7.png" alt="UltimateDonutSmp V2 feature panel 7" width="270"> |
| <img src="images/uds8.png" alt="UltimateDonutSmp V2 feature panel 8" width="270"> | <img src="images/uds9.png" alt="UltimateDonutSmp V2 feature panel 9" width="270"> | <img src="images/uds10.png" alt="UltimateDonutSmp V2 feature panel 10" width="270"> |
| <img src="images/uds11.png" alt="UltimateDonutSmp V2 feature panel 11" width="270"> | <img src="images/uds12.png" alt="UltimateDonutSmp V2 feature panel 12" width="270"> | <img src="images/uds13.png" alt="UltimateDonutSmp V2 feature panel 13" width="270"> |
| <img src="images/uds14.png" alt="UltimateDonutSmp V2 feature panel 14" width="270"> | <img src="images/uds15.png" alt="UltimateDonutSmp V2 feature panel 15" width="270"> | <img src="images/uds16.png" alt="UltimateDonutSmp V2 feature panel 16" width="270"> |
| <img src="images/uds17.png" alt="UltimateDonutSmp V2 feature panel 17" width="270"> | <img src="images/uds18.png" alt="UltimateDonutSmp V2 feature panel 18" width="270"> | <img src="images/uds19.png" alt="UltimateDonutSmp V2 feature panel 19" width="270"> |

Gameplay clips:

|   |   |
| :---: | :---: |
| <img src="images/gif1.gif" alt="UltimateDonutSmp V2 gameplay clip 1" width="420"> | <img src="images/gif2.gif" alt="UltimateDonutSmp V2 gameplay clip 2" width="420"> |
| <img src="images/gif3.gif" alt="UltimateDonutSmp V2 gameplay clip 3" width="420"> | <img src="images/gif4.gif" alt="UltimateDonutSmp V2 gameplay clip 4" width="420"> |
| <img src="images/gif5.gif" alt="UltimateDonutSmp V2 gameplay clip 5" width="420"> | <img src="images/gif6.gif" alt="UltimateDonutSmp V2 gameplay clip 6" width="420"> |

## Requirements

| Requirement | Notes |
| --- | --- |
| Plugin version | `1.0` |
| Java | Bytecode targets Java 21. The supported Minecraft versions (26.1.2 and newer) require a Java 25 server runtime. |
| Paper / Purpur / Pufferfish / Spigot | Minecraft `26.1.2` through `26.3` |
| Folia | Minecraft `26.1.2` through `26.2` |
| Java client | **1.21.6 or newer.** 1.21.5 and older cannot render Dialog API menus. Block those versions on the proxy or ViaVersion. See [Dialog API & older clients](docs/wiki/Dialog-API-and-Older-Clients.md). |
| Hard dependencies | PlaceholderAPI and ProtocolLib (declared under `depend` in `plugin.yml`; the plugin will not load without them) |
| Default storage | SQLite, bundled through the shaded JDBC driver |
| Alternative storage | MySQL or MongoDB |
| Optional network layer | Redis for cross-server staff chat, alerts, maintenance, reports, helpop, and server status |
| Build environment | Maven available as `mvn`, internet access, and a JDK 21 or newer toolchain (CI builds on JDK 25) |

Required plugins (the server will not enable UltimateDonutSmp V2 without them):

- PlaceholderAPI
- ProtocolLib

Optional integrations:

- LuckPerms
- Vault
- Apollo
- SkinsRestorer
- Multiverse-Core
- floodgate
- Simple Voice Chat (`voicechat`)

The plugin starts without the optional integrations. Their related permission, economy, client, skin, world, voice chat, and Bedrock features activate only when the corresponding plugin is installed.

> [!WARNING]
> **Minecraft 1.21.5 and older clients cannot use this plugin's menus.** UltimateDonutSMP V2 uses Mojang's native [Dialog API](https://minecraft.wiki/w/Dialog), added in Java Edition 1.21.6. Those screens are drawn by the Minecraft client itself. A 1.21.5 (or older) client — typically joining through ViaVersion or ViaBackwards — shows a broken, unusable layout. Commands such as `/ah`, `/menu`, `/homes`, `/pay`, and `/settings` will not present a working screen.
>
> This is not a plugin bug and cannot be fixed here. Other plugins that use Dialog API behave the same way. The chest-menu fallback in `menus.yml` only applies when the **server** lacks Paper's Dialog API (Spigot, or Paper older than 1.21.6). On the Paper versions this plugin supports, dialogs are sent to every player; there is no per-client switch to chest GUIs.
>
> **Require Java clients 1.21.6 or newer.** Block 1.21.5 and below on Velocity, BungeeCord, or ViaVersion. Full write-up: [Dialog API & older clients](docs/wiki/Dialog-API-and-Older-Clients.md).

## Building

Build the project using standard Maven or the build script:

```bat
build.bat
```

Or run Maven directly:

```bash
mvn clean package
```

The build compiles the codebase against the target API and packages a single unified JAR that automatically detects and adapts to Paper, Purpur, Pufferfish, Spigot, or Folia at runtime.

Generated artifact is saved to the `target/` directory:

- `UltimateDonutSmp V2-1.0.jar` (shaded JAR)

## Installation

1. Stop the Minecraft server.
2. Place the plugin jar into the server `plugins/` directory.
3. Start the server once so the default configuration files are generated.
4. Configure storage in `database.yml`.
5. Review the core gameplay files such as `config.yml`, `menus.yml`, `shop.yml`, `worth.yml`, `rtp.yml`, and `languages/<locale>.yml`.
6. Restart the server after first setup.

For production networks, MySQL plus Redis is recommended. For a single-server setup, SQLite is usually enough.

> [!WARNING]
> Do not share private customer files, database credentials, Discord tokens, Redis passwords, or other sensitive server data.

## Configuration

| File | Purpose |
| --- | --- |
| `config.yml` | Language selection, feature toggles, locations, portals, chat, AFK, cuboid binds, combat, crystals, shards, tablist, optimization, and general gameplay |
| `languages/*.yml` | Player-facing command, gameplay, moderation, economy, teleport, menu, and system text |
| `death-messages.yml` | Death-message rules and message templates |
| `menus.yml` | Shared GUI layouts for teams, homes, profiles, settings, leaderboards, shops, staff tools, rules, servers, and other menus |
| `dialog.yml` | 1.21.6+ dialog screens: the main menu grid, settings categories, pay, stats, leaderboards, homes, friends, teleport, the RTP queue prompt, and the generated ESC-screen datapack |
| `scoreboard.yml` | Modern and legacy sidebar layouts, title, lines, refresh behavior, and display rules |
| `shop.yml` | Money and shard shop categories, items, prices, permissions, currencies, and command rewards |
| `sounds.yml` | Sound effects for menus, commands, teleportation, shops, shards, boosters, and custom systems |
| `pvp.yml` | Ranked PvP arena, Elo rating, kits, boundary, broadcasts, and schematic reset |
| `rtp.yml` | Random teleport worlds, radius, cooldowns, safety checks, denied worlds, messages, and GUI |
| `worth.yml` | Sell values, worth display, container handling, browser categories, and blocked items |
| `amethyst-tools.yml` | Amethyst tool types, durations, permissions, effects, items, actions, and messages |
| `ender-chest.yml` | Custom Ender Chest size, behavior, ecsee access, layout, and messages |
| `invsee.yml` | Inventory inspection behavior, layout, permissions, and messages |
| `freeze.yml` | Freeze behavior, permissions, alerts, inventory handling, and messages |
| `auction-house.yml` | Listing limits, pricing, claims, restrictions, sorting, categories, and Auction House GUI |
| `orders.yml` | Order limits, pricing, delivery, matching, filters, sorting, Bedrock input, network behavior, and GUI |
| `enchantments.yml` | Enchantment GUI and item-specific enchantment options |
| `filter.yml` | Item category filters used by marketplace and inventory workflows |
| `duels.yml` | Duel maps, world borders, queues, countdowns, cross-server options, arena settings, rules, and GUI |
| `ffa.yml` | FFA queue, arena rules, rollback, player-state handling, and arena definitions |
| `crates.yml` | Crate definitions, keys, rewards, animations, holograms, particles, and settings |
| `spawners.yml` | Donut-style spawner types, drops, storage, and GUI |
| `spawn-stash.yml` | Temporary bait-stash types, detection rules, alerts, cleanup, and messages |
| `network.yml` | Redis network identity, staff chat, reports, helpop, server status, and maintenance routing |
| `staff-mode.yml` | Staff-mode permissions, hotbar items, vanish, better view, staff list, fake players, and menus |
| `hide.yml` | Identity scrambling, aliases, disguises, skins, cooldowns, bypass rules, GUI, and messages |
| `database.yml` | SQLite, MySQL, MongoDB, and Redis connection settings |
| `server-wipe.yml` | Guarded wipe targets, protected worlds, confirmation token lifetime, backups, and messages |
| `discord.yml` | Discord webhook endpoints and event-specific webhook controls |
| `anvil-moderation.yml` | Banned anvil words, punishments, and per-player moderation data |
| `offenses.yml` | Preset offense rules, punishment types, and durations used by `/offend` |

Language files are stored under `languages/`:

- `en_US.yml`
- `es_ES.yml`
- `id_ID.yml`
- `pt_BR.yml`
- `de_DE.yml`
- `fr_FR.yml`
- `ru_RU.yml`
- `zh_CN.yml`

On startup and reload, missing bundled configuration paths are merged into existing files. Existing files are backed up under `config-backups/` before an automatic update. Live crate definitions, duel/FFA arenas, and deployment-specific network server entries are treated as user-managed data and are not restored after removal.

## Commands

Commands can be disabled through their related feature toggle. Arguments in `<angle brackets>` are required; arguments in `[square brackets]` are optional. Every command has a dedicated permission node registered in `plugin.yml`. Most follow the `ultimatedonutsmp2.command.<command>` pattern; a few staff commands use a `ultimatedonutsmp2.staff.*` node instead, as listed below.

| Command | Aliases | Usage | Permission Node |
| --- | --- | --- | --- |
| `/addmoney` | - | `/addmoney <player> <amount>` | `ultimatedonutsmp2.command.addmoney` |
| `/addshards` | - | `/addshards <player> <amount>` | `ultimatedonutsmp2.command.addshards` |
| `/afk` | - | `/afk` | `ultimatedonutsmp2.command.afk` |
| `/alts` | - | `/alts <player>` | `ultimatedonutsmp2.command.alts` |
| `/amethysttool` | - | `/amethysttool give <player> <type> [duration]` or `/amethysttool reload` | `ultimatedonutsmp2.command.amethysttool` |
| `/amod` | - | `/amod <add\|reload>` | `ultimatedonutsmp2.command.amod` |
| `/arena` | `/duelarena` | `/arena <create\|delete\|setpos1\|setpos2\|setreturn\|setdisplay\|enable\|disable\|queue\|list\|reload>` | `ultimatedonutsmp2.command.arena` |
| `/auctionhouse` | `/ah` | `/auctionhouse [sell\|my\|claims\|cancel\|limit\|fastbuy\|fastsell\|reload]` | `ultimatedonutsmp2.command.auctionhouse` |
| `/balance` | `/bal`, `/money` | `/balance [player]` | `ultimatedonutsmp2.command.balance` |
| `/ban` | - | `/ban <player> [reason]` | `ultimatedonutsmp2.command.ban` |
| `/billford` | - | `/billford` | `ultimatedonutsmp2.command.billford` (no Java handler; inactive) |
| `/blacklist` | - | `/blacklist <player> [reason]` | `ultimatedonutsmp2.command.blacklist` |
| `/bounty` | - | `/bounty <add\|set\|info\|list> [player] [amount]` | `ultimatedonutsmp2.command.bounty` |
| `/chat` | - | `/chat <help\|mute\|unmute\|delay\|clear>` | `ultimatedonutsmp2.command.chat` |
| `/chatlog` | - | `/chatlog [player]` | `ultimatedonutsmp2.command.chatlog` |
| `/clearlag` | - | `/clearlag` | `ultimatedonutsmp2.command.clearlag` |
| `/crate` | - | `/crate <create\|delete\|type\|open\|keys\|reload\|key\|take\|set\|keyall\|add\|edit\|remove\|bind\|unbind\|info>` | `ultimatedonutsmp2.command.crate` |
| `/crates` | - | `/crates` | `ultimatedonutsmp2.command.crates` |
| `/create` | - | `/create <invite\|friends> <player> [map]` | `ultimatedonutsmp2.command.create` |
| `/cuboid` | - | `/cuboid <wand\|create\|delete\|list\|setspawn\|delspawn\|bind <cuboid> <spawn\|shard\|rtp-zone\|rtp-queue> <true\|false>\|reload>` | `ultimatedonutsmp2.command.cuboid` |
| `/delhome` | - | `/delhome <name>` | `ultimatedonutsmp2.command.delhome` |
| `/delwarp` | - | `/delwarp <name>` | `ultimatedonutsmp2.command.delwarp` |
| `/discord` | - | `/discord` | `ultimatedonutsmp2.command.discord` |
| `/disguise` | - | `/disguise [player-name\|url]` or `/disguise <alias> <player-name\|url>` | `ultimatedonutsmp2.command.disguise` |
| `/draw` | - | `/draw` | `ultimatedonutsmp2.command.draw` |
| `/duel` | - | `/duel [player\|accept\|deny\|claims\|reload]` | `ultimatedonutsmp2.command.duel` |
| `/ecsee` | - | `/ecsee <player>` | `ultimatedonutsmp2.command.ecsee` |
| `/enderchest` | `/ec` | `/enderchest [reload]` | `ultimatedonutsmp2.command.enderchest` |
| `/fakeplayer` | `/fplayer` | `/fakeplayer` | `ultimatedonutsmp2.command.fakeplayer` |
| `/feed` | - | `/feed [player]` | `ultimatedonutsmp2.command.feed` |
| `/ffa` | - | `/ffa [join\|reload\|arena ...]` | `ultimatedonutsmp2.command.ffa` |
| `/ffaarena` | - | `/ffaarena <create\|delete\|setpos\|setdisplay\|settings\|enable\|disable\|list\|reload>` | `ultimatedonutsmp2.command.ffaarena` |
| `/ffastats` | - | `/ffastats [player]` | `ultimatedonutsmp2.command.ffastats` |
| `/findplayer` | `/fp` | `/findplayer <player>` | `ultimatedonutsmp2.command.findplayer` |
| `/fly` | - | `/fly [player]` | `ultimatedonutsmp2.command.fly` |
| `/flyspeed` | `/fs` | `/flyspeed <1-10> [player]` | `ultimatedonutsmp2.command.flyspeed` |
| `/freeze` | - | `/freeze <player>` or `/freeze reload` | `ultimatedonutsmp2.command.freeze` |
| `/friend` | - | `/friend` | `ultimatedonutsmp2.command.friend` |
| `/friends` | - | `/friends [list\|follow\|remove\|search\|following\|followers\|friends]` | `ultimatedonutsmp2.command.friends` |
| `/gamemode` | `/gm`, `/gmc`, `/gms`, `/gma`, `/gmsp` | `/gamemode <mode> [player]` | `ultimatedonutsmp2.command.gamemode` |
| `/god` | `/godmode` | `/god [player]` | `ultimatedonutsmp2.staff.god` |
| `/heal` | - | `/heal [player]` | `ultimatedonutsmp2.command.heal` |
| `/help` | - | `/help` | `ultimatedonutsmp2.command.help` |
| `/helpop` | - | `/helpop <message>` | `ultimatedonutsmp2.command.helpop` |
| `/hide` | - | `/hide [status\|scramble\|remove\|check <player>\|list]` | `ultimatedonutsmp2.command.hide` |
| `/home` | - | `/home [name]` | `ultimatedonutsmp2.command.home` |
| `/homes` | - | `/homes` | `ultimatedonutsmp2.command.homes` |
| `/ignore` | - | `/ignore <player\|list>` | `ultimatedonutsmp2.command.ignore` |
| `/invsee` | `/inventorysee` | `/invsee <player>` or `/invsee reload` | `ultimatedonutsmp2.command.invsee` |
| `/keys` | - | `/keys` | `ultimatedonutsmp2.command.keys` |
| `/kick` | - | `/kick <player> [reason]` | `ultimatedonutsmp2.command.kick` |
| `/kill` | - | `/kill` | `ultimatedonutsmp2.command.kill` |
| `/leaderboard` | `/lb`, `/top`, `/leaderboards`, `/baltop` | `/leaderboard [type]` | `ultimatedonutsmp2.command.leaderboard` |
| `/leave` | - | `/leave` | `ultimatedonutsmp2.command.leave` |
| `/logs` | - | `/logs` | `ultimatedonutsmp2.command.logs` |
| `/maintenance` | - | `/maintenance <on [duration]\|off\|status\|setlobby [server]>` | `ultimatedonutsmp2.command.maintenance` |
| `/menu` | `/donut`, `/dialog` | `/menu` | `ultimatedonutsmp2.command.menu` |
| `/meta` | `/farmingmeta` | `/meta` | `ultimatedonutsmp2.command.meta` |
| `/msg` | `/message`, `/tell`, `/whisper`, `/w` | `/msg <player> <message>` | `ultimatedonutsmp2.command.msg` |
| `/mute` | - | `/mute <player> [reason]` | `ultimatedonutsmp2.command.mute` |
| `/nightvision` | `/nv` | `/nightvision` | `ultimatedonutsmp2.command.nightvision` |
| `/offend` | - | `/offend <player> <reason> [time]` | `ultimatedonutsmp2.staff.punishments.offend` |
| `/orders` | - | `/orders [my\|collect\|reload\|search query]` | `ultimatedonutsmp2.command.orders` |
| `/pay` | - | `/pay <player> <amount>` | `ultimatedonutsmp2.command.pay` |
| `/phantom` | - | `/phantom` | `ultimatedonutsmp2.command.phantom` |
| `/ping` | - | `/ping [player]` | `ultimatedonutsmp2.command.ping` |
| `/playerunwipe` | `/punwipe`, `/unwipe` | `/playerunwipe <player> [confirm]` | `ultimatedonutsmp2.command.playerunwipe` |
| `/playerwipe` | `/pwipe`, `/wipe` | `/playerwipe <player> [confirm]` | `ultimatedonutsmp2.command.playerwipe` |
| `/playtime` | `/pt` | `/playtime [player]` | `ultimatedonutsmp2.command.playtime` |
| `/pm` | `/togglepm`, `/privatemessages` | `/pm` | `ultimatedonutsmp2.command.pm` |
| `/portalmanager` | - | `/portalmanager <list\|info\|create\|delete\|setcuboid\|setdestination\|setdisplay\|toggle\|setpriority\|sethologramhere>` | `ultimatedonutsmp2.command.portalmanager` |
| `/profileviewer` | `/pv` | `/profileviewer <player>` | `ultimatedonutsmp2.command.profileviewer` |
| `/punishments` | `/phistory` | `/punishments <player>` | `ultimatedonutsmp2.command.punishments` |
| `/pvp` | - | `/pvp [join\|leave\|kit\|stats\|top\|queue\|leaderboard\|history\|assign\|wand\|create\|setspawn\|setspawn2\|setlobby\|setboundary\|schematic\|reset\|reload]` | `ultimatedonutsmp2.command.pvp` |
| `/queue` | - | `/queue [join\|leave] [map]` | `ultimatedonutsmp2.command.queue` |
| `/randomteleport` | `/randomtp` | `/randomteleport` | `ultimatedonutsmp2.command.randomteleport` |
| `/ranks` | `/rank` | `/ranks` | `ultimatedonutsmp2.command.ranks` |
| `/removemoney` | - | `/removemoney <player> <amount>` | `ultimatedonutsmp2.command.removemoney` |
| `/removeshards` | - | `/removeshards <player> <amount>` | `ultimatedonutsmp2.command.removeshards` |
| `/rename` | - | `/rename <name...\|reset>` | `ultimatedonutsmp2.command.rename` |
| `/renamehome` | - | `/renamehome <old> <new>` | `ultimatedonutsmp2.command.renamehome` |
| `/reply` | `/r` | `/reply <message>` | `ultimatedonutsmp2.command.reply` |
| `/report` | - | `/report <player> <reason>` | `ultimatedonutsmp2.command.report` |
| `/rtp` | - | `/rtp [world]` | `ultimatedonutsmp2.command.rtp` |
| `/rtpq` | `/rtpqueue` | `/rtpq [join\|leave]` | `ultimatedonutsmp2.command.rtpq` |
| `/rules` | - | `/rules` | `ultimatedonutsmp2.command.rules` |
| `/safety` | - | `/safety [reload\|add [player]]` | `ultimatedonutsmp2.command.safety` |
| `/seehomes` | `/homesee` | `/seehomes <player>` | `ultimatedonutsmp2.command.seehomes` |
| `/sell` | - | `/sell` | `ultimatedonutsmp2.command.sell` |
| `/sellall` | - | `/sellall` | `ultimatedonutsmp2.command.sellall` |
| `/sellhand` | - | `/sellhand [amount]` | `ultimatedonutsmp2.command.sellhand` |
| `/sellhistory` | - | `/sellhistory` | `ultimatedonutsmp2.command.sellhistory` |
| `/sellmulti` | - | `/sellmulti [category]` | `ultimatedonutsmp2.command.sellmulti` |
| `/sellmultiplier` | - | `/sellmultiplier [category]` | `ultimatedonutsmp2.command.sellmulti` |
| `/sellprogress` | - | `/sellprogress [category]` | `ultimatedonutsmp2.command.sellprogress` |
| `/servers` | - | `/servers` | `ultimatedonutsmp2.command.servers` |
| `/serverwipe` | - | `/serverwipe <preview\|prepare\|confirm\|cancel\|status>` | `ultimatedonutsmp2.command.serverwipe` |
| `/setafk` | - | `/setafk` | `ultimatedonutsmp2.command.setafk` |
| `/sethome` | - | `/sethome [name]` | `ultimatedonutsmp2.command.sethome` |
| `/setmoney` | - | `/setmoney <player> <amount>` | `ultimatedonutsmp2.command.setmoney` |
| `/setshards` | - | `/setshards <player> <amount>` | `ultimatedonutsmp2.command.setshards` |
| `/setspawn` | - | `/setspawn` | `ultimatedonutsmp2.command.setspawn` |
| `/settings` | - | `/settings` | `ultimatedonutsmp2.command.settings` |
| `/setwarp` | - | `/setwarp <name>` | `ultimatedonutsmp2.command.setwarp` |
| `/shardpay` | - | `/shardpay <player> <amount>` | `ultimatedonutsmp2.command.shardpay` |
| `/shards` | - | `/shards [player]` or `/shards everywhere <status\|debug> [player]` | `ultimatedonutsmp2.command.shards` |
| `/shardshop` | - | `/shardshop` | `ultimatedonutsmp2.command.shardshop` |
| `/shop` | - | `/shop [reload]` | `ultimatedonutsmp2.command.shop` |
| `/social` | `/media` | `/social` | `ultimatedonutsmp2.command.social` |
| `/spawn` | - | `/spawn` | `ultimatedonutsmp2.command.spawn` |
| `/spawner` | `/spawners` | `/spawner [give\|info\|panel\|reload\|remove\|split]` | `ultimatedonutsmp2.command.spawner` |
| `/spawnstash` | `/stash` | `/spawnstash [type\|spawn\|list\|remove\|reload]` | `ultimatedonutsmp2.command.spawnstash` |
| `/staffchat` | `/sc` | `/staffchat <message>` | `ultimatedonutsmp2.command.staffchat` |
| `/stafflist` | - | `/stafflist` | `ultimatedonutsmp2.command.stafflist` |
| `/staffmode` | `/staff` | `/staffmode [player\|reload]` | `ultimatedonutsmp2.command.staffmode` |
| `/stats` | - | `/stats [player]` | `ultimatedonutsmp2.command.stats` |
| `/store` | - | `/store` | `ultimatedonutsmp2.command.store` |
| `/team` | - | `/team <create\|disband\|invite\|kick\|join\|leave\|home\|sethome\|delhome\|chat\|info\|pvp>` | `ultimatedonutsmp2.command.team` |
| `/teleport` | `/tp`, `/tphere`, `/tpall` | `/teleport <player\|here <player>\|all\|top\|x y z [world]>` | `ultimatedonutsmp2.command.teleport` |
| `/tempban` | - | `/tempban <player> <time> [reason]` | `ultimatedonutsmp2.command.tempban` |
| `/tempmute` | - | `/tempmute <player> <time> [reason]` | `ultimatedonutsmp2.command.tempmute` |
| `/topsell` | `/sellstats` | `/topsell [gui\|items\|volume\|sellers\|export]` | `ultimatedonutsmp2.command.topsell` |
| `/tpa` | - | `/tpa <player>` | `ultimatedonutsmp2.command.tpa` |
| `/tpacancel` | - | `/tpacancel` | `ultimatedonutsmp2.command.tpacancel` |
| `/tpaccept` | - | `/tpaccept [player]` | `ultimatedonutsmp2.command.tpaccept` |
| `/tpadeny` | - | `/tpadeny [player]` | `ultimatedonutsmp2.command.tpadeny` |
| `/tpahere` | - | `/tpahere <player>` | `ultimatedonutsmp2.command.tpahere` |
| `/tpahereauto` | - | `/tpahereauto` | `ultimatedonutsmp2.command.tpahereauto` |
| `/tpauto` | - | `/tpauto` | `ultimatedonutsmp2.command.tpauto` |
| `/twitter` | - | `/twitter` | `ultimatedonutsmp2.command.twitter` |
| `/ultimatedonutsmp2` | `/uds`, `/udsmp` | `/ultimatedonutsmp2 <reload\|statswipe\|optimize\|setup\|features\|maintenance>` | `ultimatedonutsmp2.command.ultimatedonutsmp2` |
| `/unban` | `/pardon` | `/unban <player> [reason]` | `ultimatedonutsmp2.command.unban` |
| `/unblacklist` | - | `/unblacklist <player> [reason]` | `ultimatedonutsmp2.command.unblacklist` |
| `/unignore` | - | `/unignore <player>` | `ultimatedonutsmp2.command.unignore` |
| `/unmute` | - | `/unmute <player> [reason]` | `ultimatedonutsmp2.command.unmute` |
| `/vanish` | - | `/vanish` | `ultimatedonutsmp2.command.vanish` |
| `/vcmute` | - | `/vcmute <player> [reason]` | `ultimatedonutsmp2.staff.vcmute` |
| `/vcunmute` | - | `/vcunmute <player> [reason]` | `ultimatedonutsmp2.staff.vcunmute` |
| `/voicechatconsent` | `/vcconsent`, `/voiceconsent` | `/voicechatconsent [revoke]` | `ultimatedonutsmp2.command.voicechatconsent` |
| `/warn` | - | `/warn <player> [reason]` | `ultimatedonutsmp2.command.warn` |
| `/warp` | - | `/warp [name]` | `ultimatedonutsmp2.command.warp` |
| `/warpmanager` | - | `/warpmanager <create\|delete\|list> [name]` | `ultimatedonutsmp2.command.warpmanager` |
| `/worth` | `/prices` | `/worth [hand\|reload]` | `ultimatedonutsmp2.command.worth` |

Temporary punishment durations accept combined values such as `30s`, `15m`, `2h`, `5d`, or `5d 15m 30s`.

Running `/baltop` with no arguments opens the money leaderboard directly instead of the leaderboard type menu.

## Permissions

`true` means the permission is granted by default to all players, `op` means it defaults to server operators, and `false` means it must be granted explicitly. `ultimatedonutsmp2.admin` is the main admin parent node, and `ultimatedonutsmp2.command.*` grants access to all plugin commands.

### Main Parent Nodes

| Permission Node | Default | Description |
| --- | --- | --- |
| `ultimatedonutsmp2.admin` | `op` | Main admin parent node giving access to administrative commands, reload, wipe, and management systems |
| `ultimatedonutsmp2.command.*` | `op` | Grants access to execute all UltimateDonutSmp V2 commands |
| `ultimatedonutsmp2.staff.mode` | `op` | Staff moderation mode parent node (vanish, betterview, randomtp, staff list, tools) |
| `ultimatedonutsmp2.staff.alerts.receive` | `op` | Parent node for receiving staff alerts (`helpop` and `report`) |
| `ultimatedonutsmp2.staff.punishments.create` | `op` | Parent node for issuing punishments (`warn`, `kick`, `ban`, `mute`, `blacklist`) |
| `ultimatedonutsmp2.staff.punishments.remove` | `op` | Parent node for removing active punishments (`unban`, `unmute`, `unblacklist`) |

### System & Staff Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `anvilmod.admin` | `op` | Admin access for anvil word moderation |
| `safety.add` | `op` | Add players to safety check bypass list |
| `safety.reload` | `op` | Reload safety configuration |
| `safety.use` | `true` | Standard safety system permission |
| `ultimatedonutsmp2.admin.addmoney` | `op` | Add money to player balances |
| `ultimatedonutsmp2.admin.amethysttool` | `op` | Give and reload amethyst tools |
| `ultimatedonutsmp2.admin.auctionhouse` | `op` | Reload and manage Auction House settings |
| `ultimatedonutsmp2.admin.chatlog` | `op` | Browse logged public chat messages |
| `ultimatedonutsmp2.admin.clearlag` | `op` | Trigger lag clear manually |
| `ultimatedonutsmp2.admin.crate` | `op` | Manage crate balances, chest bindings, and settings |
| `ultimatedonutsmp2.admin.crate.keyall` | `op` | Trigger key-all rewards manually |
| `ultimatedonutsmp2.admin.crate.reload` | `op` | Reload crate configuration |
| `ultimatedonutsmp2.admin.cuboid` | `op` | Create, bind, and manage cuboid regions |
| `ultimatedonutsmp2.admin.delwarp` | `op` | Delete public warp points |
| `ultimatedonutsmp2.admin.duels` | `op` | Manage duel settings and arenas |
| `ultimatedonutsmp2.admin.ecsee` | `op` | View other players' Ender Chest contents |
| `ultimatedonutsmp2.admin.enderchest` | `op` | Reload Ender Chest settings |
| `ultimatedonutsmp2.admin.features` | `op` | Access and toggle runtime feature switches |
| `ultimatedonutsmp2.admin.ffa` | `op` | Manage FFA arenas and settings |
| `ultimatedonutsmp2.admin.freeze` | `op` | Reload freeze settings |
| `ultimatedonutsmp2.admin.invsee` | `op` | Reload Invsee settings |
| `ultimatedonutsmp2.admin.logs` | `op` | View command log history |
| `ultimatedonutsmp2.admin.maintenance` | `op` | Manage maintenance mode |
| `ultimatedonutsmp2.admin.maintenance.bypass` | `op` | Join server while maintenance mode is enabled |
| `ultimatedonutsmp2.admin.optimize` | `op` | Access runtime optimization controls |
| `ultimatedonutsmp2.admin.orders` | `op` | Manage and reload Orders board settings |
| `ultimatedonutsmp2.admin.playerunwipe` | `op` | Preview and restore a wiped player's stored data |
| `ultimatedonutsmp2.admin.playerwipe` | `op` | Preview and wipe a single player's stored data |
| `ultimatedonutsmp2.admin.portalmanager` | `op` | Create, display, and manage RTP portals |
| `ultimatedonutsmp2.admin.punishments.exempt` | `op` | Cannot be punished by staff who lack the exempt bypass |
| `ultimatedonutsmp2.admin.punishments.exempt.bypass` | `op` | Punish players who are exempt from being punished |
| `ultimatedonutsmp2.admin.pvp` | `op` | Manage the ranked PvP arena and its kits |
| `ultimatedonutsmp2.admin.reload` | `op` | Reload all UltimateDonutSmp V2 configurations |
| `ultimatedonutsmp2.admin.removemoney` | `op` | Remove money from player balances |
| `ultimatedonutsmp2.admin.sellstats` | `op` | View top sell statistics and economy metrics |
| `ultimatedonutsmp2.admin.serverwipe` | `op` | Execute guarded server wipe operations |
| `ultimatedonutsmp2.admin.setmoney` | `op` | Set player money balances |
| `ultimatedonutsmp2.admin.setup` | `op` | Use interactive setup status and tools |
| `ultimatedonutsmp2.admin.setwarp` | `op` | Set public warp points |
| `ultimatedonutsmp2.admin.shards` | `op` | Inspect Shards Everywhere status |
| `ultimatedonutsmp2.admin.shop` | `op` | Reload shop settings |
| `ultimatedonutsmp2.admin.spawner` | `op` | Give and manage Donut-style spawners |
| `ultimatedonutsmp2.admin.spawnstash` | `op` | Manage bait spawn stashes |
| `ultimatedonutsmp2.admin.staffmode` | `op` | Reload Staff Mode settings |
| `ultimatedonutsmp2.admin.statswipe` | `op` | Execute player stats wipe |
| `ultimatedonutsmp2.admin.teleportareas.delete` | `op` | Delete configured teleport areas |
| `ultimatedonutsmp2.admin.warpmanager` | `op` | Manage public warps |
| `ultimatedonutsmp2.admin.worth` | `op` | Reload sell/worth settings |
| `ultimatedonutsmp2.command.flyspeed` | `op` | Use `/flyspeed` |
| `ultimatedonutsmp2.command.offend` | `op` | Use `/offend` (parent of `ultimatedonutsmp2.staff.punishments.offend`) |

### Staff Moderation & Alert Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `ultimatedonutsmp2.staff.alerts.bypass-cooldown` | `op` | Bypass report and helpop cooldowns |
| `ultimatedonutsmp2.staff.alerts.receive` | `op` | Receive helpop and report staff alerts |
| `ultimatedonutsmp2.staff.alts` | `op` | Check IP history and alt accounts |
| `ultimatedonutsmp2.staff.chat.bypass.delay` | `op` | Bypass global chat delay |
| `ultimatedonutsmp2.staff.chat.bypass.filter` | `op` | Bypass global chat filter |
| `ultimatedonutsmp2.staff.chat.bypass.mute` | `op` | Bypass global chat mute |
| `ultimatedonutsmp2.staff.chat.clear` | `op` | Clear chat screen visually |
| `ultimatedonutsmp2.staff.chat.delay` | `op` | Set global chat delay |
| `ultimatedonutsmp2.staff.chat.mute` | `op` | Mute global chat |
| `ultimatedonutsmp2.staff.chat.unmute` | `op` | Unmute global chat |
| `ultimatedonutsmp2.staff.chat.use` | `op` | Send and receive staff chat |
| `ultimatedonutsmp2.staff.fakeplayer` | `op` | Spawn fake player bait entities |
| `ultimatedonutsmp2.staff.fakeplayer.alert` | `op` | Receive fake player bait attack alerts |
| `ultimatedonutsmp2.staff.fakeplayer.bypass` | `op` | Bypass fake player bait triggers |
| `ultimatedonutsmp2.staff.feed` | `op` | Feed players |
| `ultimatedonutsmp2.staff.fly` | `op` | Toggle flight mode |
| `ultimatedonutsmp2.staff.flyspeed` | `op` | Adjust flying speed for yourself or another player |
| `ultimatedonutsmp2.staff.freeze` | `op` | Freeze/unfreeze players for inspection |
| `ultimatedonutsmp2.staff.freeze.alert` | `op` | Receive player freeze alerts |
| `ultimatedonutsmp2.staff.freeze.exempt` | `op` | Exempt from being frozen by staff |
| `ultimatedonutsmp2.staff.gamemode` | `op` | Change own gamemode |
| `ultimatedonutsmp2.staff.gamemode.others` | `op` | Change other players' gamemodes |
| `ultimatedonutsmp2.staff.god` | `op` | Toggle god mode |
| `ultimatedonutsmp2.staff.heal` | `op` | Heal players |
| `ultimatedonutsmp2.staff.helpop.receive` | `op` | Receive helpop request alerts |
| `ultimatedonutsmp2.staff.invsee` | `op` | Inspect player inventories |
| `ultimatedonutsmp2.staff.invsee.modify` | `op` | Modify player inventories in invsee |
| `ultimatedonutsmp2.staff.mode.betterview` | `op` | Toggle better view in staff mode |
| `ultimatedonutsmp2.staff.mode.others` | `op` | Toggle staff mode for other players |
| `ultimatedonutsmp2.staff.mode.randomtp` | `op` | Random teleport in staff mode |
| `ultimatedonutsmp2.staff.mode.seevanished` | `op` | See vanished staff members |
| `ultimatedonutsmp2.staff.mode.stafflist` | `op` | Open online staff list |
| `ultimatedonutsmp2.staff.mode.vanish` | `op` | Toggle vanish in staff mode |
| `ultimatedonutsmp2.staff.profileviewer` | `op` | View player profiles and homes |
| `ultimatedonutsmp2.staff.seehomes` | `op` | Browse any player's homes and teleport to them |
| `ultimatedonutsmp2.staff.punishments.ban` | `false` | Apply ban and tempban punishments |
| `ultimatedonutsmp2.staff.punishments.blacklist` | `false` | Apply blacklist punishments |
| `ultimatedonutsmp2.staff.punishments.delete` | `op` | Delete punishment logs from GUI |
| `ultimatedonutsmp2.staff.punishments.mute` | `false` | Apply mute and tempmute punishments |
| `ultimatedonutsmp2.staff.punishments.offend` | `op` | Apply preset offense punishments from `offenses.yml` |
| `ultimatedonutsmp2.staff.punishments.unban` | `false` | Remove active bans |
| `ultimatedonutsmp2.staff.punishments.unblacklist` | `false` | Remove active blacklists |
| `ultimatedonutsmp2.staff.punishments.unmute` | `false` | Remove active mutes |
| `ultimatedonutsmp2.staff.punishments.view` | `op` | View punishment history |
| `ultimatedonutsmp2.staff.rename` | `op` | Rename items |
| `ultimatedonutsmp2.staff.report.receive` | `op` | Receive player report alerts |
| `ultimatedonutsmp2.staff.spawnstash` | `op` | Place and inspect spawn stash bait |
| `ultimatedonutsmp2.staff.spawnstash.alert` | `op` | Receive spawn stash trigger alerts |
| `ultimatedonutsmp2.staff.spawnstash.bypass` | `op` | Bypass spawn stash detection |
| `ultimatedonutsmp2.staff.teleport` | `op` | Access staff teleport tools |
| `ultimatedonutsmp2.staff.vcmute` | `false` | Apply voice chat mute punishments |
| `ultimatedonutsmp2.staff.vcunmute` | `false` | Remove voice chat mute punishments |

### Player & Feature Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `donutauction.use` / `ultimatedonutsmp2.auctionhouse.use` | `true` / `false` | Open Auction House GUI |
| `donutauction.buy` / `ultimatedonutsmp2.auctionhouse.buy` | `true` / `false` | Buy items on Auction House |
| `donutauction.sell` / `ultimatedonutsmp2.auctionhouse.sell` | `true` / `false` | Sell items on Auction House |
| `donutauction.my` / `ultimatedonutsmp2.auctionhouse.my` | `true` / `false` | View own listings on Auction House |
| `donutauction.claims` / `ultimatedonutsmp2.auctionhouse.claims` | `true` / `false` | Collect claims from Auction House |
| `donutauction.cancel` / `ultimatedonutsmp2.auctionhouse.cancel` | `true` / `false` | Cancel own listings on Auction House |
| `donutauction.limit` / `ultimatedonutsmp2.auctionhouse.limit` | `true` / `false` | Check listing limits on Auction House |
| `donutauction.fastbuy` / `ultimatedonutsmp2.auctionhouse.fastbuy` | `false` / `op` | Fast buy command access |
| `donutauction.fastsell` / `ultimatedonutsmp2.auctionhouse.fastsell` | `false` / `op` | Fast sell command access |
| `ultimatedonutsmp2.command.friend` | `true` | Use `/friend` |
| `ultimatedonutsmp2.command.sellmulti` | `true` | Open the sell multiplier menu (`/sellmulti`, `/sellmultiplier`) |
| `ultimatedonutsmp2.command.sellprogress` | `true` | Open the sell multiplier progress menu (`/sellprogress`) |
| `ultimatedonutsmp2.enderchest` | `true` | Open custom Ender Chest |
| `ultimatedonutsmp2.friends` | `true` | Friends and follow system |
| `ultimatedonutsmp2.helpop` | `true` | Use `/helpop` to request staff help |
| `ultimatedonutsmp2.hide.admin` | `op` | Manage and inspect player disguises |
| `ultimatedonutsmp2.hide.bypass` | `op` | Bypass disguise restrictions |
| `ultimatedonutsmp2.hide.disguise` | `op` | Change skin and disguise alias |
| `ultimatedonutsmp2.hide.scramble` | `op` | Scramble public username |
| `ultimatedonutsmp2.ignore` | `true` | Ignore and unignore players |
| `ultimatedonutsmp2.ignore.bypass` | `op` | Bypass private message ignore filter |
| `ultimatedonutsmp2.message` | `true` | Send and reply to private messages |
| `ultimatedonutsmp2.message.bypass-disabled` | `op` | Bypass recipient disabled PMs |
| `ultimatedonutsmp2.message.toggle` | `true` | Toggle private messages on/off |
| `ultimatedonutsmp2.report` | `true` | Report players to online staff |
| `ultimatedonutsmp2.servers` | `false` | View network server status GUI |
| `ultimatedonutsmp2.shards.everywhere` | `false` | Receive passive Shards Everywhere rewards |
| `ultimatedonutsmp2.shardshop` | `true` | Open Shard Shop GUI |
| `ultimatedonutsmp2.spawner.bypass` | `false` | Break spawners without a Silk Touch pickaxe while `REQUIRE_SILK_TOUCH` is enabled |
| `media` | `false` | Display configurable Media tablist badge (requires explicit LuckPerms assignment, not auto-granted to OP) |
| `rank.media` | `false` | Display configurable Media tablist badge legacy alias |
| `rank.media.plus` | `false` | Display configurable Media+ tablist badge (requires explicit LuckPerms assignment, not auto-granted to OP) |
| `rank.media.include` | `false` | Include player in media badge handling (requires explicit LuckPerms assignment, not auto-granted to OP) |

## Placeholders

UltimateDonutSmp V2 includes built-in [PlaceholderAPI](https://placeholderapi.com/) expansion modules for player economy, statistics, locations, countdowns, leaderboards, player ranks, and disguise states.

Placeholder expansion identifiers supported: `%economy_*%`, `%uds_*%`, `%ultimatedonutsmp2_*%`, `%economylb_*%`, `%economyrank_*%`, and `%hide_*%`.

### Economy & Player Placeholders (`%economy_*%` / `%uds_*%` / `%ultimatedonutsmp2_*%`)

| Placeholder | Description | Example Output |
| --- | --- | --- |
| `%economy_money%` | Raw money balance | `12500.50` |
| `%economy_money_formatted%` | Formatted money with currency symbol | `$12,500.50` |
| `%economy_money_short%` / `%economy_nicestMoney%` | Compact formatted money amount | `12.5k` |
| `%economy_money_short_formatted%` | Formatted compact money with symbol | `$12.5k` |
| `%economy_shards%` | Raw shards balance | `500` |
| `%economy_shards_formatted%` | Formatted shards balance | `500 Shards` |
| `%economy_shards_short%` / `%economy_nicestShards%` | Compact formatted shards amount | `1.2k` |
| `%economy_shards_short_formatted%` | Formatted compact shards with symbol | `1.2k Shards` |
| `%economy_kills%` | Total player kill count | `42` |
| `%economy_deaths%` | Total player death count | `10` |
| `%economy_killstreak%` | Current active killstreak | `5` |
| `%economy_highestkillstreak%` | Highest recorded killstreak | `12` |
| `%economy_playtime%` | Total formatted playtime duration | `3d 14h 22m` |
| `%economy_blocksplaced%` | Total blocks placed count | `15400` |
| `%economy_blocksbroken%` | Total blocks broken count | `48200` |
| `%economy_mobskilled%` | Total mob kills count | `1280` |
| `%economy_moneyspent%` | Total money spent in shop/marketplaces | `50000.00` |
| `%economy_moneymade%` | Total money earned from selling/markets | `120000.00` |
| `%economy_team%` | Player's team name | `TITANS` or `none` |
| `%economy_username%` | Public display username (respects `/hide` disguise) | `Steve` |
| `%economy_ping%` | Player ping latency in ms | `24` |
| `%economy_x%` / `%economy_coord_x%` | Player X coordinate (respects coordinate obfuscation) | `120` |
| `%economy_y%` / `%economy_coord_y%` | Player Y coordinate (respects coordinate obfuscation) | `64` |
| `%economy_z%` / `%economy_coord_z%` | Player Z coordinate (respects coordinate obfuscation) | `-350` |
| `%economy_coords%` / `%economy_location%` | Formatted X, Y, Z coordinates string | `120, 64, -350` |
| `%economy_randomized_coords%` | Returns `true`/`false` if coordinate obfuscation is active | `false` |
| `%economy_donutplus%` | Displays Donut+ badge if player has permission | `&d&lDonut+ &r` |
| `%economy_keyall_countdown%` | Formatted countdown until automatic key-all reward | `05:32` |
| `%economy_booster_countdown%` | Formatted countdown for active shard booster | `14:20` or `inactive` |
| `%economy_rtp_countdown%` | Formatted countdown for RTP zone cooldown | `00:45` or `disabled` |
| `%economy_billford_countdown%` | Formatted countdown for Billford rotation | `02:15:00` or `disabled` |
| `%economy_shard_cuboid_display%` | Shard cuboid display status indicator | `[Inside Zone]` |
| `%economy_shard_cuboid_status%` | Shard cuboid status | `inside` or `outside` |
| `%economy_shard_cuboid_name%` | Name of active shard cuboid region | `MainShardArea` or `none` |
| `%economy_money_symbol%` | Currency symbol for money | `$` |
| `%economy_money_symbol_colored%` | Colored currency symbol for money | `&$` |
| `%economy_shards_symbol%` | Currency symbol for shards | `⬟` |
| `%economy_shards_symbol_colored%` | Colored currency symbol for shards | `&d⬟` |

### Leaderboard Placeholders (`%economylb_*%` / `%economy_top_*%`)

Syntax: `%economylb_<type>_<position>_<property>%` or `%economy_top_<type>_<position>_<property>%`

- **Leaderboard Types (`<type>`)**: `money`, `shards`, `kills`, `deaths`, `killstreak`, `highestkillstreak`, `playtime`, `blocksplaced`, `blocksbroken`, `mobskilled`, `moneyspent`, `moneymade`
- **Positions (`<position>`)**: Rank index starting from `1` (e.g. `1`, `2`, `3`, `10`)
- **Properties (`<property>`)**:
  - `name`: Username of the player at position
  - `value`: Full un-truncated value of the player at position
  - `value_short` / `short`: Compact formatted value (e.g. `15.4M`)
  - `rank`: Rank number index
  - `display`: Pre-formatted entry line e.g. `#1 Notch: $15.4M`

Examples:

```
%economylb_money_1_name%       -> Notch
%economylb_money_1_value%      -> $15,400,000.00
%economylb_money_1_value_short% -> 15.4M
%economylb_kills_3_display%     -> #3 Alex: 450
%economy_top_shards_1_name%    -> EnderKing
```

### Rank Leaderboard Placeholders (`%economyrank_*%`)

Syntax: `%economyrank_<type>%`

Returns the player's personal rank position number on the specified leaderboard type (e.g. `1`, `15`, or `0` if unranked).

Examples:
- `%economyrank_money%` -> `5`
- `%economyrank_kills%` -> `1`

### Hide & Disguise Placeholders (`%hide_*%`)

Syntax: `%hide_<property>%`

| Placeholder | Description | Example Output |
| --- | --- | --- |
| `%hide_active%` | Returns `true` or `false` if player has an active disguise or alias | `true` |
| `%hide_name%` / `%hide_public_name%` | Player's formatted public display name | `ShadowNinja` |
| `%hide_plain_name%` | Player's plain unformatted public display name | `ShadowNinja` |
| `%hide_mode%` | Active disguise mode (`NONE`, `SCRAMBLE`, `ALIAS`, `DISGUISE`) | `ALIAS` |
| `%hide_alias%` | Active custom alias string | `ShadowNinja` |
| `%hide_skin%` | Active custom skin username | `CustomSkin123` |

## License and Terms

UltimateDonutSmp V2 is free, proprietary software.

- The plugin is free to use but remains under a proprietary license.
- Redistribution, resale, sublicensing, public mirroring, or unauthorized sharing is not permitted without written permission.
- You may modify the source for use on your own server; modified builds may not be distributed.
- The shaded jar bundles third-party libraries under their own licenses (Apache 2.0, MIT, and GPLv2 with the Universal FOSS Exception).
- For full licensing terms, see [LICENSE.md](LICENSE.md).
- For contribution guidelines and rules, see [CONTRIBUTING.md](CONTRIBUTING.md).

Copyright (c) 2026 UltimateDonutSmp V2. All rights reserved.

## Support

| Channel | Use it for |
| --- | --- |
| [GitHub Issues](https://github.com/BeestoXd/UltimateDonutSMP-V2/issues) | Bug reports, feature requests, and documentation problems |
| [Discord](https://dsc.gg/hellstarr) | Setup help, configuration questions, and general discussion |
| [`docs/wiki/`](docs/wiki) | Guides, configuration reference, and the FAQ |

When reporting an issue, include:

- Plugin version and jar file name
- Server software and version (Paper, Purpur, Pufferfish, Spigot, or Folia)
- Java version
- Relevant configuration snippets with secrets removed
- Console errors or stack traces
- Steps to reproduce the issue

Do not share database credentials, Redis passwords, Discord webhook URLs, or other sensitive server data in public channels.
