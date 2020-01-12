package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A8Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{        
        setAvailablePayload(-2, "1000000", PayloadElement.FW190_REM_GUNS);
        setAvailablePayload(-1, "10000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.SC500_X1);
        setAvailablePayload(4, "1001", PayloadElement.BR21_X2);
        setAvailablePayload(16, "11", PayloadElement.MK108_30);
        setAvailablePayload(32, "100001", PayloadElement.FW190F8);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightData().getFlightInformation().getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightData().getFlightInformation().getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        else 
        {
            createStandardPayload();
        }
        
        return selectedPrimaryPayloadId;
    }    

    protected void createStandardPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 20)
        {
            selectedPrimaryPayloadId = 16;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }

	protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 3;
        }
    }

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 16;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }    

}
