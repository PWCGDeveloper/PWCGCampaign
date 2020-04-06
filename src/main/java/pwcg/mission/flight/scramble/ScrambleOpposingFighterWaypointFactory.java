package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleOpposingFighterWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();
    
    public ScrambleOpposingFighterWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

        Double angle = MathUtils.calcAngle(startPosition, flight.getTargetDefinition().getTargetPosition());
        Orientation orientation = new Orientation();
        orientation.setyOri(angle);

        McuWaypoint scrambleTargetWP1 = createFirstScrambleWP(orientation);     
        targetWaypoints.add(scrambleTargetWP1);
        
        McuWaypoint scrambleTargetWP2 = createSecondScrambleWP(angle, orientation, scrambleTargetWP1);
        targetWaypoints.add(scrambleTargetWP2);

        return targetWaypoints;        
    }

    private McuWaypoint createFirstScrambleWP(Orientation orientation) throws PWCGException
    {
        Coordinate scrambleOpposeTargetPosition =  flight.getTargetDefinition().getTargetPosition();
        scrambleOpposeTargetPosition.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
        scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleTargetWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleTargetWP.setPosition(scrambleOpposeTargetPosition);    
        scrambleTargetWP.setOrientation(orientation);
        scrambleTargetWP.setTargetWaypoint(true);
        return scrambleTargetWP;
    }

    private McuWaypoint createSecondScrambleWP(Double angle, Orientation orientation, McuWaypoint scrambleTargetWP)
            throws PWCGException
    {
        // This will help the situation where all of the WPs get triggered and the flight deletes itself.
        Coordinate secondCoord =  MathUtils.calcNextCoord(flight.getTargetDefinition().getTargetPosition(), angle, 10000.0);
        secondCoord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint scrambleFurtherWP = WaypointFactory.createPatrolWaypointType();
        scrambleFurtherWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleFurtherWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleFurtherWP.setPosition(secondCoord);    
        scrambleFurtherWP.setTargetWaypoint(false);
        scrambleFurtherWP.setOrientation(orientation);
        return scrambleFurtherWP;
    }   
}
