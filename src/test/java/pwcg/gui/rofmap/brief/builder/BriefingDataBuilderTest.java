package pwcg.gui.rofmap.brief.builder;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BriefingDataBuilderTest
{
    private Campaign campaign;
    private static Mission mission;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
    }

    @Test
    public void testBriefingBuilder() throws PWCGException
    {
        BriefingDataBuilder briefingDataBuilder = new BriefingDataBuilder(mission);
        BriefingData briefingData = briefingDataBuilder.buildBriefingData();
        assert(briefingData != null);
        assert(briefingData.getActiveBriefingUnit() != null);
        assert(briefingData.getActiveBriefingUnit().getBriefingUnitParameters().getBriefingMapMapPoints().size() > 4);
        assert(briefingData.getActiveBriefingUnit().getBriefingAssignmentData().getCrews().size() > 0);
        
        IFlight flight = briefingData.getSelectedUnit();
        Assertions.assertTrue (flight.getCompany().getCompanyId() == SquadronTestProfile.KG53_PROFILE.getCompanyId());

        BriefingUnit briefingFlight = briefingData.getActiveBriefingUnit();
        Assertions.assertTrue (briefingFlight.getCompanyId() == SquadronTestProfile.KG53_PROFILE.getCompanyId());
        
        BriefingUnitParameters briefingFlightParameters = briefingFlight.getBriefingUnitParameters();
        List<BriefingMapPoint>  briefingMapMapPoints = briefingFlightParameters.getBriefingMapMapPoints();
        for (BriefingMapPoint briefingMapMapPoint : briefingMapMapPoints)
        {
            assert(briefingMapMapPoint.getDesc() != null);
            assert(!briefingMapMapPoint.getDesc().isEmpty());
        }

    }
}
