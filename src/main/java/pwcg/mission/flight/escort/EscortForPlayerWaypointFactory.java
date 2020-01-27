package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

public class EscortForPlayerWaypointFactory
{
    private IFlight escortFlight;
    private IFlight escortedFlight;
    private MissionPointEscortWaypointSet missionPointSet;
    
    public EscortForPlayerWaypointFactory(IFlight escortedFlight, IFlight escortFlight)
    {
        this.escortedFlight = escortedFlight;
        this.escortFlight = escortFlight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet = new MissionPointEscortWaypointSet();
        
        missionPointSet.addWaypointBefore(ingressWaypoint);
        
        McuWaypoint rtbWP = ReturnToBaseWaypoint.createReturnToBaseWaypoint(escortFlight);
        missionPointSet.addWaypointAfter(rtbWP);
        
        EscortMcuSequence escortSequence = new EscortMcuSequence(escortedFlight, escortFlight);
        escortSequence.createEscortSequence();
        missionPointSet.setCoverSequence(escortSequence);

        return missionPointSet;
    }
}
