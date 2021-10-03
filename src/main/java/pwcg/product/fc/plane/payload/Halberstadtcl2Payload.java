package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Halberstadtcl2Payload extends PlanePayload implements IPlanePayload
{
    public Halberstadtcl2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-5, "100000", PayloadElement.ATTITUDE_GUAGE);
        
        setAvailablePayload(-4, "10000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-3, "1000", PayloadElement.BECKER_GUN_TURRET);
        setAvailablePayload(-2, "100", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(-1, "10", PayloadElement.TWIN_FRONT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10000001", PayloadElement.BOMBS);
        setAvailablePayload(2, "100000001", PayloadElement.CAMERA);
        setAvailablePayload(3, "1000000001", PayloadElement.RADIO);
    }

    @Override
    public IPlanePayload copy()
    {
        Halberstadtcl2auPayload clone = new Halberstadtcl2auPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }

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
        return 2;
    }

    protected int selectArtillerySpotPayload(IFlight flight)
    {
        return 3;
    }    

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 1)
        {
            return true;
        }

        return false;
    }
}
