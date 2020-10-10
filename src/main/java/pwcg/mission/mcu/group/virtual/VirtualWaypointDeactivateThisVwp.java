package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointDeactivateThisVwp 
{       
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer deactivateThisCheckZoneTimer = new McuTimer();
    private McuDeactivate thisCheckZoneDeactivate;    

    public VirtualWaypointDeactivateThisVwp(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }
    
    public void link(VirtualWaypointCheckZone vwpCheckZone)
    {
        thisCheckZoneDeactivate.setTarget(vwpCheckZone.getCheckZone().getDeactivateEntryPoint());
    }

    private void buildMcus()
    {
        deactivateThisCheckZoneTimer.setPosition(vwpCoordinate.getPosition().copy());
        deactivateThisCheckZoneTimer.setName("VWP Deactivate Next VWP Timer");
        deactivateThisCheckZoneTimer.setDesc("VWP Deactivate Next VWP Timer");
        deactivateThisCheckZoneTimer.setTimer(0);

        thisCheckZoneDeactivate = new McuDeactivate();
        thisCheckZoneDeactivate.setPosition(vwpCoordinate.getPosition().copy());
        thisCheckZoneDeactivate.setName("Start Next VWP Timer Deactivate");
        thisCheckZoneDeactivate.setDesc("Start Next VWP Timer Deactivate");
    }

    private void setTargetAssociations() throws PWCGException
    {
        deactivateThisCheckZoneTimer.setTarget(thisCheckZoneDeactivate.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        deactivateThisCheckZoneTimer.write(writer);
        thisCheckZoneDeactivate.write(writer);
    }

    public int getEntryPoint()
    {
        return deactivateThisCheckZoneTimer.getIndex();
    }

    public McuTimer getDeactivateNextVwpTimer()
    {
        return deactivateThisCheckZoneTimer;
    }

    public McuDeactivate getThisCheckZoneDeactivate()
    {
        return thisCheckZoneDeactivate;
    }
    
    
}
