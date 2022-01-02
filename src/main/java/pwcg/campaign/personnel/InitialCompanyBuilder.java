package pwcg.campaign.personnel;

import pwcg.aar.campaign.update.CampaignUpdateNewCompanyEquipper;
import pwcg.aar.campaign.update.CampaignUpdateNewCompanyStaffer;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class InitialCompanyBuilder
{
    public void buildNewSquadrons(Campaign campaign) throws PWCGException
    {
        CampaignUpdateNewCompanyStaffer newSquadronStaffer = new CampaignUpdateNewCompanyStaffer(campaign);
        newSquadronStaffer.staffNewSquadrons();

        CampaignUpdateNewCompanyEquipper newSquadronEquipper = new CampaignUpdateNewCompanyEquipper(campaign);
        newSquadronEquipper.equipNewSquadrons();
    }
}
