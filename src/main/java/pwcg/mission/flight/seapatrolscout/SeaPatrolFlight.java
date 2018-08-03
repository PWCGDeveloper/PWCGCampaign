package pwcg.mission.flight.seapatrolscout;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class SeaPatrolFlight extends Flight
{
    public SeaPatrolFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public int calcNumPlanes() 
	{
		numPlanesInFlight = 1 + RandomNumberGenerator.getRandom(4);
		
		return numPlanesInFlight;

	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaPatrolWaypoints waypointGenerator = new SeaPatrolWaypoints(
				startPosition, 
				getTargetCoords(), 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() 
	{
		String objective = "Patrol sea lanes on the specified route.  " + 
				"Engage any enemy aircraft that you encounter";

		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
