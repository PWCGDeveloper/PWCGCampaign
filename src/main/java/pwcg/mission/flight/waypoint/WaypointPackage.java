package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPackage
{
    private Map<Integer, List<McuWaypoint>> flightWaypointsByPlane = new HashMap<Integer, List<McuWaypoint>>();
    private Flight flight = null;
    private McuTimer waypointTimer = null;

    public WaypointPackage(Flight flight)
    {
        this.flight = flight;
    }

    public void initialize(List<McuWaypoint> waypoints) throws PWCGException
    {
        createWaypointTimer();
        flightWaypointsByPlane.clear();
        flightWaypointsByPlane.put(flight.getPlanes().get(0).getIndex(), waypoints);    
        setFirstWPTarget();        
    }
    
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        waypointTimer.write(writer);
        for (List<McuWaypoint> waypoints : flightWaypointsByPlane.values())
        {
            for (McuWaypoint waypoint : waypoints)
            {
                waypoint.write(writer);
            }
        }
    }

    public McuWaypoint getWaypointByType(WaypointType waypointType) throws PWCGException
    {
        for (McuWaypoint waypoint : getWaypointsForLeadPlane())
        {
            if (waypoint.getWaypointType() == waypointType)
            {
                return waypoint;
            }
        }
        
        throw new PWCGException("Waypoint of expected type not found : " + waypointType);
    }

    public List<McuWaypoint> getWaypointsForLeadPlane()
    {
        return flightWaypointsByPlane.get(flight.getPlanes().get(0).getIndex());
    }

    public List<McuWaypoint> getWaypointsForPlane(PlaneMCU plane)
    {
        return flightWaypointsByPlane.get(plane.getIndex());
    }

    public List<McuWaypoint> getWaypointsForPlaneId(Integer planeId)
    {
        return flightWaypointsByPlane.get(planeId);
    }

    public Map<Integer, List<McuWaypoint>> getAllWaypointsSets()
    {
        return flightWaypointsByPlane;
    }
    
    public McuWaypoint getWaypointByActionForLeadPlane(WaypointAction requestedAction)
    {
        for (McuWaypoint waypoint : getWaypointsForLeadPlane())
        {
            if (waypoint.getWpAction() == requestedAction)
            {
                return waypoint;
            }
        }
        
        return null;
    }

    public McuWaypoint getWaypointByActionForLeadPlaneWithFailure(WaypointAction requestedAction) throws PWCGException
    {
        McuWaypoint rendezvousWP = getWaypointByActionForLeadPlane(requestedAction);
        if (rendezvousWP == null)
        {
            throw new PWCGException("No waypoint found for action: " + requestedAction);
        }
        return rendezvousWP;
    }

    public void duplicateWaypointsForFlight(Flight flight) throws PWCGException
    {
        resetFollowingPlaneWaypoints(flight);        
        List<McuWaypoint> waypoints = flightWaypointsByPlane.get(flight.getPlanes().get(0).getIndex());        
        for (McuWaypoint waypoint : waypoints)
        {
            resetFollowingPlanePositionForWaypoint(flight, waypoint);
        }
    }

    public BaseFlightMcu getEntryMcu()
    {
        return waypointTimer;
    }

    public void onTriggerAddTarget(int index)
    {
        waypointTimer.setTarget(index);
    }

    private void createWaypointTimer() throws PWCGException
    {
        waypointTimer = new McuTimer();
        waypointTimer.setName(flight.getName() + ": WP Timer");     
        waypointTimer.setDesc("WP Timer for " + flight.getName());
        waypointTimer.setPosition(flight.getPosition());
        waypointTimer.setTimer(1);
    }

    private void setFirstWPTarget()
    {
        List<McuWaypoint> waypoints = getWaypointsForLeadPlane();
        if (waypoints != null)
        {
            if (waypoints.size() > 0)
            {
                waypointTimer.clearTargets();
                waypointTimer.setTarget(waypoints.get(0).getIndex());
            }
        }
    }

    private void resetFollowingPlaneWaypoints(Flight flight)
    {
        for (int planeIndex = 1; planeIndex < flight.getPlanes().size(); ++planeIndex)
        {
            PlaneMCU plane = flight.getPlanes().get(planeIndex);
            List<McuWaypoint> planeWayPoints = new ArrayList<McuWaypoint>();
            flightWaypointsByPlane.put(plane.getIndex(), planeWayPoints);
        }
    }
    
    private void resetFollowingPlanePositionForWaypoint(Flight flight, McuWaypoint waypoint) throws PWCGException
    {
        FormationGenerator formationGenerator = new FormationGenerator();
        List<Coordinate> planePositionsAtWP = formationGenerator.createPlaneInitialPosition(
                        flight.getPlanes(), 
                        waypoint.getPosition().copy(),
                        waypoint.getOrientation().copy()); 

        for (int planeIndex = 1; planeIndex < flight.getPlanes().size(); ++planeIndex)
        {
            PlaneMCU plane = flight.getPlanes().get(planeIndex);
            
            // Create a new WP for this plane
            List<McuWaypoint> planeWayPoints = flightWaypointsByPlane.get(plane.getIndex());

            // The WP is the same as the original with a slightly different position
            McuWaypoint planeWaypoint = waypoint.copy();
            planeWaypoint.setPosition(planePositionsAtWP.get(planeIndex));
            
            // Offset WP location based on formation generator
            planeWaypoint.setPosition(planePositionsAtWP.get(planeIndex).copy());
            
            // Add the WP to this plane's WP set
            planeWayPoints.add(planeWaypoint);
        }
    }

    public Flight getFlight()
    {
        return flight;
    }
}
