package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

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
        Assertions.assertTrue (flight.getFlightPlanes().getPlanes().size() > 0);
        
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Assertions.assertTrue (plane.getLinkTrId() == plane.getEntity().getIndex());
            Assertions.assertTrue (plane.getIndex() == plane.getEntity().getMisObjID());
        }
    }
}
