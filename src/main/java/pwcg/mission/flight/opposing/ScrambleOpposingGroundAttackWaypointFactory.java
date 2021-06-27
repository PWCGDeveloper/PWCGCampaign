package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.groundattack.GroundAttackWaypointFactory;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequenceFactory;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;

public class ScrambleOpposingGroundAttackWaypointFactory
{
    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public ScrambleOpposingGroundAttackWaypointFactory(IFlight flight) throws PWCGException
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
        FlightInformation flightInformation = flight.getFlightInformation();
        GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(flight, ingressPosition, flightInformation.getAltitude());
        groundAttackWaypointHelper.createTargetWaypoints();
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsBefore())
        {
            missionPointSet.addWaypointBefore(groundAttackWaypoint);
        }
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsBefore())
        {
            missionPointSet.addWaypointAfter(groundAttackWaypoint);
        }
    }
    
    private AirGroundAttackMcuSequence createAttackArea() throws PWCGException 
    {
        AirGroundAttackMcuSequence attackMcuSequence = AirGroundAttackMcuSequenceFactory.buildAirGroundAttackSequence(flight, GroundAttackWaypointFactory.GROUND_ATTACK_TIME, GroundAttackWaypointFactory.GROUND_ATTACK_BINGO_TIME, AttackAreaType.GROUND_TARGETS);
        return attackMcuSequence;
    }
}
