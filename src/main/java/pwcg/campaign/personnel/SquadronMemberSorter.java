package pwcg.campaign.personnel;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class SquadronMemberSorter
{

    public static Map<String, SquadronMember> sortPilotsByStatus(Campaign campaign, Map<Integer, SquadronMember> pilots) throws PWCGException 
    {
        Map<String, SquadronMember> sortedPilots = new TreeMap<>();

        int index = 1;
        for (SquadronMember pilot : pilots.values())
        {            
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPos = rankObj.getRankPosByService(pilot.getRank(), pilot.determineService(campaign.getDate()));

            String keyVal = new String("" + (rankPos * 100) + index);
            sortedPilots.put(keyVal, pilot);
            ++index;
        }
        
        return sortedPilots;
    }

}
