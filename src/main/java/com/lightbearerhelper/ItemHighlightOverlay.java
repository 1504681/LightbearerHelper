package com.lightbearerhelper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

/**
 * Draws the Lightbearer / other ring / spec item highlights on inventory and worn-equipment items.
 */
public class ItemHighlightOverlay extends WidgetItemOverlay
{
	private static final Stroke BOX_STROKE = new BasicStroke(2f);
	private static final int UNDERLINE_HEIGHT = 2;
	private static final int FILL_ALPHA = 90;

	private final ItemManager itemManager;
	private final LightbearerHelperPlugin plugin;
	private final Map<Long, BufferedImage> outlineCache = new HashMap<>();

	@Inject
	public ItemHighlightOverlay(ItemManager itemManager, LightbearerHelperPlugin plugin)
	{
		this.itemManager = itemManager;
		this.plugin = plugin;
		showOnInventory();
		showOnEquipment();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		Highlight highlight = plugin.getHighlight(itemId, isEquipmentInterface(widgetItem));
		if (highlight == null)
		{
			return;
		}
		Rectangle bounds = widgetItem.getCanvasBounds();
		if (bounds == null)
		{
			return;
		}
		Color color = highlight.getColor();
		switch (highlight.getStyle())
		{
			case OUTLINE:
			{
				BufferedImage outline = getOutline(itemId, widgetItem.getQuantity(), color);
				if (outline != null)
				{
					graphics.drawImage(outline, bounds.x, bounds.y, null);
				}
				break;
			}
			case BOX:
			{
				Stroke old = graphics.getStroke();
				graphics.setStroke(BOX_STROKE);
				graphics.setColor(color);
				graphics.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
				graphics.setStroke(old);
				break;
			}
			case FILL:
			{
				int alpha = Math.min(color.getAlpha(), FILL_ALPHA);
				graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
				graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
				graphics.setColor(color);
				graphics.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
				break;
			}
			case UNDERLINE:
			{
				graphics.setColor(color);
				graphics.fillRect(bounds.x, bounds.y + bounds.height, bounds.width, UNDERLINE_HEIGHT);
				break;
			}
			default:
				break;
		}
	}

	void invalidateCache()
	{
		outlineCache.clear();
	}

	private static boolean isEquipmentInterface(WidgetItem widgetItem)
	{
		Widget widget = widgetItem.getWidget();
		return widget != null && WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.WORNITEMS;
	}

	private BufferedImage getOutline(int itemId, int quantity, Color color)
	{
		long key = ((long) itemId << 40) | ((long) (quantity & 0xFF) << 32) | (color.getRGB() & 0xFFFFFFFFL);
		BufferedImage image = outlineCache.get(key);
		if (image == null)
		{
			image = itemManager.getItemOutline(itemId, quantity, color);
			if (image != null)
			{
				outlineCache.put(key, image);
			}
		}
		return image;
	}
}
