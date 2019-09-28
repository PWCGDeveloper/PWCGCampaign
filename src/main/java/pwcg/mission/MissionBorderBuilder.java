package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBorderBuilder 
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
	
	public MissionBorderBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
	{
        this.participatingPlayers = participatingPlayers;
        this.campaign = campaign;
	}

    public CoordinateBox buildCoordinateBox () throws PWCGException
    {
        IMissionCenterBuilder missionCenterBuilder = null;
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE)
        {
            missionCenterBuilder = new MissionCenterBuilderMulti(campaign, participatingPlayers);
        }
        else
        {
            missionCenterBuilder = new MissionCenterBuilderSingle(campaign, participatingPlayers);
        }
        
        Coordinate missionCenterCoordinate = missionCenterBuilder.findMissionCenter();
        int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
        CoordinateBox missionBox = CoordinateBox.coordinateBoxFromCenter(missionCenterCoordinate, missionBoxRadius);
        return missionBox;
    }
}
