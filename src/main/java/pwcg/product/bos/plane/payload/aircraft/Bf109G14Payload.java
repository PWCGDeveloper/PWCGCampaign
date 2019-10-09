package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Bf109G14Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109G14Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{        
        setAvailablePayload(-1, "1000000", PayloadElement.FUG16_ZY);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.SD70_X4);
        setAvailablePayload(2, "10001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "111", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "1001", PayloadElement.BR21_X2);
        setAvailablePayload(8, "11", PayloadElement.MK108_30);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G14Payload clone = new Bf109G14Payload(planeType);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
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

    @Override
    protected void createStandardPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPrimaryPayloadId = 8;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }
    

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 25)
        {
            selectedPrimaryPayloadId = 0;
        }
        else if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (diceRoll < 75)
        {
            selectedPrimaryPayloadId = 4;
        }
        else
        {
            selectedPrimaryPayloadId = 8;
        }
    }
}
