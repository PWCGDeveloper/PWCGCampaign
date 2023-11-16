package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class HanriotHD1Payload extends PlanePayload implements IPlanePayload
{
    public HanriotHD1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "100000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "10000", PayloadElement.COMPASS_GUAGE);
        setAvailablePayload(-1, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.BALLOON_GUN);  // Weird mix of payload number and mask
        setAvailablePayload(2, "11", PayloadElement.LEWIS_WING);    // Weird mix of payload number and mask
    }

    @Override
    public IPlanePayload copy()
    {
        HanriotHD1Payload clone = new HanriotHD1Payload(getPlaneType(), getDate());
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
