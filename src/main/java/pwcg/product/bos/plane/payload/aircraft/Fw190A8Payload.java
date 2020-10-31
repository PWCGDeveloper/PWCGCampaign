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

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A8Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{        
        setAvailablePayload(-2, "1000000", PayloadElement.FW190_REM_GUNS);
        setAvailablePayload(-1, "10000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.SC500_X1);
        setAvailablePayload(4, "1001", PayloadElement.BR21_X2);
        setAvailablePayload(16, "11", PayloadElement.MK108_30);
        setAvailablePayload(32, "100001", PayloadElement.FW190F8);
        setAvailablePayload(34, "100001", PayloadElement.FW190F8_SC70_X4);
        setAvailablePayload(36, "100001", PayloadElement.FW190F8_PB1_X12);
        setAvailablePayload(37, "100001", PayloadElement.FW190F8_PB1M8_X12);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(planeType);
        
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
        else 
        {
            createStandardPayload();
        }
        
        return selectedPrimaryPayloadId;
    }    

    private void createStandardPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 20)
        {
            selectedPrimaryPayloadId = 16;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }

    private void selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        Role squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRole(flight.getCampaign().getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK))
        {
            selectFW190F8GroundAttackPayload(flight);
        }
        else
        {
            selectFW190A8GroundAttackPayload(flight);
        }
    }

    private void selectFW190F8GroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 32;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 37;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 36;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 34;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 34;
        }
    }

    private void selectFW190A8GroundAttackPayload(IFlight flight)
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
    }

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            selectedPrimaryPayloadId = 4;
        }
        else if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 16;
        }
        else
        {
            selectedPrimaryPayloadId = 0;
        }
    }    

}
