package pwcg.mission.flight.patrol;

import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
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
    public void createUnitMission() throws PWCGException 
    {
        calcPlanesInFlight();
        createWaypointPackage();
        createPlanes();
        createWaypoints();
        setPlayerInitialPosition();
        createActivation();
        createFormation();
        setFlightPayload();
        moveAirstartCloseToInitialWaypoint();
    }
    
	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();
		
		int PatrolMinimum = configManager.getIntConfigParam(ConfigItemKeys.PatrolMinimumKey);
		int PatrolAdditional = configManager.getIntConfigParam(ConfigItemKeys.PatrolAdditionalKey) + 1;
		numPlanesInFlight = PatrolMinimum + RandomNumberGenerator.getRandom(PatrolAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		PatrolFrontWaypoints waypointGenerator = new PatrolFrontWaypoints(
				startPosition, 
				getTargetCoords(), 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Patrol aircpace at the specified front location.  " + 
				"Engage any enemy aircraft that you encounter.  ";
        String objectiveName =  formMissionObjectiveLocation(getTargetCoords().copy());
        if (!objectiveName.isEmpty())
		{
			objective = "Patrol airspace " + objectiveName + 
					".  Engage any enemy aircraft that you encounter."; 
		}
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
