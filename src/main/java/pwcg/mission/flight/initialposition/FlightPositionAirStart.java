package pwcg.mission.flight.initialposition;

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

    public static void resetAirStartFormation(IFlight flight, Coordinate startCoordinate) throws PWCGException 
    {
        double startOrientation = MathUtils.calcAngle(startCoordinate, flight.getFlightInformation().getTargetPosition());
        int initialAltitude = WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude());
        
        int i = 0;
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            Coordinate planeCoords = new Coordinate();
            
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int AircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
            planeCoords.setXPos(startCoordinate.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(startCoordinate.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();
            planeCoords.setYPos(initialAltitude + (i * AircraftSpacingVertical));
            
            flight.getFlightPlanes().setPlanePosition(plane.getLinkTrId(), planeCoords, new Orientation(startOrientation), FlightStartPosition.START_IN_AIR);

            ++i;
        }
    }

 }
