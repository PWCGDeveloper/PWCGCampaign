package pwcg.mission.utils;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetType;

public class MissionInformationUtils
{

    public static boolean verifyFlightTargets(Mission mission, FlightTypes expectedFlightType, TargetType expectedTargetType, Side side) throws PWCGException
    {
        for (IFlight flight : mission.getMissionFlights().getAiFlightsForSide(side))
        {
            if (flight.getFlightType() == expectedFlightType)
            {
                if (flight.getTargetDefinition().getTargetType() != expectedTargetType)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verifyFlightTypeInMission(Mission mission, FlightTypes flightType, Side side) throws PWCGException
    {
        for (IFlight flight : mission.getMissionFlights().getFlightsForSide(side))
        {
            if (flight.getFlightInformation().getFlightType() == flightType)
            {
                return true;
            }
        }
        return false;
    }

}
