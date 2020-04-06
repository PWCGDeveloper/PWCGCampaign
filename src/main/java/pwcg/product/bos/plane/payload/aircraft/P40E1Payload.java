package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P40E1Payload extends PlanePayload implements IPlanePayload
{
    public P40E1Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "1000000", PayloadElement.MIRROR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "11", PayloadElement.MG50CAL_4x);
        setAvailablePayload(4, "10001", PayloadElement.FAB250SV_X1);
        setAvailablePayload(6, "10011", PayloadElement.MG50CAL_4x, PayloadElement.FAB250SV_X1);
        setAvailablePayload(8, "100001", PayloadElement.FAB500M_X1);
        setAvailablePayload(10, "100011", PayloadElement.MG50CAL_4x, PayloadElement.FAB500M_X1);
        setAvailablePayload(12, "1001", PayloadElement.ROS82_X4);
        setAvailablePayload(14, "1011", PayloadElement.MG50CAL_4x, PayloadElement.ROS82_X4);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P40E1Payload clone = new P40E1Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 12;
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
    }

    protected void selectSoftTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 4;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }    

    protected void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 8;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }

    protected void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 8;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 8;
    }
}
