package pwcg.mission.flight.ferry;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class FerryWaypointFactory
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public FerryWaypointFactory(IFlight flight, IAirfield fromAirfield, IAirfield toAirfield)
    {
        this.flight = flight;
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> ferryWaypoints = createTargetWaypoints();
        missionPointSet.addWaypoints(ferryWaypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }

    private List<McuWaypoint> createTargetWaypoints() throws PWCGException
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        
        McuWaypoint firstHopWP = createFirstHopWaypoint();
        targetWaypoints.add(firstHopWP);

        McuWaypoint secondHopWP = createSecondHopWaypoint();
        targetWaypoints.add(secondHopWP);
        
        return targetWaypoints;
    }

    private McuWaypoint createFirstHopWaypoint() throws PWCGException  
    {
        double wpOrientation = MathUtils.calcAngle(fromAirfield.getTakeoffLocation().getPosition(), toAirfield.getTakeoffLocation().getPosition());
        int LandingApproachWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingApproachWaypointDistanceKey);
        Coordinate hopCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition().copy(), wpOrientation, LandingApproachWaypointDistance);
        
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int initialWaypointAltitude = productSpecificConfiguration.getInitialWaypointAltitude();
        hopCoords.setYPos(initialWaypointAltitude);

        McuWaypoint hopWP = WaypointFactory.createMoveToWaypointType();
        hopWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        hopWP.setSpeed(flight.getFlightCruisingSpeed());
        hopWP.setPosition(hopCoords);
        return hopWP;
    }

    private McuWaypoint createSecondHopWaypoint() throws PWCGException  
    {
        double wpOrientation = MathUtils.calcAngle(toAirfield.getTakeoffLocation().getPosition(), fromAirfield.getTakeoffLocation().getPosition());
        int LandingApproachWaypointDistance = flight.getCampaign().getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingApproachWaypointDistanceKey);
        Coordinate hopCoords = MathUtils.calcNextCoord(toAirfield.getTakeoffLocation().getPosition().copy(), wpOrientation, LandingApproachWaypointDistance);
        
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int initialWaypointAltitude = productSpecificConfiguration.getInitialWaypointAltitude();
        hopCoords.setYPos(initialWaypointAltitude);

        McuWaypoint hopWP = WaypointFactory.createMoveToWaypointType();
        hopWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        hopWP.setSpeed(flight.getFlightCruisingSpeed());
        hopWP.setPosition(hopCoords);
        return hopWP;
    }
}
