package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;

public class Bf109E7Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109E7Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(1);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "100000", PlanePayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-2, "10000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(-1, "1000", PlanePayloadElement.ARMORED_WINDSCREEN);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X1);
    }

    @Override
    public IPlanePayload copy()
    {
        Bf109E7Payload clone = new Bf109E7Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }
}
