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
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class TyphoonMkIbPayload extends PlanePayload implements IPlanePayload
{
    private Date mkIIIRocketsIntroDate;
    private Date doubleRocketsIntroDate;
    private Date boostIntroDate;

    public TyphoonMkIbPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            doubleRocketsIntroDate = DateUtils.getDateYYYYMMDD("19440901");
            mkIIIRocketsIntroDate = DateUtils.getDateYYYYMMDD("19441216");
            boostIntroDate = DateUtils.getDateYYYYMMDD("19440628");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-5, "100000000", PlanePayloadElement.DUST_DEFLECTOR);
        setAvailablePayload(-4, "10000000", PlanePayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-3, "1000000", PlanePayloadElement.LB_11_BOOST);
        setAvailablePayload(-2, "100000", PlanePayloadElement.FOUR_PLADE_PROP);
        setAvailablePayload(-1, "10000", PlanePayloadElement.REFLECTOR_GUNSIGHT);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1", PlanePayloadElement.LB500x2);
        setAvailablePayload(2, "1", PlanePayloadElement.LB1000x2);
        setAvailablePayload(3, "11", PlanePayloadElement.MKI_HE_ROCKETS_X8);
        setAvailablePayload(4, "11", PlanePayloadElement.MKI_AP_ROCKETS_X8);
        setAvailablePayload(5, "101", PlanePayloadElement.MKI_ROCKETS_DOUBLE);
        setAvailablePayload(6, "1001", PlanePayloadElement.MKIII_HE_ROCKETS_X8);
        setAvailablePayload(7, "1001", PlanePayloadElement.MKIII_AP_ROCKETS_X8);
	}

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        
        return selectedPayloadId;
    }

    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmorAttackPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
    }

    private int selectArmorAttackPayload()
    {
        int selectedPayloadId = 4;
        if (getDate().after(doubleRocketsIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 5;
            }
        }

        if (getDate().after(mkIIIRocketsIntroDate))
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPayloadId = 7;
            }
        }
        return selectedPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
    	TyphoonMkIbPayload clone = new TyphoonMkIbPayload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadAvailableStockModifications()
    {
        registerStockModification(PlanePayloadElement.DUST_DEFLECTOR);
        registerStockModification(PlanePayloadElement.FOUR_PLADE_PROP);
        if (getDate().after(boostIntroDate))
        {
            registerStockModification(PlanePayloadElement.LB_11_BOOST);
        }
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 2, 3, 4);
        availablePayloads.addAll(alwaysAvailablePayloads);

        if (getDate().after(doubleRocketsIntroDate))
        {
            availablePayloads.add(5);
        }

        if (getDate().after(mkIIIRocketsIntroDate))
        {
            availablePayloads.add(6);
            availablePayloads.add(7);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
