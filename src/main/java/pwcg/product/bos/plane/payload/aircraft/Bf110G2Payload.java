package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Bf110G2Payload extends PlanePayload
{
    public Bf110G2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10", PayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.SC250_X2);
        setAvailablePayload(2, "1", PayloadElement.SC250_X2, PayloadElement.SC50_X4);
        setAvailablePayload(3, "101", PayloadElement.SC50_X12);
        setAvailablePayload(4, "1001", PayloadElement.SC500_X2);
        setAvailablePayload(5, "1001", PayloadElement.SC500_X2, PayloadElement.SC50_X4);
        setAvailablePayload(6, "10001", PayloadElement.SC1000_X1);
        setAvailablePayload(7, "10001", PayloadElement.SC1000_X1, PayloadElement.SC250_X2);
        setAvailablePayload(8, "10001", PayloadElement.SC1000_X1, PayloadElement.SC50_X4);
        setAvailablePayload(9,  "1000001", PayloadElement.MG151_20_PODS);
        setAvailablePayload(10,  "1000001", PayloadElement.MG151_20_PODS, PayloadElement.SC50_X4);
		setAvailablePayload(11, "1000001", PayloadElement.BK37_AP_GUNPOD);
		setAvailablePayload(12, "1000001", PayloadElement.BK37_AP_GUNPOD, PayloadElement.SC50_X4);
		setAvailablePayload(13, "1000001", PayloadElement.BK37_HE_GUNPOD);
		setAvailablePayload(14, "1000001", PayloadElement.BK37_HE_GUNPOD, PayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf110G2Payload clone = new Bf110G2Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = createStandardPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        
        if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectedPayloadId = 9;
        }
        
        return selectedPayloadId;
    }

    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 3;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 11;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 6;
        }
        return selectedPayloadId;
    }
    
    private int createStandardPayload()
    {
        return getPayloadIdByDescription(PayloadElement.STANDARD.getDescription());
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0 || 
            selectedPayloadId == 9 ||
            selectedPayloadId == 10 ||
            selectedPayloadId == 11 ||
            selectedPayloadId == 13)
        {
            return false;
        }

        return true;
    }
}
