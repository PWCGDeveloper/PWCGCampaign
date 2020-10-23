package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public final class VirtualWaypoint implements IVirtualWaypoint 
{       
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private int index = IndexGenerator.getInstance().getNextIndex();
    private int vwpIdentifier = 1;

    private VirtualWaypointPlanes vwpPlanes;
    private VirtualWaypointEscort vwpEscort;
    private VirtualWaypointCheckZone vwpCheckZone;
    private VirtualWaypointTriggered vwpTriggered;
    private VirtualWaypointStartNextVwp vwpStartNextVwp;
    private VirtualWaypointDeactivateNextVwp vwpDeactivateNextVwp;
    private VirtualWaypointDeactivateThisVwp vwpDeactivateThisVwp;
    private VirtualWaypointUpstreamKill vwpUpstreamKill;
    private VirtualWaypointDeletePlanes vwpDeletePlanes;

    public static int VWP_TRIGGGER_DISTANCE = 20000;
    
    public VirtualWaypoint(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, int vwpIdentifier)
    {
        index = IndexGenerator.getInstance().getNextIndex();

        this.flight = flight; 
        this.vwpCoordinate = vwpCoordinate; 
        this.vwpIdentifier = vwpIdentifier; 
    }

    @Override
    public void build() throws PWCGException 
    {
        buildElements();
        linkElements();
    }

    @Override
    public void addEscort(IFlightInformation vwpEscortFlightInformation) throws PWCGException 
    {
        vwpEscort = VirtualWaypointEscortBuilder.buildVirtualEscort(vwpEscortFlightInformation, vwpCoordinate, vwpPlanes, vwpTriggered);
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

            vwpPlanes.write(writer);
            vwpCheckZone.write(writer);
            vwpTriggered.write(writer);
            vwpDeactivateNextVwp.write(writer);
            vwpUpstreamKill.write(writer);
            vwpStartNextVwp.write(writer);
            vwpDeactivateThisVwp.write(writer);
            vwpDeletePlanes.write(writer);
            if (vwpEscort != null)
            {
                vwpEscort.write(writer);
            }

            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    private void buildElements() throws PWCGException
    {
        vwpPlanes = new VirtualWaypointPlanes(flight, vwpCoordinate);
        vwpPlanes.build();

        vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate, vwpPlanes, vwpIdentifier);
        vwpCheckZone.build();

        vwpTriggered = new VirtualWaypointTriggered(flight, vwpCoordinate, vwpPlanes, vwpIdentifier);
        vwpTriggered.build();

        vwpDeactivateNextVwp = new VirtualWaypointDeactivateNextVwp(vwpCoordinate);
        vwpDeactivateNextVwp.build();

        vwpUpstreamKill = new VirtualWaypointUpstreamKill(vwpCoordinate);
        vwpUpstreamKill.build();

        vwpStartNextVwp = new VirtualWaypointStartNextVwp(vwpCoordinate);
        vwpStartNextVwp.build();
        
        vwpDeactivateThisVwp = new VirtualWaypointDeactivateThisVwp(vwpCoordinate);
        vwpDeactivateThisVwp.build();

        vwpDeletePlanes = new VirtualWaypointDeletePlanes(vwpCoordinate, vwpPlanes, vwpIdentifier);
        vwpDeletePlanes.build();
    }

    private void linkElements() throws PWCGException
    {
        vwpCheckZone.link(vwpStartNextVwp, vwpTriggered);
        vwpTriggered.link(vwpDeactivateNextVwp);
        vwpDeactivateNextVwp.link(vwpStartNextVwp, vwpUpstreamKill);
        vwpStartNextVwp.link(vwpDeactivateThisVwp);
        vwpDeactivateThisVwp.link(vwpCheckZone, vwpDeletePlanes);
        vwpUpstreamKill.link(vwpDeletePlanes);

    }

    @Override
    public void linkToNextVirtualWaypoint(IVirtualWaypoint nextVwp)
    {
        vwpStartNextVwp.linkToNextVwp(nextVwp.getEntryPoint());
        vwpUpstreamKill.linkToNextVwpKill(nextVwp.getVwpUpstreamKill(), nextVwp.getVwpDeletePlanes());
    }
    
    @Override
    public PlaneMcu getVwpFlightLeader()
    {
        return vwpPlanes.getLeadActivatePlane();
    }
    
    @Override
    public void setVwpTriggerObject(int planeIndex)
    {
        vwpCheckZone.setVwpTriggerObject(planeIndex);        
    }    

    @Override
    public void addAdditionalTime(int additionalTime)
    {
        vwpStartNextVwp.addAdditionalTime(additionalTime);
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
    public VirtualWayPointCoordinate getVwpCoordinate()
    {
        return vwpCoordinate;
    }

    @Override
    public VirtualWaypointTriggered getVwpTriggered()
    {
        return vwpTriggered;
    }

    @Override
    public VirtualWaypointCheckZone getVwpCheckZone()
    {
        return vwpCheckZone;
    }

    @Override
    public VirtualWaypointStartNextVwp getVwpNextVwpStart()
    {
        return vwpStartNextVwp;
    }

    @Override
    public VirtualWaypointDeactivateNextVwp getVwpNextVwpDeactivate()
    {
        return vwpDeactivateNextVwp;
    }

    @Override
    public VirtualWaypointUpstreamKill getVwpUpstreamKill()
    {
        return vwpUpstreamKill;
    }

    @Override
    public VirtualWaypointPlanes getVwpPlanes()
    {
        return vwpPlanes;
    }

    @Override
    public VirtualWaypointDeletePlanes getVwpDeletePlanes()
    {
        return vwpDeletePlanes;
    }

    public VirtualWaypointEscort getVwpEscort()
    {
        return vwpEscort;
    }
}
