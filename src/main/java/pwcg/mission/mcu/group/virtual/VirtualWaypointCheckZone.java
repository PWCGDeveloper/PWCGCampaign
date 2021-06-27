package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.group.SelfDeactivatingTimedCheckZone;

public class VirtualWaypointCheckZone 
{       
    private VirtualWayPointCoordinate vwpCoordinate;
    
    private int vwpIdentifier = 1;
    private int index = IndexGenerator.getInstance().getNextIndex();

    private SelfDeactivatingTimedCheckZone checkZone;
    
    public VirtualWaypointCheckZone(VirtualWayPointCoordinate vwpCoordinate, int vwpIdentifier)
    {
        this.vwpCoordinate = vwpCoordinate; 
        this.vwpIdentifier = vwpIdentifier;
    }

    public void build() throws PWCGException 
    {
        buildMcus();
    }

    public void linkToTriggered(VirtualWaypointTriggered vwpTriggered)
    {
        checkZone.setCheckZoneTriggeredTarget(vwpTriggered.getEntryPoint());
    }

    public void linkToTimedOut(IVirtualWaypoint nextVirtualWaypoint)
    {
        checkZone.setCheckZoneTimedOutTarget(nextVirtualWaypoint.getEntryPoint());
    }

    private void buildMcus()
    {
        checkZone = new SelfDeactivatingTimedCheckZone(
                VirtualWaypoint.VWP_TRIGGGER_DISTANCE, 
                vwpCoordinate.getWaypointWaitTimeSeconds(),
                vwpCoordinate.getPosition().copy(), 
                vwpIdentifier);
        
        checkZone.build();
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Virtual WP Check Zone\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Virtual WP Check Zone\";");
            writer.newLine();
    
            checkZone.write(writer);
    
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
    
    public void setVwpTriggerObject(int planeIndex)
    {
        checkZone.setCheckZoneTriggerObject(planeIndex);        
    }

    public int getEntryPoint()
    {
        return checkZone.getActivateEntryPoint();
    }

    public void addAdditionalTime(double additionalTime)
    {
        checkZone.addAdditionalTime(additionalTime);
        
    }

    public SelfDeactivatingTimedCheckZone getTimedCheckZone()
    {
        return checkZone;
    }    
 }
