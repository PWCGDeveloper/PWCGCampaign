package pwcg.mission.flight.seapatrolantishipping;

import java.util.List;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
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
		SeaAntiShippingOpposingWaypoints waypointGenerator = new SeaAntiShippingOpposingWaypoints(
					startPosition, 
					getTargetCoords(), 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
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
