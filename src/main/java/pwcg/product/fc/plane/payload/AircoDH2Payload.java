package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class AircoDH2Payload extends PlanePayload implements IPlanePayload
{
    public AircoDH2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-2, "1000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-1, "100", PayloadElement.FUEL_GUAGE);
        setAvailablePayload(-1, "10", PayloadElement.REAR_SIGHT);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10001", PayloadElement.LE_PRIEUR_ROCKETS);
    }

    @Override
    public IPlanePayload copy()
    {
        AircoDH2Payload clone = new AircoDH2Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.BALLOON_BUST) 
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
