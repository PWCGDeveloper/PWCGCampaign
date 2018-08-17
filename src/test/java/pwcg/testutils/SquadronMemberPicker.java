package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;

public class SquadronMemberPicker
{
    public static SquadronMember pickNonAceSquadronMember (Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        SquadronMember selectedSquadronMember = null;
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (!squadronMember.isPlayer())
            {
                if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                    selectedSquadronMember = squadronMember;
                }
            }
            else if (SerialNumber.getSerialNumberClassification(squadronMember.getSerialNumber()) == SerialNumberClassification.ACE)
            {
                throw new PWCGException("Filter ai - ace picked instead of ai");
            }
            else
            {
                throw new PWCGException("Filter ai - player picked instead of ai");
            }
        }
        
        return selectedSquadronMember;
    }
    
    public static SquadronMember pickPlayerSquadronMember (Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActivePlayer(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        SquadronMember selectedSquadronMember = null;
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (squadronMember.isPlayer())
            {
                selectedSquadronMember = squadronMember;
            }
            else
            {
                throw new PWCGException("Filter player - non player picked");
            }
        }
        
        return selectedSquadronMember;
    }
}
