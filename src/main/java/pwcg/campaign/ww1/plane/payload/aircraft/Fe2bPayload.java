package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Fe2bPayload extends PlanePayload implements IPlanePayload
{
    public Fe2bPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X8, PayloadElement.LB230_X1);
        setAvailablePayload(1, "1", PayloadElement.LB112_X3);
        setAvailablePayload(2, "1", PayloadElement.LB20_X12);
        setAvailablePayload(3, "1", PayloadElement.LB230_X1);
        setAvailablePayload(4, "1", PayloadElement.LB112_X1);
        setAvailablePayload(5, "1", PayloadElement.LB20_X4);
        setAvailablePayload(8, "1", PayloadElement.CAMERA);
        setAvailablePayload(9, "1", PayloadElement.RADIO);
        setAvailablePayload(10, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 10;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 8;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 9;
        }
        else if ((flight.isBombingFlight()))
        {
            selectedPrimaryPayloadId = 2;
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        Fe2bPayload clone = new Fe2bPayload(planeType);
        
        return super.copy(clone);
    }
}
