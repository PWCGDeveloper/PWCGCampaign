package pwcg.mission.flight.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G6Payload extends Bf109Payload implements IPlanePayload
{
    private Date mg108IntroDate;

    public Bf109G6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PlanePayloadElement.GLASS_HEADREST);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "10001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PlanePayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(4, "11", PlanePayloadElement.MK108_30);
	}

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            mg108IntroDate = DateUtils.getDateYYYYMMDD("19440801");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    public IPlanePayload copy()
    {
        Bf109G6Payload clone = new Bf109G6Payload(getPlaneType(), getDate());
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
    
    protected int createStandardPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = setMk108Payload();
        }
        return selectedPayloadId;
    }    

    private int selectInterceptPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 3;
        }
        else if (diceRoll < 80)
        {
            selectedPayloadId = setMk108Payload();
        }
        else
        {
            selectedPayloadId = 0;
        }
        return selectedPayloadId;
    }    

    private int setMk108Payload() throws PWCGException
    {
        int selectedPayloadId = 0;
        if (getDate().before(mg108IntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 2, 3);
        availablePayloads.addAll(alwaysAvailablePayloads);
        if (getDate().after(mg108IntroDate))
        {
            List<Integer>availableShvakPayloads = Arrays.asList(4);
            availablePayloads.addAll(availableShvakPayloads);
        }
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
