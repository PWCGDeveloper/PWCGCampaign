package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Bf110E2Payload extends PlanePayload
{
    public Bf110E2Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PayloadElement.ARMORED_WINDSCREEN);
		setAvailablePayload(-1, "100", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.SC250_X2);
        setAvailablePayload(2, "1", PayloadElement.SC250_X2, PayloadElement.SC50_X4);
        setAvailablePayload(3, "1001", PayloadElement.SC50_X12);
        setAvailablePayload(4, "10001", PayloadElement.SC500_X2);
		setAvailablePayload(5, "10001", PayloadElement.SC500_X2, PayloadElement.SC50_X4);
		setAvailablePayload(6, "100001", PayloadElement.SC1000_X1);
		setAvailablePayload(7, "100001", PayloadElement.SC1000_X1, PayloadElement.SC250_X2);
		setAvailablePayload(8, "10000", PayloadElement.SC1000_X1, PayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf110E2Payload clone = new Bf110E2Payload(planeType);
        
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

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 3;
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

    private void selectSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }    

    private void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    private void selectMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }
}
