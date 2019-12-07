package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetCategory;

public class Bf110G2Payload extends PlanePayload
{
    public Bf110G2Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10", PayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.SC250_X2);
        setAvailablePayload(2, "1", PayloadElement.SC250_X2, PayloadElement.SC50_X4);
        setAvailablePayload(3, "101", PayloadElement.SC50_X12);
        setAvailablePayload(4, "1001", PayloadElement.SC500_X2);
        setAvailablePayload(5, "1001", PayloadElement.SC500_X2, PayloadElement.SC50_X4);
        setAvailablePayload(6, "10001", PayloadElement.SC1000_X1);
        setAvailablePayload(7, "10001", PayloadElement.SC1000_X1, PayloadElement.SC250_X2);
        setAvailablePayload(8, "10001", PayloadElement.SC1000_X1, PayloadElement.SC50_X4);
		setAvailablePayload(11, "1000001", PayloadElement.BK37_AP_GUNPOD);
		setAvailablePayload(12, "1000001", PayloadElement.BK37_AP_GUNPOD, PayloadElement.SC50_X4);
		setAvailablePayload(13, "1000001", PayloadElement.BK37_HE_GUNPOD);
		setAvailablePayload(14, "1000001", PayloadElement.BK37_HE_GUNPOD, PayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf110G2Payload clone = new Bf110G2Payload(planeType);
        
        return super.copy(clone);
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
        selectedPrimaryPayloadId = 3;
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
    
    private void createStandardPayload()
    {
        selectedPrimaryPayloadId = getPayloadIdByDescription(PayloadElement.STANDARD.getDescription());
    }

    protected void selectSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 13;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 75)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 11;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }


    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
    }
}
