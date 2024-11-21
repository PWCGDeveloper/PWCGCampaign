package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class SopwithStrutterPayload extends PlanePayload implements IPlanePayload
{
    public SopwithStrutterPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "10000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "1000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.WING_CUTOUT);
        setAvailablePayload(-1, "100", PayloadElement.TWIN_GUN_TURRET);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "100001", PayloadElement.BOMBS);
        setAvailablePayload(2, "1000001", PayloadElement.CAMERA);
        setAvailablePayload(3, "10000001", PayloadElement.RADIO);
    }

    @Override
    public IPlanePayload copy()
    {
        SopwithStrutterPayload clone = new SopwithStrutterPayload(getPlaneType(), getDate());
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
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPayloadId = 3;
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
