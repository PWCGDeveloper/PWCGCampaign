package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Nieuport28Payload extends PlanePayload implements IPlanePayload
{
    public Nieuport28Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        Nieuport28Payload clone = new Nieuport28Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
