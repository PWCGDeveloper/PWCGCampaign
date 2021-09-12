package pwcg.gui.utils;

import javafx.scene.paint.Color;

import javafx.scene.control.Button;
import javax.swing.plaf.basic.BasicScrollBarUI;

import pwcg.gui.colors.ColorMap;

public class PWCGScrollBarUI extends BasicScrollBarUI
{
    @Override
    protected Button createDecreaseButton(int orientation)
    {
        Color color = ColorMap.PAPER_BACKGROUND;
        Button button = super.createDecreaseButton(orientation);
        button.setBackground(color);

        return button;
    }

    @Override
    protected Button createIncreaseButton(int orientation)
    {
        Color color = ColorMap.PAPER_BACKGROUND;
        Button button = super.createIncreaseButton(orientation);
        button.setBackground(color);

        return button;
    }
}
