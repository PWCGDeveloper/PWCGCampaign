package pwcg.mission.flight.initialposition;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightStartPosition;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.WaypointType;

public class FlightPositionAirStart
{
    public static void createPlanePositionAirStart(IFlight flight) throws PWCGException
    {
        if (flight.getWaypointPackage().getAllWaypoints().size() < 2)
        {
            throw new PWCGException("Flight generated with fewer than two waypoints : " + flight.getFlightType());
        }
        
        Coordinate firstWaypointPosition = flight.getWaypointPackage().getAllWaypoints().get(0).getPosition();
        Coordinate secondWaypointPosition = flight.getWaypointPackage().getAllWaypoints().get(1).getPosition();

        double angleBetweenFirstAndSecondWaypoint = MathUtils.calcAngle(firstWaypointPosition, secondWaypointPosition);
        double angleBetweenFirstAndSecondWaypointReversed = MathUtils.adjustAngle(angleBetweenFirstAndSecondWaypoint, 180);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(firstWaypointPosition, angleBetweenFirstAndSecondWaypointReversed, 1000);
        startCoordinate.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude()));
        resetAirStartFormation(flight, startCoordinate);
    }

    
    private static void resetAirStartFormation(IFlight flight, Coordinate startCoordinate) throws PWCGException 
    {
        if (flight.getFlightPlanes().getPlanes().size() <= 4)
        {
            resetAirStartFormationForSmallFormation(flight, startCoordinate);
        }
        else
        {
            resetAirStartFormationForLargeFormation(flight, startCoordinate);
        }
    }
    
    private static void resetAirStartFormationForSmallFormation(IFlight flight, Coordinate startCoordinate) throws PWCGException 
    {
        int initialAltitude = WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude());
        resetAirStartFormationForUnit(flight.getFlightPlanes().getPlanes(), flight, startCoordinate, initialAltitude);
    }
    
    private static void resetAirStartFormationForLargeFormation(IFlight flight, Coordinate startCoordinate) throws PWCGException 
    {
        int numPlanesInFlight = determineNumberOfPlanesInFlight(flight);
        int numFlights = determineNumberOfFlights(flight, numPlanesInFlight);

        double startOrientation = MathUtils.calcAngle(startCoordinate, flight.getTargetDefinition().getPosition());
        double flightPlacementAngle = MathUtils.adjustAngle(startOrientation, 180);
        int initialAltitude = WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude());
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int aircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
        int aircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();

        for (int i = 0; i < numFlights; ++i)
        {
            int startIndex = numPlanesInFlight * i;
            int endIndex = startIndex + numPlanesInFlight;
            if (endIndex > flight.getFlightPlanes().getPlanes().size())
            {
                endIndex = flight.getFlightPlanes().getPlanes().size();
            }
            
            List<PlaneMcu> planesInFlight = new ArrayList<>();
            for (int index = startIndex; index < endIndex; ++index)
            {
                planesInFlight.add(flight.getFlightPlanes().getPlanes().get(index));
            }
            
            Coordinate unitStartCoordinate = MathUtils.calcNextCoord(startCoordinate, flightPlacementAngle, (i * aircraftSpacingHorizontal * 2));
            int flightInitialAltitude = (initialAltitude + (aircraftSpacingVertical));
            resetAirStartFormationForUnit(planesInFlight, flight, unitStartCoordinate, flightInitialAltitude);
        }

    }

    private static int determineNumberOfPlanesInFlight(IFlight flight)
    {
        int remainingPlanesFlightOfFour = (flight.getFlightPlanes().getPlanes().size() % 4);
        int remainingPlanesFlightOfThree = (flight.getFlightPlanes().getPlanes().size() % 3);
        
        int numPlanesInFlight = 4;
        if (remainingPlanesFlightOfFour == 0)
        {
            numPlanesInFlight = 4;
        }
        else if (remainingPlanesFlightOfThree == 0)
        {
            numPlanesInFlight = 3;
        }
        else if (remainingPlanesFlightOfFour == 1)
        {
            numPlanesInFlight = 3;
        }
        else if (remainingPlanesFlightOfThree == 1)
        {
            numPlanesInFlight = 4;
        }
        return numPlanesInFlight;
    }

    private static int determineNumberOfFlights(IFlight flight, int numPlanesInFlight)
    {
        int numFlights = flight.getFlightPlanes().getPlanes().size() / numPlanesInFlight;
        int remainingPlanes = flight.getFlightPlanes().getPlanes().size() % numPlanesInFlight;
        if (remainingPlanes != 0)
        {
            ++numFlights;
        }
        return numFlights;
    }
    
    
    private static void resetAirStartFormationForUnit(List<PlaneMcu> planes, IFlight flight, Coordinate startCoordinate, int flightAltitude) throws PWCGException 
    {
        double startOrientation = MathUtils.calcAngle(startCoordinate, flight.getTargetDefinition().getPosition());
        
        int i = 0;
        for (PlaneMcu plane : planes)
        {            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int aircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
            int aircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();

            double placementAngle = MathUtils.adjustAngle(startOrientation, 20);
            Coordinate planeCoords = MathUtils.calcNextCoord(startCoordinate, placementAngle, (i * aircraftSpacingHorizontal));
            planeCoords.setYPos(flightAltitude + (i * aircraftSpacingVertical));
            
            flight.getFlightPlanes().setPlanePosition(plane.getLinkTrId(), planeCoords, new Orientation(startOrientation), FlightStartPosition.START_IN_AIR);

            ++i;
        }
    }
 }
