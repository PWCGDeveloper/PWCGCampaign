package pwcg.mission.flight.waypoint.virtual;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.Mission;
import pwcg.mission.mcu.group.VirtualWayPoint;

public interface IVirtualWaypointPackage
{
    void buildVirtualWaypoints() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGIOException, PWCGException;

    List<VirtualWayPoint> getVirtualWaypoints();

    void addDelayForPlayerDelay(Mission mission) throws PWCGException;
}