package pwcg.mission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionBorderBuilderTest
{
    private Campaign campaign;
    private Campaign campaignAntiShipping;
    private Campaign coopCampaign;
    private MissionHumanParticipants participatingPlayers;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaignAntiShipping = CampaignCache.makeCampaign(SquadronTestProfile.REGIMENT_321_PROFILE);
        coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        participatingPlayers = new MissionHumanParticipants();
    }

    @Test
    public void singlePlayerMissionBoxTest() throws PWCGException
    {
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(FlightTypes.ANY);
            Coordinate missionBoxCenter = missionBorders.getCenter();
            
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
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
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(FlightTypes.ANY);
            Coordinate missionBoxCenter = missionBorders.getCenter();
            assert(missionBoxCenter.getXPos() > 0.0);
            assert(missionBoxCenter.getZPos() > 0.0);
        }
    }

    @Test
    public void scrambleMissionBoxTest() throws PWCGException
    {
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(FlightTypes.SCRAMBLE);
            Coordinate missionBoxCenter = missionBorders.getCenter();

            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            double distanceFromAirfieldToMissionCenter = MathUtils.calcDist(playerSquadron.determineCurrentPosition(campaign.getDate()), missionBoxCenter);
                        
            assert(distanceFromAirfieldToMissionCenter < 20000);
        }
    }

    @Test
    public void antiShippingMissionBoxTest() throws PWCGException
    {
        SquadronMember player = campaign.findReferencePlayer();
        participatingPlayers.addSquadronMember(player);
        
        for (int i = 0; i < 10; ++i)
        {
            MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaignAntiShipping, participatingPlayers);
            CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(FlightTypes.ANTI_SHIPPING_BOMB);
            Coordinate missionBoxCenter = missionBorders.getCenter();

            PWCGMap map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.KUBAN_MAP);
            ShippingLaneManager shippingLaneManager = map.getShippingLaneManager();
            Squadron playerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            ShippingLane shippingLane = shippingLaneManager.getClosestShippingLaneBySide(playerSquadron.determineCurrentPosition(campaignAntiShipping.getDate()), playerSquadron.determineEnemySide());
            double distanceFromSeaLaneToMissionCenter = MathUtils.calcDist(shippingLane.getShippingLaneBox().getCenter(), missionBoxCenter);
            assert(distanceFromSeaLaneToMissionCenter < 20000);
        }
    }
}
