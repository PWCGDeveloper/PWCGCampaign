package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingOpposingFlight extends Flight
{
	Coordinate startCoords = null;
	
    public SeaAntiShippingOpposingFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit,Coordinate startCoords)
    {
        super (flightInformation, missionBeginUnit);
        this.startCoords = startCoords;
    }

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaAntiShippingOpposingWaypoints waypointGenerator = new SeaAntiShippingOpposingWaypoints(this);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
