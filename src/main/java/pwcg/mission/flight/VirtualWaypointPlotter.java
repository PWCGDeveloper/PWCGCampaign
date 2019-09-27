package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.VirtualWayPointCoordinate;
import pwcg.mission.mcu.BaseFlightMcu;

public class VirtualWaypointPlotter
{
    public List<VirtualWayPointCoordinate> plotCoordinatesByMinute(Flight flight) throws PWCGException 
    {        
        List<BaseFlightMcu> allMissionPoints = flight.getAllMissionPoints();

        // For the case where a mission has no WPs
        if (allMissionPoints == null || allMissionPoints.size() == 0)
        {
            return this.generateVwpForNoWaypoints(flight);
        }
        
        double cruiseSpeedKPH = flight.getFlightCruisingSpeed();
                        
        // Meters every minute
        double movementPerInterval = (cruiseSpeedKPH / 60) * 1000;
        
        List<VirtualWayPointCoordinate> flightPath = new ArrayList<VirtualWayPointCoordinate>();
        
        // Traverse each leg of the virtual flight, generating virtual coordinates
        // for each leg
        BaseFlightMcu lastMissionPoint = null;
        int wpIndex = 0;
        for (BaseFlightMcu missionPoint: allMissionPoints)
        {
            Coordinate legStartPosition = null;
            if (lastMissionPoint == null)
            {
                PlaneMCU leadPlane = flight.getPlanes().get(0);
                legStartPosition = leadPlane.getPosition().copy();
            }
            else
            {
                legStartPosition = lastMissionPoint.getPosition().copy();
            }
            
            
            List<VirtualWayPointCoordinate> vwpForLeg = generateVwpForLeg(wpIndex, movementPerInterval, missionPoint, legStartPosition);
            flightPath.addAll(vwpForLeg);
            
            lastMissionPoint = missionPoint;
            
            ++wpIndex;
        }

        return flightPath;
    }

    private List<VirtualWayPointCoordinate> generateVwpForLeg(
                    int wpIndex,
                    double movementPerInterval, 
                    BaseFlightMcu missionPoint,
                    Coordinate legStartPosition) throws PWCGException
    {
        List<VirtualWayPointCoordinate> flightPathForLeg = new ArrayList<VirtualWayPointCoordinate>();

        Coordinate legEndPosition = missionPoint.getPosition().copy();

        double wpDistance = MathUtils.calcDist(legStartPosition, legEndPosition);
        double numVirtualWp = (wpDistance / movementPerInterval) + 1;
        double altitudeDelta = (legStartPosition.getYPos() - legEndPosition.getYPos()) / numVirtualWp;
        
        double angle = MathUtils.calcAngle(legStartPosition, legEndPosition);
        
        Coordinate flightPosition = legStartPosition.copy();
        
        for (int numVirtWpBetweenWP = 0; numVirtWpBetweenWP < numVirtualWp; ++numVirtWpBetweenWP)
        {
            double distanceToWP = MathUtils.calcDist(flightPosition, legEndPosition);

            // This is a fudge that will produce inaccuracies,
            // but it should be close enough
            Coordinate nextCoordinate = null;
            if (distanceToWP > movementPerInterval)
            {
                nextCoordinate = MathUtils.calcNextCoord(flightPosition, angle, movementPerInterval);
            }
            else
            {
                nextCoordinate = legEndPosition.copy();
            }

            // Calculate the altitude at this virtual WP
            double altitude = flightPosition.getYPos() + (altitudeDelta * numVirtWpBetweenWP);
            nextCoordinate.setYPos(altitude);

            VirtualWayPointCoordinate virtualWayPointCoordinate = createVwpCoordinate(wpIndex, nextCoordinate, missionPoint.getOrientation());

            flightPathForLeg.add(virtualWayPointCoordinate);
            
            // Now we are at the new coordinate
            flightPosition = nextCoordinate;
        }
        
        return flightPathForLeg;
    }

    private List<VirtualWayPointCoordinate> generateVwpForNoWaypoints(Flight flight)
    {
        List<VirtualWayPointCoordinate> circlingFlightPath = new ArrayList<VirtualWayPointCoordinate>();
        
        PlaneMCU leadPlane = flight.getPlanes().get(0);

        // Plane start is delayed - just hover at the start point
        for (int i = 0; i < 60; ++i)
        {
            VirtualWayPointCoordinate virtualWayPointCoordinate = createVwpCoordinate(0, leadPlane.getPosition(), leadPlane.getOrientation());
    
            circlingFlightPath.add(virtualWayPointCoordinate);
        }
        
        return circlingFlightPath;
    }

    private VirtualWayPointCoordinate createVwpCoordinate(int wpIndex, Coordinate coordinate, Orientation orientation)
    {
        VirtualWayPointCoordinate virtualWayPointCoordinate = new VirtualWayPointCoordinate();
        
        virtualWayPointCoordinate.setCoordinate(coordinate.copy());
        virtualWayPointCoordinate.setOrientation(orientation.copy());
        virtualWayPointCoordinate.setWaypointindex(wpIndex);
        
        return virtualWayPointCoordinate;
    }

}