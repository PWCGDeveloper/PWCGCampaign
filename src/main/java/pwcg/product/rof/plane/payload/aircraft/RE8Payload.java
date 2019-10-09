package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class RE8Payload extends PlanePayload implements IPlanePayload
{
    public RE8Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X12);
        setAvailablePayload(1, "1", PayloadElement.LB20_X8, PayloadElement.LB112_X2);
        setAvailablePayload(2, "1", PayloadElement.LB112_X2);
        setAvailablePayload(5, "1", PayloadElement.CAMERA);
        setAvailablePayload(6, "1", PayloadElement.RADIO);
        setAvailablePayload(7, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 5;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 6;
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        RE8Payload clone = new RE8Payload(planeType);
        
        return super.copy(clone);
    }
}
