package pwcg.mission.flight;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class PlayerFlightTypeBoSInterceptTest 
{    
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JV44_PROFILE);
    }

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        
        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        if (playerFlight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            playerFlight = null;
        }
	
        assert (playerFlight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT);

        List<IFlight> randomAiFlights = mission.getMissionFlights().getAiFlights();
        assert (randomAiFlights.isEmpty());

        List<IFlight> linkedAiFlights = playerFlight.getLinkedFlights().getLinkedFlights();
        assert (!linkedAiFlights.isEmpty());

        boolean escortFound = false;
        boolean bombersFound = false;
        for (IFlight aiFlight : linkedAiFlights)
        {
            if (aiFlight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
            {
                bombersFound = true;
            }
            if (aiFlight.getFlightType() == FlightTypes.ESCORT)
            {
                escortFound = true;
            }
        }

        assert (bombersFound);
        assert (escortFound);

	}
}
