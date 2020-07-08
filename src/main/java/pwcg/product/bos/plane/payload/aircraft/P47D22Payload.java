package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class P47D22Payload extends PlanePayload implements IPlanePayload
{
    public P47D22Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "1000000", PayloadElement.MN28);
        setAvailablePayload(-1, "100000", PayloadElement.OCTANE_150_FUEL);
                
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.MG50CAL_6x);
        setAvailablePayload(2, "101", PayloadElement.MG50CAL_4x);
        setAvailablePayload(3, "1001", PayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(6, "10001", PayloadElement.P47_GROUND_ATTACK);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P47D22Payload clone = new P47D22Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 6;
    }
}
