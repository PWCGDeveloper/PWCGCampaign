package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Spad7LatePayload extends PlanePayload implements IPlanePayload
{
    public Spad7LatePayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-5, "100000", PayloadElement.SPEED_GUAGE);
        setAvailablePayload(-4, "10000", PayloadElement.LE_PRIEUR_ROCKETS);
        setAvailablePayload(-3, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-2, "100", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-1, "10", PayloadElement.LEWIS_TOP);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        Spad7LatePayload clone = new Spad7LatePayload(getPlaneType(), getDate());
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
