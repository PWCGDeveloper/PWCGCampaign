package pwcg.mission.flight;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
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
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArnhemFlightTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
    }

    @Test
    public void hasSkirmishAndParaDropDay1Test() throws PWCGException
    {
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440917"));
    }

    @Test
    public void hasSkirmishAndParaDropDay2Test() throws PWCGException
    {
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440918"));
    }

    private void verifyParaDropOnDate(Date date) throws PWCGException
    {
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.GERMANY);

        }
        
        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.PARATROOP_DROP, Side.ALLIED));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasSkirmishAndCargoDropArnhemEarlyTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440920"));
    }

    @Test
    public void hasSkirmishAndCargoDropArnhemMidTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440925"));
    }

    @Test
    public void hasSkirmishAndCargoDropArnhemLateTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440928"));
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getCountry() == Country.GERMANY);

        }

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.CARGO_DROP, Side.ALLIED));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19440916"));
    }

    @Test
    public void doesNotHaveSkirmishAfterTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19440930"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
