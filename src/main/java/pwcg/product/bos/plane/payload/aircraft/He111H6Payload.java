package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class He111H6Payload extends PlanePayload
{
    public He111H6Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PayloadElement.BELLY_TURRET);
        setAvailablePayload(-1, "100", PayloadElement.NOSE_TURRET);
		setAvailablePayload(0, "1", PayloadElement.SC50_X16);
		setAvailablePayload(1, "1", PayloadElement.SC250_X4);
		setAvailablePayload(2, "1", PayloadElement.SC500_X1, PayloadElement.SC50_X16);
        setAvailablePayload(3, "1", PayloadElement.SC500_X1, PayloadElement.SC250_X4);
		setAvailablePayload(4, "1001", PayloadElement.SC1000_X2);
		setAvailablePayload(5, "1001", PayloadElement.SC1000_X1, PayloadElement.SC50_X16);
		setAvailablePayload(6, "1001", PayloadElement.SC1000_X1, PayloadElement.SC250_X4);
		setAvailablePayload(7, "10001", PayloadElement.SC1800_X2);
		setAvailablePayload(8, "10001", PayloadElement.SC1800_X1, PayloadElement.SC50_X16);
        setAvailablePayload(9, "10001", PayloadElement.SC1800_X1, PayloadElement.SC250_X4);
        setAvailablePayload(10, "11001", PayloadElement.SC1800_X1, PayloadElement.SC1000_X1);
		setAvailablePayload(11, "100001", PayloadElement.SC2500_X1);
		setAvailablePayload(12, "101001", PayloadElement.SC2500_X1, PayloadElement.SC1000_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        He111H6Payload clone = new He111H6Payload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectBombingPayload(flight);
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 2;
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
    }

    protected void selectSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 1;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 3;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

}
