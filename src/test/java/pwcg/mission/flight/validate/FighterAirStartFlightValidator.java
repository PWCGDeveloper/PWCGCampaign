package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightActivateVirtual;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightBeginAirStart;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class FighterAirStartFlightValidator 
{
    public void validatePatrolFlight(IFlight flight) throws PWCGException
    {
        assert(flight.getWaypointPackage().getAllWaypoints().size() > 0);
        validateWaypointLinkage(flight);
        validateWaypointTypes(flight);
        validateAirStart(flight);
    }

    private void validateAirStart(IFlight flight)
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert(plane.getPosition().getYPos() > 1000);
        }
    }

    private void validateWaypointLinkage(IFlight flight) throws PWCGException 
    {
        McuWaypoint prevWaypoint = null;
        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (prevWaypoint != null)
            {
                boolean isNextWaypointLinked = IndexLinkValidator.isIndexInTargetList(waypoint.getIndex(), prevWaypoint.getTargets());
                assert(isNextWaypointLinked);
            }
            
            prevWaypoint = waypoint;
        }
        
        verifyActivateLinkedToFormation(flight);

        assert(flight.getWaypointPackage().getAllWaypoints().get(0).getWaypointType() == WaypointType.AIR_START_WAYPOINT);
        assert(flight.getWaypointPackage().getAllWaypoints().get(1).getWaypointType() == WaypointType.INGRESS_WAYPOINT);
    }

    private void validateWaypointTypes(IFlight flight) 
    {
        boolean patrolFound = false;

        WaypointPriorityValidator.validateWaypointTypes(flight);

        for (McuWaypoint waypoint : flight.getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_PATROL))
            {
                patrolFound = true;
            }
        }
        
        assert(patrolFound);
    }
    
    public void verifyActivateLinkedToFormation(IFlight flight) throws PWCGException
    {
        MissionPointFlightActivateVirtual activateMissionPointSet = (MissionPointFlightActivateVirtual)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ACTIVATE);
        McuTimer activationTimer = activateMissionPointSet.getMissionBeginTimer();

        MissionPointFlightBeginAirStart airStartMissionPointSet = (MissionPointFlightBeginAirStart)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_BEGIN_AIR);
        int airStartEntryIndex = airStartMissionPointSet.getEntryPoint();

        boolean isActivateLinked = IndexLinkValidator.isIndexInTargetList(airStartEntryIndex, activationTimer.getTargets());
        assert(isActivateLinked);
    }
}
