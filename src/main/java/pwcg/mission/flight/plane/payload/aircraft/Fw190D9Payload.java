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
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190D9Payload extends PlanePayload implements IPlanePayload
{
    private Date r4mIntroDate;
    private Date gyroGunsightIntroDate;

    public Fw190D9Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            r4mIntroDate = DateUtils.getDateYYYYMMDD("19450305");
            gyroGunsightIntroDate = DateUtils.getDateYYYYMMDD("19450208");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{        
        setAvailablePayload(-2, "10000000", PlanePayloadElement.BUBBLE_CANOPY);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.GYRO_GUNSIGHT);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PlanePayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PlanePayloadElement.BR21_X2);
        setAvailablePayload(8, "100001", PlanePayloadElement.R4M_X26);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Fw190D9Payload clone = new Fw190D9Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }
        else 
        {
            createStandardPayload();
        }
        

        return selectedPayloadId;
    }    

    private int createStandardPayload()
    {
        return 0;
   }

	protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 3;
        }
        return selectedPayloadId;
    }

    private int selectInterceptPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPayloadId = 4;
        }
        else if (diceRoll < 60)
        {
            if (getDate().before(r4mIntroDate))
            {
                selectedPayloadId = 0;
            }
            else
            {
                selectedPayloadId = 8;
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
            selectedPayloadId == 4 ||
            selectedPayloadId == 8)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadAvailableStockModifications()
    {
        registerStockModification(PlanePayloadElement.BUBBLE_CANOPY);
        if (getDate().after(gyroGunsightIntroDate))
        {
            registerStockModification(PlanePayloadElement.GYRO_GUNSIGHT);
        }
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 2, 3, 4);
        availablePayloads.addAll(alwaysAvailablePayloads);
        if (getDate().after(r4mIntroDate))
        {
            availablePayloads.add(8);
        }
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
