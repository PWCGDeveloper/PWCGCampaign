package pwcg.gui.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import pwcg.gui.PwcgThreePanelUI;
import pwcg.gui.dialogs.PWCGMonitorSupport;

public class PWCGFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static PWCGFrame frame = null;
	
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
		
		Dimension screenSize = PWCGMonitorSupport.getPWCGMonitorSize();
		this.setSize(screenSize);
		this.setState(JFrame.MAXIMIZED_BOTH);		

		this.setVisible(true);
	}

	public void setPanel(PwcgThreePanelUI newPanel)
	{        
        frame.getContentPane().add(newPanel);
        
        if (newPanel.getLeftPanel() != null)
            frame.add(newPanel.getLeftPanel(), BorderLayout.WEST);

        if (newPanel.getCenterPanel() != null)
            frame.add(newPanel.getCenterPanel(), BorderLayout.CENTER);
        
        if (newPanel.getRightPanel() != null)
            frame.add(newPanel.getRightPanel(), BorderLayout.EAST);

        frame.revalidate();
        frame.repaint();        
	}

    public void clearPanel()
    {
        getContentPane().removeAll();
    }
}
