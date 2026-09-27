# Placeholders & Integrations

PlaceholderAPI is required. Expansions register on startup — nothing to download from eCloud.

`%economy_money%`, `%uds_money%` and `%ultimatedonutsmp2_money%` are the same placeholder.

Jump: [list](#expansions-at-a-glance) · [economy](#economy--currency-and-player-data) ·
[leaderboards](#leaderboards) · [pvp](#pvp--ranked-arena) · [hide](#hide--disguises) ·
[internal `{tokens}`](#internal-tokens) · [other plugins](#third-party-integrations) ·
[Vault](#vault-economy)

---

## Expansions at a glance

| Identifier | Class | What it covers |
| :--- | :--- | :--- |
| `economy` | `EconomyExpansion` | Balances, shards, stats, timers, coordinates, player settings, leaderboards |
| `ultimatedonutsmp2` | `UltimateDonutSmp2Expansion` | Alias of `economy` — identical parameters |
| `uds` | `UdsExpansion` | Alias of `economy` — identical parameters |
| `economylb` | `EconomyLeaderboardExpansion` | Leaderboard positions |
| `economyrank` | `EconomyRankExpansion` | The requesting player's own leaderboard position |
| `pvp` | `PvpExpansion` | Ranked PvP arena stats — see [Ranked PvP Arena](Ranked-PvP-Arena) |
| `hide` | `HideExpansion` | Disguise state — see [Staff & Security](Staff-and-Security) |

`economy`, `ultimatedonutsmp2` and `uds` are three names for one handler. Everything documented
under `%economy_…%` below works identically as `%uds_…%` and `%ultimatedonutsmp2_…%`; pick
whichever reads best in your configuration and stay consistent.

---

## `economy` — currency and player data

### Balances

| Placeholder | Returns |
| :--- | :--- |
| `%economy_money%` | Raw money balance |
| `%economy_money_formatted%` | Money with the configured symbol and colour |
| `%economy_nicestMoney%` | Compact money, e.g. `1.5K` |
| `%economy_money_short_formatted%` | Compact money, formatted with symbol |
| `%economy_shards%` | Raw shard count |
| `%economy_shards_formatted%` | Shards with symbol and colour |
| `%economy_nicestShards%` | Compact shard count |
| `%economy_shards_short_formatted%` | Compact shards, formatted |

Each of these has case-insensitive and hyphenated aliases, so the following all resolve to the
same value: `%economy_nicestMoney%`, `%economy_nicestmoney%`, `%economy_money_short%`,
`%economy_money_amount_short%`. The same pattern applies to shards, and `%economy_money-formatted%`
works alongside `%economy_money_formatted%`.

### Currency metadata

These describe the currency itself and work without a player, which makes them safe in static
menu text and console-rendered output.

| Money | Shards | Returns |
| :--- | :--- | :--- |
| `%economy_money_symbol%` | `%economy_shards_symbol%` | Symbol character |
| `%economy_money_symbol_color%` | `%economy_shards_symbol_color%` | Symbol colour code |
| `%economy_money_symbol_colored%` | `%economy_shards_symbol_colored%` | Symbol with its colour applied |
| `%economy_money_color%` | `%economy_shards_color%` | Colour used for the amount |
| `%economy_money_name%` | `%economy_shards_name%` | Singular name |
| `%economy_money_name_plural%` | `%economy_shards_name_plural%` | Plural name |

### Statistics

| Placeholder | Returns |
| :--- | :--- |
| `%economy_kills%` | Kills |
| `%economy_deaths%` | Deaths |
| `%economy_playtime%` | Formatted total playtime |
| `%economy_killStreak%` | Current kill streak |
| `%economy_highestKillStreak%` | Best kill streak |
| `%economy_blocksPlaced%` | Blocks placed |
| `%economy_blocksBroken%` | Blocks broken |
| `%economy_mobsKilled%` | Mobs killed |
| `%economy_moneySpent%` | Lifetime money spent |
| `%economy_moneyMade%` | Lifetime money earned |

Every camel-case name above also accepts an all-lowercase form, for example
`%economy_highestkillstreak%`.

### Identity, connection and timers

| Placeholder | Returns |
| :--- | :--- |
| `%economy_username%` | Public display name, respecting an active disguise; `unknown` with no player |
| `%economy_ping%` | Latency in milliseconds; `0` when offline |
| `%economy_team%` | Team name in upper case, or `none` |
| `%economy_donutplus%` | The Donut+ prefix when the player has the permission and the setting on; otherwise empty |
| `%economy_keyall_countdown%` | Time until the next Key-All |
| `%economy_booster_countdown%` | Remaining shard booster time, or `inactive` |
| `%economy_rtp_countdown%` | RTP zone countdown, or `disabled` when the RTP zone is off |
| `%economy_amethyst_pending%` | Number of amethyst tools waiting to be delivered on join |
| `%economy_last_online%` | `online`, or last-seen age as `d`/`h`/`m` (for example `2d 3h`) |
| `%economy_shard_cuboid_display%` | Shard cuboid HUD line; `-` when offline |
| `%economy_shard_cuboid_status%` | `inside` or `outside` |
| `%economy_shard_cuboid_name%` | Active shard cuboid name, or `none` |

`%economy_display_donutplus%` is an alias of `%economy_donutplus%`.

### Coordinates

| Placeholder | Returns |
| :--- | :--- |
| `%economy_x%` / `%economy_y%` / `%economy_z%` | Individual block coordinates |
| `%economy_coords%` | Formatted `X, Y, Z` |
| `%economy_randomized_coords%` | `true` or `false` |

`%economy_coord_x%` and `%economy_coords_x%` are aliases of `%economy_x%` (and likewise for Y and
Z); `%economy_location%` and `%economy_formatted_coords%` are aliases of `%economy_coords%`.

These are online-only, and they respect the player's `randomizedcoords` setting — if a player has
turned it on, the coordinates you get back are deliberately inaccurate. That is the intended
behaviour, so do not use these placeholders to build anything that needs a true position.

### Player settings — `%economy_setting_<id>%`

Returns the colourised state of any toggle from `/settings`, which is useful for building your own
settings display outside the bundled menu. Matching ignores case and hyphens. Most settings return
`ON` or `OFF`; the ones dealing with who may contact a player return `OFF`, `Anyone` or
`Friends/Followed` instead.

There are 47 settings. Chat and messaging: `publicchat`, `privatemessages`, `servermessages`,
`hotbarmessages`, `deathmessages`, `advancementmessages`, `joinleavemessages`. Alerts:
`payalerts`, `tpaalerts`, `bountyalerts`, `auctionalerts`, `orderalerts`, `followalerts`,
`serversounds`. Combat and visuals: `fastcrystals`, `totemparticles`, `explosionparticles`,
`explosionsounds`, `combattimer`, `displaydonutplus`, `moneynametags`, `worthdisplay`,
`nightvision`, `destroypearlondeath`. Teleport and payment: `tpaconfirm`, `tparequests`,
`tpahere`, `tpauto`, `payments`, `payconfirm`, `privatetransactions`, `randomizedcoords`.
Scoreboard: `scoreboard`, `showmoney`, `showshards`, `showkills`, `showdeaths`, `showplaytime`.
Marketplace: `auctionquickbuy`, `auctionquicksell`, `sellshulkers`. World: `mobspawns`,
`phantomspawning`. Lunar: `lunarteammates`. Legacy inverted aliases: `disablemobspawn`,
`disablephantom`.

An unknown setting id, or an offline player, returns an empty string.

---

## Leaderboards

Leaderboard data is reachable through two equivalent syntaxes:

```
%economy_top_<type>_<rank>_<output>%
%economylb_<type>_<rank>_<output>%
```

`<rank>` is a positive integer and is not capped — `%economylb_money_25_name%` is valid if your
leaderboard is that long. Type matching is normalised, so `blocks_placed`, `blocksPlaced` and
`BLOCKS_PLACED` are all accepted.

*Supported Types*: `money`, `shards`, `kills`, `deaths`, `playtime`, `blocksPlaced`, `blocksBroken`, `mobsKilled`, `killStreak`, `highestKillStreak`, `moneySpent`, `moneyMade`, `bounties`.

**Outputs:**

| Output | Returns |
| :--- | :--- |
| `name` | Username at that position |
| `value` | Full formatted value |
| `value_short` | Compact value — `economylb` also accepts `value-short` and `short` |
| `rank` | The rank number |
| `display` | A complete row, `#<rank> <name>: <short value>` |

A position with nobody in it returns `none` for `name` and `0` for `value`. `bounties` only counts
players who currently have a bounty on them, so it is usually much shorter than the other boards.

To show the requesting player's own position rather than a fixed rank, use `economyrank`:

```
%economyrank_<type>%
```

It accepts the same thirteen types and returns a 1-based position, or `0` when the player is
unranked.

---

## `pvp` — ranked arena

Sixteen placeholders covering the persistent ranked arena. `%pvp_arena%`, `%pvp_arena_reset%` and
`%pvp_arena_players%` describe the arena itself and work without a player; the rest are per-player:
`%pvp_rank%`, `%pvp_rank_id%`, `%pvp_elo%`, `%pvp_level%`, `%pvp_xp%`, `%pvp_next_xp%`,
`%pvp_kills%`, `%pvp_deaths%`, `%pvp_kd%`, `%pvp_streak%`, `%pvp_best_streak%`, `%pvp_joins%` and
`%pvp_in_arena%`.

Because these are published rather than drawn by the plugin, TAB keeps ownership of nametags and
any scoreboard or chat plugin can read the same values. A typical nametag format:

```
%pvp_rank% | %luckperms_prefix% %player%
```

See [Ranked PvP Arena](Ranked-PvP-Arena) for what each value means and [pvp.yml](Config-pvp.yml)
for the rank ladder and reset schedule.

---

## `hide` — disguises

| Placeholder | Returns |
| :--- | :--- |
| `%hide_active%` | `true` while a disguise is active |
| `%hide_public_name%` | Coloured public name — `%hide_name%` is an alias |
| `%hide_plain_name%` | Public name without colour — `%hide_plain_public_name%` is an alias |
| `%hide_mode%` | Current mode, or `NONE` |
| `%hide_alias%` | Active alias |
| `%hide_skin%` | Skin username used for the disguise |

---

## Internal tokens

Separately from PlaceholderAPI, the plugin substitutes `{token}` and `%token%` strings inside its
own configuration. These are not PlaceholderAPI placeholders and will not work in other plugins.

### Message tokens

`LanguageManager` and `ConfigManager` replace tokens pairwise when a message is built, so the set
available depends on the message. Commonly supplied: `{player}`, `{target}`, `{amount}`,
`{price}`, `{item}`, `{reason}`, `{issuer}`, `{staff}`, `{expires}`, `{world}`, `{x}`, `{y}`,
`{z}`, `{crate}`, `{keys}`, `{order_id}`, `{team}`, `{server}`, `{page}` and `{max_page}`.
`{quantity}` also matches `{Quantity}`. Amethyst grant messages also supply `{type}`, `{time}`
(`1h`, `5d`, never raw seconds), `{last_online}`, and `{status}`.

### Punishment tokens

Punishment screens accept both brace and percent forms of the same token, so `%reason%` and
`{reason}` are interchangeable. Available: reason, target (`player` / `target`), issuer (`issuer` /
`staff` / `by`), record `id`, punishment `type`, and expiry under any of `expires`, `expires_at`,
`expiration`, `expiry`, `duration` or `nicest_expiration`.

### Scoreboard tokens

[scoreboard.yml](Config-scoreboard.yml) lines accept, in addition to any placeholder:

| Token | Effect |
| :--- | :--- |
| `{team}` | Expands to the team line, or disappears when the player has no team |
| `{shard_booster}` | Expands only while a booster is running |
| `{shard_cuboid}` | Expands only inside a shard cuboid |
| `{money_icon}` / `{shards_icon}` | The configured currency icons |
| `{sb_icon:<glyph>}` | A glyph padded to the aligned icon column |

### Discord webhook tokens

[discord.yml](Config-discord.yml) templates accept `%player%`, `%uuid%`, `%staff%`, `%reason%`,
`%duration%`, `%date%`, `%id%`, `%server%`, `%scope%` and `%type%`.

---

## Placeholders consumed from other plugins

The plugin reads these from PlaceholderAPI when building chat, tab list and hover text. They come
from other plugins and resolve to nothing if that plugin is absent.

| Token | Provided by | Used for |
| :--- | :--- | :--- |
| `%luckperms_prefix%` | LuckPerms | Chat and tab list prefixes |
| `%luckperms_primary_group%` | LuckPerms | Chat group routing |
| `%vault_prefix%` | Vault | Prefix fallback |
| `%prefix%` | Any provider | Generic prefix fallback |

---

## Third-party integrations

### Required

| Plugin | Why it is required |
| :--- | :--- |
| **ProtocolLib** | Packet-level work: disguises, fake players, the maintenance MOTD, server list responses, item worth tooltips, and particle and sound filtering |
| **PlaceholderAPI** | Registers the expansions above and resolves external placeholders in chat, tab and menus |

Without either of these the plugin logs an error during `onEnable()` and disables itself.

### Optional

| Plugin | What it adds | Without it |
| :--- | :--- | :--- |
| **Vault** | The plugin registers itself as a Vault economy provider | Other plugins cannot read balances through Vault; the internal economy is unaffected |
| **LuckPerms** | Tab list refresh on permission changes, a staff-mode context, and `%luckperms_*%` in chat | Prefixes resolve empty and the tab list does not refresh on rank changes |
| **SkinsRestorer** | Skins for disguises, fake players and tab list heads | Falls back to Mojang profile lookups |
| **Apollo (Lunar Client)** | Rich presence and the teammate overlay for Lunar users | Logged as unavailable and skipped |
| **Floodgate** | Native Bedrock forms for Orders and Homes | Bedrock players get the Java chest GUIs. They still cannot render Java Dialog API screens — see [Dialog API & older clients](Dialog-API-and-Older-Clients). |
| **Geyser** | More accurate ping for Bedrock players | Falls back to the Spigot ping value. Bedrock clients do not implement Java dialogs. |
| **Simple Voice Chat** | Gates the microphone behind the consent menu and enables voice mutes | The consent menu still records an answer but has nothing to gate |
| **WorldEdit or FAWE** | Pastes the arena schematic on a scheduled ranked-arena reset | The reset does not happen |
| **Multiverse-Core** | Load-order only | No behavioural difference |

A note on Multiverse-Core: it appears in `softdepend` but there is **no Java API integration** with
it. The entry exists purely to influence load order around world creation. Do not expect it to
supply world flags or spawn points to this plugin.

### Infrastructure

| Component | Purpose |
| :--- | :--- |
| SQLite | Default storage, no setup required |
| MySQL (HikariCP) | Shared storage for a multi-server network |
| MongoDB | Alternative document backend with a local cache file |
| Redis (Jedis) | Cross-server staff chat, alerts, maintenance, duels and order sync |
| BungeeCord plugin messaging | Server transfers and network status |
| Discord webhooks | Punishment and staff notifications over plain HTTPS — no bot, no JDA |

All of these are bundled inside the plugin jar. See [database.yml](Config-database.yml) for
storage and Redis settings, and [network.yml](Config-network.yml) for the server list and
maintenance behaviour.

---

## Vault economy

The plugin **provides** a Vault economy rather than consuming one. `VaultEconomyHook` is registered
at `ServicePriority.Highest` during startup when Vault is installed, which means UltimateDonutSMP V2
becomes the economy other plugins see.

Only **money** is exposed through Vault. Shards are deliberately internal — a shop plugin reading
Vault will see money balances and nothing else. Formatting and currency names come from the money
settings in [config.yml](Config-config.yml).

| Capability | Supported |
| :--- | :--- |
| Player accounts, balance checks, deposits and withdrawals | Yes |
| Fractional digits | 2 |
| Bank accounts | No |
| Per-world balances | No — one balance across all worlds |

If another economy plugin is installed alongside this one, expect a conflict: both will try to
register a provider, and the highest priority wins.
