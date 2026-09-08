# Lightbearer Helper

A RuneLite plugin that nags you to do the Lightbearer ring swap in both directions and makes "spec is full" impossible to miss.

- **Spec below 100%** and the Lightbearer is not worn → the Lightbearer is highlighted in your inventory (and, optionally, the ring you are wearing is marked in the worn-equipment tab so you know what to swap out).
- **Spec at 100%** and none of your listed "other rings" is worn → those rings are highlighted in your inventory until you equip one. Optionally your special attack weapons are highlighted too, and the special attack orb pulses between its normal colour and a colour of your choice.
- Once a listed ring is worn at 100% everything goes quiet until spec drops below 100% again.

## Features

### Lightbearer highlight
- Highlight style: **Outline**, **Box**, **Fill** or **Underline**
- Any colour (with alpha)
- **Mark worn ring to swap**: also highlights the ring in the worn-equipment tab that should come off

### Other rings
- Free-text list, one item name per line (commas also work), `*` wildcards, case-insensitive, `#` comment lines
- Defaults: `Ultor ring`, `Bellator ring`, `Magus ring`, `Venator ring`, `Berserker ring*`, `Ring of suffering*`
- Own highlight style and colour

### Spec items (optional, off by default)
- Same list format; defaults cover claws, godswords, DWH, elder maul, DDS/abyssal dagger, dragon halberd, ZCB, blowpipe, nightmare staves
- Own highlight style and colour
- **Skip when spec weapon wielded**: stop highlighting spec items once one of them is already equipped

### Spec orb pulse
- Oscillates the spec orb between its normal look and the **pulse colour** (the colour's alpha is the peak strength)
- **Pulse period** 200–3000 ms for one full normal → colour → normal cycle
- **Pulse mode**: Smooth fade or hard Blink
- **Tint orb text**: also fades the percentage text towards the pulse colour

## Testing locally

Requirements: a JDK 11 or newer on your `PATH` (17 recommended). Gradle is bundled through the wrapper.

```bash
git clone git@github.com:1504681/LightbearerHelper.git
cd LightbearerHelper
./gradlew run          # Windows: gradlew.bat run
```

`run` builds the plugin and launches a full RuneLite client in developer mode with the plugin registered (see
`src/test/java/com/lightbearerhelper/LightbearerHelperLauncher.java`). Log in, open the plugin settings (search
"Lightbearer") and try it: with the Lightbearer in your inventory and spec below 100% the ring lights up; drain or
regenerate spec to see the other-ring highlight and orb pulse kick in.

Other useful commands:

```bash
./gradlew build          # compile + unit tests (what the Plugin Hub CI runs)
./gradlew test           # unit tests only
./gradlew installPlugin  # drop the jar into ~/.runelite/externalPlugins for sideloading (RuneLite must run with --developer-mode)
```

From an IDE: run the `LightbearerHelperLauncher` class with the `-ea` VM flag.

## Configuration

| Section | Settings |
|---|---|
| Lightbearer | Highlight style, Highlight colour, Mark worn ring to swap |
| Other rings | Rings (list), Highlight style, Highlight colour |
| Spec items | Highlight spec items, Spec items (list), Skip when spec weapon wielded, Highlight style, Highlight colour |
| Spec orb | Pulse spec orb, Pulse colour, Pulse period, Pulse mode, Tint orb text |

## Plugin Hub

The repo is private while in development; the Plugin Hub builds straight from GitHub, so it has to be made public before submitting (see `~/homelab/runbooks/runelite-plugin.md`).

## Changelog

- **1.0.0** — initial release: Lightbearer / other-ring / spec-item highlights with four styles, spec orb pulse.

## License

BSD 2-Clause. See `LICENSE`.
