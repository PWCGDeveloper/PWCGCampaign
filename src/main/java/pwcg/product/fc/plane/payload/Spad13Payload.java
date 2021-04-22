package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.IFlight;

public class Spad13Payload extends PlanePayload implements IPlanePayload
{
    public Spad13Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-3, "10000", PayloadElement.COCKPIT_LIGHT);
        
        setAvailablePayload(-2, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "11", PayloadElement.BALLOON_GUN);
        setAvailablePayload(3, "100001", PayloadElement.BOMBS);
        
        addStockModifications(PayloadElement.COCKPIT_LIGHT);
    }

    @Override
    public IPlanePayload copy()
    {
        Spad13Payload clone = new Spad13Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        return selectedPrimaryPayloadId;
    }
}
