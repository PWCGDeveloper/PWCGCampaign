package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;

public class GothaGVPayload extends PlanePayload implements IPlanePayload
{
    public GothaGVPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.KG50x7);
        setAvailablePayload(1, "1", PayloadElement.KG100x7);
        setAvailablePayload(2, "1", PayloadElement.KG100x4);
        setAvailablePayload(3, "1", PayloadElement.KG300x1, PayloadElement.KG100x4);
        setAvailablePayload(4, "1", PayloadElement.KG300x1);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 2;
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        GothaGVPayload clone = new GothaGVPayload(planeType);
        
        return super.copy(clone);
    }
}
