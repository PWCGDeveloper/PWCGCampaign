package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPackage implements IWaypointPackage
{
    private  Map<Integer, IMissionPointSet> missionPointSets = new TreeMap<>();
    
    private IFlight flight = null;

    public WaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints() throws PWCGException
    {
        List<MissionPoint> waypointsCoordinates = new ArrayList<>();
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            waypointsCoordinates.addAll(missionPointSet.getFlightMissionPoints());
        }
        return waypointsCoordinates;
    }

    @Override
    public MissionPoint getMissionPointByAction(WaypointAction action) throws PWCGException
    {
        List<MissionPoint> allMissionPoints = getFlightMissionPoints();
        for (MissionPoint missionPoint : allMissionPoints)
        {
            if (missionPoint.getAction() == action)
            {
                return missionPoint;
            }
        }
        return null;
    }

    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            allWaypoints.addAll(missionPointSet.getAllWaypoints());
        }
        return allWaypoints;
    }

    @Override
    public void updateWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException
    {
        removeUnwantedWaypoints(waypointsInBriefing);
        replaceWaypoints(waypointsInBriefing);
        addNewWaypoints(waypointsInBriefing);
    }

    @Override
    public void addMissionPointSet(MissionPointSetType missionPointSetType, IMissionPointSet missionPointSet)
    {
        missionPointSets.put(missionPointSetType.getMissionPointSetTypeOrder(), missionPointSet);
    }

    @Override
    public void finalize() throws PWCGException
    {
        PlaneMcu plane = flight.getFlightData().getFlightPlanes().getFlightLeader();
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            missionPointSet.finalize(plane);
        }
        linkMissionPointSets();
    }
    
    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            missionPointSet.write(writer);
        }
    }

    private void linkMissionPointSets() throws PWCGException
    {
        IMissionPointSet previousMissionPointSet = null;
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            if (previousMissionPointSet != null)
            {
                if (previousMissionPointSet.isLinkToNextTarget())
                {
                    previousMissionPointSet.setLinkToNextTarget(missionPointSet.getEntryPoint());
                }
            }
            previousMissionPointSet = missionPointSet;
        }
    }

    private void removeUnwantedWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            missionPointSet.removeUnwantedWaypoints(waypointsInBriefing);
        }        
    }

    private void replaceWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException
    {
        for (McuWaypoint waypoint : waypointsInBriefing)
        {
            IMissionPointSet targetMissionPointSet = findWaypoint(waypoint.getWaypointID());
            if (targetMissionPointSet != null)
            {
                targetMissionPointSet.replaceWaypoint(waypoint);
            }
        }        
    }

    private void addNewWaypoints(List<McuWaypoint> waypointsInBriefing) throws PWCGException
    {
        McuWaypoint previousWaypoint = null;
        for (McuWaypoint waypoint : waypointsInBriefing)
        {
            IMissionPointSet targetMissionPointSet = findWaypoint(waypoint.getWaypointID());
            if (targetMissionPointSet == null)
            {
                IMissionPointSet targetMissionPointSetToAdd = findWaypoint(previousWaypoint.getWaypointID());
                targetMissionPointSetToAdd.addWaypointAfterWaypoint(waypoint, previousWaypoint.getWaypointID());
            }
            
            previousWaypoint = waypoint;
        }
        
    }
    
    private IMissionPointSet findWaypoint(long waypointId)
    {
        for (IMissionPointSet missionPointSet : missionPointSets.values())
        {
            if (missionPointSet.containsWaypoint(waypointId))
            {
                return missionPointSet;
            }
        }
        return null;
    }
    
    
    
    
    
    
    
    
    
    /*
    
    
    
    
    @Override
    public McuWaypoint getWaypointByType(WaypointType waypointType) throws PWCGException
    {
        for (McuWaypoint waypoint : getMissionSetsForLeadPlane())
        {
            if (waypoint.getWaypointType() == waypointType)
            {
                return waypoint;
            }
        }
        
        throw new PWCGException("Waypoint of expected type not found : " + waypointType);
    }

    @Override
    public List<McuWaypoint> getWaypointsForPlane(PlaneMCU plane)
    {
        return flightWaypointsByPlane.get(plane.getIndex());
    }

    @Override
    public List<McuWaypoint> getWaypointsForPlaneId(Integer planeId)
    {
        return flightWaypointsByPlane.get(planeId);
    }

    @Override
    public Map<Integer, List<McuWaypoint>> getAllWaypointsSets()
    {
        return flightWaypointsByPlane;
    }
    
    @Override
    public McuWaypoint getWaypointByActionForLeadPlane(WaypointAction requestedAction)
    {
        for (McuWaypoint waypoint : getMissionSetsForLeadPlane())
        {
            if (waypoint.getWpAction() == requestedAction)
            {
                return waypoint;
            }
        }
        
        return null;
    }

    @Override
    public McuWaypoint getWaypointByActionForLeadPlaneWithFailure(WaypointAction requestedAction) throws PWCGException
    {
        McuWaypoint rendezvousWP = getWaypointByActionForLeadPlane(requestedAction);
        if (rendezvousWP == null)
        {
            throw new PWCGException("No waypoint found for action: " + requestedAction);
        }
        return rendezvousWP;
    }

    @Override
    public void duplicateWaypointsForFlight(IFlight flight) throws PWCGException
    {
        resetFollowingPlaneWaypoints(flight);        
        List<McuWaypoint> waypoints = flightWaypointsByPlane.get(flight.getFlightData().getFlightPlanes().getFlightLeader().getIndex());        
        for (McuWaypoint waypoint : waypoints)
        {
            resetFollowingPlanePositionForWaypoint(flight, waypoint);
        }
    }

    private void resetFollowingPlaneWaypoints(IFlight flight)
    {
        for (PlaneMCU plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {
            List<McuWaypoint> planeWayPoints = new ArrayList<McuWaypoint>();
            flightWaypointsByPlane.put(plane.getIndex(), planeWayPoints);
        }
    }
    
    private void resetFollowingPlanePositionForWaypoint(IFlight flight, McuWaypoint waypoint) throws PWCGException
    {
        FormationGenerator formationGenerator = new FormationGenerator();
        List<Coordinate> planePositionsAtWP = formationGenerator.createPlaneInitialPosition(
                        flight.getFlightData().getFlightPlanes(), 
                        waypoint.getPosition().copy(),
                        waypoint.getOrientation().copy()); 

        int planeIndex = 0;
        for (PlaneMCU plane : flight.getFlightData().getFlightPlanes().getPlanes())
        {            
            // Create a new WP for this plane
            List<McuWaypoint> planeWayPoints = flightWaypointsByPlane.get(plane.getIndex());

            // The WP is the same as the original with a slightly different position
            McuWaypoint planeWaypoint = waypoint.copy();
            planeWaypoint.setPosition(planePositionsAtWP.get(planeIndex));
            
            // Offset WP location based on formation generator
            planeWaypoint.setPosition(planePositionsAtWP.get(planeIndex).copy());
            
            // Add the WP to this plane's WP set
            planeWayPoints.add(planeWaypoint);
            
            ++planeIndex;
        }
    }

    private void setWaypointOrientation() throws PWCGException 
    {
        // TL each waypoint to the next one
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : flight.getFlightData().getWaypointPackage().getMissionSetsForLeadPlane())
        {
            if (prevWP != null)
            {
                Coordinate prevPos = prevWP.getPosition().copy();
                Coordinate nextPos = nextWP.getPosition().copy();
    
                Orientation orient = new Orientation();
                orient.setyOri(MathUtils.calcAngle(prevPos, nextPos));
    
                prevWP.setOrientation(orient);
            }
            
            prevWP = nextWP;
        }
    }

    @Override
    public void linkWPToPlane(PlaneMCU plane, List<McuWaypoint>waypointsToLink)
    {
        for (BaseFlightMcu mcu : waypointsToLink)
        {
            mcu.clearObjects();
            mcu.setObject(plane.getLinkTrId());
        }
    }

    @Override
    public List<McuWaypoint> getAllFlightWaypoints()
    {
        return getAllWaypointsForPlane(flight.getFlightData().getFlightPlanes().getFlightLeader());
    }

    private List<McuWaypoint> getAllWaypointsForPlane(PlaneMCU plane)
    {
        List<McuWaypoint> returnWaypoints = getWaypointsForPlane(plane);
        if (getMissionSetsForLeadPlane().size() == 0)
        {
            for (IFlight linkedFlight : flight.getFlightData().getLinkedFlights().getLinkedFlights())
            {
                returnWaypoints = linkedFlight.getFlightData().getWaypointPackage().getWaypointsForPlane(plane);
                if (returnWaypoints != null && returnWaypoints.size() > 0)
                {
                    return returnWaypoints;
                }
            }
        }

        return returnWaypoints;
    }

    @Override
    public double calcFlightDistance() 
    {
        double flightWPDistance = 0.0;
        int i = 0;
        McuWaypoint prevWP = null;
        for (McuWaypoint wp : getAllFlightWaypoints())
        {
            if (i == 0)
            {
                prevWP = wp;
                ++i;
                continue;
            }

            flightWPDistance += MathUtils.calcDist(prevWP.getPosition(), wp.getPosition());
            prevWP = wp;
            ++i;
        }

        return flightWPDistance;
    }

    @Override
    public McuWaypoint findFirstStartWaypoint()
    {
        List<McuWaypoint> waypoints = getMissionSetsForLeadPlane();
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_INGRESS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_START) ||
                waypoint.isTargetWaypoint())
            {
                return waypoint;
            }
        }

        return waypoints.get(0);
     }

    @Override
    public List<Coordinate> getAllMissionCoordinates()
    {
        List<Coordinate> allMissionPointsForPlane = new ArrayList<>();
        allMissionPointsForPlane.add(flight.getFlightData().getFlightPlanes().getFlightLeader().getPosition());
        
        List<McuWaypoint> allWaypoints = this.getAllWaypointsForPlane(flight.getFlightData().getFlightPlanes().getFlightLeader());
        for (McuWaypoint waypoint : allWaypoints)
        {
            allMissionPointsForPlane.add(waypoint.getPosition());
        }
        
        return allMissionPointsForPlane;
    }

*/
}
