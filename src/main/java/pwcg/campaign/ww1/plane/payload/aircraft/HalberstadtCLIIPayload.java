package pwcg.campaign.ww1.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;

public class HalberstadtCLIIPayload extends PlanePayload implements IPlanePayload
{
    public HalberstadtCLIIPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
    {
        setAvailablePayload(0, "1", PayloadElement.KG12_X12);
        setAvailablePayload(1, "1", PayloadElement.KG50x1, PayloadElement.KG12_X8);
        setAvailablePayload(2, "1", PayloadElement.KG50x3);
        setAvailablePayload(3, "1", PayloadElement.KG12_X4);
        setAvailablePayload(4, "1", PayloadElement.KG50x1);
        setAvailablePayload(7, "1", PayloadElement.CAMERA);
        setAvailablePayload(8, "1", PayloadElement.RADIO);
        setAvailablePayload(9, "1", PayloadElement.STANDARD);
    }

    @Override
    public int createWeaponsPayload(Flight flight)
    {
        selectedPrimaryPayloadId = 9;
        if (flight.getFlightType() == FlightTypes.RECON || flight.getFlightType() == FlightTypes.CONTACT_PATROL)
        {
            selectedPrimaryPayloadId = 7;
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPrimaryPayloadId = 8;
        }
        else if ((flight.isBombingFlight()))
        {
            selectedPrimaryPayloadId = 4;
        }
        
        return selectedPrimaryPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
        HalberstadtCLIIPayload clone = new HalberstadtCLIIPayload(planeType);
        
        return super.copy(clone);
    }
}
