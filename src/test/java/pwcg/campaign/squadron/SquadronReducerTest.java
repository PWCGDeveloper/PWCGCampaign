package pwcg.campaign.squadron;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class SquadronReducerTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void anomaliesRemoved() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_52_PROFILE_STALINGRAD);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> allSquadrons = squadronManager.getAllSquadrons();
        List<Squadron> squadronsWithoutAnomalies = SquadronReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Squadron squadron : squadronsWithoutAnomalies)
        {
            assert (squadron.getSquadronId() != 20115021);
            assert (squadron.getSquadronId() != 20111051);
        }
    }

    @Test
    public void jg51NotAnAnomaly() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_11_PROFILE);        
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> allSquadrons = squadronManager.getAllSquadrons();
        List<Squadron> squadronsWithoutAnomalies = SquadronReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        boolean jg51Found = false;
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Squadron squadron : squadronsWithoutAnomalies)
        {
            assert (squadron.getSquadronId() != 20115021);
            
            if (squadron.getSquadronId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        assert (jg51Found);
    }
    

    @Test
    public void noAnomaliesRemoved() throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_11_PROFILE);        
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> allSquadrons = squadronManager.getAllSquadrons();
        List<Squadron> squadronsWithoutAnomalies = SquadronReducer.reduceToNoAnomalies(allSquadrons, campaign.getDate());
        
        boolean gruppo21Found = false;
        boolean jg51Found = false;
        
        assert(squadronsWithoutAnomalies.size() > 30);
        for (Squadron squadron : squadronsWithoutAnomalies)
        {            
            if (squadron.getSquadronId() != 20115021)
            {
                gruppo21Found = true;
            }
            
            if (squadron.getSquadronId() != 20111051)
            {
                jg51Found = true;
            }
        }
        
        assert (gruppo21Found);
        assert (jg51Found);
    }
}
