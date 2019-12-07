package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetCategory;

public class La5S8Payload extends PlanePayload implements IPlanePayload
{
    public La5S8Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-4, "1000", PayloadElement.RPK10);
        setAvailablePayload(-3, "10000", PayloadElement.WINDSCREEN);
        setAvailablePayload(-2, "100000", PayloadElement.LA5_AMMO);
        setAvailablePayload(-1, "1000000", PayloadElement.M82F_ENGINE);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.FAB50SV_X2);
        setAvailablePayload(2, "101", PayloadElement.FAB100M_X2);
	}

    @Override
    public IPlanePayload copy()
    {
        La5S8Payload clone = new La5S8Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectSoftTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
    }

    protected void selectSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }    

    protected void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }
}
