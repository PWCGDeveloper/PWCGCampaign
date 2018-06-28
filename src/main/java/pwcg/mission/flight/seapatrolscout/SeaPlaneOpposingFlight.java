package pwcg.mission.flight.seapatrolscout;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class SeaPlaneOpposingFlight extends SeaPatrolFlight
{
	Coordinate startCoords = null;
	
	public SeaPlaneOpposingFlight() 
	{
		super ();
	}
	
	/**
	 * @param mission
	 * @param campaign
	 * @param targetCoords
	 * @param squad
	 * @param field
	 * @param isPlayerFlight
	 * @throws PWCGException 
	 * @
	 */
	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Coordinate startCoords, 
				Squadron squad, 
				FlightTypes flightType,
                MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squad, missionBeginUnit, isPlayerFlight);
		
		this.startCoords = startCoords;
	}
	
	

    /**
     * Create a mission for this flight
     * @throws PWCGException 
     * 
     * @
     */
    public void createUnitMission() throws PWCGException  
    {
        super.createUnitMission();
    }


	/**
	 * Create mission
	 * @throws PWCGException 
	 * 
	 * @
	 */
	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaPlaneOpposingWaypoints waypointGenerator = new SeaPlaneOpposingWaypoints(
					startPosition, 
		       		targetCoords, 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}
}
