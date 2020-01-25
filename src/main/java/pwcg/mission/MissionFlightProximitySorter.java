package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plot.FlightProximityAnalyzer;

public class MissionFlightProximitySorter
{
    private Map<Double, IFlight> axisFlightsByContactDistance = new TreeMap<>();
    private Map<Double, IFlight> alliedFlightsByContactDistance = new TreeMap<>();
        
    public void mapEnemyDistanceToPlayerFlights(Mission mission) throws PWCGException
    {
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(mission);
        proximityAnalyzer.plotFlightEncounters();
        
        for (IFlight aiFlight : mission.getMissionFlightBuilder().getAiFlights())
        {
            if (aiFlight.getFlightInformation().getSquadron().determineSide() == Side.ALLIED)
            {
                alliedFlightsByContactDistance.put(aiFlight.getFlightPlayerContact().getClosestContactWithPlayerDistance(), aiFlight);
            }
            else
            {
                axisFlightsByContactDistance.put(aiFlight.getFlightPlayerContact().getClosestContactWithPlayerDistance(), aiFlight);
            }
        }
    }
    
    public List<IFlight> getFlightsByProximity(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new ArrayList<IFlight>(alliedFlightsByContactDistance.values());
        }
        else
        {
            return new ArrayList<IFlight>(axisFlightsByContactDistance.values());
        }
    }
}
