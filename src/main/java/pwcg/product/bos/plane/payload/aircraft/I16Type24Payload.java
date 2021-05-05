package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class I16Type24Payload extends PlanePayload implements IPlanePayload
{
    public I16Type24Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
		setAvailablePayload(1, "1001", PayloadElement.FAB50SV_X2);
		setAvailablePayload(2, "1001", PayloadElement.FAB100M_X2);
		setAvailablePayload(3, "11", PayloadElement.ROS82_X4);
		setAvailablePayload(7, "101", PayloadElement.ROS82_X6);
		setAvailablePayload(11, "100001", PayloadElement.SHVAK_UPGRADE);
        setAvailablePayload(12, "101001", PayloadElement.SHVAK_UPGRADE, PayloadElement.FAB50SV_X2);
        setAvailablePayload(13, "101001", PayloadElement.SHVAK_UPGRADE, PayloadElement.FAB100M_X2);
        setAvailablePayload(14, "100011", PayloadElement.SHVAK_UPGRADE, PayloadElement.ROS82_X4);
        setAvailablePayload(18, "100101", PayloadElement.SHVAK_UPGRADE, PayloadElement.ROS82_X6);
	}

    @Override
    public IPlanePayload copy()
    {
        I16Type24Payload clone = new I16Type24Payload(planeType);
        
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

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
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
            selectedPrimaryPayloadId = 1;
        }
        else 
        {
            selectedPrimaryPayloadId = 3;
        }
    }    

    private void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 3;
    }

    private void selectMediumTargetPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 1;
        }
        else 
        {
            selectedPrimaryPayloadId = 3;
        }
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 11)
        {
            return false;
        }

        return true;
    }
}
