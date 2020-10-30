package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;

public class Bf109E7Payload extends Bf109Payload implements IPlanePayload
{
	public Bf109E7Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 1;
    }

    protected void initialize()
	{
		setAvailablePayload(-3, "100000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-2, "10000", PayloadElement.REMOVE_HEADREST);
		setAvailablePayload(-1, "1000", PayloadElement.ARMORED_WINDSCREEN);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
		setAvailablePayload(1, "11", PayloadElement.SC50_X4);
		setAvailablePayload(2, "101", PayloadElement.SC250_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109E7Payload clone = new Bf109E7Payload(planeType);
        return super.copy(clone);
    }
 }
