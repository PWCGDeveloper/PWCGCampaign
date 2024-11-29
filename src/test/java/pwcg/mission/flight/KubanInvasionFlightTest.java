package pwcg.mission.flight;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.AssaultDefinition;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KubanInvasionFlightTest
{
    private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.STG77_KUBAN_PROFILE);
    }

    @Test
    public void hasDiveBombKerchInvasionTest() throws PWCGException
    {
        campaign.setDateWithMapUpdate(DateUtils.getDateYYYYMMDD("19431101"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().equals("Kerch Amphibious Assault"));
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasShippingTargetTest() throws PWCGException
    {
        campaign.setDateWithMapUpdate(DateUtils.getDateYYYYMMDD("19431110"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        Assertions.assertTrue (mission.getSkirmish().getSkirmishName().equals("Eltigen Amphibious Assault"));
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.RUSSIA);

        }

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishAfterTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19431112"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.STG77_KUBAN_PROFILE);
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
