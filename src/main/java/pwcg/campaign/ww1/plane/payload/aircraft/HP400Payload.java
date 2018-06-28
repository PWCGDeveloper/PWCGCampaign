    package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;

public class HP400Payload extends PlanePayload implements IPlanePayload
{
    public HP400Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB112x16);
        setAvailablePayload(1, "1", PayloadElement.LB112x8);
        setAvailablePayload(2, "1", PayloadElement.LB250x8);
        setAvailablePayload(3, "1", PayloadElement.LB250x4);
        setAvailablePayload(4, "1", PayloadElement.LB1650x1);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 1;
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        HP400Payload clone = new HP400Payload(planeType);
        
        return super.copy(clone);
    }
}
