package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class IL2M41Payload extends PlanePayload implements IPlanePayload
{
    public IL2M41Payload(PlaneType planeType)
    {
        super(planeType);
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
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M41Payload clone = new IL2M41Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 4;
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
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 8;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 20;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 20;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 56;
        }
        else
        {
            selectedPrimaryPayloadId = 2;
        }
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 12;
        }
        else if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 20;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 56;
        }
        else
        {
            selectedPrimaryPayloadId = 1;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 12;
        }
        else
        {
            selectedPrimaryPayloadId = 56;
        }
    }
}
