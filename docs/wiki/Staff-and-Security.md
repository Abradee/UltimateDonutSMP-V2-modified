# Staff & Security

Staff mode, vanish, freeze, invsee, disguises, bait stashes, punishments, and wipes.

Full command tables: [Commands & Permissions](Commands-and-Permissions).

**Watch these nodes** — the command permission is not enough:

| Command | Extra node the code checks |
| --- | --- |
| `/fakeplayer` | `ultimatedonutsmp.staff.fakeplayer` (no `2`) |
| `/spawnstash` | `ultimatedonutsmp2.staff.spawnstash` |
| `/hide` `/disguise` | `ultimatedonutsmp2.hide.scramble` / `.disguise` |
| `/ecsee` edit | `ultimatedonutsmp2.admin.ecsee.edit` |
| `/amod` | `anvilmod.admin` |
| `/god` | `ultimatedonutsmp2.staff.god` (no `command.god`) |
| `/offend` | `ultimatedonutsmp2.staff.punishments.offend` |

The hide and fake-player nodes are **not** in `plugin.yml`, so they have no default — grant them yourself.

---

## Staff Mode and Vanish (`staff-mode.yml`)

`/staffmode` (alias `/staff`) swaps the moderator's survival inventory for a staff hotbar and back again. The bundled hotbar holds five tools, each with its own permission and slot under `STAFF-MODE.HOTBAR-SLOTS`:

| Tool | Slot | Permission |
| --- | --- | --- |
| Vanish toggle | 0 | `ultimatedonutsmp2.staff.mode.vanish` |
| Freeze tool | 1 | `ultimatedonutsmp2.staff.freeze` |
| Staff list | 4 | `ultimatedonutsmp2.staff.mode.stafflist` |
| Better View | 7 | `ultimatedonutsmp2.staff.mode.betterview` |
| Random teleport | 8 | `ultimatedonutsmp2.staff.mode.randomtp` |

Entering staff mode itself requires `STAFF-MODE.STAFF-PERMISSION`, which defaults to `ultimatedonutsmp2.staff.mode`. Toggling staff mode for another player requires `STAFF-MODE.OTHERS-PERMISSION` (`ultimatedonutsmp2.staff.mode.others`) or the admin node, and `/staffmode reload` requires `STAFF-MODE.ADMIN-PERMISSION` (`ultimatedonutsmp2.admin.staffmode`).

Behaviour worth configuring before you hand the command out:

- `LOCK-TOOLS` (default `true`) pins the staff tools so they cannot be dropped or moved out of the hotbar. Other items stay droppable.
- `RESTORE-INVENTORY-ON-DISABLE` (default `true`) hands the survival inventory back when staff mode ends.
- `PERSIST-ON-QUIT` and `PERSIST-ON-RESTART` (both `true`) keep the state across a logout or restart; on restart the moderator is taken out of staff mode and told their inventory was restored.
- `AUTO-VANISH-ON-ENABLE` is `false` by default, so entering staff mode does not vanish anyone until you turn it on.
- `BETTER-VIEW` grants night vision and flight while the tool is active.
- `RANDOM-TELEPORT` can exclude staff, vanished, frozen, duelling and FFA players from the target pool.
- `LUCKPERMS-CONTEXT` registers a LuckPerms context key (default `staffmode`) so permission sets can differ inside and outside staff mode. It requires LuckPerms to be installed.

`CUSTOM-ITEMS` lets you add your own hotbar entries that run commands. Each entry takes a slot, material, optional `PERMISSION`, `REQUIRE-TARGET`, and `EXECUTE-AS: PLAYER | CONSOLE`. A `CONSOLE` item runs with full console rights, which bypasses the holder's own permissions, so always pair one with a `PERMISSION` value. Slots already used by the built-in tools are refused.

### Vanish

`/vanish` toggles vanish, and it requires the moderator to already be in staff mode; outside staff mode the command refuses with a message. It has no alias. Vanish uses ProtocolLib to hide the player from other clients, and `VANISH-ACTIONBAR` repeats a reminder on the vanished player's action bar every `INTERVAL-TICKS`. `VANISH-FAKE-MESSAGES` optionally sends a fake leave message on vanish and a fake join message on unvanish, and `ONLY-TO-REGULAR-PLAYERS` keeps those fakes away from staff who hold `ultimatedonutsmp2.staff.mode.seevanished`.

`/stafflist` opens the online staff list, styled from `MENUS.STAFF-LIST` in `staff-mode.yml`.

Details of every key are on [staff-mode.yml](Config-staff-mode.yml).

---

## Staff Chat, HelpOp and Reports (`network.yml`)

Staff chat and staff alerts are network features rather than local ones. `/staffchat` (alias `/sc`) publishes to a Redis channel, and every server subscribed to the same channel prints the message to its own staff. The sender needs `ultimatedonutsmp2.staff.chat.use`.

`/helpop <message>` and `/report <player> <reason>` publish to the alert channel and are received by anyone holding `ultimatedonutsmp2.staff.alerts.receive`, or its `ultimatedonutsmp2.staff.helpop.receive` and `ultimatedonutsmp2.staff.report.receive` children. Per-player cooldowns are set by `HELPOP_COOLDOWN_SECONDS` and `REPORT_COOLDOWN_SECONDS`, and `ultimatedonutsmp2.staff.alerts.bypass-cooldown` skips them.

Redis is what makes these cross-server, and it is configured in [database.yml](Config-database.yml) under `REDIS`, not in `network.yml`. With Redis disabled the features still work, but only on the local server: `SEND_LOCAL_FALLBACK_ON_REDIS_ERROR` controls whether a staff chat message that could not be published still reaches local staff and warns the sender.

`network.yml` also carries the message formats, the maximum message and reason lengths, the local server ID and display name used in those formats, the `NETWORK-STATUS` monitoring block behind `/servers`, and the `MAINTENANCE` block behind `/maintenance`. See [network.yml](Config-network.yml).

---

## Freeze (`freeze.yml`)

`/freeze <player>` toggles a moderation freeze. Frozen players cannot move or interact, and only the commands listed in `ALLOWED-COMMANDS` still run — the bundled list is `/discord`, `/social`, `/msg`, `/r` and `/helpop`. `ALLOW-LOOK` (default `true`) still lets them turn the camera. Every `ALERT-INTERVAL-TICKS` the frozen player is shown the `ALERT` block, which by default explains the situation and points at a Discord link you should replace.

Permissions are configurable rather than fixed, and default to:

| Key | Default node | Purpose |
| --- | --- | --- |
| `STAFF-PERMISSION` | `ultimatedonutsmp2.staff.freeze` | Run `/freeze` |
| `ALERT-PERMISSION` | `ultimatedonutsmp2.staff.freeze.alert` | Receive freeze alerts |
| `EXEMPT-PERMISSION` | `ultimatedonutsmp2.staff.freeze.exempt` | Cannot be frozen |
| `ADMIN-PERMISSION` | `ultimatedonutsmp2.admin.freeze` | `/freeze reload` |

`PERSIST-ON-QUIT` and `PERSIST-ON-RESTART` are both `true`, so logging out does not clear a freeze, and staff are broadcast `QUIT_MESSAGE` when a frozen player disconnects. Full key list: [freeze.yml](Config-freeze.yml).

---

## Inventory and Ender Chest Inspection

### `/invsee <player>` (`invsee.yml`)

Opens a live view of the target's inventory, refreshed every `AUTO-REFRESH-TICKS` (default 10). Viewing needs `INVSEE.VIEW-PERMISSION` (`ultimatedonutsmp2.staff.invsee`).

Editing is off by default. A staff member can only change items when `INVSEE.ALLOW-EDIT` is `true` **and** they hold `INVSEE.MODIFY-PERMISSION` (`ultimatedonutsmp2.staff.invsee.modify`). Only one editor is granted write access at a time; a second staff member with the same permission is told the session is already being edited and gets a read-only view. `REQUIRE-ONLINE` (default `true`) and `ALLOW-SELF-VIEW` (default `false`) control who can be inspected, `NOTIFY-TARGET` is `false`, and `FREEZE-ON-LOGOUT` turns the open GUI into a snapshot if the target disconnects mid-session. See [invsee.yml](Config-invsee.yml).

### `/ecsee <player>` (`ender-chest.yml`)

The plugin replaces the vanilla ender chest with its own database-backed inventory, sized between one and six rows by `ENDER-CHEST.DEFAULT-ROWS` and the `ROW-PERMISSIONS` map (or `ultimatedonutsmp2.enderchest.rows.<1-6>` granted directly). `INTERCEPT-VANILLA-OPEN` makes right-clicking a placed ender chest open the custom one.

`/ecsee <player>` inspects another player's ender chest. Opening it needs `ENDER-CHEST.ECSEE.PERMISSION` (`ultimatedonutsmp2.admin.ecsee`). Changing its contents additionally needs `ENDER-CHEST.ECSEE.EDITABLE: true` and the `ultimatedonutsmp2.admin.ecsee.edit` node, which is checked in Java but not declared in `plugin.yml`. Details: [ender-chest.yml](Config-ender-chest.yml).

### `/seehomes <player>`

Opens the target's saved homes as a paged list, one bed per home, and clicking a bed teleports the staff member there. The world and the exact block the home sits on are written into the lore, and a home pointing at a world the server no longer loads shows as a barrier instead of teleporting anyone into nothing. Offline players work as well, since the homes are read from the database when nobody is holding them in memory. `/homesee` is the same command.

This is the list the profile viewer shows behind its Homes button; the command skips the profile screen. Running it needs `ultimatedonutsmp2.command.seehomes`, and then `ultimatedonutsmp2.staff.seehomes` or the `ultimatedonutsmp2.staff.profileviewer` node that already opens this menu. It is styled from `PROFILE-VIEWER-HOMES-MENU` in `menus.yml`, with the Back button swapped for `CLOSE-BUTTON` when there is no profile screen behind it. Disabling the `PROFILE_VIEWER` feature disables `/seehomes` alongside `/profileviewer`.

---

## Hide and Disguise (`hide.yml`)

Two commands share one manager. `/hide` opens the hide menu, and `/hide scramble` replaces the player's public name with a random string of `SCRAMBLE.LENGTH` characters drawn from `SCRAMBLE.CHARACTERS`; with `SCRAMBLE.OBFUSCATED` on, the name is drawn as a scrambling nametag lifted by `NAMETAG-OFFSET-Y`. `/disguise` opens the alias picker, or takes an alias and skin directly from the `ALIASES` and `SKINS` sections, or a skin URL.

Runtime permissions are fixed constants in the manager:

| Node | Effect |
| --- | --- |
| `ultimatedonutsmp2.hide.scramble` | Use scramble |
| `ultimatedonutsmp2.hide.disguise` | Use disguise |
| `ultimatedonutsmp2.hide.admin` | `/hide list`, `/hide check <player>`, `/hide remove <player>` |
| `ultimatedonutsmp2.hide.bypass` | See through other players' hide states |

`COOLDOWN-SECONDS` throttles changes, and a hide change is refused while the player is in combat. Hide requires ProtocolLib for the packet rewriting; SkinsRestorer, when installed, is used to resolve disguise skins, otherwise the plugin falls back to Mojang profile lookups. The `hide` PlaceholderAPI expansion exposes the resulting public name — see [Placeholders & Integrations](Placeholders-and-Integrations). Key reference: [hide.yml](Config-hide.yml).

---

## Detection Tools

### Spawn-stash bait (`spawn-stash.yml`)

`/spawnstash` (alias `/stash`) pastes a small prebuilt structure of ores, spawners and loot chests as bait, then alerts staff when a player interacts with it. The subcommands are:

```
/spawnstash                       # spawn a random stash type
/spawnstash <type>                # spawn a specific type
/spawnstash spawn <type>          # same, explicit form
/spawnstash list                  # list active and configured stashes
/spawnstash remove <id|nearest|all>
/spawnstash reload
```

Each entry under `TYPES` is a list of block offsets, so a stash can include spawners with a `SPAWNER_TYPE`, chests and shulker boxes with `CONTAINER_ITEMS`, and signs with `SIGN_LINES`. `SETTINGS` controls how long a stash lives (`DEFAULT_TTL_SECONDS`, default 900, overridable per type), how close a player must be to trigger an alert (`DEFAULT_ALERT_RADIUS`), the alert cooldown, whether existing blocks are overwritten and restored, whether the stash blocks are protected from breaking, whether spawners can be claimed on break, and a `MAX_BLOCKS_PER_STASH` ceiling. Stashes are rolled back when they expire, when removed, and — with `ROLLBACK_ON_RELOAD` — on reload.

Alerts go to `ultimatedonutsmp2.staff.spawnstash.alert`, `ultimatedonutsmp2.staff.spawnstash.bypass` exempts a player from triggering them, and `ultimatedonutsmp2.admin.spawnstash` covers administration. See [spawn-stash.yml](Config-spawn-stash.yml).

### Fake players (`staff-mode.yml`, `FAKE-PLAYER`)

`/fakeplayer` (alias `/fplayer`) spawns a packet-level dummy player near the moderator, which is useful for provoking kill aura and auto-clicker behaviour. The dummy is removed after `FAKE-PLAYER.TTL-SECONDS`, which is not present in the shipped file and falls back to ten seconds.

`SPAWN-AT-LOOK-TARGET` (default `true`) places it at the block you are looking at, within `LOOK-RANGE` blocks, instead of at your feet. `USE-DEFAULT-SKIN` swaps the moderator's skin for the vanilla default. `HIDE-NAMETAG` (default `true`) stops the dummy from copying the moderator's username, which keeps prefix, suffix and money nametag text off its head. `SNEAK` (default `true`) crouches it, which also hides leftover nametags on most clients.

The feature depends on ProtocolLib for the entity packets, and on SkinsRestorer when a skin has to be looked up. Its runtime permissions all use the legacy `ultimatedonutsmp.` prefix: `ultimatedonutsmp.staff.fakeplayer` to use it, `.alert` to be notified, and `.bypass` to be excluded.

### Alt detection (`/alts <player>`)

`/alts` looks up the player's stored IP history and reports other accounts that share an address. It needs `ultimatedonutsmp2.staff.alts` at runtime. It reports that no data exists when the account has no recorded addresses, so it is only useful once the player has connected at least once since IP logging began.

---

## Chat, Anvil and Log Moderation

### Chat filtering lives in `config.yml`, not `filter.yml`

`filter.yml` is **not** a chat filter. It defines the item categories used by the marketplace features, with top-level lists such as `Blocks`, `Tools`, `Food`, `Combat`, `Potions`, `Books`, `Ingredients` and `Utilities`. See [filter.yml](Config-filter.yml).

Chat moderation is configured under `CHAT` in [config.yml](Config-config.yml):

| Path | Purpose |
| --- | --- |
| `CHAT.FILTER.WORDS` | Blocked words; `CHAT.FILTER.ENABLED` is `true` by default |
| `CHAT.FILTER.LANGUAGE` | Restricts messages to `ALLOWED-ALPHABETS` (off by default) |
| `CHAT.FILTER.CAPS` | Blocks messages above `PERCENTAGE` capitals past `MIN-LENGTH` (off by default) |
| `CHAT.FILTER.ANTI-REPEAT` | Blocks repeated identical messages (off by default) |
| `CHAT.FILTER.ANTI-LINK` | Blocks links except those in `ALLOWED` (off by default) |
| `CHAT.FILTER.LENGTH` | Minimum and maximum message length (both off by default) |
| `CHAT.GLOBAL-CHAT-MUTED`, `CHAT.GLOBAL-CHAT-DELAY*` | Global mute and slow mode, driven by `/chat` |

The filter blocks a message rather than rewriting it; there is no lowercasing of shouted messages in the chat pipeline. Bypass nodes are `ultimatedonutsmp2.staff.chat.bypass.filter`, `.mute` and `.delay`. `/chat` itself is gated by `ultimatedonutsmp2.staff.chat.mute`, `.unmute`, `.delay` and `.clear` per subcommand.

### Anvil renames (`anvil-moderation.yml`)

The command is `/amod`, and it requires `anvilmod.admin` at runtime. `AnvilModerationListener` compares an anvil rename against `banned-words` and blocks the result. Repeat attempts escalate through the `punishments` list, one entry per offence count, executed as console commands with `%player%` substituted; the bundled list runs a 30 day mute followed by increasing temporary bans. Offence counts per player are written back into the `players` section of the same file. See [anvil-moderation.yml](Config-anvil-moderation.yml).

### Chat and activity logs

Every public chat message is written to the sender's own log alongside their `/msg` conversations and the rest of their activity. `/chatlog` opens the whole server's chat newest first, `/chatlog <player>` narrows it to one player, and `/logs <player>` shows that player's chat mixed in with their shop, economy and death history. Each entry carries the player, the message and the time it was sent. Browsing requires `ultimatedonutsmp2.admin.chatlog` and `ultimatedonutsmp2.admin.logs` respectively.

Only messages that actually reach chat are stored, so anything a mute, the filter or the chat delay blocked never appears. `CHAT.LOGGING.ENABLED` is the master switch, and `CHAT.LOGGING.PUBLIC-MESSAGES` and `CHAT.LOGGING.PRIVATE-MESSAGES` drop each kind separately.

---

## Discord Webhooks (`discord.yml`)

`DiscordWebhookManager` posts staff-relevant events to Discord webhook URLs over plain HTTP; there is no JDA dependency and no bot to run. Templates support `%player%`, `%uuid%`, `%staff%`, `%reason%`, `%duration%`, `%date%`, `%id%`, `%server%`, `%scope%` and `%type%`. With no URL configured the manager is a silent no-op. See [discord.yml](Config-discord.yml).

---

## Punishments

### Browsing punishments (`/punishments`)

Run `/punishments` with no arguments to browse every punishment on the server in one GUI, newest first. Each entry shows the punished player, the type, the reason, the staff member who issued it, the date, and the expiry (`Never` for a permanent punishment).

Controls sit along the bottom row:

| Button | Action |
| --- | --- |
| State Filter | Cycles All / Active / Inactive. Inactive covers both expired and manually removed records |
| Type Filter | Cycles All / Ban / Mute / Warn / Kick / Blacklist |
| Sort Order | Switches between newest and oldest first |
| Search | Left-click opens a sign to type a player name, right-click clears it |
| Refresh | Re-reads the list |

Search matches any part of the stored player name and ignores case, so `rod` finds `Cuteboyrodney`. A full UUID also works. Left-clicking an entry opens that player's full history; shift-right-clicking deletes the record if the viewer holds `ultimatedonutsmp2.staff.punishments.delete`.

Pages are read off the server thread, so the menu opens on a loading placeholder and fills in once the query returns. On a large history the first frame may be visible for a moment.

Passing a player name (`/punishments <player>`, alias `/phistory`) opens that player's history on its own with the same filters. This view is also reachable from the profile viewer. Both views require `ultimatedonutsmp2.staff.punishments.view`, and are styled from `PUNISHMENTS-LIST-MENU` and `PUNISHMENT-HISTORY-MENU` in `menus.yml`.

### Issuing and removing punishments

`/ban`, `/tempban`, `/mute`, `/tempmute`, `/warn`, `/kick`, `/blacklist`, `/vcmute` and their inverses all run through one handler. The parent nodes `ultimatedonutsmp2.staff.punishments.create` and `ultimatedonutsmp2.staff.punishments.remove` grant the individual children, which are declared with `default: false` so they are never handed out implicitly. `/vcmute` and `/vcunmute` only have an audible effect when a Simple Voice Chat plugin is installed.

### Protecting ranks from punishment

A punishment permission carries no notion of who it may be used on. A rank given `ultimatedonutsmp2.staff.punishments.offend` so it can hand out preset offences can point `/offend` at the owner just as easily as at a rule breaker, and the same goes for `/ban`, `/mute`, `/warn`, `/kick` and `/blacklist`.

Give the ranks you want protected `ultimatedonutsmp2.admin.punishments.exempt`. Staff below them are told they cannot punish that player and nothing is recorded. Ranks that should still be able to act on each other get `ultimatedonutsmp2.admin.punishments.exempt.bypass` as well — `ultimatedonutsmp2.admin.*` covers both, so an admin group keeps its protection without losing the ability to punish other admins.

Both nodes default to `op`, so operators are protected from non-operator staff out of the box and can still punish one another. The console is never blocked.

Keep the nodes out of `ultimatedonutsmp2.staff.punishments.*`. A wildcard over that branch is a normal way to set up a moderator rank, and it would hand the exemption to every moderator on the server.

Permissions are read off the player, so the check only applies while the target is online. Banning an exempt player while they are offline still works.

### Preset offences (`offenses.yml`)

`/offend <player> <offense> [tier]` applies a preset from `offenses.yml` instead of a hand-typed reason and duration. Each preset has a `name`, a `type` of `BAN`, `MUTE`, `WARN` or `KICK`, an optional `wipe` flag, and a `durations` list read as escalating tiers — the first entry for a first offence, the last entry for that offence and beyond. A tier of `"0s"` is issued as a warning rather than a ban or mute. The bundled file ships a large catalogue of offences covering cheating, chat, advertising and trading rules. Key reference: [offenses.yml](Config-offenses.yml).

---

## Wiping One Player (`/playerwipe`)

Where `/serverwipe` resets the whole server, `/playerwipe` clears a single player. It is the command to reach for when someone asks for a fresh start, or when a punished account should lose what it gained. `/pwipe` and `/wipe` do the same thing, and both `ultimatedonutsmp2.command.playerwipe` and `ultimatedonutsmp2.admin.playerwipe` are checked.

Running `/playerwipe <player>` on its own shows what would go, broken down by category, and changes nothing. Adding `confirm` carries it out:

```
/playerwipe Notch
/playerwipe Notch confirm
```

It works on offline players as well as online ones, and it clears:

- Kills, deaths, kill streaks, playtime, blocks placed and broken, and mobs killed
- Money (back to the configured default balance in `config.yml`) and shards
- Homes, and their team. A leader taking a wipe disbands the team
- Ender chest contents and crate keys
- Shop favourites, sell history and sell totals
- Auction listings and claims, orders and deliveries
- Duel and FFA records, bounties on them and bounties they placed
- Friends, ignores, and their activity log

Punishments, IP history, and freeze or staff-mode state survive a wipe, so a ban history stays intact and alt tracking still works. Spawners they placed are left standing as well, since those are blocks in the world rather than stored progress.

### Undoing a wipe (`/playerunwipe`)

Every wipe writes what it removed to a file in `plugins/UltimateDonutSmp2/player-wipe-backups/` before deleting anything, and `/playerunwipe` reads that file back. `/punwipe` and `/unwipe` are the same command, gated by `ultimatedonutsmp2.admin.playerunwipe`. It is the answer to a ban that turns out to have been wrong: clear the punishment, then hand the account back what it had.

`/playerunwipe <player>` on its own reports who ran the wipe, when, and what the backup holds. `confirm` puts it all back:

```
/playerunwipe Notch
/playerunwipe Notch confirm
```

The restore is exact rather than additive, so anything the account picked up between the wipe and the restore is dropped in favour of what the backup holds. On a wrongly banned player that difference is usually nothing, since they could not log in to earn any of it.

Backups are never deleted or overwritten, so a player wiped more than once keeps a file per wipe and the restore uses the most recent. The one thing a restore cannot bring back is a team the player led, because wiping a leader disbands the team outright and the team itself is not part of any single player's data; their membership row is dropped rather than restored, leaving them free to join another team. Filenames carry the player's name, their UUID and the time of the wipe, so an older backup can be restored by hand: delete the newer files, or move the one you want into place.

### Wiping on a ban (`offenses.yml`)

A wipe can also ride along with a punishment instead of being a second command. Each preset in `offenses.yml` takes an optional `wipe` flag, and when `/offend` issues that offence the account is cleared straight after the kick:

```yaml
offenses:
  duping:
    name: "Item Duping"
    type: BAN
    wipe: true
    durations:
      - "3d"
      - "perm"
```

The flag defaults to `false`, so nothing changes on a preset that does not mention it, and it is read under any casing.

It only fires on a real ban. A `MUTE`, `WARN` or `KICK` preset ignores it, and so does a tier of `"0s"`, since that tier is issued as a warning rather than a ban. Staff running the command see the number of records removed underneath the usual punishment confirmation.

Staff can also override whether to wipe per-command with an optional 4th argument:

```
/offend <player> <reason> [time] [wipe]
```

Argument 4 accepts `wipe` or `true` to force a wipe on a BAN/BLACKLIST, and `nowipe` or `false` to prevent wiping even if the preset configured `wipe: true`. Leaving the argument off falls back to the preset's configured `wipe` setting.

The wipe itself is the same one `/playerwipe` performs, down to what survives it and the backup it leaves behind, so `/playerunwipe` undoes it the same way. Turning it on for an offence your team hands out often still fills the backup folder with accounts you meant to keep, so it suits things like duping or botting rather than a first-strike chat rule.

---

## Server Wipe (`/serverwipe`, `server-wipe.yml`)

`/serverwipe` regenerates the worlds listed in `server-wipe.yml` and clears the plugin's stored data. It is irreversible beyond the backup it takes, and the destructive halves of it are console-only on purpose.

| Subcommand | Who can run it | Effect |
| --- | --- | --- |
| `preview` | Any sender with `ultimatedonutsmp2.admin.serverwipe` | Lists the worlds that would be reset and per-table row counts, then reports whether validation passed |
| `status` | Same | Prints the current wipe state |
| `prepare` | **Console only** | Validates, then prints a one-time confirmation token |
| `confirm <token>` | **Console only** | Runs the wipe. The token is the one `prepare` just printed; there is no other place to look it up |
| `cancel` | **Console only** | Discards a pending preparation |

Configuration:

- `RESET-WORLDS` is the list of worlds to regenerate. It ships empty, and `preview` fails validation until you fill it in. A world listed here is wiped even if spawn, warps, crates or the overworld/nether/end live in it.
- `PROTECTED-WORLDS` is the only denylist. A name there is never reset, even when it also appears in `RESET-WORLDS`.
- Each reset world also has to appear under `WORLD-SETTINGS` in `rtp.yml`, or as an `RTP-MENU.BUTTONS.*.WORLD` value. Stock `rtp.yml` already lists `world`, `world_nether` and `world_the_end`.
- `TOKEN-TTL-SECONDS` (default 300, floor 30) is how long the token from `prepare` stays valid.
- `BACKUP-DIRECTORY` (default `server-wipe-backups`) is where the pre-wipe copies are written, relative to the plugin folder unless you give an absolute path.
- `MESSAGES.MAINTENANCE` and `MESSAGES.KICK` are what players see while the wipe is being prepared and when the sequence starts.

When `confirm` runs, everyone online is kicked with `MESSAGES.KICK`, the database commit is made, a `server-wipe-pending.yml` marker is written, the normal shutdown saves are suppressed so nothing writes the old data back, and the server is shut down. Worlds that can be unloaded are moved immediately. The overworld usually cannot, so those folders are moved on the next start before Minecraft loads them, then the worlds come back empty. If a step fails before the database commit, the staged filesystem data is restored; if it fails after the commit, the reset files are preserved and shutdown is forced anyway.

Full key reference: [server-wipe.yml](Config-server-wipe.yml).

### Statistics wipe

`/ultimatedonutsmp2 statswipe` clears the leaderboard and statistics tables without touching worlds. It requires `ultimatedonutsmp2.admin.statswipe` and takes a `confirm` step of its own.
