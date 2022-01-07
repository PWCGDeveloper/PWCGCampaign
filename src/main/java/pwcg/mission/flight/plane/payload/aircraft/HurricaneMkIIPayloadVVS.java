package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class HurricaneMkIIPayloadVVS
{
    private Date date;
    private Date shvakIntroDate;
    private Date hispanoIntroDate;


    public HurricaneMkIIPayloadVVS(Date date)
    {
        this.date = date;
    }

    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        createVVSWeaponsModAvailabilityDates();

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

    protected void createVVSWeaponsModAvailabilityDates()
    {
        try
        {
            shvakIntroDate = DateUtils.getDateYYYYMMDD("19420418");
            hispanoIntroDate = DateUtils.getDateYYYYMMDD("19430102");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

    private int createStandardPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 17;
            }
        }

        if (date.after(hispanoIntroDate))
        {
            selectedPayloadId = 17;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 12;
            }
        }

        return selectedPayloadId;
    }

    private int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 2;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 18;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPayloadId = 18;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 13;
            }
        }

        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 4;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 19;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPayloadId = 19;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 14;
            }
        }

        return selectedPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 2;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 23;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPayloadId = 23;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 13;
            }
        }

        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 4;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPayloadId = 18;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPayloadId = 18;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 14;
            }
        }

        return selectedPayloadId;
    }

    private int selectStructureTargetPayload(IFlight flight) throws PWCGException
    {
        return selectHeavyTargetPayload(flight);
    }
}
