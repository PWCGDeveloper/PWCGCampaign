package pwcg.mission.flight.waypoint.virtual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;

public class VirtualWaypointPlotter
{
    private final int END_OF_LEG_BACK_OFF_METERS = 1000;
    
    private IFlight flight;
    
    public VirtualWaypointPlotter(IFlight flight)
    {
        this.flight = flight;
    }
    
    public List<VirtualWayPointCoordinate> plotCoordinates() throws PWCGException
    {
        List<VirtualWayPointCoordinate> plotCoordinates = plotAllCoordinates();
        
        VirtualWaypointConsolidator virtualWaypointConsolidator = new VirtualWaypointConsolidator(flight, plotCoordinates);
        List<VirtualWayPointCoordinate> afterConsolidation = virtualWaypointConsolidator.consolidatedVirtualWaypoints();
        
        return afterConsolidation;
    }

    private List<VirtualWayPointCoordinate> plotAllCoordinates() throws PWCGException 
    {        
        List<MissionPoint> allMissionCoordinates = flight.getWaypointPackage().getMissionPoints();
        if (allMissionCoordinates == null || allMissionCoordinates.size() == 0)
        {
            return this.generateVwpForNoWaypoints(flight);
        }
        else
        {
            return generateVwpForFlightPath(allMissionCoordinates);
        }
    }

    private List<VirtualWayPointCoordinate> generateVwpForFlightPath(List<MissionPoint> allMissionPoints) throws PWCGException
    {
        List<VirtualWayPointCoordinate> flightPath = new ArrayList<VirtualWayPointCoordinate>();
        int wpIndex = 0;
        for (int i = 1; i < allMissionPoints.size(); ++i)
        {
            Coordinate legStartPosition = allMissionPoints.get(i-1).getPosition().copy();
            Coordinate legEndPosition = allMissionPoints.get(i).getPosition().copy();
            List<VirtualWayPointCoordinate> vwpForLeg = generateVwpSetForLeg(wpIndex, legStartPosition, legEndPosition);
            flightPath.addAll(vwpForLeg);
            ++wpIndex;
        }
        return flightPath;
    }

    private List<VirtualWayPointCoordinate> generateVwpSetForLeg(
                    int targetWaypointIndex,
                    Coordinate legStartPosition,
                    Coordinate legEndPosition) throws PWCGException
    {
        List<VirtualWayPointCoordinate> flightPathForLeg = new ArrayList<VirtualWayPointCoordinate>();

        List<Coordinate> vwpCoordinatesForLeg = calculateVwpCoordinatesForLeg(legStartPosition, legEndPosition);
        double altitudeDeltaPerSegment = (legStartPosition.getYPos() - legEndPosition.getYPos()) / vwpCoordinatesForLeg.size();
        double legOrientationAngle = MathUtils.calcAngle(legStartPosition, legEndPosition);

        int vwpCounter = 1;
        Coordinate previousVwpPosition = null;
        for (Coordinate vwpPosition : vwpCoordinatesForLeg)
        {
            if (previousVwpPosition == null)
            {
                previousVwpPosition = legStartPosition;
            }
            
            int waitInSecondsForVWP = calculateWaitTimeForVwp(previousVwpPosition, vwpPosition);
            setVwpAltitude(legStartPosition, altitudeDeltaPerSegment, vwpCounter, vwpPosition);
            VirtualWayPointCoordinate virtualWayPointCoordinate = createVwpCoordinate(targetWaypointIndex, vwpPosition, legOrientationAngle, waitInSecondsForVWP);
            flightPathForLeg.add(virtualWayPointCoordinate);
            
            previousVwpPosition = vwpPosition;
            ++vwpCounter;
        }
        
        return flightPathForLeg;
    }

    private void setVwpAltitude(Coordinate legStartPosition, double altitudeDeltaPerSegment, int vwpCounter, Coordinate vwpPosition)
    {
        double altitude = legStartPosition.getYPos() + (altitudeDeltaPerSegment * vwpCounter);
        vwpPosition.setYPos(altitude);
    }

    private int calculateWaitTimeForVwp(Coordinate previousVwpPosition, Coordinate vwpPosition)
    {
        double distanceBetweenVWP = MathUtils.calcDist(previousVwpPosition, vwpPosition);
        int waitInSecondsForVWP = calculateWaitTimeInSecondsForVwp(distanceBetweenVWP);
        return waitInSecondsForVWP;
    }
    
    private List<Coordinate> calculateVwpCoordinatesForLeg(Coordinate legStartPosition, Coordinate legEndPosition) throws PWCGException 
    {
        List<Coordinate> vwpCoordinatesForLeg = new ArrayList<>();
        
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int vwpSeparationDistance = productSpecific.getVwpSeparationDistance();
        
        double backoffAngle = MathUtils.calcAngle(legEndPosition, legStartPosition);
        Coordinate lastVwpOfLegPosition = MathUtils.calcNextCoord(legEndPosition, backoffAngle, END_OF_LEG_BACK_OFF_METERS);
        vwpCoordinatesForLeg.add(lastVwpOfLegPosition);
        
        int numberOfVwpsInLeg = calculateNumberOfVirtualWaypointsForLeg(legStartPosition, legEndPosition, vwpSeparationDistance);
        for (int i = 1; i < numberOfVwpsInLeg; ++i)
        {
            Coordinate nextVwpOfLegDescending = MathUtils.calcNextCoord(lastVwpOfLegPosition, backoffAngle, (vwpSeparationDistance * i));
            vwpCoordinatesForLeg.add(nextVwpOfLegDescending);
        }
        
        Collections.reverse(vwpCoordinatesForLeg);
        return vwpCoordinatesForLeg;
        
    }
    
    private int calculateNumberOfVirtualWaypointsForLeg(Coordinate legStartPosition, Coordinate legEndPosition, int vwpSeparationDistance)
    {
        double distanceToWP = MathUtils.calcDist(legStartPosition, legEndPosition);
        distanceToWP -= END_OF_LEG_BACK_OFF_METERS;
        Double numberOfLegs = Double.valueOf(distanceToWP / vwpSeparationDistance);
        return numberOfLegs.intValue();
    }
    
    private int calculateWaitTimeInSecondsForVwp(double distanceBetweenVWP)
    {
        double cruiseSpeedKPH = flight.getFlightCruisingSpeed();
        double kphToMetersToSecond = 1000.0 / 3600.0;
        double cruiseSpeedMetersPerSecond = cruiseSpeedKPH * kphToMetersToSecond;
        Double waitTime = distanceBetweenVWP / cruiseSpeedMetersPerSecond;
        waitTime += 1.0;
        return waitTime.intValue();
    }


    private List<VirtualWayPointCoordinate> generateVwpForNoWaypoints(IFlight flight)
    {
        List<VirtualWayPointCoordinate> circlingFlightPath = new ArrayList<VirtualWayPointCoordinate>();
        PlaneMcu leadPlane = flight.getFlightPlanes().getFlightLeader();
        for (int i = 0; i < 60; ++i)
        {
            VirtualWayPointCoordinate virtualWayPointCoordinate = createVwpCoordinate(0, leadPlane.getPosition(), 0.0, 7200);
            circlingFlightPath.add(virtualWayPointCoordinate);
        }
        
        return circlingFlightPath;
    }

    private VirtualWayPointCoordinate createVwpCoordinate(int targetWaypointIndex, Coordinate vwpPosition, double orientationAngle, int waitInSecondsForVWP)
    {
        VirtualWayPointCoordinate virtualWayPointCoordinate = new VirtualWayPointCoordinate(flight);
        
        virtualWayPointCoordinate.setCoordinate(vwpPosition.copy());
        virtualWayPointCoordinate.setOrientation(new Orientation(orientationAngle));
        virtualWayPointCoordinate.setWaypointindex(targetWaypointIndex);
        virtualWayPointCoordinate.setWaypointWaitTimeSeconds(waitInSecondsForVWP);
        
        return virtualWayPointCoordinate;
    }
}
