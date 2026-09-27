# `sounds.yml`

One place to change every sound the plugin plays, grouped by the feature that triggers it.
Values are Bukkit `Sound` enum names; setting one to an empty string silences that
particular cue without affecting the rest.

Players can mute the lot for themselves with the `serversounds` toggle in `/settings`.

One caveat: the `BILLFORD` group is left over from a feature that is not implemented in
this build, and nothing reads it. See [FAQ](FAQ) for the details.

| | |
| :--- | :--- |
| **On disk** | `plugins/UltimateDonutSmp2/sounds.yml` |
| **Player-facing text** | Not translated. Edit this file directly. |
| **Reload** | `/ultimatedonutsmp2 reload` |

## Sections in this file

| Section | Type | Contents |
| :--- | :--- | :--- |
| [`MENUS`](#section-menus) | section | 2 keys |
| [`SPAWN`](#section-spawn) | section | 1 keys |
| [`SELL`](#section-sell) | section | 2 keys |
| [`PAY`](#section-pay) | section | 1 keys |
| [`PRIVATE_MESSAGE`](#section-private-message) | section | 1 keys |
| [`AMETHYST`](#section-amethyst) | section | 2 keys |
| [`BOOSTER`](#section-booster) | section | 2 keys |
| [`SHARDS`](#section-shards) | section | 3 keys |
| [`COMMANDS`](#section-commands) | section | 1 keys |
| [`BUCKET`](#section-bucket) | section | 1 keys |
| [`TPAUTO`](#section-tpauto) | section | 2 keys |
| [`TPA`](#section-tpa) | section | 5 keys |
| [`TELEPORT`](#section-teleport) | section | 3 keys |
| [`RTP-ZONE`](#section-rtp-zone) | section | 2 keys |
| [`RTP`](#section-rtp) | section | 4 keys |
| [`SHOP`](#section-shop) | section | 2 keys |
| [`QUICK_BUY`](#section-quick-buy) | section | 14 keys |
| [`BUY`](#section-buy) | section | 1 keys |
| [`GENERAL`](#section-general) | section | 1 keys |
| [`DRILL`](#section-drill) | section | 2 keys |
| [`BILLFORD`](#section-billford) | section | 4 keys |
| [`AUCTION_HOUSE`](#section-auction-house) | section | 14 keys |
| [`ORDERS`](#section-orders) | section | 14 keys |
| [`DUELS`](#section-duels) | section | 10 keys |
| [`KEY-ALL`](#section-key-all) | section | 1 keys |
| [`CRATES`](#section-crates) | section | 5 keys |
| [`SPAWNERS`](#section-spawners) | section | 10 keys |

---

## Section: `MENUS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `MENUS.BUTTON-CLICK` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Button click. |
| `MENUS.PAGE-TURN` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.0` | Page turn. |

<details>
<summary>Default <code>MENUS</code> block as shipped</summary>

```yaml
# Configuration section for Menus.
MENUS:
  # The text or value for Button Click. Available options: Any valid string text
  BUTTON-CLICK: minecraft:ui.button.click|1.0|1.0
  # The text or value for Page Turn. Available options: Any valid string text
  PAGE-TURN: minecraft:item.book.page_turn|1.0|1.0
```

</details>

---

## Section: `SPAWN`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWN.SLIME-JUMP` | `string` | Any text | `minecraft:entity.slime.jump\|1.0\|1.0` | Slime jump. |

<details>
<summary>Default <code>SPAWN</code> block as shipped</summary>

```yaml
# Configuration section for Spawn.
SPAWN:
  # The text or value for Slime Jump. Available options: Any valid string text
  SLIME-JUMP: minecraft:entity.slime.jump|1.0|1.0
```

</details>

---

## Section: `SELL`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SELL.SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Success. |
| `SELL.LEVEL-UP` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|2.0` | Level up. |

<details>
<summary>Default <code>SELL</code> block as shipped</summary>

```yaml
# Configuration section for Sell.
SELL:
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.0
  # The text or value for Level Up. Available options: Any valid string text
  LEVEL-UP: minecraft:entity.player.levelup|1.0|2.0
```

</details>

---

## Section: `PAY`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PAY.SUCCESS` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.2` | Success. |

<details>
<summary>Default <code>PAY</code> block as shipped</summary>

```yaml
# Configuration section for Pay.
PAY:
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.player.levelup|1.0|1.2
```

</details>

---

## Section: `PRIVATE_MESSAGE`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `PRIVATE_MESSAGE.RECEIVED` | `string` | Any text | `minecraft:block.note_block.bell\|1.0\|1.2` | Played for the receiver when a private message arrives. Empty silences it. |

<details>
<summary>Default <code>PRIVATE_MESSAGE</code> block as shipped</summary>

```yaml
# Configuration section for Private Message.
PRIVATE_MESSAGE:
  # Played for the receiver when a private message arrives. Empty silences it.
  RECEIVED: minecraft:block.note_block.bell|1.0|1.2
```

</details>

---

## Section: `AMETHYST`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AMETHYST.EXPIRED` | `string` | Any text | `minecraft:entity.item.break\|1.0\|1.0` | Expired. |
| `AMETHYST.BREAK` | `string` | Any text | `minecraft:block.amethyst_block.break\|1.0\|1.0` | Break. |

<details>
<summary>Default <code>AMETHYST</code> block as shipped</summary>

```yaml
# Configuration section for Amethyst.
AMETHYST:
  # The text or value for Expired. Available options: Any valid string text
  EXPIRED: minecraft:entity.item.break|1.0|1.0
  # The text or value for Break. Available options: Any valid string text
  BREAK: minecraft:block.amethyst_block.break|1.0|1.0
```

</details>

---

## Section: `BOOSTER`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BOOSTER.ERROR` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Error. |
| `BOOSTER.SUCCESS` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.0` | Success. |

<details>
<summary>Default <code>BOOSTER</code> block as shipped</summary>

```yaml
# Configuration section for Booster.
BOOSTER:
  # The text or value for Error. Available options: Any valid string text
  ERROR: minecraft:entity.villager.no|1.0|1.0
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.player.levelup|1.0|1.0
```

</details>

---

## Section: `SHARDS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHARDS.REWARD` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|0.85\|1.35` | Reward. |
| `SHARDS.REWARD-BOOSTED` | `string` | Any text | `minecraft:entity.player.levelup\|0.85\|1.45` | Reward boosted. |
| `SHARDS.CANCELLED` | `string` | Any text | `minecraft:entity.villager.no\|0.8\|1.1` | Cancelled. |

<details>
<summary>Default <code>SHARDS</code> block as shipped</summary>

```yaml
# Configuration section for Shards.
SHARDS:
  # The text or value for Reward. Available options: Any valid string text
  REWARD: minecraft:entity.experience_orb.pickup|0.85|1.35
  # The text or value for Reward Boosted. Available options: Any valid string text
  REWARD-BOOSTED: minecraft:entity.player.levelup|0.85|1.45
  # The text or value for Cancelled. Available options: Any valid string text
  CANCELLED: minecraft:entity.villager.no|0.8|1.1
```

</details>

---

## Section: `COMMANDS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `COMMANDS.ERROR` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Error. |

<details>
<summary>Default <code>COMMANDS</code> block as shipped</summary>

```yaml
# Configuration section for Commands.
COMMANDS:
  # The text or value for Error. Available options: Any valid string text
  ERROR: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `BUCKET`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BUCKET.FILL` | `string` | Any text | `minecraft:item.bucket.fill\|1.0\|1.0` | Fill. |

<details>
<summary>Default <code>BUCKET</code> block as shipped</summary>

```yaml
# Configuration section for Bucket.
BUCKET:
  # The text or value for Fill. Available options: Any valid string text
  FILL: minecraft:item.bucket.fill|1.0|1.0
```

</details>

---

## Section: `TPAUTO`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TPAUTO.ACTIVATE` | `string` | Any text | `minecraft:block.beacon.activate\|1.0\|1.0` | Activate. |
| `TPAUTO.DEACTIVATE` | `string` | Any text | `minecraft:block.beacon.deactivate\|1.0\|1.0` | Deactivate. |

<details>
<summary>Default <code>TPAUTO</code> block as shipped</summary>

```yaml
# Configuration section for Tpauto.
TPAUTO:
  # The text or value for Activate. Available options: Any valid string text
  ACTIVATE: minecraft:block.beacon.activate|1.0|1.0
  # The text or value for Deactivate. Available options: Any valid string text
  DEACTIVATE: minecraft:block.beacon.deactivate|1.0|1.0
```

</details>

---

## Section: `TPA`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TPA.REQUEST-RECEIVED` | `string` | Any text | `minecraft:block.note_block.chime\|1.0\|1.0` | Request received. |
| `TPA.REQUEST-SENT` | `string` | Any text | `minecraft:block.note_block.bit\|1.0\|1.0` | Request sent. |
| `TPA.REQUEST-SENT-EXTRA` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Request sent extra. |
| `TPA.NO-REQUEST` | `string` | Any text | `minecraft:entity.ender_pearl.throw\|1.0\|1.0` | No request. |
| `TPA.CONFIRM` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Confirm. |

<details>
<summary>Default <code>TPA</code> block as shipped</summary>

```yaml
# Configuration section for Tpa.
TPA:
  # The text or value for Request Received. Available options: Any valid string text
  REQUEST-RECEIVED: minecraft:block.note_block.chime|1.0|1.0
  # The text or value for Request Sent. Available options: Any valid string text
  REQUEST-SENT: minecraft:block.note_block.bit|1.0|1.0
  # The text or value for Request Sent Extra. Available options: Any valid string text
  REQUEST-SENT-EXTRA: minecraft:entity.experience_orb.pickup|1.0|1.0
  # The text or value for No Request. Available options: Any valid string text
  NO-REQUEST: minecraft:entity.ender_pearl.throw|1.0|1.0
  # The text or value for Confirm. Available options: Any valid string text
  CONFIRM: minecraft:entity.experience_orb.pickup|1.0|1.0
```

</details>

---

## Section: `TELEPORT`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `TELEPORT.SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Success. |
| `TELEPORT.COUNTDOWN` | `string` | Any text | `minecraft:entity.enderman.teleport\|1.0\|1.0` | Countdown. |
| `TELEPORT.CANCELLED` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Cancelled. |

<details>
<summary>Default <code>TELEPORT</code> block as shipped</summary>

```yaml
# Configuration section for Teleport.
TELEPORT:
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.0
  # The text or value for Countdown. Available options: Any valid string text
  COUNTDOWN: minecraft:entity.enderman.teleport|1.0|1.0
  # The text or value for Cancelled. Available options: Any valid string text
  CANCELLED: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `RTP-ZONE`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP-ZONE.COUNTDOWN` | `string` | Any text | `minecraft:block.note_block.hat\|0.9\|1.5` | Countdown. |
| `RTP-ZONE.CANCELLED` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Cancelled. |

<details>
<summary>Default <code>RTP-ZONE</code> block as shipped</summary>

```yaml
# Configuration section for Rtp Zone.
RTP-ZONE:
  # The text or value for Countdown. Available options: Any valid string text
  COUNTDOWN: minecraft:block.note_block.hat|0.9|1.5
  # The text or value for Cancelled. Available options: Any valid string text
  CANCELLED: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `RTP`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `RTP.SEARCH-START` | `string` | Any text | `minecraft:block.note_block.pling\|0.9\|1.1` | Search start. |
| `RTP.SEARCH-TICK` | `string` | Any text | `minecraft:block.note_block.hat\|0.5\|1.6` | Search tick. |
| `RTP.SEARCH-FOUND` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.35` | Search found. |
| `RTP.SEARCH-FAIL` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Search fail. |

<details>
<summary>Default <code>RTP</code> block as shipped</summary>

```yaml
# Configuration section for Rtp.
RTP:
  # The text or value for Search Start. Available options: Any valid string text
  SEARCH-START: minecraft:block.note_block.pling|0.9|1.1
  # The text or value for Search Tick. Available options: Any valid string text
  SEARCH-TICK: minecraft:block.note_block.hat|0.5|1.6
  # The text or value for Search Found. Available options: Any valid string text
  SEARCH-FOUND: minecraft:entity.experience_orb.pickup|1.0|1.35
  # The text or value for Search Fail. Available options: Any valid string text
  SEARCH-FAIL: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `SHOP`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SHOP.NO-MONEY` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | No money. |
| `SHOP.BUY-SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Buy success. |

<details>
<summary>Default <code>SHOP</code> block as shipped</summary>

```yaml
# Configuration section for Shop.
SHOP:
  # The text or value for No Money. Available options: Any valid string text
  NO-MONEY: minecraft:entity.villager.no|1.0|1.0
  # The text or value for Buy Success. Available options: Any valid string text
  BUY-SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.0
```

</details>

---

## Section: `QUICK_BUY`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `QUICK_BUY.OPEN` | `string` | Any text | `minecraft:block.chest.open\|0.8\|1.15` | Played when the Quick Buy menu is opened |
| `QUICK_BUY.CLICK` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Generic button click in Quick Buy GUIs |
| `QUICK_BUY.PAGE-TURN` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.0` | Page turn in item picker and transactions |
| `QUICK_BUY.SEARCH` | `string` | Any text | `minecraft:entity.villager.work_cartographer\|1.0\|1.2` | Search submitted or cleared |
| `QUICK_BUY.FILTER` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.2` | Filter cycled |
| `QUICK_BUY.REFRESH` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.15` | Prices or transactions refreshed |
| `QUICK_BUY.BUY-SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.2` | Successful Quick Buy purchase |
| `QUICK_BUY.FAIL` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Failed purchase, listing, or invalid action |
| `QUICK_BUY.EDIT` | `string` | Any text | `minecraft:block.note_block.pling\|0.9\|1.1` | Entered layout edit mode |
| `QUICK_BUY.SAVE` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.35` | Saved layout after editing |
| `QUICK_BUY.PIN` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.15` | Pinned an item to a Quick Buy slot |
| `QUICK_BUY.REMOVE` | `string` | Any text | `minecraft:entity.item.break\|1.0\|0.85` | Marked a pinned item for removal |
| `QUICK_BUY.CANCEL` | `string` | Any text | `minecraft:ui.button.click\|1.0\|0.8` | Cancelled a Quick Buy dialog or submenu |
| `QUICK_BUY.LIST-ITEM` | `string` | Any text | `minecraft:entity.villager.work_cartographer\|1.0\|1.0` | Listed an item from the Quick Buy sell flow |

<details>
<summary>Default <code>QUICK_BUY</code> block as shipped</summary>

```yaml
# Configuration section for Quick Buy.
QUICK_BUY:
  # Played when the Quick Buy menu is opened
  OPEN: minecraft:block.chest.open|0.8|1.15
  # Generic button click in Quick Buy GUIs
  CLICK: minecraft:ui.button.click|1.0|1.0
  # Page turn in item picker and transactions
  PAGE-TURN: minecraft:item.book.page_turn|1.0|1.0
  # Search submitted or cleared
  SEARCH: minecraft:entity.villager.work_cartographer|1.0|1.2
  # Filter cycled
  FILTER: minecraft:block.note_block.hat|1.0|1.2
  # Prices or transactions refreshed
  REFRESH: minecraft:item.book.page_turn|1.0|1.15
  # Successful Quick Buy purchase
  BUY-SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.2
  # Failed purchase, listing, or invalid action
  FAIL: minecraft:entity.villager.no|1.0|1.0
  # Entered layout edit mode
  EDIT: minecraft:block.note_block.pling|0.9|1.1
  # Saved layout after editing
  SAVE: minecraft:entity.experience_orb.pickup|1.0|1.35
  # Pinned an item to a Quick Buy slot
  PIN: minecraft:entity.item.pickup|1.0|1.15
  # Marked a pinned item for removal
  REMOVE: minecraft:entity.item.break|1.0|0.85
  # Cancelled a Quick Buy dialog or submenu
  CANCEL: minecraft:ui.button.click|1.0|0.8
  # Listed an item from the Quick Buy sell flow
  LIST-ITEM: minecraft:entity.villager.work_cartographer|1.0|1.0
```

</details>

---

## Section: `BUY`

Legacy alias for purchase success (Quick Buy also uses QUICK_BUY.BUY-SUCCESS).

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BUY.SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Success. |

<details>
<summary>Default <code>BUY</code> block as shipped</summary>

```yaml
# Legacy alias for purchase success (Quick Buy also uses QUICK_BUY.BUY-SUCCESS).
BUY:
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.0
```

</details>

---

## Section: `GENERAL`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `GENERAL.ERROR` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Error. |

<details>
<summary>Default <code>GENERAL</code> block as shipped</summary>

```yaml
# Configuration section for General sounds.
GENERAL:
  # The text or value for Error. Available options: Any valid string text
  ERROR: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `DRILL`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DRILL.ITEM-BREAK` | `string` | Any text | `minecraft:entity.item.break\|1.0\|1.0` | Item break. |
| `DRILL.AMETHYST-BREAK` | `string` | Any text | `minecraft:block.amethyst_block.break\|1.0\|1.0` | Amethyst break. |

<details>
<summary>Default <code>DRILL</code> block as shipped</summary>

```yaml
# Configuration section for Drill.
DRILL:
  # The text or value for Item Break. Available options: Any valid string text
  ITEM-BREAK: minecraft:entity.item.break|1.0|1.0
  # The text or value for Amethyst Break. Available options: Any valid string text
  AMETHYST-BREAK: minecraft:block.amethyst_block.break|1.0|1.0
```

</details>

---

## Section: `BILLFORD`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `BILLFORD.OPEN` | `string` | Any text | `minecraft:entity.villager.trade\|1.0\|1.1` | Open. |
| `BILLFORD.ROTATE` | `string` | Any text | `minecraft:block.beacon.activate\|0.8\|1.2` | Rotate. |
| `BILLFORD.SUCCESS` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.5` | Success. |
| `BILLFORD.FAIL` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Fail. |

<details>
<summary>Default <code>BILLFORD</code> block as shipped</summary>

```yaml
# Configuration section for Billford.
BILLFORD:
  # The text or value for Open. Available options: Any valid string text
  OPEN: minecraft:entity.villager.trade|1.0|1.1
  # The text or value for Rotate. Available options: Any valid string text
  ROTATE: minecraft:block.beacon.activate|0.8|1.2
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.player.levelup|1.0|1.5
  # The text or value for Fail. Available options: Any valid string text
  FAIL: minecraft:entity.villager.no|1.0|1.0
```

</details>

---

## Section: `AUCTION_HOUSE`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `AUCTION_HOUSE.OPEN` | `string` | Any text | `minecraft:block.chest.open\|0.8\|1.15` | Played when the auction house is opened |
| `AUCTION_HOUSE.CLICK` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Generic button click in auction GUIs |
| `AUCTION_HOUSE.PAGE-TURN` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.0` | Page turn. |
| `AUCTION_HOUSE.SEARCH` | `string` | Any text | `minecraft:entity.villager.work_cartographer\|1.0\|1.2` | Search submitted or cleared |
| `AUCTION_HOUSE.FILTER` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.2` | Filter or sort cycled |
| `AUCTION_HOUSE.REFRESH` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.15` | Listings refreshed |
| `AUCTION_HOUSE.SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.2` | Success. |
| `AUCTION_HOUSE.FAIL` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Fail. |
| `AUCTION_HOUSE.LIST-ITEM` | `string` | Any text | `minecraft:entity.villager.work_cartographer\|1.0\|1.0` | List item. |
| `AUCTION_HOUSE.CANCEL` | `string` | Any text | `minecraft:entity.item.break\|1.0\|0.85` | Listing cancelled and item returned |
| `AUCTION_HOUSE.CLAIM` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.15` | Claimed money or an item from a sale |
| `AUCTION_HOUSE.CONFIRM` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.2` | Opened or accepted the purchase confirmation |
| `AUCTION_HOUSE.TOGGLE` | `string` | Any text | `minecraft:block.note_block.pling\|0.9\|1.1` | Fast buy / fast sell toggled |
| `AUCTION_HOUSE.BOUGHT-YOUR-ITEM` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.2` | Bought your item. |

<details>
<summary>Default <code>AUCTION_HOUSE</code> block as shipped</summary>

```yaml
# Configuration section for Auction House.
AUCTION_HOUSE:
  # Played when the auction house is opened
  OPEN: minecraft:block.chest.open|0.8|1.15
  # Generic button click in auction GUIs
  CLICK: minecraft:ui.button.click|1.0|1.0
  # The text or value for Page Turn. Available options: Any valid string text
  PAGE-TURN: minecraft:item.book.page_turn|1.0|1.0
  # Search submitted or cleared
  SEARCH: minecraft:entity.villager.work_cartographer|1.0|1.2
  # Filter or sort cycled
  FILTER: minecraft:block.note_block.hat|1.0|1.2
  # Listings refreshed
  REFRESH: minecraft:item.book.page_turn|1.0|1.15
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.2
  # The text or value for Fail. Available options: Any valid string text
  FAIL: minecraft:entity.villager.no|1.0|1.0
  # The text or value for After Listing Item. Available options: Any valid string text
  LIST-ITEM: minecraft:entity.villager.work_cartographer|1.0|1.0
  # Listing cancelled and item returned
  CANCEL: minecraft:entity.item.break|1.0|0.85
  # Claimed money or an item from a sale
  CLAIM: minecraft:entity.item.pickup|1.0|1.15
  # Opened or accepted the purchase confirmation
  CONFIRM: minecraft:ui.button.click|1.0|1.2
  # Fast buy / fast sell toggled
  TOGGLE: minecraft:block.note_block.pling|0.9|1.1
  # The text or value for When someone buys your item. Available options: Any valid string text
  BOUGHT-YOUR-ITEM: minecraft:entity.player.levelup|1.0|1.2
```

</details>

---

## Section: `ORDERS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `ORDERS.CLICK` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Click. |
| `ORDERS.PAGE-TURN` | `string` | Any text | `minecraft:item.book.page_turn\|1.0\|1.0` | Page turn. |
| `ORDERS.OPEN` | `string` | Any text | `minecraft:block.chest.open\|0.8\|1.15` | Open. |
| `ORDERS.SEARCH` | `string` | Any text | `minecraft:entity.villager.work_cartographer\|1.0\|1.2` | Search. |
| `ORDERS.SUCCESS` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.2` | Success. |
| `ORDERS.FAIL` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Fail. |
| `ORDERS.CREATE` | `string` | Any text | `minecraft:entity.player.levelup\|0.85\|1.35` | Create. |
| `ORDERS.CANCEL` | `string` | Any text | `minecraft:entity.item.break\|1.0\|0.85` | Cancel. |
| `ORDERS.REFUND` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.45` | Refund. |
| `ORDERS.DELIVER` | `string` | Any text | `minecraft:entity.villager.yes\|1.0\|1.2` | Deliver. |
| `ORDERS.DELIVERED` | `string` | Any text | `minecraft:entity.player.levelup\|0.9\|1.25` | Delivered. |
| `ORDERS.COLLECT` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.15` | Collect. |
| `ORDERS.DROP` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|0.8` | Drop. |
| `ORDERS.EXPIRE` | `string` | Any text | `minecraft:entity.item.break\|0.9\|0.7` | Expire. |

<details>
<summary>Default <code>ORDERS</code> block as shipped</summary>

```yaml
# Configuration section for Orders.
ORDERS:
  # The text or value for Click. Available options: Any valid string text
  CLICK: minecraft:ui.button.click|1.0|1.0
  # The text or value for Page Turn. Available options: Any valid string text
  PAGE-TURN: minecraft:item.book.page_turn|1.0|1.0
  # The text or value for Open. Available options: Any valid string text
  OPEN: minecraft:block.chest.open|0.8|1.15
  # The text or value for Search. Available options: Any valid string text
  SEARCH: minecraft:entity.villager.work_cartographer|1.0|1.2
  # The text or value for Success. Available options: Any valid string text
  SUCCESS: minecraft:entity.experience_orb.pickup|1.0|1.2
  # The text or value for Fail. Available options: Any valid string text
  FAIL: minecraft:entity.villager.no|1.0|1.0
  # The text or value for Create. Available options: Any valid string text
  CREATE: minecraft:entity.player.levelup|0.85|1.35
  # The text or value for Cancel. Available options: Any valid string text
  CANCEL: minecraft:entity.item.break|1.0|0.85
  # The text or value for Refund. Available options: Any valid string text
  REFUND: minecraft:entity.experience_orb.pickup|1.0|1.45
  # The text or value for Deliver. Available options: Any valid string text
  DELIVER: minecraft:entity.villager.yes|1.0|1.2
  # The text or value for When someone delivers to your order. Available options: Any valid string text
  DELIVERED: minecraft:entity.player.levelup|0.9|1.25
  # The text or value for Collect. Available options: Any valid string text
  COLLECT: minecraft:entity.item.pickup|1.0|1.15
  # The text or value for Drop. Available options: Any valid string text
  DROP: minecraft:entity.item.pickup|1.0|0.8
  # The text or value for Expire. Available options: Any valid string text
  EXPIRE: minecraft:entity.item.break|0.9|0.7
```

</details>

---

## Section: `DUELS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DUELS.CLICK` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Click. |
| `DUELS.REQUEST-SENT` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.2` | Request sent. |
| `DUELS.REQUEST-RECEIVED` | `string` | Any text | `minecraft:block.note_block.pling\|1.0\|1.0` | Request received. |
| `DUELS.QUEUE-JOIN` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | Queue join. |
| `DUELS.MATCH-FOUND` | `string` | Any text | `minecraft:block.beacon.activate\|1.0\|1.1` | Match found. |
| `DUELS.MATCH-START` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.0` | Match start. |
| `DUELS.VICTORY` | `string` | Any text | `minecraft:ui.toast.challenge_complete\|1.0\|1.0` | Victory. |
| `DUELS.DEFEAT` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | Defeat. |
| `DUELS.CLAIM` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.0` | Claim. |

### `DUELS.START-COUNTDOWN`

#### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DUELS.START-COUNTDOWN.START-SOUND` | `string` | Any text | `minecraft:entity.firework_rocket.blast\|1.0\|1.0` | Bukkit `Sound` name, or empty to play nothing. |

#### `DUELS.START-COUNTDOWN.PER-SECOND`

##### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `DUELS.START-COUNTDOWN.PER-SECOND.5` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | 5. |
| `DUELS.START-COUNTDOWN.PER-SECOND.4` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | 4. |
| `DUELS.START-COUNTDOWN.PER-SECOND.3` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | 3. |
| `DUELS.START-COUNTDOWN.PER-SECOND.2` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | 2. |
| `DUELS.START-COUNTDOWN.PER-SECOND.1` | `string` | Any text | `minecraft:block.note_block.hat\|1.0\|1.0` | 1. |

<details>
<summary>Default <code>DUELS</code> block as shipped</summary>

```yaml
# Configuration section for Duels.
DUELS:
  # The text or value for Click. Available options: Any valid string text
  CLICK: minecraft:ui.button.click|1.0|1.0
  # The text or value for Request Sent. Available options: Any valid string text
  REQUEST-SENT: minecraft:entity.experience_orb.pickup|1.0|1.2
  # The text or value for Request Received. Available options: Any valid string text
  REQUEST-RECEIVED: minecraft:block.note_block.pling|1.0|1.0
  # The text or value for Queue Join. Available options: Any valid string text
  QUEUE-JOIN: minecraft:block.note_block.hat|1.0|1.0
  # Configuration section for Start Countdown.
  START-COUNTDOWN:
    # Configuration section for Per Second.
    PER-SECOND:
      # The text or value for 5. Available options: Any valid string text
      5: minecraft:block.note_block.hat|1.0|1.0
      # The text or value for 4. Available options: Any valid string text
      4: minecraft:block.note_block.hat|1.0|1.0
      # The text or value for 3. Available options: Any valid string text
      3: minecraft:block.note_block.hat|1.0|1.0
      # The text or value for 2. Available options: Any valid string text
      2: minecraft:block.note_block.hat|1.0|1.0
      # The text or value for 1. Available options: Any valid string text
      1: minecraft:block.note_block.hat|1.0|1.0
    # The text or value for Start Sound. Available options: Any valid string text
    START-SOUND: minecraft:entity.firework_rocket.blast|1.0|1.0
  # The text or value for Match Found. Available options: Any valid string text
  MATCH-FOUND: minecraft:block.beacon.activate|1.0|1.1
  # The text or value for Match Start. Available options: Any valid string text
  MATCH-START: minecraft:entity.player.levelup|1.0|1.0
  # The text or value for Victory. Available options: Any valid string text
  VICTORY: minecraft:ui.toast.challenge_complete|1.0|1.0
  # The text or value for Defeat. Available options: Any valid string text
  DEFEAT: minecraft:entity.villager.no|1.0|1.0
  # The text or value for Claim. Available options: Any valid string text
  CLAIM: minecraft:entity.item.pickup|1.0|1.0
```

</details>

---

## Section: `KEY-ALL`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `KEY-ALL.REWARD` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.1` | Reward. |

<details>
<summary>Default <code>KEY-ALL</code> block as shipped</summary>

```yaml
# Configuration section for Key All.
KEY-ALL:
  # The text or value for Reward. Available options: Any valid string text
  REWARD: minecraft:entity.player.levelup|1.0|1.1
```

</details>

---

## Section: `CRATES`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `CRATES.OPEN` | `string` | Any text | `minecraft:block.ender_chest.open\|1.0\|1.05` | Open. |
| `CRATES.CLAIM` | `string` | Any text | `minecraft:entity.player.levelup\|1.0\|1.25` | Claim. |
| `CRATES.NO-KEY` | `string` | Any text | `minecraft:entity.villager.no\|1.0\|1.0` | No key. |
| `CRATES.SPIN-TICK` | `string` | Any text | `minecraft:block.note_block.hat\|0.55\|1.55` | Spin tick. |
| `CRATES.SPIN-END` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.35` | Spin end. |

<details>
<summary>Default <code>CRATES</code> block as shipped</summary>

```yaml
# Configuration section for Crates.
CRATES:
  # The text or value for Open. Available options: Any valid string text
  OPEN: minecraft:block.ender_chest.open|1.0|1.05
  # The text or value for Claim. Available options: Any valid string text
  CLAIM: minecraft:entity.player.levelup|1.0|1.25
  # The text or value for No Key. Available options: Any valid string text
  NO-KEY: minecraft:entity.villager.no|1.0|1.0
  # The text or value for Spin Tick. Available options: Any valid string text
  SPIN-TICK: minecraft:block.note_block.hat|0.55|1.55
  # The text or value for Spin End. Available options: Any valid string text
  SPIN-END: minecraft:entity.experience_orb.pickup|1.0|1.35
```

</details>

---

## Section: `SPAWNERS`

### Options

| Option path | Type | Accepted values | Default | What it does |
| :--- | :--- | :--- | :--- | :--- |
| `SPAWNERS.OPEN-MENU` | `string` | Any text | `minecraft:block.chest.open\|1.0\|1.0` | Open menu. |
| `SPAWNERS.COLLECT-LOOT` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.0` | Collect loot. |
| `SPAWNERS.DROP-LOOT` | `string` | Any text | `minecraft:entity.item.pickup\|1.0\|1.2` | Drop loot. |
| `SPAWNERS.COLLECT-XP` | `string` | Any text | `minecraft:entity.experience_orb.pickup\|1.0\|1.0` | Collect xp. |
| `SPAWNERS.SELL-CONFIRM-OPEN` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Sell confirm open. |
| `SPAWNERS.SELL-SUCCESS` | `string` | Any text | `minecraft:entity.villager.yes\|1.0\|1.0` | Sell success. |
| `SPAWNERS.SELL-CANCEL` | `string` | Any text | `minecraft:ui.button.click\|1.0\|0.8` | Sell cancel. |
| `SPAWNERS.FILTER-OPEN` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.2` | Filter open. |
| `SPAWNERS.FILTER-TOGGLE` | `string` | Any text | `minecraft:ui.button.click\|1.0\|1.0` | Filter toggle. |
| `SPAWNERS.STACK` | `string` | Any text | `minecraft:block.trial_spawner.place\|1.0\|1.0` | Stack. |

<details>
<summary>Default <code>SPAWNERS</code> block as shipped</summary>

```yaml
# Configuration section for Spawners.
SPAWNERS:
  OPEN-MENU: minecraft:block.chest.open|1.0|1.0
  COLLECT-LOOT: minecraft:entity.item.pickup|1.0|1.0
  DROP-LOOT: minecraft:entity.item.pickup|1.0|1.2
  COLLECT-XP: minecraft:entity.experience_orb.pickup|1.0|1.0
  SELL-CONFIRM-OPEN: minecraft:ui.button.click|1.0|1.0
  SELL-SUCCESS: minecraft:entity.villager.yes|1.0|1.0
  SELL-CANCEL: minecraft:ui.button.click|1.0|0.8
  FILTER-OPEN: minecraft:ui.button.click|1.0|1.2
  FILTER-TOGGLE: minecraft:ui.button.click|1.0|1.0
  STACK: minecraft:block.trial_spawner.place|1.0|1.0
```

</details>

---

Defaults above match the file shipped in the jar.
