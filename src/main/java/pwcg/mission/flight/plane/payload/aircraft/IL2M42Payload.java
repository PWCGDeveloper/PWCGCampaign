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
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class IL2M42Payload extends PlanePayload implements IPlanePayload
{
    private Date sh37IntroDate;

    public IL2M42Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(87);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            sh37IntroDate = DateUtils.getDateYYYYMMDD("19421212");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-1, "100000", PlanePayloadElement.TURRET);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.VYA23_APHE_GUNPOD);
        setAvailablePayload(2, "11", PlanePayloadElement.VYA23_AP_GUNPOD);
        setAvailablePayload(3, "11", PlanePayloadElement.VYA23_HE_GUNPOD);
        setAvailablePayload(4, "101", PlanePayloadElement.SH37_APHE_GUNPOD);
        setAvailablePayload(5, "101", PlanePayloadElement.SH37_AP_GUNPOD);
        setAvailablePayload(6, "101", PlanePayloadElement.SH37_HE_GUNPOD);
        setAvailablePayload(7, "1", PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(8, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(9, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(10, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(11, "101", PlanePayloadElement.SH37_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(12, "101", PlanePayloadElement.SH37_AP_GUNPOD, PlanePayloadElement.FAB50SV_X4);
        setAvailablePayload(13, "101", PlanePayloadElement.SH37_HE_GUNPOD, PlanePayloadElement.FAB50SV_X4);        
        setAvailablePayload(14, "1", PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(15, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(16, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(17, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB50SV_X6);
        setAvailablePayload(18, "1", PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(19, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(20, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(21, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB100M_X4);
        setAvailablePayload(22, "101", PlanePayloadElement.SH37_APHE_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(23, "101", PlanePayloadElement.SH37_AP_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(24, "101", PlanePayloadElement.SH37_HE_GUNPOD, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(25, "1", PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(26, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(27, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(28, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.FAB100M_X6);
        setAvailablePayload(29, "1", PlanePayloadElement.ROS82_X8);
        setAvailablePayload(30, "11", PlanePayloadElement.VYA23_APHE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(31, "11", PlanePayloadElement.VYA23_AP_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(32, "11", PlanePayloadElement.VYA23_HE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(33, "101", PlanePayloadElement.SH37_APHE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(34, "101", PlanePayloadElement.SH37_AP_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(35, "101", PlanePayloadElement.SH37_HE_GUNPOD, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(36, "1", PlanePayloadElement.FAB50SV_X4, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(40, "1", PlanePayloadElement.FAB50SV_X6, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(44, "1", PlanePayloadElement.FAB100M_X4, PlanePayloadElement.ROS82_X8);
        setAvailablePayload(87, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        IL2M42Payload clone = new IL2M42Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 7;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }

        il2Turret();
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
            if (getDate().before(sh37IntroDate))
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
        int selectedPayloadId = 4;
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
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        return 25;
    }

    private int selectStructureTargetPayload()
    {
        int selectedPayloadId = 25;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPayloadId = 18;
        }
        return selectedPayloadId;
    }

    private void il2Turret() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        int il2TurretOddsKey = 50;
        if (roll < il2TurretOddsKey)
        {
            this.selectModification(PlanePayloadElement.TURRET);
        }
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
            selectedPayloadId == 87)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>sh37Payloads = Arrays.asList(4, 5, 6, 11, 12, 13, 22, 23, 24, 33, 34, 35);

        for (int i = 0; i < 36; ++i)
        {
            if (!sh37Payloads.contains(i))
            {
                availablePayloads.add(i);
            }
        }
        availablePayloads.add(40);
        availablePayloads.add(44);
        
        if (getDate().after(sh37IntroDate))
        {
            availablePayloads.addAll(sh37Payloads);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
