package pwcg.mission.flight;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetCategory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.artySpot.PlayerArtillerySpotFlight;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.balloonBust.BalloonBustFlight;
import pwcg.mission.flight.balloondefense.PlayerBalloonDefenseFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.bomb.StrategicBombingFlight;
import pwcg.mission.flight.escort.PlayerEscortFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.offensive.OffensiveFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.recon.PlayerReconFlight;
import pwcg.mission.flight.validate.GroundAttackFlightValidator;
import pwcg.mission.flight.validate.GroundUnitValidator;
import pwcg.mission.flight.validate.PatrolFlightValidator;
import pwcg.mission.flight.validate.PlayerArtillerySpotFlightValidator;
import pwcg.mission.flight.validate.PlayerEscortFlightValidator;
import pwcg.mission.flight.validate.PlayerReconFlightValidator;
import pwcg.mission.flight.validate.PositionEvaluator;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(Parameterized.class)
public class PlayerFlightRoFTypeTest 
{    
    @Parameterized.Parameters
    public static List<Object[]> data() 
    {
        return Arrays.asList(new Object[1][0]);
    }
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

	@Test
	public void groundAttackFlightTest() throws PWCGException
	{
		Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK);

        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.GROUND_ATTACK);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        EscortForPlayerValidator.validateEscortForPlayer(flight);
        PositionEvaluator.evaluateAiFlight(mission);
	}
	
	@Test
	public void bombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);
		
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BOMB);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.BOMB);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        PositionEvaluator.evaluateAiFlight(mission);
	}
	
	@Test
	public void strategicBombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.STRATEGIC_BOMB);
        StrategicBombingFlight flight = (StrategicBombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        PositionEvaluator.evaluateAiFlight(mission);
        assert(flight.getFlightType() == FlightTypes.STRATEGIC_BOMB);
	}

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_103_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
        PositionEvaluator.evaluateAiFlight(mission);
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
	}

    @Test
    public void balloonBustFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JASTA_11_PROFILE);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BALLOON_BUST);
        BalloonBustFlight flight = (BalloonBustFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();
        
        assert(flight.getFlightType() == FlightTypes.BALLOON_BUST);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        PositionEvaluator.evaluateAiFlight(mission);
    }

    @Test
    public void balloonDefenseFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JASTA_11_PROFILE);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.BALLOON_DEFENSE);
        PlayerBalloonDefenseFlight flight = (PlayerBalloonDefenseFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();
        
        assert(flight.getFlightType() == FlightTypes.BALLOON_DEFENSE);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        PositionEvaluator.evaluateAiFlight(mission);
    }

	@Test
	public void interceptFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JASTA_11_PROFILE);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.INTERCEPT);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
        PositionEvaluator.evaluateAiFlight(mission);
	}

	@Test
	public void offensiveFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_103_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.OFFENSIVE);
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.OFFENSIVE);
        PositionEvaluator.evaluateAiFlight(mission);
	}

	@Test
	public void reconFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.RECON);
        PlayerReconFlight flight = (PlayerReconFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PlayerReconFlightValidator reconFlightValidator = new PlayerReconFlightValidator();
		reconFlightValidator.validateReconFlight(flight);
        assert(flight.getFlightType() == FlightTypes.RECON);
        PositionEvaluator.evaluateAiFlight(mission);
	}

	@Test
	public void escortFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_103_PROFILE);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT);
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator(flight);
		escortFlightValidator.validateEscortFlight();
        assert(flight.getFlightType() == FlightTypes.ESCORT);
        PositionEvaluator.evaluateAiFlight(mission);
	}

	@Test
	public void artillerySpotFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMissionFromFlightType(TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), FlightTypes.ARTILLERY_SPOT);
        PlayerArtillerySpotFlight flight = (PlayerArtillerySpotFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		PlayerArtillerySpotFlightValidator artillerySpotFlightValidator = new PlayerArtillerySpotFlightValidator();
		artillerySpotFlightValidator.validateArtillerySpotFlight(flight);
		validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.ARTILLERY_SPOT);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
        PositionEvaluator.evaluateAiFlight(mission);
	}
	
	public void validateTargetDefinition(TargetDefinition targetDefinition)
	{
        assert (targetDefinition.getAttackingCountry() != null);
        assert (targetDefinition.getTargetCountry() != null);
        assert (targetDefinition.getTargetCategory() != TargetCategory.TARGET_CATEGORY_NONE);
        assert (targetDefinition.getTargetType() != TacticalTarget.TARGET_NONE);
	}
}
