# Lightbearer Helper

RuneLite plugin for the Lightbearer ring swap. It tells you when to put the Lightbearer on, when to take it off again, and makes it obvious when your spec is ready.

While spec is below 100% and you aren't wearing the Lightbearer, it gets highlighted in your inventory.

Once spec hits 100% the rings you've listed (Ultor, Bellator, Magus, Venator and so on) light up instead, until you equip one. At the same time the spec orb gets a glowing aura, and if you turn it on, your spec weapons get highlighted as well. When a listed ring is on (and a spec weapon, if you're highlighting those) it all goes quiet until spec drops again.

## Settings

Each of the three item highlights (Lightbearer, other rings, spec items) has its own colour and can be drawn as an outline around the sprite, a translucent tint over the item itself with adjustable opacity, an underline bar, or any mix of those. Each can also pulse between its colour and nothing. The pulse speed and smooth/blink mode are shared and live under General. Default is 2000 ms. General also has "Spec ready at", the spec percentage that counts as ready for the orb and spec item highlights (100 by default). The ring swap itself always waits for 100%.

The ring and spec item lists are plain text, one name per line. Commas work too. `*` is a wildcard, matching ignores case, and lines starting with `#` are skipped. The default ring list is the four DT2 rings plus `Berserker ring*` and `Ring of suffering*`. The spec item list covers claws, godswords, DWH, elder maul, dragon and abyssal daggers, dragon halberd, ZCB, blowpipe and the nightmare staves. Spec item highlighting is off by default. Every highlight, the orb included, starts out the same translucent tan. When it's on, spec items light up whenever spec is ready, whether or not you carry a Lightbearer at all. "Even without the ring swap" turns that off and ties them to the ring swap instead.

The spec orb has three decorations you can combine: an aura around the outside (on by default, size adjustable), an outline along the edge, and a fill inside with its own opacity. Pulse is on by default at 2000 ms with the same mode options. "Keep showing after swap" leaves it running even after you've swapped. By default the orb only lights up while one of the listed spec items is in your inventory or equipped, so it won't bother you on a herb run; "Only with a spec item" turns that off. There's also an option to tint the percentage text.

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

1.0.1: fill tints the item instead of the whole slot. The orb only lights up while a listed spec item is carried, on by default. Spec items can be highlighted whenever spec is ready, without a Lightbearer involved. "Spec ready at" moved to General and covers both. Pulses default to 2000 ms and every colour to the same tan.

1.0.0: first release.

## License

BSD 2-Clause, see LICENSE.
