package pwcg.mission.flight.factory;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public class NightFlightTypeConverter
{    
    public static FlightTypes getFlightType(FlightTypes flightType, boolean isNightMission) throws PWCGException
    {
        if (isNightMission)
        {
            if (flightType == FlightTypes.OFFENSIVE                 ||
                flightType == FlightTypes.DIVE_BOMB)
            {
                return FlightTypes.GROUND_ATTACK;
            }
        }

        return flightType;
    }
 }
