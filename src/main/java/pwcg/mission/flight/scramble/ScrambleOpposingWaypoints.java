package pwcg.mission.flight.scramble;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.egress.EgressWaypointGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleOpposingWaypoints
{
    private Flight flight;
    private List<McuWaypoint> waypoints = new ArrayList<>();

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

        waypoints = WaypointGeneratorUtils.prependInitialToExistingWaypoints(flight, waypoints);
        
        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        Double angleToTarget = MathUtils.calcAngle(flight.getPosition(), flight.getTargetPosition());
        Orientation orientation = new Orientation();
        orientation.setyOri(angleToTarget);

        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int minDistanceFromTarget = productSpecificConfiguration.getScrambleOpposeMinDistance();
        int maxDistanceFromTarget = productSpecificConfiguration.getScrambleOpposeMaxDistance();
        int distanceFromTarget = minDistanceFromTarget + (RandomNumberGenerator.getRandom(maxDistanceFromTarget - minDistanceFromTarget));
        
        double angleFromTarget = MathUtils.adjustAngle(angleToTarget, 180);
        Coordinate scrambleOpposeIngressPosition =  MathUtils.calcNextCoord(flight.getTargetPosition(), angleFromTarget, distanceFromTarget);
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

        Double angle = MathUtils.calcAngle(startPosition, flight.getTargetPosition());
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
        Coordinate coord =  flight.getTargetPosition().copy();
        coord.setYPos(flight.getFlightAltitude());

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
        Coordinate secondCoord =  MathUtils.calcNextCoord(flight.getTargetPosition(), angle, 10000.0);
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
