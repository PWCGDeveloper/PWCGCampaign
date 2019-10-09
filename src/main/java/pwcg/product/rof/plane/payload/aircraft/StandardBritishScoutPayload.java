package pwcg.product.rof.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class StandardBritishScoutPayload extends PlanePayload implements IPlanePayload
{
    public StandardBritishScoutPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.LB20_X4);
        setAvailablePayload(1, "1", PayloadElement.LB20_X2);
        setAvailablePayload(2, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 2;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectedPrimaryPayloadId = RandomNumberGenerator.getRandom(2);
        }
        
        return selectedPrimaryPayloadId;
    }
    
    
    /**
     *  
     */
    @Override
    public IPlanePayload copy()
    {
        StandardBritishScoutPayload clone = new StandardBritishScoutPayload(planeType);
        
        return super.copy(clone);
    }
}
