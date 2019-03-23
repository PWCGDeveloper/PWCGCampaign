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
        int rankOdds = RandomNumberGenerator.getRandom(100);
        
        int NewPilotRankOddsLowest = 15;
        int NewPilotRankOddsLow = 25;
        rankIndex = getReplacementPilotRankIndex(ranks, rankOdds, NewPilotRankOddsLowest, NewPilotRankOddsLow);

        String rank = rankHelper.getRankByService(rankIndex, service);
        
        return rank;
    }
    
    private int getReplacementPilotRankIndex(List<String> ranks, int rankOdds, int NewPilotRankOddsLowest, int NewPilotRankOddsLow)
    {
        int rankIndex;
        if (rankOdds < NewPilotRankOddsLowest)
        {
            rankIndex = 4;
            if (ranks.size() < 5)
            {
                rankIndex = 3;
            }
        }
        else if (rankOdds < (NewPilotRankOddsLowest + NewPilotRankOddsLow))
        {
            rankIndex = 3;
        }
        else
        {
            rankIndex = 2;
        }
        return rankIndex;
    }

}
