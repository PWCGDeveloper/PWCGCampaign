package pwcg.campaign.personnel;

import pwcg.aar.campaign.update.CampaignUpdateNewSquadronEquipper;
import pwcg.aar.campaign.update.CampaignUpdateNewSquadronStaffer;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class InitialSquadronBuilder
{
    public void buildNewSquadrons(Campaign campaign) throws PWCGException
    {
        CampaignUpdateNewSquadronStaffer newSquadronStaffer = new CampaignUpdateNewSquadronStaffer(campaign);
        newSquadronStaffer.staffNewSquadrons();

        CampaignUpdateNewSquadronEquipper newSquadronEquipper = new CampaignUpdateNewSquadronEquipper(campaign);
        newSquadronEquipper.equipNewSquadrons();
    }
}
