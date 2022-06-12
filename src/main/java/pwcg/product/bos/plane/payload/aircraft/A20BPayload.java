package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class A20BPayload extends PlanePayload implements IPlanePayload
{
    public A20BPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(6);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "1000", PayloadElement.RPK10);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.FAB100M_X8);
        setAvailablePayload(2, "1", PayloadElement.FAB100M_X16);
        setAvailablePayload(3, "11", PayloadElement.FAB100M_X20);
        setAvailablePayload(4, "101", PayloadElement.FAB250SV_X4);
        setAvailablePayload(6, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        A20BPayload clone = new A20BPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 1;
    	if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
    	{
    		selectedPayloadId = 1;
    	}
    	else
    	{
    		selectedPayloadId = selectPayload(flight);
    	}
    	
        return selectedPayloadId;
    }

    private int selectPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }

        return true;
    }
}
