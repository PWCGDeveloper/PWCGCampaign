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

public class Ju88A4Payload extends PlanePayload
{
    public Ju88A4Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(12);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PlanePayloadElement.SC250_X4);
        setAvailablePayload(1, "11", PlanePayloadElement.SC250_X6);
		setAvailablePayload(2, "1", PlanePayloadElement.SC50_X28);
		setAvailablePayload(3, "100001", PlanePayloadElement.SC50_X44);
		setAvailablePayload(4, "1", PlanePayloadElement.SC250_X4, PlanePayloadElement.SC50_X28);
		setAvailablePayload(5, "11", PlanePayloadElement.SC250_X6, PlanePayloadElement.SC50_X28);
		setAvailablePayload(6, "101", PlanePayloadElement.SC500_X4);		
        setAvailablePayload(7, "111", PlanePayloadElement.SC500_X4, PlanePayloadElement.SC250_X2);        
        setAvailablePayload(8, "101", PlanePayloadElement.SC500_X4, PlanePayloadElement.SC50_X18);        
		setAvailablePayload(9, "1001", PlanePayloadElement.SC1000_X2);		
		setAvailablePayload(10, "10001", PlanePayloadElement.SC1800_X1);
		setAvailablePayload(11, "11001", PlanePayloadElement.SC1800_X1, PlanePayloadElement.SC1000_X1);
        setAvailablePayload(12, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju88A4Payload clone = new Ju88A4Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            selectDiveBombPayload(flight);
        }
        else
        {
            selectPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectPayload(IFlight flight)
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
        int selectedPayloadId = 2;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 80)
        {
            selectedPayloadId = 2;
        }
        else
        {
            selectedPayloadId = 0;
        }
        return selectedPayloadId;
    }    

    private int selectArmoredTargetPayload()
    {
        return 6;
    }

    private int selectMediumTargetPayload()
    {
        return 1;
    }

    private int selectHeavyTargetPayload()
    {
        return 9;
    }

    private int selectStructureTargetPayload()
    {
        return 9;
    }

    private int selectGroundAttackPayload (IFlight flight)
    {
        return 2;        
    }

    private int selectDiveBombPayload(IFlight flight)
    {
        return 0;        
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
