package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;

import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;

public class CampaignGeneratorDataEntryEmpty extends ImageResizingPanel
{
	private static final long serialVersionUID = 1L;

	public CampaignGeneratorDataEntryEmpty() 
	{
        super(ContextSpecificImages.menuPathMain() + "CampaignGenCenter.jpg");
		this.setLayout(new BorderLayout());
	}
}
