package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.FixedPosition;
import pwcg.campaign.group.GroupManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionBlockBuilder
{
    private Mission mission;
    private List<FixedPosition> positionsForMission = new ArrayList<>();
    
    public MissionBlockBuilder (Mission mission)
    {
        this.mission = mission;
    }

    public void buildFixedPositionsForMission() throws PWCGException
    {
        getStandaloneBlocksPatrol();
        getTrainStationsPatrol();
        getBridgesForPatrol();
    }
    
    private void getStandaloneBlocksPatrol() throws PWCGException 
    {
        List<Block> selectedBlocks = new ArrayList<Block>();

        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(keepGroupSpread);

        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        for (Block block : groupData.getStandaloneBlocks())
        {
            if (missionBorders.isInBox(block.getPosition()))
            {
                selectedBlocks.add(block);
            }
        }

        positionsForMission.addAll(selectedBlocks);
    }

    private void getTrainStationsPatrol() throws PWCGException 
    {
        List<Block> selectedRRStations = new ArrayList<Block>();

        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(keepGroupSpread);

        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        for (Block rrStation : groupData.getRailroadList())
        {
            if (missionBorders.isInBox(rrStation.getPosition()))
            {
                selectedRRStations.add(rrStation);
            }
        }

        positionsForMission.addAll(selectedRRStations);
    }

    private void getBridgesForPatrol() throws PWCGException 
    {
        ArrayList<Bridge> selectedBridges = new ArrayList<Bridge>();

        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int keepGroupSpread = configManager.getIntConfigParam(ConfigItemKeys.KeepGroupSpreadKey);
        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(keepGroupSpread);

        GroupManager groupData =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager();
        for (Bridge bridge : groupData.getBridgeFinder().findAllBridges())
        {
            if (missionBorders.isInBox(bridge.getPosition()))
            {
                selectedBridges.add(bridge);
            }
        }

        positionsForMission.addAll(selectedBridges);
    }

    public List<FixedPosition> getPositionsForMission()
    {
        return positionsForMission;
    }
}
