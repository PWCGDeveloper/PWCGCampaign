package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class FokkerD7Payload extends PlanePayload implements IPlanePayload
{
    public FokkerD7Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "1000000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-2, "100000", PayloadElement.ALTITUDE_GUAGE);
        setAvailablePayload(-1, "1000", PayloadElement.IRON_SIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        FokkerD7Payload clone = new FokkerD7Payload(planeType);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
