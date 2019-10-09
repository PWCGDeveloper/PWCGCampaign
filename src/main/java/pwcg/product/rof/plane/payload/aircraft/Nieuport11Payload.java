package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Nieuport11Payload extends PlanePayload implements IPlanePayload
{
    public Nieuport11Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(7, "1", PayloadElement.LE_PRIEUR_ROCKETS);
    }


    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.BALLOON_BUST)
        {
            selectedPrimaryPayloadId = 7;
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        Nieuport11Payload clone = new Nieuport11Payload(planeType);
        
        return super.copy(clone);
    }
}
