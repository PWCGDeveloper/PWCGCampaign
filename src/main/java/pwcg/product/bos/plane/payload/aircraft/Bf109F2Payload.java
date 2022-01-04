package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.tank.TankType;

public class Bf109F2Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109F2Payload(TankType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
   	{
		setAvailablePayload(-2, "10000", PayloadElement.ARMORED_WINDSCREEN);
		setAvailablePayload(-1, "100000", PayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "11", PayloadElement.MG151_20_UPGRADE);
        setAvailablePayload(4, "111", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC50_X4);
        setAvailablePayload(5, "1011", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC250_X1);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Bf109F2Payload clone = new Bf109F2Payload(getTankType(), getDate());
        
        return super.copy(clone);
    }
}
