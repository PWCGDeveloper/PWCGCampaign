package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public final class VirtualWaypoint implements IVirtualWaypoint 
{       
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private int index = IndexGenerator.getInstance().getNextIndex();
    
    private VirtualWaypointActivate vwpActivate;
    private VirtualWaypointCheckZone vwpCheckZone;
    private VirtualWaypointStartNextVwp vwpNextVwpStart;
    private VirtualWaypointDeactivateNextVwp vwpNextVwpDeactivate;

    public static int VWP_TRIGGGER_DISTANCE = 20000;
    
    public VirtualWaypoint(IFlight flight, VirtualWayPointCoordinate vwpCoordinate)
    {
        index = IndexGenerator.getInstance().getNextIndex();

        this.flight = flight; 
        this.vwpCoordinate = vwpCoordinate; 
    }

    @Override
    public void build() throws PWCGException 
    {
        buildElements();
        linkElements();
    }

    private void buildElements() throws PWCGException
    {
        vwpActivate = new VirtualWaypointActivate(flight, vwpCoordinate);
        vwpActivate.build();

        vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate);
        vwpCheckZone.build();

        vwpNextVwpStart = new VirtualWaypointStartNextVwp(vwpCoordinate);
        vwpNextVwpStart.build();

        vwpNextVwpDeactivate = new VirtualWaypointDeactivateNextVwp(vwpCoordinate);
        vwpNextVwpDeactivate.build();
    }

    private void linkElements() throws PWCGException
    {
        vwpCheckZone.link(vwpNextVwpStart.getStartNextWaypointTimer().getIndex(), vwpNextVwpDeactivate.getEntryPoint(), vwpActivate.getEntryPoint());
        vwpNextVwpDeactivate.link(vwpNextVwpStart.getStartNextWaypointTimer().getIndex());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writer.write("  Name = \"Virtual WP\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"Virtual WP\";");
            writer.newLine();

            vwpCheckZone.write(writer);
            vwpActivate.write(writer);
            vwpNextVwpStart.write(writer);
            vwpNextVwpDeactivate.write(writer);

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    @Override
    public void setVwpTriggerObject(int planeIndex)
    {
        vwpCheckZone.setVwpTriggerObject(planeIndex);        
    }    

    @Override
    public void addAdditionalTime(int additionalTime)
    {
        vwpCheckZone.addAdditionalTime(additionalTime);
    }

    @Override
    public int getEntryPoint()
    {
        return vwpCheckZone.getVwpStartTimer().getIndex();
    }

    @Override
    public Coordinate getVwpPosition()
    {
        return vwpCoordinate.getPosition();
    }
    
    @Override
    public void linkToNextVirtualWaypoint(IVirtualWaypoint nextVwpIndex)
    {
        vwpNextVwpStart.linkToNextVwp(nextVwpIndex.getEntryPoint());
    }

    @Override
    public VirtualWayPointCoordinate getVwpCoordinate()
    {
        return vwpCoordinate;
    }

    @Override
    public VirtualWaypointActivate getVwpActivate()
    {
        return vwpActivate;
    }

    @Override
    public VirtualWaypointCheckZone getVwpCheckZone()
    {
        return vwpCheckZone;
    }

    @Override
    public VirtualWaypointStartNextVwp getVwpNextVwpStart()
    {
        return vwpNextVwpStart;
    }

    @Override
    public VirtualWaypointDeactivateNextVwp getVwpNextVwpDeactivate()
    {
        return vwpNextVwpDeactivate;
    }
}
