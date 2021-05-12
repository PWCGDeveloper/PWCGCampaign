package pwcg.mission.flight.opposing;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public interface IOpposingFlightBuilder
{
    List<IFlight> createOpposingFlight() throws PWCGException;
}