package pwcg.aar.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.AARCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorTransferTest
{
    private Campaign campaign;    
    private AARCoordinator aarCoordinator;
    private ExpectedResults expectedResults;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(CampaignCacheBoS.JG_51_PROFILE);
        expectedResults = new ExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarCoordinator.submitLeave(campaign, 20);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());

        AARResultValidator resultValidator = new AARResultValidator(expectedResults);
        resultValidator.validateLeave();
    }
}
