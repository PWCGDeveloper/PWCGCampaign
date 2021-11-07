package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.CountryDesignator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.ground.builder.AAASpotterBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class FlightSpotterBuilder
{

    public static void createSpotters(IFlight playerFlight, FlightInformation flightInformation) throws PWCGException
    {
        for (MissionPoint missionPoint : playerFlight.getWaypointPackage().getFlightMissionPoints())
        {
            if (missionPoint.getAction() == WaypointAction.WP_ACTION_PATROL)
            {
                addSpotter(playerFlight, missionPoint.getPosition());
            }
        }
    }

    public static void createSpottersForStrategicIntercept(IFlight playerFlight, List<IFlight> opposingFlights) throws PWCGException
    {
        for (IFlight opposingFlight : opposingFlights)
        {
            Coordinate targetPosition = opposingFlight.getWaypointPackage().getTargetWaypoints().get(0).getPosition();
            Coordinate opposingFightStartPosition = opposingFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_INGRESS).getPosition();
            double angle = MathUtils.calcAngle(targetPosition, opposingFightStartPosition);

            int spotterCount = 0;
            while (spotterCount < 6)
            {
                Coordinate spotterPosition = MathUtils.calcNextCoord(targetPosition, angle, 10000 * spotterCount);
                ICountry positionCountry = CountryDesignator.determineCountry(spotterPosition, playerFlight.getCampaign().getDate());
                if (positionCountry.getCountry() != playerFlight.getSquadron().getCountry().getCountry())
                {
                    return;
                }

                addSpotter(playerFlight, spotterPosition);
                ++spotterCount;
            }
        }
    }

    private static void addSpotter(IFlight playerFlight, Coordinate spotterPosition) throws PWCGException
    {
        GroundUnitCollection spotter = AAASpotterBuilder.createAAASpotterBattery(spotterPosition, playerFlight.getCampaign(),
                playerFlight.getSquadron().getCountry());
        if (spotter != null)
        {
            playerFlight.getMission().getGroundUnitBuilder().addFlightSpecificGroundUnit(spotter);
        }
    }

}
