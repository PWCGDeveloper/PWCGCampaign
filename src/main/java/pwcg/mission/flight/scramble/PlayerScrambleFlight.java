package pwcg.mission.flight.scramble;

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

public class PlayerScrambleFlight extends Flight
{
    public PlayerScrambleFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super (flightInformation, missionBeginUnit);
    }
    
	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = getCampaign().getCampaignConfigManager();

		int ScrambleMinimum = configManager.getIntConfigParam(ConfigItemKeys.ScrambleMinimumKey);
		int ScrambleAdditional = configManager.getIntConfigParam(ConfigItemKeys.ScrambleAdditionalKey) + 1;
		numPlanesInFlight = ScrambleMinimum + RandomNumberGenerator.getRandom(ScrambleAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		ScrambleWaypoints waypointGenerator = new ScrambleWaypoints(
				startPosition, 
				getTargetCoords(), 
				this,
				mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	public String getMissionObjective() 
	{
		String objective = "Incoming enemy aircraft are near our airbase.  Get airborne and destroy them!";
		return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
