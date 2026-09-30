# FAQ

Reload is `/ultimatedonutsmp2 reload` (`/uds reload`, `/udsmp reload`).

Jump: [won't start](#the-plugin-disabled-itself-on-startup) ·
[old clients](#menus-are-broken-on-1215-or-older) ·
[Bedrock / Geyser](#shop-or-menus-do-not-work-on-bedrock-geyser) ·
[placeholders](#economy_money-shows-as-raw-text) ·
[Vault](#another-plugin-cannot-see-money-balances) ·
[data reset](#balances-homes-or-keys-reset-after-a-restart) ·
[portals](#portal-does-nothing--cuboid-binds-do-not-pay-shards) ·
[spawners](#managed-spawners-drop-as-pig-spawners) ·
[shard shop spawners](#adding-spawners-to-the-shard-shop) ·
[crystals](#end-crystals-place-at-vanilla-speed) ·
[messages.yml](#editing-messagesyml-does-nothing) ·
[billford](#billford-and-billfordyml) ·
[v1 import](#migrating-from-ultimatedonutsmp-v1)

---

## The plugin disabled itself on startup

ProtocolLib and PlaceholderAPI are hard dependencies. If either is missing, `onEnable()` logs an
error and disables the plugin. Install both jars and restart.

The other startup abort is an unsupported Minecraft version. The allowed range is `26.1.2` to
`26.2` on Paper, Purpur, Pufferfish, Spigot and Folia.

---

## Menus are broken on 1.21.5 or older

That is expected. UltimateDonutSMP V2 uses Mojang's native **Dialog API**, added in Java Edition
**1.21.6**. The Minecraft client draws those screens. A 1.21.5 (or older) client — almost always
joining through ViaVersion or ViaBackwards — has no renderer for the packet, so `/ah`, `/menu`,
`/homes`, `/pay`, `/settings`, and similar look broken or do nothing useful.

This cannot be fixed in the plugin. Other plugins that use Dialog API have the same limit. The
chest-menu fallback in `menus.yml` only applies when the **server** itself lacks Paper's Dialog
API (Spigot, or Paper older than 1.21.6). On the Paper versions this plugin supports, every player
gets dialogs. There is no per-client switch to chest GUIs.

**Block Java clients below 1.21.6** on the proxy or ViaVersion. Do not allow 1.21.5-and-older
players onto a live network. Full write-up: [Dialog API & older clients](Dialog-API-and-Older-Clients).

---

## Shop or menus do not work on Bedrock (Geyser)

That is expected for anything that uses Mojang's **Java Dialog API**. Bedrock clients — including
everyone joining through **Geyser** — do not render those screens. `/menu`, `/ah`, `/pay`,
`/settings`, `/homes` (dialog path), and most other DonutSMP-style menus are **Java Edition only**.

**`/shop` (Quick Buy)** is different: the main grid is a **chest menu**, which Geyser can display.
To **pin a new item** from Bedrock without opening the Java Choose Item dialog:

1. Install **Floodgate** on the backend (not Geyser alone) and link it to Geyser.
2. Leave `QUICK-BUY.BEDROCK.ENABLED: true` in `shop.yml` (default).

Floodgate players get a paged Bedrock form with search. Without Floodgate, the same action uses a
chest catalogue instead. Some Quick Buy actions (for example **Search** on the main grid on Paper
with Dialog API enabled) still open a Java dialog — use the Floodgate catalogue search when pinning
items, or play on Java 1.21.6+ for the full UI.

This cannot be "fixed" into full Bedrock parity while the network relies on Dialog API for Java.
Full tables and operator steps: [Dialog API & older clients — Bedrock](Dialog-API-and-Older-Clients#bedrock-players-geyser--floodgate).

---

## `%economy_money%` shows as raw text

PlaceholderAPI is required, not optional. If it is missing the whole plugin is already disabled, so
an unparsed token usually means the display plugin is not asking PlaceholderAPI, or the identifier
is misspelt.

These three prefixes are the **same** expansion:

```
%economy_money%
%uds_money%
%ultimatedonutsmp2_money%
```

`%uds_money%` and `%ultimatedonutsmp2_money%` are valid aliases, not typos. The same aliases exist
for every other economy parameter.

Online-only placeholders (`%economy_ping%`, coordinates, shard-cuboid HUD) return a fallback when
there is no player context. See [Placeholders & Integrations](Placeholders-and-Integrations).

---

## Another plugin cannot see money balances

Install Vault. UltimateDonutSMP V2 **registers** a Vault economy provider; it does not consume one.
Without Vault, other plugins have nothing to talk to. The internal `/balance` and `/pay` commands
still work.

Only money is published through Vault. Shards are internal. Two economy plugins fighting over the
Vault service will produce inconsistent balances — pick one provider.

---

## Balances, homes or keys reset after a restart

The default SQLite file is:

```
plugins/UltimateDonutSmp2/data/data.db
```

It is **not** `database.db` at the plugin root. Copying the wrong file looks exactly like a wipe.

If you switched `DATABASE.TYPE` to `MYSQL` or `MONGODB`, check the credentials in
[database.yml](Config-database.yml) and the console for connection errors. A failed connect on boot
means the plugin never loaded the previous data.

---

## `/portal` does nothing / cuboid binds do not pay shards

There is no `/portal` command. The command is `/portalmanager`. Cuboids are managed with `/cuboid`.

Bind a region to a feature with:

```
/cuboid bind <name> <spawn|shard|rtp-zone|rtp-queue> <true|false>
```

`system` is accepted as an alias for `bind`. Shard payouts also need a matching entry under
`SHARDS.CUBOIDS.REGIONS` in [config.yml](Config-config.yml) — the bundled `spawn` region ships
disabled. See [Cuboids & Portals](Cuboids-and-Portals).

---

## Managed spawners drop as pig spawners

`SETTINGS.REQUIRE_SILK_TOUCH` in [spawners.yml](Config-spawners.yml) defaults to `true`. Breaking a
managed spawner without Silk Touch (and without `ultimatedonutsmp2.spawner.bypass`) drops a vanilla
spawner. Operators are **not** exempt — that node defaults to `false` and must be granted
explicitly.

Creative mode is exempt from the Silk Touch check; a Creative break removes the spawner instead of
returning the item.

---

## Adding spawners to the Shard Shop

`/shardshop` does not sell spawners until you add them. Each one is an entry under `SHARD-MENU` in
[shop.yml](Config-shop.yml), and `COMMAND` has to be `spawner give {player} <type> {amount}` with
`GIVE-ITEM: false`. `<type>` is the key under `TYPES` in [spawners.yml](Config-spawners.yml). The
shipped keys, free slots, and a paste-ready block for all nine types are in
[Adding spawners to the Shard Shop](Economy-and-Marketplaces#adding-spawners-to-the-shard-shop).

Reload with `/shop reload`. A new type in `spawners.yml` also needs `/spawner reload`.

---

## Vanished staff still appear

Vanish is packet-level and needs ProtocolLib, which is already a hard dependency. The usual cause
is granting a see-through node to the wrong group. Check
`ultimatedonutsmp2.staff.mode.vanish` versus whatever see-vanish / hide-bypass node your staff
group holds. Hide disguises use `ultimatedonutsmp2.hide.bypass` to see through other players.

---

## End crystals place at vanilla speed

Fast crystals are configured in **[config.yml](Config-config.yml)**, not in `duels.yml`:

```yaml
FAST-CRYSTALS:
  ENABLED: true
  DEFAULT-PLAYER-STATE: true
  EXCLUDED-WORLDS:
    - duels
  PLACE:
    ENABLED-COOLDOWN-TICKS: 0
```

The bundled `EXCLUDED-WORLDS` list includes `duels`. That is why crystals feel vanilla **inside
duel worlds** even though the feature is on. Remove `duels` from the list if you want the same
speed there. Players can also turn the feature off for themselves with the `fastcrystals` toggle
in `/settings`.

---

## Rank perks: homes, ender chests, RTP

The numbered families the code actually scans:

| Perk | Node |
| :--- | :--- |
| Extra homes | `ultimatedonutsmp2.homes.<N>` |
| Extra home GUI pages | `ultimatedonutsmp2.homes.page.<N>` |
| RTP cooldown override | `ultimatedonutsmp2.rtp.cooldown.<seconds>` |
| RTP queue priority | `ultimatedonutsmp2.rtp.priority.<N>` |
| Ender chest rows | `ultimatedonutsmp2.enderchest.rows.<N>` |
| Auction House listing cap | `ultimatedonutsmp2.auctionhouse.limit.<N>` |
| Donut+ prefix | `ultimatedonutsmp2.donutplus` (also accepts `donutplus`) |

There is no `ultimatedonutsmp2.command.sethome.multiple.<amount>` node and no `/booster` command.
Shard boosters come from the Amethyst `SHARD-BOOSTER` tool.

Chat and tab prefixes come from LuckPerms via `%luckperms_prefix%` when LuckPerms is installed.

---

## Moving homes (and everything else) to a new host

Stop both servers. Copy `plugins/UltimateDonutSmp2/data/data.db` (SQLite) or point
[database.yml](Config-database.yml) at the same MySQL / MongoDB instance. There is no built-in
EssentialsX importer — homes live in the plugin database.

---

## Migrating from UltimateDonutSMP (v1)

Keep the old `plugins/UltimateDonutSmp` folder next to v2, then from console:

```
/uds import v1
/uds import v1 confirm
```

A custom path works too: `/uds import v1 C:/servers/plugins/UltimateDonutSmp confirm`. Preview prints what it found; `confirm` writes.

SQLite rows that v2 does not already have are inserted; existing v2 rows stay. Yaml keys that exist in both files are overwritten from v1, extra crate ids and shard regions are added, and `ultimatedonutsmp.` permission nodes become `ultimatedonutsmp2.`. `database.yml` is not touched. Overwritten yaml is copied first to `plugins/UltimateDonutSmp2/config-backups/uds-v1-import-<timestamp>/`. LuckPerms groups are not part of this.

---

## "Move to keep earning shards (0/5)"

That is `SHARDS.CUBOIDS.REGIONS.<region>.MIN-MOVEMENT-BLOCKS` in [config.yml](Config-config.yml).
Set it to `0` to allow standing still:

```yaml
SHARDS:
  CUBOIDS:
    REGIONS:
      spawn:
        MIN-MOVEMENT-BLOCKS: 0
```

Then `/uds reload`.

---

## Death messages only reach some players

[death-messages.yml](Config-death-messages.yml) has **no** `SETTINGS.RADIUS` block. Audience is a
per-player setting: `deathmessages` in `/settings` can be off, friends-only, or everyone the
server still chooses to show. The file itself only holds the wording.

There is no global radius switch in that YAML. If you need everyone to see deaths, leave the
wording file alone and check the player setting default in [menus.yml](Config-menus.yml).

---

## `/setspawn` versus beds

`/setspawn` (and `/ultimatedonutsmp2 setup setspawn`) stores the spawn hub. `/spawn`, spawn-bound
portals, and deaths all use it, unless `SETTINGS.RESPAWN-ON-BED` is `true` — then a bed or charged
respawn anchor wins and everyone else still goes to the hub. The shipped default is `false`.

---

## LuckPermsChat / TAB is fighting the plugin

Chat format lives in **[config.yml](Config-config.yml)** under `CHAT`, not in `scoreboard.yml`:

```yaml
CHAT:
  FORMAT-ENABLED: false
```

The tab list is also in `config.yml` under `TABLIST`, not in `scoreboard.yml`.
[scoreboard.yml](Config-scoreboard.yml) is the sidebar only. Set `TABLIST.ENABLED: false` if TAB
(or another tab plugin) should own the player list.

---

## Adding potions to the shop

`/shop` is Quick Buy against the Auction House, not a classic admin stock list. Custom give
commands still work on **Shard Shop** entries in [shop.yml](Config-shop.yml) via `COMMAND`.
Sell prices are not in `shop.yml` — they come from [worth.yml](Config-worth.yml).

A lore line that starts with `Buy price:` (or `Harga beli:`) is rewritten with the live price when
the menu is drawn, so you do not have to keep the number in the lore in sync by hand.

---

## Text I wrote in capitals comes back in Title Case

The colour formatter title-cases a line whose visible letters are all uppercase **and** which
contains a colour code, hex code, MiniMessage tag or placeholder. Drop the colour code, mix in a
lowercase word, or use small-caps unicode if you need a shouty coloured line.

---

## `/rtp` prints coordinates in chat

Each player can hide them in `/settings` (RTP Coordinates). That swaps the chat line for
`MESSAGES.SAFE-LOCATION-FOUND-HIDDEN` in [rtp.yml](Config-rtp.yml). To pin it server-wide, set a
`DEFAULT` on the button in [menus.yml](Config-menus.yml), or remove `{x}` `{y}` `{z}` from the
message itself.

---

## Editing `messages.yml` does nothing

There is no shipped `messages.yml`. Player-facing text lives under `MESSAGES` in
`languages/<LANGUAGE.ACTIVE>.yml`. A leftover file from an older install is only a last-resort
fallback for keys the language files do not define — and every bundled key is defined.

See [Localization & Messages](Localization-and-Messages).

---

## `/billford` and `billford.yml`

`/billford` is declared in `plugin.yml` but has **no Java handler**. There is no `billford.yml`.
`sounds.yml` still has a `BILLFORD` group and the language files still mention the feature. None of
that runs. Do not spend time configuring it.

---

## `filter.yml` is not a chat filter

[filter.yml](Config-filter.yml) is the item-category list used by Orders and the Auction House
browse buttons. Chat filtering is `CHAT` in [config.yml](Config-config.yml). Anvil rename filtering
is [anvil-moderation.yml](Config-anvil-moderation.yml).

---

## The enchantment GUI has no command

Correct. [enchantments.yml](Config-enchantments.yml) is opened from the **Orders** flow when a
player lists an enchantable item. There is no `/enchant` command.

---

## Fake player permissions do not work

`/fakeplayer` is gated by `ultimatedonutsmp2.command.fakeplayer`, but the manager then checks the
**legacy** prefix `ultimatedonutsmp.staff.fakeplayer` — without the `2`. Grant that node, plus
`.alert` and `.bypass` if needed. See [Staff & Security](Staff-and-Security).
