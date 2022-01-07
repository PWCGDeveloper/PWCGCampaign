package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public interface IVirtualWaypoint
{
    void build() throws PWCGException;

    void write(BufferedWriter writer) throws PWCGException;

    void addAdditionalTime(int additionalTime);

    int getEntryPoint();

    void linkToNextVirtualWaypoint(IVirtualWaypoint nextVWP);

    Coordinate getVwpPosition();

    VirtualWayPointCoordinate getVwpCoordinate();

    VirtualWaypointTriggered getVwpTriggered();

    VirtualWaypointCheckZone getVwpCheckZone();

    void setVwpTriggerObject(int planeIndex);

    PlaneMcu getVwpFlightLeader();

    VirtualWaypointPlanes getVwpPlanes();

    boolean isShouldLinkToAttack();
}