package pwcg.gui.utils;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import pwcg.gui.colors.ColorMap;

public class PWCGJButton extends JButton
{
    private static final long serialVersionUID = 1L;
    
    private Color pressedBackgroundColor = ColorMap.NEWSPAPER_BACKGROUND;

    public PWCGJButton()
    {
        super();
    }

    public PWCGJButton(String text)
    {
        super(text);
        super.setContentAreaFilled(false);
    }

    public PWCGJButton(ImageIcon image)
    {
        super(image);
        super.setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (getModel().isPressed())
        {
            g.setColor(pressedBackgroundColor);
        }
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b)
    {
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
