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
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlunderInvasionFlightTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
    }


    @Test
    public void plunderHasGroundAttackTest() throws PWCGException
    {
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450323"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().contains("Plunder"));

        boolean axisGroundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        Assertions.assertTrue (axisGroundAttackFlightFound);

        boolean alliedGroundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.ALLIED);
        Assertions.assertTrue (alliedGroundAttackFlightFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19450322"));
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
