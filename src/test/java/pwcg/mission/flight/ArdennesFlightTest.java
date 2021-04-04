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
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class ArdennesFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGLogger.setActiveLogLevel(LogLevel.DEBUG);
    }

    @Test
    public void hasSkirmishAndGroundAttackTest() throws PWCGException
    {
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441220"));
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441224"));
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441229"));
        verifyAntiArmorOnDate(DateUtils.getDateYYYYMMDD("19441230"));
    }

    private void verifyAntiArmorOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean groundAttackFound = findFlightType(mission, FlightTypes.GROUND_ATTACK);

        assert (mission.getSkirmish() != null);
        assert (groundAttackFound);
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19441225"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19441228"));
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean cargoDropFound = findFlightType(mission, FlightTypes.CARGO_DROP);
        assert (mission.getSkirmish() != null);
        assert (cargoDropFound);        

        verifyFlightTargets(mission);
    }

    @Test
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19441219"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19441231"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.FG_362_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        
        assert (mission.getSkirmish() == null);
    }

    private void verifyFlightTargets(Mission mission) throws PWCGException
    {
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
            {
                assert (flight.getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
            }
            
            if (flight.getFlightType() == FlightTypes.BOMB)
            {
                assert (flight.getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
            }
        }
    }

    private boolean findFlightType(Mission mission, FlightTypes flightType) throws PWCGException
    {
        boolean flightTypeFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            if (flight.getFlightInformation().getFlightType() == flightType)
            {
                flightTypeFound = true;
            }
        }
        return flightTypeFound;
    }
}
