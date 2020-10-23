package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointStartNextVwp 
{       
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer startNextWaypointTimer = new McuTimer();
    private McuTimer startnextWaypointTriggeredTimer = new McuTimer();
    private int index = IndexGenerator.getInstance().getNextIndex();

    public VirtualWaypointStartNextVwp(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }
    
    public void link(VirtualWaypointDeactivateThisVwp vwpDeactivateThisVwp)
    {
        startnextWaypointTriggeredTimer.setTarget(vwpDeactivateThisVwp.getEntryPoint());
    }

    public void linkToNextVwp(int nextVwpIndex)
    {
        startnextWaypointTriggeredTimer.setTarget(nextVwpIndex);
    }

    private void buildMcus()
    {
        startNextWaypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        startNextWaypointTimer.setName("VWP Start next VWP Timer");
        startNextWaypointTimer.setDesc("VWP Start next VWP Timer");
        startNextWaypointTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());

        startnextWaypointTriggeredTimer.setPosition(vwpCoordinate.getPosition().copy());
        startnextWaypointTriggeredTimer.setName("VWP Start next VWP Triggered Timer");
        startnextWaypointTriggeredTimer.setDesc("VWP Start next VWP Triggered Timer");
        startnextWaypointTriggeredTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());
    }

    public void addAdditionalTime(int additionalTime)
    {
        int nextVwpTimerTime = startNextWaypointTimer.getTimer();
        nextVwpTimerTime += additionalTime;
        startNextWaypointTimer.setTimer(nextVwpTimerTime);
    }

    private void setTargetAssociations() throws PWCGException
    {
        startNextWaypointTimer.setTarget(startnextWaypointTriggeredTimer.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Start Next VWP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Start Next VWP\";");
            writer.newLine();

            startNextWaypointTimer.write(writer);
            startnextWaypointTriggeredTimer.write(writer);
    
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    public int getEntryPoint()
    {
        return startNextWaypointTimer.getIndex();
    }

    public McuTimer getStartNextWaypointTriggeredTimer()
    {
        return startnextWaypointTriggeredTimer;
    }

    public McuTimer getStartNextWaypointTimer()
    {
        return startNextWaypointTimer;
    }    
}
