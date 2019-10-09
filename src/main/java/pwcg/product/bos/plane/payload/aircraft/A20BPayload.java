package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class A20BPayload extends PlanePayload implements IPlanePayload
{
    public A20BPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "1000", PayloadElement.RPK10);
        setAvailablePayload(1, "1", PayloadElement.FAB100M_X8);
        setAvailablePayload(2, "1", PayloadElement.FAB100M_X16);
        setAvailablePayload(3, "11", PayloadElement.FAB100M_X20);
        setAvailablePayload(4, "101", PayloadElement.FAB250SV_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        A20BPayload clone = new A20BPayload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 1;
    	if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
    	{
    		selectGroundAttackPayload(flight);
    	}
    	else
    	{
    		selectBombingPayload(flight);
    	}
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(Flight flight)
    {
        if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectBombingSoftTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectBombingArmoredTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectBombingMediumTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectBombingHeavyTargetPayload();
        }
    }

    protected void selectGroundAttackPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectGroundAttackSoftTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectGroundAttackArmoredTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectGroundAttackMediumTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectGroundAttackHeavyTargetPayload();
        }
    }

    protected void selectBombingSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }    

    protected void selectBombingArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 10)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 3;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    protected void selectBombingMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    protected void selectBombingHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    protected void selectGroundAttackSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }    

    protected void selectGroundAttackArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    protected void selectGroundAttackMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    protected void selectGroundAttackHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }
}
