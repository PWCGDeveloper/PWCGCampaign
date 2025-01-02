package pwcg.mission.flight;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.artySpot.PlayerArtillerySpotFlight;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightFCArtillerySpotTest 
{    
	private Campaign campaign;    

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.ESC_2_PROFILE);
    }

    @Test
    public void playerReconFlightTest() throws PWCGException
    {
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(
        		TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.ARTILLERY_SPOT, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerArtillerySpotFlight flight = (PlayerArtillerySpotFlight) mission.getFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        
        McuWaypoint ingressMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (ingressMissionPoint != null);
        McuWaypoint reconMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_SPOT);
        Assertions.assertTrue (reconMissionPoint != null);
        McuWaypoint egressMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        Assertions.assertTrue (egressMissionPoint != null);
        
        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

        Assertions.assertEquals(FlightTypes.ARTILLERY_SPOT, flight.getFlightType());        
        
        List<IMissionPointSet> missionPointSets = flight.getWaypointPackage().getMissionPointSets();
    	Assertions.assertNotNull(missionPointSets);
     }

}
