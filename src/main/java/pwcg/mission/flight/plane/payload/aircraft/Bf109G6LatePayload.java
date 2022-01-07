package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
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
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000000", PlanePayloadElement.ERLA_CANOPY);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.MW50);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PlanePayloadElement.SD70_X4);
        setAvailablePayload(2, "10001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PlanePayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "100001", PlanePayloadElement.BR21_X2);
        setAvailablePayload(8, "11", PlanePayloadElement.MK108_30);
        setAvailablePayload(11, "111", PlanePayloadElement.MK108_30, PlanePayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G6LatePayload clone = new Bf109G6LatePayload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }
        else
        {
            createStandardPayload();
        }

        return selectedPayloadId;
    }    
    
    protected int createStandardPayload()
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 8;
        }
        return selectedPayloadId;
    }    

    private int selectInterceptPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPayloadId = setGunPodPayload();
        }
        else if (diceRoll < 60)
        {
            selectedPayloadId = setMortarPayload();
        }
        else
        {
            selectedPayloadId = createStandardPayload();
        }
        return selectedPayloadId;
    }    

    private int setGunPodPayload() throws PWCGException
    {
        int selectedPayloadId = createStandardPayload();
        // Use discontinued after this date
        if (getDate().before(DateUtils.getDateYYYYMMDD("19440501")))
        {
            selectedPayloadId = 3;
        }
        return selectedPayloadId;
    }

    private int setMortarPayload() throws PWCGException
    {
        int selectedPayloadId = createStandardPayload();
        // Use discontinued after this date
        if (getDate().before(DateUtils.getDateYYYYMMDD("19440501")))
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }
}
