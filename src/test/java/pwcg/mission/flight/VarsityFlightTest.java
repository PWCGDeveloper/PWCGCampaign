package pwcg.mission.flight;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.target.TargetType;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class VarsityFlightTest
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGLogger.setActiveLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void hasSkirmishAndParaDropTest() throws PWCGException
    {
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19450324"));
    }

    private void verifyParaDropOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        assert (mission.getSkirmish() != null);
        assert (mission.getSkirmish().getSkirmishName().contains("Varsity"));

        assert(MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.PARATROOP_DROP, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.GROUND_ATTACK, TargetType.TARGET_INFANTRY, Side.ALLIED));
        assert(MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_INFANTRY, Side.ALLIED));
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishAfterTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19450325"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        assert (mission.getSkirmish() == null);
        
        MissionFlightValidator.validateMission(mission);
    }
}
