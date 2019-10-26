package pwcg.mission.flight;

import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.mcu.group.IPlaneRemover;
import pwcg.mission.mcu.group.PlaneRemoverCoop;
import pwcg.mission.mcu.group.PlaneRemoverSinglePlayer;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class PlaneRemoverBuilder
{
    private Flight flight;
    
    PlaneRemoverBuilder(Flight flight)
    {
        this.flight = flight;
    }
    
    public void buildPlaneRemover(ConfigManagerCampaign configManager) throws PWCGException
    {
        if (!(configManager.getIntConfigParam(ConfigItemKeys.UsePlaneDeleteKey) == 0))
        {
            for (int i = 0; i < flight.getPlanes().size(); ++i)
            {
                PlaneMCU plane = flight.getPlanes().get(i);
                if (!plane.getPilot().isPlayer())
                {
                    PlaneMCU playerPlane = flight.getMission().getMissionFlightBuilder().getPlayerFlights().get(0).getPlayerPlanes().get(0);
                    createPlaneRemover(flight, plane, playerPlane);
                }
                
                for (VirtualWayPoint virtualWaypoint : ((VirtualWaypointPackage) flight.getWaypointPackage()).getVirtualWaypoints())
                {
                    // For virtual WP flights, enable the plane remover when
                    // the flight becomes active.
                    // Only one virtual WP will ever trigger, so the plane
                    // remover will be instantiated only once.
                    IPlaneRemover planeRemover = plane.getPlaneRemover();
                    if (planeRemover != null)
                    {
                        virtualWaypoint.onTriggerAddTarget(plane, planeRemover.getEntryPoint().getIndex());
                    }
                }
            }
        }
    }

    public void createPlaneRemover (Flight flight, PlaneMCU plane, PlaneMCU playerPlane) throws PWCGException 
    {
        if (flight.getCampaign().isCoop())
        {
            IPlaneRemover planeRemover = new PlaneRemoverCoop();
            planeRemover.initialize(flight, plane, playerPlane);
        }
        else
        {
            IPlaneRemover planeRemover =  new PlaneRemoverSinglePlayer();
            planeRemover.initialize(flight, plane, playerPlane);
        }
    }
}
