package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

/**
 * This class eliminates the obnoxious background color on a button press
 * 
 * @author Patrick Wilson
 *
 */
public class PWCGButtonNoBackground extends JButton
{
    private static final long serialVersionUID = 1L;
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public PWCGButtonNoBackground()
    {
        this(null);
    }

    public PWCGButtonNoBackground(String text)
    {
        super(text);
        super.setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b)
    {
    }

    public Color getHoverBackgroundColor()
    {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor)
    {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor()
    {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor)
    {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
