package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Nieuport17Payload extends PlanePayload implements IPlanePayload
{
    public Nieuport17Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-5, "100000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-4, "10000000", PayloadElement.COMPASS_GUAGE);
        setAvailablePayload(-3, "1000000", PayloadElement.SPEED_GUAGE);
        setAvailablePayload(-2, "100000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-1, "10000", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.LEWIS_WING);
        setAvailablePayload(2, "101", PayloadElement.TWIN_LEWIS);
        setAvailablePayload(3, "1001", PayloadElement.LE_PRIEUR_ROCKETS);
    }

    @Override
    public IPlanePayload copy()
    {
        Nieuport17Payload clone = new Nieuport17Payload(getPlaneType(), getDate());
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
