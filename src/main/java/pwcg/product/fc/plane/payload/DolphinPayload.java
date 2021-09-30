package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class DolphinPayload extends PlanePayload implements IPlanePayload
{
    public DolphinPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "100000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "10000", PayloadElement.TEMPERATURE_GUAGE);
        
        setAvailablePayload(-1, "1000", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "1000000", PayloadElement.BOMBS);
        setAvailablePayload(4, "11", PayloadElement.LEWIS_TOP);
        setAvailablePayload(7, "101", PayloadElement.LEWIS_WING);
        setAvailablePayload(10, "111", PayloadElement.LEWIS_TOP, PayloadElement.LEWIS_WING);
    }

    @Override
    public IPlanePayload copy()
    {
        DolphinPayload clone = new DolphinPayload(planeType, date);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectBombingPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectInterceptPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 4;
        int lewisGunModRoll = RandomNumberGenerator.getRandom(100);
        if (lewisGunModRoll > 40)
        {
            selectedPrimaryPayloadId = 7;
        }
    }

    @Override
    public boolean isOrdnance()
    {
        if (selectedPrimaryPayloadId == 2)
        {
            return true;
        }

        return false;
    }
}
