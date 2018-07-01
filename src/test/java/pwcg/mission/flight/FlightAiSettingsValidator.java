package pwcg.mission.flight;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.bomb.StrategicBombingFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(Parameterized.class)
public class FlightAiSettingsValidator 
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

        validatePlaneAI(mission);
    }

    @Test
    public void groundAttackFlightWithFighterTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);

        Mission mission = new Mission();
        mission.initialize(campaign);        
        mission.generate(FlightTypes.GROUND_ATTACK);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlight();
        flight.finalizeFlight();

        validatePlaneAI(mission);
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
		
		validatePlaneAI(mission);
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
		
        validatePlaneAI(mission);
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
		
        validatePlaneAI(mission);
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
		
        validatePlaneAI(mission);
	}
    
    private void validatePlaneAI(Mission mission) throws PWCGException
    {
        for (Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                SquadronMember squadronMember = mission.getCampaign().getPersonnelManager().getAnyCampaignMember(plane.getPilot().getSerialNumber());
                Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(squadronMember.getSquadronId());
                Role primaryRole = squadron.determineSquadronPrimaryRole(mission.getCampaign().getDate());
                if (primaryRole == Role.ROLE_FIGHTER)
                {
                    assert(plane.getAiLevel() == squadronMember.getAiSkillLevel());
                }
                else
                {
                    if (plane.getPilot().isPlayer())
                    {
                        assert(plane.getAiLevel() == AiSkillLevel.PLAYER);
                    }
                    else
                    {
                        assert(plane.getAiLevel() == AiSkillLevel.NOVICE);
                    }
                }
            }
        }
    }
}
