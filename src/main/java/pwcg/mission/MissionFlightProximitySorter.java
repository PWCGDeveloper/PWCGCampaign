package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class MissionFlightProximitySorter
{
    private List<IFlight> axisFlightsByContactDistance = new ArrayList<>();
    private List<IFlight> alliedFlightsByContactDistance = new ArrayList<>();
        
    public void mapEnemyDistanceToPlayerFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight aiFlight : flights)
        {
            if (aiFlight.getSquadron().determineSide() == Side.ALLIED)
            {
                addFlightToListByDistance(alliedFlightsByContactDistance, aiFlight);
            }
            else
            {
                addFlightToListByDistance(axisFlightsByContactDistance, aiFlight);
            }
        }
    }
    
    public List<IFlight> getFlightsByProximity(Side side)
    {
        if (side == Side.ALLIED)
        {
            return new ArrayList<IFlight>(alliedFlightsByContactDistance);
        }
        else
        {
            return new ArrayList<IFlight>(axisFlightsByContactDistance);
        }
    }
    
    private void addFlightToListByDistance(List<IFlight> flightsByContactDistance, IFlight aiFlight)
    {
        int position = 0;
        for (IFlight flightInList : flightsByContactDistance)
        {
            if (aiFlight.getClosestContactWithPlayerDistance() < flightInList.getClosestContactWithPlayerDistance())
            {
                break;
            }
            
            ++position;
        }
        
        flightsByContactDistance.add(position, aiFlight);
    }
}
