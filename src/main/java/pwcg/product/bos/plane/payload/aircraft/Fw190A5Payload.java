package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A5Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A5Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PayloadElement.MGFF_WING_GUNS);
        setAvailablePayload(5, "100001", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(6, "1000001", PayloadElement.U_17);
        setAvailablePayload(7, "1000011", PayloadElement.SC50_X8);
        setAvailablePayload(8, "1000101", PayloadElement.SC250_X1, PayloadElement.SC50_X4);
        setAvailablePayload(9, "1000101", PayloadElement.SC500_X1, PayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A5Payload clone = new Fw190A5Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

	private void selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        Role squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRole(flight.getCampaign().getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK))
        {
            selectedPrimaryPayloadId = 6;
        }
        else
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
                selectedPrimaryPayloadId = 2;
            }
            else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
            {
                selectedPrimaryPayloadId = 2;
            }
            else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
            {
                selectedPrimaryPayloadId = 3;
            }
        }
    }

    private void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 5;
        }
    }    
}
