package pwcg.testutils;

import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronMemberPicker
{
    public static SquadronMember pickNonAceCampaignMember (Campaign campaign) throws PWCGException
    {
        Map<Integer, SquadronMember> squadronAllCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAINoWounded(squadronAllCampaignMembers, campaign.getDate());        
        List<SquadronMember> allHealthyCampaignMembers = squadronMembers.getSquadronMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyCampaignMembers.size());
        return allHealthyCampaignMembers.get(index);
    }
    
    public static SquadronMember pickNonAceSquadronMember (Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAINoWounded(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        List<SquadronMember> allHealthySquadronMembers = squadronMembers.getSquadronMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthySquadronMembers.size());
        return allHealthySquadronMembers.get(index);
    }
    
    public static SquadronMember pickPlayerSquadronMember (Campaign campaign) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(campaign.getSquadronId());        
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActivePlayers(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());        
        List<SquadronMember> allHealthyPlayers = squadronMembers.getSquadronMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyPlayers.size());
        return allHealthyPlayers.get(index);
    }
}
