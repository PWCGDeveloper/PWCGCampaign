package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class WingmanMcuGroup
{
    private int index = IndexGenerator.getInstance().getNextIndex();;

    private IFlight flight;
    private PlaneMcu coveringPlane;
    private PlaneMcu planeToCover;
    private McuTimer coverTimer = new McuTimer();
    private McuCover cover = null;
    private McuTimer finishCoverTimer;
    private McuForceComplete forceCompleteFinishCover;

    public WingmanMcuGroup(IFlight flight, PlaneMcu coveringPlane, PlaneMcu planeToCover)
    {
        index = IndexGenerator.getInstance().getNextIndex();
        
        this.flight = flight;
        this.coveringPlane = coveringPlane;
        this.planeToCover = planeToCover;
    }

    public void buildWingmanCover() throws PWCGException 
    {
        createCover();
        createForceComplete();
    }

    private void createCover() throws PWCGException 
    {
        McuWaypoint coverStartWaypoint = getCoverStartWaypoint();
    
        cover  = new McuCover();
        cover.setPosition(coverStartWaypoint.getPosition());
        cover.setObject(coveringPlane.getLinkTrId());
        cover.setTarget(planeToCover.getLinkTrId());

        coverTimer  = new McuTimer();
        coverTimer.setName("Cover Timer");
        coverTimer.setDesc("Cover");
        coverTimer.setPosition(coverStartWaypoint.getPosition());
        coverTimer.setTime(1);
        
        coverStartWaypoint.setTarget(coverTimer.getIndex());
        coverTimer.setTarget(cover.getIndex());
    }
    
    private McuWaypoint getCoverStartWaypoint() throws PWCGException
    {
        McuWaypoint coverStartWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_CLIMB);
        if (coverStartWaypoint == null)
        {
            coverStartWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_INGRESS);
        }
            
        return coverStartWaypoint;
    }

    private void createForceComplete() throws PWCGException
    {
        McuWaypoint coverFinishWaypoint = getCoverFinishWaypoint();

        finishCoverTimer = new McuTimer();
        finishCoverTimer.setName("Escort Cover Force Complete Timer");
        finishCoverTimer.setDesc("Escort Cover Force Complete Timer");
        finishCoverTimer.setOrientation(new Orientation());
        finishCoverTimer.setPosition(coverFinishWaypoint.getPosition());
        finishCoverTimer.setTime(1);

        int emergencyDropOrdnance = 0;
        forceCompleteFinishCover = new McuForceComplete(WaypointPriority.PRIORITY_HIGH, emergencyDropOrdnance);
        forceCompleteFinishCover.setName("Escort Cover Force Complete");
        forceCompleteFinishCover.setDesc("Escort Cover Force Complete");
        forceCompleteFinishCover.setOrientation(new Orientation());
        forceCompleteFinishCover.setPosition(coverFinishWaypoint.getPosition());
        forceCompleteFinishCover.setObject(coveringPlane.getLinkTrId());
        
        coverFinishWaypoint.setTarget(finishCoverTimer.getIndex());
        finishCoverTimer.setTarget(forceCompleteFinishCover.getIndex());
    }
    
    private McuWaypoint getCoverFinishWaypoint() throws PWCGException
    {
        McuWaypoint coverFinishWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_LANDING_APPROACH);
        if (coverFinishWaypoint == null)
        {
            coverFinishWaypoint = flight.getWaypointPackage().getWaypointByAction(WaypointAction.WP_ACTION_EGRESS);
        }
            
        return coverFinishWaypoint;
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"Wingman\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Wingman\";");
            writer.newLine();

            coverTimer.write(writer);
            cover.write(writer);
            finishCoverTimer.write(writer);
            forceCompleteFinishCover.write(writer);
            
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}
