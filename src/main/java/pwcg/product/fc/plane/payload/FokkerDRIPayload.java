package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class FokkerDRIPayload extends PlanePayload implements IPlanePayload
{
    public FokkerDRIPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "100000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-2, "10000", PayloadElement.ATTITUDE_GUAGE);
        setAvailablePayload(-1, "1000", PayloadElement.IRON_SIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        FokkerDRIPayload clone = new FokkerDRIPayload(planeType);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
