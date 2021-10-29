package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Breguet14Payload extends PlanePayload implements IPlanePayload
{
    public Breguet14Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-6, "100000", PayloadElement.SIDE_SLIP_GUAGE);
        setAvailablePayload(-6, "10000", PayloadElement.COCKPIT_LIGHT);
        setAvailablePayload(-2, "1000", PayloadElement.LE_CHRETIAN_SIGHT);
        setAvailablePayload(-1, "100", PayloadElement.ALDIS_SIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.LEWIS_TOP);
        setAvailablePayload(2, "1000001", PayloadElement.KG8_X32);
        setAvailablePayload(3, "1000011", PayloadElement.LEWIS_TOP, PayloadElement.KG8_X32);
        setAvailablePayload(4, "1000001", PayloadElement.KG20_X16);
        setAvailablePayload(5, "1000011", PayloadElement.LEWIS_TOP, PayloadElement.KG20_X16);
        setAvailablePayload(6, "1000001", PayloadElement.KG20_X16);
        setAvailablePayload(7, "1000011", PayloadElement.LEWIS_TOP, PayloadElement.KG20_X16);
    }

    @Override
    public IPlanePayload copy()
    {
        AircoDH4Payload clone = new AircoDH4Payload(getPlaneType(), getDate());
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
        int selectedPayloadId = 4;
        
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPayloadId = 2;
        }
        else if (diceRoll < 60)
        {
            selectedPayloadId = 4;
        }
        else if (diceRoll < 80)
        {
            selectedPayloadId = 6;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId = 3;
        }
        else if (diceRoll < 95)
        {
            selectedPayloadId = 5;
        }
        else if (diceRoll < 100)
        {
            selectedPayloadId = 7;
        }
        
        return selectedPayloadId;
    }

    protected int selectReconPayload(IFlight flight)
    {
        return selectNonOrdnancePayload();
    }

    protected int selectArtillerySpotPayload(IFlight flight)
    {
        return selectNonOrdnancePayload();
    }

    protected int selectNonOrdnancePayload()
    {
        int selectedPayloadId = 1;
        
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 85)
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 1;
        }

        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId > 1)
        {
            return true;
        }

        return false;
    }
}
