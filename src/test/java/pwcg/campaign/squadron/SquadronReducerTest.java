package pwcg.campaign.squadron;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.CompanyReducer;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SquadronReducerTest
{    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void anomaliesRemoved() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_52_PROFILE_STALINGRAD);
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allSquadrons = squadronManager.getAllCompanies();
        List<Company> squadronsWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Company squadron : squadronsWithoutAnomalies)
        {
            Assertions.assertTrue (squadron.getCompanyId() != 20115021);
            Assertions.assertTrue (squadron.getCompanyId() != 20111051);
        }
    }

    @Test
    public void jg51NotAnAnomaly() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_11_PROFILE);        
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allSquadrons = squadronManager.getAllCompanies();
        List<Company> squadronsWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        boolean jg51Found = false;
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Company squadron : squadronsWithoutAnomalies)
        {
            Assertions.assertTrue (squadron.getCompanyId() != 20115021);
            
            if (squadron.getCompanyId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        Assertions.assertTrue (jg51Found);
    }
    

    @Test
    public void noAnomaliesRemoved() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_11_PROFILE);        
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> allSquadrons = squadronManager.getAllCompanies();
        List<Company> squadronsWithoutAnomalies = CompanyReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        boolean gruppo21Found = false;
        boolean jg51Found = false;
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Company squadron : squadronsWithoutAnomalies)
        {            
            if (squadron.getCompanyId() != 20115021)
            {
                gruppo21Found = true;
            }
            
            if (squadron.getCompanyId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        Assertions.assertTrue (gruppo21Found);
        Assertions.assertTrue (jg51Found);
    }
}
