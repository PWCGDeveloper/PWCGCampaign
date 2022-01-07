package pwcg.mission.flight.cap;

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

public class CAPWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public CAPWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> interceptWaypoints = createInterceptWaypoints(ingressWaypoint);
        setWaypointsAsTarget(interceptWaypoints);
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
        
        List<McuWaypoint> interceptWPs = this.createSearchPatternWaypoints(startWP);
        targetWaypoints.addAll(interceptWPs);
        
        return interceptWPs;        
    }

    private McuWaypoint createInterceptFirstWP(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint patternFirstWP = WaypointFactory.createPatrolWaypointType();
        patternFirstWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        patternFirstWP.setSpeed(flight.getFlightCruisingSpeed());
        patternFirstWP.setTargetWaypoint(true);
        
        Coordinate coord = createPatternStartPosition(ingressWaypoint);
        double initialAngle = MathUtils.calcAngle(ingressWaypoint.getPosition(), flight.getTargetDefinition().getPosition());
        patternFirstWP.setPosition(coord);    
        patternFirstWP.getOrientation().setyOri(initialAngle);
        
        return patternFirstWP;
    }
    
    private Coordinate createPatternStartPosition(McuWaypoint ingressWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int crossDistance = productSpecific.getInterceptCrossDiameterDistance();

        double movementAngle = MathUtils.calcAngle(flight.getTargetDefinition().getPosition(), ingressWaypoint.getPosition());
        Coordinate patternStartPosition = MathUtils.calcNextCoord(flight.getTargetDefinition().getPosition(), movementAngle, (crossDistance / 2));
        patternStartPosition.setYPos(flight.getFlightInformation().getAltitude());
        return patternStartPosition;
    }


    private List<McuWaypoint> createSearchPatternWaypoints (McuWaypoint lastWP) throws PWCGException
    {
        return  createCrossPattern (lastWP);
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
