package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointDeactivateNextVwp 
{       
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer deactivateNextVwpTimer = new McuTimer();
    private McuDeactivate startNextVwpTimerDeactivate;    
    private int index = IndexGenerator.getInstance().getNextIndex();

    public VirtualWaypointDeactivateNextVwp(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }
    
    public void link(VirtualWaypointStartNextVwp vwpNextVwpStart, VirtualWaypointUpstreamKill vwpUpstreamKill)
    {
        startNextVwpTimerDeactivate.setTarget(vwpNextVwpStart.getEntryPoint());
        deactivateNextVwpTimer.setTarget(vwpUpstreamKill.getEntryPoint());
    }

    private void buildMcus()
    {
        startNextVwpTimerDeactivate = new McuDeactivate();
        startNextVwpTimerDeactivate.setPosition(vwpCoordinate.getPosition().copy());
        startNextVwpTimerDeactivate.setName("VWP Deactivate Next VWP");
        startNextVwpTimerDeactivate.setDesc("VWP Deactivate Next VWP");

        deactivateNextVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        deactivateNextVwpTimer.setName("VWP Deactivate Next VWP Timer");
        deactivateNextVwpTimer.setDesc("VWP Deactivate Next VWP Timer");
        deactivateNextVwpTimer.setTimer(0);
    }

    private void setTargetAssociations() throws PWCGException
    {
        deactivateNextVwpTimer.setTarget(startNextVwpTimerDeactivate.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Deactivate Next VWP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Deactivate Next VWP\";");
            writer.newLine();
    
            deactivateNextVwpTimer.write(writer);
            startNextVwpTimerDeactivate.write(writer);
    
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
        return deactivateNextVwpTimer.getIndex();
    }

    public McuTimer getDeactivateNextVwpTimer()
    {
        return deactivateNextVwpTimer;
    }

    public McuDeactivate getStartNextVwpTimerDeactivate()
    {
        return startNextVwpTimerDeactivate;
    }
    
    
}
