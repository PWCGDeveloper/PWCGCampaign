package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public abstract class Bf109Payload extends PlanePayload
{
    
    public Bf109Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
    }

    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
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

    protected int createStandardPayload() throws PWCGException
    {
        return getPayloadIdByDescription(PlanePayloadElement.STANDARD.getDescription());
    }

    protected int selectSoftTargetPayload()
    {
        return 1;
    }

    protected int selectArmoredTargetPayload()
    {
        return 2;
    }

    protected int selectMediumTargetPayload()
    {
        return 2;
    }

    protected int selectHeavyTargetPayload()
    {
        return 2;
    }

    protected int selectStructureTargetPayload()
    {
        return 2;
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (this.getSelectedPayload() == 1 || this.getSelectedPayload() == 2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
