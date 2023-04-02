package pwcg.mission.flight.validate;

import org.junit.jupiter.api.Assertions;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class PositionEvaluator
{

    public static void evaluateAiFlight(Mission mission, int maxDisatance) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        boolean failed = false;
        for (IFlight aiFlight : mission.getFlights().getAiFlights())
        {
            verifyWaypointAltitude(aiFlight);

            if (shouldEvaluateDistanceToTarget(aiFlight))
            {
                double distanceMissioNCenterToTarget = MathUtils.calcDist(missionCenter, aiFlight.getTargetDefinition().getPosition());
                if (distanceMissioNCenterToTarget > maxDisatance)
                {
                    failed = true;
                }
            }

        }

        Assertions.assertTrue (!failed);
    }

    private static void verifyWaypointAltitude(IFlight aiFlight)
    {
        for (McuWaypoint waypoint : aiFlight.getWaypointPackage().getAllWaypoints())
        {
            Assertions.assertTrue (waypoint.getPosition().getYPos() > 50.0);
        }
    }

    private static boolean shouldEvaluateDistanceToTarget(IFlight aiFlight)
    {
        if (aiFlight.getFlightType() == FlightTypes.TRANSPORT)
        {
            return false;
        }

        return true;
    }

}
