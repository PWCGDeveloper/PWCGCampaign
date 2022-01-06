package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109F4Payload extends Bf109Payload implements IPlanePayload
{
    private Date mg15120mmGunPodIntroDate;

    public Bf109F4Payload(TankType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "10000", PlanePayloadElement.ARMORED_WINDSCREEN);
        setAvailablePayload(-1, "100000", PlanePayloadElement.REMOVE_HEADREST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "1001", PlanePayloadElement.SC250_X1);
		setAvailablePayload(3, "11", PlanePayloadElement.MG151_15_GUNPOD);
		setAvailablePayload(4, "1000001", PlanePayloadElement.MG151_20_GUNPOD);
	}

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            mg15120mmGunPodIntroDate = DateUtils.getDateYYYYMMDD("19420502");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    public IPlanePayload copy()
    {
        Bf109F4Payload clone = new Bf109F4Payload(getTankType(), getDate());
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
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectedPayloadId = selectInterceptPayload();
        }

        return selectedPayloadId;
    }    

    private int selectInterceptPayload() throws PWCGException
    {
        int selectedPayloadId = 0;

        if (getDate().before(mg15120mmGunPodIntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 50)
            {
                selectedPayloadId = 4;
            }
            else
            {
                selectedPayloadId = 0;
            }
        }
        return selectedPayloadId;
    }    
}
