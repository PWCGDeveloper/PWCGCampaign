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
public class ArdennesFlightTest
{
    private Campaign fg362Campaign = null;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        fg362Campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.FG_362_PROFILE);
    }

    @Test
    public void hasGermanAssaultdGroundAttackTest() throws PWCGException
    {
        verifyAntiArmorOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441220"), Side.AXIS);
        verifyAntiArmorOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441224"), Side.AXIS);
    }

    @Test
    public void hasAlliednAssaultdGroundAttackTest() throws PWCGException
    {
        verifyAntiArmorOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441229"), Side.ALLIED);
        verifyAntiArmorOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441230"), Side.ALLIED);
    }

    private void verifyAntiArmorOnDate(Campaign campaign, Date date, Side attackingSide) throws PWCGException
    {
        campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() != null);
        for (AssaultDefinition assaultDefinition : mission.getBattleManager().getMissionAssaultDefinitions())
        {
            Assertions.assertTrue (assaultDefinition.getAssaultingCountry().getSide() == attackingSide);

        }
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {
        verifyCargoDropsOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441225"));
        verifyCargoDropsOnDate(fg362Campaign, DateUtils.getDateYYYYMMDD("19441228"));
    }

    private void verifyCargoDropsOnDate(Campaign campaign, Date date) throws PWCGException
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
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19441219"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19441231"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        fg362Campaign.setDateWithMapUpdate(date);
        MissionGenerator missionGenerator = new MissionGenerator(fg362Campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(fg362Campaign));
        mission.finalizeMission();

        Assertions.assertTrue (mission.getSkirmish() == null);
    }
}
