package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointFlightActivate;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.BaseFlightMcu;
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
    public McuWaypoint getWaypointByAction(WaypointAction action) throws PWCGException
    {
        List<McuWaypoint> allWaypoints = this.getAllWaypoints();
        for (McuWaypoint waypoint : allWaypoints)
        {
            if (waypoint.getWpAction() == action)
            {
                return waypoint;
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
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            allFlightPoints.addAll(missionPointSet.getAllFlightPoints());
        }
        return allFlightPoints;
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
    public void finalize(PlaneMcu plane) throws PWCGException
    {
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
            if (!(missionPointSet instanceof MissionPointFlightActivate))
            {
                IMissionPointSet duplicateMissionPointSet = missionPointSet.duplicateWithOffset(flight.getFlightInformation(), positionInFormation);
                duplucateWaypointPackage.addMissionPointSet(duplicateMissionPointSet);
            }
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

    @Override
    public IMissionPointSet getMissionPointSet(MissionPointSetType missionPointSetType) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            if (missionPointSet.getMissionPointSetType() == missionPointSetType)
            {
                return missionPointSet;
            }
        }
        throw new PWCGException("No mission point set found for requested type " + missionPointSetType);
    }
}
