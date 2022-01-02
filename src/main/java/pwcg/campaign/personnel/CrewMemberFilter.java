package pwcg.campaign.personnel;

import java.util.Date;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;

public class CrewMemberFilter
{
    public static CrewMembers filterActivePlayers(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActivePlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAI(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayer(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndAces(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerAndAces(Map<Integer, CrewMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterInactiveAIAndPlayerAndAces(Map<Integer, CrewMember> squadroNMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildInactiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }
    
    public static CrewMembers filterActiveAIForSquadron(Map<Integer, CrewMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIByForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerForSquadron(Map<Integer, CrewMember> squadronMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerAndAcesForSquadron(Map<Integer, CrewMember> squadroNMembersToFilter, Date date, int squadronId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesForSquadronFilter(date, squadronId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadroNMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAINoWounded(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAINoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }    

    private static CrewMembers mapToCrewMembers(Map<Integer, CrewMember> map)
    {
        CrewMembers squadronMembers = new CrewMembers();
        for (CrewMember crewMember : map.values())
        {
            squadronMembers.addToCrewMemberCollection(crewMember);
        }
        return squadronMembers;
    }

    public static CrewMembers filterActiveAIAndPlayerAndAcesNoWounded(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndAcesNoWounded(Map<Integer, CrewMember> squadronMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }
}
