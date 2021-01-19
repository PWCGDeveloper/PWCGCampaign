package pwcg.product.bos.plane.payload.aircraft;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class HurricaneMkIIPayloadVVS
{
    public HurricaneMkIIPayloadVVS()
    {
    }

    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
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
        if (flight.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420801")))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 12;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 17;
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

    protected int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (flight.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420801")))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 13;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 18;
        }

        return selectedPrimaryPayloadId;
    }    

    protected int selectArmoredTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (flight.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420801")))
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 14;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 10)
        {
            selectedPrimaryPayloadId = 15;
        }
        else if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 19;
        }

        return selectedPrimaryPayloadId;
    }

    protected int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 2;
        if (flight.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420801")))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 13;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 23;
        }

        return selectedPrimaryPayloadId;
    }

    protected int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        if (flight.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19420801")))
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 14;
        }

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 18;
        }

        return selectedPrimaryPayloadId;
    }

    protected int selectStructureTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 4;
        return selectedPrimaryPayloadId;
    }
}
