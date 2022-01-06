package pwcg.mission.playerunit.objective;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.PlayerUnit;

public class MissionObjectiveFactory
{
    public static String formMissionObjective(PlayerUnit unit, Date date) throws PWCGException
    {
        return "Do something useful for God and Country!";
    }
}
