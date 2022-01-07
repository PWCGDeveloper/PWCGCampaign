package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P51B5Payload extends PlanePayload implements IPlanePayload
{
    public P51B5Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-5, "100000000", PlanePayloadElement.MIRROR);
        setAvailablePayload(-4, "10000000", PlanePayloadElement.MALCOLM_CANOPY);
        setAvailablePayload(-3, "100000", PlanePayloadElement.OCTANE_150_FUEL);
        setAvailablePayload(-2, "10000", PlanePayloadElement.PACKARD_ENGINE);
        setAvailablePayload(-1, "1000", PlanePayloadElement.P51_GUNSIGHT);
	    
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.M64_X2);
        setAvailablePayload(2, "11", PlanePayloadElement.M65_X2);
        setAvailablePayload(3, "11", PlanePayloadElement.LB500x2);
        setAvailablePayload(4, "11", PlanePayloadElement.LB1000x2);
        setAvailablePayload(5, "11", PlanePayloadElement.M8_X6);
        setAvailablePayload(6, "11", PlanePayloadElement.M64_X2, PlanePayloadElement.M8_X6);
        
        
        setAvailablePayload(4, "1001", PlanePayloadElement.M64_X2);
        setAvailablePayload(8, "10001", PlanePayloadElement.M65_X2);
        setAvailablePayload(12, "100001", PlanePayloadElement.P51_ROCKETS);

	}
 
    @Override
    public IPlanePayload copy()
    {
        P51B5Payload clone = new P51B5Payload(getPlaneType(), getDate());
        
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
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 5;
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
}
