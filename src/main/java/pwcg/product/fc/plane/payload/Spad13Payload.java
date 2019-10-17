package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;

public class Spad13Payload extends PlanePayload implements IPlanePayload
{
    public Spad13Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        Spad13Payload clone = new Spad13Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
