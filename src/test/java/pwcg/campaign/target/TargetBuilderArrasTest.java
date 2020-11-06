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
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetPriorityGeneratorGroundUnit;
import pwcg.mission.target.TargetSelectorGroundUnit;
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TargetPriorityGeneratorGroundUnit.class})
public class TargetBuilderArrasTest
{
    private Campaign campaign;
    private Mission mission;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);

        if (mission == null)
        {
            MissionGenerator missionGenerator = new MissionGenerator(campaign);
            mission = missionGenerator.makeTestSingleMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        }
    }
    
    @Test
    public void findInfantryTest()  throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorGroundUnit.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_ASSAULT, TargetType.TARGET_ASSAULT, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(Mockito.any(), Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        
        TargetSelectorGroundUnit targetBuilder = new TargetSelectorGroundUnit(playerFlight.getFlightInformation());
        IGroundUnitCollection groundUnits = targetBuilder.findTarget();
        
        assert(groundUnits.getGroundUnits().size() >= 1);
        assert(groundUnits.getTargetType() == TargetType.TARGET_ASSAULT);

        IGroundUnit groundUnit = groundUnits.getInterestingGroundUnitsForSide(playerFlight.getFlightInformation().getSquadron().determineEnemySide()).get(0);
        assert(groundUnit.getCountry().getCountry() == Country.GERMANY);
        assert(groundUnit.getTargetType() == TargetType.TARGET_ARMOR || groundUnit.getTargetType() == TargetType.TARGET_INFANTRY || groundUnit.getTargetType() == TargetType.TARGET_ARTILLERY);
    }
    
    @Test
    public void findTransportTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorGroundUnit.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRANSPORT, TargetType.TARGET_ASSAULT, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(Mockito.any(), Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        
        TargetSelectorGroundUnit targetBuilder = new TargetSelectorGroundUnit(playerFlight.getFlightInformation());
        IGroundUnitCollection groundUnits = targetBuilder.findTarget();
        
        assert(groundUnits.getGroundUnits().size() >= 1);
        assert(groundUnits.getTargetType() == TargetType.TARGET_TRANSPORT);

        IGroundUnit groundUnit = groundUnits.getGroundUnits().get(0);
        assert(groundUnit.getCountry().getCountry() == Country.GERMANY);
        assert(groundUnit.getTargetType() == TargetType.TARGET_TRANSPORT);
    }

    @Test
    public void findTrainTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorGroundUnit.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_TRAIN, TargetType.TARGET_ASSAULT, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(Mockito.any(), Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        
        TargetSelectorGroundUnit targetBuilder = new TargetSelectorGroundUnit(playerFlight.getFlightInformation());
        IGroundUnitCollection groundUnits = targetBuilder.findTarget();
        
        assert(groundUnits.getGroundUnits().size() >= 1);
        assert(groundUnits.getTargetType() == TargetType.TARGET_TRAIN);

        IGroundUnit groundUnit = groundUnits.getGroundUnits().get(0);
        assert(groundUnit.getCountry().getCountry() == Country.GERMANY);
        assert(groundUnit.getTargetType() == TargetType.TARGET_TRAIN);
    }

    @Test
    public void findBalloonTest() throws PWCGException
    {
        PowerMockito.mockStatic(TargetPriorityGeneratorGroundUnit.class);
        List<TargetType> shuffledTargetTypes = Arrays.asList(TargetType.TARGET_BALLOON, TargetType.TARGET_ASSAULT, TargetType.TARGET_FACTORY);
        Mockito.when(TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(Mockito.any(), Mockito.any())).thenReturn(shuffledTargetTypes);

        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        
        TargetSelectorGroundUnit targetBuilder = new TargetSelectorGroundUnit(playerFlight.getFlightInformation());
        IGroundUnitCollection groundUnits = targetBuilder.findTarget();
        
        assert(groundUnits.getGroundUnits().size() >= 1);
        assert(groundUnits.getTargetType() == TargetType.TARGET_BALLOON);

        IGroundUnit groundUnit = groundUnits.getGroundUnits().get(0);
        assert(groundUnit.getCountry().getCountry() == Country.GERMANY);
        assert(groundUnit.getTargetType() == TargetType.TARGET_BALLOON);
    }
}
