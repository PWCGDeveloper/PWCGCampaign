package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A6PayloadHelper
{
    public static int createFW190A6Payload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPrimaryPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectedPrimaryPayloadId = selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    private static int selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            return 0;
        }
        else
        {
            return 4;
        }
    }    

	private static int selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        int selectedPrimaryPayloadId = 6;

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
                selectedPrimaryPayloadId = 3;
            }
            else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
            {
                selectedPrimaryPayloadId = 3;
            }
        }
        return selectedPrimaryPayloadId;
    }
}
