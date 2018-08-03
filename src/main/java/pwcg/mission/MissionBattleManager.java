package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionBattleManager
{
    private List<AssaultInformation> missionBattles = new ArrayList<>();

    public void addMissionBattle(AssaultInformation missionBattle)
    {
        missionBattles.add(missionBattle);
    }
    
    public boolean isNearAnyBattle(Coordinate coordinate) throws PWCGException
    {
        for (AssaultInformation missionBattle : missionBattles)
        {
            if (missionBattle.isNearBattle(coordinate))
            {
                return true;
            }
        }
        
        return false;
    }
}
