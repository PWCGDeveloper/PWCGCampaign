package pwcg.mission.flight.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A8Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{        
        setAvailablePayload(-2, "1000000", PlanePayloadElement.FW190A8_REM_GUNS);
        setAvailablePayload(-1, "10000", PlanePayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PlanePayloadElement.SC500_X1);
        setAvailablePayload(4, "1001", PlanePayloadElement.BR21_X2);
        setAvailablePayload(16, "11", PlanePayloadElement.MK108_30);
        
        setAvailablePayload(32, "100001", PlanePayloadElement.FW190F8);
        setAvailablePayload(34, "100001", PlanePayloadElement.FW190F8, PlanePayloadElement.FW190F8_SC70_X4);
        setAvailablePayload(36, "100001", PlanePayloadElement.FW190F8, PlanePayloadElement.FW190F8_PB1_X12);
        setAvailablePayload(37, "100001", PlanePayloadElement.FW190F8, PlanePayloadElement.FW190F8_PB1M8_X12);        
	}

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
    }    

    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        return selectedPayloadId;
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
            selectedPayloadId == 4 ||
            selectedPayloadId == 16)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight) throws PWCGException
    {
        List<Integer>availablePayloads = new ArrayList<>();
        availablePayloads.add(0);
         
        List<Integer>availableFighterPayloads = Arrays.asList(1, 2, 3, 4, 16);
        availablePayloads.addAll(availableFighterPayloads);

        return getAvailablePayloadDesignations(availablePayloads);
    }
}
