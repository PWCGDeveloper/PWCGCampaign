package pwcg.mission.flight.waypoint.ingress;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;

public interface IIngressWaypoint
{

    McuWaypoint createIngressWaypoint() throws PWCGException;

}