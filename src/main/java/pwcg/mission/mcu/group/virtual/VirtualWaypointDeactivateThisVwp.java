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

public class VirtualWaypointDeactivateThisVwp 
{       
    private VirtualWayPointCoordinate vwpCoordinate;

    private McuTimer deactivateThisCheckZoneTimer = new McuTimer();
    private McuDeactivate thisCheckZoneDeactivate;    
    private int index = IndexGenerator.getInstance().getNextIndex();

    public VirtualWaypointDeactivateThisVwp(VirtualWayPointCoordinate vwpCoordinate)
    {
        this.vwpCoordinate = vwpCoordinate; 
    }

    public void build() throws PWCGException 
    {
        buildMcus();
        setTargetAssociations();
    }
    
    public void link(VirtualWaypointCheckZone vwpCheckZone, VirtualWaypointDeletePlanes vwpDeletePlanes)
    {
        thisCheckZoneDeactivate.setTarget(vwpCheckZone.getCheckZone().getDeactivateEntryPoint());
        thisCheckZoneDeactivate.setTarget(vwpDeletePlanes.getEntryPoint());
    }

    private void buildMcus()
    {
        deactivateThisCheckZoneTimer.setPosition(vwpCoordinate.getPosition().copy());
        deactivateThisCheckZoneTimer.setName("VWP Deactivate This VWP Timer");
        deactivateThisCheckZoneTimer.setDesc("VWP Deactivate This VWP Timer");
        deactivateThisCheckZoneTimer.setTimer(0);

        thisCheckZoneDeactivate = new McuDeactivate();
        thisCheckZoneDeactivate.setPosition(vwpCoordinate.getPosition().copy());
        thisCheckZoneDeactivate.setName("VWP Deactivate This VWP");
        thisCheckZoneDeactivate.setDesc("VWP Deactivate This VWP");
    }

    private void setTargetAssociations() throws PWCGException
    {
        deactivateThisCheckZoneTimer.setTarget(thisCheckZoneDeactivate.getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Deactivate This VWP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Deactivate This VWP\";");
            writer.newLine();
    
            deactivateThisCheckZoneTimer.write(writer);
            thisCheckZoneDeactivate.write(writer);
    
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
