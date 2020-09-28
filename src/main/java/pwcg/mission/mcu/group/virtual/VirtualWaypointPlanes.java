package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public class VirtualWaypointPlanes
{
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;

    private List<PlaneMcu> planesAtActivate = new ArrayList<>();

    public VirtualWaypointPlanes(IFlight flight, VirtualWayPointCoordinate vwpCoordinate)
    {
        this.flight = flight;
        this.vwpCoordinate = vwpCoordinate;
    }

    public void build() throws PWCGException
    {
        buildPlanesAtActivate();
    }

    private void buildPlanesAtActivate() throws PWCGException
    {
        int altitudeOffset = 0;
        VirtualWaypointPlaneBuilder vwpPlaneBuilder = new VirtualWaypointPlaneBuilder(vwpCoordinate, altitudeOffset);
        planesAtActivate = vwpPlaneBuilder.buildVwpPlanes(flight.getFlightPlanes().getPlanes());
        flight.getWaypointPackage().addObjectToAllMissionPoints(planesAtActivate.get(0));
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (PlaneMcu plane : planesAtActivate)
        {
            plane.write(writer);
        }
    }
    
    public PlaneMcu getLeadActivatePlane()
    {
        return planesAtActivate.get(0);
    }
    
    public List<PlaneMcu> getAllPlanes()
    {
        return planesAtActivate;
    }

}
