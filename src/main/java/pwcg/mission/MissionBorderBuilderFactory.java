package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignModeChooser;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.utils.TestDriver;
import pwcg.core.exception.PWCGException;

public class MissionBorderBuilderFactory 
{
    public static IMissionCenterBuilder buildMissionCenterBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers, Skirmish skirmish) throws PWCGException
    {
        if (TestDriver.getInstance().getTestMissionCenter() != null)
        {
            return new MissionCenterBuilderTest();
        }
        else if (CampaignModeChooser.isCampaignModeCompetitive(campaign))
        {
            return new MissionCenterBuilderMulti(campaign, participatingPlayers);
        }
        else if (skirmish != null)
        {
            return new MissionCenterBuilderSkirmish(campaign, skirmish);
        }
        else
        {
            return new MissionCenterBuilderFrontLines(campaign, participatingPlayers);
        }
    }
}
