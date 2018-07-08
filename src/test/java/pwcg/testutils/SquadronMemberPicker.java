package pwcg.testutils;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class SquadronMemberPicker
{
    public static SquadronMember pickNonAceSquadronMember (Campaign campaign) throws PWCGException
    {
        Map<Integer, SquadronMember> squadronMembers = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId()).getActiveSquadronMembers().getSquadronMemberCollection();
        SquadronMember selectedSquadronMember = null;
        for (SquadronMember squadronMember : squadronMembers.values())
        {
            if (!squadronMember.isPlayer())
            {
                if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                    selectedSquadronMember = squadronMember;
                    break;
                }
            }
        }
        
        return selectedSquadronMember;
    }
}
