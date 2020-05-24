package pwcg.mission.flight.waypoint.begin;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class AirStartWaypointFactory 
{
    public enum AirStartPattern
    {
        AIR_START_FROM_AIRFIELD,
        AIR_START_NEAR_WAYPOINT
    }

    public static McuWaypoint createAirStart(IFlight flight, AirStartPattern pattern, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            return createAirStartNearWaypoint(flight, referenceWaypointForAirStart);
        }
        else if (pattern == AirStartPattern.AIR_START_NEAR_WAYPOINT)
        {
            return createAirStartNearWaypoint(flight, referenceWaypointForAirStart);
        }
        else
        {
            return createAirStartFromAirfield(flight, referenceWaypointForAirStart);
        }
    }

    private static McuWaypoint createAirStartNearWaypoint(IFlight flight, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        Orientation waypolintOrientation = referenceWaypointForAirStart.getOrientation();
        
        double angleBack = MathUtils.adjustAngle(waypolintOrientation.getyOri(), 180);
        Coordinate airStartPosition = MathUtils.calcNextCoord(referenceWaypointForAirStart.getPosition(), angleBack, 3000.0);
        airStartPosition.setYPos(referenceWaypointForAirStart.getPosition().getYPos());
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(waypolintOrientation.copy());
        airStartWP.setSpeed(flight.getFlightCruisingSpeed());
        return airStartWP;
    }  

    private static McuWaypoint createAirStartFromAirfield(IFlight flight, McuWaypoint referenceWaypointForAirStart) throws PWCGException
    {
        Coordinate airfieldCoordinate = flight.getFlightHomePosition();
        double distaneToTravel = calculateDistanceFromAirStartToIngress(flight, referenceWaypointForAirStart, airfieldCoordinate);
        
        double angleFromFirstWPToAirfield = MathUtils.calcAngle(referenceWaypointForAirStart.getPosition(), airfieldCoordinate);
        Coordinate airStartPosition = MathUtils.calcNextCoord(referenceWaypointForAirStart.getPosition(), angleFromFirstWPToAirfield, distaneToTravel);
        airStartPosition.setYPos(referenceWaypointForAirStart.getPosition().getYPos());
        airStartPosition.setYPos(WaypointType.getAltitudeForWaypointType(WaypointType.AIR_START_WAYPOINT, flight.getFlightInformation().getAltitude()));
        
        McuWaypoint airStartWP = WaypointFactory.createAirStartWaypointType();
        airStartWP.setPosition(airStartPosition);
        airStartWP.setOrientation(new Orientation(MathUtils.adjustAngle(angleFromFirstWPToAirfield, 180)));
        airStartWP.setSpeed(flight.getFlightCruisingSpeed());
        return airStartWP;
    }

    private static double calculateDistanceFromAirStartToIngress(IFlight flight, McuWaypoint referenceWaypointForAirStart, Coordinate airfieldCoordinate)
            throws PWCGException
    {
        double distanceFromFieldToRefrenceWP = MathUtils.calcDist(airfieldCoordinate, referenceWaypointForAirStart.getPosition());
        double distanceForIngressToTarget = MathUtils.calcDist(referenceWaypointForAirStart.getPosition(), flight.getTargetDefinition().getPosition());
        double distanceToTarget = distanceFromFieldToRefrenceWP + distanceForIngressToTarget;
        
        double straightLineistanceForPlayerToReachTargetArea = flight.getMission().getPlayerDistanceToTarget();
        double estimatedDistanceToTarget = straightLineistanceForPlayerToReachTargetArea + 30000;
        
        double distaneToTravel = distanceToTarget;
        if (distanceToTarget > estimatedDistanceToTarget)
        {
            distaneToTravel = estimatedDistanceToTarget;
        }

        if (FlightTypes.isBombingFlight(flight.getFlightInformation().getFlightType()))
        {
            distaneToTravel -= distanceForIngressToTarget;
            int accountForRendezvous = 7000;
            if (distaneToTravel < accountForRendezvous)
            {
                distaneToTravel = accountForRendezvous;
            }
        }
        else
        {
            int accountForDistanceToIngress = 2000;
            if (distaneToTravel < accountForDistanceToIngress)
            {
                distaneToTravel = accountForDistanceToIngress;
            }
        }
        return distaneToTravel;
    }
}
