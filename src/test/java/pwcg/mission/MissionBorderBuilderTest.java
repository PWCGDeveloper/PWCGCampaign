package pwcg.mission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionBorderBuilderTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().setCurrentMap(FrontMapIdentifier.STALINGRAD_MAP);
    }

    @Test
    public void singlePlayerMissionBoxTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        PWCGContext.getInstance().setCampaign(campaign);
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();

        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
            
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();
            Coordinate missionBoxCenter = missionBorders.getCenter();
            
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            Coordinate playerSquadronLocation = playerSquadron.determineCurrentPosition(campaign.getDate());
            
            double distanceToMission = MathUtils.calcDist(missionBoxCenter, playerSquadronLocation);
            assert(distanceToMission < 85000);
        }
    }

    @Test
    public void multiPlayerMissionBoxTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(coopCampaign);
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (SquadronMember player: coopCampaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
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
