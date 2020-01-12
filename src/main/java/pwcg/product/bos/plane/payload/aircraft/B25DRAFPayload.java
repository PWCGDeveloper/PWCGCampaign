package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class B25DRAFPayload extends PlanePayload implements IPlanePayload
{
    public B25DRAFPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(1, "1", PayloadElement.LB250x4);
	}

    @Override
    public IPlanePayload copy()
    {
        B25DRAFPayload clone = new B25DRAFPayload(planeType);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        return selectedPrimaryPayloadId;
    }
}
