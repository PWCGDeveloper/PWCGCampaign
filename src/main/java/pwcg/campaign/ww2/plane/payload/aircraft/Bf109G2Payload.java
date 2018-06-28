package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Bf109G2Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109G2Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000", PayloadElement.GLASS_HEADREST);
        setAvailablePayload(-1, "100000", PayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "11", PayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G2Payload clone = new Bf109G2Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }    
}
