package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Me410A1Payload extends PlanePayload
{
    public Me410A1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    /**
     *
     */
    protected void initialize()
	{
        setAvailablePayload(-2, "100000000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-1, "10000000", PayloadElement.DB603A_ENGINE);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.SD70_X8);
        setAvailablePayload(2, "1", PayloadElement.SC250_X2);
        setAvailablePayload(3, "1", PayloadElement.SC500_X1);
        setAvailablePayload(4, "1", PayloadElement.SC1000_X1);
        setAvailablePayload(5, "11", PayloadElement.MG131_x2);
//        setAvailablePayload(6, "11", PayloadElement.MG131_x2, PayloadElement.SD70_X8);
//        setAvailablePayload(7, "11", PayloadElement.MG131_x2, PayloadElement.SC250_X2);
//        setAvailablePayload(8, "11", PayloadElement.MG131_x2, PayloadElement.SC500_X1);
//        setAvailablePayload(9, "11", PayloadElement.MG131_x2, PayloadElement.SC1000_X1);
        setAvailablePayload(10, "101", PayloadElement.MG151_15_GUNPOD);
//        setAvailablePayload(11, "101", PayloadElement.MG151_15_GUNPOD, PayloadElement.SD70_X8);
//        setAvailablePayload(12, "101", PayloadElement.MG151_15_GUNPOD, PayloadElement.SC250_X2);
//        setAvailablePayload(13, "101", PayloadElement.MG151_15_GUNPOD, PayloadElement.SC500_X1);
//        setAvailablePayload(14, "101", PayloadElement.MG151_15_GUNPOD, PayloadElement.SC1000_X1);
        setAvailablePayload(30, "100001", PayloadElement.MK103_GUNS);
        setAvailablePayload(35, "1000001", PayloadElement.BK5);
	}

    @Override
    public IPlanePayload copy()
    {
        Me410A1Payload clone = new Me410A1Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = createStandardPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        
        if (flight.getFlightType() == FlightTypes.INTERCEPT || flight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT)
        {
            selectedPayloadId = 30;
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 10) 
            {
                selectedPayloadId = 35;
            }
        }
        
        if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING || flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 3;
        }

        return selectedPayloadId;
    }

    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 30;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 3;
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
            selectedPayloadId == 10 ||
            selectedPayloadId == 30 ||
            selectedPayloadId == 35)
        {
            return false;
        }

        return true;
    }
}
