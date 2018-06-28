package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;

public class SeaAntiShippingOpposingFlight extends Flight
{
	Coordinate startCoords = null;
	
	public SeaAntiShippingOpposingFlight() 
	{
		super ();
	}

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

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		SeaAntiShippingOpposingWaypoints waypointGenerator = new SeaAntiShippingOpposingWaypoints(
					startPosition, 
		       		targetCoords, 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
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
    public String getMissionObjective() throws PWCGMissionGenerationException, PWCGException
    {
        return "";
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
