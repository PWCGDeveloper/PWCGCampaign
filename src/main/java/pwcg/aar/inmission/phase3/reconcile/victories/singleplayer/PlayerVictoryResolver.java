package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public abstract class PlayerVictoryResolver
{
    protected List<LogVictory> confirmedPlayerVictories = new ArrayList<LogVictory>();

    public static boolean isPlayerVictory(SquadronMember squadronMember, LogPlane victoriousPlane) throws PWCGException
    {
    	if (victoriousPlane == null)
    	{
    		return false;
    	}
    	
        if (squadronMember.isPlayer())
        {
            if (squadronMember.getSerialNumber() == victoriousPlane.getPilotSerialNumber())
            {
                return true;
            }
        }
        return false;
    }

}
