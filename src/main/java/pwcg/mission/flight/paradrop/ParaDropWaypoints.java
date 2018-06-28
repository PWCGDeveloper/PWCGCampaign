package pwcg.mission.flight.paradrop;

import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.GroundAttackWaypointHelper;
import pwcg.mission.flight.waypoint.WaypointGeneratorBase;
import pwcg.mission.mcu.McuWaypoint;

public class ParaDropWaypoints extends WaypointGeneratorBase
{    
	public ParaDropWaypoints(Coordinate startCoords, 
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
    protected int determineFlightAltitude() throws PWCGException 
    {
        int altitude = 800;
        int randomAltitude = RandomNumberGenerator.getRandom(800);
        int additionalAltitudeForMountains = 0;

        FrontMapIdentifier map = PWCGContextManager.getInstance().getCurrentMap().getMapIdentifier();
        if (map == FrontMapIdentifier.KUBAN_MAP)
        {
            additionalAltitudeForMountains = 1000;
        }
        
        return altitude + randomAltitude + additionalAltitudeForMountains;
    }
}
