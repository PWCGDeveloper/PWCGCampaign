package pwcg.mission.flight.waypoint.virtual;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;

public class VirtualAdditionalTimeCalculator
{
    public int addDelayForPlayerDelay(Mission mission, IFlight flightToBeDelayed) throws PWCGException
    {
        int additionalTime = 0;
        int averagePlayerIngressTime = calculateAveragePlayerIngressTime(mission);
        if (averagePlayerIngressTime > 0)
        {
            int virtualFlightIngressTime = flightToBeDelayed.getWaypointPackage().secondsUntilWaypoint(WaypointAction.WP_ACTION_INGRESS);
            additionalTime = averagePlayerIngressTime - virtualFlightIngressTime;
            if (additionalTime > 30)
            {
                additionalTime = randomizeAdditionalTIme(additionalTime);
            }
        }
        
        return additionalTime;
    }

    private int randomizeAdditionalTIme(int additionalTime)
    {
        int halfAdditionalTime = additionalTime / 2;
        int randomAdditionalTime = RandomNumberGenerator.getRandom(additionalTime);
        additionalTime = halfAdditionalTime + randomAdditionalTime;
        return additionalTime;
    }

    private int calculateAveragePlayerIngressTime(Mission mission) throws PWCGException
    {
        int averagePlayerIngressTime = 0;
        int numPlayerFlightsWithIngress = 0;
        int totalPlayerIngressTime = 0;
        for (IFlight playerFlight : mission.getMissionFlightBuilder().getPlayerFlights())
        {
            int timeToIngress = playerFlight.getWaypointPackage().secondsUntilWaypoint(WaypointAction.WP_ACTION_INGRESS);
            if (timeToIngress > 0)
            {
                totalPlayerIngressTime += timeToIngress;
                ++numPlayerFlightsWithIngress;
            }
        }
        
        if (numPlayerFlightsWithIngress > 0)
        {
            averagePlayerIngressTime = totalPlayerIngressTime / numPlayerFlightsWithIngress;
        }
        
        return averagePlayerIngressTime;
    }

}
