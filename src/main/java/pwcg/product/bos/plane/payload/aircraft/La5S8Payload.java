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

public class La5S8Payload extends PlanePayload implements IPlanePayload
{
    private Date m82FIntroDate;

    public La5S8Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            m82FIntroDate = DateUtils.getDateYYYYMMDD("19421212");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-4, "1000", PayloadElement.RPK10);
        setAvailablePayload(-3, "10000", PayloadElement.WINDSCREEN);
        
        setAvailablePayload(-2, "100000", PayloadElement.LA5_AMMO);
        setAvailablePayload(-1, "1000000", PayloadElement.M82F_ENGINE);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.FAB50SV_X2);
        setAvailablePayload(2, "101", PayloadElement.FAB100M_X2);
	}

    @Override
    public IPlanePayload copy()
    {
        La5S8Payload clone = new La5S8Payload(planeType, date);
        
        return super.copy(clone);
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

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 2;
        }
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
        stockModifications.add(PayloadElement.RPK10);
        stockModifications.add(PayloadElement.WINDSCREEN);
        if (date.after(m82FIntroDate))
        {
            stockModifications.add(PayloadElement.M82F_ENGINE);
        }
    }
}
