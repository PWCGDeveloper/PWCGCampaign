package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightPositionHelperAirStart;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptOpposingFlight extends BombingFlight
{
	Coordinate startCoords = null;
	
	public InterceptOpposingFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Coordinate startCoords, 
				Squadron squad, 
				FlightTypes flightType,
                MissionBeginUnit missionBeginUnit,
				boolean isOpposingFlight,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squad, missionBeginUnit, isPlayerFlight);
		
		this.startCoords = startCoords;
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		InterceptOpposingWaypoints waypointGenerator = new InterceptOpposingWaypoints(
					startPosition, 
		       		targetCoords, 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public void createUnitMission() throws PWCGException, PWCGException  
	{
		super.setName(squadron.determineDisplayName(campaign.getDate()));
		super.setPosition(departureAirfield.getPosition().copy());

		createPlanes();
		List<McuWaypoint> waypointList = createWaypoints(mission, departureAirfield.getPosition());
		waypointPackage.setWaypoints(waypointList);
		
		if (playerFlight && !(airstart))
		{
			// Players flight takes off
			createTakeoff();
		}

		createActivation();
		createFormation();

		setFlightPayload();
		
		createAttackArea(this.getMaximumFlightAltitude());
		
		FlightPositionHelperAirStart flightPositionHelperAirStart = new FlightPositionHelperAirStart(campaign, this);
		flightPositionHelperAirStart.createPlanePositionAirStart(startCoords.copy(), new Orientation());
		
		// This is AI only. Reset fuel for burn
		for (PlaneMCU plane : planes)
		{
            plane.setFuel(.6);
		}
	} 
}
