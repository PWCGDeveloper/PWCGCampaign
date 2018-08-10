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

    public VirtualWaypointPackage(Flight flight)
    {
        super(flight);
    }

    public BaseFlightMcu getEntryMcu()
    {
        if (virtualWaypoints.size() > 0)
        {
            VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
            return firstVirtualWayPoint.getEntryPoint();
        }
        
        return null;
    }

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

    public List<VirtualWayPointCoordinate> getVirtualWaypointCoordinates()
    {
        List<VirtualWayPointCoordinate> virtualWayPointCoordinates = new ArrayList<>();
        for (VirtualWayPoint virtualWayPoint : virtualWaypoints)
        {
            virtualWayPointCoordinates.add(virtualWayPoint.getCoordinate());
        }
        return virtualWayPointCoordinates;
    }

    public void setVirtualWaypoints(List<VirtualWayPoint> virtualWaypoints)
    {
        this.virtualWaypoints = virtualWaypoints;
    }
}
