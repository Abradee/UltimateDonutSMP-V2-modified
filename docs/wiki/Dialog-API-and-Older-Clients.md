# Dialog API and older Minecraft clients

Players on **Minecraft 1.21.5 or older** cannot use UltimateDonutSMP V2 menus correctly.
This is a Minecraft client limitation, not a plugin bug, and it cannot be patched in
UltimateDonutSMP V2.

**Require Java clients 1.21.6 or newer.** Block 1.21.5 and below on the proxy or with
ViaVersion / ViaBackwards. Do not let those clients onto a live network.

---

## What happens

Join on 1.21.5 or lower (usually through ViaVersion or ViaBackwards) and dialog screens
render as a broken, unusable layout. Commands that open those screens — `/menu`, `/ah`,
`/homes`, `/pay`, `/settings`, `/stats`, `/friends`, `/rtp` queue, and similar — do not
present a usable UI. Some chest-based menus still exist for a few flows, but dialog-first
features (Auction House included) will not work for that player.

That is the same result any other plugin gets when it uses Mojang's Dialog API.

---

## Why it cannot be fixed here

[Dialogs](https://minecraft.wiki/w/Dialog) are a **vanilla Minecraft client feature**.
Mojang added the Dialog API in **1.21.6**. The server sends a dialog packet; the client
draws the screen. A 1.21.5 (or older) client has no renderer for that packet, so the
layout is garbage.

UltimateDonutSMP V2 cannot:

- Repair the old client's missing UI
- Detect each player's protocol and swap that player onto chest menus
- Ship a "DonutSMP-style" dual UI that uses dialogs on 1.21.6+ and chests on older
  clients

`DialogSupport` only checks whether **this server** exposes Paper's dialog classes. On
Spigot, or on Paper older than 1.21.6, every command falls back to the chest menus in
`menus.yml`. On a current Paper / Purpur / Pufferfish / Folia build (the versions this
plugin actually supports: `26.1.2`–`26.2`), dialogs are on for everyone. The plugin does
not look at ViaVersion protocol versions.

Turning `ENABLED: false` in [dialog.yml](Config-dialog.yml) forces chest menus for the
whole server. That is a global switch, not a per-client fallback.

---

## What operators should do

1. Tell players the Java client must be **1.21.6 or newer**.
2. On Velocity, BungeeCord, or ViaVersion, **block protocol versions below 1.21.6**.
   That is the supported way to keep 1.21.5-and-older clients off the network.
3. Do not rely on ViaBackwards as a compatibility layer for this plugin's menus.

Layout and copy for the dialogs themselves live in [dialog.yml](Config-dialog.yml) and
`DIALOG` in `languages/<locale>.yml`.

---

## Bedrock players (Geyser / Floodgate)

**Minecraft Bedrock Edition does not implement Mojang's Java Dialog API.** Geyser translates
Java packets to Bedrock, but it cannot turn a Java dialog into a working Bedrock UI. That is
expected: it is the same class of limitation as an old Java client on ViaBackwards, not a
UltimateDonutSMP V2 bug.

### What Bedrock players cannot use

Any flow that opens a **Java dialog** on Paper 26.x will fail or look broken on Bedrock. That
includes most of the DonutSMP-style hub, not only one command:

| Area | Examples | On Bedrock |
| :--- | :--- | :--- |
| Hub / navigation | `/menu`, `/settings`, `/stats`, `/friends`, leaderboards | Dialog — not usable |
| Economy UI | `/pay`, `/ah` (dialog browser), `/worth` choose-item | Dialog — not usable |
| Teleport / social | `/homes` (dialog path), `/rtp` queue, teleport menus | Dialog — not usable |
| Shop search (Java path) | Search button in Quick Buy when the server sends a dialog | Dialog — not usable |

Tell Bedrock players to use **Java Edition 1.21.6+** for the full experience, or accept that
dialog-first features stay Java-only.

### `/shop` (Quick Buy) on Bedrock

Quick Buy is **not** entirely dialog-based. `/shop` opens a **chest menu** (the pinned-item grid)
that Geyser can display. Pinning a new item is different:

1. **Java** — empty slot opens the Choose Item **dialog** (one button per material).
2. **Bedrock with Floodgate** — the plugin sends a **paged Floodgate form** with search
   (`QUICK-BUY.BEDROCK` in [shop.yml](Config-shop.yml)). Geyser alone is not enough; install
   **Floodgate** on the backend and link it to Geyser.
3. **Bedrock without Floodgate** — the plugin falls back to the **chest item catalogue**
   (45 materials per page) instead of the dialog.

If someone reports "the shop does not work on Bedrock," check:

- Is **only Geyser** installed? Install **Floodgate** for Quick Buy pinning and for Orders /
  Homes Bedrock forms (see [Placeholders & Integrations](Placeholders-and-Integrations)).
- Are they using **Search** in Quick Buy? That button still opens a Java dialog on current
  Paper; use the Floodgate catalogue's search button when pinning items, or clear search with
  right-click on the search control in the chest menu where that applies.
- Are they expecting `/menu`, `/ah`, or `/pay` dialogs? Those remain **Java-only**.

### What operators should do for Bedrock

1. Run **Geyser + Floodgate** together if Bedrock players should use Quick Buy pinning, Orders
   prompts, or Homes forms.
2. Keep `QUICK-BUY.BEDROCK.ENABLED: true` in `shop.yml` (default) when Floodgate is present.
3. Set player expectations: Bedrock can use chest-based Quick Buy and configured Floodgate
   flows; the rest of the Dialog API network UI requires a Java 1.21.6+ client.
