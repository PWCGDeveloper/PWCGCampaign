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

	public void getAAAForMission (MissionGroundUnitBuilder missionGroundUnitBuilder) throws PWCGException
	{
        getAAAFrontLinesForMission(missionGroundUnitBuilder);
        getAAAKeyPositionForMission(missionGroundUnitBuilder);
	}
	   
	public void getAAAFrontLinesForMission (MissionGroundUnitBuilder missionGroundUnitBuilder) throws PWCGException
	{
		List<GroundUnitCollection> allAAA = new ArrayList<>();
		        
		AAAFrontLinesBuilder aaaFrontLinesBuilder = new AAAFrontLinesBuilder(campaign);
		List<GroundUnitCollection> frontAAA = aaaFrontLinesBuilder.generateAAAEmplacements();
		allAAA.addAll(frontAAA);

        selectAndAddAAAForMission(missionGroundUnitBuilder, allAAA);
	}

    public void getAAAKeyPositionForMission (MissionGroundUnitBuilder missionGroundUnitBuilder) throws PWCGException
    {
        List<GroundUnitCollection> allAAA = new ArrayList<>();
                
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
        
        selectAndAddAAAForMission(missionGroundUnitBuilder, allAAA);
    }

    private void selectAndAddAAAForMission(MissionGroundUnitBuilder missionGroundUnitBuilder, List<GroundUnitCollection> allAAA) throws PWCGException
    {
        List<GroundUnitCollection> selectedAAA = selectAAAForMission(allAAA);
        for (GroundUnitCollection aaa : selectedAAA)
        {
            missionGroundUnitBuilder.addMissionAAA(aaa);
        }
    }


	private List<GroundUnitCollection> selectAAAForMission(List<GroundUnitCollection> allAAA) throws PWCGException, PWCGException
	{
		List<GroundUnitCollection> selectedAAA = new ArrayList<>();
		for (GroundUnitCollection aaa : allAAA)
		{
			if (isAddAAA(aaa))
			{
				selectedAAA.add(aaa);
			}
		}
		
		return selectedAAA;
	}
	
	private boolean isAddAAA(GroundUnitCollection aaa) throws PWCGException
	{
        CoordinateBox missionBorders = getFrontBorders();
        if (!missionBorders.isInBox(aaa.getPosition()))
        {
            return false;
        }
        
        if (!isAAAFarEnoughAwayFromOtherUnits(aaa))
        {
            return false;
        }

        return true;
	}

	private boolean isAAAFarEnoughAwayFromOtherUnits(GroundUnitCollection aaa) throws PWCGException
    {
	    GroundUnitPositionDuplicateDetector groundUnitPositionVerifier = new GroundUnitPositionDuplicateDetector();
	    return groundUnitPositionVerifier.verifyGroundCollectionUnitPositionsNotDuplicated(mission.getGroundUnitBuilder().getAllMissionGroundUnits(), aaa);
    }

    private CoordinateBox getFrontBorders() throws PWCGException, PWCGException
	{
		ConfigManager configManager = campaign.getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);        
        CoordinateBox keepFrontBorders = CoordinateBox.copy(mission.getMissionBorders());
        keepFrontBorders.expandBox(keepGroupSpread);
		return keepFrontBorders;
	}
}
