package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage extends WaypointPackage
{
    protected List<VirtualWayPoint> virtualWaypoints = new ArrayList<VirtualWayPoint>();
    
    /**
     * @param flight
     */
    public VirtualWaypointPackage(Flight flight)
    {
        super(flight);
    }


    /**
     * @param timerToLink
     */
    public BaseFlightMcu getEntryMcu()
    {
        if (virtualWaypoints.size() > 0)
        {
            VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
            return firstVirtualWayPoint.getEntryPoint();
        }
        
        return null;
    }

    /**
     * Write the mission to a file
     * 
     * @param writer
     * @throws PWCGIOException 
     * @
     */
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        for (VirtualWayPoint virtualWaypoint : virtualWaypoints)
        {
            virtualWaypoint.write(writer);
        }

        super.write(writer);
    }



    public List<VirtualWayPoint> getVirtualWaypoints()
    {
        return this.virtualWaypoints;
    }


    public void setVirtualWaypoints(List<VirtualWayPoint> virtualWaypoints)
    {
        this.virtualWaypoints = virtualWaypoints;
    }

}
