# Lightbearer Helper

RuneLite plugin for the Lightbearer ring swap. It tells you when to put the Lightbearer on, when to take it off again, and makes it obvious when your spec is ready.

While spec is below 100% and you aren't wearing the Lightbearer, it gets highlighted in your inventory.

Once spec hits 100% the rings you've listed (Ultor, Bellator, Magus, Venator and so on) light up instead, until you equip one. At the same time the spec orb gets a thin ring along its edge (a glowing aura and a pulse are there if you want them), and if you turn it on, your spec weapons get highlighted as well. When a listed ring is on (and a spec weapon, if you're highlighting those) it all goes quiet until spec drops again.

## Settings

Each of the three item highlights (Lightbearer, other rings, spec items) has its own colour and can be drawn as an outline around the sprite, a translucent tint over the item itself with adjustable opacity, an underline bar, or any mix of those. Each can also pulse between its colour and nothing. The pulse speed and smooth/blink mode are shared and live under General. Default is 2000 ms. General also has "Spec ready at", the spec percentage that counts as ready for the orb and spec item highlights (100 by default). The ring swap itself always waits for 100%.

The ring and spec item lists are plain text, one name per line. Commas work too. `*` is a wildcard, matching ignores case, and lines starting with `#` are skipped. The default ring list is the four DT2 rings plus `Berserker ring*` and `Ring of suffering*`. The spec item list covers claws, godswords, DWH, elder maul, dragon and abyssal daggers, dragon halberd, ZCB, blowpipe and the nightmare staves. Spec item highlighting is off by default, and so is marking the worn ring in the equipment tab. Every highlight, the orb included, starts out the same translucent tan. When it's on, spec items light up whenever spec is ready, whether or not you carry a Lightbearer at all. "Even without the ring swap" turns that off and ties them to the ring swap instead.

"Only when affordable" (off by default) swaps the Spec ready at threshold for each weapon's own special attack cost: claws light up at 50%, a dragon dagger at 25%, a Zaryte crossbow at 75%. The costs come from a built-in table of the wiki's numbers; end a line of the spec item list in a percentage to set your own, e.g. `Dragon claws 50%`, and anything not in the table keeps using Spec ready at. With this on, the spec orb lights up when one of the spec items you carry is affordable. Inside the Tombs of Amascut, the liquid adrenaline buff halves the costs (turn off "Halve costs on Liquid adrenaline" if you'd rather it didn't). The Keris partisan of the sun's special also drains 50 prayer points: with spec ready but fewer prayer points than that, the Keris turns the "Short of prayer colour" (red by default) instead; the orb keeps its own colour. Since its special is a heal, it only highlights while your hitpoints are below 40 ("Keris of the sun only when hurt" and "Hurt means below"). "Always mark Keris of the sun" keeps a standing mark on it in its own colour whatever the spec, like an inventory tag; the spec colours take over when its special is ready.

The spec orb has three decorations you can combine: an outline along the edge (the only one on by default), an aura around the outside with adjustable size, and a fill inside with its own opacity. Pulse is off by default; on, it runs at 2000 ms with the same mode options. "Keep showing after swap" leaves it running even after you've swapped. By default the orb only lights up while one of the listed spec items is in your inventory or equipped, so it won't bother you on a herb run; "Only with a spec item" turns that off. There's also an option to tint the percentage text.

## Changelog

1.0.4: only the Keris turns red when short of prayer, not the orb. "Always mark Keris of the sun" keeps it marked in its own colour at all times.

1.0.3: "Only when affordable": spec items (and the orb) go by each weapon's own spec cost, with costs set per line (`Dragon claws 50%`) and halved under liquid adrenaline. Keris partisan of the sun turns red when short of the 50 prayer points its special needs, and only highlights while hurt (below 40 HP by default). Keris added to the default spec item list.

1.0.2: quieter defaults. The spec orb gets a plain outline instead of the aura, the pulse is off, and the worn ring is no longer marked in the equipment tab. Anyone who changed those settings keeps them.

1.0.1: fill tints the item instead of the whole slot. The orb only lights up while a listed spec item is carried, on by default. Spec items can be highlighted whenever spec is ready, without a Lightbearer involved. "Spec ready at" moved to General and covers both. Pulses default to 2000 ms and every colour to the same tan.

1.0.0: first release.

## License

BSD 2-Clause, see LICENSE.
