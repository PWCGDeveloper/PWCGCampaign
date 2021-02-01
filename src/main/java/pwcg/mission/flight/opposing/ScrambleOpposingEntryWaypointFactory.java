package pwcg.mission.flight.opposing;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory;
import pwcg.mission.flight.waypoint.begin.AirStartWaypointFactory.AirStartPattern;
import pwcg.mission.flight.waypoint.begin.IngressWaypointFactory.IngressWaypointPattern;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetFactory;
import pwcg.mission.mcu.McuWaypoint;

public class ScrambleOpposingEntryWaypointFactory
{

    public static McuWaypoint createScrambleEntryWaypoints(IFlight scrambleOpposingFlight) throws PWCGException
    {
        McuWaypoint ingressWaypoint = IngressWaypointFactory.createIngressWaypoint(IngressWaypointPattern.INGRESS_AT_TARGET, scrambleOpposingFlight);
        moveIngressToScrambleOpposeStartPoint(scrambleOpposingFlight, ingressWaypoint);
        
        IMissionPointSet flightActivate = MissionPointSetFactory.createFlightActivate(scrambleOpposingFlight);
        scrambleOpposingFlight.getWaypointPackage().addMissionPointSet(flightActivate);

        IMissionPointSet flightBegin = MissionPointSetFactory.createFlightBegin(scrambleOpposingFlight, flightActivate, AirStartPattern.AIR_START_NEAR_WAYPOINT, ingressWaypoint);
        scrambleOpposingFlight.getWaypointPackage().addMissionPointSet(flightBegin);
        
        return ingressWaypoint;
    }

    private static void moveIngressToScrambleOpposeStartPoint(IFlight scrambleOpposingFlight, McuWaypoint ingressWaypoint) throws PWCGException
    {
        Coordinate basePosition = scrambleOpposingFlight.getFlightHomePosition();
        Coordinate targetPosition = scrambleOpposingFlight.getTargetDefinition().getPosition();
        double angleToBase = MathUtils.calcAngle(targetPosition, basePosition);
        Coordinate scrambleIngressPosition = MathUtils.calcNextCoord(targetPosition, angleToBase, 20000);
        ingressWaypoint.setPosition(scrambleIngressPosition);
    }

}
