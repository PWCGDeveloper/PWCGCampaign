package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.target.TargetCategory;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class P39L1Payload extends PlanePayload implements IPlanePayload
{
    public P39L1Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PayloadElement.P3937MM_AP);
        setAvailablePayload(-1, "1000000", PayloadElement.RPK10);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "1001", PayloadElement.ADDITIONJAL_AMMO);
        setAvailablePayload(4, "10001", PayloadElement.REM_M230);
        setAvailablePayload(6, "11", PayloadElement.FAB100M_X1);
        setAvailablePayload(12, "101", PayloadElement.FAB250SV_X1);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P39L1Payload clone = new P39L1Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 6;
        if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectSoftTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
    }

    protected void selectSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 6;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 12;
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 6;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 12;
    }
}
