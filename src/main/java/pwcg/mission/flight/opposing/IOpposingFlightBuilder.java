package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public interface IOpposingFlightBuilder
{
    IFlight createOpposingFlight() throws PWCGException;
}