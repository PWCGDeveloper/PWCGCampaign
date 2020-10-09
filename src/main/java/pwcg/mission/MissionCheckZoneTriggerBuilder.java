package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionCheckZoneTriggerBuilder
{
    private Mission mission;
    
    public MissionCheckZoneTriggerBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public void triggerGroundUnits() throws PWCGException
    {
        for (IGroundUnitCollection groundUnit: mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            groundUnit.triggerGroundUnitCollection(mission);
        }
    }
}
