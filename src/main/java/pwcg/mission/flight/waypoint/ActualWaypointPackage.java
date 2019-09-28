package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class ActualWaypointPackage extends WaypointPackage
{
    protected McuTimer waypointTimer = null;

    public ActualWaypointPackage(Flight flight) throws PWCGException
    {
        super(flight);
        createWaypointTimer();
    }

    private void createWaypointTimer() throws PWCGException
    {
        // Only unspawned air starts do this.  
        // flights are triggered from the virtual waypoints.getWaypoints().
        waypointTimer = new McuTimer();
        waypointTimer.setName(flight.getName() + ": WP Timer");     
        waypointTimer.setDesc("WP Timer for " + flight.getName());
        waypointTimer.setPosition(flight.getHomePosition());
        waypointTimer.setTimer(1);
    }

    public void setWaypoints(List<McuWaypoint> waypoints)
    {
        super.setWaypoints(waypoints);
        setFirstWPTarget();
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

    public BaseFlightMcu getEntryMcu()
    {
        return waypointTimer;
    }

    public void onTriggerAddTarget(int index)
    {
        waypointTimer.setTarget(index);
    }

    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        waypointTimer.write(writer);
        super.write(writer);
    }

}
