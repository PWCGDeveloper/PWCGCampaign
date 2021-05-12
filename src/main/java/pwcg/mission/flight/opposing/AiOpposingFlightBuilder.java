package pwcg.mission.flight.opposing;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class AiOpposingFlightBuilder
{
    public static List<IFlight> createOpposingAiFlights(Mission mission, MissionSquadronFlightTypes playerFlightTypes) throws PWCGException 
    {
        List<IFlight> missionFlights = new ArrayList<IFlight>();
        for (Squadron playerSquadron : playerFlightTypes.getSquadrons())
        {
            FlightTypes playerFlightType = playerFlightTypes.getFlightTypeForSquadron(playerSquadron.getSquadronId());
            IOpposingFlightBuilder opposingFlightBuilder = OpposingFlightBuilderFactory.buildFlightBuilderFactory(mission, playerSquadron, playerFlightType);
            if (opposingFlightBuilder != null)
            {
                List<IFlight> flight = opposingFlightBuilder.createOpposingFlight();
                if (flight != null)
                {
                    missionFlights.addAll(flight);
                }
            }
        }
        return missionFlights;
    }
}
