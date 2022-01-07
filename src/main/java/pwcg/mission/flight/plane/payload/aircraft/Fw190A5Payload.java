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

public class Fw190A5Payload extends PlanePayload implements IPlanePayload
{
    private Date mkFFWingGunsIntroDate;
    private Date mg151GunPodIntroDate;

    public Fw190A5Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            mkFFWingGunsIntroDate = DateUtils.getDateYYYYMMDD("19430506");
            mg151GunPodIntroDate = DateUtils.getDateYYYYMMDD("19430506");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    protected void initialize()
	{
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PlanePayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PlanePayloadElement.MGFF_WING_GUNS);
        setAvailablePayload(5, "100001", PlanePayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(6, "1000001", PlanePayloadElement.U_17);
        setAvailablePayload(7, "1000011", PlanePayloadElement.SC50_X8);
        setAvailablePayload(8, "1000101", PlanePayloadElement.SC250_X1, PlanePayloadElement.SC50_X4);
        setAvailablePayload(9, "1000101", PlanePayloadElement.SC500_X1, PlanePayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A5Payload clone = new Fw190A5Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = selectDefaultPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }

        return selectedPayloadId;
    }    

	private int selectGroundAttackPayload (IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = selectSoftTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectMediumTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = selectMediumTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = selectHeavyTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = selectHeavyTargetPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectSoftTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 2;
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 3;
        return selectedPayloadId;
    }

    private int selectInterceptPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        if (getDate().before(mkFFWingGunsIntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 4;
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                if (getDate().after(mg151GunPodIntroDate) && getDate().before(DateUtils.getDateYYYYMMDD("19440501")))
                {
                    selectedPayloadId = 5;
                }
            }
        }
        return selectedPayloadId;
    }    

    private int selectDefaultPayload() throws PWCGException
    {
        int selectedPayloadId = 0;
        if (getDate().before(mkFFWingGunsIntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 4;
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
            selectedPayloadId == 5)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight) throws PWCGException
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 2, 3);
        availablePayloads.addAll(alwaysAvailablePayloads);
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
