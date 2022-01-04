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

public class IL2M41Payload extends PlanePayload implements IPlanePayload
{
    public IL2M41Payload(TankType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(72);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.VYA23_APHE_GUNPOD);
        setAvailablePayload(2, "11", PayloadElement.VYA23_AP_GUNPOD);
        setAvailablePayload(3, "11", PayloadElement.VYA23_HE_GUNPOD);
        setAvailablePayload(4, "1", PayloadElement.FAB50SV_X4);
        setAvailablePayload(5, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(6, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(7, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(8, "101", PayloadElement.FAB50SV_X6);
        setAvailablePayload(12, "1", PayloadElement.FAB100M_X4);
        setAvailablePayload(13, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(14, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(15, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(16, "101", PayloadElement.FAB100M_X6);
        setAvailablePayload(20, "1", PayloadElement.ROS82_X8);
        setAvailablePayload(21, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(22, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(23, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(24, "1", PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(25, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(26, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(27, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(28, "101", PayloadElement.FAB50SV_X6, PayloadElement.ROS82_X8);
        setAvailablePayload(32, "1", PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(33, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(34, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(35, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(56, "100001", PayloadElement.ROFS132_X8);
        setAvailablePayload(57, "100011", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.ROFS132_X8);
        setAvailablePayload(58, "100011", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.ROFS132_X8);
        setAvailablePayload(59, "100011", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.ROFS132_X8);
        setAvailablePayload(72, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M41Payload clone = new IL2M41Payload(getTankType(), getDate());
        
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
        int selectedPayloadId = 3;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPayloadId = 4;
        }
        else if (diceRoll < 60)
        {
            selectedPayloadId = 8;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 20;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 2;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPayloadId = 20;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 56;
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 1;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 12;
        }
        else if (diceRoll < 80)
        {
            selectedPayloadId = 20;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 56;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        int selectedPayloadId = 56;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 12;
        }
        return selectedPayloadId;
    }

    private int selectStructureTargetPayload()
    {
        int selectedPayloadId = 16;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 12;
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
            selectedPayloadId == 2 ||
            selectedPayloadId == 3 ||
            selectedPayloadId == 72)
        {
            return false;
        }

        return true;
    }
}
