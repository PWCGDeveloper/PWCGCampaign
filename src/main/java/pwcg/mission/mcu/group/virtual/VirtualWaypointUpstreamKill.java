package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointUpstreamKill
{
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer upstreamKillTimer = new McuTimer();
    private int index = IndexGenerator.getInstance().getNextIndex();

    public VirtualWaypointUpstreamKill(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException
    {
        buildMcus();
    }

    private void buildMcus()
    {
        upstreamKillTimer.setTimer(1);
        upstreamKillTimer.setPosition(vwpCoordinate.getPosition().copy());
        upstreamKillTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        upstreamKillTimer.setName("Upstream Kill Timer");
        upstreamKillTimer.setDesc("Upstream Kill Timer");
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Upstream Kill\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Upstream Kill\";");
            writer.newLine();

            upstreamKillTimer.write(writer);
    
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
        return upstreamKillTimer.getIndex();
    }
    
    
    public void link(VirtualWaypointDeletePlanes vwpDeletePlanes)
    {
    }

    public void linkToNextVwpKill(VirtualWaypointUpstreamKill nextVwpUpstreamKill, VirtualWaypointDeletePlanes nextVwpDeletePlanes)
    {
        upstreamKillTimer.setTarget(nextVwpUpstreamKill.getEntryPoint());
        upstreamKillTimer.setTarget(nextVwpDeletePlanes.getEntryPoint());
    }

    public McuTimer getUpstreamKillTimer()
    {
        return upstreamKillTimer;
    }
}
