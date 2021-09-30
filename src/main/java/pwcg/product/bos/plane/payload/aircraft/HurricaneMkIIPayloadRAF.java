package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class HurricaneMkIIPayloadRAF
{
    private Date date;
    private Date hispanoIntroDate;
    private Date boforsIntroDate;

    public HurricaneMkIIPayloadRAF(Date date)
    {
        this.date = date;
    }

    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        createRAFWeaponsModAvailabilityDates();

        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            return selectGroundAttackPayload(flight);
        }
        else 
        {
            return createStandardPayload(flight);
        }        
    }

    protected void createRAFWeaponsModAvailabilityDates()
    {
        try
        {
            hispanoIntroDate = DateUtils.getDateYYYYMMDD("19410601");
            boforsIntroDate = DateUtils.getDateYYYYMMDD("19430102");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    private int createStandardPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 0;
        if (date.before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 12;
        }

        return selectedPrimaryPayloadId;
    }

    private int selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 13;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = selectSoftTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = selectArmoredTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = selectMediumTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = selectHeavyTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = selectStructureTargetPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }

    private int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (date.before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 13;
        }

        return selectedPrimaryPayloadId;
    }    

    private int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (date.before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 14;
        }

        if (date.after(boforsIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 30)
            {
                selectedPrimaryPayloadId = 15;
            }
        }
        
        return selectedPrimaryPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (date.before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 13;
        }

        return selectedPrimaryPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (date.before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 14;
        }

        return selectedPrimaryPayloadId;
    }

    private int selectStructureTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        return selectedPrimaryPayloadId;
    }
}
