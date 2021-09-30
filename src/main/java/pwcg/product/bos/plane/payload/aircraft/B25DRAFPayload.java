package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class B25DRAFPayload extends PlanePayload implements IPlanePayload
{
    public B25DRAFPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 5;
    }

    protected void initialize()
	{
        setAvailablePayload(1, "1", PayloadElement.LB250x4);
        setAvailablePayload(5, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        B25DRAFPayload clone = new B25DRAFPayload(planeType, date);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
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
