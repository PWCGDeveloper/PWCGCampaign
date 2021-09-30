package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class AircoDH4Payload extends PlanePayload implements IPlanePayload
{
    public AircoDH4Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
    {
        setAvailablePayload(-8, "1000000000", PayloadElement.RADIO);
        setAvailablePayload(-7, "100000000", PayloadElement.CAMERA);

        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-5, "100000", PayloadElement.FUEL_GUAGE);
        
        setAvailablePayload(-4, "10000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-3, "1000", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(-2, "100", PayloadElement.LEWIS_TOP);
        setAvailablePayload(-1, "10", PayloadElement.TWIN_FRONT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(4, "10000001", PayloadElement.COOPER_BOMBS);
        setAvailablePayload(8, "10000001", PayloadElement.COOPER_BOMBS, PayloadElement.LB112_X2);
        setAvailablePayload(12, "10000001", PayloadElement.LB112_X2);
        setAvailablePayload(16, "10000001", PayloadElement.LB112_X4);
        setAvailablePayload(20, "10000001", PayloadElement.LB230_X2);
    }

    @Override
    public IPlanePayload copy()
    {
        AircoDH4Payload clone = new AircoDH4Payload(planeType, date);
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
        selectedPrimaryPayloadId = 12;
    }

    protected void selectReconPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        addModification(PayloadElement.CAMERA);
    }

    protected void selectArtillerySpotPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        addModification(PayloadElement.RADIO);
    }

    @Override
    public boolean isOrdnance()
    {
        if (selectedPrimaryPayloadId > 0)
        {
            return true;
        }

        return false;
    }
}
