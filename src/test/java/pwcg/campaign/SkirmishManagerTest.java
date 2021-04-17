package pwcg.campaign;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishManager;
import pwcg.campaign.skirmish.SkirmishProfileType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class SkirmishManagerTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void verifyArnhemParaDropSkirmishes() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        
        assert (skirmishManager.getSkirmishes().getSkirmishes().size() == 18);
        
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        assert (skirmishesForDate.size() == 3);
        
        for (Skirmish skirmish : skirmishesForDate)
        {
            assert(skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_PARA_DROP);
        }
    }

    @Test
    public void verifyArnhemCargoDropSkirmishes() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440921"));
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
                
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        assert (skirmishesForDate.size() == 3);
        
        for (Skirmish skirmish : skirmishesForDate)
        {
            assert(skirmish.getProfileType() == SkirmishProfileType.SKIRMISH_PROFILE_CARGO_DROP);
        }
    }

}
