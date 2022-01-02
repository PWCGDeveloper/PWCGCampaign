package pwcg.mission.flight.factory;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MissionWeather;

public class WeatherFlightTypeConverter
{    
    public static FlightTypes getFlightType(FlightTypes flightType, MissionWeather weather) throws PWCGException
    {
        if (weather.isWeatherFlightTypeImpactful(flightType))
        {
            if (flightType == FlightTypes.DIVE_BOMB)
            {
                flightType = FlightTypes.GROUND_ATTACK;
            }
            else if (flightType == FlightTypes.BOMB)
            {
                flightType = FlightTypes.LOW_ALT_BOMB;
            }
            else if (flightType == FlightTypes.PATROL)
            {
                flightType = FlightTypes.LOW_ALT_PATROL;
            }
            else if (flightType == FlightTypes.OFFENSIVE)
            {
                flightType = FlightTypes.LOW_ALT_CAP;
            }
            else if (flightType == FlightTypes.INTERCEPT)
            {
                flightType = FlightTypes.LOW_ALT_CAP;
            }
        }

        return flightType;
    }
 }
