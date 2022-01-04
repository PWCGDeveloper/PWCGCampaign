package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialCompanyStaffer;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewCompanyStaffer
{
    private Campaign campaign;
    private List<Integer> squadronsAdded = new ArrayList<>();
    
    public CampaignUpdateNewCompanyStaffer (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> staffNewCompanies() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> squadronsToStaff = squadronManager.getActiveCompanies(campaign.getDate());
        for (Company squadron : squadronsToStaff)
        {
            if (campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId()) == null)
            {
                InitialCompanyStaffer squadronStaffer = new InitialCompanyStaffer(campaign, squadron);
                CompanyPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
                campaign.getPersonnelManager().addPersonnelForCompany(squadronPersonnel);
                squadronsAdded.add(squadron.getCompanyId());
            }
        }
        
        return squadronsAdded;
    }
}
