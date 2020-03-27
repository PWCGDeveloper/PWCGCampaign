package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.ground.builder.AAASpotterBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class FlightSpotterBuilder
{

    public static void createSpotters(IFlight playerFlight, IFlightInformation flightInformation) throws PWCGException
    {
        AAASpotterBuilder spotterBuilder = new AAASpotterBuilder(flightInformation);
        for (MissionPoint missionPoint : playerFlight.getWaypointPackage().getFlightMissionPoints())
        {
            if (missionPoint.getAction() == WaypointAction.WP_ACTION_PATROL)
            {
                IGroundUnitCollection spotter = spotterBuilder.createAAASpotterBattery(missionPoint.getPosition());
                if (spotter != null)
                {
                    playerFlight.addLinkedGroundUnit(spotter);
                }
            }
        }
    }

}
