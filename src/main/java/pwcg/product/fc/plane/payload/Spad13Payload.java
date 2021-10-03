package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Spad13Payload extends PlanePayload implements IPlanePayload
{
    public Spad13Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "10000", PayloadElement.COCKPIT_LIGHT);
        
        setAvailablePayload(-2, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "11", PayloadElement.BALLOON_GUN);
        setAvailablePayload(3, "100001", PayloadElement.BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        Spad13Payload clone = new Spad13Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 3)
        {
            return true;
        }

        return false;
    }
}
