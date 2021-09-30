package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P47D22Payload extends PlanePayload implements IPlanePayload
{
    public P47D22Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "1000000", PayloadElement.MN28);
        setAvailablePayload(-1, "100000", PayloadElement.OCTANE_150_FUEL);
                
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.MG50CAL_6x);
        setAvailablePayload(2, "101", PayloadElement.MG50CAL_4x);
        setAvailablePayload(3, "1001", PayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(6, "10001", PayloadElement.LB500x1);
        setAvailablePayload(12, "10001", PayloadElement.LB500x2);
        setAvailablePayload(18, "10001", PayloadElement.LB500x3);
        setAvailablePayload(24, "10001", PayloadElement.LB1000x2);
        setAvailablePayload(36, "10001", PayloadElement.M8X6);
        setAvailablePayload(48, "10001", PayloadElement.P47_BOMBS_AND_ROCKETS);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P47D22Payload clone = new P47D22Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 4;
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
    

    private void selectSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 12;
    }    

    private void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 36;
        }
        else
        {
            selectedPrimaryPayloadId = 48;
        }
    }

    private void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 70)
        {
            selectedPrimaryPayloadId = 12;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 18;
        }
        else
        {
            selectedPrimaryPayloadId = 48;
        }
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 24;
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 24;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 1 || 
            selectedPrimaryPayloadId == 2 || 
            selectedPrimaryPayloadId == 3)
        {
            return false;
        }

        return true;
    }
}
