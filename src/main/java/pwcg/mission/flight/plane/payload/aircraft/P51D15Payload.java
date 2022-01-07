package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P51D15Payload extends PlanePayload implements IPlanePayload
{
    public P51D15Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-4, "1000000000", PlanePayloadElement.MIRROR);
        setAvailablePayload(-3, "100000000", PlanePayloadElement.MN28);
        setAvailablePayload(-2, "10000000", PlanePayloadElement.OCTANE_150_FUEL);
	    setAvailablePayload(-1, "1000000", PlanePayloadElement.P51_GUNSIGHT);
	    
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.MG50CAL_4x);
        setAvailablePayload(2, "101", PlanePayloadElement.ADDITIONAL_AMMO);
        setAvailablePayload(4, "1001", PlanePayloadElement.M64_X2);
        setAvailablePayload(8, "10001", PlanePayloadElement.M65_X2);
        setAvailablePayload(12, "100001", PlanePayloadElement.P51_ROCKETS);

	}
 
    @Override
    public IPlanePayload copy()
    {
        P51D15Payload clone = new P51D15Payload(getPlaneType(), getDate());
        
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
        int selectedPayloadId = 4;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 12;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 8;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 8;
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
        if (selectedPayloadId <= 2)
        {
            return false;
        }

        return true;
    }
}
