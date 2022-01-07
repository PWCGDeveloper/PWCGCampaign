package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.IFlight;

public class SpitfireMkVbPayload extends PlanePayload implements IPlanePayload
{
    public SpitfireMkVbPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PlanePayloadElement.MERLIN_ENGINE);
        setAvailablePayload(-1, "100", PlanePayloadElement.MIRROR);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);        
	}

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
    	SpitfireMkVbPayload clone = new SpitfireMkVbPayload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
