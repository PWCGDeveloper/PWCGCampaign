package pwcg.mission.flight.opposing;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class OpposingFlightBuilderFactory
{
    public static IOpposingFlightBuilder buildFlightBuilderFactory(Mission mission, Squadron playerSquadron, FlightTypes playerFlightType) throws PWCGException
    {
        if (playerFlightType == FlightTypes.BALLOON_BUST)
        {
            return new BalloonBustOpposingFlightBuilder(mission, playerSquadron);
        }
        else if (playerFlightType == FlightTypes.BALLOON_DEFENSE)
        {
            return new BalloonDefenseOpposingFlightBuilder(mission, playerSquadron);
        }
        else if (playerFlightType == FlightTypes.INTERCEPT)
        {
            return new InterceptOpposingFlightBuilder(mission, playerSquadron);
        }
        else if (playerFlightType == FlightTypes.LOW_ALT_CAP)
        {
            return new CAPOpposingFlightBuilder(mission, playerSquadron);
        }
        else if (playerFlightType == FlightTypes.SCRAMBLE)
        {
            return new ScrambleOpposingFlightBuilder(mission, playerSquadron);
        }

        return null;
    }
}
