package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public abstract class PlayerVictoryResolver
{
    protected List<LogVictory> confirmedPlayerVictories = new ArrayList<LogVictory>();

    public static boolean isPlayerVictory(SquadronMember squadronMember, Integer victorSerialNumber) throws PWCGException
    {
        if (squadronMember.isPlayer())
        {
            if (squadronMember.getSerialNumber() == victorSerialNumber)
            {
                return true;
            }
        }
        return false;
    }

}
