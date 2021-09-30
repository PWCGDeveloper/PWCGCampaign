package pwcg.product.bos.plane.payload.aircraft;

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

        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            return selectGroundAttackPayload(flight);
        }
        else 
        {
            return createStandardPayload(flight);
        }        
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

    private int createStandardPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 1;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 17;
            }
        }

        if (date.after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 17;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 12;
            }
        }

        return selectedPrimaryPayloadId;
    }

    private int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 18;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 18;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 13;
            }
        }

        return selectedPrimaryPayloadId;
    }    

    private int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 19;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 19;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 14;
            }
        }

        return selectedPrimaryPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 23;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 23;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 13;
            }
        }

        return selectedPrimaryPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (date.after(shvakIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 60)
            {
                selectedPrimaryPayloadId = 18;
            }
        }
        
        if (date.after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 18;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 14;
            }
        }

        return selectedPrimaryPayloadId;
    }

    private int selectStructureTargetPayload(IFlight flight) throws PWCGException
    {
        return selectHeavyTargetPayload(flight);
    }
}
