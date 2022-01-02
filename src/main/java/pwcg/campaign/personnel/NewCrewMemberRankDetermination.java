package pwcg.campaign.personnel;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class NewCrewMemberRankDetermination
{    
    public NewCrewMemberRankDetermination ()
    {
    }
    
    public String getReplacementCrewMemberRank(ArmedService service) throws PWCGException
    {
        IRankHelper rankHelper = RankFactory.createRankHelper();
        List<String> ranks = rankHelper.getRanksByService(service);

        int rankIndex = 2;
        
        rankIndex = getReplacementCrewMemberRankIndex(ranks);

        String rank = rankHelper.getRankByService(rankIndex, service);
        
        return rank;
    }
    
    private int getReplacementCrewMemberRankIndex(List<String> ranks)
    {
        int rankIndex = 3;
        if (ranks.size() < 5)
    	{
            rankIndex = 3;
    	}
        else
        {
            int NewCrewMemberRankOddsLowest = 50;
            int rankOdds = RandomNumberGenerator.getRandom(100);
            if (rankOdds < NewCrewMemberRankOddsLowest)
            {
                rankIndex = 4;
            }
            else
            {
                rankIndex = 3;
            }
        }
    	
        return rankIndex;
    }

}
