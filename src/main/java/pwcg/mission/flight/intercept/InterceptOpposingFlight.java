package pwcg.mission.flight.intercept;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.bomb.BombingFlight;
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
		InterceptOpposingWaypoints waypointGenerator = new InterceptOpposingWaypoints(this);
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();        
        return waypointList;
	}
}
