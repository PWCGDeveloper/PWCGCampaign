package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;

public class FlightPositionRendezvous
{
    private Flight flight;
    
    public FlightPositionRendezvous(Flight flight)
    {
        this.flight = flight;
    }
    
    public void createPlanePositionAtRendezvous(Coordinate rendezvousCoordinate) throws PWCGException
    {
        Coordinate firstWaypointCoordinate = flight.findFirstStartWaypoint().getPosition();
        double angleBetweenTargetAndInitialWaypoint = MathUtils.calcAngle(rendezvousCoordinate, firstWaypointCoordinate);
        Orientation startOrientation = new Orientation(MathUtils.adjustAngle(angleBetweenTargetAndInitialWaypoint, 180));

        AirStartFormationSetter flightPositionHelperAirStart = new AirStartFormationSetter(flight);
        flightPositionHelperAirStart.resetAirStartFormation(rendezvousCoordinate, startOrientation);
    }
}
