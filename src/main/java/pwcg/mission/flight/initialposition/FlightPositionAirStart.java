package pwcg.mission.flight.initialposition;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
import pwcg.mission.flight.waypoint.WaypointType;

public class FlightPositionAirStart
{
    public static void createPlanePositionAirStart(IFlight flight) throws PWCGException
    {
        if (flight.getWaypointPackage().getAllWaypoints().size() < 2)
        {
            throw new PWCGException("Flight generated with fewer than two waypoints : " + flight.getFlightType());
        }

        double angleBetweenFirstAndSecondWaypoint = calculateAirStartOrientation(flight);
        Coordinate startCoordinate = calculateAirStartPosition(flight, angleBetweenFirstAndSecondWaypoint);
        resetAirStartFormation(flight, startCoordinate, angleBetweenFirstAndSecondWaypoint);
    }

    private static double calculateAirStartOrientation(IFlight flight) throws PWCGException
    {
        Coordinate firstWaypointPosition = flight.getWaypointPackage().getAllWaypoints().get(0).getPosition();
        Coordinate secondWaypointPosition = flight.getWaypointPackage().getAllWaypoints().get(1).getPosition();

        double angleBetweenFirstAndSecondWaypoint = MathUtils.calcAngle(firstWaypointPosition, secondWaypointPosition);
        return angleBetweenFirstAndSecondWaypoint;
    }

    private static Coordinate calculateAirStartPosition(IFlight flight, double angleBetweenFirstAndSecondWaypoint) throws PWCGException
    {
        Coordinate firstWaypointPosition = flight.getWaypointPackage().getAllWaypoints().get(0).getPosition();
        double angleBetweenFirstAndSecondWaypointReversed = MathUtils.adjustAngle(angleBetweenFirstAndSecondWaypoint, 180);

        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.getCampaign().getCampaignMap(), firstWaypointPosition, angleBetweenFirstAndSecondWaypointReversed, 1000);
        startCoordinate.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude()));
        return startCoordinate;
    }

    private static void resetAirStartFormation(IFlight flight, Coordinate startCoordinate, double angleBetweenFirstAndSecondWaypoint) throws PWCGException
    {
        setFlightOrientation(flight.getFlightPlanes().getPlanes(), angleBetweenFirstAndSecondWaypoint);
        setLeadPlanePosition(flight.getFlightPlanes().getFlightLeader(), startCoordinate);
        FormationGenerator formationGenerator = new FormationGenerator(flight.getCampaign());
        formationGenerator.generatePositionForPlaneInFormation(flight.getFlightPlanes().getPlanes(), flight.getFlightInformation().getFormationType());
    }

    private static void setFlightOrientation(List<PlaneMcu> planes, double orientationAngle)
    {
        for (PlaneMcu plane : planes)
        {
            plane.setOrientation(new Orientation(orientationAngle));
        }
    }

    private static void setLeadPlanePosition(PlaneMcu leadPlane, Coordinate startCoordinate) throws PWCGException
    {
        Coordinate leadPlaneCoordinate = startCoordinate.copy();
        double planeAltitude = startCoordinate.getYPos();
        if (planeAltitude < 800.0)
        {
            planeAltitude = 800.0;
        }
        leadPlaneCoordinate.setYPos(planeAltitude);
        leadPlane.setPosition(leadPlaneCoordinate);
    }
}
