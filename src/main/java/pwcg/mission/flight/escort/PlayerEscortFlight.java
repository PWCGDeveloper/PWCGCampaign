package pwcg.mission.flight.escort;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuForceComplete;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortFlight extends Flight
{
    protected McuCover cover = null;
    protected McuTimer coverTimer = null;

    protected McuTimer forceCompleteTimer = null;
    protected McuForceComplete forceCompleteEntity = null;

    protected McuTimer escortedFlightWaypointTimer = null;
    protected McuTimer egressTimer = null;

    public PlayerEscortFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super(flightInformation, missionBeginUnit);
    }

    @Override
    public void createUnitMission() throws PWCGException
    {
        super.createUnitMission();
        createUnitMissionEscortSpecific();
    }

    public void createUnitMissionEscortSpecific() throws PWCGException
    {
        createTimers();
        createForceComplete();
        createActivation();
        createCover();
        
        FlightPositionSetter.setFlightInitialPosition(this);
    }

    @Override
    protected void createFlightSpecificTargetAssociations()
    {
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());
    }

     @Override
    public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException
    {
        PlayerEscortWaypoints waypointGenerator = new PlayerEscortWaypoints(this);
        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        return waypointList;
    }

    protected void createForceComplete() throws PWCGException
    {
        // Deactivate the cover entity
        forceCompleteEntity = new McuForceComplete();
        forceCompleteEntity.setName("Escort Cover Force Complete");
        forceCompleteEntity.setDesc("Escort Cover Force Complete");
        forceCompleteEntity.setOrientation(new Orientation());
        forceCompleteEntity.setPosition(getTargetPosition().copy());
        forceCompleteEntity.setObject(planes.get(0).getEntity().getIndex());

        forceCompleteTimer = new McuTimer();
        forceCompleteTimer.setName("Escort Cover Force Complete Timer");
        forceCompleteTimer.setDesc("Escort Cover Force Complete Timer");
        forceCompleteTimer.setOrientation(new Orientation());
        forceCompleteTimer.setPosition(getTargetPosition().copy());
        forceCompleteTimer.setTimer(2);
        forceCompleteTimer.setTarget(forceCompleteEntity.getIndex());
    }

    protected void createTimers() throws PWCGException
    {
        escortedFlightWaypointTimer = new McuTimer();
        escortedFlightWaypointTimer.setName("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setDesc("Escort Cover Ingress Timer");
        escortedFlightWaypointTimer.setOrientation(new Orientation());
        escortedFlightWaypointTimer.setPosition(getTargetPosition().copy());
        escortedFlightWaypointTimer.setTimer(2);

        egressTimer = new McuTimer();
        egressTimer.setName("Escort Cover Egress Timer");
        egressTimer.setDesc("Escort Cover Egress Timer");
        egressTimer.setOrientation(new Orientation());
        egressTimer.setPosition(getTargetPosition().copy());
        egressTimer.setTimer(2);
    }
    

    private void createCover() throws PWCGException
    {
        Coordinate rendevousPoint = flightInformation.getTargetPosition();

        // Cover the escorted flight
        cover = new McuCover();
        cover.setPosition(rendevousPoint);
        cover.setObject(planes.get(0).getEntity().getIndex());

        // The cover timer.
        // Activate the cover command
        // Activate the escorted squadron
        coverTimer = new McuTimer();
        coverTimer.setName("Cover Timer for " + getSquadron().determineDisplayName(getCampaign().getDate()));
        coverTimer.setDesc("Cover " + getSquadron().determineDisplayName(getCampaign().getDate()));
        coverTimer.setPosition(rendevousPoint);
        coverTimer.setTarget(cover.getIndex());
    }


    @Override
    protected void createActivation() throws PWCGException
    {
        // Do this again to compensate for the movement of the escorted flight
        FlightPositionSetter.setFlightInitialPosition(this);
        super.createActivation();
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        super.write(writer);

        coverTimer.write(writer);
        cover.write(writer);
        escortedFlightWaypointTimer.write(writer);

        forceCompleteTimer.write(writer);
        forceCompleteEntity.write(writer);
        egressTimer.write(writer);
    }

    public McuCover getCover()
    {
        return cover;
    }

    public McuTimer getCoverTimer()
    {
        return coverTimer;
    }

    public McuTimer getForceCompleteTimer()
    {
        return forceCompleteTimer;
    }

    public McuForceComplete getForceCompleteEntity()
    {
        return forceCompleteEntity;
    }

    public McuTimer getEscortedFlightWaypointTimer()
    {
        return escortedFlightWaypointTimer;
    }

    public McuTimer getEgressTimer()
    {
        return egressTimer;
    }
}
