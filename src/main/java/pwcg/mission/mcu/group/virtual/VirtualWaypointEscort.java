package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointEscort
{
    private VirtualWaypointPlanes vwpPlanes;
    private VirtualWayPointCoordinate vwpCoordinate;
    private VirtualWaypointTriggered vwpActivate;
    private Squadron escortSquadron;

    private McuTimer activateEscortTimer = new McuTimer();
    private McuActivate activateEscort = new McuActivate();
    private McuTimer coverTimer = null;
    private McuCover cover = null;
    private List<PlaneMcu> escortPlanes = new ArrayList<>();
    private int index = IndexGenerator.getInstance().getNextIndex();

    public VirtualWaypointEscort(VirtualWayPointCoordinate vwpCoordinate, Squadron escortSquadron, VirtualWaypointPlanes vwpPlanes, VirtualWaypointTriggered vwpActivate)
    {
        this.vwpCoordinate = vwpCoordinate;
        this.escortSquadron = escortSquadron;
        this.vwpPlanes = vwpPlanes;
        this.vwpActivate = vwpActivate;
    }

    public void buildEscort(FlightInformation vwpEscortFlightInformation) throws PWCGException
    {
        buildEscortPlanes(vwpEscortFlightInformation);
        buildPayloadForEscorts(vwpEscortFlightInformation.getCampaign().getDate());
        buildMcus();
        createTargetAssociations();
        createObjectAssociations();
        linkEscortToActivateFlight();
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
    
            writer.write("  Name = \"VWP Group Escort\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = \"VWP Group Escort\";");
            writer.newLine();

            for (PlaneMcu plane : escortPlanes)
            {
                plane.write(writer);
            }

            activateEscortTimer.write(writer);
            activateEscort.write(writer);
            coverTimer.write(writer);
            cover.write(writer);
    
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private void buildEscortPlanes(FlightInformation vwpEscortFlightInformation) throws PWCGException
    {
        int altitudeOffset = 500;
        VirtualWaypointPlaneBuilder vwpPlaneBuilder = new VirtualWaypointPlaneBuilder(vwpCoordinate, altitudeOffset);
        escortPlanes = vwpPlaneBuilder.buildVwpPlanes(vwpEscortFlightInformation.getPlanes(), vwpEscortFlightInformation.getFormationType());
    }

    private void buildPayloadForEscorts(Date date) throws PWCGException
    {
        for (PlaneMcu escortPlane : escortPlanes)
        {
            escortPlane.buildStandardPlanePayload(date);
        }
    }

    private void buildMcus()
    {
        activateEscort.setPosition(vwpCoordinate.getPosition().copy());
        activateEscort.setOrientation(vwpCoordinate.getOrientation().copy());
        activateEscort.setName("Activate Escort");
        activateEscort.setDesc("Activate Escort");

        activateEscortTimer.setTime(1);
        activateEscortTimer.setPosition(vwpCoordinate.getPosition().copy());
        activateEscortTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateEscortTimer.setName("Activate Escort Timer");
        activateEscortTimer.setDesc("Activate Escort Timer");

        cover = new McuCover();
        cover.setName("Virtual Escort Cover");
        cover.setDesc("Virtual Escort Cover");
        cover.setPosition(vwpCoordinate.getPosition());

        coverTimer = new McuTimer();
        coverTimer.setName("Virtual Escort Cover Timer");
        coverTimer.setDesc("Virtual Escort Cover Timer");
        coverTimer.setPosition(vwpCoordinate.getPosition());
    }

    private void createTargetAssociations()
    {
        activateEscortTimer.setTimerTarget(activateEscort.getIndex());
        activateEscortTimer.setTimerTarget(coverTimer.getIndex());
        coverTimer.setTimerTarget(cover.getIndex());
        cover.setCoverTarget(vwpPlanes.getLeadActivatePlane().getLinkTrId());
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu escortPlane : escortPlanes)
        {
            activateEscort.setObject(escortPlane.getLinkTrId());
        }
        
        cover.setObject(escortPlanes.get(0).getLinkTrId());
    }

    private void linkEscortToActivateFlight()
    {
        vwpActivate.linkToEscort(activateEscortTimer.getIndex());
    }

    public Squadron getEscortSquadron()
    {
        return escortSquadron;
    }

    public List<PlaneMcu> getEscortPlanes()
    {
        return escortPlanes;
    }
}
