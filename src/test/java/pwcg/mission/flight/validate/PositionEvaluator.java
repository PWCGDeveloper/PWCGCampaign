package pwcg.mission.flight.validate;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class PositionEvaluator
{

    public static void evaluateAiFlight(Mission mission) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        boolean failed = false;
        for (IFlight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            verifyWaypointAltitude(aiFlight);

            if (shouldEvaluateDistanceToTarget(aiFlight))
            {
                double distanceMissioNCenterToTarget = MathUtils.calcDist(missionCenter, aiFlight.getTargetDefinition().getPosition());
                if (distanceMissioNCenterToTarget > 150000)
                {
                    failed = true;
                }
            }

        }

        assert (!failed);
    }

    private static void verifyWaypointAltitude(IFlight aiFlight)
    {
        for (McuWaypoint waypoint : aiFlight.getWaypointPackage().getAllWaypoints())
        {
            assert (waypoint.getPosition().getYPos() > 50.0);
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
