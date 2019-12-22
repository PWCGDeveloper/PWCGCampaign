package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.virtual.VirtualWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage
{
    private List<VirtualWayPoint> virtualWaypoints = new ArrayList<VirtualWayPoint>();
    private WaypointPackage waypointPackage;

    public VirtualWaypointPackage(WaypointPackage waypointPackage)
    {
        this.waypointPackage = waypointPackage;
    }

    public void buildVirtualWaypoints() throws PWCGException
    {
        generateVirtualWaypoints();
        linkVirtualWaypoints();
    }
    
    private void generateVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(waypointPackage.getFlight());
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
    }

    private void linkVirtualWaypoints()
    {
        BaseFlightMcu wpEntryMcu = getEntryMcu();
        if (wpEntryMcu != null)
        {
            waypointPackage.getFlight().getMissionBeginUnit().linkToMissionBegin(wpEntryMcu.getIndex());
        }
    }

    private BaseFlightMcu getEntryMcu()
    {
        VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
        return firstVirtualWayPoint.getEntryPoint();
    }

    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        for (VirtualWayPoint virtualWaypoint : virtualWaypoints)
        {
            virtualWaypoint.write(writer);
        }
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
