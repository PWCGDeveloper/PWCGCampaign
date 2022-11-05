package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;

public class MissionBorderBuilder 
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    private Skirmish skirmish;
    private MissionSquadronFlightTypes playerFlightTypes;
	
	public MissionBorderBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers, Skirmish skirmish, MissionSquadronFlightTypes playerFlightTypes)
	{
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
        this.skirmish = skirmish;
        this.playerFlightTypes = playerFlightTypes;
	}

    public CoordinateBox buildCoordinateBox() throws PWCGException
    {
        IMissionCenterBuilder missionCenterBuilder = MissionBorderBuilderFactory.buildMissionCenterBuilder(campaign, participatingPlayers, skirmish, playerFlightTypes);
        
        int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
        Coordinate missionCenterCoordinate = missionCenterBuilder.findMissionCenter(missionBoxRadius);
        CoordinateBox missionBox = CoordinateBox.coordinateBoxFromCenter(campaign.getCampaignMap(), missionCenterCoordinate, missionBoxRadius);
        return missionBox;
    }
}
