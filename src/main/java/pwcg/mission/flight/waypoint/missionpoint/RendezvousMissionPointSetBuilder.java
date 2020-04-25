package pwcg.mission.flight.waypoint.missionpoint;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;

public class RendezvousMissionPointSetBuilder
{
    private IFlight flightThatNeedsEscort;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public RendezvousMissionPointSetBuilder(IFlight escortedFlight)
    {
        this.flightThatNeedsEscort = escortedFlight;
    }

    public IMissionPointSet createFlightRendezvous(McuWaypoint ingressWaypoint) throws PWCGException
    {
        McuWaypoint rendezvousWaypoint = createRendezvousWaypoint(ingressWaypoint);

        missionPointSet.addWaypoint(rendezvousWaypoint);
        return missionPointSet;
    }
    
    private McuWaypoint createRendezvousWaypoint(McuWaypoint ingressWaypoint) throws PWCGException
    {
        Coordinate ingressPosition = ingressWaypoint.getPosition();
        Coordinate airfieldPosition = flightThatNeedsEscort.getFlightHomePosition();
        
        double angleFromIngressToAirfield = MathUtils.calcAngle(ingressPosition, airfieldPosition);
        Coordinate rendezvousWaypointPosition = MathUtils.calcNextCoord(ingressPosition, angleFromIngressToAirfield, 6000);
        rendezvousWaypointPosition.setYPos(ingressPosition.getYPos());

        McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
        rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
        rendezvousWaypoint.setPosition(rendezvousWaypointPosition);
        rendezvousWaypoint.setTargetWaypoint(true);
        rendezvousWaypoint.setSpeed(flightThatNeedsEscort.getFlightCruisingSpeed());
        return rendezvousWaypoint;
    }
    
    
}
