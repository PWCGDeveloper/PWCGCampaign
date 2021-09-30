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

public class MiG3Ser24Payload extends PlanePayload implements IPlanePayload
{
    private Date shvakIntroDate;

    public MiG3Ser24Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
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
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.ROS82_X6);
        setAvailablePayload(5, "101", PayloadElement.FAB50SV_X2);
        setAvailablePayload(6, "101", PayloadElement.FAB100M_X2);
        setAvailablePayload(16, "100001", PayloadElement.SHVAK_UPGRADE);
        setAvailablePayload(17, "100011", PayloadElement.SHVAK_UPGRADE, PayloadElement.FAB50SV_X2);
        setAvailablePayload(21, "100101", PayloadElement.SHVAK_UPGRADE, PayloadElement.FAB100M_X2);
        setAvailablePayload(22, "100101", PayloadElement.SHVAK_UPGRADE, PayloadElement.ROS82_X6);
	}

    @Override
    public IPlanePayload copy()
    {
        MiG3Ser24Payload clone = new MiG3Ser24Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectDefaultPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 5;
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

    private void selectDefaultPayload()
    {
        selectedPrimaryPayloadId = 0;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 16;
            }
        }
    }    

    private void selectSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 5;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 17;
            }
        }
    }    

    private void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 22;
            }
        }
    }

    private void selectMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 21;
            }
        }
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 21;
            }
        }
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 21;
            }
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
            selectedPrimaryPayloadId == 16)
        {
            return false;
        }

        return true;
    }
}
