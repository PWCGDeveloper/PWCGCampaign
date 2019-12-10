package pwcg.mission.ground.org;

import java.util.List;

import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;

public class GroundUnitCollectionData
{
    private GroundUnitCollectionType groundUnitCollectionType;
    private String name;
    private List<Coalition> triggerCoalitions;
    private TacticalTarget targetType;

    public GroundUnitCollectionData(GroundUnitCollectionType groundUnitCollectionType, String name, TacticalTarget targetType, List<Coalition> triggerCoalitions)
    {
        this.groundUnitCollectionType = groundUnitCollectionType;
        this.name = name;
        this.targetType = targetType;
        this.triggerCoalitions = triggerCoalitions;
    }
    
    public TacticalTarget getTargetType()
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
