package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingOpposingWaypoints extends WaypointGeneratorBase
{
	public SeaAntiShippingOpposingWaypoints(Coordinate startCoords, 
					  		Coordinate targetCoords, 
					  		Flight flight,
					  		Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
	

    public List<McuWaypoint> createWaypoints() throws PWCGException 
    {
        super.createWaypoints();
        if (flight.getPlanes().get(0).isRole(Role.ROLE_SEA_PLANE_LARGE))
        {
            super.setWaypointsNonFighterPriority();
        }

        return waypoints;
    }

	@Override
	protected void createTargetWaypoints(Coordinate targetCoords) throws PWCGException  
	{		
		createTargetApproachWaypoint();
		
		createTargetWaypoint();

		// Move back towards base for egress
		String airfieldName = flight.getSquadron().determineCurrentAirfieldName(campaign.getDate());
		IAirfield field =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
		Coordinate baseCoords = field.getPosition().copy();
		double angle = MathUtils.calcAngle(baseCoords, targetCoords);
		Coordinate egressCoords = MathUtils.calcNextCoord(baseCoords, angle, 10000.0);
		createEgressWaypoint(egressCoords);		
	}

	protected McuWaypoint createTargetApproachWaypoint() throws PWCGException  
	{
		double angle = 80.0;
		double distance = 4000.0;
		Coordinate coord = MathUtils.calcNextCoord(targetCoords, angle, distance);
		coord.setYPos(getFlightAlt());
		
		McuWaypoint targetApproachWP = WaypointFactory.createPatrolWaypointType();
		targetApproachWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
		targetApproachWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.PATROL_WAYPOINT.getName());
		targetApproachWP.setSpeed(waypointSpeed);
		targetApproachWP.setPosition(coord);
		targetApproachWP.setTargetWaypoint(false);
				
		waypoints.add(targetApproachWP);	
		
		return targetApproachWP;
	}

	private void createTargetWaypoint() throws PWCGException  
	{
 		Coordinate coord = targetCoords.copy();
		coord.setYPos(getFlightAlt());

		McuWaypoint targetWP = WaypointFactory.createPatrolWaypointType();
		targetWP.setTriggerArea(McuWaypoint.TARGET_AREA);
		targetWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()),WaypointType.PATROL_WAYPOINT.getName());
		targetWP.setSpeed(waypointSpeed);
		targetWP.setPosition(coord);	
		targetWP.setTargetWaypoint(true);

		waypoints.add(targetWP);
	}	

	@Override
	protected int determineFlightAltitude() 
	{
		int altitude = 300;
		int randomAlt = RandomNumberGenerator.getRandom(300);
		
		altitude = altitude + randomAlt;			
		
		return altitude;
	}

}
