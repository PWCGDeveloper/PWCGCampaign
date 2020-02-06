package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;

public class EscortedByPlayerRendezvousBuilder
{
    private IFlight escortedFlight;
    private McuWaypoint ingressWaypoint;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public EscortedByPlayerRendezvousBuilder(IFlight escortedFlight, McuWaypoint ingressWaypoint)
    {
        this.escortedFlight = escortedFlight;
        this.ingressWaypoint = ingressWaypoint;
    }

    public IMissionPointSet createFlightRendezvous() throws PWCGException
    {
        McuWaypoint rendezvousWaypoint = createRendezvousWaypoint();
        McuWaypoint airStartWaypoint = AirStartWaypointFactory.createAirStart(escortedFlight, AirStartPattern.AIR_START_NEAR_WAYPOINT, rendezvousWaypoint);

        missionPointSet.addWaypoint(airStartWaypoint);
        missionPointSet.addWaypoint(rendezvousWaypoint);
        return missionPointSet;
    }
    
    private McuWaypoint createRendezvousWaypoint() throws PWCGException
    {
        Coordinate targetPosition = escortedFlight.getFlightInformation().getTargetPosition();
        double angleAwayFromTarget = MathUtils.calcAngle(targetPosition, ingressWaypoint.getPosition());
        Coordinate rendezvousCoordinate = MathUtils.calcNextCoord(ingressWaypoint.getPosition(), angleAwayFromTarget, 7000);
        rendezvousCoordinate.setYPos(escortedFlight.getFlightInformation().getAltitude());

        McuWaypoint rendezvousWaypoint = WaypointFactory.createRendezvousWaypointType();
        rendezvousWaypoint.setTriggerArea(McuWaypoint.COMBAT_AREA);
        rendezvousWaypoint.setPosition(rendezvousCoordinate);
        rendezvousWaypoint.setTargetWaypoint(true);
        return rendezvousWaypoint;
    }
}
