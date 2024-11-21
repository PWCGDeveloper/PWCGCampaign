package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class SopwithStrutterBPayload extends PlanePayload implements IPlanePayload
{
    public SopwithStrutterBPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "1000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "100", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-1, "10", PayloadElement.WING_CUTOUT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10001", PayloadElement.BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        SopwithStrutterBPayload clone = new SopwithStrutterBPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isBombingFlight(flight.getFlightType()))
        {
            selectedPayloadId = 1;
        }
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 1)
        {
            return true;
        }

        return false;
    }
}
