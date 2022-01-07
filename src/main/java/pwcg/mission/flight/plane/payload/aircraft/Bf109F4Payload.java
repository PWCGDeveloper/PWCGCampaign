package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109F4Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109F4Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000", PlanePayloadElement.ARMORED_WINDSCREEN);
        setAvailablePayload(-1, "100000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PlanePayloadElement.SC250_X1);
		setAvailablePayload(3, "11", PlanePayloadElement.MG151_15_GUNPOD);
		setAvailablePayload(4, "1000001", PlanePayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109F4Payload clone = new Bf109F4Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }

        return selectedPayloadId;
    }    
}
