package pwcg.mission.flight.waypoint;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;

public interface IIngressWaypoint
{

    McuWaypoint createIngressWaypoint() throws PWCGException;

}