package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class OutOfMissionPilotSelector
{

    public static boolean shouldPilotBeEvaluated (Campaign campaign, SquadronMember squadronMember) throws PWCGException
    {
        if (squadronMember instanceof Ace)
        {
            return false;
        }

        if (squadronMember.isPlayer())
        {
            return false;
        }

        if (squadronMember.getPilotActiveStatus() != SquadronMemberStatus.STATUS_ACTIVE)
        {
            return false;
        }
        
        if (!isSquadronViable(campaign, squadronMember))
        {
            return false;
        }
        
        return true; 
    }

    private static boolean isSquadronViable(Campaign campaign, SquadronMember squadronMember) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
        if (squadron == null)
        {
            return false;
        }
        
        if (!squadron.isSquadronViable(campaign))
        {
            return false;
        }
        
        if (!squadron.isCanFly(campaign.getDate()))
        {
            return false;
        }

        return true;
    }

}
