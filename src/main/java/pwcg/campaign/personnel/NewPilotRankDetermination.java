package pwcg.campaign.personnel;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class NewPilotRankDetermination
{
    private Campaign campaign;
    
    public NewPilotRankDetermination (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public String getReplacementPilotRank() throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(campaign.getPlayer().determineService(campaign.getDate()));

        int rankIndex = 2;
        int rankOdds = RandomNumberGenerator.getRandom(100);

        ConfigManagerCampaign campaignConfigManager = campaign.getCampaignConfigManager();
        
        int NewPilotRankOddsLowest = campaignConfigManager.getIntConfigParam(ConfigItemKeys.NewPilotRankOddsLowestKey);
        int NewPilotRankOddsLow = campaignConfigManager.getIntConfigParam(ConfigItemKeys.NewPilotRankOddsLowKey);
        rankIndex = getReplacementPilotRankIndex(ranks, rankOdds, NewPilotRankOddsLowest, NewPilotRankOddsLow);

        String rank = rankObj.getRankByService(rankIndex, campaign.getPlayer().determineService(campaign.getDate()));
        
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
