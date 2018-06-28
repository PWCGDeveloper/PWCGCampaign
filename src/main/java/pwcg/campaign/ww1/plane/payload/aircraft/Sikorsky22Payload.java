package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;

public class Sikorsky22Payload extends PlanePayload implements IPlanePayload
{
    public Sikorsky22Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.POOD_1_10X);
        setAvailablePayload(1, "1", PayloadElement.POOD_2_10X);
        setAvailablePayload(2, "1", PayloadElement.POOD_2_5X);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        Sikorsky22Payload clone = new Sikorsky22Payload(planeType);
        
        return super.copy(clone);
    }
}
