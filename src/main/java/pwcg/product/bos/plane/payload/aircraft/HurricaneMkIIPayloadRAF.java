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
    private Date hispanoIntroDate;

    public HurricaneMkIIPayloadRAF()
    {
    }

    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        try
        {
            hispanoIntroDate = DateUtils.getDateYYYYMMDD("19410601");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            return selectGroundAttackPayload(flight);
        }
        else 
        {
            return createStandardPayload(flight);
        }        
    }    

    private int createStandardPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 1;
        if (flight.getCampaign().getDate().after(hispanoIntroDate))
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
        if (flight.getCampaign().getDate().after(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 13;
        }

        return selectedPrimaryPayloadId;
    }    

    private int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (flight.getCampaign().getDate().before(hispanoIntroDate))
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 14;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 15;
        }

        return selectedPrimaryPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (flight.getCampaign().getDate().before(hispanoIntroDate))
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
        if (flight.getCampaign().getDate().before(hispanoIntroDate))
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
