package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightTypeAbstractFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.NightFlightTypeConverter;

public class PlayerFlightTypeBuilder
{
    private Campaign campaign;
 
    public PlayerFlightTypeBuilder(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public FlightTypes determinePlayerFlightType(Squadron squadron, MissionHumanParticipants participatingPlayers, boolean isNightMission) throws PWCGException
    {
        FlightTypes flightType = FlightTypes.ANY;
        IFlightTypeFactory flightTypeFactory = PWCGFlightTypeAbstractFactory.createFlightFactory(campaign);
        if (flightType == FlightTypes.ANY)
        {
            flightType = getCampaignContextFlightType(participatingPlayers);
            if (flightType == FlightTypes.ANY)
            {
                boolean isPlayerFlight = true;
                flightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
            }
        }
        
        flightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);

        return flightType;
    }

    private FlightTypes getCampaignContextFlightType(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE))
        {
            List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
            if (playerSquadronsInMission.size() == 1)
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
                IFlightTypeFactory flightTypeFactory = PWCGFlightTypeAbstractFactory.createCampaignContextFlightFactory(campaign);
                boolean isPlayerFlight = true;
                FlightTypes playerFlightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
                return playerFlightType;
            }
        }
        
        return FlightTypes.ANY;
    }
}
