package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class SpitfireMkXIVPayload extends PlanePayload implements IPlanePayload
{
    public SpitfireMkXIVPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(-4, "100000000", PlanePayloadElement.OCTANE_150_FUEL);
        setAvailablePayload(-3, "10000000", PlanePayloadElement.GYRO_GUNSIGHT);
        setAvailablePayload(-2, "100000", PlanePayloadElement.MIRROR);
        
        setAvailablePayload(-1, "1000", PlanePayloadElement.SPITFIRE_XIV_E_TYPE_WINGS);

        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.SC500_X1);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X2);
        setAvailablePayload(3, "111", PlanePayloadElement.SC500_X1, PlanePayloadElement.SC250_X2);
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
        int selectedPayloadId = 2;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
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
    
    @Override
    public IPlanePayload copy()
    {
    	SpitfireMkXIVPayload clone = new SpitfireMkXIVPayload(getPlaneType(), getDate());
        
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
}
