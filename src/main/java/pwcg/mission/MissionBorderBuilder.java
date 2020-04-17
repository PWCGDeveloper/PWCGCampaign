package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class MissionBorderBuilder 
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
	
	public MissionBorderBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
	{
        this.participatingPlayers = participatingPlayers;
        this.campaign = campaign;
	}

    public CoordinateBox buildCoordinateBox(List<FlightTypes> playerFlightTypes) throws PWCGException
    {
        FlightTypes primaryPlayerFlightType = playerFlightTypes.get(0);
        if (primaryPlayerFlightType == FlightTypes.ANTI_SHIPPING_BOMB || primaryPlayerFlightType == FlightTypes.ANTI_SHIPPING_ATTACK || primaryPlayerFlightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            return buildMissionBoxAntiShipping();
        }
        else if (primaryPlayerFlightType == FlightTypes.SCRAMBLE)
        {
            return buildMissionBoxScramble();
        }
        else if (primaryPlayerFlightType == FlightTypes.STRATEGIC_INTERCEPT || primaryPlayerFlightType == FlightTypes.ANTI_SHIPPING_BOMB)
        {
            return buildMissionBoxStrategic();
        }
        else
        {
            return buildCoordinateBoxStandard();
        }
    }

    private CoordinateBox buildCoordinateBoxStandard() throws PWCGException
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
    

    private CoordinateBox buildMissionBoxAntiShipping() throws PWCGException
    {
        List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
        CoordinateBox missionBox = null;
        if (playerSquadronsInMission.size() == 1)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
            MissionAntiShippingSeaLaneFinder seaLaneFinder = new MissionAntiShippingSeaLaneFinder(campaign);
            ShippingLane shippingLane = seaLaneFinder.getShippingLaneForMission(squadron);
            if (shippingLane != null)
            {
                int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
                int randomAngle = RandomNumberGenerator.getRandom(360);
                int randomDistance = RandomNumberGenerator.getRandom(20000);
                Coordinate seaLaneTargetLocation = MathUtils.calcNextCoord(shippingLane.getShippingLaneBox().getCenter(), randomAngle, randomDistance);
                
                missionBox = CoordinateBox.coordinateBoxFromCenter(seaLaneTargetLocation, missionBoxRadius);
            }
            else
            {
                missionBox = buildCoordinateBoxStandard();
            }
        }
        else
        {
            missionBox = buildCoordinateBoxStandard();
        }
        return missionBox;
    }

    private CoordinateBox buildMissionBoxScramble() throws PWCGException
    {
        List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
        CoordinateBox missionBox = null;
        if (playerSquadronsInMission.size() == 1)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
            int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
            int randomAngle = RandomNumberGenerator.getRandom(360);
            int randomDistance = RandomNumberGenerator.getRandom(20000);
            Coordinate playerAirfieldTargetLocation = MathUtils.calcNextCoord(squadron.determineCurrentPosition(campaign.getDate()), randomAngle, randomDistance);
            missionBox = CoordinateBox.coordinateBoxFromCenter(playerAirfieldTargetLocation, missionBoxRadius);            
        }
        else
        {
            missionBox = buildCoordinateBoxStandard();
        }
        return missionBox;
    }

    private CoordinateBox buildMissionBoxStrategic() throws PWCGException
    {
        List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
        CoordinateBox missionBox = null;
        if (playerSquadronsInMission.size() == 1)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
            int strategicTargetRadius = 60000;
            List<PWCGLocation> possibleTargets = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findTownsForSideWithinRadius(
                    squadron.determineSide(), campaign.getDate(), squadron.determineCurrentPosition(campaign.getDate()), strategicTargetRadius);
            
            int index = RandomNumberGenerator.getRandom(possibleTargets.size());
            PWCGLocation targetTownLocation = possibleTargets.get(index);
            
            int missionBoxRadius = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxSizeKey) * 1000;
            missionBox = CoordinateBox.coordinateBoxFromCenter(targetTownLocation.getPosition(), missionBoxRadius);            
        }
        else
        {
            missionBox = buildCoordinateBoxStandard();
        }
        return missionBox;
    }
}
