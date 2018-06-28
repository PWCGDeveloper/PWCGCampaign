package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;

public class He111H16Payload extends PlanePayload
{
    public He111H16Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.SC50_X16);
        setAvailablePayload(1, "1", PayloadElement.SC50_X32);
        setAvailablePayload(2, "1", PayloadElement.SC250_X4);
        setAvailablePayload(3, "1", PayloadElement.SC250_X8);
        setAvailablePayload(4, "1",PayloadElement.SC250_X4, PayloadElement.SC50_X16);
        setAvailablePayload(5, "1", PayloadElement.SC500_X1, PayloadElement.SC50_X16);
        setAvailablePayload(6, "1", PayloadElement.SC500_X1, PayloadElement.SC250_X4);
        setAvailablePayload(7, "1", PayloadElement.SC500_X2);
        setAvailablePayload(8, "11", PayloadElement.SC1000_X1, PayloadElement.SC50_X16);
        setAvailablePayload(9, "11", PayloadElement.SC1000_X1, PayloadElement.SC250_X4);
        setAvailablePayload(10, "11", PayloadElement.SC1000_X2);
        setAvailablePayload(11, "101", PayloadElement.SC1800_X1, PayloadElement.SC50_X16);
        setAvailablePayload(12, "101", PayloadElement.SC1800_X1, PayloadElement.SC250_X4);
        setAvailablePayload(13, "101", PayloadElement.SC1800_X2);
        setAvailablePayload(14, "111", PayloadElement.SC1800_X1, PayloadElement.SC1000_X1);
        setAvailablePayload(15, "1001", PayloadElement.SC2500_X1);
        setAvailablePayload(16, "1001", PayloadElement.SC2500_X1, PayloadElement.SC50_X16);
        setAvailablePayload(17, "1001", PayloadElement.SC2500_X1, PayloadElement.SC250_X4);
        setAvailablePayload(18, "1011", PayloadElement.SC2500_X1, PayloadElement.SC1000_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        He111H16Payload clone = new He111H16Payload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(Flight flight)
    {
        selectBombingPayload(flight);
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 2;
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
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 7;
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
            selectedPrimaryPayloadId = 3;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 10;
    }
}
