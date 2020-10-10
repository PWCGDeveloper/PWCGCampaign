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
    
    private VirtualWaypointPlanes vwpPlanes;
    private VirtualWaypointEscort vwpEscort;
    private VirtualWaypointCheckZone vwpCheckZone;
    private VirtualWaypointActivate vwpActivate;
    private VirtualWaypointStartNextVwp vwpStartNextVwp;
    private VirtualWaypointDeletePlanes vwpDeletePlanes;
    private VirtualWaypointDeactivateNextVwp vwpDeactivateNextVwp;
    private VirtualWaypointDeactivateThisVwp vwpDeactivateThisVwp;
    private VirtualWaypointKillFuture vwpKillFuture;

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

    @Override
    public void addEscort(IFlightInformation vwpEscortFlightInformation) throws PWCGException 
    {
        vwpEscort = VirtualWaypointEscortBuilder.buildVirtualEscort(vwpEscortFlightInformation, vwpCoordinate, vwpPlanes, vwpActivate);
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
            vwpActivate.write(writer);
            vwpStartNextVwp.write(writer);
            vwpDeletePlanes.write(writer);
            vwpDeactivateNextVwp.write(writer);
            vwpDeactivateThisVwp.write(writer);
            vwpKillFuture.write(writer);
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

        vwpActivate = new VirtualWaypointActivate(flight, vwpCoordinate, vwpPlanes);
        vwpActivate.build();

        vwpCheckZone = new VirtualWaypointCheckZone(vwpCoordinate);
        vwpCheckZone.build();

        vwpDeletePlanes = new VirtualWaypointDeletePlanes(vwpCoordinate, vwpPlanes);
        vwpDeletePlanes.build();

        vwpKillFuture = new VirtualWaypointKillFuture(vwpCoordinate, vwpDeletePlanes);
        vwpKillFuture.build();

        vwpStartNextVwp = new VirtualWaypointStartNextVwp(vwpCoordinate, vwpDeletePlanes);
        vwpStartNextVwp.build();

        vwpDeactivateNextVwp = new VirtualWaypointDeactivateNextVwp(vwpCoordinate);
        vwpDeactivateNextVwp.build();
        
        vwpDeactivateThisVwp = new VirtualWaypointDeactivateThisVwp(vwpCoordinate);
        vwpDeactivateThisVwp.build();
    }

    private void linkElements() throws PWCGException
    {
        vwpCheckZone.link(vwpStartNextVwp, vwpDeactivateNextVwp, vwpActivate);
        vwpDeactivateNextVwp.link(vwpStartNextVwp, vwpDeactivateThisVwp);
    }

    @Override
    public void linkToNextVirtualWaypoint(IVirtualWaypoint nextVwp)
    {
        vwpStartNextVwp.linkToNextVwp(nextVwp.getEntryPoint());
    }
    
    @Override
    public PlaneMcu getVwpFlightLeader()
    {
        return vwpPlanes.getLeadActivatePlane();
    }
    
    @Override
    public void linkKillToNextKill(IVirtualWaypoint nextVwp)
    {
        vwpKillFuture.linkToNextVwpKill(nextVwp.getVwpKillFuture().getEntryPoint());
    }
    
    @Override
    public void linkActivateToNextKill(IVirtualWaypoint nextVwp)
    {
        vwpActivate.linkToNextDeletePlanesTimer(nextVwp.getVwpKillFuture().getEntryPoint());
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
        return vwpStartNextVwp;
    }

    @Override
    public VirtualWaypointDeactivateNextVwp getVwpNextVwpDeactivate()
    {
        return vwpDeactivateNextVwp;
    }

    @Override
    public VirtualWaypointKillFuture getVwpKillFuture()
    {
        return vwpKillFuture;
    }

    @Override
    public VirtualWaypointPlanes getVwpPlanes()
    {
        return vwpPlanes;
    }

    public VirtualWaypointEscort getVwpEscort()
    {
        return vwpEscort;
    }
}
