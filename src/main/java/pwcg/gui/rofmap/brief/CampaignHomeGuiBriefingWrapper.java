package pwcg.gui.rofmap.brief;

import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.home.CampaignHomeScreen;

public class CampaignHomeGuiBriefingWrapper
{
    private CampaignHomeScreen campaignHomeGui = null;
    
    public CampaignHomeGuiBriefingWrapper(CampaignHomeScreen campaignHomeGui)
    {
        this.campaignHomeGui =  campaignHomeGui;
    }

    public void refreshCampaignPage() throws PWCGException
    {
        if (campaignHomeGui != null)
        {
            campaignHomeGui.refreshInformation();
        }
    }
}
