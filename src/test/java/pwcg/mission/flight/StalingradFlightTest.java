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

public class StalingradFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGLogger.setActiveLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void hasBombTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420824"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        assert (mission.getSkirmish() != null);
        
        MissionInformationUtils.verifyFlightTargets(mission, FlightTypes.BOMB, TargetType.TARGET_CITY, Side.AXIS);
        MissionFlightValidator.validateMission(mission);
    }

    @Test
    public void hasDiveBombTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19420910"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean diveBombFlightFound = findFlightType(mission, FlightTypes.DIVE_BOMB, Side.AXIS);

        assert (mission.getSkirmish() != null);
        assert (diveBombFlightFound);
    }

    @Test
    public void hasGroundAttackTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421114"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean groundAttackFlightFound = findFlightType(mission, FlightTypes.GROUND_ATTACK, Side.ALLIED);

        assert (mission.getSkirmish() != null);
        assert (groundAttackFlightFound);
    }

    @Test
    public void hasCargoDropTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19421220"));
       MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean cargoDropFound = findFlightType(mission, FlightTypes.CARGO_DROP, Side.AXIS);
        
        assert (mission.getSkirmish() != null);
        assert (cargoDropFound);
    }

    @Test
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19420815"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19430101"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        assert (mission.getSkirmish() == null);
    }

    private boolean findFlightType(Mission mission, FlightTypes flightType, Side side) throws PWCGException
    {
        boolean flightTypeFound = false;
        for (IFlight flight : mission.getMissionFlights().getAiFlightsForSide(side))
        {
            if (flight.getFlightInformation().getFlightType() == flightType)
            {
                flightTypeFound = true;
            }
        }
        return flightTypeFound;
    }
}
