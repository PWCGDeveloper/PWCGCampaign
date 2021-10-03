package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class C47Payload extends PlanePayload
{
    public C47Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(6);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "11", PayloadElement.CARGO);
        setAvailablePayload(1, "101", PayloadElement.PARADROP_CARGO);
        setAvailablePayload(2, "1001", PayloadElement.PARATROOPERS);
        setAvailablePayload(6, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        C47Payload clone = new C47Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            selectedPayloadId = 1;
        }

        return selectedPayloadId;
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
