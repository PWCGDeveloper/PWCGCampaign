package pwcg.campaign.personnel;

import java.util.Date;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class SquadronMemberFilter
{
    public static Map<Integer, SquadronMember> filterActiveAI(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    public static Map<Integer, SquadronMember> filterActiveAIAndPlayer(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    public static Map<Integer, SquadronMember> filterActiveAIAndPlayerAndAces(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    public static Map<Integer, SquadronMember> filterInactiveAIAndPlayerAndAces(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildInactiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }
    
    public static Map<Integer, SquadronMember> filterActiveAIForSquadron(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIByForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    public static Map<Integer, SquadronMember> filterActiveAIAndPlayerForSquadron(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    public static Map<Integer, SquadronMember> filterActiveAIAndPlayerAndAcesForSquadron(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerAndAcesForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return filteredSquadronMembers;
    }

    
    
    
}
