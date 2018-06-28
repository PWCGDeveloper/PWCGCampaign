package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;

public class Ju88A4Payload extends PlanePayload
{
    public Ju88A4Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.SC250_X4);
        setAvailablePayload(1, "11", PayloadElement.SC250_X6);
		setAvailablePayload(2, "1", PayloadElement.SC50_X28);
		setAvailablePayload(3, "100001", PayloadElement.SC50_X44);
		setAvailablePayload(4, "1", PayloadElement.SC250_X4, PayloadElement.SC50_X28);
		setAvailablePayload(5, "11", PayloadElement.SC250_X6, PayloadElement.SC50_X28);
		setAvailablePayload(6, "101", PayloadElement.SC500_X4);		
        setAvailablePayload(7, "111", PayloadElement.SC500_X4, PayloadElement.SC250_X2);        
        setAvailablePayload(8, "101", PayloadElement.SC500_X4, PayloadElement.SC50_X18);        
		setAvailablePayload(9, "1001", PayloadElement.SC1000_X2);		
		setAvailablePayload(10, "10001", PayloadElement.SC1800_X1);
		setAvailablePayload(11, "11001", PayloadElement.SC1800_X1, PayloadElement.SC1000_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju88A4Payload clone = new Ju88A4Payload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(Flight flight)
    {
        selectBombingPayload(flight);
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(Flight flight)
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
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (diceRoll < 80)
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
        if (diceRoll < 85)
        {
            selectedPrimaryPayloadId = 6;
        }
        else
        {
            selectedPrimaryPayloadId = 9;
        }
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 6;
        }
        else
        {
            selectedPrimaryPayloadId = 7;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 9;
    }
}
