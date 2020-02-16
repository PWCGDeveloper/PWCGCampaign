package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.group.VirtualWayPoint;

public interface IVirtualWaypointPackage
{
    List<BaseFlightMcu> getAllFlightPointsForPlane(PlaneMcu plane)  throws PWCGException;

    void buildVirtualWaypoints() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGIOException, PWCGException;

    List<VirtualWayPoint> getVirtualWaypoints();

    IWaypointPackage getWaypointsForPlane(int planeIndex);
}