package pwcg.mission.flight.waypoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointDuplicator
{
    private Map<Integer, Map<Integer, IMissionPointSet>> missionPointSets = new HashMap<>();
    
    private IFlight flight = null;

    public WaypointDuplicator(IFlight flight)
    {
        this.flight = flight;
    }
    
    public void addMissionPointSet(MissionPointSetType missionPointSetType, IMissionPointSet missionPointSet)
    {
        if (flight.getFlightData().getFlightInformation().isVirtual())
        {
            for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
            {
                addMissionPointSetForPlane(plane.getLinkTrId(), missionPointSetType, missionPointSet);
            }
        }
        else
        {
            int leadPlaneIndex = flight.getFlightData().getFlightPlanes().getFlightLeader().getIndex();
            addMissionPointSetForPlane(leadPlaneIndex, missionPointSetType, missionPointSet);
        }
    }

    private void addMissionPointSetForPlane(int planeIndex, MissionPointSetType missionPointSetType, IMissionPointSet missionPointSet)
    {
        if (!missionPointSets.containsKey(planeIndex))
        {
            Map<Integer, IMissionPointSet> missionSetForPlane = new TreeMap<>();
            missionPointSets.put(planeIndex, missionSetForPlane);
        }
        Map<Integer, IMissionPointSet> missionSetForLeadPlane = missionPointSets.get(planeIndex);
        missionSetForLeadPlane.put(missionPointSetType.getMissionPointSetTypeOrder(), missionPointSet.copy());
    }

    public void finalize() throws PWCGException
    {
        Map<Integer, IMissionPointSet> missionSetsForLeadPlane = getMissionSetsForLeadPlane();
        for (IMissionPointSet missionPointSet : missionSetsForLeadPlane.values())
        {
            missionPointSet.finalize();
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        Map<Integer, IMissionPointSet> missionSetsForLeadPlane = getMissionSetsForLeadPlane();
        for (IMissionPointSet missionPointSet : missionSetsForLeadPlane.values())
        {
            missionPointSet.write(writer);
        }
    }

    private Map<Integer, IMissionPointSet> getMissionSetsForLeadPlane()
    {
        int leadPlaneIndex = flight.getFlightData().getFlightPlanes().getFlightLeader().getIndex();
        return missionPointSets.get(leadPlaneIndex);
    }
}
