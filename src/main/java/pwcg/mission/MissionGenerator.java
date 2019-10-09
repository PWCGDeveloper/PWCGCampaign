package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightFactoryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.IFlightTypeFactory;

public class MissionGenerator
{
    Campaign campaign = null;

    public MissionGenerator(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Mission makeMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        FlightTypes playerFlightType = getSpecialFlightType(participatingPlayers);
        Mission mission = makeMissionFromFlightType(participatingPlayers, playerFlightType);
        return mission;
    }

    public Mission makeLoneWolfMission(MissionHumanParticipants participatingPlayers) throws PWCGException 
    {
        Mission mission = makeMissionFromFlightType(participatingPlayers, FlightTypes.LONE_WOLF);
        return mission;
    }

    public Mission makeMissionFromFlightType(MissionHumanParticipants participatingPlayers, FlightTypes playerFlightType) throws PWCGException 
    {
        Mission mission = buildMission(participatingPlayers, playerFlightType);
        return mission;
    }

    private Mission buildMission(MissionHumanParticipants participatingPlayers, FlightTypes overrideFlightType) throws PWCGException 
    {
        campaign.setCurrentMission(null);        
        CoordinateBox missionBorders = buildMissionBorders(overrideFlightType, participatingPlayers);
        Mission mission = new Mission(campaign, participatingPlayers, missionBorders);
        campaign.setCurrentMission(mission);
        mission.generate(overrideFlightType);
        
        return mission;
    }

    private CoordinateBox buildMissionBorders(FlightTypes overrideFlightType, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        MissionBorderBuilder missionBorderBuilder = new MissionBorderBuilder(campaign, participatingPlayers);
        CoordinateBox missionBorders = missionBorderBuilder.buildCoordinateBox(overrideFlightType);
        return missionBorders;
    }
    
    private FlightTypes getSpecialFlightType(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE))
        {
            List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
            if (playerSquadronsInMission.size() == 1)
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
                IFlightTypeFactory flightTypeFactory = PWCGFlightFactoryFactory.createSpecialFlightFactory(campaign);
                FlightTypes playerFlightType = flightTypeFactory.getFlightType(squadron, true);
                return playerFlightType;
            }
        }
        
        return FlightTypes.ANY;
    }
}
