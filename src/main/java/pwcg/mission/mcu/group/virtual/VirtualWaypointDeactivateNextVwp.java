package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointDeactivateNextVwp 
{       
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer deactivateNextVwpTimer = new McuTimer();
    private McuDeactivate startNextVwpTimerDeactivate;    

    public VirtualWaypointDeactivateNextVwp(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }
    
    public void link(VirtualWaypointStartNextVwp vwpNextVwpStart)
    {
        startNextVwpTimerDeactivate.setTarget(vwpNextVwpStart.getEntryPoint());
    }

    private void buildMcus()
    {
        startNextVwpTimerDeactivate = new McuDeactivate();
        startNextVwpTimerDeactivate.setPosition(vwpCoordinate.getPosition().copy());
        startNextVwpTimerDeactivate.setName("Start Next VWP Timer Deactivate");
        startNextVwpTimerDeactivate.setDesc("Start Next VWP Timer Deactivate");

        deactivateNextVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        deactivateNextVwpTimer.setName("VWP Deactivate Next VWP Timer");
        deactivateNextVwpTimer.setDesc("VWP Deactivate Next VWP Timer");
        deactivateNextVwpTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());
    }

    private void setTargetAssociations() throws PWCGException
    {
        deactivateNextVwpTimer.setTarget(startNextVwpTimerDeactivate.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        deactivateNextVwpTimer.write(writer);
        startNextVwpTimerDeactivate.write(writer);
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
