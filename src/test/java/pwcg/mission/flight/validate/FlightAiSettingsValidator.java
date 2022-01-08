package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.bomb.BombingFlight;
import pwcg.mission.flight.groundattack.GroundAttackFlight;
import pwcg.mission.flight.intercept.InterceptFlight;
import pwcg.mission.flight.patrol.PatrolFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
public class FlightAiSettingsValidator 
{    
    public FlightAiSettingsValidator() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void groundAttackFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getFlights().getUnits().get(0);
        flight.finalizeFlight();

        validatePlaneAI(mission);
    }

    @Test
    public void groundAttackFlightWithFighterTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_46_PROFILE);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
        GroundAttackFlight flight = (GroundAttackFlight) mission.getFlights().getUnits().get(0);
        flight.finalizeFlight();

        validatePlaneAI(mission);
    }

    @Test
	public void bombFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_2_PROFILE);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);
        BombingFlight flight = (BombingFlight) mission.getFlights().getUnits().get(0);
		flight.finalizeFlight();
		
		validatePlaneAI(mission);
	}

	@Test
	public void patrolFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.PATROL, MissionProfile.DAY_TACTICAL_MISSION);
        PatrolFlight flight = (PatrolFlight) mission.getFlights().getUnits().get(0);
		flight.finalizeFlight();
		
        validatePlaneAI(mission);
	}

	@Test
	public void interceptFlightTest() throws PWCGException
	{
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JASTA_11_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.INTERCEPT, MissionProfile.DAY_TACTICAL_MISSION);
        InterceptFlight flight = (InterceptFlight) mission.getFlights().getUnits().get(0);
		flight.finalizeFlight();
		
        validatePlaneAI(mission);
	}

    @Test
    public void balloonDefenseFlightTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RFC_46_PROFILE);
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BALLOON_DEFENSE, MissionProfile.DAY_TACTICAL_MISSION);
        IFlight flight = (IFlight) mission.getFlights().getUnits().get(0);
        flight.finalizeFlight();

        validatePlaneAI(mission);
    }

    private void validatePlaneAI(Mission mission) throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                CrewMember crewMember = mission.getCampaign().getPersonnelManager().getAnyCampaignMember(plane.getCrewMember().getSerialNumber());
                if (plane.getCrewMember().isPlayer())
                {
                    assert(plane.getAiLevel() == AiSkillLevel.PLAYER);
                }
                else if (!plane.isNovice())
                {
                    assert(plane.getAiLevel() == crewMember.getAiSkillLevel());
                }
                else
                {
                    assert(plane.getAiLevel() == AiSkillLevel.NOVICE);
                }
            }
        }
    }
}
