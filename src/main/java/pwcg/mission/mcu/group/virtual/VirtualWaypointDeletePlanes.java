package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuDelete;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointDeletePlanes
{
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointPlanes vwpPlanes;
    private McuTimer deletePlanesTimer = new McuTimer();
    private McuDelete deletePlanes = new McuDelete();

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

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
        makeSubtitles();
    }

    private void buildMcus()
    {
        deletePlanes.setPosition(vwpCoordinate.getPosition().copy());
        deletePlanes.setOrientation(vwpCoordinate.getOrientation().copy());
        deletePlanes.setName("Delete Planes");
        deletePlanes.setDesc("Delete Planes");
        
        deletePlanesTimer.setTimer(5);
        deletePlanesTimer.setPosition(vwpCoordinate.getPosition().copy());
        deletePlanesTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        deletePlanesTimer.setName("Delete Planes Timer");
        deletePlanesTimer.setDesc("Delete Planes Timer");

    }

    private void makeSubtitles() throws PWCGException
    {
        McuSubtitle planeRemoverStartedSubtitle = new McuSubtitle();
        planeRemoverStartedSubtitle.setName("Delete VWP Planes Subtitle");
        planeRemoverStartedSubtitle.setText("Delete VWP Planes " +   vwpPlanes.getLeadActivatePlane().getLinkTrId());
        planeRemoverStartedSubtitle.setPosition(vwpCoordinate.getPosition().copy());
        subTitleList.add(planeRemoverStartedSubtitle);
        
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(planeRemoverStartedSubtitle.getLcText(), planeRemoverStartedSubtitle.getText());
        
        deletePlanesTimer.setTarget(planeRemoverStartedSubtitle.getIndex());
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
        McuSubtitle.writeSubtitles(subTitleList, writer);
    }
    
    public int getEntryPoint()
    {
        return deletePlanesTimer.getIndex();
    }
}
