package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;

public class DuplicatedWaypointSet
{
    private IFlight flight;
    private IMissionPointSet activateMissionPointSet;
    private Map<Integer, IWaypointPackage> waypointPackagesForVirtualFlight = new TreeMap<>();

    public DuplicatedWaypointSet(IFlight flight)
    {
        this.flight = flight;
    }
    
    public void create() throws PWCGException
    {
        getActivateMissionPointSetFromWaypointPackage();
        generateDuplicateWaypoints();
    }

    public IWaypointPackage getWayPointsForPlane(int planeIndex)
    {
        IWaypointPackage waypointPackage = waypointPackagesForVirtualFlight.get(planeIndex);
        return waypointPackage;
    }


    public void write(BufferedWriter writer) throws PWCGException 
    {
        activateMissionPointSet.write(writer);
        for (IWaypointPackage waypointPackageForPlane : waypointPackagesForVirtualFlight.values())
        {
            waypointPackageForPlane.write(writer);
        }
    }
    

    private void getActivateMissionPointSetFromWaypointPackage() throws PWCGException
    {
        activateMissionPointSet = flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ACTIVATE);
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

    public IMissionPointSet getActivateMissionPointSet()
    {
        return this.activateMissionPointSet;
    }
}
