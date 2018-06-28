package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Pe2S87Payload extends PlanePayload implements IPlanePayload
{
    public Pe2S87Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
	    
        setAvailablePayload(-1, "100000", PayloadElement.TURRET);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.FAB100M_X4);
        setAvailablePayload(2, "1", PayloadElement.FAB100M_X6);
        setAvailablePayload(3, "1", PayloadElement.FAB250SV_X2);
        setAvailablePayload(4, "11", PayloadElement.FAB100M_X10);
        setAvailablePayload(5, "101", PayloadElement.FAB250SV_X4);
        setAvailablePayload(6, "1001", PayloadElement.FAB500M_X2);
        setAvailablePayload(7, "10001", PayloadElement.ROS132_X10);
        setAvailablePayload(8, "10001", PayloadElement.FAB100M_X4, PayloadElement.ROS132_X10);
        setAvailablePayload(9, "10001", PayloadElement.FAB100M_X6, PayloadElement.ROS132_X10);
        setAvailablePayload(10, "10001", PayloadElement.FAB250SV_X2, PayloadElement.ROS132_X10);
	}

    @Override
    public IPlanePayload copy()
    {
        Pe2S87Payload clone = new Pe2S87Payload(planeType);
        
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
        selectedPrimaryPayloadId = 2;
    }    

    protected void selectBombingArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 5;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    protected void selectBombingMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 5;
    }

    protected void selectBombingHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
    }

    protected void selectGroundAttackSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 7;
        }
    }    

    protected void selectGroundAttackArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 5;
        }
        else
        {
            selectedPrimaryPayloadId = 7;
        }
    }

    protected void selectGroundAttackMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 5;
        }
        else
        {
            selectedPrimaryPayloadId = 7;
        }
    }

    protected void selectGroundAttackHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
    }
}
