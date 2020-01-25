package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class P47D28Payload extends PlanePayload implements IPlanePayload
{
    public P47D28Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-3, "10000000", PayloadElement.MIRROR);
        setAvailablePayload(-2, "1000000", PayloadElement.MN28);
        setAvailablePayload(-1, "100000", PayloadElement.P47_GUNSIGHT);
                
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.MG50CAL_6x);
        setAvailablePayload(2, "101", PayloadElement.MG50CAL_4x);
        setAvailablePayload(3, "1001", PayloadElement.ADDITIONAL_AMMO);
        
        setAvailablePayload(6, "10001", PayloadElement.M64_X1);
        setAvailablePayload(12, "10001", PayloadElement.M64_X2);
        setAvailablePayload(18, "10001", PayloadElement.M64_X3);
        setAvailablePayload(24, "10001", PayloadElement.M65_X2);
        setAvailablePayload(36, "10001", PayloadElement.M8_X6);
        
        setAvailablePayload(30, "10001", PayloadElement.M65_X2, PayloadElement.M64_X1);
        setAvailablePayload(42, "10001", PayloadElement.M64_X1, PayloadElement.M8_X6);
        setAvailablePayload(46, "10001", PayloadElement.M64_X2, PayloadElement.M8_X6);
        setAvailablePayload(54, "10001", PayloadElement.M64_X3, PayloadElement.M8_X6);
        setAvailablePayload(60, "10001", PayloadElement.M65_X2, PayloadElement.M8_X6);
        setAvailablePayload(66, "10001", PayloadElement.M65_X2, PayloadElement.M64_X1, PayloadElement.M8_X6);
	}
 
    @Override
    public IPlanePayload copy()
    {
        P47D28Payload clone = new P47D28Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 6;
        if (flight.getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 12;
        }
        else if (flight.getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 36;
        }
        else if (flight.getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 18;
        }
        else if (flight.getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 24;
        }
    }
}
