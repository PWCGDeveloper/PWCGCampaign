package pwcg.mission.flight.scramble;

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

public class PlayerScrambleFlight extends Flight
{
	public PlayerScrambleFlight() 
	{
		super ();
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squad, 
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.SCRAMBLE, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

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
				targetCoords, 
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
