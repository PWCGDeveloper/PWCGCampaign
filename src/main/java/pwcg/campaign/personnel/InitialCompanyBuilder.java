package pwcg.campaign.personnel;

import pwcg.aar.campaign.update.CampaignUpdateNewCompanyEquipper;
import pwcg.aar.campaign.update.CampaignUpdateNewCompanyStaffer;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class InitialCompanyBuilder
{
    public void buildNewCompanies(Campaign campaign) throws PWCGException
    {
        CampaignUpdateNewCompanyStaffer newSquadronStaffer = new CampaignUpdateNewCompanyStaffer(campaign);
        newSquadronStaffer.staffNewCompanies();

        CampaignUpdateNewCompanyEquipper newSquadronEquipper = new CampaignUpdateNewCompanyEquipper(campaign);
        newSquadronEquipper.equipNewSquadrons();
    }
}
