package pwcg.mission.flight.divebomb;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class DiveBombingWaypoints extends WaypointGeneratorBase
{
	public DiveBombingWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);		
		this.flightAlt = determineFlightAltitude();
	}

    @Override
    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        super.createWaypoints();
        setWaypointsNonFighterPriority();
    
        return waypoints;
    }
	
	protected void createTargetWaypoints(Coordinate ingressPosition) throws PWCGException  
	{
		int attackAltitude = determineFlightAltitude();
		GroundAttackWaypointHelper groundAttackWaypointHelper = new GroundAttackWaypointHelper(ingressPosition, targetCoords, attackAltitude, waypointSpeed);
		List<McuWaypoint> groundAttackWaypoints = groundAttackWaypointHelper.createTargetWaypoints();
		waypoints.addAll(groundAttackWaypoints);
	}

	@Override
	protected int determineFlightAltitude() 
	{
		int altitude = 4100;
		return altitude;
	}

}
