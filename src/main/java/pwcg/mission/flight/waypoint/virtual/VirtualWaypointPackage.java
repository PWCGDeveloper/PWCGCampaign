package pwcg.mission.flight.waypoint.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.missionpoint.IVirtualActivate;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private List<VirtualWaypoint> virtualWaypoints = new ArrayList<>();

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override   
    public void buildVirtualWaypoints() throws PWCGException    
    {   
        virtualWaypoints = new ArrayList<>();
        
        generateVirtualWaypoints(); 
        linkVirtualWaypointToMissionBegin();
        if (FlightTypes.isFlightWithTargetArea(flight.getFlightType()))
        {
            VirtualWaypointFlightResolver.resolveForAttackFlight(flight, this);
        }
    }   

    @Override
    public void addDelayForPlayerDelay(Mission mission) throws PWCGException
    {
        VirtualAdditionalTimeCalculator additionalTimeCalculator = new VirtualAdditionalTimeCalculator();
        int additionalTime = additionalTimeCalculator.addDelayForPlayerDelay(mission, flight);
        if (additionalTime > 30)
        {
            virtualWaypoints.get(0).addAdditionalTime(additionalTime);
        }
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (IVirtualWaypoint virtualWaypoint : virtualWaypoints)
        {
            virtualWaypoint.write(writer);
        }
    }

    @Override
    public List<VirtualWaypoint> getVirtualWaypoints()
    {
        return this.virtualWaypoints;
    }    

    private void generateVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
    }

    private void linkVirtualWaypointToMissionBegin() throws PWCGException   
    {   
        VirtualWaypoint firstVirtualWayPoint = virtualWaypoints.get(0);        
        IVirtualActivate activateMissionPointSet = (IVirtualActivate)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ACTIVATE);
        activateMissionPointSet.linkMissionBeginToFirstVirtualWaypoint(firstVirtualWayPoint.getEntryPoint());
    }
}
