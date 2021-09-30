package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class SpitfireMkVbPayload extends PlanePayload implements IPlanePayload
{
    public SpitfireMkVbPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PayloadElement.MERLIN_ENGINE);
        setAvailablePayload(-1, "100", PayloadElement.MIRROR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);        
	}

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
    	SpitfireMkVbPayload clone = new SpitfireMkVbPayload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
