package pwcg.gui.utils;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

import pwcg.gui.colors.ColorMap;

public class PWCGScrollBarUI extends BasicScrollBarUI
{
    @Override
    protected JButton createDecreaseButton(int orientation)
    {
        Color color = ColorMap.PAPER_BACKGROUND;
        JButton button = super.createDecreaseButton(orientation);
        button.setBackground(color);

        return button;
    }

    @Override
    protected JButton createIncreaseButton(int orientation)
    {
        Color color = ColorMap.PAPER_BACKGROUND;
        JButton button = super.createIncreaseButton(orientation);
        button.setBackground(color);

        return button;
    }
}
