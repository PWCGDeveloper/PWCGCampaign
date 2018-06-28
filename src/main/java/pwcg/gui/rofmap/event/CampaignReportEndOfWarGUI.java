package pwcg.gui.rofmap.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignReportEndOfWarGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public CampaignReportEndOfWarGUI()
	{
		super(ContextSpecificImages.imagesNewspaper() + "EndOfWar.jpg");
		makeGUI();		
	}
	
	private void makeGUI() 
	{
	}
	
	public void actionPerformed(ActionEvent ae)
	{
	}
	
	public void makeVisible(boolean visible) 
	{
	}
}
