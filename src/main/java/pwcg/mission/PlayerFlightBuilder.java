package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.PWCGFlightFactoryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.factory.IFlightTypeFactory;
import pwcg.mission.flight.factory.NightFlightTypeConverter;
import pwcg.mission.flight.plane.PlaneMcu;

public class PlayerFlightBuilder
{
    private Campaign campaign;
    private Mission mission;

    private IFlight playerFlight;
 
    public PlayerFlightBuilder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public IFlight createPlayerFlight(FlightTypes requestedFlightType, Squadron squadron) throws PWCGException 
    {
        FlightTypes flightType = finalizeFlightType(requestedFlightType, squadron);        
        buildFlight(flightType, squadron);
        return playerFlight;
    }

    private void buildFlight(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        FlightFactory flightFactory = new FlightFactory(campaign);
        boolean isPlayerFlight = true;
        playerFlight = flightFactory.buildFlight(mission, squadron, flightType, isPlayerFlight);        
        validatePlayerFlight();
    }

    private FlightTypes finalizeFlightType(FlightTypes flightType, Squadron squadron) throws PWCGException
    {
        IFlightTypeFactory flightTypeFactory = PWCGFlightFactoryFactory.createFlightFactory(campaign);
        if (flightType == FlightTypes.ANY)
        {
            flightType = getCampaignContextFlightType(mission.getParticipatingPlayers());
            if (flightType == FlightTypes.ANY)
            {
                boolean isPlayerFlight = true;
                flightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
            }
        }
        
        flightType = NightFlightTypeConverter.getFlightType(mission, flightType);

        return flightType;
    }

    private void validatePlayerFlight() throws PWCGException
    {
        boolean playerIsInFlight = false;
        for (PlaneMcu plane : playerFlight.getFlightPlanes().getPlanes())
        {
            if (plane.getPilot().isPlayer())
            {
                playerIsInFlight = true;
            }
        }
        
        if (!playerIsInFlight)
        {
            throw new PWCGException("No plane assigned to player");
        }
    }

    private FlightTypes getCampaignContextFlightType(MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        if (!(campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COMPETITIVE))
        {
            List<Integer> playerSquadronsInMission = participatingPlayers.getParticipatingSquadronIds();
            if (playerSquadronsInMission.size() == 1)
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(playerSquadronsInMission.get(0));
                IFlightTypeFactory flightTypeFactory = PWCGFlightFactoryFactory.createCampaignContextFlightFactory(campaign);
                boolean isPlayerFlight = true;
                FlightTypes playerFlightType = flightTypeFactory.getFlightType(squadron, isPlayerFlight);
                return playerFlightType;
            }
        }
        
        return FlightTypes.ANY;
    }


}
