package pwcg.mission.flight.patrol;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFlight extends Flight
{
    public PatrolFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
	{
		super (flightInformation, missionBeginUnit);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		PatrolFrontWaypoints waypointGenerator = new PatrolFrontWaypoints(this);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

}
