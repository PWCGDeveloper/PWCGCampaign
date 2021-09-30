package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class AlbatrosD5Payload extends PlanePayload implements IPlanePayload
{
    public AlbatrosD5Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-8, "100000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-7, "10000000", PayloadElement.TEMPERATURE_GUAGE);
        setAvailablePayload(-6, "1000000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-5, "100000", PayloadElement.ATTITUDE_GUAGE);
        setAvailablePayload(-4, "10000", PayloadElement.ALTITUDE_GUAGE);
        
        setAvailablePayload(-3, "1000", PayloadElement.IRON_SIGHT);
        setAvailablePayload(-2, "100", PayloadElement.NIGHT_SIGHT);
        setAvailablePayload(-1, "10", PayloadElement.DAY_SIGHT);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "1000000001", PayloadElement.LEWIS_TOP);
    }

    @Override
    public IPlanePayload copy()
    {
        AlbatrosD5Payload clone = new AlbatrosD5Payload(planeType, date);
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
