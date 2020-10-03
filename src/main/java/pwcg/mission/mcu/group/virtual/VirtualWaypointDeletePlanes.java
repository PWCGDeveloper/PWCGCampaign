package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointDeletePlanes
{
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointPlanes vwpPlanes;
    private McuTimer deletePlanesTimer = new McuTimer();
    private McuDelete deletePlanes = new McuDelete();

    public VirtualWaypointDeletePlanes(VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes)
    {
        this.vwpCoordinate = vwpCoordinate;
        this.vwpPlanes = vwpPlanes;
    }

    public void build() throws PWCGException
    {
        buildMcus();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildMcus()
    {
        deletePlanes.setPosition(vwpCoordinate.getPosition().copy());
        deletePlanes.setOrientation(vwpCoordinate.getOrientation().copy());
        deletePlanes.setName("Delete Planes");
        deletePlanes.setDesc("Delete Planes");
        
        deletePlanesTimer.setTimer(1);
        deletePlanesTimer.setPosition(vwpCoordinate.getPosition().copy());
        deletePlanesTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        deletePlanesTimer.setName("Delete Planes Timer");
        deletePlanesTimer.setDesc("Delete Planes Timer");

    }

    private void createTargetAssociations()
    {
        deletePlanesTimer.setTarget(deletePlanes.getIndex());
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu plane : vwpPlanes.getAllPlanes())
        {
            deletePlanes.setObject(plane.getEntity().getIndex());
        }        
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        deletePlanesTimer.write(writer);
        deletePlanes.write(writer);        
    }
    
    public int getEntryPoint()
    {
        return deletePlanesTimer.getIndex();
    }
}
