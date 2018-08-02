package pwcg.mission.flight;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetCategory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.divebomb.DiveBombingFlight;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheBoS;

public class PlayerFlightTypeBoSDiveBombTest
{
    Mission mission;
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(CampaignCacheBoS.STG77_PROFILE);
    }

    @Test
    public void diveBombFlightTest() throws PWCGException
    {
        mission = new Mission();
        mission.initialize(campaign);
        mission.generate(FlightTypes.DIVE_BOMB);
        DiveBombingFlight flight = (DiveBombingFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
        groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert (flight.getFlightType() == FlightTypes.DIVE_BOMB);
    }

    public void validateTargetDefinition(TargetDefinition targetDefinition)
    {
        assert (targetDefinition.getAttackingCountry() != null);
        assert (targetDefinition.getTargetCountry() != null);
        assert (targetDefinition.getTargetGeneralPosition() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TacticalTarget.TARGET_NONE);
    }
}
