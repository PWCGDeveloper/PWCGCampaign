package pwcg.mission.flight.patrol;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class PatrolFlight extends Flight
{
	public PatrolFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit,
	            FlightTypes flightType,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
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
				targetCoords, 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Patrol aircpace at the specified front location.  " + 
				"Engage any enemy aircraft that you encounter.  ";
        String objectiveName =  formMissionObjectiveLocation(targetCoords.copy());
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
