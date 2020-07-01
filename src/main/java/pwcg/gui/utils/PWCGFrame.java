package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pwcg.gui.dialogs.PWCGMonitorSupport;

public class PWCGFrame
{	
    private static PWCGFrame pwcgFrame = null;
    
    private JFrame frame = new JFrame();
    private JPanel base = new JPanel();
	
	public static PWCGFrame getInstance()
	{
		if (pwcgFrame == null)
		{
		    pwcgFrame = new PWCGFrame();
		}
		
		return pwcgFrame;
	}
	
	private PWCGFrame()
	{
		super();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = PWCGMonitorSupport.getPWCGMonitorSize();
		frame.setSize(screenSize);
		frame.setState(JFrame.MAXIMIZED_BOTH);		
		
		base.setLayout(new BorderLayout());
        base.setBackground(Color.DARK_GRAY);
		
        frame.setVisible(false);
        frame.add(base);
 	}

	public void setPanel(JPanel newPanel)
	{
        base.removeAll();
	    base.add(newPanel, BorderLayout.CENTER);
	    base.revalidate();
	    base.repaint();
	    
	    if (!frame.isVisible())
	    {
	        frame.setVisible(true);
	    }
	}

    public Rectangle getBounds()
    {
        return frame.getBounds();
    }
}
