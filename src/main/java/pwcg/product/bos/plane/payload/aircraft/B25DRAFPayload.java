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
        setNoOrdnancePayloadId(5);
    }

    protected void initialize()
	{
        setAvailablePayload(1, "1", PayloadElement.LB250x4);
        setAvailablePayload(5, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        B25DRAFPayload clone = new B25DRAFPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 1;

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
