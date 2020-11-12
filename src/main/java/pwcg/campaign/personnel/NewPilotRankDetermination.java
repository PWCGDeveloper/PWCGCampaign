package pwcg.campaign.personnel;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class NewPilotRankDetermination
{    
    public NewPilotRankDetermination ()
    {
    }
    
    public String getReplacementPilotRank(ArmedService service) throws PWCGException
    {
        IRankHelper rankHelper = RankFactory.createRankHelper();
        List<String> ranks = rankHelper.getRanksByService(service);

        int rankIndex = 2;
        
        rankIndex = getReplacementPilotRankIndex(ranks);

        String rank = rankHelper.getRankByService(rankIndex, service);
        
        return rank;
    }
    
    private int getReplacementPilotRankIndex(List<String> ranks)
    {
        int rankIndex = 3;
        if (ranks.size() < 5)
    	{
            rankIndex = 3;
    	}
        else
        {
            int NewPilotRankOddsLowest = 50;
            int rankOdds = RandomNumberGenerator.getRandom(100);
            if (rankOdds < NewPilotRankOddsLowest)
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
