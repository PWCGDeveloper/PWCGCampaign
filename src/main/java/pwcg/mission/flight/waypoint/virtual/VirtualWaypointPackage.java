package pwcg.mission.flight.waypoint.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.virtual.VirtualWaypointGenerator;
import pwcg.mission.mcu.group.IVirtualWaypoint;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private List<VirtualWayPoint> virtualWaypoints = new ArrayList<>();

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override
    public void buildVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
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
    public List<VirtualWayPoint> getVirtualWaypoints()
    {
        return this.virtualWaypoints;
    }
}
