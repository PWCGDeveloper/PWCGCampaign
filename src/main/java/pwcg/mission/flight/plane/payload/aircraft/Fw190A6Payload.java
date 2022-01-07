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

public class Fw190A6Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
    }    

    protected void initialize()
	{
        setAvailablePayload(-1, "10000000", PlanePayloadElement.SET_MG17);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "10001", PlanePayloadElement.SC70_X4);
        setAvailablePayload(2, "10001", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "10001", PlanePayloadElement.SC500_X1);
        setAvailablePayload(4, "100001", PlanePayloadElement.BR21_X2);
        setAvailablePayload(8, "1000001", PlanePayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(9, "1000001", PlanePayloadElement.SC70_X4, PlanePayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(10, "1000001", PlanePayloadElement.SC250_X1, PlanePayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(11, "1000001", PlanePayloadElement.SC500_X1, PlanePayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(16, "11", PlanePayloadElement.FW190A6_STURMJAGER);

        setAvailablePayload(48, "101", PlanePayloadElement.FW190G3);
        setAvailablePayload(49, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC70_X4);
        setAvailablePayload(50, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC250_X1);
        setAvailablePayload(51, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC250_X2);
        setAvailablePayload(52, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC250_X3);
        setAvailablePayload(53, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC250_X2, PlanePayloadElement.SC70_X4);
        setAvailablePayload(54, "10101", PlanePayloadElement.FW190G3, PlanePayloadElement.SC500_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A6Payload clone = new Fw190A6Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        return Fw190A6PayloadHelper.createFW190A6Payload(flight);
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
            selectedPayloadId == 8 ||
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
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 8);
        availablePayloads.addAll(alwaysAvailablePayloads);
        
        List<Integer>availableFighterPayloads = Arrays.asList(1, 2, 3, 4, 16);
        availablePayloads.addAll(availableFighterPayloads);
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
