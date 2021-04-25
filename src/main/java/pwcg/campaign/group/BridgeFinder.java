package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;

public class BridgeFinder
{
    private List<Bridge> bridges = new ArrayList<Bridge>();

    public BridgeFinder(List<Bridge> bridges)
    {
        this.bridges = bridges;
    }

    public Bridge findClosestBridge (Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<Bridge> positionFinder = new PositionFinder<Bridge>();
        Bridge closestBridge = positionFinder.selectClosestPosition(bridges, referenceLocation);
        return closestBridge;
    }
    
    public Bridge findClosestBridgeForSide (Side side, Date date, Coordinate referenceLocation) throws PWCGException 
    {
        PositionFinder<Bridge> positionFinder = new PositionFinder<Bridge>();
        Bridge closestBridge = positionFinder.selectClosestPosition(findAllBridgesForSide(side, date), referenceLocation);
        return closestBridge;
    }

    public Bridge findBridgeForSideWithinRadius(Side side, Date date, Coordinate referenceLocation, double radius) throws PWCGException 
    {
        PositionFinder<Bridge> positionFinder = new PositionFinder<Bridge>();
        List<Bridge> bridgesForSide = findAllBridgesForSide(side, date);
        Bridge selectedBridge = positionFinder.selectPositionWithinExpandingRadius(
                bridgesForSide, 
                referenceLocation, 
                radius, 
                radius);
        return selectedBridge;
    }

    public List<Bridge> findBridgesForSideWithinRadius(Side side, Date date, Coordinate targetGeneralLocation, double radius) throws PWCGException 
    {
        PositionFinder<Bridge> positionFinder = new PositionFinder<Bridge>();
        List<Bridge> selectedBridges = positionFinder.findWithinExpandingRadius(findAllBridgesForSide(side, date), targetGeneralLocation, radius, radius);
        return selectedBridges;
    }

    public Bridge findDestinationBridge(Coordinate location, Side side, Date date) throws PWCGException 
    {
        PositionFinder<Bridge> positionFinder = new PositionFinder<Bridge>();
        List<Bridge> bridgesForSide = findAllBridgesForSide(side, date);
        Bridge destinationBridge = positionFinder.selectDestinationPosition(bridgesForSide, location);
        return destinationBridge;
    }

    public List<Bridge> findAllBridgesForSide(Side side, Date date) throws PWCGException 
    {
        List<Bridge>selectedBridges = new ArrayList<Bridge>();
        for (Bridge bridge : bridges)
        {
            if (bridge.determineCountryOnDate(date).getSide() == side)
            {
                selectedBridges.add(bridge);
            }
        }
            
        return selectedBridges;
    }

    public List<Bridge> findAllBridges() 
    {
        ArrayList<Bridge> selectedBridges = new ArrayList<Bridge>();
        try
        {
            selectedBridges.addAll(bridges);
        }
        catch (Exception e)
        {
        }

        return selectedBridges;
    }
}
