package pwcg.mission.flight;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubanInvasionFlightTest
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
    }

    @Test
    public void hasDiveBombKerchInvasionTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        assert (mission.getSkirmish() != null);
        assert (mission.getSkirmish().getSkirmishName().equals("Kerch Amphibious Assault"));
        for (AssaultDefinition assaultDefinition : mission.getMissionBattleManager().getMissionAssaultDefinitions())
        {
            assert (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.DIVE_BOMB, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        assert (diveBombFlightFound || groundAttackFlightFound);

        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.DIVE_BOMB, TargetType.TARGET_SHIPPING, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_SHIPPING, Side.AXIS);
        assert (diveBombFlightTargetFound || groundAttackFlightTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasDiveBombEltigenInvasionTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19431110"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        assert (mission.getSkirmish() != null);
        assert (mission.getSkirmish().getSkirmishName().equals("Eltigen Amphibious Assault"));
        for (AssaultDefinition assaultDefinition : mission.getMissionBattleManager().getMissionAssaultDefinitions())
        {
            assert (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.DIVE_BOMB, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        assert (diveBombFlightFound || groundAttackFlightFound);

        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.DIVE_BOMB, TargetType.TARGET_SHIPPING, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_SHIPPING, Side.AXIS);
        assert (diveBombFlightTargetFound || groundAttackFlightTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishAfterTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431112"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.STG77_KUBAN_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        assert (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
