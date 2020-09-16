package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.group.SelfDeactivatingCheckZone;

public final class VirtualWaypointCheckZone 
{       
    private VirtualWayPointCoordinate vwpCoordinate;
    
    private McuTimer vwpStartTimer = new McuTimer();
    private McuTimer triggeredDisableNextVwpTimer = new McuTimer();
    private McuTimer triggeredActivateContainerTimer = new McuTimer();
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

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

    public void link(int vwpStartNextWPIndex,int disableNextVwpIndex, int activateContainerIndex)
    {
        vwpStartTimer.setTarget(vwpStartNextWPIndex);
        triggeredDisableNextVwpTimer.setTarget(disableNextVwpIndex);
        triggeredActivateContainerTimer.setTarget(activateContainerIndex);
    }

    private void buildMcus()
    {
        checkZone = new SelfDeactivatingCheckZone("Activate Check Zone", vwpCoordinate.getPosition().copy(), VirtualWaypoint.VWP_TRIGGGER_DISTANCE);
 
        vwpStartTimer.setPosition(vwpCoordinate.getPosition().copy());
        vwpStartTimer.setName("VWP Start Timer");
        vwpStartTimer.setDesc("VWP Start Timer");
        vwpStartTimer.setTimer(vwpCoordinate.getWaypointWaitTimeSeconds());

        triggeredDisableNextVwpTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredDisableNextVwpTimer.setName("Triggered Activate Container Timer");
        triggeredDisableNextVwpTimer.setDesc("Triggered Activate Container Timer");
        triggeredDisableNextVwpTimer.setTimer(1);

        triggeredActivateContainerTimer.setPosition(vwpCoordinate.getPosition().copy());
        triggeredActivateContainerTimer.setName("Triggered Activate Container Timer");
        triggeredActivateContainerTimer.setDesc("Triggered Activate Container Timer");
        triggeredActivateContainerTimer.setTimer(1);
    }

    private void setTargetAssociations() throws PWCGException
    {
        vwpStartTimer.setTarget(checkZone.getActivateEntryPoint());
        checkZone.setCheckZoneTarget(triggeredDisableNextVwpTimer.getIndex());
        triggeredDisableNextVwpTimer.setTarget(triggeredActivateContainerTimer.getIndex());
        
        makeSubtitles();
    }

    protected void makeSubtitles() throws PWCGException
    {
        McuSubtitle vwpCzStartedSubtitle = new McuSubtitle();
        vwpCzStartedSubtitle.setName("VWP Subtitle");
        vwpCzStartedSubtitle.setText("VWP CZ Started For " +  vwpStartTimer.getIndex());
        vwpCzStartedSubtitle.setPosition(vwpCoordinate.getPosition().copy());
        vwpStartTimer.setTarget(vwpCzStartedSubtitle.getIndex());
        
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(vwpCzStartedSubtitle.getLcText(), vwpCzStartedSubtitle.getText());
        subTitleList.add(vwpCzStartedSubtitle);
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            vwpStartTimer.write(writer);
            checkZone.write(writer);
            triggeredDisableNextVwpTimer.write(writer);
            triggeredActivateContainerTimer.write(writer);
            
            for (McuSubtitle subtitle : subTitleList)
            {
                subtitle.write(writer);
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
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
        return triggeredActivateContainerTimer;
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
