package pwcg.mission.finalize;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;

public class MissionCheckZoneTriggerBuilder
{
    private Mission mission;
    
    public MissionCheckZoneTriggerBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public void triggerGroundUnits() throws PWCGException
    {
        for (GroundUnitCollection groundUnit: mission.getGroundUnitBuilder().getAllMissionGroundUnits())
        {
            groundUnit.triggerGroundUnitCollection(mission);
        }
    }
}
