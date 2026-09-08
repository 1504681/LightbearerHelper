package com.lightbearerhelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Decorates the special attack orb with any combination of a glowing aura around it, an outline
 * along its edge and a tint inside it, optionally pulsing between the normal orb and the colour.
 */
public class SpecOrbOverlay extends Overlay
{
	private static final Stroke OUTLINE_STROKE = new BasicStroke(2f);

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
		if (!plugin.isOrbHighlightActive())
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
		float factor = plugin.orbPulseFactor();
		int peakAlpha = Math.round(color.getAlpha() * factor);
		if (peakAlpha <= 0)
		{
			return null;
		}

		Object oldAa = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D orbShape = new Ellipse2D.Float(bounds.x, bounds.y, bounds.width, bounds.height);

		if (config.orbAura())
		{
			drawAura(graphics, bounds, orbShape, color, peakAlpha);
		}
		if (config.orbFill())
		{
			int alpha = Math.round(peakAlpha * Math.max(0, Math.min(100, config.orbFillOpacity())) / 100f);
			if (alpha > 0)
			{
				graphics.setColor(withAlpha(color, alpha));
				graphics.fill(orbShape);
			}
		}
		if (config.orbOutline())
		{
			Stroke old = graphics.getStroke();
			graphics.setStroke(OUTLINE_STROKE);
			graphics.setColor(withAlpha(color, peakAlpha));
			graphics.draw(orbShape);
			graphics.setStroke(old);
		}

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			oldAa == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : oldAa);
		return null;
	}

	/** Soft halo from the orb edge outwards, fading to transparent at {@code auraSize} px. */
	private void drawAura(Graphics2D graphics, Rectangle bounds, Ellipse2D orbShape, Color color, int peakAlpha)
	{
		int auraSize = Math.max(1, config.orbAuraSize());
		float innerRadius = Math.max(bounds.width, bounds.height) / 2f;
		float outerRadius = innerRadius + auraSize;
		Point2D center = new Point2D.Float(bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);

		Ellipse2D outer = new Ellipse2D.Float(
			(float) center.getX() - outerRadius, (float) center.getY() - outerRadius,
			outerRadius * 2, outerRadius * 2);
		Area halo = new Area(outer);
		halo.subtract(new Area(orbShape));

		float innerFraction = innerRadius / outerRadius;
		Paint oldPaint = graphics.getPaint();
		graphics.setPaint(new RadialGradientPaint(
			center, outerRadius,
			new float[]{innerFraction, 1f},
			new Color[]{withAlpha(color, peakAlpha), withAlpha(color, 0)}));
		graphics.fill(halo);
		graphics.setPaint(oldPaint);
	}

	private static Color withAlpha(Color color, int alpha)
	{
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, alpha)));
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
