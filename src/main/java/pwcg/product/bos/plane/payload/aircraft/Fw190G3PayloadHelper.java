package pwcg.product.bos.plane.payload.aircraft;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190G3PayloadHelper
{
    public static int selectFW190G3Payload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 48;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectFW190G3GroundAttackPayload(flight);
        }
        return selectedPayloadId;
    }    

    private static int selectFW190G3GroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 48;
        if (flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 52;
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            selectedPayloadId = 54;
        }
        else if (flight.getFlightType() == FlightTypes.RAID)
        {
            selectedPayloadId = 52;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 49;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 52;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 53;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 52;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 54;
        }
        return selectedPayloadId;
    }
}
