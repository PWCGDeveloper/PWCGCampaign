package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
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
        setAvailablePayload(-2, "10", PayloadElement.BELLY_TURRET);
        setAvailablePayload(-1, "100", PayloadElement.NOSE_TURRET);
		setAvailablePayload(0, "1", PayloadElement.SC50_X16);
		setAvailablePayload(1, "1", PayloadElement.SC250_X4);
		setAvailablePayload(2, "1", PayloadElement.SC500_X1, PayloadElement.SC50_X16);
        setAvailablePayload(3, "1", PayloadElement.SC500_X1, PayloadElement.SC250_X4);
		setAvailablePayload(4, "1001", PayloadElement.SC1000_X2);
		setAvailablePayload(5, "1001", PayloadElement.SC1000_X1, PayloadElement.SC50_X16);
		setAvailablePayload(6, "1001", PayloadElement.SC1000_X1, PayloadElement.SC250_X4);
		setAvailablePayload(7, "10001", PayloadElement.SC1800_X2);
		setAvailablePayload(8, "10001", PayloadElement.SC1800_X1, PayloadElement.SC50_X16);
        setAvailablePayload(9, "10001", PayloadElement.SC1800_X1, PayloadElement.SC250_X4);
        setAvailablePayload(10, "11001", PayloadElement.SC1800_X1, PayloadElement.SC1000_X1);
		setAvailablePayload(11, "100001", PayloadElement.SC2500_X1);
		setAvailablePayload(12, "101001", PayloadElement.SC2500_X1, PayloadElement.SC1000_X1);
        setAvailablePayload(13, "1", PayloadElement.EMPTY);
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
