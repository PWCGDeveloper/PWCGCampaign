package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Yak9TS1Payload extends PlanePayload implements IPlanePayload
{
    public Yak9TS1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-5, "100000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-4, "10000", PayloadElement.PBP_1A);
        setAvailablePayload(-3, "1000", PayloadElement.MIRROR);
        setAvailablePayload(-2, "100", PayloadElement.LANDING_LIGHTS);
        setAvailablePayload(-1, "10", PayloadElement.RPK10);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.YAK937MM_AP);
        setAvailablePayload(2, "1", PayloadElement.YAK937MM_HE);
	}

    @Override
    public IPlanePayload copy()
    {
        Yak9TS1Payload clone = new Yak9TS1Payload(planeType, date);
        
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
        selectedPrimaryPayloadId = 2;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 2;
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
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 1;
        }
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
