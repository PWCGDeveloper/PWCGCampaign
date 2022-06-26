package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointTriggered
{
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointPlanes vwpPlanes;

    private McuFormation formation;
    private McuActivate activate = new McuActivate();

    private McuTimer activateTimer = new McuTimer();
    private McuTimer formationTimer = new McuTimer();
    private McuTimer attackTimer = new McuTimer();
    private McuTimer waypointTimer = new McuTimer();
    private McuTimer escortTimer = new McuTimer();
    private int vwpIdentifier = 1;
    private int index = IndexGenerator.getInstance().getNextIndex();

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    public VirtualWaypointTriggered(IFlight flight, VirtualWayPointCoordinate vwpCoordinate, VirtualWaypointPlanes vwpPlanes, int vwpIdentifier)
    {
        this.flight = flight;
        this.vwpCoordinate = vwpCoordinate;
        this.vwpPlanes = vwpPlanes;
        this.vwpIdentifier = vwpIdentifier;
    }

    public void build() throws PWCGException
    {
        buildMcus();
        makeSubtitles();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildMcus() throws PWCGException
    {
        activate.setPosition(vwpCoordinate.getPosition().copy());
        activate.setOrientation(vwpCoordinate.getOrientation().copy());
        activate.setName("VWP Activate");
        activate.setDesc("VWP Activate");
        
        activateTimer.setTime(1);
        activateTimer.setPosition(vwpCoordinate.getPosition().copy());
        activateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateTimer.setName("VWP Activate Timer");
        activateTimer.setDesc("VWP Activate Timer");
        
        attackTimer.setTime(1);
        attackTimer.setPosition(vwpCoordinate.getPosition().copy());
        attackTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        attackTimer.setName("VWP Attack Timer");
        attackTimer.setDesc("VWP Attack Timer");

        waypointTimer.setTime(1);
        waypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        waypointTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        waypointTimer.setName("VWP Waypoint Timer");
        waypointTimer.setDesc("VWP Waypoint Timer");

        escortTimer.setTime(1);
        escortTimer.setPosition(vwpCoordinate.getPosition().copy());
        escortTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        escortTimer.setName("VWP Escort Timer");
        escortTimer.setDesc("VWP Escort Timer");

        formationTimer.setPosition(vwpCoordinate.getPosition().copy());
        formationTimer.setName("VWP Formation Timer");
        formationTimer.setDesc("VWP Formation Timer");

        formation = new McuFormation(flight.getFlightInformation().getFormationType(), McuFormation.FORMATION_DENSITY_LOOSE);
        formation.setPosition(vwpCoordinate.getPosition().copy());
    }

    private void makeSubtitles() throws PWCGException
    {
        Coordinate subtitlePosition = vwpCoordinate.getPosition().copy();
        McuSubtitle activatedSubtitle = McuSubtitle.makeActivatedSubtitle("VWP Activate: " + vwpIdentifier + " " + vwpPlanes.getLeadActivatePlane().getName(), subtitlePosition);
        activateTimer.setTimerTarget(activatedSubtitle.getIndex());
        subTitleList.add(activatedSubtitle);
    }

    private void createTargetAssociations()
    {
        activateTimer.setTimerTarget(activate.getIndex());
        formationTimer.setTimerTarget(formation.getIndex());

        triggerAttackMcuForPlanes();

        int wpIndex = vwpCoordinate.getWaypointIdentifier();
        waypointTimer.setTimerTarget(wpIndex);

        activateTimer.setTimerTarget(formationTimer.getIndex());
        formationTimer.setTimerTarget(attackTimer.getIndex());
        attackTimer.setTimerTarget(waypointTimer.getIndex());
        waypointTimer.setTimerTarget(escortTimer.getIndex());
    }

    private void triggerAttackMcuForPlanes()
    {
        for (PlaneMcu plane : vwpPlanes.getAllPlanes())
        {
            attackTimer.setTimerTarget(plane.getFighterAttack().getIndex());
        }
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu plane : vwpPlanes.getAllPlanes())
        {
            activate.setObject(plane.getLinkTrId());
        }
        
        formation.setObject(vwpPlanes.getLeadActivatePlane().getLinkTrId());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Virtual WP Triggered\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Virtual WP Triggered\";");
            writer.newLine();
    
            activate.write(writer);
            formation.write(writer);
            activateTimer.write(writer);
            formationTimer.write(writer);
            attackTimer.write(writer);            
            waypointTimer.write(writer);
            escortTimer.write(writer);
            
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
    
    public void linkToEscort(int escortAcvtivateTarget)
    {
        escortTimer.setTimerTarget(escortAcvtivateTarget);
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

    public McuTimer getWaypointTimer()
    {
        return waypointTimer;
    }

    public McuTimer getAttackTimer()
    {
        return attackTimer;
    }

    public McuTimer getEscortTimer()
    {
        return escortTimer;
    }    
    
}
