package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Pe2S87Payload extends PlanePayload implements IPlanePayload
{
    public Pe2S87Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 11;
    }

    protected void initialize()
	{
	    
        setAvailablePayload(-1, "100000", PayloadElement.TURRET);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.FAB100M_X4);
        setAvailablePayload(2, "1", PayloadElement.FAB100M_X6);
        setAvailablePayload(3, "1", PayloadElement.FAB250SV_X2);
        setAvailablePayload(4, "11", PayloadElement.FAB100M_X10);
        setAvailablePayload(5, "101", PayloadElement.FAB250SV_X4);
        setAvailablePayload(6, "1001", PayloadElement.FAB500M_X2);
        setAvailablePayload(7, "10001", PayloadElement.ROS132_X10);
        setAvailablePayload(8, "10001", PayloadElement.FAB100M_X4, PayloadElement.ROS132_X10);
        setAvailablePayload(9, "10001", PayloadElement.FAB100M_X6, PayloadElement.ROS132_X10);
        setAvailablePayload(10, "10001", PayloadElement.FAB250SV_X2, PayloadElement.ROS132_X10);
        setAvailablePayload(11, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Pe2S87Payload clone = new Pe2S87Payload(planeType, date);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            selectDiveBombPayload(flight);
        }
    	else
    	{
    		selectBombingPayload(flight);
    	}
        return selectedPrimaryPayloadId;
    }

    private void selectBombingPayload(IFlight flight)
    {
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 6;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 7;
    }

    private void selectDiveBombPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 3;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 11)
        {
            return false;
        }

        return true;
    }
}
