package pwcg.mission.flight.balloondefense;

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

public class BalloonDefenseWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet;

    private static final int NUM_LEGS_IN_BALLOON_DEFENSE_CIRCLE = 6;

    public BalloonDefenseWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
        this.missionPointSet = new MissionPointRouteSet();
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        McuWaypoint initialWP = createInterceptFirstWP(ingressWaypoint);
        missionPointSet.addWaypoint(initialWP);

        List<McuWaypoint> interceptWaypoints = createCirclePattern(initialWP);
        missionPointSet.addWaypoints(interceptWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }
    

    private McuWaypoint createInterceptFirstWP(McuWaypoint ingressWaypoint) throws PWCGException
    {
        double angleTargetToIngress = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), ingressWaypoint.getPosition());

        double distanceFromTarget = getRadiusFromCircumferennceOfLoop();
        Coordinate balloonDefensePosition = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), angleTargetToIngress, distanceFromTarget);
        balloonDefensePosition.setYPos(flight.getFlightInformation().getAltitude());

        McuWaypoint loopFirstWP = WaypointFactory.createPatrolWaypointType();
        loopFirstWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        loopFirstWP.setSpeed(flight.getFlightCruisingSpeed());
        loopFirstWP.setPosition(balloonDefensePosition);    
        loopFirstWP.setTargetWaypoint(true);
        
        double initialAngle = MathUtils.calcAngle(flight.getFlightHomePosition(), flight.getTargetDefinition().getPosition());
        loopFirstWP.getOrientation().setyOri(initialAngle);
        
        return loopFirstWP;
    }

    private double getRadiusFromCircumferennceOfLoop()
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int loopDistance = productSpecific.getBalloonDefenseLoopDistance();
        double loopCircumference = NUM_LEGS_IN_BALLOON_DEFENSE_CIRCLE * loopDistance;
        double distanceFromTarget = loopCircumference / (2 * Math.PI);
        return distanceFromTarget;
    }

    private List<McuWaypoint> createCirclePattern (McuWaypoint ingressWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int loopDistance = productSpecific.getBalloonDefenseLoopDistance();
        
        List<McuWaypoint> interceptWPs = WaypointPatternFactory.generateCirclePattern(
                flight.getCampaign(),
                flight, 
                WaypointType.BALLOON_DEFENSE_WAYPOINT, 
                WaypointAction.WP_ACTION_PATROL, 
                McuWaypoint.FLIGHT_AREA,
                NUM_LEGS_IN_BALLOON_DEFENSE_CIRCLE,
                ingressWaypoint,
                flight.getFlightInformation().getAltitude(),
                loopDistance);
                        
        return interceptWPs;
    }
}
