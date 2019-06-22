package pwcg.mission.flight.validate;

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
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.attack.GroundAttackFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.bomb.StrategicBombingFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

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
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.GROUND_ATTACK);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        validatePlaneAI(mission);
    }

    @Test
    public void groundAttackFlightWithFighterTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_103_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.GROUND_ATTACK);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
        flight.finalizeFlight();

        validatePlaneAI(mission);
    }

    @Test
	public void bombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.BOMB);
        BombingFlight flight = (BombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
		validatePlaneAI(mission);
	}
	
	@Test
	public void strategicBombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_2_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.STRATEGIC_BOMB);
        StrategicBombingFlight flight = (StrategicBombingFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
        validatePlaneAI(mission);
	}

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.ESC_103_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.PATROL);
        PatrolFlight flight = (PatrolFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
		flight.finalizeFlight();
		
        validatePlaneAI(mission);
	}

	@Test
	public void interceptFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JASTA_11_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        Mission mission = new Mission(campaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(campaign), missionBorders);
        mission.generate( FlightTypes.INTERCEPT);
        InterceptFlight flight = (InterceptFlight) mission.getMissionFlightBuilder().getPlayerFlights().get(0);
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
