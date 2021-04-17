package pwcg.mission;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class MissionCenterBuilderSkirmishTest
{    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testNoSkirmish() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        PWCGContext.getInstance().setCampaign(campaign);

        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        assert (skirmishes.size() == 0);
    }

    @Test
    public void singlePlayerMissionBoxArnhemEarlyTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));

        createMissionAtSkirmish(campaign);
    }

    @Test
    public void singlePlayerMissionBoxArnhemLateTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440928"));

        createMissionAtSkirmish(campaign);
    }

    private void createMissionAtSkirmish(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = makeParticipatingPlayers(campaign);
                
        List<Skirmish> skirmishes = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        assert (skirmishes.size() > 0);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, skirmishes.get(0));
        
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
        Coordinate missionBoxCenter = missionBorders.getCenter();
                
        assert(skirmishes.get(0).getCoordinateBox().isInBox(missionBoxCenter));
    }

    private MissionHumanParticipants makeParticipatingPlayers(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        return participatingPlayers;
    }

}
