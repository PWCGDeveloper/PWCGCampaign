package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;

public class StandardScoutPayload extends PlanePayload implements IPlanePayload
{
    public StandardScoutPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
       setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;        
    }

    @Override
    public IPlanePayload copy()
    {
        StandardScoutPayload clone = new StandardScoutPayload(planeType);
        
        return super.copy(clone);
    }

}
