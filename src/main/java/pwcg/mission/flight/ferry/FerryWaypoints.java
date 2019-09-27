package pwcg.mission.flight.ferry;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.ApproachWaypointGenerator;
import pwcg.mission.flight.waypoint.ClimbWaypointGenerator;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.mcu.McuWaypoint;

public class FerryWaypoints
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

    private Flight flight;
    private Campaign campaign;
    private List<McuWaypoint> waypoints = new ArrayList<McuWaypoint>();

    public FerryWaypoints(Flight flight, IAirfield fromAirfield, IAirfield toAirfield) throws PWCGException
    {
        this.flight = flight;
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
        this.campaign = flight.getCampaign();
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            ClimbWaypointGenerator climbWaypointGenerator = new ClimbWaypointGenerator(campaign, flight);
            List<McuWaypoint> climbWPs = climbWaypointGenerator.createClimbWaypoints(flight.getFlightInformation().getAltitude());
            waypoints.addAll(climbWPs);
        }

        List<McuWaypoint> targetWaypoints = createTargetWaypoints();
        waypoints.addAll(targetWaypoints);
        
        McuWaypoint approachWaypoint = ApproachWaypointGenerator.createApproachWaypoint(flight);
        waypoints.add(approachWaypoint);

        WaypointGeneratorUtils.setWaypointsNonFighterPriority(waypoints);

        return waypoints;
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
        int InitialWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
        Coordinate hopCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition().copy(), wpOrientation, InitialWaypointDistance);
        
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
        int InitialWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
        Coordinate hopCoords = MathUtils.calcNextCoord(toAirfield.getTakeoffLocation().getPosition().copy(), wpOrientation, InitialWaypointDistance);
        
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
