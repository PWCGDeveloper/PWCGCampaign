package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class PfalzD12Payload extends PlanePayload implements IPlanePayload
{
    public PfalzD12Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-9, "1000000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-8, "100000000", PayloadElement.TEMPERATURE_GUAGE);
        setAvailablePayload(-7, "10000000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-6, "1000000", PayloadElement.ATTITUDE_GUAGE);
        setAvailablePayload(-5, "100000", PayloadElement.CLOCK_GUAGE);
        setAvailablePayload(-4, "10000", PayloadElement.COMPASS_GUAGE);        
        setAvailablePayload(-3, "1000", PayloadElement.SPEED_GUAGE);
        
        setAvailablePayload(-2, "100", PayloadElement.NIGHT_SIGHT);
        setAvailablePayload(-1, "10", PayloadElement.DAY_SIGHT);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
    }

    @Override
    public IPlanePayload copy()
    {
        PfalzD12Payload clone = new PfalzD12Payload(planeType, date);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        return false;
    }
}
