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

public class Bf109G6LatePayload extends Bf109Payload implements IPlanePayload
{
    public Bf109G6LatePayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000000", PayloadElement.ERLA_CANOPY);
        setAvailablePayload(-1, "1000000", PayloadElement.MW50);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.SD70_X4);
        setAvailablePayload(2, "10001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "100001", PayloadElement.BR21_X2);
        setAvailablePayload(8, "11", PayloadElement.MK108_30);
        setAvailablePayload(11, "111", PayloadElement.MK108_30, PayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G6LatePayload clone = new Bf109G6LatePayload(planeType, date);
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
    
    protected void createStandardPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 8;
        }
    }    

    protected void selectInterceptPayload() throws PWCGException
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            setGunPodPayload();
        }
        else if (diceRoll < 60)
        {
            setMortarPayload();
        }
        else if (diceRoll < 90)
        {
            selectedPrimaryPayloadId = 8;
        }
        else
        {
            selectedPrimaryPayloadId = 11;
        }
    }    

    private void setGunPodPayload() throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19440902")))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 3;
        }
    }

    private void setMortarPayload() throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19440902")))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }
}
