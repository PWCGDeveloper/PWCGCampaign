package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class IL2M42Payload extends PlanePayload implements IPlanePayload
{
    public IL2M42Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "100000", PayloadElement.TURRET);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.VYA23_APHE_GUNPOD);
        setAvailablePayload(2, "11", PayloadElement.VYA23_AP_GUNPOD);
        setAvailablePayload(3, "11", PayloadElement.VYA23_HE_GUNPOD);
        setAvailablePayload(4, "101", PayloadElement.SH37_APHE_GUNPOD);
        setAvailablePayload(5, "101", PayloadElement.SH37_AP_GUNPOD);
        setAvailablePayload(6, "101", PayloadElement.SH37_HE_GUNPOD);
        setAvailablePayload(7, "1", PayloadElement.FAB50SV_X4);
        setAvailablePayload(8, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(9, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(10, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(11, "101", PayloadElement.SH37_APHE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(12, "101", PayloadElement.SH37_AP_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(13, "101", PayloadElement.SH37_HE_GUNPOD, PayloadElement.FAB50SV_X4);        
        setAvailablePayload(14, "1", PayloadElement.FAB50SV_X6);
        setAvailablePayload(15, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(16, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(17, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(18, "1", PayloadElement.FAB100M_X4);
        setAvailablePayload(19, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(20, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(21, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(22, "101", PayloadElement.SH37_APHE_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(23, "101", PayloadElement.SH37_AP_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(24, "101", PayloadElement.SH37_HE_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(25, "1", PayloadElement.FAB100M_X6);
        setAvailablePayload(26, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(27, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(28, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(29, "1", PayloadElement.ROS82_X8);
        setAvailablePayload(30, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(31, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(32, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(33, "101", PayloadElement.SH37_APHE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(34, "101", PayloadElement.SH37_AP_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(35, "101", PayloadElement.SH37_HE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(36, "1", PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(40, "1", PayloadElement.FAB50SV_X6, PayloadElement.ROS82_X8);
        setAvailablePayload(44, "1", PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M42Payload clone = new IL2M42Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 7;
        if (flight.getFlightData().getFlightInformation().getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        il2Turret();
        return selectedPrimaryPayloadId;
    }

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 4;
        if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectSoftTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
    }

    protected void selectSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 7;
        }
        else if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 14;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 29;
        }
        else if (diceRoll < 95)
        {
            selectedPrimaryPayloadId = 3;
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 18;
        }
        else if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 29;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 5;
        }
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 18;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 29;
        }
        else if (diceRoll < 95)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 25;
    }

    private void il2Turret() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        int il2TurretOddsKey = 50;
        if (roll < il2TurretOddsKey)
        {
        	this.addModification(PayloadElement.TURRET);
        }
    }
}
