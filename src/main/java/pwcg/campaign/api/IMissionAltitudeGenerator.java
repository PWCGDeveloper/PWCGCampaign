package pwcg.campaign.api;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;

public interface IMissionAltitudeGenerator
{
    public int determineFlightAltitude(Campaign campaign, FlightTypes flightType) throws PWCGException;
}