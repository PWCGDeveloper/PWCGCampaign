package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetCategory;

public abstract class Bf109Payload extends PlanePayload
{
    
    public Bf109Payload(PlaneType planeType)
    {
        super(planeType);
    }

    public int createWeaponsPayload(Flight flight)
    {
        createStandardPayload();
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

    protected void createStandardPayload()
    {
        selectedPrimaryPayloadId = getPayloadIdByDescription(PayloadElement.STANDARD.getDescription());
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
