package pwcg.mission.flight.paradrop;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.attack.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
import pwcg.mission.mcu.AttackAreaType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.AirGroundAttackMcuSequence;

public class ParaDropWaypointFactory
{    
    static public int PARA_DROP_TIME = 240;

    private IFlight flight;
    private MissionPointAttackSet missionPointSet = new MissionPointAttackSet();

    public ParaDropWaypointFactory(IFlight flight) throws PWCGException
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
        IFlightInformation flightInformation = flight.getFlightInformation();
        AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence(flightInformation);
        attackMcuSequence.createAttackArea(PARA_DROP_TIME, AttackAreaType.INDIRECT);        
        return attackMcuSequence;
    }
}
