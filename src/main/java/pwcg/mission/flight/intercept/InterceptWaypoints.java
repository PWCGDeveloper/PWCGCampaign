package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.approach.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ingress.IIngressWaypoint;
import pwcg.mission.flight.waypoint.ingress.IngressWaypointNearTarget;
import pwcg.mission.flight.waypoint.initial.InitialWaypointGenerator;
import pwcg.mission.flight.waypoint.intercept.WaypointPatternFactory;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptWaypoints
{
    private InterceptSearchPattern pattern = InterceptSearchPattern.INTERCEPT_CROSS;
                    
    private enum InterceptSearchPattern
    {
        INTERCEPT_CIRCLE,
        INTERCEPT_CREEP,
        INTERCEPT_CROSS
    }
    
    private static final int NUM_LEGS_IN_INTERCEPT_CIRCLE = 6;
    private static final int NUM_SEGMENTS_IN_INTERCEPT_CREEP = 2;

    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public InterceptWaypoints(Flight flight) throws PWCGException
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        pattern = selectSearchPattern();

        InitialWaypointGenerator initialWaypointGenerator = new InitialWaypointGenerator(flight);
        List<McuWaypoint> initialWPs = initialWaypointGenerator.createInitialFlightWaypoints();
        waypoints.addAll(initialWPs);

        McuWaypoint ingressWaypoint = createIngressWaypoint();
        waypoints.add(ingressWaypoint);

        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);
        
        McuWaypoint egressWaypoint = createEgressWaypoint(ingressWaypoint.getPosition());
        waypoints.add(egressWaypoint);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        return waypoints;
    }

    private McuWaypoint createIngressWaypoint() throws PWCGException  
    {
        IIngressWaypoint ingressWaypointGenerator = new IngressWaypointNearTarget(flight);
        McuWaypoint ingressWaypoint = ingressWaypointGenerator.createIngressWaypoint();
        return ingressWaypoint;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        
        McuWaypoint startWP = createInterceptFirstWP();
        targetWaypoints.add(startWP);       
        
        List<McuWaypoint> interceptWPs = this.createSearchPatternWaypoints(startWP);
        targetWaypoints.addAll(interceptWPs);
        
        return interceptWPs;        
    }

    private McuWaypoint createInterceptFirstWP() throws PWCGException
    {
        Coordinate coord = createInterceptFirstWPCoordinates();

        McuWaypoint innerLoopFirstWP = WaypointFactory.createPatrolWaypointType();
        innerLoopFirstWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        innerLoopFirstWP.setSpeed(flight.getFlightCruisingSpeed());
        innerLoopFirstWP.setPosition(coord);    
        innerLoopFirstWP.setTargetWaypoint(true);
        
        double initialAngle = MathUtils.calcAngle(flight.getPosition(), flight.getTargetPosition());
        innerLoopFirstWP.getOrientation().setyOri(initialAngle);
        
        return innerLoopFirstWP;
    }

    private Coordinate createInterceptFirstWPCoordinates() throws PWCGException
    {
        // This puts the WP right in the middle of the pattern
        double angleToMovePattern = getPatternMoveAngleForPattern();
        Coordinate coordinatesAfterFixedMove = getPatternMoveDistanceForPattern(angleToMovePattern);

        // Move the WP a bit so it is not so precise
        double randomOffsetAngle = RandomNumberGenerator.getRandom(360);
        double randomOffsetDistance = RandomNumberGenerator.getRandom(5000);
        Coordinate coordinatesAfterRandomMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, randomOffsetAngle, randomOffsetDistance);

        coordinatesAfterRandomMove.setYPos(flight.getFlightAltitude());

        return coordinatesAfterRandomMove;
    }
    

    
    private double getPatternMoveAngleForPattern() throws PWCGException
    {
        double angleToMovePattern = 0.0;
        
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetPosition(), flight.getPosition());
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetPosition(), flight.getPosition());
        }
        else
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetPosition(), flight.getPosition());
        }
        
        return angleToMovePattern;
    }

    private Coordinate getPatternMoveDistanceForPattern (double angleToMovePattern) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        Coordinate coordinatesAfterFixedMove = null;
        double distanceToMovePattern = 1000.0;
        
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            distanceToMovePattern = productSpecific.getInterceptCrossDiameterDistance() / 2;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(flight.getTargetPosition(), angleToMovePattern, distanceToMovePattern);
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            distanceToMovePattern = productSpecific.getInterceptCreepCrossDistance() * 4;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(flight.getTargetPosition(), angleToMovePattern, distanceToMovePattern);
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptCreepLegDistance() * .5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
        }
        else
        {
            coordinatesAfterFixedMove = flight.getTargetPosition().copy();
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptInnerLoopDistance() / 1.5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
            
            double shiftAngle2 = MathUtils.adjustAngle(angleToMovePattern, 180);
            double distanceToShiftPattern2 = productSpecific.getInterceptInnerLoopDistance() / 3;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle2, distanceToShiftPattern2);
        }
        
        return coordinatesAfterFixedMove;
    }

    private InterceptSearchPattern selectSearchPattern () 
    {
        int searchPatternRoll = RandomNumberGenerator.getRandom(100);
        if (searchPatternRoll < 45)
        {
            return  InterceptSearchPattern.INTERCEPT_CROSS;
        }
        else if (searchPatternRoll < 75)
        {
            return  InterceptSearchPattern.INTERCEPT_CREEP;
        }
        else
        {          
            return  InterceptSearchPattern.INTERCEPT_CIRCLE;
        }
    }

    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint lastWP) throws PWCGException
    {
        if (pattern  == InterceptSearchPattern.INTERCEPT_CROSS)
        {
            return  createCrossPattern (lastWP);
        }
        else if (pattern  == InterceptSearchPattern.INTERCEPT_CREEP)
        {
            return createCreepingPattern (lastWP);
        }
        else
        {
            return createCirclePattern(lastWP);
        }
    }

    private List<McuWaypoint> createCirclePattern (McuWaypoint innerLoopFirstWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int innerLoopDistance = productSpecific.getInterceptInnerLoopDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCirclePattern(
                campaign,
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                NUM_LEGS_IN_INTERCEPT_CIRCLE,
                innerLoopFirstWP,
                flight.getFlightAltitude(),
                innerLoopDistance);
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCreepingPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int creepLegDistance = productSpecific.getInterceptCreepLegDistance();
        int creepCrossDistance = productSpecific.getInterceptCreepCrossDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCreepingPattern(
                campaign, 
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                NUM_SEGMENTS_IN_INTERCEPT_CREEP,
                lastWP,
                creepLegDistance,
                creepCrossDistance);
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCrossPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int crossDistance = productSpecific.getInterceptCrossDiameterDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCrossPattern(
                campaign, 
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                lastWP,
                crossDistance);
                        
        return interceptWPs;
    }

    private McuWaypoint createEgressWaypoint(Coordinate lastWaypointCoord) throws PWCGException  
    {
        IAirfield airfield = flight.getSquadron().determineCurrentAirfieldCurrentMap(campaign.getDate());
        if (airfield == null)
        {
            throw new PWCGException("No airfield found for squadron " + flight.getSquadron().getSquadronId() + ".  Should not have been included in mission");
        }
        
        double angleFromFrontToField = MathUtils.calcAngle(flight.getPosition(), airfield.getPosition());
        double distanceFromFrontToField = MathUtils.calcDist(lastWaypointCoord, airfield.getPosition());
        Coordinate egressCoord = MathUtils.calcNextCoord(lastWaypointCoord, angleFromFrontToField, (distanceFromFrontToField / 2));
        egressCoord.setYPos(flight.getFlightAltitude());

        McuWaypoint egressWP = WaypointFactory.createEgressWaypointType();
        egressWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        egressWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.EGRESS_WAYPOINT.getName());
        egressWP.setSpeed(flight.getFlightCruisingSpeed());
        egressWP.setPosition(egressCoord);
        
        return egressWP;
    }
}
