package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightFactory;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.escort.EscortForPlayerFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;
import pwcg.mission.flight.opposing.AiOpposingFlightBuilder;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;

    public MissionFlightBuilder(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<IFlight> createPlayerFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        List<IFlight> playerAndAssociatedFlights = new ArrayList<>();
        for (Squadron playerSquadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(playerSquadron.getSquadronId());
            
            FlightFactory flightFactory = new FlightFactory(campaign);
            List<IFlight>  playerFlights = flightFactory.buildFlight(mission, playerSquadron, playerFlightType, NecessaryFlightType.PLAYER_FLIGHT);        

            playerAndAssociatedFlights.addAll(playerFlights);
            
            IFlight playerFlight = getPlayerFlight(playerFlights);
            if (NeedsEscortDecider.playerNeedsEscort(playerFlight))
            {
                EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                IFlight escortFlight = escortFlightBuilder.addEscort(mission, playerFlight);
                if (escortFlight != null)
                {
                    playerFlight.setAssociatedFlight(escortFlight);
                    playerAndAssociatedFlights.add(escortFlight);
                }
            }
        }
        return playerAndAssociatedFlights;
    }

    public List<IFlight> createOpposingAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        List<IFlight> opposingFlights = AiOpposingFlightBuilder.createOpposingAiFlights(mission, playerFlightTypes);
        return opposingFlights;
    }

    public List<IFlight> createAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        if (isCreateAiFlights(playerFlightTypes))
        {
            AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
            List<IFlight> otherAiFlights = aiFlightBuilder.createAiFlights(mission.getWeather());
            return otherAiFlights;
        }
        else
        {
            return new ArrayList<>();
        }
    }

    private boolean isCreateAiFlights(MissionSquadronFlightTypes playerFlightTypes)
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE)
        {
            for (FlightTypes playerFlightType : playerFlightTypes.getFlightTypes())
            {
                if (playerFlightType == FlightTypes.STRATEGIC_INTERCEPT)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static IFlight getPlayerFlight(List<IFlight> playerFlightSet) throws PWCGException
    {
        for (IFlight possiblePlayerFlight : playerFlightSet)
        {
            if (possiblePlayerFlight.getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_FLIGHT)
            {
                return possiblePlayerFlight;
            }
        }
        throw new PWCGException("No player flight created");
    }
}
