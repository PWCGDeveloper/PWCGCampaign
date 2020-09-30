package pwcg.mission.mcu.group.virtual;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;

public class VirtualWaypointPlaneBuilder
{
    private VirtualWayPointCoordinate vwpCoordinate;
    private int altitudeOffset;

    public VirtualWaypointPlaneBuilder(VirtualWayPointCoordinate vwpCoordinate, int altitudeOffset)
    {
        this.vwpCoordinate = vwpCoordinate;
        this.altitudeOffset = altitudeOffset;
    }

    public List<PlaneMcu> buildVwpPlanes(List<PlaneMcu> sourcePlane) throws PWCGException
    {
        List<PlaneMcu> planesAtActivate = new ArrayList<>();
        
        for (int i = 0; i < sourcePlane.size(); ++i)
        {
            PlaneMcu plane = sourcePlane.get(i).copy();
            setPlaneIndex(plane);
            setPlaneNumberInFormation(i, plane);
            setPlanePosition(i, plane);
            setPlaneDisabled(plane);
            
            if (i > 0)
            {
                linkToFlightLeader(plane, planesAtActivate.get(0).getLinkTrId());
            }
                        
            planesAtActivate.add(plane);
        }
        
        return planesAtActivate;
    }

    private void linkToFlightLeader(PlaneMcu plane, int linkTrId)
    {
        plane.getEntity().clearTargets();
        plane.getEntity().setTarget(linkTrId);
        
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
        double planeAltitude = vwpCoordinate.getPosition().getYPos() + altitudeOffset + (70 * i);
        if (planeAltitude < 800.0)
        {
            planeAltitude = 800.0 + (70 * i);
        }
        planeCoordinate.setYPos(planeAltitude);
        plane.setPosition(planeCoordinate);
        plane.setOrientation(vwpCoordinate.getOrientation().copy());
    }
    
    private void setPlaneDisabled(PlaneMcu plane)
    {
        plane.getEntity().setEnabled(0);
    }

    private void setPlaneNumberInFormation(int i, PlaneMcu plane)
    {
        plane.setNumberInFormation(i+1);        
    }
}
