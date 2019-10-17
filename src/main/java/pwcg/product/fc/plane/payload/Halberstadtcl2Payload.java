package pwcg.product.fc.plane.payload;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class Halberstadtcl2Payload extends PlanePayload implements IPlanePayload
{
    public Halberstadtcl2Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "1000000000", PayloadElement.RADIO);
        setAvailablePayload(-3, "100000000", PayloadElement.CAMERA);
        setAvailablePayload(-2, "100000", PayloadElement.ATTITUDE_GUAGE);
        setAvailablePayload(-1, "100", PayloadElement.TWIN_GUN_TURRET);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "10000001", PayloadElement.BOMBS);
    }

    @Override
    public IPlanePayload copy()
    {
        Halberstadtcl2Payload clone = new Halberstadtcl2Payload(planeType);
        return super.copy(clone);
    }

    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK || flight.getFlightType() == FlightTypes.BOMB)
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

    protected void selectBombingPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectReconPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
    }

    protected void selectArtillerySpotPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 0;
    }    
}
