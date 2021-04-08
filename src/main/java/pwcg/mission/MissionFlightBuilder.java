package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortForPlayerFlightBuilder;
import pwcg.mission.flight.escort.NeedsEscortDecider;
import pwcg.mission.flight.opposing.AiOpposingFlightBuilder;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private List<IFlight> playerFlights = new ArrayList<>();
    private List<IFlight> aiflights = new ArrayList<>();

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
            IFlight playerFlight = playerFlightBuilder.createPlayerFlight(playerFlightType, playerSquadron, mission.getParticipatingPlayers(), mission.isNightMission());
            playerFlights.add(playerFlight);
            
            if (NeedsEscortDecider.playerNeedsEscort(playerFlight))
            {
                EscortForPlayerFlightBuilder escortFlightBuilder = new EscortForPlayerFlightBuilder();
                escortFlightBuilder.addEscort(mission, playerFlight);
            }
            
            mission.getMissionSquadronRecorder().registerSquadronInUse(playerSquadron);
        }
    }

    private void createOpposingAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        AiOpposingFlightBuilder aiOpposingFlightBuilder = new AiOpposingFlightBuilder(mission, playerFlightTypes);
        List<IFlight> opposingFlights = aiOpposingFlightBuilder.createOpposingAiFlights();
        aiflights.addAll(opposingFlights);
    }

    private void createAiFlights(MissionSquadronFlightTypes playerFlightTypes) throws PWCGException
    {
        if (isCreateAiFlights(playerFlightTypes))
        {
            AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
            List<IFlight> otherAiFlights = aiFlightBuilder.createAiFlights(mission.getWeather());
            aiflights.addAll(otherAiFlights);
        }
    }

    private List<IFlight> keepAiFlights() throws PWCGException
    {
        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(mission, playerFlights, aiflights);
        List<IFlight> keptAiFlights = missionFlightKeeper.keepLimitedFlights();
        
        List<IFlight> keptFlights = new ArrayList<>();
        keptFlights.addAll(playerFlights);
        keptFlights.addAll(keptAiFlights);
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
}
