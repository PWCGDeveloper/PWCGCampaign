package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginAirStart extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private AirStartPattern airStartPattern;
    private McuWaypoint referenceWaypointForAirStart;

    private McuTimer formationTimer = null;
    private McuFormation formationEntity = null;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightBeginAirStart(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint referenceWaypointForAirStart)
    {
        this.flight = flight;
        this.airStartPattern = airStartNearAirfield;
        this.referenceWaypointForAirStart = referenceWaypointForAirStart;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_BEGIN_AIR;
    }

    public void createFlightBegin() throws PWCGException, PWCGException
    {
        createFormation();
        createAirStartWaypoint();  
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        super.getLastWaypoint().setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return formationTimer.getIndex();
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

    @Override
    public void finalizeMissionPointSet(PlaneMcu plane) throws PWCGException
    {
        super.finalizeMissionPointSet(plane);
        createTargetAssociations();
        createObjectAssociations(plane);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        formationTimer.write(writer);
        formationEntity.write(writer);
        super.write(writer);
    }

    private void createFormation() throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();

        formationEntity = new McuFormation(flightInformation.getFormationType(), McuFormation.FORMATION_DENSITY_LOOSE);
        formationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition());

        formationTimer = new McuTimer();
        formationTimer.setName(flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + ": Formation Timer");
        formationTimer.setDesc("Formation timer entity for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        formationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
        formationTimer.setTime(2);
    }

    private void createAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(flight, airStartPattern, referenceWaypointForAirStart);
        super.addWaypoint(airStartWaypoint);
        
    }

    private void createTargetAssociations() throws PWCGException
    {
        formationTimer.setTarget(formationEntity.getIndex());
        formationTimer.setTarget(this.getFirstWaypoint().getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        formationEntity.setObject(plane.getLinkTrId());
    }

    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        allWaypoints.addAll(waypoints.getWaypoints());
        return allWaypoints;
    }

    @Override
    public List<BaseFlightMcu> getAllFlightPoints()
    {
        List<BaseFlightMcu> allFlightPoints = new ArrayList<>();
        allFlightPoints.addAll(waypoints.getWaypoints());
        return allFlightPoints;
    }

    @Override
    public MissionPointSetType getMissionPointSetType()
    {
        return missionPointSetType;
    }
}
