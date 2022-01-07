package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class I16Type24Payload extends PlanePayload implements IPlanePayload
{
    public I16Type24Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
		setAvailablePayload(1, "1001", PlanePayloadElement.FAB50SV_X2);
		setAvailablePayload(2, "1001", PlanePayloadElement.FAB100M_X2);
		setAvailablePayload(3, "11", PlanePayloadElement.ROS82_X4);
		setAvailablePayload(7, "101", PlanePayloadElement.ROS82_X6);
		setAvailablePayload(11, "100001", PlanePayloadElement.SHVAK_UPGRADE);
        setAvailablePayload(12, "101001", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.FAB50SV_X2);
        setAvailablePayload(13, "101001", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(14, "100011", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.ROS82_X4);
        setAvailablePayload(18, "100101", PlanePayloadElement.SHVAK_UPGRADE, PlanePayloadElement.ROS82_X6);
	}

    @Override
    public IPlanePayload copy()
    {
        I16Type24Payload clone = new I16Type24Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }


        return selectedPayloadId;
    }    

    private int selectGroundAttackPayload (IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = selectSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = selectHeavyTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = selectStructureTargetPayload();
        }
        return selectedPayloadId;
    }

    private int selectSoftTargetPayload()
    {
        int selectedPayloadId = 3;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPayloadId = 1;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        return 3;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 3;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPayloadId = 1;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload()
    {
        return 1;
    }

    private int selectStructureTargetPayload()
    {
        return 1;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0 || 
            selectedPayloadId == 11)
        {
            return false;
        }

        return true;
    }
}
