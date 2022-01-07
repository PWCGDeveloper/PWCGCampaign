package pwcg.mission.flight.plane.payload.aircraft;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A8PayloadHelper
{
    public static int selectFW190A8Payload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }
        else 
        {
            selectedPayloadId = createStandardPayload();
        }
        
        return selectedPayloadId;
    }    

    private static int createStandardPayload()
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 20)
        {
            selectedPayloadId =  16;
        }
        else
        {
            selectedPayloadId =  0;
        }
        return selectedPayloadId;
    }

    private static int selectInterceptPayload()
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPayloadId =  4;
        }
        else if (diceRoll < 60)
        {
            selectedPayloadId =  16;
        }
        else
        {
            selectedPayloadId =  0;
        }
        return selectedPayloadId;
    }    

    private static int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 3;
        }
        return selectedPayloadId;
    }
}
