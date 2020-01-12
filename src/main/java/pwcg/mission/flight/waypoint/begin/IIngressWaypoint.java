package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;

public interface IIngressWaypoint
{

    McuWaypoint createIngressWaypoint() throws PWCGException;

}