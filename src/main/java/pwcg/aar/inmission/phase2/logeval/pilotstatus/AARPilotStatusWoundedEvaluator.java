package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.campaign.squadmember.SquadronMemberStatus;

public class AARPilotStatusWoundedEvaluator
{
    public int getCrewMemberWoundedLevel(List<IAType2> damageForBot) 
    {        
        double damageToThisBot = 0;
        for (IAType2 damage : damageForBot)
        {
            damageToThisBot += damage.getDamageLevel();
        }

        if (damageToThisBot >= .75)
        {
            return SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (damageToThisBot >= .25)
        {
            return SquadronMemberStatus.STATUS_WOUNDED;
        }
        else
        {
            return SquadronMemberStatus.STATUS_ACTIVE;
        }
    }

}
