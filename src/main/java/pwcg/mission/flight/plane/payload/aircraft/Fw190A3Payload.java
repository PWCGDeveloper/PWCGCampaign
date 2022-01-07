package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A3Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A3Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "11", PlanePayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PlanePayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PlanePayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PlanePayloadElement.MGFF_WING_GUNS);
        setAvailablePayload(5, "100001", PlanePayloadElement.MGFF_WING_GUNS_EXTRA_AMMO);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A3Payload clone = new Fw190A3Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }
    
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = createStandardPayload();
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
            selectedPayloadId = selectSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = selectArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = selectMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = selectHeavyTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = selectStructureTargetPayload();
        }
        return selectedPayloadId;
    }
    
    private int createStandardPayload()
    {
        return  getPayloadIdByDescription(PlanePayloadElement.STANDARD.getDescription());
    }

    private int selectSoftTargetPayload()
    {
        return 1;
    }    

    private int selectArmoredTargetPayload()
    {
        return 2;
    }

    private int selectMediumTargetPayload()
    {
        return 2;
    }

    private int selectHeavyTargetPayload()
    {
        return 2;
    }

    private int selectStructureTargetPayload()
    {
        return 3;
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
            selectedPayloadId == 4 ||
            selectedPayloadId == 5)
        {
            return false;
        }

        return true;
    }
}
