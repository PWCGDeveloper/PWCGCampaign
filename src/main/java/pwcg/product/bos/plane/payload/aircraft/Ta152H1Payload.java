package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class Ta152H1Payload extends PlanePayload implements IPlanePayload
{
    public Ta152H1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void initialize()
	{        
        setAvailablePayload(-1, "10", PayloadElement.GYRO_GUNSIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Ta152H1Payload clone = new Ta152H1Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
    }    

    @Override
    public boolean isOrdnance()
    {
        return false;
    }

    @Override
    protected void loadAvailableStockModifications()
    {
        registerStockModification(PayloadElement.GYRO_GUNSIGHT);
    }
 }
