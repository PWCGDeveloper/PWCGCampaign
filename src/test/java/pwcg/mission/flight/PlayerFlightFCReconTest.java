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
import pwcg.mission.flight.recon.PlayerReconFlight;
import pwcg.mission.flight.recon.ReconPlayerWaypoint;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointPlayerReconSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerFlightFCReconTest 
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
        Mission mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.RECON, MissionProfile.DAY_TACTICAL_MISSION);
        PlayerReconFlight flight = (PlayerReconFlight) mission.getFlights().getPlayerFlights().get(0);
        mission.finalizeMission();
        
        McuWaypoint ingressMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (ingressMissionPoint != null);
        McuWaypoint reconMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RECON);
        Assertions.assertTrue (reconMissionPoint != null);
        McuWaypoint egressMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        Assertions.assertTrue (egressMissionPoint != null);
        
        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

        Assertions.assertEquals(FlightTypes.RECON, flight.getFlightType());        
        
        List<IMissionPointSet> missionPointSets = flight.getWaypointPackage().getMissionPointSets();
    	MissionPointPlayerReconSet reconSet = null;
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
        	if (missionPointSet instanceof MissionPointPlayerReconSet)
        	{
            	reconSet = (MissionPointPlayerReconSet)missionPointSet;

            	// Verify reference into recon WP list
        		ReconPlayerWaypoint firstReconPlayerWaypoint = reconSet.getReconPlayerWaypoints().getFirst();
                Assertions.assertEquals(
                		ingressMissionPoint.getTargets().getFirst(), 
                		Integer.valueOf(firstReconPlayerWaypoint.getEntryTimer().getIndex()).toString());

        		// Verify reference out of recon WP list
                ReconPlayerWaypoint lastReconPlayerWaypoint = reconSet.getReconPlayerWaypoints().getLast();
                Assertions.assertEquals(
                		Integer.valueOf(egressMissionPoint.getIndex()).toString(), 
                		lastReconPlayerWaypoint.getExitTimer().getTargets().getFirst());
        		
                // Verify internal recon WP construction
                ReconPlayerWaypoint prevReconPlayerWaypoint = null;
                for (ReconPlayerWaypoint reconPlayerWaypoint : reconSet.getReconPlayerWaypoints())
                {
                	// Entry timer connected to WP
                    Assertions.assertEquals(
                    		Integer.valueOf(reconPlayerWaypoint.getWaypoint().getIndex()).toString(),
                    		reconPlayerWaypoint.getEntryTimer().getTargets().getFirst());
                    
                	// WP connected to Media
                    Assertions.assertEquals(
                    		Integer.valueOf(reconPlayerWaypoint.getPhotoMedia().getIndex()).toString(),
                    		reconPlayerWaypoint.getWaypoint().getTargets().getFirst());
                    
                	// Media connected to Exit Timer
                    Assertions.assertEquals(
                    		reconPlayerWaypoint.getExitTimer().getIndex(),
                    		reconPlayerWaypoint.getPhotoMedia().getEventList().getFirst().getTarId());
                    
                	if (prevReconPlayerWaypoint != null)
                	{               		
                		// Verify reference to next recon WP
                        Assertions.assertEquals(
                        		Integer.valueOf(reconPlayerWaypoint.getEntryTimer().getIndex()).toString(),
                        		prevReconPlayerWaypoint.getExitTimer().getTargets().getFirst());
                	}
                	prevReconPlayerWaypoint = reconPlayerWaypoint;
                }
        	}        	
        }
    	Assertions.assertNotNull(reconSet);
     }

}
