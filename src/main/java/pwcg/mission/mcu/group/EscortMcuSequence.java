package pwcg.mission.mcu.group;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
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
    
    private IFlight escortFlight;
    private IFlight escortedFlight;
    
    public EscortMcuSequence(IFlight escortedFlight, IFlight escortFlight) throws PWCGException
    {
        this.escortedFlight = escortedFlight;
        this.escortFlight = escortFlight;
    }
    
    public void createPointDefenseSequence() throws PWCGException
    {
        createCover();
        createForceComplete();
        createTargetAssociations();
    }

    public void createCover() throws PWCGException 
    {
        PlaneMcu flightLeader = escortFlight.getFlightData().getFlightPlanes().getFlightLeader();
        Coordinate rendevousPosition = getCoverPosition();
        
        cover  = new McuCover();
        cover.setPosition(rendevousPosition);
        cover.setObject(flightLeader.getEntity().getIndex());
        cover.setTarget(escortedFlight.getFlightData().getFlightPlanes().getFlightLeader().getEntity().getIndex());

        coverTimer  = new McuTimer();
        coverTimer.setName("Cover Timer");
        coverTimer.setDesc("Cover");
        coverTimer.setPosition(rendevousPosition);
    }

    private Coordinate getCoverPosition()
    {
        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        Coordinate coverPosition = rendezvousWP.getPosition().copy();
        coverPosition.setYPos(coverPosition.getYPos() + 400);
        return coverPosition;
    }

    private void createForceComplete() throws PWCGException
    {
        PlaneMcu flightLeader = escortFlight.getFlightData().getFlightPlanes().getFlightLeader();
        Coordinate rendevousPosition = getCoverPosition();

        forceCompleteTimer = new McuTimer();
        forceCompleteTimer.setName("Escort Cover Force Complete Timer");
        forceCompleteTimer.setDesc("Escort Cover Force Complete Timer");
        forceCompleteTimer.setOrientation(new Orientation());
        forceCompleteTimer.setPosition(rendevousPosition);
        forceCompleteTimer.setTimer(1);

        forceComplete = new McuForceComplete();
        forceComplete.setName("Escort Cover Force Complete");
        forceComplete.setDesc("Escort Cover Force Complete");
        forceComplete.setOrientation(new Orientation());
        forceComplete.setPosition(rendevousPosition);
        forceComplete.setObject(flightLeader.getEntity().getIndex());
    }
    
    private void createTargetAssociations()
    {
        McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                WaypointType.INGRESS_WAYPOINT.getName());

        McuWaypoint partingWP = WaypointGeneratorUtils.findWaypointByType(escortedFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                WaypointType.EGRESS_WAYPOINT.getName());

        McuWaypoint rtbWP = WaypointGeneratorUtils.findWaypointByType(escortFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                WaypointType.RETURN_TO_BASE_WAYPOINT.getName());

        rendezvousWP.setTarget(coverTimer.getIndex());
        coverTimer.setTarget(cover.getIndex());        
        
        partingWP.setTarget(forceCompleteTimer.getIndex());
        forceCompleteTimer.setTarget(forceComplete.getIndex());
        forceCompleteTimer.setTarget(rtbWP.getIndex());
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
}
