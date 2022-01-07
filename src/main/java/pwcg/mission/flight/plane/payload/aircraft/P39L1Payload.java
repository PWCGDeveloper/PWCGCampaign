package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P39L1Payload extends PlanePayload implements IPlanePayload
{
    public P39L1Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PlanePayloadElement.P3937MM_AP);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.RPK10);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(2, "1001", PlanePayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(4, "10001", PlanePayloadElement.REM_M230);
        setAvailablePayload(6, "11", PlanePayloadElement.FAB100M_X1);
        setAvailablePayload(12, "101", PlanePayloadElement.FAB250SV_X1);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P39L1Payload clone = new P39L1Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
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
        int selectedPayloadId = 6;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 12;
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
            selectedPayloadId == 2 || 
            selectedPayloadId == 4)
        {
            return false;
        }

        return true;
    }
}
