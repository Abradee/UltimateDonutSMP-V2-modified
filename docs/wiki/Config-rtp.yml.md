# `rtp.yml`

Random teleport. The interesting part is that destinations are found ahead of time rather
than on demand: `RTPCacheTask` pre-scans and caches safe locations so `/rtp` can hand one
out instantly instead of freezing the server while it looks for solid ground.

`WORLD-SETTINGS` sets the search area and rules per world, `DENIED-WORLDS` blocks the rest,
and `QUEUE` handles the case where several players ask at once. Two permission families
modify behaviour per rank: `ultimatedonutsmp2.rtp.cooldown.<seconds>` shortens the wait and
`ultimatedonutsmp2.rtp.priority.<N>` moves a player up the queue.

Comment at the top of the shipped file:

```text
UltimateDonutSMP2 - Random Teleport (RTP) Configuration
```

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/rtp.yml` |
| **Commands** | `/rtp`, `/rtpq` |
| **Player-facing text** | Edit `CONFIG.RTP` in `languages/<locale>.yml` — not this file |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`ENABLED`](#section-enabled) | boolean | `true` |
| [`SETTINGS`](#section-settings) | section | 26 keys |
| [`QUEUE`](#section-queue) | section | 6 keys |
| [`MESSAGES`](#section-messages) | section | 18 keys |
| [`DENIED-WORLDS`](#section-denied-worlds) | list | 1 entries |
| [`LOBBY-WORLDS`](#section-lobby-worlds) | list | 3 entries |
| [`WORLD-SETTINGS`](#section-world-settings) | section | 3 keys |
| [`RTP-MENU`](#section-rtp-menu) | section | 5 keys |

---

## Section: `ENABLED`

Enable or disable the Random Teleport (RTP) system globally (true / false)

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ENABLED` | `boolean` | `true`, `false` | `true` | Enable or disable the Random Teleport (RTP) system globally (true / false) |

<details>
<summary>Default <code>ENABLED</code> block as shipped</summary>

```yaml
# Enable or disable the Random Teleport (RTP) system globally (true / false)
ENABLED: true
```

</details>

---

## Section: `SETTINGS`

General RTP settings

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.PLAYERS-IN-RTP` | `integer` | Any integer | `3` | Maximum number of players allowed to perform RTP simultaneously |
| `SETTINGS.MAX-ATTEMPTS` | `integer` | Any integer | `64` | Maximum safe location search attempts per RTP request |
| `SETTINGS.MAX-CHUNK-SAMPLES` | `integer` | Any integer | `128` | Maximum chunk samples to inspect while looking for a valid location |
| `SETTINGS.ATTEMPT-INTERVAL-TICKS` | `integer` | Any integer | `1` | Ticks between chunk samples |
| `SETTINGS.SEARCH-ATTEMPTS-PER-TICK` | `integer` | Any integer | `4` | Number of location attempts evaluated in parallel per sample interval |
| `SETTINGS.MIN-PLAYER-DISTANCE` | `integer` | Any integer | `2000` | Minimum horizontal blocks between the player and the RTP landing spot, in the same world. Radius below is measured from CENTER-X/Z, so without this a player already inside the ring can land a few hundred blocks away. 0 turns the check off. Per-world MIN-PLAYER-DISTANCE overrides this, and the value is capped at that world's MAX-RADIUS |
| `SETTINGS.SEARCH-DISPLAY-MIN-TICKS` | `integer` | Any integer | `0` | Ticks the searching display is held for before the teleport starts. Lower it to teleport sooner once a spot is found, 0 teleports the moment the search succeeds |
| `SETTINGS.FOUND-DISPLAY-TICKS` | `integer` | Any integer | `0` | Ticks the found location display is held for before the teleport is queued. This one is paid on every RTP, including one served from LOCATION-CACHE below. 0 teleports immediately |
| `SETTINGS.GENERATE-CHUNKS` | `boolean` | `true`, `false` | `false` | Generate new chunks while searching. Keep false for pregenerated RTP worlds to protect TPS |
| `SETTINGS.GENERATE-FALLBACK-CHUNKS` | `boolean` | `true`, `false` | `true` | Generate a limited number of chunks only after pregenerated/loaded RTP search cannot find a safe spot |
| `SETTINGS.GENERATE-FALLBACK-AFTER-SAMPLES` | `integer` | Any integer | `32` | Chunk samples to try before limited fallback generation starts. Keep this high enough that a pregenerated world is searched before new terrain is generated, because generating is the wait after clicking RTP |
| `SETTINGS.MAX-GENERATE-FALLBACK-SAMPLES` | `integer` | Any integer | `32` | Maximum fallback chunks allowed to generate during one RTP search |
| `SETTINGS.LOAD-GENERATED-CHUNKS` | `boolean` | `true`, `false` | `true` | Allow loading already-generated chunks from disk if chunk generation is disabled. Turning this off with GENERATE-CHUNKS off as well leaves the search no way to reach a chunk |
| `SETTINGS.FALLBACK-TO-LOADED-CHUNKS` | `boolean` | `true`, `false` | `true` | If random samples cannot be prepared, try already-loaded chunks as a fallback |
| `SETTINGS.LOADED-CHUNK-FALLBACK-AFTER-SAMPLES` | `integer` | Any integer | `32` | Chunk samples to try before loaded chunk fallback starts |
| `SETTINGS.PRELOAD-TELEPORT-CHUNKS` | `boolean` | `true`, `false` | `true` | Load the chunks around the destination before the teleport lands, so a player does not arrive in terrain the server has not read yet. Off teleports straight away and lets the client catch up on its own |
| `SETTINGS.PRELOAD-RADIUS` | `integer` | Any integer | `2` | Chunk radius loaded around the destination, from 2 to 4. This is a floor rather than a cap: while POST-TELEPORT-CHUNK-THROTTLE is on, the throttled view distance below raises it, so on stock settings the radius is 4 whatever you put here |
| `SETTINGS.PRELOAD-CHUNKS-PER-TICK` | `integer` | Any integer | `2` | Chunks loaded per tick while preloading. Values below 2 are treated as 2 |
| `SETTINGS.PRELOAD-MAX-TICKS` | `integer` | Any integer | `40` | Give up preloading after this many ticks. Raised on its own when the radius and the per-tick rate above need longer than this to finish |
| `SETTINGS.POST-TELEPORT-CHUNK-THROTTLE` | `boolean` | `true`, `false` | `true` | Hold a player at a shorter view and simulation distance for a moment after an RTP, so the server is not sending a full render of brand new terrain all at once |
| `SETTINGS.POST-TELEPORT-VIEW-DISTANCE` | `integer` | Any integer | `4` | View distance to hold them at while the throttle is on. It only ever lowers a player's distance, never raises it, and values below 2 are treated as 2 |
| `SETTINGS.POST-TELEPORT-SIMULATION-DISTANCE` | `integer` | Any integer | `4` | Simulation distance to hold them at while the throttle is on. Same rules as above |
| `SETTINGS.POST-TELEPORT-THROTTLE-TICKS` | `integer` | Any integer | `80` | Ticks before the throttled distances are handed back. Values below 20 are treated as 20 |

### `SETTINGS.LOCATION-CACHE`

Safe locations found ahead of time in the background so RTP can teleport without searching

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.LOCATION-CACHE.ENABLED` | `boolean` | `true`, `false` | `true` | Enable the location cache. Locations are filled in the background while players are online so a click on RTP can teleport without searching |
| `SETTINGS.LOCATION-CACHE.BACKGROUND-WARM-UP` | `boolean` | `true`, `false` | `false` | Keep false. The old join-time warm-up walked overworld/nether/end the moment the first player logged in and is what dropped TPS. The cache still fills a few seconds later |
| `SETTINGS.LOCATION-CACHE.SIZE` | `integer` | Any integer | `3` | How many ready locations are kept per RTP world. 0 disables the cache |
| `SETTINGS.LOCATION-CACHE.MAX-AGE-SECONDS` | `integer` | Any integer | `600` | Seconds a cached location stays usable before it is thrown away. 0 keeps it until the next reload |
| `SETTINGS.LOCATION-CACHE.GENERATE-CHUNKS` | `boolean` | `true`, `false` | `false` | Let the background fill generate new terrain. Keep false. Loading already-generated chunks from disk (LOAD-GENERATED-CHUNKS) is enough to stock far RTP spots on a pregenerated world |

### `SETTINGS.PRIORITY-QUEUE`

Priority queue settings for RTP requests

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.PRIORITY-QUEUE.ENABLED` | `boolean` | `true`, `false` | `true` | Enable or disable the RTP permission priority queue system |
| `SETTINGS.PRIORITY-QUEUE.DEFAULT-PRIORITY` | `integer` | Any integer | `0` | Default priority for players without explicit priority permissions |

#### `SETTINGS.PRIORITY-QUEUE.PERMISSIONS`

Explicit mapping from permission node to priority weight (higher = higher priority)

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.PRIORITY-QUEUE.PERMISSIONS.ultimatedonutsmp2.rtp.priority.vip++` | `integer` | Any integer | `30` | Ultimatedonutsmp2.rtp.priority.vip++. |
| `SETTINGS.PRIORITY-QUEUE.PERMISSIONS.ultimatedonutsmp2.rtp.priority.vip+` | `integer` | Any integer | `20` | Ultimatedonutsmp2.rtp.priority.vip+. |
| `SETTINGS.PRIORITY-QUEUE.PERMISSIONS.ultimatedonutsmp2.rtp.priority.vip` | `integer` | Any integer | `10` | Ultimatedonutsmp2.rtp.priority.vip. |
| `SETTINGS.PRIORITY-QUEUE.PERMISSIONS.ultimatedonutsmp2.rtp.priority.high` | `integer` | Any integer | `5` | Ultimatedonutsmp2.rtp.priority.high. |

### `SETTINGS.RANK-COOLDOWNS`

Per-rank RTP cooldown overrides resolved from permissions

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.RANK-COOLDOWNS.ENABLED` | `boolean` | `true`, `false` | `true` | Enable or disable permission based RTP cooldown overrides |

#### `SETTINGS.RANK-COOLDOWNS.PERMISSIONS`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SETTINGS.RANK-COOLDOWNS.PERMISSIONS.ultimatedonutsmp2.rtp.cooldown.vip++` | `integer` | Any integer | `3` | Ultimatedonutsmp2.rtp.cooldown.vip++. |
| `SETTINGS.RANK-COOLDOWNS.PERMISSIONS.ultimatedonutsmp2.rtp.cooldown.vip+` | `integer` | Any integer | `10` | Ultimatedonutsmp2.rtp.cooldown.vip+. |
| `SETTINGS.RANK-COOLDOWNS.PERMISSIONS.ultimatedonutsmp2.rtp.cooldown.vip` | `integer` | Any integer | `15` | Ultimatedonutsmp2.rtp.cooldown.vip. |

<details>
<summary>Default <code>SETTINGS</code> block as shipped</summary>

```yaml
# General RTP settings
SETTINGS:
  # Maximum number of players allowed to perform RTP simultaneously
  PLAYERS-IN-RTP: 3
  # Maximum safe location search attempts per RTP request
  MAX-ATTEMPTS: 64
  # Maximum chunk samples to inspect while looking for a valid location
  MAX-CHUNK-SAMPLES: 128
  # Ticks between chunk samples
  ATTEMPT-INTERVAL-TICKS: 1
  # Number of location attempts evaluated in parallel per sample interval
  SEARCH-ATTEMPTS-PER-TICK: 4
  # Minimum horizontal blocks between the player and the RTP landing spot, in the same world.
  # Radius below is measured from CENTER-X/Z, so without this a player already inside the ring
  # can land a few hundred blocks away. 0 turns the check off. Per-world MIN-PLAYER-DISTANCE
  # overrides this, and the value is capped at that world's MAX-RADIUS
  MIN-PLAYER-DISTANCE: 2000
  # Ticks the searching display is held for before the teleport starts. Lower it to teleport
  # sooner once a spot is found, 0 teleports the moment the search succeeds
  SEARCH-DISPLAY-MIN-TICKS: 0
  # Ticks the found location display is held for before the teleport is queued. This one is paid
  # on every RTP, including one served from LOCATION-CACHE below. 0 teleports immediately
  FOUND-DISPLAY-TICKS: 0
  # Generate new chunks while searching. Keep false for pregenerated RTP worlds to protect TPS
  GENERATE-CHUNKS: false
  # Generate a limited number of chunks only after pregenerated/loaded RTP search cannot find a safe spot
  GENERATE-FALLBACK-CHUNKS: true
  # Chunk samples to try before limited fallback generation starts.
  # Keep this high enough that a pregenerated world is searched before new terrain is generated,
  # because generating is the wait after clicking RTP
  GENERATE-FALLBACK-AFTER-SAMPLES: 32
  # Maximum fallback chunks allowed to generate during one RTP search
  MAX-GENERATE-FALLBACK-SAMPLES: 32
  # Allow loading already-generated chunks from disk if chunk generation is disabled.
  # Turning this off with GENERATE-CHUNKS off as well leaves the search no way to reach a chunk
  LOAD-GENERATED-CHUNKS: true
  # If random samples cannot be prepared, try already-loaded chunks as a fallback
  FALLBACK-TO-LOADED-CHUNKS: true
  # Chunk samples to try before loaded chunk fallback starts
  LOADED-CHUNK-FALLBACK-AFTER-SAMPLES: 32
  # Load the chunks around the destination before the teleport lands, so a player does not
  # arrive in terrain the server has not read yet. Off teleports straight away and lets the
  # client catch up on its own
  PRELOAD-TELEPORT-CHUNKS: true
  # Chunk radius loaded around the destination, from 2 to 4. This is a floor rather than a
  # cap: while POST-TELEPORT-CHUNK-THROTTLE is on, the throttled view distance below raises
  # it, so on stock settings the radius is 4 whatever you put here
  PRELOAD-RADIUS: 2
# ... section continues, see the file on disk for the full block
```

</details>

---

## Section: `QUEUE`

Matchmaking queue in front of RTP. Players run /rtpq to wait for other players, and the whole group is dropped at one shared random location instead of at a random location each

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUEUE.ENABLED` | `boolean` | `true`, `false` | `true` | Enable or disable the RTP matchmaking queue and the /rtpq command (true / false) |
| `QUEUE.MATCH-SIZE` | `integer` | Any integer | `2` | How many players have to be waiting before the group is teleported |
| `QUEUE.WORLD` | `string` | Any text | `world` | World the matched group is teleported into |
| `QUEUE.SPREAD-RADIUS` | `integer` | Any integer | `16` | How far the matched players are scattered around the shared location, in blocks. 0 drops the whole group on the exact same block |
| `QUEUE.CUBOID` | `string` | Any text | `''` | Cuboid that feeds the queue on its own. Standing anywhere inside it puts a player in the queue without typing /rtpq, and walking back out takes them off it again. Leave empty to keep the command as the only way in. Bind it in game with /cuboid bind &lt;cuboid&gt; rtp-queue true |

### `QUEUE.MESSAGES`

User feedback for the matchmaking queue

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUEUE.MESSAGES.DISABLED` | `string` | Any text | `&cThe RTP queue is currently disabled.` | Disabled. |
| `QUEUE.MESSAGES.UNAVAILABLE` | `string` | Any text | `&cThe RTP queue is not available right now.` | Unavailable. |
| `QUEUE.MESSAGES.BUSY` | `string` | Any text | `&cFinish the teleport you already started before joining …` | Busy. |
| `QUEUE.MESSAGES.JOINED` | `string` | Any text | `&e[RTP Queue] &fYou have joined the queue. Waiting for an…` | Joined. |
| `QUEUE.MESSAGES.WAITING-ACTIONBAR` | `string` | Any text | `&eWaiting for another player... &7({waiting}/{needed})` | Waiting actionbar. |
| `QUEUE.MESSAGES.PLAYER-JOINED` | `string` | Any text | `&e[RTP Queue] &f{player} has joined the queue. &7({waitin…` | Player joined. |
| `QUEUE.MESSAGES.ALREADY-QUEUED` | `string` | Any text | `&cYou are already in the RTP queue at position #{position…` | Already queued. |
| `QUEUE.MESSAGES.LEFT` | `string` | Any text | `&e[RTP Queue] &fYou have left the queue.` | Left. |
| `QUEUE.MESSAGES.PLAYER-LEFT` | `string` | Any text | `&e[RTP Queue] &f{player} has left the queue. &7({waiting}…` | Player left. |
| `QUEUE.MESSAGES.NOT-QUEUED` | `string` | Any text | `&cYou are not in the RTP queue.` | Not queued. |
| `QUEUE.MESSAGES.MATCH-FOUND` | `string` | Any text | `&a[RTP Queue] &fMatch found! Teleporting you to the same …` | Match found. |
| `QUEUE.MESSAGES.MATCH-FOUND-ACTIONBAR` | `string` | Any text | `&aMatch found! Teleporting you to the same area...` | Match found actionbar. |
| `QUEUE.MESSAGES.MATCH-FAILED` | `string` | Any text | `&c[RTP Queue] &fNo safe location was found for the match,…` | Match failed. |
| `QUEUE.MESSAGES.MATCH-ABANDONED` | `string` | Any text | `&c[RTP Queue] &fThe other players left before the match s…` | Match abandoned. |

<details>
<summary>Default <code>QUEUE</code> block as shipped</summary>

```yaml
# Matchmaking queue in front of RTP. Players run /rtpq to wait for other players, and the whole
# group is dropped at one shared random location instead of at a random location each
QUEUE:
  # Enable or disable the RTP matchmaking queue and the /rtpq command (true / false)
  ENABLED: true
  # How many players have to be waiting before the group is teleported
  MATCH-SIZE: 2
  # World the matched group is teleported into
  WORLD: world
  # How far the matched players are scattered around the shared location, in blocks.
  # 0 drops the whole group on the exact same block
  SPREAD-RADIUS: 16
  # Cuboid that feeds the queue on its own. Standing anywhere inside it puts a player in the
  # queue without typing /rtpq, and walking back out takes them off it again. Leave empty to
  # keep the command as the only way in. Bind it in game with
  # /cuboid bind <cuboid> rtp-queue true
  CUBOID: ''
  # User feedback for the matchmaking queue
  MESSAGES:
    DISABLED: '&cThe RTP queue is currently disabled.'
    UNAVAILABLE: '&cThe RTP queue is not available right now.'
    BUSY: '&cFinish the teleport you already started before joining the RTP queue.'
    JOINED: '&e[RTP Queue] &fYou have joined the queue. Waiting for another player... &7({waiting}/{needed})'
    WAITING-ACTIONBAR: '&eWaiting for another player... &7({waiting}/{needed})'
    PLAYER-JOINED: '&e[RTP Queue] &f{player} has joined the queue. &7({waiting}/{needed})'
    ALREADY-QUEUED: '&cYou are already in the RTP queue at position #{position}.'
    LEFT: '&e[RTP Queue] &fYou have left the queue.'
    PLAYER-LEFT: '&e[RTP Queue] &f{player} has left the queue. &7({waiting}/{needed})'
    NOT-QUEUED: '&cYou are not in the RTP queue.'
    MATCH-FOUND: '&a[RTP Queue] &fMatch found! Teleporting you to the same area...'
    MATCH-FOUND-ACTIONBAR: '&aMatch found! Teleporting you to the same area...'
    MATCH-FAILED: '&c[RTP Queue] &fNo safe location was found for the match, so you are back in the queue.'
    MATCH-ABANDONED: '&c[RTP Queue] &fThe other players left before the match started, so you are back in the queue.'
```

</details>

---

## Section: `MESSAGES`

User feedback and status messages

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MESSAGES.DISABLED` | `string` | Any text | `&cRTP is currently disabled.` | Disabled. |
| `MESSAGES.COOLDOWN` | `string` | Any text | `&cYou can't rtp for another {remaining}s.` | Wait time before the action can run again. |
| `MESSAGES.MAX-PLAYERS` | `string` | Any text | `&cToo many players are using RTP right now. Please try ag…` | Max players. |
| `MESSAGES.WORLD-NOT-EXIST` | `string` | Any text | `&cThe world does not exist.` | World not exist. |
| `MESSAGES.SEARCHING` | `string` | Any text | `&aSearching for a safe location in {world}...` | Searching. |
| `MESSAGES.TP-WARNING` | `string` | Any text | `&eDo not move for &b{countdown}&e seconds or the teleport…` | Tp warning. |
| `MESSAGES.SEARCH-ACTIONBAR` | `string` | Any text | `&7Searching {world}... &b{elapsed}s &8(&f{attempts}/{max_…` | Search actionbar. |
| `MESSAGES.SEARCH-FOUND-ACTIONBAR` | `string` | Any text | `&aSafe location found in {world}! &7Preparing teleport...` | Search found actionbar. |
| `MESSAGES.SAFE-LOCATION-FOUND` | `string` | Any text | `&aSafe location found at: X:{x} Y:{y} Z:{z}` | Safe location found. |
| `MESSAGES.MAX-ATTEMPTS` | `string` | Any text | `&cCould not find a safe location after %attempts% attempt…` | Max attempts. |
| `MESSAGES.DESTINATION-DISABLED` | `string` | Any text | `&cThis RTP destination is currently disabled.` | Destination disabled. |
| `MESSAGES.PLAYTIME-REQUIRED` | `string` | Any text | `&cYou need at least {required} hours of playtime to RTP t…` | Playtime required. |
| `MESSAGES.UNSAFE-LOCATION` | `string` | Any text | `&cThe location at X:{x} Y:{y} Z:{z} was rejected: {reason}` | Unsafe location. |
| `MESSAGES.SAFE-LOCATION-FOUND-HIDDEN` | `string` | Any text | `&aSafe location found! Teleporting you blindly...` | Safe location found hidden. |
| `MESSAGES.SEARCH-FOUND-ACTIONBAR-HIDDEN` | `string` | Any text | `&aFound safe location` | Search found actionbar hidden. |
| `MESSAGES.QUEUE-JOINED` | `string` | Any text | `&eRTP slots are full. You are in queue at position &#4B72…` | Queue joined. |
| `MESSAGES.QUEUE-POSITION` | `string` | Any text | `&7Waiting for RTP slot... Your current position: &#4B72FF…` | Queue position. |
| `MESSAGES.ALREADY-IN-QUEUE` | `string` | Any text | `&cYou are already in the RTP waiting queue at position #{…` | Already in queue. |

<details>
<summary>Default <code>MESSAGES</code> block as shipped</summary>

```yaml
# User feedback and status messages
MESSAGES:
  DISABLED: '&cRTP is currently disabled.'
  COOLDOWN: '&cYou can''t rtp for another {remaining}s.'
  MAX-PLAYERS: '&cToo many players are using RTP right now. Please try again later.'
  WORLD-NOT-EXIST: '&cThe world does not exist.'
  SEARCHING: '&aSearching for a safe location in {world}...'
  TP-WARNING: '&eDo not move for &b{countdown}&e seconds or the teleport will be canceled.'
  SEARCH-ACTIONBAR: '&7Searching {world}... &b{elapsed}s &8(&f{attempts}/{max_attempts}&8)'
  SEARCH-FOUND-ACTIONBAR: '&aSafe location found in {world}! &7Preparing teleport...'
  SAFE-LOCATION-FOUND: '&aSafe location found at: X:{x} Y:{y} Z:{z}'
  MAX-ATTEMPTS: '&cCould not find a safe location after %attempts% attempts.'
  DESTINATION-DISABLED: '&cThis RTP destination is currently disabled.'
  PLAYTIME-REQUIRED: '&cYou need at least {required} hours of playtime to RTP to {world}. &7(Current: {current}h)'
  UNSAFE-LOCATION: '&cThe location at X:{x} Y:{y} Z:{z} was rejected: {reason}'
  SAFE-LOCATION-FOUND-HIDDEN: '&aSafe location found! Teleporting you blindly...'
  SEARCH-FOUND-ACTIONBAR-HIDDEN: '&aFound safe location'
  QUEUE-JOINED: '&eRTP slots are full. You are in queue at position &#4B72FF#{position}&e (Priority: &f{priority}&e).'
  QUEUE-POSITION: '&7Waiting for RTP slot... Your current position: &#4B72FF#{position}&7.'
  ALREADY-IN-QUEUE: '&cYou are already in the RTP waiting queue at position #{position}.'
```

</details>

---

## Section: `DENIED-WORLDS`

List of world names where RTP execution is denied

A list of 1 values:

```yaml
DENIED-WORLDS:
  - afk
```

<details>
<summary>Default <code>DENIED-WORLDS</code> block as shipped</summary>

```yaml
# List of world names where RTP execution is denied
DENIED-WORLDS:
  - afk
```

</details>

---

## Section: `LOBBY-WORLDS`

List of lobby / hub worlds where running /rtp automatically redirects to the survival Overworld

A list of 3 values:

```yaml
LOBBY-WORLDS:
  - lobby
  - hub
  - spawn
```

<details>
<summary>Default <code>LOBBY-WORLDS</code> block as shipped</summary>

```yaml
# List of lobby / hub worlds where running /rtp automatically redirects to the survival Overworld
LOBBY-WORLDS:
  - lobby
  - hub
  - spawn
```

</details>

---

## Section: `WORLD-SETTINGS`

Per-world RTP boundary and cooldown settings

### `WORLD-SETTINGS.world`

Overworld configuration

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `WORLD-SETTINGS.world.MAX-RADIUS` | `integer` | Any integer | `5000` | Max radius. Blocks. |
| `WORLD-SETTINGS.world.MIN-RADIUS` | `integer` | Any integer | `500` | Min radius. Blocks. |
| `WORLD-SETTINGS.world.CENTER-X` | `integer` | Any integer | `0` | Center x. |
| `WORLD-SETTINGS.world.CENTER-Z` | `integer` | Any integer | `0` | Center z. |
| `WORLD-SETTINGS.world.MIN-PLAYER-DISTANCE` | `integer` | Any integer | `2000` | Minimum blocks from the player's current position. Keeps /rtp from dropping someone nearby |
| `WORLD-SETTINGS.world.COOLDOWN` | `integer` | Any integer | `30` | Wait time before the action can run again. |

### `WORLD-SETTINGS.world_nether`

Nether configuration

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `WORLD-SETTINGS.world_nether.MAX-RADIUS` | `integer` | Any integer | `500` | Max radius. Blocks. |
| `WORLD-SETTINGS.world_nether.MIN-RADIUS` | `integer` | Any integer | `50` | Min radius. Blocks. |
| `WORLD-SETTINGS.world_nether.CENTER-X` | `integer` | Any integer | `0` | Center x. |
| `WORLD-SETTINGS.world_nether.CENTER-Z` | `integer` | Any integer | `0` | Center z. |
| `WORLD-SETTINGS.world_nether.MIN-PLAYER-DISTANCE` | `integer` | Any integer | `200` | Min player distance. |
| `WORLD-SETTINGS.world_nether.COOLDOWN` | `integer` | Any integer | `30` | Wait time before the action can run again. |
| `WORLD-SETTINGS.world_nether.REQUIRED-PLAYTIME-HOURS` | `decimal` | Any decimal number | `5.0` | Required playtime in hours to use RTP in the Nether (0.0 = no requirement) |

### `WORLD-SETTINGS.world_the_end`

The End configuration

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `WORLD-SETTINGS.world_the_end.MAX-RADIUS` | `integer` | Any integer | `2000` | Max radius. Blocks. |
| `WORLD-SETTINGS.world_the_end.MIN-RADIUS` | `integer` | Any integer | `150` | Min radius. Blocks. |
| `WORLD-SETTINGS.world_the_end.CENTER-X` | `integer` | Any integer | `0` | Center x. |
| `WORLD-SETTINGS.world_the_end.CENTER-Z` | `integer` | Any integer | `0` | Center z. |
| `WORLD-SETTINGS.world_the_end.MIN-PLAYER-DISTANCE` | `integer` | Any integer | `750` | Min player distance. |
| `WORLD-SETTINGS.world_the_end.COOLDOWN` | `integer` | Any integer | `30` | Wait time before the action can run again. |
| `WORLD-SETTINGS.world_the_end.REQUIRED-PLAYTIME-HOURS` | `decimal` | Any decimal number | `10.0` | Required playtime in hours to use RTP in The End (0.0 = no requirement) |

<details>
<summary>Default <code>WORLD-SETTINGS</code> block as shipped</summary>

```yaml
# Per-world RTP boundary and cooldown settings
WORLD-SETTINGS:
  # Overworld configuration
  world:
    MAX-RADIUS: 5000
    MIN-RADIUS: 500
    CENTER-X: 0
    CENTER-Z: 0
    # Minimum blocks from the player's current position. Keeps /rtp from dropping someone nearby
    MIN-PLAYER-DISTANCE: 2000
    COOLDOWN: 30

  # Nether configuration
  world_nether:
    MAX-RADIUS: 500
    MIN-RADIUS: 50
    CENTER-X: 0
    CENTER-Z: 0
    MIN-PLAYER-DISTANCE: 200
    COOLDOWN: 30
    # Required playtime in hours to use RTP in the Nether (0.0 = no requirement)
    REQUIRED-PLAYTIME-HOURS: 5.0

  # The End configuration
  world_the_end:
    MAX-RADIUS: 2000
    MIN-RADIUS: 150
    CENTER-X: 0
    CENTER-Z: 0
    MIN-PLAYER-DISTANCE: 750
    COOLDOWN: 30
    # Required playtime in hours to use RTP in The End (0.0 = no requirement)
    REQUIRED-PLAYTIME-HOURS: 10.0
```

</details>

---

## Section: `RTP-MENU`

RTP GUI Menu configuration

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP-MENU.ENABLED` | `boolean` | `true`, `false` | `true` | When true, `/rtp` without arguments opens the RTP menu. When false, it teleports immediately. |
| `RTP-MENU.TITLE` | `string` | Any text | `&8RTP Menu` | Title text. Supports `&` colours and `&#RRGGBB` hex. |
| `RTP-MENU.SIZE` | `integer` | Any integer | `27` | Chest size. Must be a multiple of 9 between 9 and 54. |
| `RTP-MENU.PLACEHOLDER` | `boolean` | `true`, `false` | `true` | Enable background glass filler item in GUI (true / false) |

### `RTP-MENU.BUTTONS`

#### `RTP-MENU.BUTTONS.OVERWORLD`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP-MENU.BUTTONS.OVERWORLD.DISPLAY-NAME` | `string` | Any text | `&#4B72FFOverworld` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RTP-MENU.BUTTONS.OVERWORLD.MATERIAL` | `string` | Any text | `GRASS_BLOCK` | Bukkit `Material` name for the icon. |
| `RTP-MENU.BUTTONS.OVERWORLD.SLOT` | `integer` | Any integer | `11` | Inventory slot, `0` is the top-left cell. |
| `RTP-MENU.BUTTONS.OVERWORLD.WORLD` | `string` | Any text | `world` | World name, as shown in `/minecraft:worlds` / Multiverse. |
| `RTP-MENU.BUTTONS.OVERWORLD.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `OVERWORLD` section on or off. |
| `RTP-MENU.BUTTONS.OVERWORLD.LORE` | `list` | A list of values | _list of 5 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>RTP-MENU.BUTTONS.OVERWORLD.LORE</code> (5 items)</summary>

```yaml
LORE:
  - '&fClick to randomly teleport'
  - ''
  - '&7Players: &b{players}'
  - '&7Range: &b{min_radius}-{max_radius}'
  - '&7Cooldown: &b{cooldown}s'
```

</details>

#### `RTP-MENU.BUTTONS.NETHER`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP-MENU.BUTTONS.NETHER.DISPLAY-NAME` | `string` | Any text | `&#FF4B4BNether` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RTP-MENU.BUTTONS.NETHER.MATERIAL` | `string` | Any text | `NETHERRACK` | Bukkit `Material` name for the icon. |
| `RTP-MENU.BUTTONS.NETHER.SLOT` | `integer` | Any integer | `13` | Inventory slot, `0` is the top-left cell. |
| `RTP-MENU.BUTTONS.NETHER.WORLD` | `string` | Any text | `world_nether` | World name, as shown in `/minecraft:worlds` / Multiverse. |
| `RTP-MENU.BUTTONS.NETHER.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `NETHER` section on or off. |
| `RTP-MENU.BUTTONS.NETHER.LORE` | `list` | A list of values | _list of 6 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>RTP-MENU.BUTTONS.NETHER.LORE</code> (6 items)</summary>

```yaml
LORE:
  - '&fClick to randomly teleport'
  - ''
  - '&7Players: &b{players}'
  - '&7Range: &b{min_radius}-{max_radius}'
  - '&7Cooldown: &b{cooldown}s'
  - '&7Required playtime: &b{required_playtime}'
```

</details>

#### `RTP-MENU.BUTTONS.THE_END`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP-MENU.BUTTONS.THE_END.DISPLAY-NAME` | `string` | Any text | `&#A84BFFThe End` | Item name. Supports `&` colours and `&#RRGGBB` hex. |
| `RTP-MENU.BUTTONS.THE_END.MATERIAL` | `string` | Any text | `END_STONE` | Bukkit `Material` name for the icon. |
| `RTP-MENU.BUTTONS.THE_END.SLOT` | `integer` | Any integer | `15` | Inventory slot, `0` is the top-left cell. |
| `RTP-MENU.BUTTONS.THE_END.WORLD` | `string` | Any text | `world_the_end` | World name, as shown in `/minecraft:worlds` / Multiverse. |
| `RTP-MENU.BUTTONS.THE_END.ENABLED` | `boolean` | `true`, `false` | `true` | Turns the `THE_END` section on or off. |
| `RTP-MENU.BUTTONS.THE_END.LORE` | `list` | A list of values | _list of 6 items_ | Tooltip lines under the item name. |

<details>
<summary>Default contents of <code>RTP-MENU.BUTTONS.THE_END.LORE</code> (6 items)</summary>

```yaml
LORE:
  - '&fClick to randomly teleport'
  - ''
  - '&7Players: &b{players}'
  - '&7Range: &b{min_radius}-{max_radius}'
  - '&7Cooldown: &b{cooldown}s'
  - '&7Required playtime: &b{required_playtime}'
```

</details>

<details>
<summary>Default <code>RTP-MENU</code> block as shipped</summary>

```yaml
# RTP GUI Menu configuration
RTP-MENU:
  # Enable GUI menu when /rtp is run without arguments (true / false)
  ENABLED: true
  TITLE: '&8RTP Menu'
  SIZE: 27
  # Enable background glass filler item in GUI (true / false)
  PLACEHOLDER: true
  BUTTONS:
    OVERWORLD:
      DISPLAY-NAME: '&#4B72FFOverworld'
      MATERIAL: GRASS_BLOCK
      SLOT: 11
      WORLD: world
      ENABLED: true
      LORE:
      - '&fClick to randomly teleport'
      - ''
      - '&7Players: &b{players}'
      - '&7Range: &b{min_radius}-{max_radius}'
      - '&7Cooldown: &b{cooldown}s'
    NETHER:
      DISPLAY-NAME: '&#FF4B4BNether'
      MATERIAL: NETHERRACK
      SLOT: 13
      WORLD: world_nether
      ENABLED: true
      LORE:
      - '&fClick to randomly teleport'
      - ''
      - '&7Players: &b{players}'
      - '&7Range: &b{min_radius}-{max_radius}'
      - '&7Cooldown: &b{cooldown}s'
      - '&7Required playtime: &b{required_playtime}'
    THE_END:
      DISPLAY-NAME: '&#A84BFFThe End'
      MATERIAL: END_STONE
      SLOT: 15
      WORLD: world_the_end
      ENABLED: true
      LORE:
      - '&fClick to randomly teleport'
      - ''
      - '&7Players: &b{players}'
      - '&7Range: &b{min_radius}-{max_radius}'
      - '&7Cooldown: &b{cooldown}s'
      - '&7Required playtime: &b{required_playtime}'
```

</details>

---

Defaults above match the file shipped in the jar.
