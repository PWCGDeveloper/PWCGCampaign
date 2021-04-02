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
import pwcg.mission.target.TargetType;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

public class ArnhemFlightTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void hasSkirmishAndParaDropTest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(DateUtils.getDateYYYYMMDD("19440917"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean paraDropFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            if (flight.getFlightInformation().getFlightType() == FlightTypes.PARATROOP_DROP)
            {
                paraDropFound = true;
            }
        }
        
        assert (mission.getSkirmish() != null);
        assert (paraDropFound);
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440918"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440925"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440928"));
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(date);
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        boolean cargoDropFound = false;
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlightsForSide(Side.ALLIED))
        {
            if (flight.getFlightInformation().getFlightType() == FlightTypes.CARGO_DROP)
            {
                cargoDropFound = true;
            }
        }
        
        assert (mission.getSkirmish() != null);
        assert (cargoDropFound);        

        verifyFlightTargets(mission);
    }

    @Test
    public void doesNotHaveSkirmishTest() throws PWCGException
    {
        noSkirmish(DateUtils.getDateYYYYMMDD("19440916"));
        noSkirmish(DateUtils.getDateYYYYMMDD("19440930"));
    }

    private void noSkirmish(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
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
            
            if (flight.getFlightType() == FlightTypes.LOW_ALT_BOMB)
            {
                assert (flight.getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
            }
            
            if (flight.getFlightType() == FlightTypes.BOMB)
            {
                assert (flight.getTargetDefinition().getTargetType() == TargetType.TARGET_INFANTRY);
            }
        }
    }
}
