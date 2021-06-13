package pwcg.mission.aaatruck;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class AAATruckTargetFlightFinder
{
    private Mission mission;

    public AAATruckTargetFlightFinder(Mission mission)
    {
        this.mission = mission;
    }

    public IFlight getBestTargetFlight(Side truckSide) throws PWCGException
    {
        List<IFlight> enemyFlights = mission.getMissionFlights().getAiFlightsForSide(truckSide.getOppositeSide());
        IFlight enemyFlight = findEnemyFlightOfType(FlightTypes.GROUND_ATTACK, enemyFlights);
        if (enemyFlight != null)
        {
            return enemyFlight;
        }

        enemyFlight = findEnemyFlightOfType(FlightTypes.LOW_ALT_BOMB, enemyFlights);
        if (enemyFlight != null)
        {
            return enemyFlight;
        }

        enemyFlight = findEnemyFlightOfType(FlightTypes.DIVE_BOMB, enemyFlights);
        if (enemyFlight != null)
        {
            return enemyFlight;
        }

        enemyFlight = findEnemyFlightOfType(FlightTypes.BOMB, enemyFlights);
        if (enemyFlight != null)
        {
            return enemyFlight;
        }

        Collections.shuffle(enemyFlights);
        return enemyFlights.get(0);

    }

    private IFlight findEnemyFlightOfType(FlightTypes flightType, List<IFlight> enemyFlights) throws PWCGException
    {
        for (IFlight enemyFlight : enemyFlights)
        {
            if (enemyFlight.getFlightType() == flightType)
            {
                return enemyFlight;
            }
        }
        return null;
    }
}
