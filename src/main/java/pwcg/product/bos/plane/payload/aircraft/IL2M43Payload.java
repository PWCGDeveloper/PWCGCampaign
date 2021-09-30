package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class IL2M43Payload extends PlanePayload implements IPlanePayload
{
    private Date ns37IntroDate;

    public IL2M43Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 104;
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            ns37IntroDate = DateUtils.getDateYYYYMMDD("19430702");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.VYA23_APHE_GUNPOD);
        setAvailablePayload(2, "11", PayloadElement.VYA23_AP_GUNPOD);
        setAvailablePayload(3, "11", PayloadElement.VYA23_HE_GUNPOD);
        setAvailablePayload(4, "101", PayloadElement.NS37_APHE_GUNPOD);
        setAvailablePayload(5, "101", PayloadElement.NS37_AP_GUNPOD);
        setAvailablePayload(6, "101", PayloadElement.NS37_HE_GUNPOD);
        setAvailablePayload(7, "1", PayloadElement.FAB50SV_X4);
        setAvailablePayload(8, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(9, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(10, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(11, "101", PayloadElement.NS37_APHE_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(12, "101", PayloadElement.NS37_AP_GUNPOD, PayloadElement.FAB50SV_X4);
        setAvailablePayload(13, "101", PayloadElement.NS37_HE_GUNPOD, PayloadElement.FAB50SV_X4);    
        setAvailablePayload(14, "1", PayloadElement.FAB50SV_X6);
        setAvailablePayload(15, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(16, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(17, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB50SV_X6);
        setAvailablePayload(18, "1", PayloadElement.FAB100M_X4);
        setAvailablePayload(19, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(20, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(21, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X4);
        setAvailablePayload(22, "101", PayloadElement.NS37_APHE_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(23, "101", PayloadElement.NS37_AP_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(24, "101", PayloadElement.NS37_HE_GUNPOD, PayloadElement.FAB100M_X2);
        setAvailablePayload(25, "1", PayloadElement.FAB100M_X6);
        setAvailablePayload(26, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(27, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(28, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.FAB100M_X6);
        setAvailablePayload(29, "1", PayloadElement.ROS82_X8);
        setAvailablePayload(30, "11", PayloadElement.VYA23_APHE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(31, "11", PayloadElement.VYA23_AP_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(32, "11", PayloadElement.VYA23_HE_GUNPOD, PayloadElement.ROS82_X8);
        setAvailablePayload(33, "1", PayloadElement.FAB50SV_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(37, "1", PayloadElement.FAB50SV_X6, PayloadElement.ROS82_X8);
        setAvailablePayload(41, "1", PayloadElement.FAB100M_X4, PayloadElement.ROS82_X8);
        setAvailablePayload(104, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M43Payload clone = new IL2M43Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 7;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 4;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectStructureTargetPayload();
        }
    }

    private void selectSoftTargetPayload()
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

    private void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 29;
        }
        else if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            if (date.before(ns37IntroDate))
            {
                selectedPrimaryPayloadId = 2;
            }
            else
            {
                selectedPrimaryPayloadId = 5;
            }
        }
    }

    private void selectMediumTargetPayload()
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

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 25;
    }

    private void selectStructureTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 18;
        }
        else
        {
            selectedPrimaryPayloadId = 25;
        }
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 1 ||
            selectedPrimaryPayloadId == 2 ||
            selectedPrimaryPayloadId == 3 ||
            selectedPrimaryPayloadId == 4 ||
            selectedPrimaryPayloadId == 5 ||
            selectedPrimaryPayloadId == 6 ||
            selectedPrimaryPayloadId == 104)
        {
            return false;
        }

        return true;
    }
}
