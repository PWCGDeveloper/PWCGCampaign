package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G2Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109G2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000", PlanePayloadElement.GLASS_HEADREST);
        setAvailablePayload(-1, "100000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "11", PlanePayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G2Payload clone = new Bf109G2Payload(getPlaneType(), getDate());
        
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
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }

        return selectedPayloadId;
    }    

    private int selectInterceptPayload()
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 3;
        }
        return selectedPayloadId;
    }    
}
