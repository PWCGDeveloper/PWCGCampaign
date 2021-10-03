package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Yak9S1Payload extends PlanePayload implements IPlanePayload
{
    public Yak9S1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-4, "10000", PayloadElement.PBP_1A);

        setAvailablePayload(-3, "1000", PayloadElement.MIRROR);
        setAvailablePayload(-2, "100", PayloadElement.LANDING_LIGHTS);
        setAvailablePayload(-1, "10", PayloadElement.RPK10);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
	}

    @Override
    public IPlanePayload copy()
    {
        Yak9S1Payload clone = new Yak9S1Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
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
