package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignModeChooser;
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

    public CoordinateBox buildCoordinateBox() throws PWCGException
    {
        return buildCoordinateBoxNearFront() ;
    }

    private CoordinateBox buildCoordinateBoxNearFront() throws PWCGException
    {
        IMissionCenterBuilder missionCenterBuilder = null;
        if (CampaignModeChooser.isCampaignModeCompetitive(campaign))
        {
            missionCenterBuilder = new MissionCenterBuilderMulti(campaign, participatingPlayers);
        }
        else
        {
            missionCenterBuilder = new MissionCenterBuilderFrontLines(campaign, participatingPlayers);
        }
        
        int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
        Coordinate missionCenterCoordinate = missionCenterBuilder.findMissionCenter(missionBoxRadius);
        CoordinateBox missionBox = CoordinateBox.coordinateBoxFromCenter(missionCenterCoordinate, missionBoxRadius);
        return missionBox;
    }
}
