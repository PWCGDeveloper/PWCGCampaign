package pwcg.mission;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class MissionFighterTargetBuilder
{

    public void buildFighterTargets(Mission mission)
    {
        List<IFlight> alliedFlights = mission.getFlights().getFlightsForSide(Side.ALLIED);
        List<IFlight> axisFlights = mission.getFlights().getFlightsForSide(Side.AXIS);
        
        setFighterTargetsForSide(alliedFlights, axisFlights);
        setFighterTargetsForSide(axisFlights, alliedFlights);
    }

    
    private void setFighterTargetsForSide (List<IFlight> flightsForSide, List<IFlight> enemyFlights)
    {
        for (IFlight flight : flightsForSide)
        {
            if (FlightTypes.isFighterFlight(flight.getFlightType()))
            {
                setEnemiesForFighterFlight(flight, enemyFlights);
            }
        }

    }


    private void setEnemiesForFighterFlight(IFlight flight, List<IFlight> enemyFlights)
    {
        for (IFlight enemyFlight : enemyFlights)
        {
            List<Integer> enemyPlaneIds = enemyFlight.getAllPlanesIdsInFlight();
            flight.setEnemiesForFlight(enemyPlaneIds);
        }
    }
}
