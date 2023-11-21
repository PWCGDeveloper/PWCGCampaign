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
        return 12;
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
