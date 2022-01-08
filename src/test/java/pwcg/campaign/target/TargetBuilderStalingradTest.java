package pwcg.campaign.target;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TargetBuilderStalingradTest
{
    private Campaign campaign;
    private static Mission mission;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_PROFILE);

        if (mission == null)
        {
            MissionGenerator missionGenerator = new MissionGenerator(campaign);
            mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign),
                    FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        }
    }

    @Test
    public void findInfantryTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_INFANTRY, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_ARMOR || targetDefinition.getTargetType() == TargetType.TARGET_INFANTRY
                    || targetDefinition.getTargetType() == TargetType.TARGET_ARTILLERY);
        }
    }

    @Test
    public void findTransportTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRANSPORT, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_TRANSPORT);
        }
    }

    @Test
    public void findAirfieldTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_AIRFIELD, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_AIRFIELD);
        }
    }

    @Test
    public void findBridgeTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_BRIDGE, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_BRIDGE);
        }
    }

    @Test
    public void findDrifterTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_DRIFTER, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_DRIFTER);
        }
    }

    @Test
    public void findTrainTest() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRAIN, TargetType.TARGET_INFANTRY, TargetType.TARGET_FACTORY);
        try (MockedStatic<TargetPriorityGeneratorTactical> mocked = Mockito.mockStatic(TargetPriorityGeneratorTactical.class))
        {
            mocked.when(() -> TargetPriorityGeneratorTactical.getTargetTypePriorities(Mockito.any())).thenReturn(shuffledTargetTypes);

            IFlight playerFlight = mission.getFlights().getUnits().get(0);

            TargetDefinition targetDefinition = GroundTargetDefinitionFactory.buildTargetDefinition(playerFlight.getFlightInformation());

            Assertions.assertTrue (targetDefinition.getCountry().getCountry() == Country.RUSSIA);
            Assertions.assertTrue (targetDefinition.getTargetType() == TargetType.TARGET_TRAIN);
        }
    }
}
