package pwcg.mission.flight.groundhunt;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class GroundFreeHuntWaypointHelper
{
	private IFlight flight;
	
	
	public GroundFreeHuntWaypointHelper(IFlight flight)
	{
        this.flight = flight;       
	}

	public McuWaypoint createBeginHuntWaypoint() throws PWCGException  
	{
		McuWaypoint huntBeginWaypoint = WaypointFactory.createFreeHuntWaypointType();		
		huntBeginWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
		huntBeginWaypoint.setSpeed(flight.getFlightCruisingSpeed());
		huntBeginWaypoint.setPosition(flight.getTargetDefinition().getPosition().copy());	
		huntBeginWaypoint.setTargetWaypoint(true);
		huntBeginWaypoint.setWaypointAltitude(flight.getFlightInformation().getAltitude());
		
		return huntBeginWaypoint;
	}
}
