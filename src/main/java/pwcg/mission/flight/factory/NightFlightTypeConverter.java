package pwcg.mission.flight.factory;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class NightFlightTypeConverter
{    
    public static FlightTypes getFlightType(Mission mission, FlightTypes flightType) throws PWCGException
    {
        if (!mission.isNightMission())
        {
            return flightType;
        }
        
        if (flightType == FlightTypes.ESCORT                    ||
            flightType == FlightTypes.ARTILLERY_SPOT            ||
            flightType == FlightTypes.BALLOON_BUST              ||
            flightType == FlightTypes.BALLOON_DEFENSE           ||
            flightType == FlightTypes.OFFENSIVE                 ||
            flightType == FlightTypes.DIVE_BOMB)
        {
            return FlightTypes.GROUND_ATTACK;
        }

        if (flightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB)
        {
            return FlightTypes.ANTI_SHIPPING_ATTACK;
        }

        return flightType;
    }
 }
