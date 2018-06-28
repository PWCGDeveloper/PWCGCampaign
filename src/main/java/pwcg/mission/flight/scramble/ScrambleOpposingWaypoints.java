package pwcg.mission.flight.scramble;

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

public class ScrambleOpposingWaypoints extends WaypointGeneratorBase
{
	double ingressAngle;
	
	public ScrambleOpposingWaypoints(Coordinate startCoords, 
					  			 Coordinate targetCoords, 
					  			 Flight flight,
					  			 Mission mission) throws PWCGException 
	{
		super(startCoords, targetCoords, flight, mission);
	}
    
	@Override
    protected void createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {
        createAttackWaypoint();
        createAttackEgressWaypoint();
    }

	protected void createAttackWaypoint() throws PWCGException  
	{
		Coordinate coord = targetCoords.copy();
		int attackAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.GroundAttackAltitudeKey);
		coord.setYPos(attackAltitude);

		McuWaypoint attackWP = WaypointFactory.createTargetFinalWaypointType();
		attackWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		attackWP.setSpeed(waypointSpeed);
		attackWP.setPosition(coord);	
		attackWP.setTargetWaypoint(true);
		
		waypoints.add(attackWP);	
	}

	protected void createAttackEgressWaypoint() throws PWCGException  
	{
		double egressAngle = MathUtils.adjustAngle(ingressAngle, 180.0);
		Coordinate egressCoords = MathUtils.calcNextCoord(targetCoords, egressAngle, 200000.00);
		egressCoords.setYPos(getFlightAlt());

		McuWaypoint egressWP = WaypointFactory.createTargetEgressWaypointType();
		egressWP.setTriggerArea(McuWaypoint.COMBAT_AREA);
		egressWP.setSpeed(waypointSpeed);
		egressWP.setPosition(egressCoords);	
		egressWP.setTargetWaypoint(true);
		waypoints.add(egressWP);	
	}

    @Override
    protected int determineFlightAltitude() 
    {
        int altitude = 1000;
        int randomAlt = RandomNumberGenerator.getRandom(1000);
        
        altitude = altitude + randomAlt;
        
        return altitude;
    }

}
