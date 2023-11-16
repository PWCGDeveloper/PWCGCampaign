package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class HalberstadtD2Payload extends PlanePayload implements IPlanePayload
{
    public HalberstadtD2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "100000", PayloadElement.TEMPERATURE_GUAGE);
        setAvailablePayload(-3, "10000", PayloadElement.SPEED_GUAGE);
        setAvailablePayload(-2, "1000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-1, "100", PayloadElement.ALDIS_SIGHT);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        HalberstadtD2Payload clone = new HalberstadtD2Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
