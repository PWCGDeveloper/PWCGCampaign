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
        
        double searchAngle = calculateCreepingSearchAngle();
        McuWaypoint startPatternWP = createInterceptStartPatternWP(searchAngle);
        targetWaypoints.add(startPatternWP);       
        
        List<McuWaypoint> interceptWPs = this.createSearchPatternWaypoints(startPatternWP, searchAngle);
        targetWaypoints.addAll(interceptWPs);
        
        return interceptWPs;        
    }

    private McuWaypoint createInterceptFirstWP(McuWaypoint playerIngressWaypoint) throws PWCGException
    {
        Coordinate coord = createInterceptFirstWPCoordinates();
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint interceptWP = WaypointFactory.createPatrolWaypointType();
        interceptWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        interceptWP.setSpeed(flight.getFlightCruisingSpeed());
        interceptWP.setPosition(coord);
        interceptWP.setTargetWaypoint(true);
        
        double initialAngle = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        interceptWP.getOrientation().setyOri(initialAngle);
        
        return interceptWP;
    }

    private McuWaypoint createInterceptStartPatternWP(double angleToPointBetweenIngressaAndEgress) throws PWCGException
    {
        Coordinate coord = createStartPatternCoordinate(angleToPointBetweenIngressaAndEgress);
        coord.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint interceptWP = WaypointFactory.createPatrolWaypointType();
        interceptWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        interceptWP.setSpeed(flight.getFlightCruisingSpeed());
        interceptWP.setPosition(coord);    
        interceptWP.setTargetWaypoint(true);
        
        double initialAngle = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        interceptWP.getOrientation().setyOri(initialAngle);
        
        return interceptWP;
    }

    private Coordinate createInterceptFirstWPCoordinates() throws PWCGException
    {
        return targetFlight.getTargetDefinition().getPosition().copy();
    }
    
    private Coordinate createStartPatternCoordinate(double angleToPointBetweenIngressaAndEgress) throws PWCGException
    {
        Coordinate targetCoord = targetFlight.getTargetDefinition().getPosition().copy();
        Coordinate startPatternCoordinate = MathUtils.calcNextCoord(targetFlight.getCampaignMap(), targetCoord, angleToPointBetweenIngressaAndEgress, 10000);
        return startPatternCoordinate;
    }
    
    private double calculateCreepingSearchAngle() throws PWCGException
    {
        Coordinate targetFlightIngress = targetFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS).getPosition().copy();
        Coordinate targetFlightEgress = targetFlight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS).getPosition().copy();
        double angleBetweenIngressaAndEgress = MathUtils.calcAngle(targetFlightIngress, targetFlightIngress);
        double distanceBetweenIngressaAndEgress = MathUtils.calcDist(targetFlightIngress, targetFlightEgress);
        Coordinate pointBetweenIngressaAndEgress = MathUtils.calcNextCoord(targetFlight.getCampaignMap(), targetFlightIngress, angleBetweenIngressaAndEgress, distanceBetweenIngressaAndEgress / 2);
        Coordinate targetCoord = targetFlight.getTargetDefinition().getPosition().copy();
        double angleToPointBetweenIngressaAndEgress = MathUtils.calcAngle(targetCoord, pointBetweenIngressaAndEgress);
        return angleToPointBetweenIngressaAndEgress;
    }

    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint startPatternCoordinate, double angle) throws PWCGException
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
                startPatternCoordinate,
                creepLegDistance,
                creepCrossDistance,
                angle);
        
        for (McuWaypoint interceptWP : interceptWPs)
        {
            interceptWP.setTargetWaypoint(true);
        }
                        
        return interceptWPs;
    }
}
