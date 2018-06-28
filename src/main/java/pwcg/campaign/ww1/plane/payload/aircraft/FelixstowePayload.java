package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class FelixstowePayload extends PlanePayload implements IPlanePayload
{
    public FelixstowePayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB230_X2);
        setAvailablePayload(1, "1", PayloadElement.CAMERA, PayloadElement.LB230_X2);
        setAvailablePayload(2, "1", PayloadElement.CAMERA);
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
        }
        
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        FelixstowePayload clone = new FelixstowePayload(planeType);
        
        return super.copy(clone);
    }
}
