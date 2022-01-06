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
import pwcg.testutils.CampaignCache;
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
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void hasBombTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420824"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        Assertions.assertTrue (mission.getSkirmish() != null);
        
        boolean bombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.BOMB, Side.AXIS);
        boolean lowAltBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.LOW_ALT_BOMB, Side.AXIS);
        Assertions.assertTrue (bombFlightFound || lowAltBombFlightFound);

        boolean bombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_CITY, Side.AXIS);
        boolean lowAltBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.LOW_ALT_BOMB, TargetType.TARGET_CITY, Side.AXIS);
        Assertions.assertTrue (bombFlightTargetFound || lowAltBombFlightTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasDiveBombTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420910"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        Assertions.assertTrue (mission.getSkirmish() != null);

        boolean diveBombFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.DIVE_BOMB, Side.AXIS);
        boolean groundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        Assertions.assertTrue (diveBombFlightFound || groundAttackFlightFound);

        boolean diveBombFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.DIVE_BOMB, TargetType.TARGET_DRIFTER, Side.AXIS);
        boolean groundAttackFlightTargetFound = MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_DRIFTER, Side.AXIS);
        Assertions.assertTrue (diveBombFlightTargetFound || groundAttackFlightTargetFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasGroundAttackTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421114"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        Assertions.assertTrue (mission.getSkirmish() != null);

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_INFANTRY, Side.ALLIED));

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasCargoDropTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421220"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

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
        
        Assertions.assertTrue (mission.getSkirmish() == null);
        MissionFlightValidator.validateMission(mission);
    }
}
