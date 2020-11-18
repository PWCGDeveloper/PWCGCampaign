package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G6LatePayload extends Bf109Payload implements IPlanePayload
{
    public Bf109G6LatePayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000000", PayloadElement.ERLA_CANOPY);
        setAvailablePayload(-1, "1000000", PayloadElement.MW50);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.SD70_X4);
        setAvailablePayload(2, "10001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "100001", PayloadElement.BR21_X2);
        setAvailablePayload(8, "11", PayloadElement.MK108_30);
        setAvailablePayload(11, "111", PayloadElement.MK108_30, PayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G6LatePayload clone = new Bf109G6LatePayload(planeType);
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
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
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
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 8;
        }
    }    

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 8;
        }
        else
        {
            selectedPrimaryPayloadId = 11;
        }
    }    
}
