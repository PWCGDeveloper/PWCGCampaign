package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class BristolF2B3Payload extends PlanePayload implements IPlanePayload
{
    public BristolF2B3Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-5, "100000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-4, "10000", PayloadElement.FUEL_GUAGE);
        
        setAvailablePayload(-3, "1000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-2, "100", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(-1, "10", PayloadElement.LEWIS_TOP);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "1000001", PayloadElement.BOMBS);
        setAvailablePayload(1, "10000001", PayloadElement.CAMERA);
        setAvailablePayload(3, "100000001", PayloadElement.RADIO);
        
        addStockModifications(PayloadElement.COCKPIT_LIGHT);
        addStockModifications(PayloadElement.FUEL_GUAGE);
    }

    @Override
    public IPlanePayload copy()
    {
        BristolF2B2Payload clone = new BristolF2B2Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isBombingFlight(flight.getFlightType()))
        {
            selectBombingPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            selectReconPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectArtillerySpotPayload(flight);
        }
        return selectedPrimaryPayloadId;
    }

    protected void selectBombingPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectReconPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
    }

    protected void selectArtillerySpotPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 3;
    }
}
