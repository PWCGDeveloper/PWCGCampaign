package pwcg.gui.campaign.home;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignHomeContext
{
    private static Campaign campaign;
    
    public static void setCampaign(Campaign newCampaign)
    {
        campaign = newCampaign;
    }
    
    
    public static Campaign getCampaign()
    {
        return campaign;
    }
    
    public static Campaign writeCampaign() throws PWCGException
    {
        campaign.write();
        campaign.reopen();
        return campaign;
    }
    
}
