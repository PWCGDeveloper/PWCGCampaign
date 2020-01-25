package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.virtual.VirtualWaypointGenerator;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private Map<Integer, IWaypointPackage> waypointPackagesForVirtualFlight = new HashMap<>();
    private List<VirtualWayPoint> virtualWaypoints = new ArrayList<>();

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
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
    public List<BaseFlightMcu> getAllFlightPointsForPlane(PlaneMcu plane) throws PWCGException
    {
        IWaypointPackage waypointPackage = waypointPackagesForVirtualFlight.get(plane.getIndex());
        return waypointPackage.getAllFlightPoints();
    }


    private void generateDuplicateWaypoints() throws PWCGException
    {
        int positionInFormation = 0;
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            IWaypointPackage duplicatedWaypointPackage = flight.getWaypointPackage().duplicate(positionInFormation);
            waypointPackagesForVirtualFlight.put(plane.getIndex(), duplicatedWaypointPackage);
            ++positionInFormation;
        }
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
            IWaypointPackage waypointPackage = waypointPackagesForVirtualFlight.get(plane.getIndex());
            waypointPackage.finalize(plane);
        }
    }

    private void linkVirtualWaypointToMissionBegin() throws PWCGException
    {
        PlaneMcu flightLeader = flight.getFlightPlanes().getFlightLeader();
        IWaypointPackage waypointPackage = waypointPackagesForVirtualFlight.get(flightLeader.getIndex());
        
        VirtualWayPoint firstVirtualWayPoint = virtualWaypoints.get(0);
        if (firstVirtualWayPoint != null)
        {
            waypointPackage.triggerOnFlightActivation(firstVirtualWayPoint.getEntryPoint().getIndex());
        }
    }
}
