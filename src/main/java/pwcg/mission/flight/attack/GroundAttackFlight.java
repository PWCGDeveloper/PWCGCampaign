package pwcg.mission.flight.attack;

import java.io.BufferedWriter;
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
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.GroundTargetAttackFlight;
import pwcg.mission.mcu.McuWaypoint;

public class GroundAttackFlight extends GroundTargetAttackFlight
{
    static private int GROUND_ATTACK_ALT = 500;
    static private int GROUND_ATTACK_TIME = 360;
    	
	public GroundAttackFlight() 
	{
		super (GROUND_ATTACK_TIME);
	}

	public void initialize(
				Mission mission, 
				Campaign campaign, 
				Coordinate targetCoords, 
				Squadron squadron, 
	            MissionBeginUnit missionBeginUnit,
				boolean isPlayerFlight) throws PWCGException 
	{
		super.initialize (mission, campaign, FlightTypes.GROUND_ATTACK, targetCoords, squadron, missionBeginUnit, isPlayerFlight);
	}

	public void createUnitMission() throws PWCGException  
	{
		super.createUnitMission();
        super.createAttackArea(GROUND_ATTACK_ALT);
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
		GroundAttackWaypoints waypointGenerator = new GroundAttackWaypoints(
					startPosition, 
		       		targetCoords, 
		       		this,
		       		mission);

		List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
	    
        return waypointList;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);
    }
}
