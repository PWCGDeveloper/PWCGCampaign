package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointPackage implements IWaypointPackage
{
    private  List<IMissionPointSet> missionPointSets = new ArrayList<>();
    
    private IFlight flight = null;

    public WaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints() throws PWCGException
    {
        List<MissionPoint> waypointsCoordinates = new ArrayList<>();
        for (IMissionPointSet missionPointSet : missionPointSets)
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
        for (IMissionPointSet missionPointSet : missionPointSets)
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
    public void addMissionPointSet(IMissionPointSet missionPointSet)
    {
        missionPointSets.add(missionPointSet);
    }

    @Override
    public void finalize() throws PWCGException
    {
        PlaneMcu plane = flight.getFlightData().getFlightPlanes().getFlightLeader();
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            missionPointSet.finalize(plane);
        }
        linkMissionPointSets();
    }

    @Override
    public IWaypointPackage duplicate(int positionInFormation) throws PWCGException
    {
        WaypointPackage duplucateWaypointPackage = new WaypointPackage(flight);
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            IMissionPointSet duplicateMissionPointSet = missionPointSet.duplicateWithOffset(flight.getFlightData().getFlightInformation(), positionInFormation);
            duplucateWaypointPackage.addMissionPointSet(duplicateMissionPointSet);
        }
        return duplucateWaypointPackage;
    }
    
    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            missionPointSet.write(writer);
        }
    }

    private void linkMissionPointSets() throws PWCGException
    {
        IMissionPointSet previousMissionPointSet = null;
        for (IMissionPointSet missionPointSet : missionPointSets)
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
        for (IMissionPointSet missionPointSet : missionPointSets)
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
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            if (missionPointSet.containsWaypoint(waypointId))
            {
                return missionPointSet;
            }
        }
        return null;
    }
}
