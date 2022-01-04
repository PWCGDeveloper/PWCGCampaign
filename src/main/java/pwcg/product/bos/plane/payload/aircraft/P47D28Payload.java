package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.tank.TankType;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P47D28Payload extends PlanePayload implements IPlanePayload
{
    public P47D28Payload(TankType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-4, "100000000", PayloadElement.OCTANE_150_FUEL);
        setAvailablePayload(-3, "10000000", PayloadElement.MIRROR);
        setAvailablePayload(-2, "1000000", PayloadElement.MN28);
        setAvailablePayload(-1, "100000", PayloadElement.P47_GUNSIGHT);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.MG50CAL_6x);
        setAvailablePayload(2, "101", PayloadElement.MG50CAL_4x);
        setAvailablePayload(3, "1001", PayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(6, "10001", PayloadElement.LB500x1);
        setAvailablePayload(12, "10001", PayloadElement.LB500x2);
        setAvailablePayload(18, "10001", PayloadElement.LB500x3);
        setAvailablePayload(24, "10001", PayloadElement.LB1000x2);
        setAvailablePayload(36, "10001", PayloadElement.M8X6);
        setAvailablePayload(48, "10001", PayloadElement.P47_BOMBS_AND_ROCKETS);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P47D28Payload clone = new P47D28Payload(getTankType(), getDate());
        
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
        return 12;
    }    

    private int selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            return 36;
        }
        else
        {
            return 48;
        }
    }

    private int selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            return 12;
        }
        else if (diceRoll < 90)
        {
            return 18;
        }
        else
        {
            return 48;
        }
    }

    private int selectHeavyTargetPayload()
    {
        return 24;
    }

    private int selectStructureTargetPayload()
    {
        return 24;
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
            selectedPayloadId == 2 || 
            selectedPayloadId == 3)
        {
            return false;
        }

        return true;
    }
}
