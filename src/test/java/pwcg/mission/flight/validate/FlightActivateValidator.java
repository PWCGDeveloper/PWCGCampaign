package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class FlightActivateValidator
{
    public static void validate(IFlight flight) throws PWCGException
    {
        validateAttackLinkage(flight);
    }

    private static void validateAttackLinkage(IFlight flight) throws PWCGException
    {
        assert (flight.getFlightPlanes().getPlanes().size() > 0);
    }
}
