package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Nieuport17GBRPayload extends PlanePayload implements IPlanePayload
{
    public Nieuport17GBRPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "1000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-3, "100000", PayloadElement.CLOCK_GUAGE);
        setAvailablePayload(-2, "10000", PayloadElement.COMPASS_GUAGE);
        setAvailablePayload(-1, "1000", PayloadElement.SIDE_SLIP_GUAGE);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.TWIN_LEWIS);
        setAvailablePayload(2, "101", PayloadElement.LE_PRIEUR_ROCKETS);
    }

    @Override
    public IPlanePayload copy()
    {
        Nieuport17GBRPayload clone = new Nieuport17GBRPayload(getPlaneType(), getDate());
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
