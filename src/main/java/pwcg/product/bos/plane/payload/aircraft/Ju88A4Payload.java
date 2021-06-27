package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ju88A4Payload extends PlanePayload
{
    public Ju88A4Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 12;
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.SC250_X4);
        setAvailablePayload(1, "11", PayloadElement.SC250_X6);
		setAvailablePayload(2, "1", PayloadElement.SC50_X28);
		setAvailablePayload(3, "100001", PayloadElement.SC50_X44);
		setAvailablePayload(4, "1", PayloadElement.SC250_X4, PayloadElement.SC50_X28);
		setAvailablePayload(5, "11", PayloadElement.SC250_X6, PayloadElement.SC50_X28);
		setAvailablePayload(6, "101", PayloadElement.SC500_X4);		
        setAvailablePayload(7, "111", PayloadElement.SC500_X4, PayloadElement.SC250_X2);        
        setAvailablePayload(8, "101", PayloadElement.SC500_X4, PayloadElement.SC50_X18);        
		setAvailablePayload(9, "1001", PayloadElement.SC1000_X2);		
		setAvailablePayload(10, "10001", PayloadElement.SC1800_X1);
		setAvailablePayload(11, "11001", PayloadElement.SC1800_X1, PayloadElement.SC1000_X1);
        setAvailablePayload(12, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju88A4Payload clone = new Ju88A4Payload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            selectDiveBombPayload(flight);
        }
        else
        {
            selectBombingPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }

    private void selectBombingPayload(IFlight flight)
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
        if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }    

    private void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 6;
    }

    private void selectMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }

    private void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 9;
    }

    private void selectStructureTargetPayload()
    {
        selectedPrimaryPayloadId = 9;
    }

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 2;        
    }

    private void selectDiveBombPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;        
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
