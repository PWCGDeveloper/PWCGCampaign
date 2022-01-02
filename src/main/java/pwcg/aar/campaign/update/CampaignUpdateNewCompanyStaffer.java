package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyStaffer;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewCompanyStaffer
{
    private Campaign campaign;
    private List<Integer> squadronsAdded = new ArrayList<>();
    
    public CampaignUpdateNewCompanyStaffer (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> staffNewSquadrons() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Company> squadronsToStaff = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Company squadron : squadronsToStaff)
        {
            if (campaign.getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId()) == null)
            {
                InitialCompanyStaffer squadronStaffer = new InitialCompanyStaffer(campaign, squadron);
                CompanyPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
                campaign.getPersonnelManager().addPersonnelForSquadron(squadronPersonnel);
                squadronsAdded.add(squadron.getSquadronId());
            }
        }
        
        return squadronsAdded;
    }
}
