package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionBattleManager
{
    private List<MissionBattle> missionBattles = new ArrayList<>();

    public void addMissionBattle(MissionBattle missionBattle)
    {
        missionBattles.add(missionBattle);
    }
    
    public boolean isNearAnyBattle(Coordinate coordinate) throws PWCGException
    {
        for (MissionBattle missionBattle : missionBattles)
        {
            if (missionBattle.isNearBattle(coordinate))
            {
                return true;
            }
        }
        
        return false;
    }
}
