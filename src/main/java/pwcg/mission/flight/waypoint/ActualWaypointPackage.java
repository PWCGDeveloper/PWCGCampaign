package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class ActualWaypointPackage extends WaypointPackage
{
    protected McuTimer waypointTimer = null;

    /**
     * @param flight
     */
    public ActualWaypointPackage(Flight flight)
    {
        super(flight);
        createWaypointTimer();
    }
   

    /**
     * @
     */
    private void createWaypointTimer()
    {
        // Only unspawned air starts do this.  
        // flights are triggered from the virtual waypoints.getWaypoints().
        waypointTimer = new McuTimer();
        waypointTimer.setName(flight.getName() + ": WP Timer");     
        waypointTimer.setDesc("WP Timer for " + flight.getName());
        waypointTimer.setPosition(flight.getPosition());
        waypointTimer.setTimer(1);
    }
    

    /**
     * @see rof.campaign.mission.flight.waypoint.WaypointPackage#setWaypoints(java.util.List)
     */
    public void setWaypoints(List<McuWaypoint> waypoints)
    {
        super.setWaypoints(waypoints);
        setFirstWPTarget();
    }

    
    /**
     * 
     */
    private void setFirstWPTarget()
    {
        List<McuWaypoint> waypoints = getWaypointsForLeadPlane();
        if (waypoints != null)
        {
            if (waypoints.size() > 0)
            {
                waypointTimer.clearTargets();
                waypointTimer.setTarget(waypoints.get(0).getIndex());
            }
        }
    }


    /**
     * @param timerToLink
     */
    public BaseFlightMcu getEntryMcu()
    {
        return waypointTimer;
    }


    /**
     * @param index
     */
    public void onTriggerAddTarget(int index)
    {
        waypointTimer.setTarget(index);
    }

    
    /**
     * For player air starts on escort missions, move the escort and the player flight
     * @throws PWCGException 
     * 
     * @
     */
    public void movePlayerEscortFlightToBombApproach() throws PWCGException 
    {
        List<McuWaypoint> waypoints = getWaypointsForLeadPlane();
        if (waypoints.size() > 0)
        {
            // Find the rendezvous WP
            McuWaypoint rendevousPoint = null;
            McuWaypoint targetPoint = null;
            for (McuWaypoint waypoint : waypoints)
            {
                targetPoint = waypoint;
                if (waypoint.getName().equals(WaypointType.RECON_WAYPOINT.getName()))
                {
                    break;
                }
                
                rendevousPoint = waypoint;
            }

            // Start the flight at the rendezvous WP
            List<McuWaypoint> adjustedWaypoints = new  ArrayList<McuWaypoint>();
            boolean startAdding = false;
            for (McuWaypoint waypoint : waypoints)
            {
                if (waypoint.equals(rendevousPoint))
                {
                    startAdding = true;
                }
                
                if (startAdding)
                {
                    adjustedWaypoints.add(waypoint);
                }
            }

            waypoints = adjustedWaypoints;

            // Set the rendezvous WP between the target and the escort location
            Campaign campaign =     PWCGContextManager.getInstance().getCampaign();
            
            String airfieldName = flight.getSquadron().determineCurrentAirfieldName(campaign.getDate());
            IAirfield airfield =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
            Coordinate playerAirfieldPos = airfield.getPosition();
            
            double angle = MathUtils.calcAngle(playerAirfieldPos, targetPoint.getPosition());
            double distanceToTarget = MathUtils.calcDist(playerAirfieldPos, targetPoint.getPosition());
            distanceToTarget = distanceToTarget * 0.30;
            Coordinate rendevousCoordinate = MathUtils.calcNextCoord(playerAirfieldPos, angle, distanceToTarget);
            rendevousCoordinate.setYPos(targetPoint.getPosition().getYPos());
            
            rendevousPoint.setPosition(rendevousCoordinate);
            rendevousPoint.setName(WaypointType.RENDEZVOUS_WAYPOINT.getName());
            rendevousPoint.setWpAction(WaypointAction.WP_ACTION_RENDEVOUS);
            
            waypoints.remove(0);
            waypoints.add(0, rendevousPoint);
            
            McuWaypoint departWP = rendevousPoint.copy();
            departWP.setName(WaypointType.DEPART_WAYPOINT.getName());
            rendevousPoint.setWpAction(WaypointAction.WP_ACTION_EGRESS);
            waypoints.add(2, departWP);

            setFirstWPTarget();
        }               
    }


    /**
     * Write the mission to a file
     * 
     * @param writer
     * @throws PWCGIOException 
     * @
     */
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        waypointTimer.write(writer);
        super.write(writer);
    }

}
