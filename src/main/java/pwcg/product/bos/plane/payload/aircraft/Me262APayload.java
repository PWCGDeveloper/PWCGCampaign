package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Me262APayload extends PlanePayload implements IPlanePayload
{
    public Me262APayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {        
        setAvailablePayload(-4, "10000", PayloadElement.REMOVE_ARMOR);
        setAvailablePayload(-3, "10000", PayloadElement.EXTRA_ARMOR);

        setAvailablePayload(-5, "100000000", PayloadElement.AUTO_VALVE);
        setAvailablePayload(-2, "1000", PayloadElement.ARMORED_HEADREST);
        setAvailablePayload(-1, "10", PayloadElement.GYRO_GUNSIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1000001", PayloadElement.REMOVE_INNER_GUNS);
        setAvailablePayload(2, "101", PayloadElement.R4M_X26);
        setAvailablePayload(3, "10000001", PayloadElement.KG500x1);
    }

    @Override
    public IPlanePayload copy()
    {
        Me262APayload clone = new Me262APayload(planeType);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 3;
    }

    protected void selectInterceptPayload()
    {
        selectedPrimaryPayloadId = 2;
    }    

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 1 || 
            selectedPrimaryPayloadId == 2)
        {
            return false;
        }

        return true;
    }
}
