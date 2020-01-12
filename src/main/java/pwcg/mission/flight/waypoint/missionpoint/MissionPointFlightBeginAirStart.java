package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginAirStart extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private AirStartPattern airStartNearAirfield;
    private McuWaypoint ingressWaypoint;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer activationTimer = null;
    private McuActivate activationEntity = null;
    private McuTimer formationTimer = null;
    private McuFormation formationEntity = null;
    private boolean linkToNextTarget = true;

    public MissionPointFlightBeginAirStart(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint ingressWaypoint)
    {
        this.flight = flight;
        this.airStartNearAirfield = airStartNearAirfield;
        this.ingressWaypoint = ingressWaypoint;
    }

    public void createFlightBegin() throws PWCGException, PWCGException
    {
        this.missionBeginUnit = new MissionBeginUnit(flight.getFlightData().getFlightHomePosition());

        createActivation();
        createFormation();
        createAirStartWaypoint();  
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        formationTimer.setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return activationTimer.getIndex();
    }

    @Override
    public List<MissionPoint> getFlightMissionPoints()
    {
        return super.getWaypointsAsMissionPoints();
    }

    @Override
    public void disableLinkToNextTarget()
    {
        linkToNextTarget = false;
    }

    @Override
    public boolean isLinkToNextTarget()
    {
        return linkToNextTarget;
    }

    private void createActivation() throws PWCGException
    {
        IFlightInformation flightInformation = flight.getFlightData().getFlightInformation();

        activationEntity = new McuActivate();
        activationEntity.setName("Activate");
        activationEntity.setDesc("Activate entity");
        activationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

        activationTimer = new McuTimer();
        activationTimer.setName("Activation Timer");
        activationTimer.setDesc("Activation Timer");
        activationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
    }

    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        super.finalize(plane);
        createTargetAssociations();
        createObjectAssociations(plane);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        missionBeginUnit.write(writer);
        activationTimer.write(writer);
        activationEntity.write(writer);
        formationTimer.write(writer);
        formationEntity.write(writer);
    }

    private void createFormation() throws PWCGException
    {
        IFlightInformation flightInformation = flight.getFlightData().getFlightInformation();

        formationEntity = new McuFormation();
        formationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition());

        formationTimer = new McuTimer();
        formationTimer.setName(flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + ": Formation Timer");
        formationTimer.setDesc("Formation timer entity for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        formationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
        formationTimer.setTimer(2);
    }

    private void createAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(flight, airStartNearAirfield, ingressWaypoint);
        super.addWaypoint(airStartWaypoint);
        
    }

    private void createTargetAssociations() throws PWCGException
    {
        missionBeginUnit.linkToMissionBegin(activationTimer.getIndex());

        activationTimer.setTarget(activationEntity.getIndex());
        activationTimer.setTarget(formationTimer.getIndex());

        formationTimer.setTarget(formationEntity.getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        activationEntity.setObject(plane.getLinkTrId());
        formationEntity.setObject(plane.getLinkTrId());
    }

    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        return allWaypoints;
    }
}
