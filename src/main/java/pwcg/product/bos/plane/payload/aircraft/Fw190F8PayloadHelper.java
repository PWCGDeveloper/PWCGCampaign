package pwcg.product.bos.plane.payload.aircraft;

import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190F8PayloadHelper
{

    public static int selectFW190F8Payload(IFlight flight)
    {
        int selectedPayloadId = 32;
        if (flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 36;
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            selectedPayloadId = 34;
        }
        else if (flight.getFlightType() == FlightTypes.RAID)
        {
            selectedPayloadId = 34;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 37;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 36;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 34;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 34;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 34;
        }
        return selectedPayloadId;
    }
}
