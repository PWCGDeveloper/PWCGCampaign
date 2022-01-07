package pwcg.mission.flight.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.campaign.plane.payload.PlanePayloadElement;
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
        setNoOrdnancePayloadId(104);
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
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.VYA23_APHE_GUNPOD);
        setAvailablePayload(2, "11", PlanePayloadElement.VYA23_AP_GUNPOD);
        setAvailablePayload(3, "11", PlanePayloadElement.VYA23_HE_GUNPOD);
        setAvailablePayload(4, "101", PlanePayloadElement.NS37_APHE_GUNPOD);
        setAvailablePayload(5, "101", PlanePayloadElement.NS37_AP_GUNPOD);
        setAvailablePayload(6, "101", PlanePayloadElement.NS37_HE_GUNPOD);
        setAvailablePayload(7, "1", PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(8, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(9, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(10, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(11, "101", PlanePayloadElement.NS37_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(12, "101", PlanePayloadElement.NS37_AP_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(13, "101", PlanePayloadElement.NS37_HE_GUNPOD, PlanePayloadElement.FAB50SV_X4);    
        setAvailablePayload(14, "1", PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(15, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(16, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(17, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(18, "1", PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(19, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(20, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(21, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(22, "101", PlanePayloadElement.NS37_APHE_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(23, "101", PlanePayloadElement.NS37_AP_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(24, "101", PlanePayloadElement.NS37_HE_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(25, "1", PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(26, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(27, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(28, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(29, "1", PlanePayloadElement.ROS82_X8);
        setAvailablePayload(30, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(31, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(32, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(33, "1", PlanePayloadElement.FAB50SV_X4, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(37, "1", PlanePayloadElement.FAB50SV_X6, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(41, "1", PlanePayloadElement.FAB100M_X4, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(104, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M43Payload clone = new IL2M43Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 7;
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
        int selectedPayloadId = 6;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPayloadId = 7;
        }
        else if (diceRoll < 60)
        {
            selectedPayloadId = 14;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 29;
        }
        else if (diceRoll < 95)
        {
            selectedPayloadId = 3;
        }
        else
        {
            selectedPayloadId = 6;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 2;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPayloadId = 29;
        }
        else if (diceRoll < 80)
        {
            selectedPayloadId = 2;
        }
        else
        {
            if (getDate().before(ns37IntroDate))
            {
                selectedPayloadId = 2;
            }
            else
            {
                selectedPayloadId = 5;
            }
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 1;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 18;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 29;
        }
        else if (diceRoll < 95)
        {
            selectedPayloadId = 1;
        }
        else
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        return 25;
    }

    private int selectStructureTargetPayload()
    {
        int selectedPayloadId = 18;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 18;
        }
        else
        {
            selectedPayloadId = 25;
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
            selectedPayloadId == 4 ||
            selectedPayloadId == 5 ||
            selectedPayloadId == 6 ||
            selectedPayloadId == 104)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>ns37Payloads = Arrays.asList(4, 5, 6, 11, 12, 13, 22, 23, 24);

        for (int i = 0; i < 33; ++i)
        {
            if (!ns37Payloads.contains(i))
            {
                availablePayloads.add(i);
            }
        }
        availablePayloads.add(37);
        availablePayloads.add(41);
        
        if (getDate().after(ns37IntroDate))
        {
            availablePayloads.addAll(ns37Payloads);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
