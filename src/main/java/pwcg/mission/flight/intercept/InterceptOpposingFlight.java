package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightPositionHelperAirStart;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptOpposingFlight extends BombingFlight
{
	Coordinate startCoords = null;
	
    public InterceptOpposingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, Coordinate startCoords)
    {
        super (flightInformation, missionBeginUnit);
        this.startCoords = startCoords;
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		InterceptOpposingWaypoints waypointGenerator = new InterceptOpposingWaypoints(
					startPosition, 
		       		getTargetCoords(), 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}
	
	@Override
    public void createUnitMission() throws PWCGException 
    {
        super.createUnitMission();
        createUnitMissionInterceptOpposingSpecific();
    }
    

	public void createUnitMissionInterceptOpposingSpecific() throws PWCGException, PWCGException  
	{		
		createAttackArea(this.getMaximumFlightAltitude());
		
		FlightPositionHelperAirStart flightPositionHelperAirStart = new FlightPositionHelperAirStart(getCampaign(), this);
		flightPositionHelperAirStart.createPlanePositionAirStart(startCoords.copy(), new Orientation());
		
		// This is AI only. Reset fuel for burn
		for (PlaneMCU plane : planes)
		{
            plane.setFuel(.6);
		}
	}
}
