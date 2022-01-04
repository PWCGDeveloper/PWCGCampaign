package pwcg.product.bos.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.tank.TankType;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class SpitfireMkIXePayload extends PlanePayload implements IPlanePayload
{
    private Date smallBombIntroDate;
    private Date rp3IntroDate;
    private Date gyroGunsightIntroDate;
    private Date highOctaneFuelIntroDate;

    public SpitfireMkIXePayload(TankType planeType, Date date)
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
        setAvailablePayload(-5, "100000000", PayloadElement.OCTANE_150_FUEL);
        setAvailablePayload(-4, "10000000", PayloadElement.MERLIN_70_ENGINE);
        setAvailablePayload(-3, "1000000", PayloadElement.SPITFIRE_CLIPPED_WINGS);
        setAvailablePayload(-2, "100000", PayloadElement.MIRROR);
        setAvailablePayload(-1, "10000", PayloadElement.GYRO_GUNSIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.SC500_X1);
        setAvailablePayload(2, "101", PayloadElement.SC250_X2);
        setAvailablePayload(4, "1001", PayloadElement.RP3_X2);
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
            selectLightTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectLightTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 1;
        }
        return selectedPayloadId;
    }

    private int selectLightTargetPayload()
    {
        int selectedPayloadId = 1;
        if (getDate().after(smallBombIntroDate))
        {
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
    }

    private int selectArmoredTargetPayload()
    {
        int selectedPayloadId = 1;
        if (getDate().after(rp3IntroDate))
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }

    @Override
    public IPlanePayload copy()
    {
    	SpitfireMkIXePayload clone = new SpitfireMkIXePayload(getTankType(), getDate());
        
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
