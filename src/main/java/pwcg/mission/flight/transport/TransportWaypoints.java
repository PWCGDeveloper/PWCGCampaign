package pwcg.mission.flight.transport;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class TransportWaypoints extends WaypointGeneratorBase
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

	public TransportWaypoints(
	        IAirfield fromAirfield, 
	        IAirfield toAirfield, 
	        Flight flight,
	        Mission mission) throws PWCGException 
	{
 		super(fromAirfield.getPosition().copy(), toAirfield.getPosition().copy(), flight, mission);
 	     
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
	}

	@Override
    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        createStartWaypoint();
        createMidWaypoint();
        createDestinationWaypoint();
        
        createApproachWaypoint(toAirfield);
        return waypoints;
    }

    protected void createStartWaypoint() throws PWCGException  
    {
        double airfieldOrientation = fromAirfield.getOrientation().getyOri();
        int initialWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
        
        startCoords = MathUtils.calcNextCoord(fromAirfield.getPosition().copy(), airfieldOrientation, initialWaypointDistance);
        int InitialWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointAltitudeKey);
        startCoords.setYPos(InitialWaypointAltitude);

		McuWaypoint startWP = WaypointFactory.createMoveToWaypointType();
        startWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        startWP.setSpeed(waypointSpeed - 10);
        startWP.setPosition(startCoords);
        startWP.setName("Start");
        waypoints.add(startWP);
    }
    
    protected void createMidWaypoint() throws PWCGException  
    {
        McuWaypoint lastWp = waypoints.get(waypoints.size()-1);
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(lastWp.getPosition(), toAirfield.getLandingLocation().getPosition());
        double distanceBetweenAirfields = MathUtils.calcDist(fromAirfield.getTakeoffLocation().getPosition(), toAirfield.getLandingLocation().getPosition());
        distanceBetweenAirfields = distanceBetweenAirfields / 2;
        
        Coordinate midPointCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition(), angleFromTargetToHomeAirfield, distanceBetweenAirfields);
        midPointCoords.setYPos(determineFlightAltitude());

        McuWaypoint midPointWP = WaypointFactory.createMoveToWaypointType();
        midPointWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        midPointWP.setSpeed(waypointSpeed - 10);
        midPointWP.setPosition(midPointCoords);
        midPointWP.setName("Mid Waypoint");
        waypoints.add(midPointWP);
    }
			
	protected void createDestinationWaypoint() throws PWCGException  
	{
	    McuWaypoint lastWp = waypoints.get(waypoints.size()-1);
        double angleFromTargetToHomeAirfield = MathUtils.calcAngle(toAirfield.getLandingLocation().getPosition(), lastWp.getPosition());
        
        Coordinate destinationCoords = MathUtils.calcNextCoord(toAirfield.getLandingLocation().getPosition(), angleFromTargetToHomeAirfield, 10000.0);
        destinationCoords.setYPos(determineFlightAltitude());

        McuWaypoint destinationWP = WaypointFactory.createMoveToWaypointType();
        destinationWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        destinationWP.setSpeed(waypointSpeed - 10);
        destinationWP.setPosition(destinationCoords);
        destinationWP.setName("Destination");
        waypoints.add(destinationWP);
	}

    @Override
    protected int determineFlightAltitude() throws PWCGException 
    {
        int altitude = 2000;
        int randomAltitude = RandomNumberGenerator.getRandom(2000);
        int additionalAltitudeForMountains = 0;

        FrontMapIdentifier map = PWCGContextManager.getInstance().getCurrentMap().getMapIdentifier();
        if (map == FrontMapIdentifier.KUBAN_MAP)
        {
            additionalAltitudeForMountains = 1000;
        }
        return altitude + randomAltitude + additionalAltitudeForMountains;
    }

    @Override
    protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException
    {
        // Not used
    }
}
