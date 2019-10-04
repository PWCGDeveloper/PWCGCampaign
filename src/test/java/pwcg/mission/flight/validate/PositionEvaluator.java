package pwcg.mission.flight.validate;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.mcu.McuWaypoint;

public class PositionEvaluator
{
    
    public static void evaluateAiFlight(Mission mission) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        boolean failed = false;
        for (Flight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (aiFlight.getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK ||
                aiFlight.getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB ||
                aiFlight.getFlightType() == FlightTypes.TRANSPORT)
            {
                continue;
            }
            
            double distanceMissioNCenterToTarget = MathUtils.calcDist(missionCenter, aiFlight.getTargetCoords());
            if (distanceMissioNCenterToTarget > 100000)
            {
                failed = true;
            }
            
            for (Integer planeId : aiFlight.getWaypointPackage().getAllWaypointsSets().keySet())
            {
                for (McuWaypoint waypoint : aiFlight.getWaypointPackage().getWaypointsForPlaneId(planeId))
                {
                    assert(waypoint.getPosition().getYPos() > 50.0);
                }
            }
            
            List<Coordinate> planePositionsEvaluated = new ArrayList<>();
            for (PlaneMCU plane : aiFlight.getPlanes())
            {
                assert(plane.getPosition().getYPos() > 50.0);
                for (Coordinate evaluatedPlanePosition: planePositionsEvaluated)
                {
                    assert (plane.getPosition().equals(evaluatedPlanePosition) == false);
                }
            }
        }
        
        assert(!failed);
    }

}
