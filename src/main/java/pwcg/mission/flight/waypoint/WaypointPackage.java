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
import pwcg.mission.flight.plane.Plane;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public abstract class WaypointPackage
{
    protected Map<Integer, List<McuWaypoint>> flightWaypointsByPlane = new HashMap<Integer, List<McuWaypoint>>();
    protected Flight flight = null;

    /**
     * @param flight
     */
    public WaypointPackage(Flight flight)
    {
        this.flight = flight;
    }


    /**
     * @param timerToLink
     */
    public abstract BaseFlightMcu getEntryMcu();

    /**
     * Write the mission to a file
     * 
     * @param writer
     * @
     */
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        for (List<McuWaypoint> waypoints : flightWaypointsByPlane.values())
        {
            for (McuWaypoint waypoint : waypoints)
            {
                waypoint.write(writer);
            }
        }
    }

    /**
     * @return
     */
    public List<McuWaypoint> getWaypointsForLeadPlane()
    {
        return flightWaypointsByPlane.get(flight.getPlanes().get(0).getIndex());
    }

    /**
     * @param plane
     * @return
     */
    public List<McuWaypoint> getWaypointsForPlane(Plane plane)
    {
        return flightWaypointsByPlane.get(plane.getIndex());
    }

    /**
     * Set the waypoints and associate with the flight leader
     * 
     * @param waypoints
     */
    public void setWaypoints(List<McuWaypoint> waypoints)
    {
        flightWaypointsByPlane.clear();
        flightWaypointsByPlane.put(flight.getPlanes().get(0).getIndex(), waypoints);    
    }
    
    /**
     * Duplicates and offsets waypoints for each plane in the flight
     * 
     * @param flight
     * @throws PWCGException 
     */
    public void duplicateWaypointsForFlight(Flight flight) throws PWCGException
    {
        List<McuWaypoint> waypoints = flightWaypointsByPlane.get(flight.getPlanes().get(0).getIndex());
        
        for (McuWaypoint waypoint : waypoints)
        {
            // Generate the formation for each plane at this position
            FormationGenerator formationGenerator = new FormationGenerator();
            List<Coordinate> planePositionsAtWP = formationGenerator.createPlaneInitialPosition(
                            flight.getPlanes(), 
                            waypoint.getPosition().copy(),
                            waypoint.getOrientation().copy()); 

            // Cycle through the planes, creating  new WP for each plane
            for (int planeIndex = 0; planeIndex < flight.getPlanes().size(); ++planeIndex)
            {
                // The flight leader will use the original WP set
                if (planeIndex > 0)
                {
                    Plane plane = flight.getPlanes().get(planeIndex);
                    
                    // Add a new WP set for each plane
                    if (!flightWaypointsByPlane.containsKey(plane.getIndex()))
                    {
                        List<McuWaypoint> planeWayPoints = new ArrayList<McuWaypoint>();
                        flightWaypointsByPlane.put(plane.getIndex(), planeWayPoints);
                    }
                    
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
        }
    }
}
