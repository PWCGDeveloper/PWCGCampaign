package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
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

    public SpitfireMkIXePayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
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
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }
        
        return selectedPrimaryPayloadId;
    }

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectLightTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectLightTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 1;
        }
    }

    private void selectLightTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
        if (date.before(smallBombIntroDate))
        {
            selectedPrimaryPayloadId = 2;
        }
    }

    private void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
        if (date.before(rp3IntroDate))
        {
            selectedPrimaryPayloadId = 4;
        }
    }

    @Override
    public IPlanePayload copy()
    {
    	SpitfireMkIXePayload clone = new SpitfireMkIXePayload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadStockModifications()
    {
        stockModifications.add(PayloadElement.MIRROR);        
        if (date.after(gyroGunsightIntroDate))
        {
            stockModifications.add(PayloadElement.GYRO_GUNSIGHT);
        }
        
        if (date.after(highOctaneFuelIntroDate))
        {
            stockModifications.add(PayloadElement.OCTANE_150_FUEL);
        }
    }
}
