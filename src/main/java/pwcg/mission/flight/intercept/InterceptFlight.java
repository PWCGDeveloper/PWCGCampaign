package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class InterceptFlight extends Flight
{
    public InterceptFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		InterceptWaypoints waypointGenerator = new InterceptWaypoints(this);
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();        
        return waypointList;
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Intercept enemy aircraft" + formMissionObjectiveLocation(getTargetCoords().copy()) + ".";		
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

}
