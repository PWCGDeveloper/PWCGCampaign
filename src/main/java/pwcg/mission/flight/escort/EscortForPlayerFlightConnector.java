package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointEscortForPlayerWaypointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortForPlayerFlightConnector
{
    EscortForPlayerFlight escortFlight;
    IFlight playerFlight;
    
    public EscortForPlayerFlightConnector(EscortForPlayerFlight escortForPlayerFlight, IFlight playerFlight2)
    {
        this.escortFlight = escortForPlayerFlight;
        this.playerFlight = playerFlight2;
    }
    
    public void connectEscortAndEscortedFlight() throws PWCGException
    {
        connectEscortCoverToEscortedRendezvous();
        connectEscortedEgressToEscortForceComplete();
    }

    private void connectEscortCoverToEscortedRendezvous() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = escortFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortForPlayerWaypointSet escortMissionPointSet = (MissionPointEscortForPlayerWaypointSet) escortMissionPointSetInterface;

        McuTimer coverTimer = escortMissionPointSet.getEscortSequence().getCoverTimer();
        McuWaypoint escortedIngressWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RENDEZVOUS);
        escortedIngressWP.setTarget(coverTimer.getIndex());
    }

    private void connectEscortedEgressToEscortForceComplete() throws PWCGException
    {
        IMissionPointSet escortMissionPointSetInterface = escortFlight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ESCORT);
        MissionPointEscortForPlayerWaypointSet escortMissionPointSet = (MissionPointEscortForPlayerWaypointSet) escortMissionPointSetInterface;
        McuTimer forceCompleteTimer = escortMissionPointSet.getEscortSequence().getForceCompleteTimer();

        McuWaypoint playerEgressWP = getPlayerFlightDeparturePoint();
        
        playerEgressWP.setTarget(forceCompleteTimer.getIndex());
    }

    private McuWaypoint getPlayerFlightDeparturePoint() throws PWCGException
    {
        McuWaypoint playerDepartureWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        if (playerDepartureWP == null) 
        {
            playerDepartureWP = playerFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_LANDING_APPROACH);
        }
        return playerDepartureWP;
    }
}
