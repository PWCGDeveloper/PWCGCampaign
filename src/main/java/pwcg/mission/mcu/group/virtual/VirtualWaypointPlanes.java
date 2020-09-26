package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
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
        for (int i = 0; i < flight.getFlightPlanes().getFlightSize(); ++i)
        {
            PlaneMcu plane = flight.getFlightPlanes().getPlanes().get(i).copy();
            setPlaneIndex(plane);
            setPlanePosition(i, plane);
            setPlaneDisabled(plane);
            
            planesAtActivate.add(plane);
        }
        
        flight.getWaypointPackage().addObjectToAllMissionPoints(planesAtActivate.get(0));
    }

    private void setPlaneIndex(PlaneMcu plane)
    {
        int planeIndex = IndexGenerator.getInstance().getNextIndex();
        int planeLinkIndex = IndexGenerator.getInstance().getNextIndex();
        
        plane.setIndex(planeIndex);
        plane.setLinkTrId(planeLinkIndex);
        
        plane.getEntity().setIndex(planeLinkIndex);
        plane.getEntity().setMisObjID(planeIndex);
    }

    private void setPlanePosition(int i, PlaneMcu plane) throws PWCGException
    {
        Coordinate planeCoordinate = FormationGenerator.generatePositionForPlaneInFormation(vwpCoordinate.getOrientation(), vwpCoordinate.getPosition(), i);
        double planeAltitude = vwpCoordinate.getPosition().getYPos() + (70 * i);
        if (planeAltitude < 800.0)
        {
            planeAltitude = 800.0 + (70 * i);
        }
        planeCoordinate.setYPos(planeAltitude);
        plane.setPosition(planeCoordinate);
    }

    private void setPlaneDisabled(PlaneMcu plane)
    {
        plane.getEntity().setEnabled(0);
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
