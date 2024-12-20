package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.EscortMcuSequence;

// Ingress -> Rendezvous WP -> Cover Timer -> Cover -> force Complete Timer -> Egress Wp
public class MissionPointEscortForPlayerWaypointSet extends MissionPointSetMultipleWaypointSet implements IMissionPointSet
{
    private EscortMcuSequence escortSequence;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;
    private McuTimer rendezvousCheckZoneTimer = null;
    private McuCheckZone rendezvousCheckZone = null;
    
    public MissionPointEscortForPlayerWaypointSet()
    {
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_ESCORT;
    }
    
    public void addWaypointBefore(McuWaypoint waypoint)
    {
        super.addWaypointBefore(waypoint);
    }
    
    public void addWaypointAfter(McuWaypoint waypoint)
    {
        super.addWaypointAfter(waypoint);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.setLinkToLastWaypoint(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return getEntryPointAtFirstWaypoint();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        List<MissionPoint> missionPoints = new ArrayList<>();
        
        List<MissionPoint> missionPointsBefore = super.getWaypointsBeforeAsMissionPoints();
        missionPoints.addAll(missionPointsBefore);
        
        MissionPoint coverPoint = new MissionPoint(escortSequence.getPosition(), WaypointAction.WP_ACTION_RENDEZVOUS);
        missionPoints.add(coverPoint);
        
        List<MissionPoint> missionPointsAfter = super.getWaypointsAfterAsMissionPoints();
        missionPoints.addAll(missionPointsAfter);

        return missionPoints;
    }
    

    private void createRendezvousCheckZone(IFlight escortedFlight) throws PWCGException 
    {                
        rendezvousCheckZone = new McuCheckZone("CheckZone Balloon Winch");
        rendezvousCheckZone.setZone(5000);
        rendezvousCheckZone.triggerCheckZoneByFlight(escortedFlight);

        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getWaypointPackage().getAllWaypoints(), 
                WaypointType.RENDEZVOUS_WAYPOINT.getName());

        rendezvousCheckZone.setDesc("Rendezvous Check Zone");
        rendezvousCheckZone.setPosition(rendezvousWP.getPosition().copy());
        
        // Make the winch down CZ Timer
        rendezvousCheckZoneTimer = new McuTimer();
        rendezvousCheckZoneTimer.setName("Rendezvous Check Zone Timer");
        rendezvousCheckZoneTimer.setDesc("Rendezvous Check Zone Timer");
        rendezvousCheckZoneTimer.setPosition(rendezvousWP.getPosition().copy());
    }

    @Override
    public void finalizeMissionPointSet(FlightPlanes flightPlanes) throws PWCGException
    {
        super.finalizeMissionPointSet(flightPlanes);
        linkEscortSequenceToWaypoints();
        escortSequence.finalize();
    }

    public void setEscortSequence(EscortMcuSequence coverSequence) throws PWCGException
    {
        this.escortSequence = coverSequence;
        createRendezvousCheckZone(escortSequence.getEscortedFlight());
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;        
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);
        rendezvousCheckZoneTimer.write(writer);
        rendezvousCheckZone.write(writer);
        escortSequence.write(writer);
    }

    private void linkEscortSequenceToWaypoints() throws PWCGException
    {
        McuWaypoint lastWaypointBefore = super.getLastWaypointBefore();
        lastWaypointBefore.setTarget(rendezvousCheckZoneTimer.getIndex());
        rendezvousCheckZoneTimer.setTimerTarget(rendezvousCheckZone.getIndex());
        
        McuWaypoint firstWaypointAfter = super.getFirstWaypointAfter();
        escortSequence.setLinkToNextTarget(firstWaypointAfter.getIndex());
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypointsBefore.getWaypoints());
        allFlightPoints.addAll(waypointsAfter.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }

    // test purposes
    public EscortMcuSequence getEscortSequence()
    {
        return escortSequence;
    }

    public McuTimer getRendezvousCheckZoneTimer()
    {
        return rendezvousCheckZoneTimer;
    }

    public McuCheckZone getRendezvousCheckZone()
    {
        return rendezvousCheckZone;
    }
    
    
}