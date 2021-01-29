package pwcg.mission.target.locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class TargetLocatorOpposing
{
    private IFlight opposingFlight;

    public TargetLocatorOpposing (IFlight opposingFlight)
    {
        this.opposingFlight = opposingFlight;
    }


    public Coordinate getOpposingLocationFromTargetWaypoints() throws PWCGException
    {
        List<McuWaypoint> opposingTargetWaypoints = new ArrayList<>();
        opposingTargetWaypoints.addAll(opposingFlight.getWaypointPackage().getTargetWaypoints());
        if (opposingTargetWaypoints.isEmpty())
        {
            throw new PWCGException("No target waypoints found for opposing flight of type " + opposingFlight.getFlightInformation().getFlightType());
        }
        Collections.shuffle(opposingTargetWaypoints);
        return opposingTargetWaypoints.get(0).getPosition();
    }
}
