package pwcg.mission.flight.offensive;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.context.FrontLinesDirectionFinder;
import pwcg.campaign.context.FrontLinesDirectionFinder.FrontLinesDirection;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class SortedFixedPositions
{
    private List <IFixedPosition> sortedFixedPositionsNS = new ArrayList<IFixedPosition>();
    private List <IFixedPosition> sortedFixedPositionsEW = new ArrayList<IFixedPosition>();

    public void sortPositionsByDirecton(List <IFixedPosition> fixedPositions) throws PWCGException
    {        
        TreeMap<Double, IFixedPosition> sortedMapNS = new TreeMap<Double, IFixedPosition>();
        TreeMap<Double, IFixedPosition> sortedMapEW = new TreeMap<Double, IFixedPosition>();
        
        for (IFixedPosition fixedPosition : fixedPositions)
        {
            sortedMapNS.put(fixedPosition.getPosition().getXPos(), fixedPosition);
            sortedMapEW.put(fixedPosition.getPosition().getZPos(), fixedPosition);
        }
        
        sortedFixedPositionsNS.addAll(sortedMapNS.values());
        sortedFixedPositionsEW.addAll(sortedMapEW.values());
    }
    
    public List <IFixedPosition> getSortedPositionsWithMaxDistanceTTravelled(Campaign campaign, Coordinate targetCoords, double maxDistanceTravelled) throws PWCGException
    {
        Coordinate lastCoord = null;
        double totalDistance = 0.0;
        List <IFixedPosition> sortedPositions = chooseBestSortedSet(campaign, targetCoords);
        List <IFixedPosition> chosenPositions = new ArrayList<>();;
        for (int i = 0; i < sortedPositions.size(); ++i)
        {
            IFixedPosition fixedPosition = sortedPositions.get(i);
            double distance = 0.0;
            if (lastCoord != null)
            {
                distance = MathUtils.calcDist(fixedPosition.getPosition(), lastCoord);
            }

            lastCoord = fixedPosition.getPosition().copy();
            totalDistance += distance;
            chosenPositions.add(fixedPosition);
            if (totalDistance > maxDistanceTravelled)
            {
                break;
            }
        }
        return chosenPositions;
    }

    private List <IFixedPosition> chooseBestSortedSet(Campaign campaign, Coordinate targetCoords) throws PWCGException
    {
        FrontLinesDirection frontLinesDirection = FrontLinesDirectionFinder.findFrontLinesDirection(campaign, targetCoords);
        List <IFixedPosition> sortedPositions = sortedFixedPositionsNS;
        if (frontLinesDirection == FrontLinesDirection.DIRECTION_EW)
        {
            sortedPositions = sortedFixedPositionsEW;
        }
        return sortedPositions;
    }

}
