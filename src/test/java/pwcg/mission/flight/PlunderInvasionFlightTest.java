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
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.utils.MissionFlightValidator;
import pwcg.mission.utils.MissionInformationUtils;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class PlunderInvasionFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void plunderHasGroundAttackTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19450323"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        assert (mission.getSkirmish() != null);
        assert (mission.getSkirmish().getSkirmishName().contains("Plunder"));

        boolean axisGroundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.AXIS);
        assert (axisGroundAttackFlightFound);

        boolean alliedGroundAttackFlightFound = MissionInformationUtils.verifyFlightTypeInMission(mission, FlightTypes.GROUND_ATTACK, Side.ALLIED);
        assert (alliedGroundAttackFlightFound);

        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void doesNotHaveSkirmishBeforeTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19450322"));
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
