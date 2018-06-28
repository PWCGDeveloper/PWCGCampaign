package pwcg.mission.flight.intercept;

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

public class InterceptFlight extends Flight
{
	public InterceptFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				FlightTypes flightType, 
				Coordinate targetCoords, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, flightType, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

		int InterceptMinimum = configManager.getIntConfigParam(ConfigItemKeys.InterceptMinimumKey);
		int InterceptAdditional = configManager.getIntConfigParam(ConfigItemKeys.InterceptAdditionalKey) + 1;
		numPlanesInFlight = InterceptMinimum + RandomNumberGenerator.getRandom(InterceptAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		InterceptWaypoints waypointGenerator = new InterceptWaypoints(
				startPosition, 
				targetCoords, 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() throws PWCGException 
	{
		String objective = "Intercept enemy aircraft" + formMissionObjectiveLocation(targetCoords.copy()) + ".";		
		
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }

}
