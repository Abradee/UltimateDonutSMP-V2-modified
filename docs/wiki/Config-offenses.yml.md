# `offenses.yml`

The preset punishments staff pick from with `/offend`, and the escalating tiers each one
runs through. Rather than asking staff to judge a duration, they choose the offence and the
plugin applies the right tier based on how many times that player has done it before.

`durations` is that ladder: the first entry is the first offence, the second the second,
and the last entry repeats for every offence after it. A tier of `perm` never expires, and
a tier of `0s` is issued as a warning instead of a ban.

Treat `wipe: true` with care. It only fires when the offence actually bans someone, and it
clears that player's stats, balance, homes, ender chest, keys, auctions and orders — the
same thing `/playerwipe` does, with no undo. Punishment history, IP history and placed
spawners are kept.

Comment at the top of the shipped file:

```text
Configuration file for preset offenses and escalating punishment tiers.
Syntax per offense:
  <key>:
    name: "Display Name"
    type: BAN | MUTE | WARN | KICK
    wipe: false  # optional, defaults to false
    durations:
      - "30d"  # 1st offense
      - "60d"  # 2nd offense
      - "perm" # 3rd offense (and beyond)

wipe only fires when the offense actually bans someone. It clears their stats, balance, homes,
ender chest, crate keys, auctions, orders and the rest of their progress, the same thing
/playerwipe does, and there is no undo. Their punishment history, IP history and any spawners
they placed are kept. A tier of "0s" is issued as a warning rather than a ban, so it does not
wipe, and neither do MUTE, WARN or KICK offenses.
```

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/offenses.yml` |
| **Commands** | `/offend <player> <reason> [time] [wipe]` |
| **Player-facing text** | Not translated. Edit this file directly. |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`offenses`](#section-offenses) | section | 61 keys |

---

## Section: `offenses`

### Entry schema (61 entries)

Each entry under `offenses` is keyed by a name you choose, and every entry accepts the same options:

### Shipped entries

| Entry key | `name` | `type` | `durations` |
| :--- | :--- | :--- | :--- |
| `advertising` | `Advertising` | `BAN` | `30d` |
| `advertising-invite-rewards` | `Advertising Invite Rewards` | `BAN` | `30d` |
| `advertising-irl-trade` | `Advertising IRL Trade` | `BAN` | `3d` |
| `alt-limit` | `Alt Limit Violation` | `BAN` | `perm` |
| `anticheat` | `Anticheat Detection` | `BAN` | `30d` |
| `auto-chat-filter` | `Auto Chat Filter Violation` | `MUTE` | `24h, 3d` |
| `ban-evasion` | `Ban Evasion` | `BAN` | `perm` |
| `baritone` | `Baritone / Automation` | `BAN` | `30d, 60d, 360d` |
| `botting` | `Botting` | `BAN` | `perm` |
| `bug-abuse` | `Bug Abuse` | `BAN` | `14d, perm` |
| `entity-radar` | `Entity Radar` | `BAN` | `3d, 30d` |
| `esp` | `ESP Hacks` | `BAN` | `30d, 60d, 360d` |
| `macros` | `Macros` | `BAN` | `7d, 40d` |
| `major-inappropriate-build` | `Major Inappropriate Build` | `BAN` | `7d, 30d` |
| `major-language` | `Major Language Violation` | `MUTE` | `24h, 7d` |
| `make-a-ticket` | `Ticket Abuse / Ban Appeal Required` | `BAN` | `perm` |
| `chat-capslock` | `Chat Caps Lock Spam` | `MUTE` | `24h, 3d` |
| `chat-death-threats` | `Chat Death Threats` | `BAN` | `24h, 30d, perm` |
| `chat-flooding` | `Chat Flooding` | `MUTE` | `24h, 3d` |
| `chat-harassment` | `Chat Harassment` | `MUTE` | `24h, 3d, 30d, 720d, perm` |
| `chat-hate` | `Chat Hate Speech` | `MUTE` | `0s, 24h, 3d, 30d, perm` |
| `chat-racism` | `Chat Racism` | `BAN` | `30d, perm` |
| `chat-spam` | `Chat Spam` | `MUTE` | `0s, 3d, 30d` |
| `chat-toxicity` | `Chat Toxicity` | `MUTE` | `24h, 3d, 30d, perm` |
| `cheating` | `Cheating / Unfair Advantage` | `BAN` | `14d, 30d, perm` |
| `compromised-account` | `Compromised Account` | `BAN` | `perm` |
| `macros-skripts` | `Macros / Skripts` | `BAN` | `7d, 40d` |
| `autopunish` | `Auto Punish Trigger` | `BAN` | `14d, 30d, perm` |
| `ban-evading` | `Ban Evading` | `BAN` | `30d, perm` |
| `cross-trading` | `Cross Trading` | `BAN` | `30d, perm` |
| `doxing` | `Doxing / PII Leak` | `BAN` | `24h, 30d, perm` |
| `duping` | `Item Duping` | `BAN` | `3d, perm` |
| `external-gambling` | `External Gambling` | `BAN` | `14d` |
| `gambling-ownership` | `Gambling Ownership` | `BAN` | `3d, 30d` |
| `hacking` | `Hacking` | `BAN` | `14d, 30d, perm` |
| `radar-minimap` | `Radar / Minimap Hacks` | `BAN` | `3d, 30d` |
| `ratting` | `Ratting / Malware` | `BAN` | `perm` |
| `soundboard-proximity` | `Soundboard Proximity Abuse` | `MUTE` | `30d` |
| `spamming-chat` | `Spamming Chat` | `MUTE` | `24h, 3d` |
| `streaming-advertising-rat-clients` | `Streaming/Advertising Rat Clients` | `BAN` | `14d, 60d, 180d` |
| `streaming-cheat-client-pvp` | `Streaming Cheat Client PVP` | `BAN` | `7d, 30d` |
| `streaming-just-hack-client-no-advantage` | `Streaming Hack Client No Advantage` | `BAN` | `14d, 60d` |
| `suicide-encouragement-proximity` | `Suicide Encouragement Proximity` | `BAN` | `30d, perm` |
| `toxicity-hate-harassment-in-chat-sign` | `Toxicity/Hate/Harassment in Chat/Sign` | `MUTE` | `24h, 3d, 30d, perm` |
| `xray-esp-baritone` | `X-Ray / ESP / Baritone` | `BAN` | `30d, 60d, 360d` |
| `bot-owner` | `Bot Owner` | `BAN` | `perm` |
| `health-indicators` | `Health Indicators Abuse` | `BAN` | `7d, 30d` |
| `mute-evasion` | `Mute Evasion` | `MUTE` | `7d, 30d` |
| `inappropriate-builds` | `Inappropriate Builds` | `BAN` | `7d, 30d` |
| `inappropriate-language` | `Inappropriate Language` | `MUTE` | `30d` |
| `inappropriate-proximity` | `Inappropriate Proximity Chat` | `MUTE` | `30d` |
| `inappropriate-skin` | `Inappropriate Skin` | `BAN` | `1d, 7d` |
| `invite-rewards` | `Invite Rewards Exploitation` | `BAN` | `14d` |
| `invite-rewards-ownership` | `Invite Rewards Ownership Abuse` | `BAN` | `30d` |
| `racism-in-chat-sign` | `Racism in Chat/Sign` | `BAN` | `30d, perm` |
| `proximity-racism` | `Proximity Racism` | `BAN` | `30d, perm` |
| `proximity-toxicity-hate-harassment` | `Proximity Toxicity/Hate/Harassment` | `MUTE` | `30d, perm` |
| `lying-to-staff` | `Lying to Staff` | `BAN` | `7d, 30d` |
| `irltrade-booster` | `IRL Trade / Booster Abuse` | `BAN` | `3d, 14d, 180d` |
| `irl-trading-ownership` | `IRL Trading Ownership` | `BAN` | `720d, perm` |
| `teaming-with-cheater` | `Teaming with Cheater` | `BAN` | `perm` |

| Key | Type | Accepted values | Required? | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `durations` | `list` | A list of values | Required | Escalation ladder, one duration per prior offence. Accepts `30d`, `24h`, `perm`, and `0s` for a warning. The final entry repeats for all later offences. |
| `name` | `string` | Any text | Required | Display name shown to staff in `/offend` and in punishment messages. |
| `type` | `string` | Any text | Required | Punishment applied: `BAN`, `MUTE`, `WARN` or `KICK`. |
| `wipe` | `boolean` | `true`, `false` | Optional | When `true`, automatically runs a player wipe on `BAN` and `BLACKLIST` punishments. Defaults to `false`. Can be overridden per-use with the 4th argument of `/offend <player> <reason> [time] [wipe]` (`wipe`/`true` or `nowipe`/`false`). |

<details>
<summary>Default <code>offenses</code> block as shipped</summary>

```yaml
offenses:
  advertising:
    name: "Advertising"
    type: BAN
    durations:
      - "30d"

  advertising-invite-rewards:
    name: "Advertising Invite Rewards"
    type: BAN
    durations:
      - "30d"

  advertising-irl-trade:
    name: "Advertising IRL Trade"
    type: BAN
    durations:
      - "3d"

  alt-limit:
    name: "Alt Limit Violation"
    type: BAN
    durations:
      - "perm"

  anticheat:
    name: "Anticheat Detection"
    type: BAN
    durations:
      - "30d"

  auto-chat-filter:
    name: "Auto Chat Filter Violation"
    type: MUTE
    durations:
      - "24h"
      - "3d"

  ban-evasion:
    name: "Ban Evasion"
    type: BAN
    durations:
      - "perm"

  baritone:
    name: "Baritone / Automation"
    type: BAN
    durations:
# ... section continues, see the file on disk for the full block
```

</details>

---

Defaults above match the file shipped in the jar.
