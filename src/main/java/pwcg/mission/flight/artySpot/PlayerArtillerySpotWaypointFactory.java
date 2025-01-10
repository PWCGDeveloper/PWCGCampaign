package pwcg.mission.flight.artySpot;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointPlayerArtillerySpotSet;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerArtillerySpotWaypointFactory
{
    private IFlight flight;

    public PlayerArtillerySpotWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint artillerySpotWaypoint = createTargetWaypoints(ingressWaypoint.getPosition());
        MissionPointPlayerArtillerySpotSet missionPointSet = new MissionPointPlayerArtillerySpotSet(flight, ingressWaypoint, artillerySpotWaypoint);    	 
        missionPointSet.create();
        return missionPointSet;
    }

	private McuWaypoint createTargetWaypoints(Coordinate startPosition) throws PWCGException  
	{
		McuWaypoint artillerySpotWaypoint = WaypointFactory.createArtillerySpotWaypointType();

		artillerySpotWaypoint.setTriggerArea(McuWaypoint.TARGET_AREA);
		artillerySpotWaypoint.setSpeed(flight.getFlightCruisingSpeed());
        artillerySpotWaypoint.setTargetWaypoint(true);

		Coordinate initialArtillerySpotCoord = flight.getTargetDefinition().getPosition();
		initialArtillerySpotCoord.setYPos(flight.getFlightInformation().getAltitude());
		
		// Don't put the grid centered over the target
		double offsetDirection = RandomNumberGenerator.getRandom(360);
		double offsetDistance = 200 + RandomNumberGenerator.getRandom(700);
		Coordinate offsetGridCoordinate = MathUtils.calcNextCoord(flight.getCampaignMap(), initialArtillerySpotCoord, offsetDirection, offsetDistance);
        offsetGridCoordinate.setYPos(flight.getFlightInformation().getAltitude());		
        artillerySpotWaypoint.setPosition(offsetGridCoordinate);	

		return artillerySpotWaypoint;	
	}
}
