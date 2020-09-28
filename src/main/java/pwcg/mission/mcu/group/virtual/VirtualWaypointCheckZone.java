package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public final class VirtualWaypointCheckZone 
{       
    private VirtualWayPointCoordinate vwpCoordinate;
    
    private McuTimer vwpStartTimer = new McuTimer();
    private McuTimer triggeredDisableNextVwpTimer = new McuTimer();
    private McuTimer triggeredActivateTimer = new McuTimer();

    private SelfDeactivatingCheckZone checkZone;
    
    public VirtualWaypointCheckZone(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }

    public void link(VirtualWaypointStartNextVwp vwpNextVwpStart, VirtualWaypointDeactivateNextVwp vwpNextVwpDeactivate, VirtualWaypointActivate vwpActivate)
    {
        vwpStartTimer.setTarget(vwpNextVwpStart.getEntryPoint());
        triggeredDisableNextVwpTimer.setTarget(vwpNextVwpDeactivate.getEntryPoint());
        triggeredActivateTimer.setTarget(vwpActivate.getEntryPoint());
    }

    private void buildMcus()
    {
        checkZone = new SelfDeactivatingCheckZone("Activate Check Zone", vwpCoordinate.getPosition().copy(), VirtualWaypoint.VWP_TRIGGGER_DISTANCE);
 
        vwpStartTimer.setPosition(vwpCoordinate.getPosition().copy());
        vwpStartTimer.setName("VWP Start Timer");
        vwpStartTimer.setDesc("VWP Start Timer");
        vwpStartTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());

        triggeredDisableNextVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredDisableNextVwpTimer.setName("Disable Next VWP Timer");
        triggeredDisableNextVwpTimer.setDesc("Disable Next VWP Timer");
        triggeredDisableNextVwpTimer.setTimer(1);

        triggeredActivateTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredActivateTimer.setName("Triggered Activate Timer");
        triggeredActivateTimer.setDesc("Triggered Activate Timer");
        triggeredActivateTimer.setTimer(1);
    }

    private void setTargetAssociations() throws PWCGException
    {
        vwpStartTimer.setTarget(checkZone.getActivateEntryPoint());
        checkZone.setCheckZoneTarget(triggeredDisableNextVwpTimer.getIndex());
        checkZone.setCheckZoneTarget(triggeredActivateTimer.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        vwpStartTimer.write(writer);
        checkZone.write(writer);
        triggeredDisableNextVwpTimer.write(writer);
        triggeredActivateTimer.write(writer);        
    }

    public void addAdditionalTime(int additionalTime)
    {
        int activateTimerTime = vwpStartTimer.getTimer();
        activateTimerTime += additionalTime;
        vwpStartTimer.setTimer(activateTimerTime);
    }

    public McuTimer getVwpStartTimer()
    {
        return vwpStartTimer;
    }

    public McuTimer getTriggeredDisableNextVwpTimer()
    {
        return triggeredDisableNextVwpTimer;
    }

    public McuTimer getTriggeredActivateContainerTimer()
    {
        return triggeredActivateTimer;
    }

    public SelfDeactivatingCheckZone getCheckZone()
    {
        return checkZone;
    }

    public void setVwpTriggerObject(int planeIndex)
    {
        checkZone.setCheckZoneTriggerObject(planeIndex);        
    }
 }
