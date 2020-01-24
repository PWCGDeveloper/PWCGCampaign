package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.virtual.VirtualWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private Map<Integer, IWaypointPackage> waypointPackagesForVirtualFlight = new HashMap<>();
    private Map<Integer, VirtualWayPoint> virtualWaypoints = new HashMap<>();

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override
    public void buildVirtualWaypoints() throws PWCGException
    {
        generateDuplicateWaypoints();
        generateVirtualWaypoints();
        linkVirtualWaypoints();
    }
    
    private void generateDuplicateWaypoints()
    {
        for (int positionInFormation = 1; positionInFormation < flight.getFlightData().getFlightPlanes().getPlanes().size())
        {
            waypointPackagesForVirtualFlight
        }
    }

    private void generateVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
    }

    private void linkVirtualWaypoints()
    {
        BaseFlightMcu wpEntryMcu = getEntryMcu();
        if (wpEntryMcu != null)
        {
            flight.getMissionBeginUnit().linkToMissionBegin(wpEntryMcu.getIndex());
        }
    }

    private BaseFlightMcu getEntryMcu()
    {
        VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
        return firstVirtualWayPoint.getEntryPoint();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        for (VirtualWayPoint virtualWaypoint : virtualWaypoints)
        {
            virtualWaypoint.write(writer);
        }
    }

    @Override
    public List<VirtualWayPoint> getVirtualWaypoints()
    {
        return this.virtualWaypoints;
    }

    @Override
    public List<VirtualWayPointCoordinate> getVirtualWaypointCoordinates()
    {
        List<VirtualWayPointCoordinate> virtualWayPointCoordinates = new ArrayList<>();
        for (VirtualWayPoint virtualWayPoint : virtualWaypoints)
        {
            virtualWayPointCoordinates.add(virtualWayPoint.getCoordinate());
        }
        return virtualWayPointCoordinates;
    }

    @Override
    public void setVirtualWaypoints(List<VirtualWayPoint> virtualWaypoints)
    {
        this.virtualWaypoints = virtualWaypoints;
    }
}
