package pwcg.mission.mcu.group.virtual;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
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

    public List<PlaneMcu> buildVwpPlanes(List<PlaneMcu> originalPlanes, int formationType) throws PWCGException
    {
        List<PlaneMcu> planesAtActivate = new ArrayList<>();
        
        int numberInFormation = 1;
        for (PlaneMcu originalPlane : originalPlanes)
        {
            PlaneMcu plane = originalPlane.copy();
            plane.setOrientation(new Orientation(vwpCoordinate.calculateAngleToWaypoint()));
            
            setPlaneNumberInFormation(plane, numberInFormation);
            setPlaneDisabled(plane);
            
            if (numberInFormation == 1)
            {
                setLeadPlanePosition(plane);
            }
            else
            {
                linkToFlightLeader(plane, planesAtActivate.get(0).getLinkTrId());
            }
                        
            planesAtActivate.add(plane);
            ++numberInFormation;
        }
        
        setFormationPlanePosition(planesAtActivate, formationType);

        
        return planesAtActivate;
    }

    private void linkToFlightLeader(PlaneMcu plane, int flightLeaderLinkTrId)
    {
        plane.resetTarget(flightLeaderLinkTrId);
    }
    
    private void setLeadPlanePosition(PlaneMcu plane) throws PWCGException
    {
        Coordinate leadPlaneCoordinate = vwpCoordinate.getPosition().copy();
        double planeAltitude = vwpCoordinate.getPosition().getYPos() + altitudeOffset;
        if (planeAltitude < 800.0)
        {
            planeAltitude = 800.0;
        }
        leadPlaneCoordinate.setYPos(planeAltitude);
        plane.setPosition(leadPlaneCoordinate);
        plane.setOrientation(vwpCoordinate.getOrientation().copy());
    }

    private void setFormationPlanePosition(List<PlaneMcu> planesAtActivate, int formationType) throws PWCGException
    {
        FormationGenerator.generatePositionForPlaneInFormation(planesAtActivate, formationType);
    }

    private void setPlaneDisabled(PlaneMcu plane)
    {
        plane.enable(false);
    }

    private void setPlaneNumberInFormation(PlaneMcu plane, int numberInFormation)
    {
        plane.setNumberInFormation(numberInFormation);        
    }
}
