package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointActivate
{
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointPlanes vwpPlanes;

    private McuFormation formation = new McuFormation();
    private McuActivate activate = new McuActivate();

    private McuTimer activateTimer = new McuTimer();
    private McuTimer formationTimer = new McuTimer();
    private McuTimer waypointTimer = new McuTimer();
    private McuTimer escortTimer = new McuTimer();
    private McuTimer deleteUpstreamPlanesTimer = new McuTimer();

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public VirtualWaypointActivate(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes)
    {
        this.flight = flight;
        this.vwpCoordinate = vwpCoordinate;
        this.vwpPlanes = vwpPlanes;
    }

    public void build() throws PWCGException
    {
        buildMcus();
        // Comment out subtitles but leave the code here as an an example
        //makeSubtitles();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildMcus()
    {
        activate.setPosition(vwpCoordinate.getPosition().copy());
        activate.setOrientation(vwpCoordinate.getOrientation().copy());
        activate.setName("Activate");
        activate.setDesc("Activate");
        
        activateTimer.setTimer(1);
        activateTimer.setPosition(vwpCoordinate.getPosition().copy());
        activateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateTimer.setName("Activate Timer");
        activateTimer.setDesc("Activate Timer");

        waypointTimer.setTimer(1);
        waypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        waypointTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        waypointTimer.setName("Activate WP Timer");
        waypointTimer.setDesc("Activate WP Timer");

        escortTimer.setTimer(1);
        escortTimer.setPosition(vwpCoordinate.getPosition().copy());
        escortTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        escortTimer.setName("Escort Timer");
        escortTimer.setDesc("Escort Timer");

        formationTimer.setPosition(vwpCoordinate.getPosition().copy());
        formationTimer.setName("Formation Timer");
        formationTimer.setDesc("Formation Timer");
        
        deleteUpstreamPlanesTimer.setPosition(vwpCoordinate.getPosition().copy());
        deleteUpstreamPlanesTimer.setName("Delete Upstream Planes Timer");
        deleteUpstreamPlanesTimer.setDesc("Delete Upstream Planes Timer");

        formation.setPosition(vwpCoordinate.getPosition().copy());
    }

    private void makeSubtitles() throws PWCGException
    {
        McuSubtitle czTriggeredSubtitle = new McuSubtitle();
        czTriggeredSubtitle.setName("CheckZone Subtitle");
        czTriggeredSubtitle.setText("VWP Activate: " +   vwpPlanes.getLeadActivatePlane().getLinkTrId() + " " + vwpPlanes.getLeadActivatePlane().getName());
        czTriggeredSubtitle.setPosition(vwpCoordinate.getPosition().copy());
        czTriggeredSubtitle.setDuration(5);
        subTitleList.add(czTriggeredSubtitle);
        
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(czTriggeredSubtitle.getLcText(), czTriggeredSubtitle.getText());
        
        activateTimer.setTarget(czTriggeredSubtitle.getIndex());
    }

    private void createTargetAssociations()
    {
        activateTimer.setTarget(activate.getIndex());
        formationTimer.setTarget(formation.getIndex());
        
        int wpIndex = vwpCoordinate.getWaypointIdentifier(flight);
        waypointTimer.setTarget(wpIndex);

        activateTimer.setTarget(formationTimer.getIndex());
        formationTimer.setTarget(waypointTimer.getIndex());
        waypointTimer.setTarget(escortTimer.getIndex());
        escortTimer.setTarget(deleteUpstreamPlanesTimer.getIndex());
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu plane : vwpPlanes.getAllPlanes())
        {
            activate.setObject(plane.getEntity().getIndex());
        }
        
        formation.setObject(vwpPlanes.getLeadActivatePlane().getEntity().getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        activate.write(writer);
        formation.write(writer);

        activateTimer.write(writer);
        formationTimer.write(writer);
        waypointTimer.write(writer);
        escortTimer.write(writer);
        deleteUpstreamPlanesTimer.write(writer);
        
        McuSubtitle.writeSubtitles(subTitleList, writer);
    }
    
    public void linkToNextDeletePlanesTimer(int upstreamVwpTarget)
    {
        deleteUpstreamPlanesTimer.setTarget(upstreamVwpTarget);
    }
    
    public void linkToEscort(int escortAcvtivateTarget)
    {
        escortTimer.setTarget(escortAcvtivateTarget);
    }

    public int getEntryPoint()
    {
        return activateTimer.getIndex();
    }

    public McuFormation getFormation()
    {
        return formation;
    }

    public McuActivate getActivate()
    {
        return activate;
    }

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public McuTimer getFormationTimer()
    {
        return formationTimer;
    }

    public McuTimer getDeleteUpstreamPlanesTimer()
    {
        return deleteUpstreamPlanesTimer;
    }

    public McuTimer getWaypointTimer()
    {
        return waypointTimer;
    }
}
