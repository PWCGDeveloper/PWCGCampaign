package pwcg.campaign.mode;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignActiveSinglePlayer implements ICampaignActive 
{
    private Campaign campaign;
    
    public CampaignActiveSinglePlayer(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public boolean isCampaignActive() throws PWCGException
    {
        if (campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().size() > 0)
        {
            return true;
        }
        
        return false;
    }
}
