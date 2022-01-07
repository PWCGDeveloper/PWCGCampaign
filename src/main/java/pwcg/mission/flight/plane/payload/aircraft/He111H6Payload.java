package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class He111H6Payload extends PlanePayload
{
    public He111H6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(13);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PlanePayloadElement.BELLY_TURRET);
        setAvailablePayload(-1, "100", PlanePayloadElement.NOSE_TURRET);
		setAvailablePayload(0, "1", PlanePayloadElement.SC50_X16);
		setAvailablePayload(1, "1", PlanePayloadElement.SC250_X4);
		setAvailablePayload(2, "1", PlanePayloadElement.SC500_X1, PlanePayloadElement.SC50_X16);
        setAvailablePayload(3, "1", PlanePayloadElement.SC500_X1, PlanePayloadElement.SC250_X4);
		setAvailablePayload(4, "1001", PlanePayloadElement.SC1000_X2);
		setAvailablePayload(5, "1001", PlanePayloadElement.SC1000_X1, PlanePayloadElement.SC50_X16);
		setAvailablePayload(6, "1001", PlanePayloadElement.SC1000_X1, PlanePayloadElement.SC250_X4);
		setAvailablePayload(7, "10001", PlanePayloadElement.SC1800_X2);
		setAvailablePayload(8, "10001", PlanePayloadElement.SC1800_X1, PlanePayloadElement.SC50_X16);
        setAvailablePayload(9, "10001", PlanePayloadElement.SC1800_X1, PlanePayloadElement.SC250_X4);
        setAvailablePayload(10, "11001", PlanePayloadElement.SC1800_X1, PlanePayloadElement.SC1000_X1);
		setAvailablePayload(11, "100001", PlanePayloadElement.SC2500_X1);
		setAvailablePayload(12, "101001", PlanePayloadElement.SC2500_X1, PlanePayloadElement.SC1000_X1);
        setAvailablePayload(13, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        He111H6Payload clone = new He111H6Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 2;
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
        int selectedPayloadId = 2;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPayloadId = 0;
        }
        else
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
        return 1;
    }

    private int selectHeavyTargetPayload()
    {
        return 2;
    }

    private int selectStructureTargetPayload()
    {
        return 7;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }

        return true;
    }
}
