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

public class MosquitoFBMkVIS2Payload extends PlanePayload implements IPlanePayload
{
    private Date smallBombIntroDate;
    private Date rp3IntroDate;
    private Date gyroGunsightIntroDate;
    private Date highOctaneFuelIntroDate;

    public MosquitoFBMkVIS2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            smallBombIntroDate = DateUtils.getDateYYYYMMDD("19440610");
            rp3IntroDate = DateUtils.getDateYYYYMMDD("19440610");
            gyroGunsightIntroDate = DateUtils.getDateYYYYMMDD("19440801");
            highOctaneFuelIntroDate = DateUtils.getDateYYYYMMDD("19440505");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-3, "100000000", PayloadElement.EXHAUST_OPEN);
        setAvailablePayload(-2, "10000000", PayloadElement.LB_25_BOOST);
        setAvailablePayload(-1, "1000000", PayloadElement.GYRO_GUNSIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(2, "1", PayloadElement.SC250_X2);
        setAvailablePayload(6, "1", PayloadElement.SC250_X4);
        setAvailablePayload(8, "1", PayloadElement.SC500_X2);
        setAvailablePayload(12, "1", PayloadElement.SC500_X4);
        
        setAvailablePayload(50, "11", PayloadElement.CANNON_57MM, PayloadElement.MG_4X);
        setAvailablePayload(68, "101", PayloadElement.CANNON_57MM, PayloadElement.MG_2X);
        
        setAvailablePayload(14, "1001", PayloadElement.RP3_HE_X8);
        setAvailablePayload(16, "1001", PayloadElement.RP3_AP_X8);
        setAvailablePayload(18, "1001", PayloadElement.SC250_X2, PayloadElement.RP3_HE_X8);
        setAvailablePayload(20, "1001", PayloadElement.SC250_X2, PayloadElement.RP3_AP_X8);
        
        setAvailablePayload(26, "10001", PayloadElement.RP3_MKIIII_HE_X8);
        setAvailablePayload(28, "10001", PayloadElement.RP3_MKIIII_AP_X8);
        setAvailablePayload(30, "10001", PayloadElement.SC250_X2, PayloadElement.RP3_MKIIII_HE_X8);
        setAvailablePayload(32, "10001", PayloadElement.SC250_X2, PayloadElement.RP3_MKIIII_AP_X8);
        
        setAvailablePayload(38, "100001", PayloadElement.RP3_MKIIIIT_HE_X8);
        setAvailablePayload(40, "100001", PayloadElement.RP3_MKIIIIT_AP_X8);
        setAvailablePayload(42, "100001", PayloadElement.SC250_X2, PayloadElement.RP3_MKIIIIT_HE_X8);
        setAvailablePayload(44, "100001", PayloadElement.SC250_X2, PayloadElement.RP3_MKIIIIT_AP_X8);
	}

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.TRAIN_BUST)
        {
            selectedPayloadId = 20;
        }
        else if (flight.getFlightType() == FlightTypes.ANTI_SHIPPING)
        {
            selectedPayloadId = 8;
        }
        else if (flight.getFlightType() == FlightTypes.RAID)
        {
            selectedPayloadId = 2;
        }
        else if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        
        return selectedPayloadId;
    }

    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 2;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectLightTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload(flight);
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectHeavyTargetPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectLightTargetPayload()
    {
        int selectedPayloadId = 1;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPayloadId =  1;
        }
        else if (diceRoll < 70)
        {
            selectedPayloadId =  14;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId =  26;
        }
        else
        {
            selectedPayloadId =  38;
        }
        return selectedPayloadId;
    }

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 16;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 20)
        {
            selectedPayloadId =  8;
        }
        else if (diceRoll < 70)
        {
            selectedPayloadId =  16;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId =  28;
        }
        else
        {
            selectedPayloadId =  40;
        }
        return selectedPayloadId;
    }

    private int selectMediumTargetPayload()
    {
        int selectedPayloadId = 8;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 40)
        {
            selectedPayloadId =  8;
        }
        else if (diceRoll < 70)
        {
            selectedPayloadId =  16;
        }
        else if (diceRoll < 90)
        {
            selectedPayloadId =  28;
        }
        else
        {
            selectedPayloadId =  40;
        }
        return selectedPayloadId;
    }

    private int selectHeavyTargetPayload(IFlight flight)
    {
        int selectedPayloadId = 12;
        if (flight.getFlightType() == FlightTypes.RAID) 
        {
            selectedPayloadId = 8;
        }
        return selectedPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
    	MosquitoFBMkVIS2Payload clone = new MosquitoFBMkVIS2Payload(getPlaneType(), getDate());
        
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
        registerStockModification(PayloadElement.MIRROR);        
        if (getDate().after(gyroGunsightIntroDate))
        {
            registerStockModification(PayloadElement.GYRO_GUNSIGHT);
        }
        
        if (getDate().after(highOctaneFuelIntroDate))
        {
            registerStockModification(PayloadElement.OCTANE_150_FUEL);
        }
    }
    
    @Override
    protected List<PayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1);
        availablePayloads.addAll(alwaysAvailablePayloads);
        
        if (getDate().after(smallBombIntroDate))
        {
            availablePayloads.add(2);
        }
        
        if (getDate().after(rp3IntroDate))
        {
            availablePayloads.add(4);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
