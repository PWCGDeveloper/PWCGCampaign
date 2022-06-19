package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ar234B2Payload extends PlanePayload
{
    public Ar234B2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(17);
    }

    protected void initialize()
	{
        setAvailablePayload(-3, "10000000", PayloadElement.BRAKING_PARACHUTE);
        setAvailablePayload(-2, "10000", PayloadElement.MIRROR);
        setAvailablePayload(-1, "100", PayloadElement.BOMB_SIGHT);

        setAvailablePayload(0, "1", PayloadElement.SC500_X1);
        setAvailablePayload(1, "1", PayloadElement.SC500_X1, PayloadElement.SC250_X2);
        setAvailablePayload(2, "1", PayloadElement.SC500_X3);
        setAvailablePayload(3, "11", PayloadElement.SC1000_X1);
        setAvailablePayload(4, "11", PayloadElement.SC1000_X1, PayloadElement.SC250_X2);
        
        setAvailablePayload(8, "100001", PayloadElement.BOOSTER, PayloadElement.SC500_X1);
        setAvailablePayload(9, "100001", PayloadElement.BOOSTER, PayloadElement.SC500_X1, PayloadElement.SC250_X2);
        setAvailablePayload(10, "100001", PayloadElement.BOOSTER, PayloadElement.SC500_X3);
        setAvailablePayload(11, "100011", PayloadElement.BOOSTER, PayloadElement.SC1000_X1);
        setAvailablePayload(12, "100011", PayloadElement.BOOSTER, PayloadElement.SC1000_X1, PayloadElement.SC250_X2);

        setAvailablePayload(17, "1001", PayloadElement.CAMERA);
        
        setAvailablePayload(3, "1000001", PayloadElement.BOMBS);
        setAvailablePayload(6, "101001", PayloadElement.MG151_20_GUNPOD, PayloadElement.INTERNAL_BOMBS);
        setAvailablePayload(9, "1100001", PayloadElement.INTERNAL_BOMBS, PayloadElement.BOMBS);
        setAvailablePayload(10, "1001001", PayloadElement.MG151_20_GUNPOD, PayloadElement.BOMBS);
        setAvailablePayload(13, "1101001", PayloadElement.MG151_20_GUNPOD, PayloadElement.INTERNAL_BOMBS, PayloadElement.BOMBS);
	}

    @Override
    public IPlanePayload copy()
    {
        Ar234B2Payload clone = new Ar234B2Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        if (flight.getFlightType() == FlightTypes.RECON)
        {
            return 17;
        }
        else
        {
            return selectGroundAttackPayload(flight);
        }
    }

    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getFlightType() == FlightTypes.RAID)
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectGenericGroundAttackPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectGenericGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 2;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 0;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 0;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 1;
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
