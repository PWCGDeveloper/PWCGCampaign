package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Ju52Payload extends PlanePayload
{
    public Ju52Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(3);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10000", PlanePayloadElement.TURRET);
        setAvailablePayload(0, "11", PlanePayloadElement.CARGO);
        setAvailablePayload(1, "101", PlanePayloadElement.MAB_250);
        setAvailablePayload(2, "1001", PlanePayloadElement.PARATROOPERS);
        setAvailablePayload(3, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju52Payload clone = new Ju52Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.CARGO_DROP)
        {
            selectedPayloadId = selectCargoPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.PARATROOP_DROP)
        {
            selectedPayloadId = selectParatroopPayload();
        }
        else if ((FlightTypes.isBombingFlight(flight.getFlightType())))
        {
            selectedPayloadId = selectBombPayload();
        }
        return selectedPayloadId;
    }    

    private int selectCargoPayload(IFlight flight)
    {
        return 0;
    }

    private int selectBombPayload()
    {
        return 1;
    }    

    private int selectParatroopPayload()
    {
        return 2;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0 || 
            selectedPayloadId == 3)
        {
            return false;
        }

        return true;
    }
}
