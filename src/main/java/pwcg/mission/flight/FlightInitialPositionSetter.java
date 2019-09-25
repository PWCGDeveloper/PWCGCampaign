package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.McuWaypoint;

public class FlightInitialPositionSetter
{
    private Flight flight;
    
    public FlightInitialPositionSetter(Flight flight)
    {
        this.flight = flight;
    }
    
    public void setFlightInitialPosition() throws PWCGException
    {
        if (flight.getFlightInformation().isPlayerFlight())
        {
            setPlayerFlightInitialPosition();
        }
        else
        {
            FlightPositionHelperAirStart.createPlanePositionCloseToFirstWP(flight);
        }
    }
    
    private void setPlayerFlightInitialPosition() throws PWCGException
    {
        if (flight.getFlightInformation().isAirStart())
        {
            advancePlayerAirStart();
        }
        
        FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(flight.getCampaign(), flight);
        flightPositionHelperPlayerStart.createPlayerPlanePosition();
    }

    protected void advancePlayerAirStart()
    {
        List<McuWaypoint> keptWaypoints = new ArrayList<McuWaypoint>();
        List<McuWaypoint> waypoints = flight.getWaypointPackage().getWaypointsForLeadPlane();
        boolean keepIt = false;
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_INGRESS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS) ||
                waypoint.isTargetWaypoint())
            {
                keepIt = true;
            }

            if (keepIt)
            {
                keptWaypoints.add(waypoint);
            }
        }

        flight.getWaypointPackage().setWaypoints(keptWaypoints);
    }
}
