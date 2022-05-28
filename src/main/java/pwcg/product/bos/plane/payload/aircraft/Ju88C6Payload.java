package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ju88C6Payload extends PlanePayload
{
    public Ju88C6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(12);
    }

    protected void initialize()
	{
        setAvailablePayload(-3, "10000", PayloadElement.TURRET);
        setAvailablePayload(-2, "100", PayloadElement.EXHAUST_SUPPRESSOR);
        setAvailablePayload(-1, "10", PayloadElement.EXTRA_ARMOR);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(2, "100001", PayloadElement.INTERNAL_BOMBS);
        setAvailablePayload(3, "1000001", PayloadElement.BOMBS);
        setAvailablePayload(6, "101001", PayloadElement.MG151_20_GUNPOD, PayloadElement.INTERNAL_BOMBS);
        setAvailablePayload(9, "1100001", PayloadElement.INTERNAL_BOMBS, PayloadElement.BOMBS);
        setAvailablePayload(10, "1001001", PayloadElement.MG151_20_GUNPOD, PayloadElement.BOMBS);
        setAvailablePayload(13, "1101001", PayloadElement.MG151_20_GUNPOD, PayloadElement.INTERNAL_BOMBS, PayloadElement.BOMBS);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju88C6Payload clone = new Ju88C6Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 2;
        if (flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getFlightType() == FlightTypes.RAID)
        {
            selectedPayloadId = 2;
        }
        else
        {
            selectGroundAttackPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 2;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 9;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 9;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 9;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 9;
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
