package pwcg.aar.inmission.phase2.logeval.crewmemberstatus;

import java.util.List;

import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.logfiles.event.IAType2;

public class AARCrewMemberStatusWoundedEvaluator
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
            return CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (damageToThisBot >= .25)
        {
            return CrewMemberStatus.STATUS_WOUNDED;
        }
        else
        {
            return CrewMemberStatus.STATUS_ACTIVE;
        }
    }
}
