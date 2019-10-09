package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class SopStrutterPayload extends PlanePayload implements IPlanePayload
{
    public SopStrutterPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X8);
        setAvailablePayload(1, "1", PayloadElement.LB65_X2);
        setAvailablePayload(4, "1", PayloadElement.CAMERA);
        setAvailablePayload(5, "1", PayloadElement.RADIO);
        setAvailablePayload(6, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 6;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 5;
        }
        else if ((flight.isBombingFlight()))
        {
            selectedPrimaryPayloadId = 0;
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        SopStrutterPayload clone = new SopStrutterPayload(planeType);
        
        return super.copy(clone);
    }
}
