package pwcg.mission.flight.waypoint.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.BaseFlightMcu;

/**
 * A VWP Coordinate contains the position of a VWP and the associated action point (WP, ATtackArea, etc) 
 * that the flight will transition to.  It is used to create a VWP group for the mission file.
 * 
 * @author Patrick Wilson
 *
 */
public class VirtualWayPointCoordinate
{
    private IFlight flight;
    private Coordinate coordinate = new Coordinate();
    private Orientation orientation = new Orientation();
    private int waypointindex = 0;
    private int waypointWaitTimeSeconds = 0;
    
    public VirtualWayPointCoordinate(IFlight flight)
    {
        this.flight = flight;
    }
    
    public Coordinate getPosition()
    {
        return coordinate.copy();
    }
    
    public void setCoordinate(Coordinate coordinate)
    {
        this.coordinate = coordinate;
    }
    
    public Orientation getOrientation()
    {
        return orientation;
    }
    
    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public int getWaypointArrayIndex()
    {
        return waypointindex;
    }

    public void setWaypointindex(int waypointindex)
    {
        this.waypointindex = waypointindex;
    }

    public int getWaypointWaitTimeSeconds()
    {
        return waypointWaitTimeSeconds;
    }

    public void setWaypointWaitTimeSeconds(int waypointWaitTimeSeconds)
    {
        this.waypointWaitTimeSeconds = waypointWaitTimeSeconds;
    }
    
    public double calculateAngleToWaypoint() throws PWCGException
    {
        return MathUtils.calcAngle(getPosition(), getWaypoint().getPosition());
    }

    public int getWaypointIdentifier()
    {
        if (flight.getWaypointPackage().getAllFlightPoints().size() > waypointindex)
        {
            return flight.getWaypointPackage().getAllFlightPoints().get(waypointindex).getIndex();
        }
        else
        {
            return 0;
        }
    }
    
    public BaseFlightMcu getWaypoint()
    {
        if (flight.getWaypointPackage().getAllFlightPoints().size() > waypointindex)
        {
            return flight.getWaypointPackage().getAllFlightPoints().get(waypointindex);
        }
        else
        {
            return null;
        }
    }

}
