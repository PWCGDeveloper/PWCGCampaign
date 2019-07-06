package pwcg.mission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionBorderBuilderTest
{
    private Campaign campaign;
    private Campaign coopCampaign;
    private MissionHumanParticipants participatingPlayers;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_STALINGRAD);
        coopCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.COOP_PROFILE);
        participatingPlayers = new MissionHumanParticipants();
    }

    @Test
    public void singlePlayerMissionBoxTest() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
        participatingPlayers.addSquadronMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
            Coordinate missionBoxCenter = missionBorders.getCenter();
            
            Squadron playerSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Coordinate playerSquadronLocation = playerSquadron.determineCurrentPosition(campaign.getDate());
            
            double distanceToMission = MathUtils.calcDist(missionBoxCenter, playerSquadronLocation);
            assert(distanceToMission < 80000);
        }
    }


    @Test
    public void multiPlayerMissionBoxTest() throws PWCGException
    {
        for (SquadronMember player: campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(coopCampaign, participatingPlayers);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
            Coordinate missionBoxCenter = missionBorders.getCenter();
            assert(missionBoxCenter.getXPos() > 0.0);
            assert(missionBoxCenter.getZPos() > 0.0);
        }
    }

}
