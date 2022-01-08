package pwcg.gui.rofmap.brief.update;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.gui.rofmap.brief.builder.BriefingDataBuilder;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BriefingMissionUpdaterTest
{
    private Campaign campaign;
    private static Mission mission;
    private static BriefingData briefingData;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.BOMB, MissionProfile.DAY_TACTICAL_MISSION);

        BriefingDataBuilder briefingDataBuilder = new BriefingDataBuilder(mission);
        briefingData = briefingDataBuilder.buildBriefingData();
    }

    @Test
    public void testBriefingUpdates() throws PWCGException
    {
        changeWaypointData();
        BriefingUnitUpdater.pushEditsToMission(briefingData);
        
        for (McuWaypoint waypoint : mission.getFlights().getPlayerFlightForSquadron(SquadronTestProfile.KG53_PROFILE.getCompanyId()).getWaypointPackage().getAllWaypoints())
        {
            if (waypoint.getDesc().equals("Target Approach"))
            {
                double distanceApart = MathUtils.calcDist(waypoint.getPosition(), new Coordinate(1000, 0, 1000));
                assert(distanceApart < 10.0);

                double altitudeApart = waypoint.getPosition().getYPos() - 5001;
                assert(altitudeApart < 10.0);
            }
            if (waypoint.getDesc().equals("Ingress"))
            {
                double distanceApart = MathUtils.calcDist(waypoint.getPosition(), new Coordinate(1400, 0, 1400));
                assert(distanceApart < 10.0);

                double altitudeApart = waypoint.getPosition().getYPos() - 4501;
                assert(altitudeApart < 10.0);
            }
            if (waypoint.getDesc().contains(": Egress"))
            {
                double distanceApart = MathUtils.calcDist(waypoint.getPosition(), new Coordinate(1800, 0, 1800));
                assert(distanceApart < 10.0);

                double altitudeApart = waypoint.getPosition().getYPos() - 4001;
                assert(altitudeApart < 10.0);
            }
        }
    }

    private void changeWaypointData()
    {
        BriefingUnit briefingFlight = briefingData.getActiveBriefingUnit();
        BriefingUnitParameters briefingFlightParameters = briefingFlight.getBriefingUnitParameters();
        List<BriefingMapPoint>  briefingMapMapPoints = briefingFlightParameters.getBriefingMapMapPoints();
        
        BriefingMapPoint targetApproachBriefingMapMapPoint = null;
        for (BriefingMapPoint briefingMapMapPoint : briefingMapMapPoints)
        {
            if (briefingMapMapPoint.getDesc().equals("Target Approach"))
            {
                targetApproachBriefingMapMapPoint = briefingMapMapPoint;
                break;
            }
        }
        assert(targetApproachBriefingMapMapPoint != null);
        targetApproachBriefingMapMapPoint.setAltitude(5001);
        targetApproachBriefingMapMapPoint.setPosition(new Coordinate(1000, 0, 1000));
        
        BriefingMapPoint ingressBriefingMapMapPoint = null;
        for (BriefingMapPoint briefingMapMapPoint : briefingMapMapPoints)
        {
            if (briefingMapMapPoint.getDesc().equals("Ingress"))
            {
                ingressBriefingMapMapPoint = briefingMapMapPoint;
                break;
            }
        }
        assert(ingressBriefingMapMapPoint != null);
        ingressBriefingMapMapPoint.setAltitude(4501);
        ingressBriefingMapMapPoint.setPosition(new Coordinate(1400, 0, 1400));
        
        BriefingMapPoint egressBriefingMapMapPoint = null;
        for (BriefingMapPoint briefingMapMapPoint : briefingMapMapPoints)
        {
            if (briefingMapMapPoint.getDesc().equals("Egress"))
            {
                egressBriefingMapMapPoint = briefingMapMapPoint;
                break;
            }
        }
        assert(egressBriefingMapMapPoint != null);
        egressBriefingMapMapPoint.setAltitude(4001);
        egressBriefingMapMapPoint.setPosition(new Coordinate(1800, 0, 1800));
    }

}
