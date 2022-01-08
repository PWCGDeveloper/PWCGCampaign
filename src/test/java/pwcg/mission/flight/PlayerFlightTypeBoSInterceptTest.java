package pwcg.mission.flight;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightTypeBoSInterceptTest 
{    
    private Campaign campaign;    
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JV44_PROFILE);
    }

    @Test
    public void strategicInterceptFlightLowAirConfigTest() throws PWCGException
    {
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_LOW);
        runStrategicInterceptFlightTest(1, 1);
    }

    @Test
    public void strategicInterceptFlightMediumAirConfigTest() throws PWCGException
    {
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_MED);
        runStrategicInterceptFlightTest(2, 2);
    }

    @Test
    public void strategicInterceptFlightHighAirConfigTest() throws PWCGException
    {
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.MaxVirtualEscortedFlightKey, "3");
        runStrategicInterceptFlightTest(3, 3);
    }

    @Test
    public void strategicInterceptFlightHighAirConfigWithNotEnoughEscortsTest() throws PWCGException
    {
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.SimpleConfigAirKey, ConfigSimple.CONFIG_LEVEL_HIGH);
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.MaxVirtualEscortedFlightKey, "2");
        runStrategicInterceptFlightTest(3, 2);
    }

	public void runStrategicInterceptFlightTest(int expectedFlights, int expectedEscorts) throws PWCGException
	{
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();
        
        IFlight playerFlight = mission.getFlights().getUnits().get(0);
        if (playerFlight.getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            playerFlight = null;
        }
	
        Assertions.assertTrue (playerFlight.getFlightType() == FlightTypes.STRATEGIC_INTERCEPT);

        List<IFlight> aiFlights = mission.getFlights().getAiFlights();
        Assertions.assertTrue (aiFlights.size() == expectedFlights);

        int actualBombers = 0;
        int actualEscorts = 0;
        for (IFlight aiFlight : aiFlights)
        {
            if (aiFlight.getFlightType() == FlightTypes.STRATEGIC_BOMB)
            {
                ++actualBombers;
               if (aiFlight.getVirtualWaypointPackage().getEscort() != null)
               {
                   ++actualEscorts;
               }
            }
        }

        Assertions.assertTrue (expectedFlights == actualBombers);
        Assertions.assertTrue (expectedEscorts == actualEscorts);

	}
}
