package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class AiCrewMemberRemovalChooser
{
    private Campaign campaign;
    
    public AiCrewMemberRemovalChooser(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public CrewMember findAiCrewMemberToRemove(String humanCrewMemberRank, int squadronId) throws PWCGException
    {

        CrewMember squadronMemberToRemove = removeSameRank(humanCrewMemberRank, squadronId);
        if (squadronMemberToRemove == null)
        {
            squadronMemberToRemove = removeSimilarRank(humanCrewMemberRank, squadronId);
            if (squadronMemberToRemove == null)
            {
                squadronMemberToRemove = removeAnyRank(humanCrewMemberRank, squadronId);
            }
        }
        
        return squadronMemberToRemove;
    }

    private CrewMember removeSameRank(String humanCrewMemberRank, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);

        CrewMembers squadronMembers = squadronPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(squadronMembers.getCrewMemberCollection(), campaign.getDate());

        List<CrewMember> squadronMembersOfSameRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            if (crewMember.getRank().equals(humanCrewMemberRank))
            {
                squadronMembersOfSameRank.add(crewMember);
            }
        }
        
        if (squadronMembersOfSameRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(squadronMembersOfSameRank.size());
            return (squadronMembersOfSameRank.get(index));
        }
        
        return null;
    }

    private CrewMember removeSimilarRank(String humanCrewMemberRank, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
        CrewMembers squadronMembers = squadronPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(squadronMembers.getCrewMemberCollection(), campaign.getDate());
        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        List<CrewMember> squadronMembersOfSimilarRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfHumanCrewMember = rankObj.getRankPosByService(humanCrewMemberRank, service);
            int rankPosOfAiCrewMember = rankObj.getRankPosByService(crewMember.getRank(), service);

            if (rankPosOfAiCrewMember > IRankHelper.COMMAND_RANK)
            {
                if (Math.abs(rankPosOfHumanCrewMember - rankPosOfAiCrewMember) < 2)
                {
                    squadronMembersOfSimilarRank.add(crewMember);
                }
            }
        }
        
        if (squadronMembersOfSimilarRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(squadronMembersOfSimilarRank.size());
            return (squadronMembersOfSimilarRank.get(index));
        }

        return null;
    }

    private CrewMember removeAnyRank(String humanCrewMemberRank, int squadronId) throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);
        CrewMembers squadronMembers = squadronPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(squadronMembers.getCrewMemberCollection(), campaign.getDate());
        
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        List<CrewMember> squadronMembersOfAnyNonCommandRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfAiCrewMember = rankObj.getRankPosByService(crewMember.getRank(), service);

            if (rankPosOfAiCrewMember > IRankHelper.COMMAND_RANK)
            {
                squadronMembersOfAnyNonCommandRank.add(crewMember);
            }
        }
        
        if (squadronMembersOfAnyNonCommandRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(squadronMembersOfAnyNonCommandRank.size());
            return (squadronMembersOfAnyNonCommandRank.get(index));
        }

        return null;
    }
}
