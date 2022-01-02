package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public abstract class PlayerVictoryResolver
{
    protected List<LogVictory> confirmedPlayerVictories = new ArrayList<LogVictory>();

    public static boolean isPlayerVictory(CrewMember crewMember, LogAIEntity victoriousVehicle) throws PWCGException
    {
        if (victoriousVehicle instanceof LogPlane)
        {
            LogPlane victoriousPlane = (LogPlane)victoriousVehicle;
            if (crewMember.isPlayer())
            {
                if (crewMember.getSerialNumber() == victoriousPlane.getCrewMemberSerialNumber())
                {
                    return true;
                }
            }
        }
        return false;
    }

}
