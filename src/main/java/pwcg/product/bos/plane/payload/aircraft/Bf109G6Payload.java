package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G6Payload extends Bf109Payload implements IPlanePayload
{
    public Bf109G6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PayloadElement.GLASS_HEADREST);
        setAvailablePayload(-1, "1000000", PayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.SC50_X4);
        setAvailablePayload(2, "10001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "11", PayloadElement.MK108_30);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G6Payload clone = new Bf109G6Payload(planeType, date);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
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
        else
        {
            createStandardPayload();
        }
        return selectedPrimaryPayloadId;
    }    
    
    protected void createStandardPayload() throws PWCGException
    {
        selectedPrimaryPayloadId = 0;

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            setMk108Payload();
        }
    }    

    protected void selectInterceptPayload() throws PWCGException
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (diceRoll < 80)
        {
            setMk108Payload();
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }    

    private void setMk108Payload() throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19440801")))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }
}
