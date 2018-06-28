package pwcg.mission.flight.contactpatrol;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class ContactPatrolFlight extends Flight
{
	public ContactPatrolFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
                MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.CONTACT_PATROL, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
	public int calcNumPlanes() 
	{
		return 1;
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		ContactPatrolWaypoints waypoints = new ContactPatrolWaypoints(startPosition, 
													  targetCoords,
													  this,
												 	  mission);
		return waypoints.createWaypoints();
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Perform reconnaissance at the specified front location.  " + 
				"Make contact with friendly troop concentrations to establish front lines.";
		
        objective = "Perform reconnaissance" + formMissionObjectiveLocation(targetCoords.copy()) + 
                        ".  Make contact with friendly troop concentrations to establish front lines.";
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

}
