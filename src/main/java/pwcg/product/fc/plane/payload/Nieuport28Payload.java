package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Nieuport28Payload extends PlanePayload implements IPlanePayload
{
    public Nieuport28Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-8, "1000000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-7, "100000000", PayloadElement.SIDE_SLIP_GUAGE);
        setAvailablePayload(-6, "10000000", PayloadElement.CLOCK_GUAGE);
        setAvailablePayload(-5, "1000000", PayloadElement.COMPASS_GUAGE);
        setAvailablePayload(-4, "100000", PayloadElement.ATTITUDE_GUAGE);
        
        setAvailablePayload(-3, "10000", PayloadElement.SPEED_GUAGE);
        setAvailablePayload(-2, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.BALLOON_GUN);
    }

    @Override
    public IPlanePayload copy()
    {
        Nieuport28Payload clone = new Nieuport28Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
