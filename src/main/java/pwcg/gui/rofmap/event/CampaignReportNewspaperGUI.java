package pwcg.gui.rofmap.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pwcg.aar.ui.events.model.NewspaperEvent;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignReportNewspaperGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public CampaignReportNewspaperGUI(NewspaperEvent newspaperEvent)
	{
		super(newspaperEvent.getNewspaperFile());
		setLayout(null);
		makeGUI() ;
	}
	
	private void makeGUI() 
	{
        setSize(800, 640);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
	}
	
	public void makeVisible(boolean visible) 
	{
	}
}
