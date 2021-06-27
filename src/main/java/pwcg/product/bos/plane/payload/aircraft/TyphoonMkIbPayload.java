package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class TyphoonMkIbPayload extends PlanePayload implements IPlanePayload
{
    public TyphoonMkIbPayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-5, "100000000", PayloadElement.DUST_DEFLECTOR);
        setAvailablePayload(-4, "10000000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-3, "1000000", PayloadElement.LB_11_BOOST);
        setAvailablePayload(-2, "100000", PayloadElement.FOUR_PLADE_PROP);
        setAvailablePayload(-1, "10000", PayloadElement.REFLECTOR_GUNSIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.LB500x2);
        setAvailablePayload(2, "1", PayloadElement.LB1000x2);
        setAvailablePayload(3, "11", PayloadElement.MKI_HE_ROCKETS_X8);
        setAvailablePayload(4, "11", PayloadElement.MKI_AP_ROCKETS_X8);
        setAvailablePayload(5, "101", PayloadElement.MKI_ROCKETS_DOUBLE);
        setAvailablePayload(6, "1001", PayloadElement.MKIII_HE_ROCKETS_X8);
        setAvailablePayload(7, "1001", PayloadElement.MKIII_AP_ROCKETS_X8);
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
            selectedPrimaryPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 7;
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
    public IPlanePayload copy()
    {
    	TyphoonMkIbPayload clone = new TyphoonMkIbPayload(planeType);
        
        return super.copy(clone);
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
