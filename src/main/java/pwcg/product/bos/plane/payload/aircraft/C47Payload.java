package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class C47Payload extends PlanePayload
{
    public C47Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 6;
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
        C47Payload clone = new C47Payload(planeType);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            selectedPrimaryPayloadId = 1;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
        return selectedPrimaryPayloadId;
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
