package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class AiOpposingFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private MissionSquadronFlightTypes playerFlightTypes;
    private List<IFlight> missionFlights = new ArrayList<IFlight>();

    public AiOpposingFlightBuilder (Mission mission, MissionSquadronFlightTypes playerFlightTypes)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.playerFlightTypes = playerFlightTypes;
    }
    

    public List<IFlight> createOpposingAiFlights() throws PWCGException 
    {
        for (Squadron playerSquadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(playerSquadron.getSquadronId());
            IOpposingFlightBuilder opposingFlightBuilder = OpposingFlightBuilderFactory.buildFlightBuilderFactory(campaign, mission, playerSquadron, playerFlightType);
            if (opposingFlightBuilder != null)
            {
                IFlight flight = opposingFlightBuilder.createOpposingFlight();
                if (flight != null)
                {
                    flight.getFlightInformation().setOpposingFlight(true);
                    missionFlights.add(flight);
                }
            }
        }
        return missionFlights;
    }
}
