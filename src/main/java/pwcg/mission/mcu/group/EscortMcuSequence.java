package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class EscortMcuSequence
{
    private McuCover cover = null;
    private McuTimer coverTimer  = null;
    private McuTimer forceCompleteTimer;
    private McuForceComplete forceComplete;
    private McuTimer escortCompleteTimer;
    
    private IFlight escortFlight;
    private IFlight escortedFlight;
    
    public EscortMcuSequence(IFlight escortedFlight, IFlight escortFlight) throws PWCGException
    {
        this.escortedFlight = escortedFlight;
        this.escortFlight = escortFlight;
    }
    
    public void createEscortSequence() throws PWCGException
    {
        createCover();
        createForceComplete();
    }
    
    public void finalize() throws PWCGException
    {
        createTargetAssociations();
    }

    public void createCover() throws PWCGException 
    {
        PlaneMcu flightLeader = escortFlight.getFlightPlanes().getFlightLeader();
        Coordinate rendevousPosition = getCoverPosition();
        
        cover  = new McuCover();
        cover.setPosition(rendevousPosition);
        cover.setObject(flightLeader.getLinkTrId());
        cover.setTarget(escortedFlight.getFlightPlanes().getFlightLeader().getLinkTrId());

        coverTimer  = new McuTimer();
        coverTimer.setName("Cover Timer");
        coverTimer.setDesc("Cover");
        coverTimer.setPosition(rendevousPosition);
    }

    private Coordinate getCoverPosition()
    {
        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getWaypointPackage().getAllWaypoints(), 
                WaypointType.RENDEZVOUS_WAYPOINT.getName());

        Coordinate coverPosition = rendezvousWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        return coverPosition;
    }

    private void createForceComplete() throws PWCGException
    {
        PlaneMcu flightLeader = escortFlight.getFlightPlanes().getFlightLeader();
        Coordinate rendevousPosition = getCoverPosition();

        forceCompleteTimer = new McuTimer();
        forceCompleteTimer.setName("Escort Cover Force Complete Timer");
        forceCompleteTimer.setDesc("Escort Cover Force Complete Timer");
        forceCompleteTimer.setOrientation(new Orientation());
        forceCompleteTimer.setPosition(rendevousPosition);
        forceCompleteTimer.setTime(1);

        int emergencyDropOrdnance = 0;
        forceComplete = new McuForceComplete(WaypointPriority.PRIORITY_HIGH, emergencyDropOrdnance);
        forceComplete.setName("Escort Cover Force Complete");
        forceComplete.setDesc("Escort Cover Force Complete");
        forceComplete.setOrientation(new Orientation());
        forceComplete.setPosition(rendevousPosition);
        forceComplete.setObject(flightLeader.getLinkTrId());

        escortCompleteTimer = new McuTimer();
        escortCompleteTimer.setName("Escort Complete Timer");
        escortCompleteTimer.setDesc("Escort Complete Timer");
        escortCompleteTimer.setOrientation(new Orientation());
        escortCompleteTimer.setPosition(rendevousPosition);
        escortCompleteTimer.setTime(1);
    }
    
    private void createTargetAssociations()
    {
        McuWaypoint rtbWP = WaypointGeneratorUtils.findWaypointByType(escortFlight.getWaypointPackage().getAllWaypoints(), 
                WaypointType.EGRESS_WAYPOINT.getName());

        coverTimer.setTarget(cover.getIndex());        
        
        forceCompleteTimer.setTarget(forceComplete.getIndex());
        forceCompleteTimer.setTarget(escortCompleteTimer.getIndex());
        escortCompleteTimer.setTarget(rtbWP.getIndex());
    }
    
    public void write(BufferedWriter writer) throws PWCGException 
    {
        coverTimer.write(writer);
        cover.write(writer);
        forceCompleteTimer.write(writer);
        forceComplete.write(writer);
    }

    public Coordinate getPosition()
    {
        return cover.getPosition().copy();
    }

    public int getCoverEntry()
    {
        return coverTimer.getIndex();
    }

    public void setLinkToNextTarget(int nextIndex)
    {
        forceCompleteTimer.setTarget(nextIndex);        
    }

    // These getters are for test purposes
    public McuCover getCover()
    {
        return cover;
    }

    public McuTimer getCoverTimer()
    {
        return coverTimer;
    }

    public McuTimer getForceCompleteTimer()
    {
        return forceCompleteTimer;
    }

    public McuForceComplete getForceComplete()
    {
        return forceComplete;
    }

    public McuTimer getEscortCompleteTimer()
    {
        return escortCompleteTimer;
    }

    public IFlight getEscortFlight()
    {
        return escortFlight;
    }

    public IFlight getEscortedFlight()
    {
        return escortedFlight;
    }
}
