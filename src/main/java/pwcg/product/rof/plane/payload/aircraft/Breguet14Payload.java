package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Breguet14Payload extends PlanePayload implements IPlanePayload, Cloneable
{
    public Breguet14Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB40_X8);
        setAvailablePayload(1, "1", PayloadElement.LB20_X16);
        setAvailablePayload(2, "1", PayloadElement.KG8_X32);
        setAvailablePayload(3, "1", PayloadElement.CAMERA);
        setAvailablePayload(4, "1", PayloadElement.RADIO);
        setAvailablePayload(5, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 4;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 3;
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
        Breguet14Payload clone = new Breguet14Payload(planeType);
        
        return super.copy(clone);
    }
}
