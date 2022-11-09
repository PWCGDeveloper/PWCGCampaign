package pwcg.mission.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AiAdjusterTest
{
    private static Mission mission;
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void verifyFighterAdjustmentToAce() throws PWCGException
    {
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.FighterAISkillAdjustmentKey, Integer.valueOf(4).toString());
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.BomberAISkillAdjustmentKey, Integer.valueOf(0).toString());
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getAiPlanes())
            {
                if (plane.isPrimaryRole(PwcgRole.ROLE_FIGHTER) || plane.isNovice() == false)
                {
                    assert(plane.getAiLevel() == AiSkillLevel.ACE);
                }
                else 
                {
                    assert(plane.getAiLevel() == AiSkillLevel.NOVICE);
                }
            }
        }
    }

    @Test
    public void verifyFighterAdjustmentToNovice() throws PWCGException
    {
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.FighterAISkillAdjustmentKey, Integer.valueOf(-4).toString());
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.BomberAISkillAdjustmentKey, Integer.valueOf(0).toString());
        
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getAiPlanes())
            {
                if (plane.isPrimaryRole(PwcgRole.ROLE_FIGHTER) || plane.isNovice() == false)
                {
                    assert(plane.getAiLevel() == AiSkillLevel.NOVICE);
                }
                else 
                {
                    assert(plane.getAiLevel() == AiSkillLevel.NOVICE);
                }
            }
        }
    }

    @Test
    public void verifyBomberAdjustmentToAce() throws PWCGException
    {
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.FighterAISkillAdjustmentKey, Integer.valueOf(0).toString());
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.BomberAISkillAdjustmentKey, Integer.valueOf(4).toString());

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getAiPlanes())
            {
                if (plane.isPrimaryRole(PwcgRole.ROLE_FIGHTER) || plane.isNovice() == false)
                {
                    assert(plane.getAiLevel().getAiSkillLevel() >= AiSkillLevel.NOVICE.getAiSkillLevel() &&
                           plane.getAiLevel().getAiSkillLevel() <= AiSkillLevel.ACE.getAiSkillLevel());
                }
                else 
                {
                    assert(plane.getAiLevel() == AiSkillLevel.ACE);
                }
            }
        }

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getPlayerPlanes())
            {
                assert(plane.getAiLevel() == AiSkillLevel.PLAYER);
            }
        }
    }

    @Test
    public void verifyAllAdjustmentToCommonOrBetter() throws PWCGException
    {
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.FighterAISkillAdjustmentKey, Integer.valueOf(1).toString());
        campaign.getCampaignConfigManager().setParam(ConfigItemKeys.BomberAISkillAdjustmentKey, Integer.valueOf(1).toString());

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ESCORT, MissionProfile.DAY_TACTICAL_MISSION);
        mission.finalizeMission();

        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane: flight.getFlightPlanes().getAiPlanes())
            {
                if (plane.isPrimaryRole(PwcgRole.ROLE_FIGHTER) || plane.isNovice() == false)
                {
                    assert(plane.getAiLevel().getAiSkillLevel() >= AiSkillLevel.COMMON.getAiSkillLevel() &&
                           plane.getAiLevel().getAiSkillLevel() <= AiSkillLevel.ACE.getAiSkillLevel());
                }
                else 
                {
                    assert(plane.getAiLevel() == AiSkillLevel.COMMON);
                }
            }
        }
    }

}
