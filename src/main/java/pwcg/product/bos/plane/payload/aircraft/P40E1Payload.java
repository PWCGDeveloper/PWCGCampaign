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

public class P40E1Payload extends PlanePayload implements IPlanePayload
{
    public P40E1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
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
        P40E1Payload clone = new P40E1Payload(planeType, date);
        
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
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectStructureTargetPayload();
        }
    }

    private void selectSoftTargetPayload()
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

    private void selectArmoredTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 4;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }

    private void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 4;
        }
        else 
        {
            selectedPrimaryPayloadId = 12;
        }
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 8;
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 8;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 2)
        {
            return false;
        }

        return true;
    }
}
