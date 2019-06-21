package pwcg.mission.flight.contactpatrol;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class ContactPatrolFlight extends Flight
{
    public ContactPatrolFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		ContactPatrolWaypoints waypoints = new ContactPatrolWaypoints(startPosition, 
		        getTargetCoords(),
		        this,
		        mission);
		return waypoints.createWaypoints();
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Perform reconnaissance at the specified front location.  " + 
				"Make contact with friendly troop concentrations to establish front lines.";
		
        objective = "Perform reconnaissance" + formMissionObjectiveLocation(getTargetCoords().copy()) + 
                        ".  Make contact with friendly troop concentrations to establish front lines.";
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

}
