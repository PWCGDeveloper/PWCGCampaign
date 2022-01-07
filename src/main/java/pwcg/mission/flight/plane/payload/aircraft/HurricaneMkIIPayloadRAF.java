package pwcg.mission.flight.plane.payload.aircraft;

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

        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else 
        {
            selectedPayloadId = createStandardPayload(flight);
        }
        return selectedPayloadId;        
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
        int selectedPayloadId = 0;
        if (date.before(hispanoIntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 12;
        }

        return selectedPayloadId;
    }

    private int selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 13;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = selectSoftTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = selectMediumTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = selectHeavyTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = selectStructureTargetPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 2;
        if (date.before(hispanoIntroDate))
        {
            selectedPayloadId = 2;
        }
        else
        {
            selectedPayloadId = 13;
        }

        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 4;
        if (date.before(hispanoIntroDate))
        {
            selectedPayloadId = 4;
        }
        else
        {
            selectedPayloadId = 14;
        }

        if (date.after(boforsIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 30)
            {
                selectedPayloadId = 15;
            }
        }
        
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 2;
        if (date.before(hispanoIntroDate))
        {
            selectedPayloadId = 2;
        }
        else
        {
            selectedPayloadId = 13;
        }

        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 4;
        if (date.before(hispanoIntroDate))
        {
            selectedPayloadId = 4;
        }
        else
        {
            selectedPayloadId = 14;
        }

        return selectedPayloadId;
    }

    private int selectStructureTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 4;
        return selectedPayloadId;
    }
}
