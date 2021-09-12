package pwcg.gui.utils;

import javafx.scene.text.Font;
import javafx.scene.text.FontMetrics;
import java.awt.Graphics;

public class TextGraphicsMeasurement
{
    public static int measureTextWidth(Graphics graphics, Font font, String text)
    {
        FontMetrics metrics = graphics.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        return textWidth;
    }
}
