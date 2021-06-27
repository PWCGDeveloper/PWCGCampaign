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
@PrepareForTest({TargetPriorityGeneratorTactical.class})
public class TargetBuilderStalingradTest
{
    private Campaign campaign;
    private Mission mission;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);

        if (mission == null)
        {
            MissionGenerator missionGenerator = new MissionGenerator(campaign);
            mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        }
    }
    
    @Test
    public void findInfantryTest()  throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_ARMOR || targetDefinition.getTargetType() == TargetType.TARGET_INFANTRY || targetDefinition.getTargetType() == TargetType.TARGET_ARTILLERY);
    }
    
    @Test
    public void findTransportTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRANSPORT, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_TRANSPORT);
    }
    
    @Test
    public void findAirfieldTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_AIRFIELD, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_AIRFIELD);
    }
    
    @Test
    public void findBridgeTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_BRIDGE, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_BRIDGE);
    }
    
    @Test
    public void findDrifterTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_DRIFTER, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_DRIFTER);
    }

    @Test
    public void findTrainTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorTactical.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRAIN, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlights().getPlayerFlights().get(0);
        
        TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

        assert(targetDefinition.getCountry().getCountry() == Country.RUSSIA);
        assert(targetDefinition.getTargetType() == TargetType.TARGET_TRAIN);
    }
}
