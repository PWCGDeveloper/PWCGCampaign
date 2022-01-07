package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
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
        setAvailablePayload(-1, "1000000", PlanePayloadElement.FUG16_ZY);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PlanePayloadElement.SD70_X4);
        setAvailablePayload(2, "10001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "111", PlanePayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "1001", PlanePayloadElement.BR21_X2);
        setAvailablePayload(8, "11", PlanePayloadElement.MK108_30);
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
}
