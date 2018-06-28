package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronMember.getSquadronId());
        if (squadronPersonnel != null)
        {
            return squadronPersonnel.isSquadronViable();
        }
        
        return false;
    }

}
