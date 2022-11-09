package pwcg.mission.flight;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StalingradFlightTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
    }

    @Test
    public void hasCityTargetTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420824"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);

        boolean cityTargetFound = MissionInformationUtils.verifyFlightTargets(mission, TargetType.TARGET_CITY, Side.AXIS);
        Assertions.assertTrue (cityTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasDrifterTargetTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420910"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);

        boolean drifterTargetFound = MissionInformationUtils.verifyFlightTargets(mission, TargetType.TARGET_DRIFTER, Side.AXIS);
        Assertions.assertTrue (drifterTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasGroundAttackTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421114"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasCargoDropTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421220"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.CARGO_DROP, Side.AXIS));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19420815"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19430101"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);
        MissionFlightValidator.validateMission(mission);
    }
}
