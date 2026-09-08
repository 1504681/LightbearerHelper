package com.lightbearerhelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Tints the special attack orb with a colour that oscillates between fully transparent (the
 * normal orb) and the configured pulse colour.
 */
public class SpecOrbOverlay extends Overlay
{
	/** Orb fill widgets across the minimap layouts; first non-hidden one wins. */
	private static final int[] ORB_COMPONENTS = {
		InterfaceID.Orbs.SPECENERGY_INDICATOR,
		InterfaceID.OrbsNomap.SPECENERGY_INDICATOR,
		InterfaceID.OrbsOsm.SPECENERGY_INDICATOR,
		InterfaceID.OrbsOsmNomap.SPECENERGY_INDICATOR,
		InterfaceID.Orbs.ORB_SPECENERGY,
		InterfaceID.OrbsNomap.ORB_SPECENERGY,
		InterfaceID.OrbsOsm.ORB_SPECENERGY,
		InterfaceID.OrbsOsmNomap.ORB_SPECENERGY,
	};

	private final Client client;
	private final LightbearerHelperPlugin plugin;
	private final LightbearerHelperConfig config;

	@Inject
	public SpecOrbOverlay(Client client, LightbearerHelperPlugin plugin, LightbearerHelperConfig config)
	{
		super(plugin);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isOrbPulseActive())
		{
			return null;
		}
		Widget orb = findOrbWidget();
		if (orb == null)
		{
			return null;
		}
		Rectangle bounds = orb.getBounds();
		if (bounds == null || bounds.width <= 0 || bounds.height <= 0)
		{
			return null;
		}

		Color color = config.orbColor();
		int alpha = Math.round(color.getAlpha() * plugin.pulseIntensity());
		if (alpha <= 0)
		{
			return null;
		}

		Object oldAa = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(255, alpha)));
		graphics.fill(new Ellipse2D.Float(bounds.x, bounds.y, bounds.width, bounds.height));
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			oldAa == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : oldAa);
		return null;
	}

	private Widget findOrbWidget()
	{
		for (int componentId : ORB_COMPONENTS)
		{
			Widget widget = client.getWidget(componentId);
			if (widget != null && !widget.isHidden())
			{
				return widget;
			}
		}
		return null;
	}
}
