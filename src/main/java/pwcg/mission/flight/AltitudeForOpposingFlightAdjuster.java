package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class AltitudeForOpposingFlightAdjuster
{
    public static double getAltitudeForOpposingFlights(Mission mission) throws PWCGException
    {
        double highestOpposingAltitude = 0.0;
        for (IFlight flight : mission.getMissionFlights().getNecessaryFlightsByType(NecessaryFlightType.OPPOSING_FLIGHT))
        {
            if (flight.getFlightInformation().getAltitude() > highestOpposingAltitude)
            {
                highestOpposingAltitude = flight.getFlightInformation().getAltitude();
            }
        }
        return highestOpposingAltitude;
    }
}
