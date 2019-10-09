package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class RolandCIIPayload extends PlanePayload implements IPlanePayload
{
    public RolandCIIPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.KG12_X4);
        setAvailablePayload(1, "1", PayloadElement.KG50x1);
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
            // Radio for arty spot
            selectedPrimaryPayloadId = 5;
        }
        else if ((flight.isBombingFlight()))
        {
            selectedPrimaryPayloadId = 0;
            selectedPrimaryPayloadId = RandomNumberGenerator.getRandom(2);
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        RolandCIIPayload clone = new RolandCIIPayload(planeType);
        
        return super.copy(clone);
    }
}
