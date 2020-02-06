package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortByPlayerFlightConnector
{
    EscortForPlayerFlight escortFlight;
    IFlight playerFlight;
    
    public EscortByPlayerFlightConnector(EscortForPlayerFlight escortForPlayerFlight, IFlight playerFlight2)
    {
        this.escortFlight = escortForPlayerFlight;
        this.playerFlight = playerFlight2;
    }
    
    public void connectEscortAndEscortedFlight() throws PWCGException
    {
        connectEscortCoverToEscortedIngress();
        connectEscortedEgressToEscortForceComplete();
    }

    private void connectEscortCoverToEscortedIngress() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = escortFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();

        McuWaypoint escortedIngressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);

        escortedIngressWP.setTarget(coverTimer.getIndex());
    }

    private void connectEscortedEgressToEscortForceComplete() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = escortFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();

        McuWaypoint escortedEgressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        
        escortedEgressWP.setTarget(forceCompleteTimer.getIndex());
    }
}
