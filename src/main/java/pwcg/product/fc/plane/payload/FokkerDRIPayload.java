package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class FokkerDRIPayload extends PlanePayload implements IPlanePayload
{
    public FokkerDRIPayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-5, "100000", PayloadElement.AMMO_COUNTER);
        setAvailablePayload(-4, "10000", PayloadElement.ATTITUDE_GUAGE);
        
        setAvailablePayload(-3, "1000", PayloadElement.IRON_SIGHT);
        setAvailablePayload(-2, "100", PayloadElement.NIGHT_SIGHT);
        setAvailablePayload(-1, "10", PayloadElement.DAY_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        
        addStockModifications(PayloadElement.COCKPIT_LIGHT);
        addStockModifications(PayloadElement.AMMO_COUNTER);
        addStockModifications(PayloadElement.ATTITUDE_GUAGE);
    }

    @Override
    public IPlanePayload copy()
    {
        FokkerDRIPayload clone = new FokkerDRIPayload(planeType);
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
