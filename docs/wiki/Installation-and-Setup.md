# Installation & Setup

1. Put ProtocolLib + PlaceholderAPI + this jar in `plugins/`.
2. Start the server once. Files appear under `plugins/UltimateDonutSmp2/`.
3. Stand at spawn and run `/setspawn`. Then pick storage (below) if you need MySQL or Redis.

Reload later with `/uds reload`. [config.yml](Config-config.yml) ·
[database.yml](Config-database.yml) · [network.yml](Config-network.yml)

---

## Requirements

| Requirement | Supported | Notes |
| :--- | :--- | :--- |
| **Minecraft** | `26.1.2` – `26.2` | Checked in `onEnable()`. Outside this range the plugin disables itself. |
| **Java client** | **1.21.6 or newer** | 1.21.5 and older cannot render Dialog API menus. Block them on the proxy or ViaVersion. See [Dialog API & older clients](Dialog-API-and-Older-Clients). |
| **Server** | Paper, Purpur, Pufferfish, Spigot, Folia | Folia uses the plugin's own scheduler abstraction; everything else uses Bukkit's. |
| **Java** | Java 21+ | The plugin is compiled with `--release 21`. Use the JDK your Paper or Folia build requires. `/ultimatedonutsmp2 setup` reports the running Java version. |
| **Required plugins** | ProtocolLib, PlaceholderAPI | Both are hard dependencies. Missing either one disables UltimateDonutSMP V2. |
| **Optional plugins** | Vault, LuckPerms, SkinsRestorer, Apollo, Floodgate, Geyser, Simple Voice Chat, WorldEdit or FAWE | See [Placeholders & Integrations](Placeholders-and-Integrations). |

Install ProtocolLib and PlaceholderAPI in `plugins/` **before** the first boot. The plugin does not
soft-fail and wait for them later.

### Client version (Dialog API)

Menus use Mojang's native Dialog API, added in Java Edition **1.21.6**. A player on **1.21.5 or
older** (ViaVersion, ViaBackwards, or an old jar) sees broken, unusable screens. `/ah`, `/menu`,
`/homes`, `/pay`, and similar will not work for that player. This cannot be fixed in the plugin —
the old client has no dialog renderer.

The chest-menu fallback in `menus.yml` is **server-wide**: it only runs when this server lacks
Paper's Dialog API. It does not switch per player. Require 1.21.6+ and block older protocol
versions on Velocity, BungeeCord, or ViaVersion. Full detail:
[Dialog API & older clients](Dialog-API-and-Older-Clients).

### Bedrock (Geyser / Floodgate)

Bedrock clients do not implement Java dialogs. Geyser does not change that — dialog-first commands
(`/menu`, `/ah`, `/pay`, and similar) stay **Java-only**.

If Bedrock players should use **Quick Buy pinning**, **Orders** text prompts, or **Homes** forms,
install **Floodgate** on the backend and pair it with Geyser. `/shop` still opens a chest grid
without Floodgate; pinning then uses the chest catalogue instead of Bedrock forms. See
[Dialog API & older clients — Bedrock](Dialog-API-and-Older-Clients#bedrock-players-geyser--floodgate).

---

## Install

1. Build the jar, or use a release build:
   ```bash
   mvn clean package
   ```
   The shaded artifact is `target/UltimateDonutSmp2-1.0.jar`.
2. Copy it into the server's `plugins/` folder, next to ProtocolLib and PlaceholderAPI.
3. Start the server. On first boot the plugin writes `plugins/UltimateDonutSmp2/` and extracts every
   bundled YAML file plus `languages/`.

A leftover `messages.yml` from an older install is read as a last-resort fallback for `en_US` only.
New installs never create one. Edit `languages/<locale>.yml` instead — see
[Localization & Messages](Localization-and-Messages).

---

## First things to set

Stand where you want the spawn hub and run:

```
/setspawn
```

That location is used by `/spawn`, by portals that target spawn, and — unless
`SETTINGS.RESPAWN-ON-BED` is `true` in [config.yml](Config-config.yml) — by every death. Until you
set it, deaths fall back to vanilla respawn.

Then:

- Pick a language with `LANGUAGE.ACTIVE` in [config.yml](Config-config.yml).
- Turn features on or off with `/ultimatedonutsmp2 features` or `FEATURES.<KEY>.ENABLED`.
- If this server is part of a network, fill in [database.yml](Config-database.yml) and
  [network.yml](Config-network.yml) before you open the doors.

`/ultimatedonutsmp2` is also `/uds` and `/udsmp`.

Coming from UltimateDonutSMP v1, leave `plugins/UltimateDonutSmp` in place and run
`/uds import v1` then `/uds import v1 confirm`. Details:
[Migrating from UltimateDonutSMP (v1)](FAQ#migrating-from-ultimatedonutsmp-v1).

---

## Storage (`database.yml`)

The keys are `DATABASE` and `REDIS`, not a `STORAGE` root. The shipped file looks like this:

```yaml
DATABASE:
  TYPE: SQLITE          # SQLITE, MYSQL or MONGODB
  SQLITE:
    FILE: data/data.db
  MYSQL:
    HOST: localhost
    PORT: 3306
    DATABASE: ultimatedonutsmp2
    USERNAME: root
    PASSWORD: ''
    CREATE-DATABASE: true
    PARAMETERS: useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
  MONGODB:
    URI: mongodb://localhost:27017
    DATABASE: ultimatedonutsmp2
    CACHE-FILE: data/mongodb-cache.db
    SYNC-ON-AUTOSAVE: true
```

**SQLite** is the default. The file lands at `plugins/UltimateDonutSmp2/data/data.db`, not at the
plugin root. Copy that path — not `database.db` — if you are moving a server.

**MySQL** is the right choice when several servers must share balances, homes, punishments,
auctions and orders. `CREATE-DATABASE: true` issues `CREATE DATABASE IF NOT EXISTS` on connect.

**MongoDB** keeps a local cache file so short outages do not take the server down. Set
`SYNC-ON-AUTOSAVE` if you want the cache flushed on every autosave rather than only on shutdown.

A full key-by-key breakdown is on [database.yml](Config-database.yml).

---

## Redis and a multi-server network

Redis is configured in **`database.yml`**, not in `network.yml`:

```yaml
REDIS:
  ENABLED: true
  HOST: localhost
  PORT: 6379
  PASSWORD: ''
  DATABASE: 0
```

Leave `ENABLED` false on a single server. Nothing else depends on it.

[network.yml](Config-network.yml) then identifies **this** server and lists its siblings:

```yaml
NETWORK:
  ENABLED: true
  LOCAL_SERVER_ID: crystal
  LOCAL_DISPLAY_NAME: Crystal
```

`LOCAL_SERVER_ID` must match the name the proxy uses for this backend. Redis carries staff chat,
helpop, reports, maintenance state, cross-server duels and order sync. Player transfers use
BungeeCord plugin messaging, so the names must also match the proxy configuration.

---

## Folia

Folia is supported. All scheduling goes through `SpigotScheduler`, which routes work to Folia's
global, region, entity or async schedulers when those classes exist, and to Bukkit's scheduler
everywhere else. You do not configure this; it is detected from
`io.papermc.paper.threadedregions.RegionizedServer`.

Cuboids, portals, spawners, crystal placement and similar region-owned work run on the owning
region thread. Database work is already async.

---

## Reload versus restart

`/ultimatedonutsmp2 reload` (or `/uds reload`) re-reads the YAML files and most managers. Missing
keys from a new plugin version are merged into your files and the previous copies land in
`config-backups/<timestamp>/`.

The one exception is `dialog.yml` → `PAUSE-SCREEN`. That datapack is written during `onLoad()`,
before worlds load, so a change there needs a full server restart.

---

## After install

| Task | Where |
| :--- | :--- |
| Toggle features | `/ultimatedonutsmp2 features` or `FEATURES` in [config.yml](Config-config.yml) |
| Set prices | [worth.yml](Config-worth.yml) |
| Set up crates | [Crates & Spawners](Crates-and-Spawners) |
| Set up duels / FFA / ranked arena | [Duels & Instanced FFA](Duels-and-FFA), [Ranked PvP Arena](Ranked-PvP-Arena) |
| Grant staff tools | [Staff & Security](Staff-and-Security), [Commands & Permissions](Commands-and-Permissions) |
| Wire placeholders | [Placeholders & Integrations](Placeholders-and-Integrations) |
