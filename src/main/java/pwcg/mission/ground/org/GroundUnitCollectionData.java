package pwcg.mission.ground.org;

import java.util.List;

import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class GroundUnitCollectionData
{
    private GroundUnitCollectionType groundUnitCollectionType;
    private String name;
    private List<Coalition> triggerCoalitions;
    private TargetType targetType;

    public GroundUnitCollectionData(GroundUnitCollectionType groundUnitCollectionType, String name, TargetType targetType, List<Coalition> triggerCoalitions)
    {
        this.groundUnitCollectionType = groundUnitCollectionType;
        this.name = name;
        this.targetType = targetType;
        this.triggerCoalitions = triggerCoalitions;
    }
    
    public TargetType getTargetType()
    {
        return targetType;
    }

    public GroundUnitCollectionType getGroundUnitCollectionType()
    {
        return groundUnitCollectionType;
    }

    public String getName()
    {
        return name;
    }

    public List<Coalition> getTriggerCoalitions()
    {
        return triggerCoalitions;
    }
}
