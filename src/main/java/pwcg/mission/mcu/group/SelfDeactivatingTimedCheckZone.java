package pwcg.mission.mcu.group;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuTimerFactory;

public class SelfDeactivatingTimedCheckZone
{
	private String name = "Self Deactivating CZ";
	private String desc = "Self Deactivating CZ";
    private int index = IndexGenerator.getInstance().getNextIndex();;
	
    private McuTimer activateCZTimer = new McuTimer();

    private McuCheckZone checkZone;
    private McuTimer checkZoneTriggeredTimer = new McuTimer();
    private McuTimer checkZoneTriggeredExternalTimer = new McuTimer();
    
    private McuTimer deactivateCZTimer = new McuTimer();
    private McuDeactivate deactivateCZ = new McuDeactivate();
    
    private McuTimer waitTimer = new McuTimer();
    private McuTimer waitTriggeredTimer = new McuTimer();
    private McuTimer waitTriggeredExternalTimer = new McuTimer();

    private int activeTime;
    private int sequenceNumber;
    private Coordinate coordinate;
    private int checkZoneTriggerDistanceMeters;
	
    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public SelfDeactivatingTimedCheckZone (int checkZoneTriggerDistanceMeters, int activeTime, Coordinate coordinate, int sequenceNumber)
    {
        this.checkZoneTriggerDistanceMeters = checkZoneTriggerDistanceMeters;        
        this.activeTime = activeTime;
        this.coordinate = coordinate;        
        this.sequenceNumber = sequenceNumber;        
    }
    
    public void build()
    {
        buildMcus();
        linkTargets();
        makeSubtitles();
    }

    private void buildMcus() 
    {
        checkZone = new McuCheckZone(name);

        activateCZTimer = McuTimerFactory.buildStandardMcuTimer("CZ Activate Timer", coordinate.copy());
        
        checkZone = McuTimerFactory.buildMcuCheckZone("CZ", coordinate.copy(), checkZoneTriggerDistanceMeters);
        checkZoneTriggeredTimer = McuTimerFactory.buildStandardMcuTimer("CZ Triggered Timer", coordinate.copy());
        checkZoneTriggeredExternalTimer = McuTimerFactory.buildStandardMcuTimer("CZ Triggered External Timer", coordinate.copy());

        waitTimer = McuTimerFactory.buildMcuTimer("CZ Wait Timer", coordinate.copy(), activeTime);
        waitTriggeredTimer = McuTimerFactory.buildStandardMcuTimer("CZ Wait Triggered Timer", coordinate.copy());
        waitTriggeredExternalTimer = McuTimerFactory.buildStandardMcuTimer("CZ Wait Triggered External Timer", coordinate.copy());
        
        deactivateCZTimer = McuTimerFactory.buildStandardMcuTimer("CZ Deactivate Timer", coordinate.copy());
        deactivateCZ = McuTimerFactory.buildMcuDeactivate("CZ Deactivate", coordinate.copy());
    }
    
    private void linkTargets()
    {
        activateCZTimer.setTarget(checkZone.getIndex());
        activateCZTimer.setTarget(waitTimer.getIndex());

        checkZone.setTarget(checkZoneTriggeredTimer.getIndex());
        checkZoneTriggeredTimer.setTarget(checkZoneTriggeredExternalTimer.getIndex());
        checkZoneTriggeredTimer.setTarget(deactivateCZTimer.getIndex());

        waitTimer.setTarget(waitTriggeredTimer.getIndex());
        waitTriggeredTimer.setTarget(waitTriggeredExternalTimer.getIndex());
        waitTriggeredTimer.setTarget(deactivateCZTimer.getIndex());

        deactivateCZTimer.setTarget(deactivateCZ.getIndex());

        deactivateCZ.setTarget(checkZone.getIndex());        
        deactivateCZ.setTarget(waitTimer.getIndex());    
    }

    private void makeSubtitles()
    {
        Coordinate subtitlePosition = coordinate.copy();
        McuSubtitle waitTriggeredSubtitle = McuSubtitle.makeActivatedSubtitle("CZ Wait Triggered: " + sequenceNumber, subtitlePosition);
        waitTriggeredTimer.setTarget(waitTriggeredSubtitle.getIndex());
        subTitleList.add(waitTriggeredSubtitle);

        McuSubtitle czTriggeredSubtitle = McuSubtitle.makeActivatedSubtitle("CZ Triggered: " + sequenceNumber, subtitlePosition);
        checkZoneTriggeredTimer.setTarget(czTriggeredSubtitle.getIndex());
        subTitleList.add(czTriggeredSubtitle);

        McuSubtitle thisVwpDisabledSubtitle = McuSubtitle.makeActivatedSubtitle("CZ Disabled: " + sequenceNumber, subtitlePosition);
        deactivateCZTimer.setTarget(thisVwpDisabledSubtitle.getIndex());
        subTitleList.add(thisVwpDisabledSubtitle);
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = \"" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"" +  desc + "\";");
            writer.newLine();
            writer.newLine();

            activateCZTimer.write(writer);
            
            checkZone.write(writer);
            checkZoneTriggeredTimer.write(writer);
            checkZoneTriggeredExternalTimer.write(writer);
            
            waitTimer.write(writer);
            waitTriggeredTimer.write(writer);
            waitTriggeredExternalTimer.write(writer);
            
            deactivateCZTimer.write(writer);
            deactivateCZ.write(writer);
            
            McuSubtitle.writeSubtitles(subTitleList, writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
    
    public int getActivateEntryPoint()
    {
        return activateCZTimer.getIndex();
    }

    public void setCheckZoneTriggeredTarget(int targetMcuIndex)
    {
        checkZoneTriggeredExternalTimer.setTarget(targetMcuIndex);
    }

    public void setCheckZoneTimedOutTarget(int targetMcuIndex)
    {
        waitTriggeredExternalTimer.setTarget(targetMcuIndex);
    }

    public void setCheckZoneTriggerObject(int objectMcuIndex)
    {
        checkZone.setObject(objectMcuIndex);
    }

    public void addAdditionalTime(double additionalTime)
    {
        double timeNow = waitTimer.getTime();
        double newTime = timeNow + additionalTime;
        waitTimer.setTime(newTime);
    }

    /// Test only

    public McuCheckZone getCheckZone()
    {
        return checkZone;
    }

    public McuTimer getTriggeredExternal()
    {
        return checkZoneTriggeredExternalTimer;
    }

    public McuTimer getTimedOutExternal()
    {
        return waitTriggeredExternalTimer;
    }

    public McuTimer getActivateCZTimer()
    {
        return activateCZTimer;
    }

    public McuTimer getCheckZoneTriggeredTimer()
    {
        return checkZoneTriggeredTimer;
    }

    public McuTimer getCheckZoneTriggeredExternalTimer()
    {
        return checkZoneTriggeredExternalTimer;
    }

    public McuTimer getDeactivateCZTimer()
    {
        return deactivateCZTimer;
    }

    public McuDeactivate getDeactivateCZ()
    {
        return deactivateCZ;
    }

    public McuTimer getWaitTimer()
    {
        return waitTimer;
    }

    public McuTimer getWaitTriggeredTimer()
    {
        return waitTriggeredTimer;
    }

    public McuTimer getWaitTriggeredExternalTimer()
    {
        return waitTriggeredExternalTimer;
    }    
}
