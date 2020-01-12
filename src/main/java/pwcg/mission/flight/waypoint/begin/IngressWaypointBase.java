package pwcg.mission.flight.waypoint.begin;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public abstract class IngressWaypointBase implements IIngressWaypoint
{
    protected Campaign campaign;
    protected IFlight flight;
    protected Coordinate lastPosition;
    protected Coordinate targetPosition;
    protected int waypointSpeed;
    protected int waypointAltitude;
    
    protected final int INGRESS_DISTANCE = 10000;
    
    public IngressWaypointBase(IFlight flight, Coordinate lastPosition, Coordinate targetPosition, int waypointSpeed, int waypointAltitude) throws PWCGException 
    {
        this.campaign = flight.getCampaign();
        this.flight = flight;
        this.lastPosition = lastPosition;
        this.targetPosition = targetPosition;
        this.waypointSpeed = waypointSpeed;
        this.waypointAltitude = waypointAltitude;
    }

    abstract public McuWaypoint createIngressWaypoint() throws PWCGException;
}
