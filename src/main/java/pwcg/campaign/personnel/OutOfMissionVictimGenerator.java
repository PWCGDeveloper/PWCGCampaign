package pwcg.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OutOfMissionVictimGenerator 
{
    private static Map<String, String> aiCrewMembers = new HashMap<String, String>();
    
    private List<SquadronMember> selectedPilots = new ArrayList<SquadronMember>();
    
    public static void clean() 
    {
        aiCrewMembers.clear();
    }

    public void generateAiCrewForFlight(Squadron squadron, Date date, int numNeeded) throws PWCGException 
    {        
        List<SquadronMember> allPilots = generateAIPilots(squadron, date);        
        selectedPilots = selectAiCrewMembers(allPilots, numNeeded);
    }

    private List<SquadronMember> selectAiCrewMembers(List<SquadronMember> squadronMembers, int numNeeded) throws PWCGException 
    {
        List<SquadronMember> selectedCrew = new ArrayList<SquadronMember>();
        HashSet<Integer> selected = new HashSet<Integer>();
        
        while (selectedCrew.size() < numNeeded)
        {
            int index = RandomNumberGenerator.getRandom(squadronMembers.size());
            if (!selected.contains(index))
            {
                selectedCrew.add(squadronMembers.get(index));
                selected.add(index);
            }
        }
        
        return selectedCrew;
    }

    private List<SquadronMember> generateAIPilots(Squadron squadron, Date campaignDate) throws PWCGException 
    {
        // Now other AI squad mates - account for the player and any aces   
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

                String squaddieName = PilotNames.getInstance().getName(squadron.determineSquadronCountry(campaignDate), aiCrewMembers);
                aiSquadronMember.setName(squaddieName);
                aiSquadronMember.setRank(rankLists.getRankByService(rankPos, squadron.determineServiceForSquadron(campaignDate)));

                squadMembers.add(aiSquadronMember);
            }
        }
        
        return squadMembers;
    }


    public List<SquadronMember> getSelectedPilots()
    {
        return selectedPilots;
    }


    public void setSelectedPilots(List<SquadronMember> selectedPilots)
    {
        this.selectedPilots = selectedPilots;
    }
}
