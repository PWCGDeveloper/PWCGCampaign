package pwcg.mission.flight.bomb;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequenceFactory;

public class BombingWaypointFactory
{
    static public int BOMB_ATTACK_TIME = 120;
    static public int BOMB_ATTACK_BINGO_TIME = 10;

    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public BombingWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }
    
    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {        
        missionPointSet.addWaypointBefore(ingressWaypoint);

        createTargetWaypoints(ingressWaypoint.getPosition());
        
        AirGroundAttackMcuSequence attackMcuSequence = createAttackArea();
        missionPointSet.setAttackSequence(attackMcuSequence);
                
        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypointAfter(egressWaypoint);

        return missionPointSet;
    }

    private void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
    {
        GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(flight, ingressPosition, flight.getFlightInformation().getAltitude());
        groundAttackWaypointHelper.createTargetWaypoints();
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsBefore())
        {
            missionPointSet.addWaypointBefore(groundAttackWaypoint);
        }
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsAfter())
        {
            missionPointSet.addWaypointAfter(groundAttackWaypoint);
        }
    }
    
    private AirGroundAttackMcuSequence createAttackArea() throws PWCGException 
    {
        AirGroundAttackMcuSequence attackMcuSequence = AirGroundAttackMcuSequenceFactory.buildAirGroundAttackSequence(flight, BOMB_ATTACK_TIME, BOMB_ATTACK_BINGO_TIME, AttackAreaType.INDIRECT);
        return attackMcuSequence;
    }
}
