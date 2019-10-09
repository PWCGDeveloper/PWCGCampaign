package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class SPADXIIIPayload extends PlanePayload implements IPlanePayload
{
    public SPADXIIIPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X2);
        setAvailablePayload(1, "1", PayloadElement.LB20_X1);
        setAvailablePayload(2, "1", PayloadElement.STANDARD);
    }


    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 2;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectedPrimaryPayloadId = 1;
        }
        
        return selectedPrimaryPayloadId;
    }
    
    
    /**
     *  
     */
    @Override
    public IPlanePayload copy()
    {
        SPADXIIIPayload clone = new SPADXIIIPayload(planeType);
        
        return super.copy(clone);
    }
}
