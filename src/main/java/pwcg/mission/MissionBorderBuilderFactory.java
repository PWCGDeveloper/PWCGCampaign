package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignModeChooser;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;

public class MissionBorderBuilderFactory 
{
    public static IMissionCenterBuilder buildCoordinateBoxNearFront(Campaign campaign, MissionHumanParticipants participatingPlayers, Skirmish skirmish) throws PWCGException
    {
        if (skirmish != null)
        {
            return new MissionCenterBuilderSkirmish(campaign, skirmish);
        }
        else if (CampaignModeChooser.isCampaignModeCompetitive(campaign))
        {
            return new MissionCenterBuilderMulti(campaign, participatingPlayers);
        }
        else
        {
            return new MissionCenterBuilderFrontLines(campaign, participatingPlayers);
        }
    }
}
