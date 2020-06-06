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
    
    public void triggerGroundUnitsOnPlayerProximity() throws PWCGException
    {
        triggerPlayerTargetsOnAnyPlayer();
        triggerGroundUnitsOnAnyPlayer();
    }

    private void triggerPlayerTargetsOnAnyPlayer() throws PWCGException
    {
        for (IGroundUnitCollection playerTarget : mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            playerTarget.triggerOnPlayerProximity(mission);
        }
    }

    private void triggerGroundUnitsOnAnyPlayer() throws PWCGException
    {
        for (IGroundUnitCollection groundUnit: mission.getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            groundUnit.triggerOnPlayerProximity(mission);
        }
    }
}
