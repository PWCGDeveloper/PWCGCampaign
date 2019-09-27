package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class FlightPositionHelperAirStart
{
    public static void createPlayerPlanePositionCloseToFirstWP(Flight flight) throws PWCGException
    {
        createPlanePositionCloseToFirstWP(flight);
    }
    
    public static void createAiPlanePositionAirStartPosition(Flight flight) throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstWaypointPosition();
        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(flight.getTargetCoords(), firstWaypointCoordinate);

        Coordinate startCoordinate = getAiAirStartCoordinate(flight, angleBetweenTargetAndInitialWaypoint);
        McuWaypoint airStartWP = createAiAirStartWaypoint(flight, startCoordinate);
        
        List<McuWaypoint> waypointsAfterAddingAirStart = new ArrayList<>();
        waypointsAfterAddingAirStart.add(airStartWP);
        waypointsAfterAddingAirStart.addAll(flight.getWaypointPackage().getWaypointsForLeadPlane());
        flight.getWaypointPackage().setWaypoints(waypointsAfterAddingAirStart);

        createPlanePositionCloseToFirstWP(flight);
    }

    private static Coordinate getAiAirStartCoordinate(Flight flight, double angleBetweenTargetAndInitialWaypoint) throws PWCGException
    {
        Coordinate airStartCoordinateByOffset = getAiAirStartCoordinateByOffset(flight, angleBetweenTargetAndInitialWaypoint);
        double distanceFromOffset = MathUtils.calcDist(flight.findFirstWaypointPosition(), airStartCoordinateByOffset);
        double distanceFromHome = MathUtils.calcDist(flight.findFirstWaypointPosition(), flight.getHomePosition());
        if (distanceFromOffset < distanceFromHome)
        {
            return airStartCoordinateByOffset;
        }
        else
        {
            return flight.getHomePosition();
        }
    }

    private static Coordinate getAiAirStartCoordinateByOffset(Flight flight, double angleBetweenTargetAndInitialWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int maxKmFromIngress = productSpecific.getMaxDistanceForVirtualFlightAirStart();
        int numKmFromIngress = RandomNumberGenerator.getRandom(maxKmFromIngress);
        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.findFirstWaypointPosition(), angleBetweenTargetAndInitialWaypoint, numKmFromIngress);
        startCoordinate.setYPos(flight.getFlightAltitude());
        return startCoordinate;
    }

    private static void createPlanePositionCloseToFirstWP(Flight flight) throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstWaypointPosition();

        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(flight.getTargetCoords(), firstWaypointCoordinate);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.findFirstWaypointPosition(), angleBetweenTargetAndInitialWaypoint, 1000);
        startCoordinate.setYPos(firstWaypointCoordinate.getYPos());

        Orientation startOrientation = new Orientation(MathUtils.adjustAngle(angleBetweenTargetAndInitialWaypoint, 180));

        AirStartFormationSetter flightPositionHelperAirStart = new AirStartFormationSetter(flight);
        flightPositionHelperAirStart.resetAirStartFormation(startCoordinate, startOrientation);
    }
    
    private static McuWaypoint createAiAirStartWaypoint(Flight flight, Coordinate startCoordinate) throws PWCGException  
    {
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        airStartWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.AIR_START_WAYPOINT.getName());
        airStartWP.setSpeed(flight.getFlightCruisingSpeed());
        airStartWP.setPosition(startCoordinate);
        
       return airStartWP;
    }

}
