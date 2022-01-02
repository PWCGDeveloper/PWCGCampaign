package pwcg.testutils;

import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMemberPicker
{
    public static CrewMember pickNonAceCampaignMember (Campaign campaign, int squadronId) throws PWCGException
    {
        Map<Integer, CrewMember> squadronAllCampaignMembers = campaign.getPersonnelManager().getAllCampaignMembers();        
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(squadronAllCampaignMembers, campaign.getDate());        
        List<CrewMember> allHealthyCampaignMembers = squadronMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyCampaignMembers.size());
        return allHealthyCampaignMembers.get(index);
    }
    
    public static CrewMember pickNonAceCrewMember (Campaign campaign, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);        
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        List<CrewMember> allHealthyCrewMembers = squadronMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyCrewMembers.size());
        return allHealthyCrewMembers.get(index);
    }
    
    public static CrewMember pickPlayerCrewMember (Campaign campaign, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);        
        CrewMembers squadronMembers = CrewMemberFilter.filterActivePlayers(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());        
        List<CrewMember> allHealthyPlayers = squadronMembers.getCrewMemberList();
        int index = RandomNumberGenerator.getRandom(allHealthyPlayers.size());
        return allHealthyPlayers.get(index);
    }
}
