package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class IAR80bPayload extends PlanePayload implements IPlanePayload
{
    public IAR80bPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        IAR80bPayload clone = new IAR80bPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
