package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.mcu.group.VirtualWayPoint;

public interface IVirtualWaypointPackage
{

    void buildVirtualWaypoints() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGIOException;

    List<VirtualWayPoint> getVirtualWaypoints();

    List<VirtualWayPointCoordinate> getVirtualWaypointCoordinates();

    void setVirtualWaypoints(List<VirtualWayPoint> virtualWaypoints);

}