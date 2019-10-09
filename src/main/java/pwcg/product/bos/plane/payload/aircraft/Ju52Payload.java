package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Ju52Payload extends PlanePayload
{
    public Ju52Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10000", PayloadElement.TURRET);
        setAvailablePayload(0, "11", PayloadElement.CARGO);
        setAvailablePayload(1, "101", PayloadElement.MAB_250);
        setAvailablePayload(2, "1001", PayloadElement.PARATROOPERS);
        setAvailablePayload(3, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju52Payload clone = new Ju52Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.TRANSPORT || flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            selectCargoPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            selectParatroopPayload();
        }
        else if (flight.getFlightType() == FlightTypes.SPY_EXTRACT)
        {
            selectParatroopPayload();
        }
        else if ((flight.isBombingFlight()))
        {
            selectBombPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    private void selectCargoPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
    }

    private void selectBombPayload()
    {
        selectedPrimaryPayloadId = 1;
    }    

    private void selectParatroopPayload()
    {
        selectedPrimaryPayloadId = 2;
    }
}
