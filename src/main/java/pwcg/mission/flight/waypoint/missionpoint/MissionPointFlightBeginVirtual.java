package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginVirtual extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private AirStartPattern airStartNearAirfield;
    private McuTimer activationVwpTimer = null;
    private McuWaypoint ingressWaypoint;
    private boolean linkToNextTarget = true;
    private MissionPointSetType missionPointSetType;

    public MissionPointFlightBeginVirtual(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint ingressWaypoint)
    {
        this.flight = flight;
        this.airStartNearAirfield = airStartNearAirfield;
        this.ingressWaypoint = ingressWaypoint;
        this.missionPointSetType = MissionPointSetType.MISSION_POINT_SET_BEGIN_VIRTUAL;
    }
    
    public void createFlightBegin() throws PWCGException, PWCGException 
    {
        createVwpActivationTimer();
        createAirStartWaypoint();  
    }


    private void createVwpActivationTimer() throws PWCGException
    {
        IFlightInformation flightInformation = flight.getFlightInformation();
   
        activationVwpTimer = new McuTimer();
        activationVwpTimer.setName("Activation VWP Timer");
        activationVwpTimer.setDesc("Activation VWP Timer");
        activationVwpTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());        
        activationVwpTimer.setTimer(5);
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        this.activationVwpTimer.setTarget(nextTargetIndex);
    }

    @Override
    public int getEntryPoint() throws PWCGException
    {
        return this.activationVwpTimer.getIndex();
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
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        activationVwpTimer.write(writer);
        super.write(writer);
    }

    private void createAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(flight, airStartNearAirfield, ingressWaypoint);
        super.addWaypoint(airStartWaypoint);
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
