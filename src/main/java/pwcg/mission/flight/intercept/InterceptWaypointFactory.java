package pwcg.mission.flight.intercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.WaypointPatternFactory;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptWaypointFactory
{
    private FlightSearchPattern pattern = FlightSearchPattern.INTERCEPT_CROSS;
    public enum FlightSearchPattern
    {
        INTERCEPT_CIRCLE,
        INTERCEPT_CREEP,
        INTERCEPT_CROSS
    }
    
    private static final int NUM_LEGS_IN_INTERCEPT_CIRCLE = 6;
    private static final int NUM_SEGMENTS_IN_INTERCEPT_CREEP = 2;

    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public InterceptWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> interceptWaypoints = createInterceptWaypoints();
        setWaypointsAsTarget(interceptWaypoints);
        missionPointSet.addWaypoints(interceptWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createInterceptWaypoints() throws PWCGException  
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
        
        double initialAngle = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        innerLoopFirstWP.getOrientation().setyOri(initialAngle);
        
        return innerLoopFirstWP;
    }

    private Coordinate createInterceptFirstWPCoordinates() throws PWCGException
    {
        pattern = selectSearchPattern();

        // This puts the WP right in the middle of the pattern
        double angleToMovePattern = getPatternMoveAngleForPattern();
        Coordinate coordinatesAfterFixedMove = getPatternMoveDistanceForPattern(angleToMovePattern);

        // Move the WP a bit so it is not so precise
        double randomOffsetAngle = RandomNumberGenerator.getRandom(360);
        double randomOffsetDistance = RandomNumberGenerator.getRandom(5000);
        Coordinate coordinatesAfterRandomMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, randomOffsetAngle, randomOffsetDistance);

        coordinatesAfterRandomMove.setYPos(flight.getFlightInformation().getAltitude());

        return coordinatesAfterRandomMove;
    }

    private double getPatternMoveAngleForPattern() throws PWCGException
    {
        double angleToMovePattern = 0.0;
        
        if (pattern  == FlightSearchPattern.INTERCEPT_CROSS)
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), flight.getFlightHomePosition());
        }
        else if (pattern  == FlightSearchPattern.INTERCEPT_CREEP)
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), flight.getFlightHomePosition());
        }
        else
        {
            angleToMovePattern = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), flight.getFlightHomePosition());
        }
        
        return angleToMovePattern;
    }

    private Coordinate getPatternMoveDistanceForPattern (double angleToMovePattern) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        Coordinate coordinatesAfterFixedMove = null;
        double distanceToMovePattern = 1000.0;
        
        if (pattern  == FlightSearchPattern.INTERCEPT_CROSS)
        {
            distanceToMovePattern = productSpecific.getInterceptCrossDiameterDistance() / 2;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleToMovePattern, distanceToMovePattern);
        }
        else if (pattern  == FlightSearchPattern.INTERCEPT_CREEP)
        {
            distanceToMovePattern = productSpecific.getInterceptCreepCrossDistance() * 4;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleToMovePattern, distanceToMovePattern);
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptCreepLegDistance() * .5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
        }
        else
        {
            coordinatesAfterFixedMove = flight.getTargetDefinition().getPosition().copy();
            
            double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
            double distanceToShiftPattern = productSpecific.getInterceptInnerLoopDistance() / 1.5;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
            
            double shiftAngle2 = MathUtils.adjustAngle(angleToMovePattern, 180);
            double distanceToShiftPattern2 = productSpecific.getInterceptInnerLoopDistance() / 3;
            coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle2, distanceToShiftPattern2);
        }
        
        return coordinatesAfterFixedMove;
    }

    private FlightSearchPattern selectSearchPattern () 
    {
        int searchPatternRoll = RandomNumberGenerator.getRandom(100);
        if (searchPatternRoll < 45)
        {
            return  FlightSearchPattern.INTERCEPT_CROSS;
        }
        else if (searchPatternRoll < 75)
        {
            return  FlightSearchPattern.INTERCEPT_CREEP;
        }
        else
        {          
            return  FlightSearchPattern.INTERCEPT_CIRCLE;
        }
    }

    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint lastWP) throws PWCGException
    {
        if (pattern  == FlightSearchPattern.INTERCEPT_CROSS)
        {
            return  createCrossPattern (lastWP);
        }
        else if (pattern  == FlightSearchPattern.INTERCEPT_CREEP)
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
                flight.getCampaign(),
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                NUM_LEGS_IN_INTERCEPT_CIRCLE,
                innerLoopFirstWP,
                flight.getFlightInformation().getAltitude(),
                innerLoopDistance);
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCreepingPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int creepLegDistance = productSpecific.getInterceptCreepLegDistance();
        int creepCrossDistance = productSpecific.getInterceptCreepCrossDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCreepingPattern(
                flight.getCampaign(), 
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                NUM_SEGMENTS_IN_INTERCEPT_CREEP,
                lastWP,
                creepLegDistance,
                creepCrossDistance,
                lastWP.getOrientation().getyOri());
                        
        return interceptWPs;
    }

    private List<McuWaypoint> createCrossPattern (McuWaypoint lastWP) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int crossDistance = productSpecific.getInterceptCrossDiameterDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCrossPattern(
                flight.getCampaign(), 
                flight, 
                WaypointType.INTERCEPT_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                lastWP,
                crossDistance);
                        
        return interceptWPs;
    }
    
    private void setWaypointsAsTarget(List<McuWaypoint> interceptWaypoints)
    {
        for (McuWaypoint interceptWaypoint : interceptWaypoints)
        {
            interceptWaypoint.setTargetWaypoint(true);
        }
    }
}
