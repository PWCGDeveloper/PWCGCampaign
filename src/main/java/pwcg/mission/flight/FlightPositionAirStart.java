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

public class FlightPositionAirStart
{
    private Flight flight;
    
    public FlightPositionAirStart(Flight flight)
    {
        this.flight = flight;
    }
    
    public void createPlanePositionAirStart() throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            createPlanePositionCloseToFirstStartWP();
        }
        else
        {
            createAiPlanePositionAirStartPosition();
            createPlanePositionCloseToFirstStartWP();
        }
    }
    
    private void createAiPlanePositionAirStartPosition() throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstStartWaypoint().getPosition();
        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(flight.getTargetCoords(), firstWaypointCoordinate);

        Coordinate startCoordinate = getAiAirStartCoordinate(angleBetweenTargetAndInitialWaypoint);
        McuWaypoint airStartWP = createAiAirStartWaypoint(startCoordinate);
        
        List<McuWaypoint> waypointsAfterAddingAirStart = new ArrayList<>();
        waypointsAfterAddingAirStart.add(airStartWP);
        waypointsAfterAddingAirStart.addAll(flight.getWaypointPackage().getWaypointsForLeadPlane());
        flight.getWaypointPackage().setWaypoints(waypointsAfterAddingAirStart);
    }

    private Coordinate getAiAirStartCoordinate(double angleBetweenTargetAndInitialWaypoint) throws PWCGException
    {
        Coordinate airStartCoordinateByOffset = getAiAirStartCoordinateByOffset(angleBetweenTargetAndInitialWaypoint);
        double distanceFromOffset = MathUtils.calcDist(flight.findFirstStartWaypoint().getPosition(), airStartCoordinateByOffset);
        double distanceFromHome = MathUtils.calcDist(flight.findFirstStartWaypoint().getPosition(), flight.getHomePosition());
        if (distanceFromOffset > distanceFromHome)
        {
            airStartCoordinateByOffset = flight.getHomePosition().copy();
        }
        airStartCoordinateByOffset.setYPos(flight.getFlightAltitude());
        return airStartCoordinateByOffset;
    }
    
    private McuWaypoint createAiAirStartWaypoint(Coordinate startCoordinate) throws PWCGException  
    {
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setTriggerArea(McuWaypoint.FLIGHT_AREA);
        airStartWP.setDesc(flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()), WaypointType.AIR_START_WAYPOINT.getName());
        airStartWP.setSpeed(flight.getFlightCruisingSpeed());
        airStartWP.setPosition(startCoordinate);
        
       return airStartWP;
    }

    private Coordinate getAiAirStartCoordinateByOffset(double angleBetweenTargetAndInitialWaypoint) throws PWCGException
    {
        IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int maxKmFromIngress = productSpecific.getMaxDistanceForVirtualFlightAirStart();
        int numKmFromIngress = RandomNumberGenerator.getRandom(maxKmFromIngress);
        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.findFirstStartWaypoint().getPosition(), angleBetweenTargetAndInitialWaypoint, numKmFromIngress);
        startCoordinate.setYPos(flight.getFlightAltitude());
        return startCoordinate;
    }

    private void createPlanePositionCloseToFirstStartWP() throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstStartWaypoint().getPosition();

        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(flight.getTargetCoords(), firstWaypointCoordinate);
        
        Coordinate startCoordinate = MathUtils.calcNextCoord(flight.findFirstStartWaypoint().getPosition(), angleBetweenTargetAndInitialWaypoint, 1000);
        startCoordinate.setYPos(flight.getFlightAltitude());

        Orientation startOrientation = new Orientation(MathUtils.adjustAngle(angleBetweenTargetAndInitialWaypoint, 180));

        AirStartFormationSetter flightPositionHelperAirStart = new AirStartFormationSetter(flight);
        flightPositionHelperAirStart.resetAirStartFormation(startCoordinate, startOrientation);
    }

}
