package pwcg.aar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARCoordinatorTransferTest
{
    private Campaign campaign;    
    private AARCoordinator aarCoordinator;
    private ExpectedResults expectedResults;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
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
