package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.context.PWCGFront;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Bf109G6ASPayload extends Bf109Payload implements IPlanePayload
{
    public Bf109G6ASPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "1000000", PayloadElement.MW50);
        setAvailablePayload(-1, "10000", PayloadElement.ASM);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.SC250_X1);
        setAvailablePayload(2, "101", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(3, "10001", PayloadElement.BR21_X2);
        setAvailablePayload(7, "11", PayloadElement.MK108_30);
        setAvailablePayload(8, "1011", PayloadElement.SC250_X1, PayloadElement.MK108_30);
        setAvailablePayload(9, "111", PayloadElement.MG151_20_GUNPOD, PayloadElement.MK108_30);
        setAvailablePayload(10, "10011", PayloadElement.BR21_X2, PayloadElement.MK108_30);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109G6ASPayload clone = new Bf109G6ASPayload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload109AS(flight);
        }
        else
        {
            createStandardPayload();
        }

        return selectedPayloadId;
    }    
    
    private int selectGroundAttackPayload109AS(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (flight.getCampaign().getCampaignMap().getFront() == PWCGFront.WWII_WESTERN_FRONT)
        {
            selectedPayloadId = 8;
        }
        return selectedPayloadId;
    }
    
    protected int createStandardPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (flight.getCampaign().getCampaignMap().getFront() == PWCGFront.WWII_WESTERN_FRONT)
        {
            selectedPayloadId = 7;
        }
        else
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 70)
            {
                selectedPayloadId = 0;
            }
            else
            {
                selectedPayloadId = 7;
            }
        }
        return selectedPayloadId;
    }    
}
