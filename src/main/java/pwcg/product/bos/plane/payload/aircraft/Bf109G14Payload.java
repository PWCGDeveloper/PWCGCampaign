package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G14Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109G14Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
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
        Bf109G14Payload clone = new Bf109G14Payload(getPlaneType(), getDate());
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
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectedPayloadId = selectInterceptPayload();
        }
        else
        {
            selectedPayloadId = createStandardPayload();
        }

        return selectedPayloadId;

    }    

    @Override
    protected int createStandardPayload()
    {
        int selectedPayloadId = 0;

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPayloadId = 8;
        }
        else
        {
            selectedPayloadId = 0;
        }
        return selectedPayloadId;
    }
    

    private int selectInterceptPayload()
    {
        int selectedPayloadId = 0;

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 25)
        {
            selectedPayloadId = 0;
        }
        else if (diceRoll < 50)
        {
            selectedPayloadId = 3;
        }
        else if (diceRoll < 75)
        {
            selectedPayloadId = 4;
        }
        else
        {
            selectedPayloadId = 8;
        }
        return selectedPayloadId;
    }
}
