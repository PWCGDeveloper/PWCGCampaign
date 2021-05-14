package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
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
    private List<IFlight> flights = new ArrayList<>();

    public MissionFlightBuilder(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<IFlight> generateFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        createOpposingAiFlights(playerFlightTypes);
        createAiFlights(playerFlightTypes);
        createPlayerFlights(playerFlightTypes);
        List<IFlight> keptFlights = keepAiFlights();
        return keptFlights;
    }

    private void createPlayerFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        for (Squadron playerSquadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(playerSquadron.getSquadronId());
            
            PlayerFlightBuilder playerFlightBuilder = new PlayerFlightBuilder(campaign, mission);
            List<IFlight> playerFlights = playerFlightBuilder.createPlayerFlight(playerFlightType, playerSquadron, mission.getParticipatingPlayers(), mission.isNightMission());
            addFlights(playerFlights);
            
            IFlight playerFlight = getPlayerFlight(playerFlights);
            if (NeedsEscortDecider.playerNeedsEscort(playerFlight))
            {
                EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                IFlight escortFlight = escortFlightBuilder.addEscort(mission, playerFlight);
                if (escortFlight != null)
                {
                    playerFlight.setAssociatedFlight(escortFlight);
                    addFlifght(escortFlight);
                }
            }
        }
    }

    private void createOpposingAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        List<IFlight> opposingFlights = AiOpposingFlightBuilder.createOpposingAiFlights(mission, playerFlightTypes);
        addFlights(opposingFlights);
    }

    private void createAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        if (isCreateAiFlights(playerFlightTypes))
        {
            AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
            List<IFlight> otherAiFlights = aiFlightBuilder.createAiFlights(mission.getWeather());
            addFlights(otherAiFlights);
        }
    }
    
    private void addFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            addFlifght(flight);
        }
    }
    
    private void addFlifght(IFlight flight) throws PWCGException
    {
        if (flight != null)
        {
            flights.add(flight);
        }
        else
        {
            throw new PWCGException("Null flight added");
        }
    }

    private List<IFlight> keepAiFlights() throws PWCGException
    {
        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(mission, flights);
        List<IFlight> keptFlights = missionFlightKeeper.keepLimitedFlights();
        return keptFlights;
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
