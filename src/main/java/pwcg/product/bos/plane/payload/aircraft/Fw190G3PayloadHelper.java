package pwcg.product.bos.plane.payload.aircraft;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190G3PayloadHelper
{
    public static int selectFW190G3Payload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 48;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPrimaryPayloadId = selectFW190G3GroundAttackPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }    

    private static int selectFW190G3GroundAttackPayload(IFlight flight)
    {
        int selectedPrimaryPayloadId = 48;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 49;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 52;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 53;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 52;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 54;
        }
        return selectedPrimaryPayloadId;
    }
}
