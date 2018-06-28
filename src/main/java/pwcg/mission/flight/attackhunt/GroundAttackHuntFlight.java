package pwcg.mission.flight.attackhunt;

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

public class GroundAttackHuntFlight extends Flight
{
	public GroundAttackHuntFlight() 
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
		super.initialize (mission, campaign, FlightTypes.GROUND_ATTACK, targetCoords, squad, missionBeginUnit, isPlayerFlight);
	}

	@Override
	public int calcNumPlanes() throws PWCGException 
	{
		ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
		
        int GroundAttackMinimum = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackMinimumKey);
        int GroundAttackAdditional = configManager.getIntConfigParam(ConfigItemKeys.GroundAttackAdditionalKey) + 1;
        numPlanesInFlight = GroundAttackMinimum + RandomNumberGenerator.getRandom(GroundAttackAdditional);
		
        return modifyNumPlanes(numPlanesInFlight);
	}

    @Override
    public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
    {
        GroundAttackHuntWaypoints waypointGenerator = new GroundAttackHuntWaypoints(
                startPosition, 
                targetCoords, 
                this,
                mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
    }

	public String getMissionObjective() throws PWCGException 
	{
	    String objective = "";

        String objectiveName =  formMissionObjectiveLocation(targetCoords.copy());
        if (!objectiveName.isEmpty())
        {
            objective = "Perform a free hunt near " + objectiveName + 
                            ".  Attack any ground units that you see.";
        }
        else
        {
            objective = "Perform a free hunt at the specified front location.  " + 
                            ".  Attack any ground units that you see.";
        }
	    
	    return objective;
	}

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        this.createSimpleTargetAssociations();
    }
}
