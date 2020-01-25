package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class CamelPayload extends PlanePayload implements IPlanePayload
{
    public CamelPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(-2, "100", PayloadElement.WING_CUTOUT);
        setAvailablePayload(-1, "10", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "10001", PayloadElement.BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        CamelPayload clone = new CamelPayload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectBombingPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 2;
    }
}
