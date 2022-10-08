package pwcg.product.bos.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
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
    private Date fourBladePropDate;
    private Date dustDeflectorDate;

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
            fourBladePropDate = DateUtils.getDateYYYYMMDD("19440601");
            dustDeflectorDate = DateUtils.getDateYYYYMMDD("19440601");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-5, "100000000", PayloadElement.DUST_DEFLECTOR);
        setAvailablePayload(-4, "10000000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-3, "1000000", PayloadElement.LB_11_BOOST);
        setAvailablePayload(-2, "100000", PayloadElement.FOUR_PLADE_PROP);
        setAvailablePayload(-1, "10000", PayloadElement.REFLECTOR_GUNSIGHT);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.LB500x2);
        setAvailablePayload(2, "1", PayloadElement.LB1000x2);
        setAvailablePayload(3, "11", PayloadElement.MKI_HE_ROCKETS_X8);
        setAvailablePayload(4, "11", PayloadElement.MKI_AP_ROCKETS_X8);
        setAvailablePayload(5, "101", PayloadElement.MKI_ROCKETS_DOUBLE);
        setAvailablePayload(6, "1001", PayloadElement.MKIII_HE_ROCKETS_X8);
        setAvailablePayload(7, "1001", PayloadElement.MKIII_AP_ROCKETS_X8);
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
        if (getDate().after(boostIntroDate))
        {
            registerStockModification(PayloadElement.LB_11_BOOST);
        }
        
        if (getDate().after(dustDeflectorDate))
        {
            registerStockModification(PayloadElement.DUST_DEFLECTOR);
        }
    
        if (getDate().after(fourBladePropDate))
        {
            registerStockModification(PayloadElement.FOUR_PLADE_PROP);
        }
    }
    
    @Override
    protected List<PayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
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
