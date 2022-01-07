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

public class MiG3Ser24Payload extends PlanePayload implements IPlanePayload
{
    private Date shvakIntroDate;

    public MiG3Ser24Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            shvakIntroDate = DateUtils.getDateYYYYMMDD("19420502");
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
        setAvailablePayload(1, "11", PlanePayloadElement.ROS82_X6);
        setAvailablePayload(5, "101", PlanePayloadElement.FAB50SV_X2);
        setAvailablePayload(6, "101", PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(16, "100001", PlanePayloadElement.SHVAK_UPGRADE);
        setAvailablePayload(17, "100011", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.FAB50SV_X2);
        setAvailablePayload(21, "100101", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(22, "100101", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.ROS82_X6);
	}

    @Override
    public IPlanePayload copy()
    {
        MiG3Ser24Payload clone = new MiG3Ser24Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = selectDefaultPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }

        return selectedPayloadId;
    }    

    private int selectGroundAttackPayload (IFlight flight)
    {
        int selectedPayloadId = 5;
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

    private int selectDefaultPayload()
    {
        int selectedPayloadId = 0;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 16;
            }
        }
        return selectedPayloadId;
    }    

    private int selectSoftTargetPayload()
    {
        int selectedPayloadId = 5;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 17;
            }
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 1;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 22;
            }
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 6;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 21;
            }
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        int selectedPayloadId = 6;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 21;
            }
        }
        return selectedPayloadId;
    }

    private int selectStructureTargetPayload()
    {
        int selectedPayloadId = 6;
        if (getDate().after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 21;
            }
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
            selectedPayloadId == 16)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 5,6 );
        availablePayloads.addAll(alwaysAvailablePayloads);
        if (getDate().after(shvakIntroDate))
        {
            List<Integer>availableShvakPayloads = Arrays.asList(16, 17, 21, 22);
            availablePayloads.addAll(availableShvakPayloads);
        }
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
