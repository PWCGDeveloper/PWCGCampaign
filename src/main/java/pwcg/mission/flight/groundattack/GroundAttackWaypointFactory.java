package pwcg.mission.flight.groundattack;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequenceFactory;

public class GroundAttackWaypointFactory
{
    static public int GROUND_ATTACK_TIME = 300;
    static public int GROUND_ATTACK_BINGO_TIME = 30;
        
    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public GroundAttackWaypointFactory(IFlight flight) throws PWCGException
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
        for (McuWaypoint groundAttackWaypoint : groundAttackWaypointHelper.getWaypointsAfter())
        {
            missionPointSet.addWaypointAfter(groundAttackWaypoint);
        }
	}
    
    private AirGroundAttackMcuSequence createAttackArea() throws PWCGException 
    {
        AirGroundAttackMcuSequence attackMcuSequence = AirGroundAttackMcuSequenceFactory.buildAirGroundAttackSequence(flight, GROUND_ATTACK_TIME, GROUND_ATTACK_BINGO_TIME, AttackAreaType.GROUND_TARGETS);
        return attackMcuSequence;
    }
}
