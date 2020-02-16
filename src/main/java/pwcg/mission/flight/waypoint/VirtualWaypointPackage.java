package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.virtual.VirtualWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private List<VirtualWayPoint> virtualWaypoints = new ArrayList<>();
    private DuplicatedWaypointSet duplicatedWaypointSet;

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
        this.duplicatedWaypointSet = new DuplicatedWaypointSet(flight);
    }

    @Override
    public void buildVirtualWaypoints() throws PWCGException
    {
        generateDuplicateWaypoints();
        generateVirtualWaypoints();
        finalizeWaypointPackages();
        linkVirtualWaypointToMissionBegin();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        duplicatedWaypointSet.write(writer);
        
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
    public IWaypointPackage getWaypointsForPlane(int planeIndex)
    {
        return duplicatedWaypointSet.getWayPointsForPlane(planeIndex);
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPointsForPlane(PlaneMcu plane) throws PWCGException
    {
        IWaypointPackage waypointPackage = duplicatedWaypointSet.getWayPointsForPlane(plane.getIndex());
        return waypointPackage.getAllFlightPoints();
    }

    public DuplicatedWaypointSet getDuplicatedWaypointSet()
    {
        return duplicatedWaypointSet;
    }

    public void setDuplicatedWaypointSet(DuplicatedWaypointSet duplicatedWaypointSet)
    {
        this.duplicatedWaypointSet = duplicatedWaypointSet;
    }

    private void generateDuplicateWaypoints() throws PWCGException
    {
        duplicatedWaypointSet.create();
    }

    private void generateVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
    }

    private void finalizeWaypointPackages() throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            IWaypointPackage waypointPackage = duplicatedWaypointSet.getWayPointsForPlane(plane.getIndex());
            waypointPackage.finalize(plane);
        }
    }

    private void linkVirtualWaypointToMissionBegin() throws PWCGException
    {
        VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
        if (firstVirtualWayPoint != null)
        {
            duplicatedWaypointSet.getActivateMissionPointSet().setLinkToNextTarget(firstVirtualWayPoint.getEntryPoint().getIndex());
        }
    }
}
