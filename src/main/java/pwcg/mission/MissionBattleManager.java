package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.target.AssaultDefinition;

public class MissionBattleManager
{
    private List<AssaultDefinition> missionAssaultDefinitions = new ArrayList<>();

    public void addMissionBattle(AssaultDefinition missionBattle)
    {
        missionAssaultDefinitions.add(missionBattle);
    }
    
    public boolean isNearAnyBattle(Coordinate coordinate) throws PWCGException
    {
        for (AssaultDefinition missionBattle : missionAssaultDefinitions)
        {
            if (missionBattle.isNearBattle(coordinate))
            {
                return true;
            }
        }
        
        return false;
    }

    public List<AssaultDefinition> getMissionAssaultDefinitions()
    {
        return missionAssaultDefinitions;
    }
}
