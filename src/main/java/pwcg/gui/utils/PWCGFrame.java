package pwcg.gui.utils;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pwcg.gui.dialogs.MonitorSupport;

public class PWCGFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static PWCGFrame frame = null;

	private JPanel panel = null;
	
	public static PWCGFrame getInstance()
	{
		if (frame == null)
		{
			frame = new PWCGFrame();
		}
		
		return frame;
	}
	
	private PWCGFrame()
	{
		super();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = MonitorSupport.getPWCGMonitorSize();
		this.setSize(screenSize);
		this.setState(JFrame.MAXIMIZED_BOTH);
		
		this.setVisible(true);
	}
		
	/**
	 * @param newPanel
	 * @param panelSection
	 */
	public void setPanel(JPanel newPanel)
	{
	    if (panel != null)
	    {
	    	this.remove(panel);
	    }
	    
	    panel = newPanel;
	    add(panel);
	    
	    panel.revalidate();
	    panel.repaint();
	}
}
