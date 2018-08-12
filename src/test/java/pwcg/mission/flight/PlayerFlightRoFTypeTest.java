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
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

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
		Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_2_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.GROUND_ATTACK);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.GROUND_ATTACK);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
	}
	
	@Test
	public void bombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_2_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.BOMB);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.BOMB);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
	}
	
	@Test
	public void strategicBombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_2_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.STRATEGIC_BOMB);
        StrategicBombingFlight flight = (StrategicBombingFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		GroundAttackFlightValidator groundAttackFlightValidator = new GroundAttackFlightValidator();
		groundAttackFlightValidator.validateGroundAttackFlight(flight);
        validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.STRATEGIC_BOMB);
	}

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.PATROL);
	}

    @Test
    public void balloonBustFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.BALLOON_BUST);
        BalloonBustFlight flight = (BalloonBustFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();
        
        assert(flight.getFlightType() == FlightTypes.BALLOON_BUST);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
    }

    @Test
    public void balloonDefenseFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.BALLOON_DEFENSE);
        PlayerBalloonDefenseFlight flight = (PlayerBalloonDefenseFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();
        
        assert(flight.getFlightType() == FlightTypes.BALLOON_DEFENSE);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
    }

	@Test
	public void interceptFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.JASTA_11_PROFILE);

	    Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.INTERCEPT);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.INTERCEPT);
	}

	@Test
	public void offensiveFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);

	    Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.OFFENSIVE);
        OffensiveFlight flight = (OffensiveFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PatrolFlightValidator patrolFlightValidator = new PatrolFlightValidator();
		patrolFlightValidator.validatePatrolFlight(flight);
        assert(flight.getFlightType() == FlightTypes.OFFENSIVE);
	}

	@Test
	public void reconFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_2_PROFILE);

	    Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.RECON);
        PlayerReconFlight flight = (PlayerReconFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PlayerReconFlightValidator reconFlightValidator = new PlayerReconFlightValidator();
		reconFlightValidator.validateReconFlight(flight);
        assert(flight.getFlightType() == FlightTypes.RECON);
	}

	@Test
	public void escortFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);

	    Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.ESCORT);
        PlayerEscortFlight flight = (PlayerEscortFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PlayerEscortFlightValidator escortFlightValidator = new PlayerEscortFlightValidator();
		escortFlightValidator.validateEscortFlight(flight);
        assert(flight.getFlightType() == FlightTypes.ESCORT);
	}

	@Test
	public void artillerySpotFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_2_PROFILE);
		
	    Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.ARTILLERY_SPOT);
        PlayerArtillerySpotFlight flight = (PlayerArtillerySpotFlight) mission.getMissionFlightBuilder().getPlayerFlight();
		flight.finalizeFlight();
		
		PlayerArtillerySpotFlightValidator artillerySpotFlightValidator = new PlayerArtillerySpotFlightValidator();
		artillerySpotFlightValidator.validateArtillerySpotFlight(flight);
		validateTargetDefinition(flight.getTargetDefinition());
        assert(flight.getFlightType() == FlightTypes.ARTILLERY_SPOT);
        
        GroundUnitValidator groundUnitValidator = new GroundUnitValidator();
        groundUnitValidator.validateGroundUnitsForMission(mission);
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
