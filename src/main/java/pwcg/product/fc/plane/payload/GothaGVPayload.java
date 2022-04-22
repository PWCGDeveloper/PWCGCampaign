package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class GothaGVPayload extends PlanePayload implements IPlanePayload
{
    public GothaGVPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);        
        setAvailablePayload(-5, "100000", PayloadElement.FUEL_GUAGE);
        setAvailablePayload(-4, "10000", PayloadElement.BECKER_GUN_TURRET);
        setAvailablePayload(-3, "1000", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(-2, "100", PayloadElement.BECKER_GUN_TURRET);
        setAvailablePayload(-1, "10", PayloadElement.TWIN_GUN_TURRET);
        
        setAvailablePayload(0, "1", PayloadElement.KG50x7);
        setAvailablePayload(1, "10000001", PayloadElement.KG100x7);
        setAvailablePayload(2, "10000001", PayloadElement.KG100x4);
        setAvailablePayload(3, "10000001", PayloadElement.KG100x4, PayloadElement.KG300x1);
        setAvailablePayload(4, "10000001", PayloadElement.KG300x1);
    }

    @Override
    public IPlanePayload copy()
    {
        GothaGVPayload clone = new GothaGVPayload(getPlaneType(), getDate());
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
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        return true;
    }
}
