package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class MissionBlocks
{
    private Mission mission;
    private List<FixedPosition> structuresForMission = new ArrayList<>();

    public MissionBlocks(Mission mission, List<FixedPosition> structuresForMission)
    {
        this.mission = mission;
        this.structuresForMission = structuresForMission;
    }

    public List<FixedPosition> getStructuresWithinMissionBorders()
    {
        List<FixedPosition> structuresWithinMissionBorders = new ArrayList<>();
        for (FixedPosition structure : structuresForMission)
        {
            if (mission.getStructureBorders().isInBox(structure.getPosition()))
            {
                structuresWithinMissionBorders.add(structure);
            }
        }
        return structuresWithinMissionBorders;
    }

    public List<FixedPosition> getAllStructuresForMission()
    {
        return structuresForMission;
    }

    public void adjustBlockStatus() throws PWCGException
    {
        adjustBlockDamage();
        adjustBlockDurability();
    }
    
    public void createBlockSmoke() throws PWCGException
    {
        MissionBlockSmoke missionBlockSmoke = new MissionBlockSmoke(mission);      
        missionBlockSmoke.addSmokeToDamagedAreas(structuresForMission);
    }

    private List<FixedPosition> adjustBlockDamage() throws PWCGException
    {
        MissionBlockDamageDecorator missionBlockDamage = new MissionBlockDamageDecorator();      
        return missionBlockDamage.setDamageToFixedPositions(structuresForMission, mission.getCampaign().getDate());
    }

    private void adjustBlockDurability()
    {
        BlockDefinitionManager blockDefinitionManager = BlockDefinitionManager.getInstance();
        for (FixedPosition fixedPosition : structuresForMission)
        {
            BlockDefinition blockDefinition = blockDefinitionManager.getBlockDefinition(fixedPosition);
            if (blockDefinition != null)
            {
                fixedPosition.setDurability(blockDefinition.getDurability());
            }
        }
    }

    public void removeExtraStructures(Coordinate truckCoordinates, int keepRadius)
    {
        List<FixedPosition> structuresToKeep = new ArrayList<>();
        for (FixedPosition structureForMission : structuresForMission)
        {
            double distance = MathUtils.calcDist(truckCoordinates, structureForMission.getPosition());
            if (distance < keepRadius)
            {
                structuresToKeep.add(structureForMission);
            }
        }
        structuresForMission = structuresToKeep;    
    }
}
