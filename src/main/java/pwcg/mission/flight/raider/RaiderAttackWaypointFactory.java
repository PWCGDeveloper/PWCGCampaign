package pwcg.mission.flight.raider;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.attack.RaidAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequenceFactory;

public class RaiderAttackWaypointFactory
{
    static public int RAID_ATTACK_TIME = 180;
    static public int RAID_ATTACK_BINGO_TIME = 30;
        
    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public RaiderAttackWaypointFactory(IFlight flight) throws PWCGException
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
        egressWaypoint.setWaypointAltitude(ingressWaypoint.getWaypointAltitude());
        missionPointSet.addWaypointAfter(egressWaypoint);

        return missionPointSet;
    }

    private void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
	{
        FlightInformation flightInformation = flight.getFlightInformation();
        RaidAttackWaypointHelper raidAttackWaypointHelper = new RaidAttackWaypointHelper(flight, ingressPosition, flightInformation.getAltitude());
		raidAttackWaypointHelper.createTargetWaypoints();
        for (McuWaypoint groundAttackWaypoint : raidAttackWaypointHelper.getWaypointsBefore())
        {
            missionPointSet.addWaypointBefore(groundAttackWaypoint);
        }
        for (McuWaypoint groundAttackWaypoint : raidAttackWaypointHelper.getWaypointsAfter())
        {
            missionPointSet.addWaypointAfter(groundAttackWaypoint);
        }
	}
    
    private AirGroundAttackMcuSequence createAttackArea() throws PWCGException 
    {
        AirGroundAttackMcuSequence attackMcuSequence = AirGroundAttackMcuSequenceFactory.buildAirGroundAttackSequence(flight, RAID_ATTACK_TIME, RAID_ATTACK_BINGO_TIME, AttackAreaType.GROUND_TARGETS);
        return attackMcuSequence;
    }
}
