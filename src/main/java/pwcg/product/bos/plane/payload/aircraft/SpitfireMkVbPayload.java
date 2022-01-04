package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.tank.TankType;
import pwcg.mission.flight.IFlight;

public class SpitfireMkVbPayload extends PlanePayload implements IPlanePayload
{
    public SpitfireMkVbPayload(TankType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10", PayloadElement.MERLIN_ENGINE);
        setAvailablePayload(-1, "100", PayloadElement.MIRROR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);        
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
    	SpitfireMkVbPayload clone = new SpitfireMkVbPayload(getTankType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
