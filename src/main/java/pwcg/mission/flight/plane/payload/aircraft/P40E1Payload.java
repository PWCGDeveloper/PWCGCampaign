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

public class P40E1Payload extends PlanePayload implements IPlanePayload
{
    public P40E1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "1000000", PlanePayloadElement.MIRROR);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(2, "11", PlanePayloadElement.MG50CAL_4x);
        setAvailablePayload(4, "10001", PlanePayloadElement.FAB250SV_X1);
        setAvailablePayload(6, "10011", PlanePayloadElement.MG50CAL_4x, PlanePayloadElement.FAB250SV_X1);
        setAvailablePayload(8, "100001", PlanePayloadElement.FAB500M_X1);
        setAvailablePayload(10, "100011", PlanePayloadElement.MG50CAL_4x, PlanePayloadElement.FAB500M_X1);
        setAvailablePayload(12, "1001", PlanePayloadElement.ROS82_X4);
        setAvailablePayload(14, "1011", PlanePayloadElement.MG50CAL_4x, PlanePayloadElement.ROS82_X4);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P40E1Payload clone = new P40E1Payload(getPlaneType(), getDate());
        
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
        int selectedPayloadId = 12;
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
        int selectedPayloadId = 12;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPayloadId = 4;
        }
        else 
        {
            selectedPayloadId = 12;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 12;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 4;
        }
        else 
        {
            selectedPayloadId = 12;
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 12;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPayloadId = 4;
        }
        else 
        {
            selectedPayloadId = 12;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        return 8;
    }

    private int selectStructureTargetPayload()
    {
        return 8;
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
            selectedPayloadId == 2)
        {
            return false;
        }

        return true;
    }
}
