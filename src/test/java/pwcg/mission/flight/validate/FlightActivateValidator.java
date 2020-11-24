package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class FlightActivateValidator
{
    public static void validate(IFlight flight) throws PWCGException
    {
        validateAttackLinkage(flight);
    }

    private static void validateAttackLinkage(IFlight flight) throws PWCGException
    {
        assert (flight.getFlightPlanes().getPlanes().size() > 0);
        
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            assert (plane.getLinkTrId() == plane.getEntity().getIndex());
            assert (plane.getIndex() == plane.getEntity().getMisObjID());
        }
    }
}
