package pwcg.mission.flight.packages;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionBorderBuilder;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.strategicintercept.StrategicInterceptPackage;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

public class StrategicInterceptPackageTest
{
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void playerPackageTest() throws PWCGException
    {
        IFlight flight = buildFlight();
        for (IFlight opposingFlight : flight.getLinkedFlights().getLinkedFlights())
        {
            verifyInterceptOpposingIsCloseToPlayer(flight, opposingFlight);        
        }
   }

    private IFlight buildFlight() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_26_PROFILE_WEST);
        MissionHumanParticipants participatingPlayers = TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign);
        
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(FlightTypes.STRATEGIC_INTERCEPT);

        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        campaign.setCurrentMission(mission);
        mission.generateProfile();

        StrategicInterceptPackage flightPackage = new StrategicInterceptPackage();
        boolean isPlayerFlight = true;
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.JG_26_PROFILE_WEST.getSquadronId());
        FlightBuildInformation flightBuildInformation = new FlightBuildInformation(mission, squadron, isPlayerFlight);
        IFlight flight = flightPackage.createPackage(flightBuildInformation);
        return flight;
    }

    private void verifyInterceptOpposingIsCloseToPlayer(IFlight flight, IFlight opposingFlight)
    {
        List<McuWaypoint> targetWaypoints = flight.getWaypointPackage().getTargetWaypoints();
        List<McuWaypoint> opposingTargetWaypoints = opposingFlight.getWaypointPackage().getTargetWaypoints();
        
        boolean interceptIsCloseToTarget = false;
        for (McuWaypoint waypoint : targetWaypoints)
        {
            for (McuWaypoint opposingWaypoint : opposingTargetWaypoints)
            {
                double distanceFromPlayerFlight = MathUtils.calcDist(waypoint.getPosition(), opposingWaypoint.getPosition());
                if (distanceFromPlayerFlight < 15000)
                {
                    interceptIsCloseToTarget = true;
                }
            }
        }
        assert(interceptIsCloseToTarget == true);
    }
}
