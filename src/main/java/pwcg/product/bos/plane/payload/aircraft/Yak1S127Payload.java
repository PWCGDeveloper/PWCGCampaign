package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Yak1S127Payload extends PlanePayload implements IPlanePayload
{
    public Yak1S127Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000", PayloadElement.RPK10);
        setAvailablePayload(-1, "100000", PayloadElement.MIRROR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.FAB50SV_X2);
        setAvailablePayload(2, "101", PayloadElement.FAB100M_X2);
	}

    @Override
    public IPlanePayload copy()
    {
        Yak1S127Payload clone = new Yak1S127Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 2;
        }
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0)
        {
            return false;
        }

        return true;
    }
}
