package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;

public class Bf109F2Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109F2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
   	{
		setAvailablePayload(-2, "10000", PlanePayloadElement.ARMORED_WINDSCREEN);
		setAvailablePayload(-1, "100000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "11", PlanePayloadElement.MG151_20_UPGRADE);
        setAvailablePayload(4, "111", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC50_X4);
        setAvailablePayload(5, "1011", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC250_X1);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Bf109F2Payload clone = new Bf109F2Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
}
