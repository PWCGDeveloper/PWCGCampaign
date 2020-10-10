package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointKillFuture
{
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointDeletePlanes vwpDeletePlanes;

    private McuTimer upstreamKillTimer = new McuTimer();
    
    public VirtualWaypointKillFuture(VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointDeletePlanes vwpDeletePlanes)
    {
        this.vwpCoordinate = vwpCoordinate; 
        this.vwpDeletePlanes = vwpDeletePlanes; 
    }

    public void build() throws PWCGException
    {
        buildMcus();
        createTargetAssociations();
    }

    private void buildMcus()
    {
        upstreamKillTimer.setTimer(1);
        upstreamKillTimer.setPosition(vwpCoordinate.getPosition().copy());
        upstreamKillTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        upstreamKillTimer.setName("Upstream Kill Timer");
        upstreamKillTimer.setDesc("Upstream Kill Timer");
    }

    private void createTargetAssociations()
    {
        upstreamKillTimer.setTarget(vwpDeletePlanes.getEntryPoint());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        upstreamKillTimer.write(writer);
    }
    
    public int getEntryPoint()
    {
        return upstreamKillTimer.getIndex();
    }
    
    public void linkToNextVwpKill(int target)
    {
        upstreamKillTimer.setTarget(target);
    }
}
