package pwcg.mission.flight.waypoint.missionpoint;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class MissionPointFlightBeginVirtual extends MissionPointSetSingleWaypointSet implements IMissionPointSet
{
    private IFlight flight;
    private AirStartPattern airStartNearAirfield;
    private McuWaypoint ingressWaypoint;

    private MissionBeginUnit missionBeginUnit;
    private McuTimer activationTimer = null;
    private McuActivate activationEntity = null;
    private IVirtualWaypointPackage virtualWaypointPackage;
    private boolean linkToNextTarget = true;

    public MissionPointFlightBeginVirtual(IFlight flight, AirStartPattern airStartNearAirfield, McuWaypoint ingressWaypoint)
    {
        this.flight = flight;
        this.airStartNearAirfield = airStartNearAirfield;
        this.ingressWaypoint = ingressWaypoint;
    }
    
    public void createFlightBegin() throws PWCGException, PWCGException 
    {
        this.missionBeginUnit = new MissionBeginUnit(flight.getFlightData().getFlightHomePosition());
        this.virtualWaypointPackage = new VirtualWaypointPackage(flight);        
        createActivation();  
        createAirStartWaypoint();  
    }

    @Override
    public void setLinkToNextTarget(int nextTargetIndex) throws PWCGException
    {
        activationTimer.setTarget(nextTargetIndex);
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

    @Override
    public void finalize(PlaneMcu plane) throws PWCGException
    {
        virtualWaypointPackage.buildVirtualWaypoints();                    

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
        virtualWaypointPackage.write(writer);
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

    private void createAirStartWaypoint() throws PWCGException
    {
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(flight, airStartNearAirfield, ingressWaypoint);
        super.addWaypoint(airStartWaypoint);
        
    }

    private void createTargetAssociations()
    {
        missionBeginUnit.linkToMissionBegin(activationTimer.getIndex());
        activationTimer.setTarget(activationEntity.getIndex());
        activationTimer.setTarget(activationEntity.getIndex());
    }

    private void createObjectAssociations(PlaneMcu plane)
    {
        activationEntity.setObject(plane.getLinkTrId());
    }
    
    @Override
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> allWaypoints = new ArrayList<>();
        return allWaypoints;
    }

    @Override
    public boolean containsWaypoint(long waypointIdToFind)
    {
        return false;
    }

    @Override
    public McuWaypoint getWaypointById(long waypointId) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight begin");                
    }

    @Override
    public void replaceWaypoint(McuWaypoint waypoint) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight begin");                
    }

    @Override
    public void addWaypointAfterWaypoint(McuWaypoint newWaypoint, long waypointIdAfter) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight begin");                        
    }

    @Override
    public void addWaypointBeforeWaypoint(McuWaypoint newWaypoint, long waypointIdBefore) throws PWCGException
    {
        throw new PWCGException("No waypoints in flight begin");                        
    }
}
