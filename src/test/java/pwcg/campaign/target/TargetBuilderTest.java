package pwcg.campaign.target;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.GroundTargetDefinitionFactory;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetPriorityGeneratorTactical;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TargetPriorityGeneratorTactical.class, RandomNumberGenerator.class})
public class TargetBuilderTest
{
    private Campaign campaign;
    private Mission mission;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_365_BODENPLATTE_PROFILE);

        if (mission == null)
        {
            MissionGenerator missionGenerator = new MissionGenerator(campaign);
            mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        }
    }
    
    @Test
    public void useSquadronPreferredTargetTest()  throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        PowerMockito.mockStatic(RandomNumberGenerator.class);
        
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        Mockito.when(RandomNumberGenerator.getRandom(100)).thenReturn(1);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.GERMANY);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_TRAIN);
    }
    
    @Test
    public void useSquadronPreferredTargetDoNotUseTest()  throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        PowerMockito.mockStatic(RandomNumberGenerator.class);
        
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        Mockito.when(RandomNumberGenerator.getRandom(100)).thenReturn(99);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.GERMANY);
        assert(targetDefinition.getTargetType() != TargetType.TARGET_TRAIN);
    }
}
