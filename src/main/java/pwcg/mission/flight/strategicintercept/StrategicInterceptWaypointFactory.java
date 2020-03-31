package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.WaypointPatternFactory;
import pwcg.mission.mcu.McuWaypoint;

public class StrategicInterceptWaypointFactory
{
    private static final int NUM_SEGMENTS_IN_INTERCEPT_CREEP = 3;

    private IFlight flight;
    private IFlight targetFlight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public StrategicInterceptWaypointFactory(IFlight flight, IFlight targetFlight) throws PWCGException
    {
        this.flight = flight;
        this.targetFlight = targetFlight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> interceptWaypoints = createInterceptWaypoints(ingressWaypoint);
        missionPointSet.addWaypoints(interceptWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createInterceptWaypoints(McuWaypoint ingressWaypoint) throws PWCGException  
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        
        McuWaypoint startWP = createInterceptFirstWP(ingressWaypoint);
        targetWaypoints.add(startWP);       
        
        List<McuWaypoint> interceptWPs = this.createSearchPatternWaypoints(startWP, ingressWaypoint);
        targetWaypoints.addAll(interceptWPs);
        
        return interceptWPs;        
    }

    private McuWaypoint createInterceptFirstWP(McuWaypoint playerIngressWaypoint) throws PWCGException
    {
        Coordinate coord = createInterceptFirstWPCoordinates();

        McuWaypoint innerLoopFirstWP = WaypointFactory.createPatrolWaypointType();
        innerLoopFirstWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        innerLoopFirstWP.setSpeed(flight.getFlightPlanes().getFlightCruisingSpeed());
        innerLoopFirstWP.setPosition(coord);    
        innerLoopFirstWP.setTargetWaypoint(true);
        
        double initialAngle = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getFlightInformation().getTargetPosition());
        innerLoopFirstWP.getOrientation().setyOri(initialAngle);
        
        return innerLoopFirstWP;
    }

    private Coordinate createInterceptFirstWPCoordinates() throws PWCGException
    {
        double angleToMovePattern = getPatternMoveAngleForPattern();
        Coordinate coordinatesAfterFixedMove = getPatternMoveDistanceForPattern(angleToMovePattern);
        coordinatesAfterFixedMove.setYPos(flight.getFlightInformation().getAltitude());
        return coordinatesAfterFixedMove;
    }

    private double getPatternMoveAngleForPattern() throws PWCGException
    {
        double angleToMovePattern = 0.0;
        angleToMovePattern = MathUtils.calcAngle(flight.getFlightInformation().getTargetPosition(), flight.getFlightHomePosition());
        return angleToMovePattern;
    }

    private Coordinate getPatternMoveDistanceForPattern (double angleToMovePattern) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();

        Coordinate coordinatesAfterFixedMove = null;
        double distanceToMovePattern = 1000.0;
        
        distanceToMovePattern = productSpecific.getInterceptCreepCrossDistance() * 4;
        coordinatesAfterFixedMove = MathUtils.calcNextCoord(flight.getFlightInformation().getTargetPosition(), angleToMovePattern, distanceToMovePattern);
        
        double shiftAngle = MathUtils.adjustAngle(angleToMovePattern, 90);
        double distanceToShiftPattern = productSpecific.getInterceptCreepLegDistance() * .5;
        coordinatesAfterFixedMove = MathUtils.calcNextCoord(coordinatesAfterFixedMove, shiftAngle, distanceToShiftPattern);
        
        return coordinatesAfterFixedMove;
    }

    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint lastWP, McuWaypoint playerIngressWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int creepLegDistance = productSpecific.getInterceptCreepLegDistance();
        int creepCrossDistance = productSpecific.getInterceptCreepCrossDistance();
        
        double angle = calculateCreepingSearchAngle(playerIngressWaypoint);
                
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
                angle);
                        
        return interceptWPs;
    }
    
    private double calculateCreepingSearchAngle(McuWaypoint playerIngressWaypoint) throws PWCGException
    {
        Coordinate playerFlightIngressPosition = playerIngressWaypoint.getPosition();
        
        MissionPoint enemyFlightTarget = targetFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TARGET_FINAL);
        Coordinate enemyFlightTargetPosition = enemyFlightTarget.getPosition();
        
        double angle = MathUtils.calcAngle(playerFlightIngressPosition, enemyFlightTargetPosition);
        return angle;
    }
}
