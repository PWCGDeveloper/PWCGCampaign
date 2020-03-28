package pwcg.campaign.personnel;

import java.util.Date;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class SquadronMemberFilter
{
    public static SquadronMembers filterActivePlayers(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActivePlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAI(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndPlayer(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndAces(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndPlayerAndAces(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterInactiveAIAndPlayerAndAces(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildInactiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }
    
    public static SquadronMembers filterActiveAIForSquadron(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIByForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndPlayerForSquadron(Map<Integer, SquadronMember> squadronMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndPlayerAndAcesForSquadron(Map<Integer, SquadronMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerAndAcesForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAINoWounded(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAINoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }    

    private static SquadronMembers mapToSquadronMembers(Map<Integer, SquadronMember> map)
    {
        SquadronMembers squadronMembers = new SquadronMembers();
        for (SquadronMember squadronMember : map.values())
        {
            squadronMembers.addToSquadronMemberCollection(squadronMember);
        }
        return squadronMembers;
    }

    public static SquadronMembers filterActiveAIAndPlayerAndAcesNoWounded(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndPlayerAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }

    public static SquadronMembers filterActiveAIAndAcesNoWounded(Map<Integer, SquadronMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        SquadronMemberFilterSpecification filterSpecification = SquadronMemberFilterFactory.buildActiveAIAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, SquadronMember> filteredSquadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
        return mapToSquadronMembers(filteredSquadronMembers);
    }
}
