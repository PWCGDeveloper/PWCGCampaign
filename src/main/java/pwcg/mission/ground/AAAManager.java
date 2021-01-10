package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;

public class AAAManager 
{
	private Campaign campaign;
	private Mission mission;
	
	public AAAManager(Campaign campaign, Mission mission) throws PWCGException
	{
		this.campaign = campaign;
		this.mission = mission;
	}

	public List<GroundUnitCollection> getAAAForMission () throws PWCGException
	{
		List<GroundUnitCollection> allAAA = new ArrayList<>();
		        
		AAAFrontLinesBuilder aaaFrontLinesBuilder = new AAAFrontLinesBuilder(campaign);
		List<GroundUnitCollection> frontAAA = aaaFrontLinesBuilder.generateAAAEmplacements();
		allAAA.addAll(frontAAA);
				
        String currentGroundSetting = campaign.getCampaignConfigManager().getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (!currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
		{
        	AAABridgeBuilder aaaBridgeBuilder = new AAABridgeBuilder(campaign);
    		List<GroundUnitCollection> bridgeAAA = aaaBridgeBuilder.createAAAForBridges();
    		allAAA.addAll(bridgeAAA);

    		AAARailroadBuilder aaaRailroadBuilder = new AAARailroadBuilder(campaign);
    		List<GroundUnitCollection> railroadAAA = aaaRailroadBuilder.createAAAForRailroads();
    		allAAA.addAll(railroadAAA);
		}
		
        List<GroundUnitCollection> selectedAAA = selectAAAForMission(allAAA);
		return selectedAAA;
	}


	private List<GroundUnitCollection> selectAAAForMission(List<GroundUnitCollection> allAAA) throws PWCGException, PWCGException
	{
	    CoordinateBox missionBorders = getFrontBorders();

		List<GroundUnitCollection> selectedAAA = new ArrayList<>();
		for (GroundUnitCollection aaa : allAAA)
		{
			if (missionBorders.isInBox(aaa.getPosition()))
			{
				selectedAAA.add(aaa);
			}
		}
		
		return selectedAAA;
	}

	private CoordinateBox getFrontBorders() throws PWCGException, PWCGException
	{
		ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);        
        CoordinateBox missionBorders = mission.getMissionBorders().expandBox(keepGroupSpread);
		return missionBorders;
	}
}
