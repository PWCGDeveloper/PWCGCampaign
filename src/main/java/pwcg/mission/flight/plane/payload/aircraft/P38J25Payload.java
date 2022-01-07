package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P38J25Payload extends PlanePayload implements IPlanePayload
{
    public P38J25Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-1, "100000", PlanePayloadElement.MN28);

        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(2, "101", PlanePayloadElement.LB500x2);
        setAvailablePayload(4, "101", PlanePayloadElement.LB1000x2);
        setAvailablePayload(6, "101", PlanePayloadElement.LB2000x2);
        setAvailablePayload(8, "10001", PlanePayloadElement.M8X6);
        setAvailablePayload(10, "1001", PlanePayloadElement.P38_BOMBS_AND_ROCKETS);
        setAvailablePayload(14, "1001", PlanePayloadElement.LB500x4);
        setAvailablePayload(16, "1001", PlanePayloadElement.LB500x6);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P38J25Payload clone = new P38J25Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }

        return selectedPayloadId;
    }    

    private int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 4;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = selectSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = selectHeavyTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = selectStructureTargetPayload();
        }
        return selectedPayloadId;
    }
    

    private int selectSoftTargetPayload()
    {
        int selectedPayloadId = 16;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPayloadId = 14;
        }
        else
        {
            selectedPayloadId = 16;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 10;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 8;
        }
        else
        {
            selectedPayloadId = 10;
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 16;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 10;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 14;
        }
        else
        {
            selectedPayloadId = 16;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        int selectedPayloadId = 6;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPayloadId = 4;
        }
        else
        {
            selectedPayloadId = 6;
        }
        return selectedPayloadId;
    }

    private int selectStructureTargetPayload()
    {
        int selectedPayloadId = 6;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 4;
        }
        else
        {
            selectedPayloadId = 6;
        }
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0 || 
            selectedPayloadId == 1 || 
            selectedPayloadId == 8)
        {
            return false;
        }

        return true;
    }
}
