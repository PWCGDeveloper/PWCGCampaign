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

public class P47D22Payload extends PlanePayload implements IPlanePayload
{
    public P47D22Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "1000000", PlanePayloadElement.MN28);
        setAvailablePayload(-1, "100000", PlanePayloadElement.OCTANE_150_FUEL);
                
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.MG50CAL_6x);
        setAvailablePayload(2, "101", PlanePayloadElement.MG50CAL_4x);
        setAvailablePayload(3, "1001", PlanePayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(6, "10001", PlanePayloadElement.LB500x1);
        setAvailablePayload(12, "10001", PlanePayloadElement.LB500x2);
        setAvailablePayload(18, "10001", PlanePayloadElement.LB500x3);
        setAvailablePayload(24, "10001", PlanePayloadElement.LB1000x2);
        setAvailablePayload(36, "10001", PlanePayloadElement.M8X6);
        setAvailablePayload(48, "10001", PlanePayloadElement.P47_BOMBS_AND_ROCKETS);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P47D22Payload clone = new P47D22Payload(getPlaneType(), getDate());
        
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

    private int selectGroundAttackPayload (IFlight flight)
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
        int selectedPayloadId = 48;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 36;
        }
        else
        {
            selectedPayloadId = 48;
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 48;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 12;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 18;
        }
        else
        {
            selectedPayloadId = 48;
        }
        return selectedPayloadId;
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
