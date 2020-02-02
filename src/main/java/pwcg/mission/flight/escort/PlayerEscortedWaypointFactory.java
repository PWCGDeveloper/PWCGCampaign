package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;

public class PlayerEscortedWaypointFactory
{
    static public int BOMB_ATTACK_TIME = 240;

    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public PlayerEscortedWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }
    
    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(
                flight, ingressWaypoint.getPosition(), flight.getFlightInformation().getAltitude());
        groundAttackWaypointHelper.createTargetWaypoints();

        missionPointSet.addWaypointBefore(ingressWaypoint);

        createTargetWaypointsBefore(groundAttackWaypointHelper);
        createAttackArea();
        createTargetWaypointsAfter(groundAttackWaypointHelper);
        createEgressWaypoints(ingressWaypoint);

        return missionPointSet;
    }

    private void createTargetWaypointsBefore(GroundAttackWaypointHelper groundAttackWaypointHelper) throws PWCGException  
    {
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsBefore())
        {
            missionPointSet.addWaypointBefore(groundAttackWaypoint);
        }
    }

    private void createAttackArea() throws PWCGException 
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
        AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence(flightInformation);
        attackMcuSequence.createAttackArea(BOMB_ATTACK_TIME, AttackAreaType.INDIRECT);        
        missionPointSet.setAttackSequence(attackMcuSequence);
    }

    private void createTargetWaypointsAfter(GroundAttackWaypointHelper groundAttackWaypointHelper) throws PWCGException  
    {
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsAfter())
        {
            missionPointSet.addWaypointAfter(groundAttackWaypoint);
        }
    }

    private void createEgressWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypointAfter(egressWaypoint);
    }
}
