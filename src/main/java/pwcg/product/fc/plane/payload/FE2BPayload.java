package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class FE2BPayload extends PlanePayload implements IPlanePayload
{
    public FE2BPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "10000000", PayloadElement.RADIO);
        setAvailablePayload(-5, "1000000", PayloadElement.CAMERA);
        
        setAvailablePayload(-4, "10000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-3, "1000", PayloadElement.CLOCK_GUAGE);
        setAvailablePayload(-2, "100", PayloadElement.FUEL_GUAGE);
        setAvailablePayload(-1, "10", PayloadElement.TWIN_GUN_TURRET);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10000001", PayloadElement.COOPER_BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        FE2BPayload clone = new FE2BPayload(getPlaneType(), getDate());
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
