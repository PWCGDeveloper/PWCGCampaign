package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.plot.FlightProximityAnalyzer;

public class MissionFlightProximitySorter
{
    private Map<Double, Flight> axisFlightsByContactDistance = new TreeMap<>();
    private Map<Double, Flight> alliedFlightsByContactDistance = new TreeMap<>();
        
    public void mapEnemyDistanceToPlayerFlights(Mission mission) throws PWCGException
    {
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(mission);
        proximityAnalyzer.plotFlightEncounters();
        
        for (Flight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (aiFlight.getSquadron().determineSide() == Side.ALLIED)
            {
                alliedFlightsByContactDistance.put(aiFlight.getClosestContactWithPlayerDistance(), aiFlight);
            }
            else
            {
                axisFlightsByContactDistance.put(aiFlight.getClosestContactWithPlayerDistance(), aiFlight);
            }
        }
    }
    
    public List<Flight> getFlightsByProximity(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new ArrayList<Flight>(alliedFlightsByContactDistance.values());
        }
        else
        {
            return new ArrayList<Flight>(axisFlightsByContactDistance.values());
        }
    }
}
