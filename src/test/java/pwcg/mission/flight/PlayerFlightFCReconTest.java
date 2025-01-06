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
import pwcg.mission.flight.recon.PlayerReconWaypoint;
import pwcg.mission.flight.validate.PlaneRtbValidator;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightBeginTakeoff;
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
        
        McuWaypoint ingressWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        Assertions.assertTrue (ingressWaypoint != null);
        Assertions.assertEquals(
        		ingressWaypoint.getObjects().get(0), 
        		Integer.valueOf(flight.getFlightPlanes().getFlightLeader().getLinkTrId()).toString());
        
        McuWaypoint reconMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_RECON);
        Assertions.assertTrue (reconMissionPoint != null);
        
        McuWaypoint egressMissionPoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        Assertions.assertTrue (egressMissionPoint != null);
        Assertions.assertEquals(
        		egressMissionPoint.getObjects().get(0), 
        		Integer.valueOf(flight.getFlightPlanes().getFlightLeader().getLinkTrId()).toString());

        PlaneRtbValidator.verifyPlaneRtbDisabled(mission);

        Assertions.assertEquals(FlightTypes.RECON, flight.getFlightType());        
        
        List<IMissionPointSet> missionPointSets = flight.getWaypointPackage().getMissionPointSets();
    	MissionPointPlayerReconSet reconSet = null;
    	McuWaypoint lastClimbWP = null;
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
        	if (missionPointSet instanceof MissionPointFlightBeginTakeoff)
        	{
        		MissionPointFlightBeginTakeoff climbSet = (MissionPointFlightBeginTakeoff)missionPointSet;
        		for (McuWaypoint waypoint : climbSet.getAllWaypoints())
        		{
        			if (waypoint.getWpAction() == WaypointAction.WP_ACTION_CLIMB)
        			{
        				lastClimbWP = waypoint;
        			}
        		}
        	}
 
        	
        	if (missionPointSet instanceof MissionPointPlayerReconSet)
        	{
               	
                Assertions.assertEquals(
                		lastClimbWP.getTargets().get(0), 
                		Integer.valueOf(ingressWaypoint.getIndex()).toString());

            	reconSet = (MissionPointPlayerReconSet)missionPointSet;

            	// Verify reference into recon WP list
        		PlayerReconWaypoint firstReconPlayerWaypoint = reconSet.getReconPlayerWaypoints().get(0);
                Assertions.assertEquals(
                		ingressWaypoint.getTargets().get(0), 
                		Integer.valueOf(firstReconPlayerWaypoint.getEntryTimer().getIndex()).toString());

        		// Verify reference out of recon WP list
                PlayerReconWaypoint lastReconPlayerWaypoint = reconSet.getReconPlayerWaypoints().get(reconSet.getReconPlayerWaypoints().size()-1);
                Assertions.assertEquals(
                		Integer.valueOf(egressMissionPoint.getIndex()).toString(), 
                		lastReconPlayerWaypoint.getExitTimer().getTargets().get(0));
        		
                // Verify internal recon WP construction
                PlayerReconWaypoint prevReconPlayerWaypoint = null;
                for (PlayerReconWaypoint reconPlayerWaypoint : reconSet.getReconPlayerWaypoints())
                {
                	// Entry timer connected to WP
                    Assertions.assertEquals(
                    		Integer.valueOf(reconPlayerWaypoint.getWaypoint().getIndex()).toString(),
                    		reconPlayerWaypoint.getEntryTimer().getTargets().get(0));
                    
                	// WP connected to Media
                    Assertions.assertEquals(
                    		Integer.valueOf(reconPlayerWaypoint.getPhotoMedia().getIndex()).toString(),
                    		reconPlayerWaypoint.getWaypoint().getTargets().get(0));
                    
                	// Media connected to Exit Timer
                    Assertions.assertEquals(
                    		reconPlayerWaypoint.getExitTimer().getIndex(),
                    		reconPlayerWaypoint.getPhotoMedia().getEventList().get(0).getTarId());
                    
                	if (prevReconPlayerWaypoint != null)
                	{               		
                		// Verify reference to next recon WP
                        Assertions.assertEquals(
                        		Integer.valueOf(reconPlayerWaypoint.getEntryTimer().getIndex()).toString(),
                        		prevReconPlayerWaypoint.getExitTimer().getTargets().get(0));
                	}
                	
            		// Verify object association to flight leader
                    Assertions.assertEquals(
                    		reconPlayerWaypoint.getWaypoint().getObjects().get(0), 
                    		Integer.valueOf(flight.getFlightPlanes().getFlightLeader().getLinkTrId()).toString());

                	prevReconPlayerWaypoint = reconPlayerWaypoint;
                }
        	}        	
        }
    	Assertions.assertNotNull(reconSet);
     }

}
