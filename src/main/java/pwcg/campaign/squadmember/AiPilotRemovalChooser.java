package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class AiPilotRemovalChooser
{
    private Campaign campaign;
    
    public AiPilotRemovalChooser(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public SquadronMember findAiPilotToRemove(String humanPilotRank, int squadronId) throws PWCGException
    {

        SquadronMember squadronMemberToRemove = removeSameRank(humanPilotRank, squadronId);
        if (squadronMemberToRemove == null)
        {
            squadronMemberToRemove = removeSimilarRank(humanPilotRank, squadronId);
            if (squadronMemberToRemove == null)
            {
                squadronMemberToRemove = removeAnyRank(humanPilotRank, squadronId);
            }
        }
        
        return squadronMemberToRemove;
    }

    private SquadronMember removeSameRank(String humanPilotRank, int squadronId) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);

        SquadronMembers squadronMembers = squadronPersonnel.getSquadronMembers();
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAI(squadronMembers.getSquadronMemberCollection(), campaign.getDate());

        List<SquadronMember> squadronMembersOfSameRank = new ArrayList<>();
        for (SquadronMember squadronMember : activeSquadronMembers.getSquadronMemberList())
        {
            if (squadronMember.getRank().equals(humanPilotRank))
            {
                squadronMembersOfSameRank.add(squadronMember);
            }
        }
        
        if (squadronMembersOfSameRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(squadronMembersOfSameRank.size());
            return (squadronMembersOfSameRank.get(index));
        }
        
        return null;
    }

    private SquadronMember removeSimilarRank(String humanPilotRank, int squadronId) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
        SquadronMembers squadronMembers = squadronPersonnel.getSquadronMembers();
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAI(squadronMembers.getSquadronMemberCollection(), campaign.getDate());
        
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        List<SquadronMember> squadronMembersOfSimilarRank = new ArrayList<>();
        for (SquadronMember squadronMember : activeSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfHumanPilot = rankObj.getRankPosByService(humanPilotRank, service);
            int rankPosOfAiPilot = rankObj.getRankPosByService(squadronMember.getRank(), service);

            if (rankPosOfAiPilot > IRankHelper.COMMAND_RANK)
            {
                if (Math.abs(rankPosOfHumanPilot - rankPosOfAiPilot) < 2)
                {
                    squadronMembersOfSimilarRank.add(squadronMember);
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

    private SquadronMember removeAnyRank(String humanPilotRank, int squadronId) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);
        SquadronMembers squadronMembers = squadronPersonnel.getSquadronMembers();
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAI(squadronMembers.getSquadronMemberCollection(), campaign.getDate());
        
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());

        List<SquadronMember> squadronMembersOfAnyNonCommandRank = new ArrayList<>();
        for (SquadronMember squadronMember : activeSquadronMembers.getSquadronMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfAiPilot = rankObj.getRankPosByService(squadronMember.getRank(), service);

            if (rankPosOfAiPilot > IRankHelper.COMMAND_RANK)
            {
                squadronMembersOfAnyNonCommandRank.add(squadronMember);
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
