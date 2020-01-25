package pwcg.mission.flight.validate;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuWaypoint;

public class PositionEvaluator
{
    
    public static void evaluateAiFlight(Mission mission) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();

        boolean failed = false;
        for (IFlight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (aiFlight.getFlightInformation().getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK ||
                aiFlight.getFlightInformation().getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB ||
                aiFlight.getFlightInformation().getFlightType() == FlightTypes.TRANSPORT)
            {
                continue;
            }
            
            double distanceMissioNCenterToTarget = MathUtils.calcDist(missionCenter, aiFlight.getFlightInformation().getTargetPosition());
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
            for (PlaneMcu plane : aiFlight.getFlightPlanes())
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
