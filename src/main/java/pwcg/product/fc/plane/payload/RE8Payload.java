package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class RE8Payload extends PlanePayload implements IPlanePayload
{
    public RE8Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-5, "10000000", PayloadElement.RADIO);
        setAvailablePayload(-4, "1000000", PayloadElement.CAMERA);
        setAvailablePayload(-3, "10000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "1000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.TWIN_GUN_TURRET);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.LEWIS_WING);
        setAvailablePayload(2, "100001", PayloadElement.BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        RE8Payload clone = new RE8Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isBombingFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            selectedPayloadId = selectReconPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPayloadId = selectArtillerySpotPayload(flight);
        }
        return selectedPayloadId;
    }

    protected int selectPayload(IFlight flight)
    {
        return 1;
    }

    protected int selectReconPayload(IFlight flight)
    {
        this.selectModification(PayloadElement.CAMERA);
        return 0;
    }

    protected int selectArtillerySpotPayload(IFlight flight)
    {
        this.selectModification(PayloadElement.RADIO);
        return 0;
    }

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId > 0)
        {
            return true;
        }

        return false;
    }
}
