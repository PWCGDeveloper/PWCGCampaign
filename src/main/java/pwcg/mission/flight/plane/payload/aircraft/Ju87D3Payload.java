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
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ju87D3Payload extends PlanePayload
{    
    private Date bk37IntroDate;

    public Ju87D3Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            bk37IntroDate = DateUtils.getDateYYYYMMDD("19430302");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-2, "1000", PlanePayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-1, "10", PlanePayloadElement.SIREN);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
		setAvailablePayload(1, "1", PlanePayloadElement.SC250_X1, PlanePayloadElement.SD70_X4);
        setAvailablePayload(2, "1", PlanePayloadElement.SC500_X1);
        setAvailablePayload(3, "1", PlanePayloadElement.SC500_X1, PlanePayloadElement.SD70_X4);
        setAvailablePayload(4, "1", PlanePayloadElement.SC500_X1, PlanePayloadElement.SC250_X2);
		setAvailablePayload(5, "1", PlanePayloadElement.SC250_X3);
		setAvailablePayload(6, "1", PlanePayloadElement.SC1000_X1);
		setAvailablePayload(7, "101", PlanePayloadElement.SC1800_X1);
		setAvailablePayload(9, "100001", PlanePayloadElement.BK37_AP_GUNPOD);
		setAvailablePayload(10, "100001", PlanePayloadElement.BK37_HE_GUNPOD);
        setAvailablePayload(11, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju87D3Payload clone = new Ju87D3Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            selectedPayloadId = selectDiveBombPayload(flight);
        }
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        return selectedPayloadId;
    }    

    private int selectDiveBombPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 6;
        }
        return selectedPayloadId;
    }

    private int selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (getDate().before(bk37IntroDate))
        {
            selectedPayloadId = selectDiveBombPayload(flight);
        }
        else
        {
            if (flight.getTargetDefinition().getTargetCategory() != TargetCategory.TARGET_CATEGORY_STRUCTURE)
            {
                selectedPayloadId = 9;
            }
            else
            {
                selectedPayloadId = 6;
            }
        }
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
            selectedPayloadId == 9 || 
            selectedPayloadId == 10)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>bk37Payloads = Arrays.asList(9, 10);

        for (int i = 0; i < 8; ++i)
        {
            if (!bk37Payloads.contains(i))
            {
                availablePayloads.add(i);
            }
        }

        if (getDate().after(bk37IntroDate))
        {
            availablePayloads.addAll(bk37Payloads);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
