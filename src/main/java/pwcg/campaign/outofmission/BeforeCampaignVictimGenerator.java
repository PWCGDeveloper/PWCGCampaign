package pwcg.campaign.outofmission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class BeforeCampaignVictimGenerator implements IVictimGenerator
{
    private SquadronMember victimPilot;

    private Squadron victimSquadron;
    private Date date;

    public BeforeCampaignVictimGenerator (Squadron squadron, Date date)
    {
        this.victimSquadron = squadron;
        this.date = date;
    }

    public SquadronMember generateVictimAiCrew() throws PWCGException 
    {        
        List<SquadronMember> allPilots = generateAIPilots(victimSquadron, date);
        victimPilot = selectVictim(allPilots);
        return victimPilot;
    }

    private SquadronMember selectVictim(List<SquadronMember> squadronMembers) throws PWCGException 
    {
        int index = RandomNumberGenerator.getRandom(squadronMembers.size());
        return squadronMembers.get(index);
    }

    private List<SquadronMember> generateAIPilots(Squadron squadron, Date campaignDate) throws PWCGException 
    {
        Map<Integer, Integer> numAtRank = new HashMap<Integer, Integer>();
        numAtRank.put(0, 1);
        numAtRank.put(1, 1);
        numAtRank.put(2, 4);
        numAtRank.put(3, 3);
        numAtRank.put(4, 3);
                
        return generateAISquadMembers(squadron, campaignDate, numAtRank);
    }

    private List<SquadronMember> generateAISquadMembers(
                    Squadron squadron, 
                    Date campaignDate, 
                    Map<Integer, Integer> numAtRank)
                    throws PWCGException
    {
        // Add the AI pilots
        IRankHelper rankLists = RankFactory.createRankHelper();
        List<String> ranks = rankLists.getRanksByService(squadron.determineServiceForSquadron(campaignDate));
        
        List<SquadronMember> squadMembers = new ArrayList<SquadronMember>();
        
        for (int i = 0; i < numAtRank.size(); ++i)
        {
            int rankPos = i;
            // Some countries have fewer ranks
            if (rankPos >= ranks.size())
            {
                rankPos = ranks.size() - 1;
            }

            int atThisRank = numAtRank.get(i);
            for (int j = 0; j < atThisRank; ++j)
            {
                SquadronMember aiSquadronMember = new SquadronMember();

                String squaddieName = PilotNames.getInstance().getName(squadron.determineSquadronCountry(campaignDate), new HashMap<String, String>());
                aiSquadronMember.setName(squaddieName);
                aiSquadronMember.setRank(rankLists.getRankByService(rankPos, squadron.determineServiceForSquadron(campaignDate)));

                squadMembers.add(aiSquadronMember);
            }
        }
        
        return squadMembers;
    }
}
