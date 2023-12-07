package pwcg.aar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARCoordinatorTransferTest
{
    private Campaign campaign;    
    private static AARCoordinator aarCoordinator;
    private static AARTestExpectedResults expectedResults;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        expectedResults = new AARTestExpectedResults(campaign);
        aarCoordinator = AARCoordinator.getInstance();
        aarCoordinator.reset(campaign);
    }

    @Test
    public void runMissionAARLeave () throws PWCGException
    {
        aarCoordinator.submitLeave(campaign, 20);
        expectedResults.buildExpectedResultsFromAARContext(aarCoordinator.getAarContext());

        BoSAARResultValidator resultValidator = new BoSAARResultValidator(expectedResults);
        resultValidator.validateLeave();
    }
}
