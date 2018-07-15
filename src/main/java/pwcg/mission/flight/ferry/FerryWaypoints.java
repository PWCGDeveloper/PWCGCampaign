package pwcg.mission.flight.ferry;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class FerryWaypoints extends WaypointGeneratorBase
{
    private IAirfield fromAirfield;
    private IAirfield toAirfield;

	public FerryWaypoints(IAirfield fromAirfield, 
	                      IAirfield toAirfield, 
					      Flight flight,
					      Mission mission) throws PWCGException 
	{
 		super(fromAirfield.getPosition().copy(), toAirfield.getPosition().copy(), flight, mission);
 	     
        this.toAirfield = toAirfield;
        this.fromAirfield = fromAirfield;
	}

    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        createStartWaypoint();
        createApproachWaypoint(toAirfield);
        dumpWaypoints();
                
        return waypoints;
    }

    protected void createStartWaypoint() throws PWCGException  
    {
        double takeoffOrientation = fromAirfield.getTakeoffLocation().getOrientation().getyOri();
        int InitialWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointDistanceKey);
        startCoords = MathUtils.calcNextCoord(fromAirfield.getTakeoffLocation().getPosition().copy(), takeoffOrientation, InitialWaypointDistance);
        int InitialWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.InitialWaypointAltitudeKey);
        startCoords.setYPos(InitialWaypointAltitude);

		McuWaypoint startWP = WaypointFactory.createMoveToWaypointType();
        startWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        startWP.setSpeed(waypointSpeed - 10);
        startWP.setPosition(startCoords);
        waypoints.add(startWP);
    }
			
	protected void createTargetWaypoints(Coordinate notUsed)  
	{
	}
}
