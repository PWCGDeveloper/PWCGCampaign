package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class DuplicatedMissionPointSetBuilder
{
    private IFlight escortedFlight;
    private int altitudeOffset;
    private int horizontalOffset;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public DuplicatedMissionPointSetBuilder(IFlight escortedFlight, int altitudeOffset, int horizontalOffset)
    {
        this.escortedFlight = escortedFlight;
        this.altitudeOffset = altitudeOffset;
        this.horizontalOffset = horizontalOffset;
    }

    public IMissionPointSet createDuplicateWaypointsWithOffset() throws PWCGException
    {
        for (McuWaypoint escortedFlightWaypoint : escortedFlight.getWaypointPackage().getAllWaypoints())
        {
            McuWaypoint escortFlightWaypoint = escortedFlightWaypoint.copy();            
            escortFlightWaypoint.getPosition().adjustPosition(altitudeOffset, horizontalOffset);
            missionPointSet.addWaypoint(escortFlightWaypoint);
        }
        return missionPointSet;
    }
}
