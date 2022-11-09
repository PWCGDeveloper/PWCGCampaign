package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishManager;
import pwcg.campaign.skirmish.SkirmishProfileType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SkirmishManagerTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
    }

    @Test
    public void verifyArnhemParaDropSkirmishes() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        
        Assertions.assertTrue (skirmishManager.getSkirmishes().getSkirmishes().size() == 18);
        
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        Assertions.assertTrue (skirmishesForDate.size() == 3);
        
        for (Skirmish skirmish : skirmishesForDate)
        {
            assert(skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_PARA_DROP);
        }
    }

    @Test
    public void verifyArnhemCargoDropSkirmishes() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440921"));
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
                
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        Assertions.assertTrue (skirmishesForDate.size() == 3);
        
        for (Skirmish skirmish : skirmishesForDate)
        {
            assert(skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_CARGO_DROP);
        }
    }

}
