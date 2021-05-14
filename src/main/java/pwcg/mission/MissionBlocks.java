package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;

public class MissionBlocks
{
    private Mission mission;
    private List<FixedPosition> positionsForMission = new ArrayList<>();

    public MissionBlocks(Mission mission, List<FixedPosition> positionsForMission)
    {
        this.mission = mission;
        this.positionsForMission = positionsForMission;
    }

    public List<FixedPosition> getPositionsForMission()
    {
        return positionsForMission;
    }

    public void adjustBlockStatus() throws PWCGException
    {
        adjustBlockDamage();
        adjustBlockSmoke();
        adjustBlockDurability();
    }

    private List<FixedPosition> adjustBlockDamage() throws PWCGException
    {
        MissionBlockDamageDecorator missionBlockDamage = new MissionBlockDamageDecorator();      
        return missionBlockDamage.setDamageToFixedPositions(positionsForMission, mission.getCampaign().getDate());
    }
    
    private void adjustBlockSmoke() throws PWCGException
    {
        MissionBlockSmoke missionBlockSmoke = new MissionBlockSmoke(mission);      
        missionBlockSmoke.addSmokeToDamagedAreas(positionsForMission);
    }

    private void adjustBlockDurability()
    {
        BlockDefinitionManager blockDefinitionManager = BlockDefinitionManager.getInstance();
        for (FixedPosition fixedPosition : positionsForMission)
        {
            BlockDefinition blockDefinition = blockDefinitionManager.getBlockDefinition(fixedPosition);
            if (blockDefinition != null)
            {
                fixedPosition.setDurability(blockDefinition.getDurability());
            }
        }
    }
}
