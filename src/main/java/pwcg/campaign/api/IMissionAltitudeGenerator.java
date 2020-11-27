package pwcg.campaign.api;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.options.MissionWeather;

public interface IMissionAltitudeGenerator
{
    public int determineFlightAltitude(Campaign campaign, FlightTypes flightType, MissionWeather missionWeather) throws PWCGException;
}