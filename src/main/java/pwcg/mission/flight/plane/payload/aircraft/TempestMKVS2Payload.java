package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class TempestMKVS2Payload extends PlanePayload implements IPlanePayload
{
    private Date boostIntroDate;

    public TempestMKVS2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            boostIntroDate = DateUtils.getDateYYYYMMDD("19440610");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-1, "1000", PlanePayloadElement.LB_11_BOOST);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.LB500x2);
        setAvailablePayload(2, "101", PlanePayloadElement.LB1000x2);
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

    private int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 1;
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

    @Override
    public IPlanePayload copy()
    {
    	TempestMKVS2Payload clone = new TempestMKVS2Payload(getPlaneType(), getDate());
        
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
            registerStockModification(PlanePayloadElement.LB_11_BOOST);
        }
    }
}
