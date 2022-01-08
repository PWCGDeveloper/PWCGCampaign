package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.plot.FlightPathToWaypointPlotter;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointAttackSet;
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
    public List<MissionPoint> getMissionPoints() throws PWCGException
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
        List<MissionPoint> allMissionPoints = getMissionPoints();
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
    public List<McuWaypoint> getTargetWaypoints()
    {
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        for (McuWaypoint waypoint : getAllWaypoints())
        {
            if (waypoint.isTargetWaypoint())
            {
                targetWaypoints.add(waypoint);
            }
        }
        return targetWaypoints;
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
    public void setAttackToTriggerOnPlane(List<PlaneMcu> planes) throws PWCGException
    {
        MissionPointAttackSet attackMissionPoint = (MissionPointAttackSet)getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ATTACK);
        if (attackMissionPoint != null)
        {
            attackMissionPoint.getAttackSequence().setAttackToTriggerOnPlane(planes);
        }
    }


    @Override
    public void updateWaypointsFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        removeUnwantedWaypoints(briefingMapPoints);
        modifyWaypointsFromBriefing(briefingMapPoints);
        addBriefingMapPoints(briefingMapPoints);
    }

    @Override
    public void addMissionPointSet(IMissionPointSet missionPointSet)
    {
        missionPointSets.add(missionPointSet);
    }

    @Override
    public void clearMissionPointSet()
    {
        missionPointSets.clear();
    }

    @Override
    public void finalize(FlightPlanes flightPlanes) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            missionPointSet.finalizeMissionPointSet(flightPlanes);
        }
        linkMissionPointSets();
    }
    
    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            missionPointSet.write(writer);
        }
    }

    @Override
    public int secondsUntilWaypoint(WaypointAction action) throws PWCGException
    {
        FlightPathToWaypointPlotter plotter = new FlightPathToWaypointPlotter(flight);
        return plotter.plotTimeToWaypointAction(action);
    }

    @Override
    public double getDistanceStartToTarget() throws PWCGException
    {
        double distanceToTarget = 0.0;
        McuWaypoint previousWaypoint = null;
        for (McuWaypoint waypoint : getAllWaypoints())
        {
            double distanceBetweenWaypoints = MathUtils.calcAngle(previousWaypoint.getPosition(), waypoint.getPosition());
            distanceToTarget += distanceBetweenWaypoints;
            if (previousWaypoint != null)
            {
                if (waypoint.isTargetWaypoint())
                {
                    return distanceToTarget;
                }
            }
            previousWaypoint = waypoint;
        }
        
        throw new PWCGException("Malformed flight.  No target waypoint");
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

    private void removeUnwantedWaypoints(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        for (IMissionPointSet missionPointSet : missionPointSets)
        {
            missionPointSet.removeUnwantedWaypoints(briefingMapPoints);
        }        
    }

    private void modifyWaypointsFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        for (BriefingMapPoint briefingMapPoint : briefingMapPoints)
        {
            IMissionPointSet targetMissionPointSet = findWaypoint(briefingMapPoint.getWaypointID());
            if (targetMissionPointSet != null)
            {
                targetMissionPointSet.updateWaypointFromBriefing(briefingMapPoint);
            }
        }
    }

    private void addBriefingMapPoints(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        BriefingMapPoint previousMapPointFromBriefing = null;
        for (BriefingMapPoint briefingMapPoint : briefingMapPoints)
        {
            if (briefingMapPoint.isWaypoint() && previousMapPointFromBriefing != null)
            {
                IMissionPointSet targetMissionPointSet = findWaypoint(briefingMapPoint.getWaypointID());
                if (targetMissionPointSet == null)
                {
                    IMissionPointSet targetMissionPointSetToAdd = findWaypoint(previousMapPointFromBriefing.getWaypointID());
                    long waypointIdForAddedWP = targetMissionPointSetToAdd.addWaypointFromBriefing(briefingMapPoint, previousMapPointFromBriefing.getWaypointID());
                    briefingMapPoint.setWaypointID(waypointIdForAddedWP);
                }
            }
            previousMapPointFromBriefing = briefingMapPoint;
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

    @Override
    public void addObjectToAllMissionPoints(PlaneMcu leadPlane)
    {
        for (IMissionPointSet missionPointSet : missionPointSets) 
        {
            for (BaseFlightMcu mcu : missionPointSet.getAllFlightPoints()) 
            {
                mcu.setObject(leadPlane.getLinkTrId());
            }
        }
    }
}
