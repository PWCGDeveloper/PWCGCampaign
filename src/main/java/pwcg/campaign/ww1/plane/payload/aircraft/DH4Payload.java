package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class DH4Payload extends PlanePayload implements IPlanePayload
{
    public DH4Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X12);
        setAvailablePayload(1, "1", PayloadElement.LB20_X8, PayloadElement.LB112_X2);
        setAvailablePayload(2, "1", PayloadElement.LB112_X4);
        setAvailablePayload(3, "1", PayloadElement.LB112_X2);
        setAvailablePayload(4, "1", PayloadElement.LB230_X2);
        setAvailablePayload(7, "1", PayloadElement.CAMERA);
        setAvailablePayload(8, "1", PayloadElement.RADIO);
        setAvailablePayload(9, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 9;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 7;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 8;
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
        DH4Payload clone = new DH4Payload(planeType);
        
        return super.copy(clone);
    }
}
