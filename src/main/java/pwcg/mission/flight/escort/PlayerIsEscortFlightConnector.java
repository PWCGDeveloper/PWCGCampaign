package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerIsEscortFlightConnector
{
    PlayerIsEscortFlight playerEscort;
    EscortedByPlayerFlight escortedFlight;
    
    public PlayerIsEscortFlightConnector(PlayerIsEscortFlight playerEscort, EscortedByPlayerFlight escortedFlight)
    {
        this.playerEscort = playerEscort;
        this.escortedFlight = escortedFlight;
    }
    
    public void connectEscortAndEscortedFlight() throws PWCGException
    {
        connectEscortCoverToEscortedIngress();
        connectEscortedEgressToEscortForceComplete();
    }

    private void connectEscortCoverToEscortedIngress() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = playerEscort.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();

        McuWaypoint escortedIngressWP = escortedFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);

        coverTimer.setTimerTarget(escortedIngressWP.getIndex());
    }

    private void connectEscortedEgressToEscortForceComplete() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = playerEscort.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortWaypointSet escortMissionPointSet = (MissionPointEscortWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();

        McuWaypoint escortedEgressWP = escortedFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        
        escortedEgressWP.setTarget(forceCompleteTimer.getIndex());
    }
}
