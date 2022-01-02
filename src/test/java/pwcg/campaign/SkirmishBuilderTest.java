package pwcg.campaign;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionHumanParticipants;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SkirmishBuilderTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
    }
    
    @Test
    public void testNoSkirmish() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19441010"));

        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        Assertions.assertTrue (skirmishes.size() == 0);
    }

    @Test
    public void singlePlayerSkirmishArnhemStartTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));

        Skirmish skirmish =createMissionAtSkirmish(campaign);
        Assertions.assertTrue (skirmish != null);
    }

    @Test
    public void singlePlayerSkirmishArnhemEndTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440928"));

        Skirmish skirmish =createMissionAtSkirmish(campaign);
        Assertions.assertTrue (skirmish != null);
    }

    private Skirmish createMissionAtSkirmish(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = makeParticipatingPlayers(campaign);
       
        SkirmishBuilder skirmishBuilder = new SkirmishBuilder(campaign, participatingPlayers);
        Skirmish skirmish = skirmishBuilder.chooseBestSkirmish();

        return skirmish;
    }

    private MissionHumanParticipants makeParticipatingPlayers(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        CrewMember player = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(player);
        return participatingPlayers;
    }


}
