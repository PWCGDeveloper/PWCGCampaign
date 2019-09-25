package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleOpposingWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public ScrambleOpposingWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        McuWaypoint ingressWaypoint = createIngressWaypoint();
        waypoints.add(ingressWaypoint);
        
        List<McuWaypoint> targetWaypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        waypoints.addAll(targetWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Double angleToTarget = MathUtils.calcAngle(flight.getHomePosition(), flight.getTargetCoords());
        Orientation orientation = new Orientation();
        orientation.setyOri(angleToTarget);

        double angleFromTarget = MathUtils.adjustAngle(angleToTarget, 180);
        Coordinate scrambleOpposeIngressPosition =  MathUtils.calcNextCoord(flight.getTargetCoords(), angleFromTarget, 20000.0);
        scrambleOpposeIngressPosition.setYPos(flight.getFlightAltitude());

        McuWaypoint scrambleOpposeIngressWP = WaypointFactory.createPatrolWaypointType();
        scrambleOpposeIngressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleOpposeIngressWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleOpposeIngressWP.setPosition(scrambleOpposeIngressPosition);    
        scrambleOpposeIngressWP.setTargetWaypoint(false);
        scrambleOpposeIngressWP.setOrientation(orientation);
        return scrambleOpposeIngressWP;
    }

    protected List<McuWaypoint> createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();

        Double angle = MathUtils.calcAngle(startPosition, flight.getTargetCoords());
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
        Coordinate coord =  flight.getTargetCoords().copy();
        coord.setYPos(flight.getFlightAltitude());

        // Two waypoints - one to the target and another beyond.
        McuWaypoint scrambleTargetWP = WaypointFactory.createPatrolWaypointType();
        scrambleTargetWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleTargetWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleTargetWP.setPosition(coord);    
        scrambleTargetWP.setOrientation(orientation);
        scrambleTargetWP.setTargetWaypoint(true);
        return scrambleTargetWP;
    }

    private McuWaypoint createSecondScrambleWP(Double angle, Orientation orientation, McuWaypoint scrambleTargetWP)
            throws PWCGException
    {
        // This will help the situation where all of the WPs get triggered and the flight deletes itself.
        Coordinate secondCoord =  MathUtils.calcNextCoord(flight.getTargetCoords(), angle, 10000.0);
        secondCoord.setYPos(flight.getFlightAltitude());

        McuWaypoint scrambleFurtherWP = WaypointFactory.createPatrolWaypointType();
        scrambleFurtherWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
        scrambleFurtherWP.setSpeed(flight.getFlightCruisingSpeed());
        scrambleFurtherWP.setPosition(secondCoord);    
        scrambleFurtherWP.setTargetWaypoint(false);
        scrambleFurtherWP.setOrientation(orientation);
        return scrambleFurtherWP;
    }   
}
