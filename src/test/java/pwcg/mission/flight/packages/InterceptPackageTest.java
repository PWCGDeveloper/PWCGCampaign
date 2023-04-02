package pwcg.mission.flight.packages;

import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionFlights;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.PwcgTestBase;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class InterceptPackageTest extends PwcgTestBase
{
    public InterceptPackageTest() throws PWCGException
    {
        super (PWCGProduct.BOS);
    }

    @Test
    public void playerPackageTest() throws PWCGException
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_26_PROFILE_WEST);
        MissionFlights missionFlights = buildFlight(campaign);
        
        List<IFlight> opposingFlights = missionFlights.getNecessaryFlightsByType(NecessaryFlightType.OPPOSING_FLIGHT);
        assert(opposingFlights.size() == 1);

        IFlight playerFlight = missionFlights.getPlayerFlights().get(0);
        verifyInterceptOpposingIsCloseToPlayer(playerFlight, opposingFlights.get(0));        
   }

    private MissionFlights buildFlight(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = TestMissionBuilderUtility.buildTestParticipatingHumans(campaign);

        Squadron playerSquadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();
        MissionSquadronFlightTypes playerFlightTypes = MissionSquadronFlightTypes.buildPlayerFlightType(FlightTypes.INTERCEPT, playerSquadron);

        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers, null, playerFlightTypes);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox();

        Mission mission = TestMissionBuilderUtility.createTestMission(campaign, participatingPlayers, missionBorders, MissionProfile.DAY_TACTICAL_MISSION);
        mission.generate(playerFlightTypes);

        campaign.setCurrentMission(mission);
        return mission.getFlights();
    }

    private void verifyInterceptOpposingIsCloseToPlayer(IFlight flight, IFlight opposingFlight) throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = flight.getWaypointPackage().getTargetWaypoints();
        List<MissionPoint> opposingTargetWaypoints = opposingFlight.getWaypointPackage().getFlightMissionPoints();
        
        boolean interceptIsCloseToTarget = false;
        for (McuWaypoint waypoint : targetWaypoints)
        {
            for (MissionPoint opposingWaypoint : opposingTargetWaypoints)
            {
                double distanceFromBalloon = MathUtils.calcDist(waypoint.getPosition(), opposingWaypoint.getPosition());
                if (distanceFromBalloon < 30000)
                {
                    interceptIsCloseToTarget = true;
                }
            }
        }
        assert(interceptIsCloseToTarget == true);
    }
}
