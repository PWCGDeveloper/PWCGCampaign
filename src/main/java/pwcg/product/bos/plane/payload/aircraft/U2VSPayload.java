package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class U2VSPayload extends PlanePayload implements IPlanePayload
{
    public U2VSPayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10", PayloadElement.U2_GUNNER);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.FAB_U2VS);
	}

    @Override
    public IPlanePayload copy()
    {
        U2VSPayload clone = new U2VSPayload(planeType);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 1;
        return selectedPrimaryPayloadId;
    }    
}
