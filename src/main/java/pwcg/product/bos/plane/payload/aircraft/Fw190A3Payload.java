package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A3Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A3Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PayloadElement.MGFF_WING_GUNS);
        setAvailablePayload(5, "100001", PayloadElement.MGFF_WING_GUNS_EXTRA_AMMO);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A3Payload clone = new Fw190A3Payload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        createStandardPayload();
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
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
            selectSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectStructureTargetPayload();
        }
    }
    
    private void createStandardPayload()
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

    protected void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 3;
    }
}
