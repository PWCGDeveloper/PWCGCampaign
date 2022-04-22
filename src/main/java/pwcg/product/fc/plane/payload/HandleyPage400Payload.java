package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class HandleyPage400Payload extends PlanePayload implements IPlanePayload
{
    public HandleyPage400Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "10000", PayloadElement.FUEL_GUAGE);
        
        setAvailablePayload(-3, "1000", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(-2, "100", PayloadElement.DAVIS_GUN_TOP);
        setAvailablePayload(-1, "10", PayloadElement.DAVIS_GUN);
        
        setAvailablePayload(0, "1", PayloadElement.LB112_X8);
        setAvailablePayload(2, "1", PayloadElement.LB112_X16);
        setAvailablePayload(1, "1", PayloadElement.LB250x4);
        setAvailablePayload(3, "1", PayloadElement.LB250x8);
        setAvailablePayload(4, "100000", PayloadElement.LB1650x1);
    }

    @Override
    public IPlanePayload copy()
    {
        HandleyPage400Payload clone = new HandleyPage400Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 0;
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
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
    }

    protected int selectPayload(IFlight flight)
    {
        return 2;
    }


    @Override
    public boolean isOrdnance()
    {
        return true;
    }
}
