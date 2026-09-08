# Lightbearer Helper

RuneLite plugin for the Lightbearer ring swap. It tells you when to put the Lightbearer on, when to take it off again, and makes it obvious when your spec is ready.

While spec is below 100% and you aren't wearing the Lightbearer, it gets highlighted in your inventory.

Once spec hits 100% the rings you've listed (Ultor, Bellator, Magus, Venator and so on) light up instead, until you equip one. At the same time the spec orb gets a glowing aura, and if you turn it on, your spec weapons get highlighted as well. When a listed ring is on (and a spec weapon, if you're highlighting those) it all goes quiet until spec drops again.

## Settings

Each of the three item highlights (Lightbearer, other rings, spec items) has its own colour and can be drawn as an outline around the sprite, a translucent tint over the item itself with adjustable opacity, an underline bar, or any mix of those. Each can also pulse between its colour and nothing. The pulse speed and smooth/blink mode are shared and live under General. Default is 1200 ms.

The ring and spec item lists are plain text, one name per line. Commas work too. `*` is a wildcard, matching ignores case, and lines starting with `#` are skipped. The default ring list is the four DT2 rings plus `Berserker ring*` and `Ring of suffering*`. The spec item list covers claws, godswords, DWH, elder maul, dragon and abyssal daggers, dragon halberd, ZCB, blowpipe and the nightmare staves. Spec item highlighting is off by default and the Lightbearer and spec items share the same light blue.

The spec orb has three decorations you can combine: an aura around the outside (on by default, size adjustable), an outline along the edge, and a fill inside with its own opacity. Pulse is on by default with the same period and mode options. "Show from" sets the spec percentage the orb highlight starts at (100 by default), and "Keep showing after swap" leaves it running even after you've swapped. There's also an option to tint the percentage text.

## Running it locally

You need a JDK, 11 or newer. Gradle comes with the wrapper.

```
git clone https://github.com/1504681/LightbearerHelper.git
cd LightbearerHelper
./gradlew run
```

On Windows use `.\gradlew.bat run`. That starts a normal RuneLite client in developer mode with the plugin already loaded. Log in, search the plugin list for Lightbearer and play with the settings.

`./gradlew build` compiles and runs the unit tests, which is what the Plugin Hub CI does. `./gradlew installPlugin` puts a jar in `~/.runelite/externalPlugins` if you'd rather sideload. From an IDE, run `LightbearerHelperLauncher` with `-ea`.

## Changelog

1.0.0: first release.

## License

BSD 2-Clause, see LICENSE.
