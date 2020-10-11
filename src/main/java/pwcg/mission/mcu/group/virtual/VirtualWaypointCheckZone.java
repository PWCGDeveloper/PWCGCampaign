package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public final class VirtualWaypointCheckZone 
{       
    private VirtualWayPointCoordinate vwpCoordinate;
    
    private McuTimer vwpStartTimer = new McuTimer();
    private VirtualWaypointPlanes vwpPlanes;
    private McuTimer triggeredDisableNextVwpTimer = new McuTimer();
    private McuTimer triggeredActivateTimer = new McuTimer();
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();
    private int vwpIdentifier = 1;

    private SelfDeactivatingCheckZone checkZone;
    
    public VirtualWaypointCheckZone(VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes, int vwpIdentifier)
    {
        this.vwpCoordinate = vwpCoordinate; 
        this.vwpPlanes = vwpPlanes; 
        this.vwpIdentifier = vwpIdentifier;
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
        makeSubtitles();
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
        vwpStartTimer.setTimer(1);

        triggeredDisableNextVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredDisableNextVwpTimer.setName("Disable Next VWP Timer");
        triggeredDisableNextVwpTimer.setDesc("Disable Next VWP Timer");
        triggeredDisableNextVwpTimer.setTimer(0);

        triggeredActivateTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredActivateTimer.setName("Triggered Activate Timer");
        triggeredActivateTimer.setDesc("Triggered Activate Timer");
        triggeredActivateTimer.setTimer(1);
    }

    private void makeSubtitles() throws PWCGException
    {
        Coordinate subtitlePosition = vwpCoordinate.getPosition().copy();
        McuSubtitle activatedSubtitle = McuSubtitle.makeActivatedSubtitle("VWP Started: " + vwpIdentifier + " " + vwpPlanes.getLeadActivatePlane().getName(), subtitlePosition);
        vwpStartTimer.setTarget(activatedSubtitle.getIndex());
        subTitleList.add(activatedSubtitle);

        McuSubtitle triggeredSubtitle = McuSubtitle.makeActivatedSubtitle("VWP Trigegred: " + vwpIdentifier + " " + vwpPlanes.getLeadActivatePlane().getName(), subtitlePosition);
        triggeredDisableNextVwpTimer.setTarget(triggeredSubtitle.getIndex());
        subTitleList.add(triggeredSubtitle);
    }

    private void setTargetAssociations() throws PWCGException
    {
        vwpStartTimer.setTarget(checkZone.getActivateEntryPoint());
        checkZone.setCheckZoneTarget(triggeredDisableNextVwpTimer.getIndex());
        triggeredDisableNextVwpTimer.setTarget(triggeredActivateTimer.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        vwpStartTimer.write(writer);
        checkZone.write(writer);
        triggeredDisableNextVwpTimer.write(writer);
        triggeredActivateTimer.write(writer);        
        
        McuSubtitle.writeSubtitles(subTitleList, writer);
    }

    public McuTimer getVwpStartTimer()
    {
        return vwpStartTimer;
    }

    public McuTimer getTriggeredDisableNextVwpTimer()
    {
        return triggeredDisableNextVwpTimer;
    }

    public McuTimer getTriggeredActivateTimer()
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
